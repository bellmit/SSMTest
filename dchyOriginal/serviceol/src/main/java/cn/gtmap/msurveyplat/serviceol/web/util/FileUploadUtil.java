package cn.gtmap.msurveyplat.serviceol.web.util;

import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglSjcl;
import cn.gtmap.msurveyplat.common.dto.DchyXmglChxmDto;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.CalendarUtil;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.serviceol.core.service.FileUploadService;
import cn.gtmap.msurveyplat.serviceol.core.service.UploadService;
import cn.gtmap.msurveyplat.serviceol.utils.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.gtis.fileCenter.model.Node;
import com.gtis.fileCenter.service.FileService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static cn.gtmap.msurveyplat.serviceol.utils.PlatformUtil.getFileService;

/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2020/12/4 12:22
 * @description
 */
public class FileUploadUtil {
    private static final Log LOGGER = LogFactory.getLog(FileUploadUtil.class);

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
            LOGGER.error("错误信息:{}", e);
        }
        return map;
    }

    /**
     * 文件上传后，更新名录库信息与材料信息
     */
    public static ResponseMessage updateSjxxAndClxx(Map<String, Object> map) {
        ResponseMessage message = new ResponseMessage();
        FileUploadService fileUploadService = (FileUploadService) Container.getBean("fileUpload");
        String ssmkid = CommonUtil.formatEmptyValue(MapUtils.getString(map, "ssmkid"));
        String sjclid = CommonUtil.formatEmptyValue(MapUtils.getString(map, "sjclid"));
        String sjxxid = CommonUtil.formatEmptyValue(MapUtils.getString(map, "sjxxid"));
        String glsxid = CommonUtil.formatEmptyValue(MapUtils.getString(map, "glsxid"));
        String clmc = CommonUtil.formatEmptyValue(MapUtils.getString(map, "clmc"));
        String fs = CommonUtil.formatEmptyValue(MapUtils.getString(map, "fs"));
        String xh = CommonUtil.formatEmptyValue(MapUtils.getString(map, "xh"));
        String cllx = CommonUtil.formatEmptyValue(MapUtils.getString(map, "cllx"));
        String folderId = CommonUtil.formatEmptyValue(MapUtils.getString(map, "folderId"));
        String parentId = CommonUtil.formatEmptyValue(MapUtils.getString(map, "parentId"));
        String ys = CommonUtil.formatEmptyValue(MapUtils.getString(map, "ys"));
        try {
            DchyXmglSjcl dchyXmglSjcl = fileUploadService.queryDataById(DchyXmglSjcl.class, sjclid);
            if (dchyXmglSjcl == null) {
                dchyXmglSjcl = new DchyXmglSjcl();
                dchyXmglSjcl.setSjclid(sjclid);
            }
            dchyXmglSjcl.setSjxxid(sjxxid);
            dchyXmglSjcl.setWjzxid(folderId);
            dchyXmglSjcl.setClmc(clmc);
            dchyXmglSjcl.setFs(StringUtils.isEmpty(fs) ? Constants.DCHY_XMGL_MRFS : Integer.parseInt(fs));
            dchyXmglSjcl.setYs(StringUtils.isEmpty(ys) ? Constants.DCHY_XMGL_MRYS : Integer.parseInt(ys));
            dchyXmglSjcl.setXh(StringUtils.isEmpty(xh) ? Constants.DCHY_XMGL_MRXH : Integer.parseInt(xh));
            dchyXmglSjcl.setCllx(cllx);
            dchyXmglSjcl.setClrq(CalendarUtil.getCurHMSDate());
            /*更新材料信息*/
            int result = fileUploadService.updateSjclByClass(dchyXmglSjcl, dchyXmglSjcl.getSjclid());
            /*这是一个多实现，用于更新对应的wjzxid*/
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
    public static ResponseMessage updateSjxxAndClxx(Map<String, Object> map, MultipartFile[] files) {
        FileUploadService fileUploadService = (FileUploadService) Container.getBean("fileUpload");
        ResponseMessage message = new ResponseMessage();
        String ssmkid = CommonUtil.formatEmptyValue(MapUtils.getString(map, "ssmkid"));
        String sjclid = CommonUtil.formatEmptyValue(MapUtils.getString(map, "sjclid"));
        String sjxxid = CommonUtil.formatEmptyValue(MapUtils.getString(map, "sjxxid"));
        String glsxid = CommonUtil.formatEmptyValue(MapUtils.getString(map, "glsxid"));
        String clmc = CommonUtil.formatEmptyValue(MapUtils.getString(map, "clmc"));
        String fs = CommonUtil.formatEmptyValue(MapUtils.getString(map, "fs"));
        String xh = CommonUtil.formatEmptyValue(MapUtils.getString(map, "xh"));
        String cllx = CommonUtil.formatEmptyValue(MapUtils.getString(map, "cllx"));
        String folderId = CommonUtil.formatEmptyValue(MapUtils.getString(map, "folderId"));
        String parentId = CommonUtil.formatEmptyValue(MapUtils.getString(map, "parentId"));
        String ys = CommonUtil.formatEmptyValue(MapUtils.getString(map, "ys"));
        String ssclsx = CommonUtil.formatEmptyValue(MapUtils.getString(map, "ssclsx"));
        String htxxid = CommonUtil.formatEmptyValue(MapUtils.getString(map, "htxxid"));
        try {
            DchyXmglSjcl dchyXmglSjcl = fileUploadService.queryDataById(DchyXmglSjcl.class, sjclid);
            if (dchyXmglSjcl == null) {
                dchyXmglSjcl = new DchyXmglSjcl();
                dchyXmglSjcl.setSjclid(sjclid);
            }
            dchyXmglSjcl.setSjxxid(sjxxid);
            dchyXmglSjcl.setClsx(ssclsx);
            dchyXmglSjcl.setWjzxid(folderId);
            dchyXmglSjcl.setClmc(clmc);
            dchyXmglSjcl.setFs(StringUtils.isEmpty(fs) ? Constants.DCHY_XMGL_MRFS : Integer.parseInt(fs));
            dchyXmglSjcl.setYs(StringUtils.isEmpty(ys) ? Constants.DCHY_XMGL_MRYS : Integer.parseInt(ys));
            dchyXmglSjcl.setXh(StringUtils.isEmpty(xh) ? Constants.DCHY_XMGL_MRXH : Integer.parseInt(xh));
            dchyXmglSjcl.setCllx(cllx);
            dchyXmglSjcl.setClrq(new Date());
            /*更新材料信息*/
            int result = fileUploadService.updateSjclByClass(dchyXmglSjcl, dchyXmglSjcl.getSjclid());
            /*这是一个多实现，用于更新对应的wjzxid*/
            UploadService uploadService = DsxYwljUtil.getUploadServiceBySsmkid(ssmkid);
            if (uploadService != null) {
                if(!StringUtils.equals(ssmkid,Constants.DCHY_XMGL_SSMKID_HTBA)){
                    uploadService.updateWjzxid(parentId, glsxid);
                }
                else {
                    uploadService.updateWjzxid(parentId, htxxid);
                }
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
     * 文件删除
     *
     * @param sjclId
     * @param wjzxId
     * @return
     */
    public static ResponseMessage deleteFile(String sjclId, String wjzxId) {
        ResponseMessage message = new ResponseMessage();
        FileUploadService fileUploadService = (FileUploadService) Container.getBean("fileUpload");
        try {
            PlatformUtil platformUtil = (PlatformUtil) Container.getBean("platformUtil");
            platformUtil.deleteNodeById(Integer.parseInt(wjzxId));
            /*根据sjclid删除对应材料记录*/
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
     * @param jsdwFbwtModel
     * @return
     * @description 2020/12/18  添加合同备案信息中base64加密后携带的文件
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    public static void getBase64FileList(DchyXmglChxmDto jsdwFbwtModel, MultipartFile[] files) throws IOException {
        if (files.length > 0 && null != jsdwFbwtModel) {
            List<Map<String, String>> list = Lists.newArrayList();
            jsdwFbwtModel.setSjclList(list);
            for (MultipartFile file : files) {
                byte[] bytes = file.getBytes();
                String str = Base64.getEncoder().encodeToString(bytes);
                Map<String, String> clMap = Maps.newHashMap();
                clMap.put(file.getOriginalFilename(), str);
                list.add(clMap);
            }
        }
    }

}
