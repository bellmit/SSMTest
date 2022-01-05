package cn.gtmap.msurveyplat.portalol.utils;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglSjcl;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.portalol.core.service.FileUploadService;
import cn.gtmap.msurveyplat.portalol.core.service.UploadService;
import com.google.common.collect.Maps;
import com.gtis.fileCenter.model.Node;
import com.gtis.fileCenter.service.FileService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2020/12/5
 * @description TODO
 */
public class FileUploadUtil {

    private static final Log logger = LogFactory.getLog(FileUploadUtil.class);

    private static PlatformUtil platformUtil = (PlatformUtil) Container.getBean("platformUtil");
    private static FileUploadService fileUploadService = (FileUploadService) Container.getBean("fileUpload");

    @Autowired
    EntityMapper entityMapper;

    public static EntityMapper entityMapperStatic;

    @PostConstruct
    public void setEntityMapper(){
        this.entityMapperStatic = this.entityMapper;
    }



    /**
     * 文件上传
     */
    public static Map<String, Object> uploadFile(MultipartFile[] files, String glsxid, String mlmc) {
        HashMap<String, Object> map = Maps.newHashMap();
        try {
            FileService fileService = PlatformUtil.getFileService();
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
            logger.error("错误信息:{}", e);
        }
        return map;
    }

    /**
     * 文件上传后，更新名录库信息与材料信息
     */
    public static ResponseMessage updateSjxxAndClxx(Map<String, Object> map) {
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
            DchyXmglSjcl dchyXmglSjcl = fileUploadService.querySjclBySjclId(DchyXmglSjcl.class, sjclid);
            if (dchyXmglSjcl == null) {
                dchyXmglSjcl = new DchyXmglSjcl();
            }
            dchyXmglSjcl.setSjclid(sjclid);
            dchyXmglSjcl.setSjxxid(sjxxid);
            dchyXmglSjcl.setWjzxid(folderId);
            dchyXmglSjcl.setClmc(clmc);
            dchyXmglSjcl.setFs(Integer.parseInt(StringUtils.isBlank(fs) ? Constants.DCHY_XMGL_SCCL_MRFS : fs));
            dchyXmglSjcl.setYs(Integer.parseInt(StringUtils.isBlank(ys) ? Constants.DCHY_XMGL_SCCL_MRFS : ys));
            if (StringUtils.isNotBlank(xh)){
                dchyXmglSjcl.setXh(Integer.parseInt(xh));
            }
            dchyXmglSjcl.setCllx(cllx);
            dchyXmglSjcl.setClrq(new Date());
            /*更新材料信息*/
            int result = fileUploadService.updateSjclByClass(dchyXmglSjcl, dchyXmglSjcl.getSjclid());


            UploadService uploadService = DsxYwljUtil.getUploadServiceBySsmkid(ssmkid);
            if (uploadService != null) {
                uploadService.updateWjzxid(parentId, glsxid);
            }
            if (result > 0) {
                message.getHead().setMsg(ResponseMessage.CODE.SUCCESS.getMsg());
                message.getHead().setCode(ResponseMessage.CODE.SUCCESS.getCode());
                message.getData().put("wjzxid", map.get("folderId"));
            }
        } catch (Exception e) {
            logger.error("错误信息:{}", e);
            message.getHead().setMsg(ResponseMessage.CODE.UPLOAD_FAIL.getMsg());
            message.getHead().setCode(ResponseMessage.CODE.UPLOAD_FAIL.getCode());
        }
        return message;
    }
}
