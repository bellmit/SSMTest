package cn.gtmap.msurveyplat.promanage.web.utils;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglChxm;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglSjcl;
import cn.gtmap.msurveyplat.common.dto.DchyXmglChxmDto;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.promanage.core.mapper.DchyXmglDyhzdMapper;
import cn.gtmap.msurveyplat.promanage.core.service.FileUploadService;
import cn.gtmap.msurveyplat.promanage.core.service.UploadService;
import cn.gtmap.msurveyplat.promanage.core.service.mq.service.PushDataToMqService;
import cn.gtmap.msurveyplat.promanage.utils.Container;
import cn.gtmap.msurveyplat.promanage.utils.DsxYwljUtil;
import cn.gtmap.msurveyplat.promanage.utils.PlatformUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.gtis.fileCenter.model.Node;
import com.gtis.fileCenter.service.FileService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static cn.gtmap.msurveyplat.promanage.utils.PlatformUtil.getFileService;

/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2020/12/4 12:22
 * @description
 */
public class FileUploadUtil {
    private static final Log logger = LogFactory.getLog(FileUploadUtil.class);

    private static EntityMapper entityMapper = (EntityMapper) Container.getBean("entityMapper");

    /**
     * 文件上传
     */
    public static Map<String, Object> uploadFile(MultipartFile[] files, String glsxid, String mlmc) {
        HashMap<String, Object> map = Maps.newHashMap();
        try {
            FileService fileService = getFileService();
            PlatformUtil platformUtil = (PlatformUtil) Container.getBean("platformUtil");
            Integer parentId = platformUtil.creatNode(glsxid);
            Integer folderId = platformUtil.createFileFolderByclmc(parentId, mlmc);
            List<Node> nodeList = platformUtil.getChildNodeListByParentId(folderId);
            if (CollectionUtils.isNotEmpty(nodeList)) {
                for (Node node : nodeList) {
                    platformUtil.deleteNodeById(node.getId());
                }
            }
            for (MultipartFile file : files) {
                String fileName = file.getOriginalFilename();
                if (StringUtils.isNotBlank(fileName)) {
                    fileService.uploadFile(file.getInputStream(), folderId, fileName);
                }
            }
            map.put("parentId", parentId + "");
            map.put("folderId", folderId + "");
        } catch (Exception e) {
            logger.error("错误原因{}：", e);
        }
        return map;
    }

    /**
     * 文件上传后，更新名录库信息与材料信息
     */
    public static ResponseMessage updateSjxxAndClxx(Map<String, Object> map, MultipartFile[] files) {
        DchyXmglChxmDto dchyXmglChxmDto = new DchyXmglChxmDto();
        ResponseMessage message = new ResponseMessage();
        String ssmkid = (String) map.get("ssmkid");
        String sjclid = (String) map.get("sjclid");
        String sjxxid = (String) map.get("sjxxid");
        String glsxid = (String) map.get("glsxid");
        String clmc = (String) map.get("clmc");
        String fs = (String) map.get("fs");
        String xh = (String) map.get("xh");
        String cllx = (String) map.get("cllx");
        String folderId = (String) map.get("folderId");
        String parentId = (String) map.get("parentId");
        String ys = (String) map.get("ys");
        String ssclsx = (String) map.get("ssclsx");
        //取出chxm用于同步材料
        if (StringUtils.isNoneBlank(glsxid)) {
            DchyXmglChxm dchyXmglChxm = entityMapper.selectByPrimaryKey(DchyXmglChxm.class, glsxid);
            dchyXmglChxmDto.setDchyXmglChxm(dchyXmglChxm);
        }
        try {
            FileUploadService fileUploadService = (FileUploadService) Container.getBean("fileUpload");
            DchyXmglSjcl dchyXmglSjcl = fileUploadService.querySjclBySjclId(DchyXmglSjcl.class, sjclid);
            if (dchyXmglSjcl == null) {
                dchyXmglSjcl = new DchyXmglSjcl();
                dchyXmglSjcl.setSjclid(sjclid);
            }
            dchyXmglSjcl.setSjxxid(sjxxid);
            dchyXmglSjcl.setClsx(ssclsx);
            dchyXmglSjcl.setWjzxid(folderId);
            dchyXmglSjcl.setClmc(clmc);
            dchyXmglSjcl.setFs(StringUtils.isBlank(fs) || StringUtils.equals("undefined", fs) ? 1 : Integer.parseInt(fs));
            dchyXmglSjcl.setYs(StringUtils.isBlank(ys) || StringUtils.equals("undefined", ys) ? 1 : Integer.parseInt(ys));
            dchyXmglSjcl.setXh(StringUtils.isBlank(xh) || StringUtils.equals("undefined", xh) ? 1 : Integer.parseInt(xh));
            dchyXmglSjcl.setCllx(cllx);
            dchyXmglSjcl.setClrq(new Date());
            /*更新材料信息*/
            int result = fileUploadService.updateSjclByClass(dchyXmglSjcl, dchyXmglSjcl.getSjclid());
            /*if (result > 0) {
                List<DchyXmglSjcl> sjclList = new ArrayList<>();
                sjclList.add(dchyXmglSjcl);
                DchyXmglSjxx dchyXmglSjxx = entityMapper.selectByPrimaryKey(DchyXmglSjxx.class, dchyXmglSjcl.getSjxxid());
                dchyXmglChxmDto.setDchyXmglSjclList(sjclList);
                dchyXmglChxmDto.setDchyXmglSjxx(dchyXmglSjxx);
                getBase64FileList(dchyXmglChxmDto, files);
                pushDataToMqService.pushSlxxMsgToMq(dchyXmglChxmDto);
            }*/

            UploadService uploadService = DsxYwljUtil.getUploadServiceBySsmkid(ssmkid);
            if (uploadService != null) {
                uploadService.updateWjzxid(parentId, glsxid);
            }

            if (result > 0) {
                message = ResponseUtil.wrapSuccessResponse();
                message.getData().put("wjzxid", map.get("folderId"));
            }
        } catch (Exception e) {
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    /**
     * 文件上传后，更新名录库信息与材料信息
     */
    public static ResponseMessage updateSjxxAndClxxByCgcc(Map<String, Object> map) {
        ResponseMessage message = new ResponseMessage();
        String ssmkid = (String) map.get("ssmkid");
        String sjclid = (String) map.get("sjclid");
        String sjxxid = (String) map.get("sjxxid");
        String glsxid = (String) map.get("glsxid");
        String clmc = (String) map.get("clmc");
        String fs = (String) map.get("fs");
        String xh = (String) map.get("xh");
        String cllx = (String) map.get("cllx");
        String folderId = (String) map.get("folderId");
        String parentId = (String) map.get("parentId");
        String ys = (String) map.get("ys");
        try {
            FileUploadService fileUploadService = (FileUploadService) Container.getBean("fileUpload");
            DchyXmglSjcl dchyXmglSjcl = fileUploadService.querySjclBySjclId(DchyXmglSjcl.class, sjclid);
            if (dchyXmglSjcl == null) {
                dchyXmglSjcl = new DchyXmglSjcl();
                dchyXmglSjcl.setSjclid(sjclid);
            }
            dchyXmglSjcl.setSjxxid(sjxxid);
            dchyXmglSjcl.setWjzxid(folderId);
            dchyXmglSjcl.setClmc(clmc);
            dchyXmglSjcl.setFs(StringUtils.isBlank(fs) || StringUtils.equals("undefined", fs) ? 1 : Integer.parseInt(fs));
            dchyXmglSjcl.setYs(StringUtils.isBlank(ys) || StringUtils.equals("undefined", ys) ? 1 : Integer.parseInt(ys));
            dchyXmglSjcl.setXh(StringUtils.isBlank(xh) || StringUtils.equals("undefined", xh) ? 1 : Integer.parseInt(xh));
            dchyXmglSjcl.setCllx(cllx);
            dchyXmglSjcl.setClrq(new Date());
            /*更新材料信息*/
            int result = fileUploadService.updateSjclByClass(dchyXmglSjcl, dchyXmglSjcl.getSjclid());

            if (result > 0) {
                message = ResponseUtil.wrapSuccessResponse();
                message.getData().put("wjzxid", folderId);
            }
        } catch (Exception e) {
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }


    /**
     * 文件删除
     *
     * @param sjclId
     * @param wjzxId
     * @return
     */
    public static ResponseMessage deleteFile(String sjclId, String wjzxId) {
        ResponseMessage message = new ResponseMessage();
        try {
            PlatformUtil platformUtil = (PlatformUtil) Container.getBean("platformUtil");
            platformUtil.deleteNodeById(Integer.parseInt(wjzxId));
            /*根据sjclid删除对应材料记录*/
            FileUploadService fileUploadService = (FileUploadService) Container.getBean("fileUpload");
            int result = fileUploadService.deleteSjclById(sjclId);
            if (result > 0) {
                message = ResponseUtil.wrapSuccessResponse();
                message.getData().put("wjzxid", wjzxId);
            }
        } catch (Exception e) {
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    /**
     * @param dchyXmglChxmDto
     * @return
     * @description 2020/12/18  添加合同备案信息中base64加密后携带的文件
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    public static void getBase64FileList(DchyXmglChxmDto dchyXmglChxmDto, MultipartFile[] files) throws IOException {
        if (files.length > 0 && null != dchyXmglChxmDto && CollectionUtils.isNotEmpty(dchyXmglChxmDto.getDchyXmglSjclList())) {
            DchyXmglSjcl dchyXmglSjcl = dchyXmglChxmDto.getDchyXmglSjclList().get(0);
            Map<String, List<Map<String, String>>> sjclList = Maps.newHashMap();
            dchyXmglChxmDto.setSjcl(sjclList);
            List<Map<String, String>> list = Lists.newArrayList();
            sjclList.put(dchyXmglSjcl.getSjclid(), list);
            for (MultipartFile file : files) {
                String str = Base64.getEncoder().encodeToString(file.getBytes());
                Map<String, String> clMap = Maps.newHashMap();
                clMap.put(file.getOriginalFilename(), str);
                list.add(clMap);
            }
        }
    }
}
