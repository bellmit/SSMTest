package cn.gtmap.msurveyplat.promanage.web.utils;

import cn.gtmap.msurveyplat.common.dto.DchyXmglChxmDto;
import cn.gtmap.msurveyplat.common.dto.DchyXmglHtxxDto;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.promanage.core.service.FileUploadHtxxService;
import cn.gtmap.msurveyplat.promanage.core.service.FileUploadService;
import cn.gtmap.msurveyplat.promanage.core.service.XcsldjDtoService;
import cn.gtmap.msurveyplat.promanage.core.service.mq.service.PushDataToMqService;
import cn.gtmap.msurveyplat.promanage.utils.Container;
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

public class FileUploadHtglUtil {
    private static final Log logger = LogFactory.getLog(FileUploadHtglUtil.class);

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
     * 文件上传后，更新合同信息、名录库信息与材料信息
     */
    public static ResponseMessage updateSjxxAndClxx(Map<String, Object> map, MultipartFile[] files) {
        ResponseMessage message = new ResponseMessage();
        DchyXmglChxmDto dchyXmglChxmDto = new DchyXmglChxmDto();
        List<DchyXmglHtxxDto> dchyXmglHtxxDtoList = new ArrayList<>();
        try {
            XcsldjDtoService xcsldjDtoService = (XcsldjDtoService) Container.getBean("xcsldjDtoServiceImpl");
            DchyXmglHtxxDto dchyXmglHtxxDto = xcsldjDtoService.generateHtxxDto(map);
            FileUploadHtxxService fileUploadHtxxService = (FileUploadHtxxService) Container.getBean("fileUploadHtxx");
            boolean resultHt = fileUploadHtxxService.saveHtxx(dchyXmglHtxxDto);
            /*if (resultHt) {
                //文件上传之后同步数据
                dchyXmglHtxxDtoList.add(dchyXmglHtxxDto);
                dchyXmglChxmDto.setDchyXmglHtxxDtoList(dchyXmglHtxxDtoList);
                getBase64FileList(dchyXmglChxmDto, files);
                pushDataToMqService.pushSlxxMsgToMq(dchyXmglChxmDto);
            }*/
            message = ResponseUtil.wrapSuccessResponse();
            message.getData().put("wjzxid", map.get("folderId"));
        } catch (Exception e) {
            logger.error("错误原因{}：", e);
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
        if (files.length > 0 && null != dchyXmglChxmDto && CollectionUtils.isNotEmpty(dchyXmglChxmDto.getDchyXmglHtxxDtoList())) {
            List<Map<String, String>> list = Lists.newArrayList();
            dchyXmglChxmDto.getDchyXmglHtxxDtoList().get(0).setSjclList(list);
            for (MultipartFile file : files) {
                byte[] bytes = file.getBytes();
                String str = Base64.getEncoder().encodeToString(bytes);
                logger.info("*************" + str);
                Map<String, String> clMap = Maps.newHashMap();
                clMap.put(file.getOriginalFilename(), str);
                list.add(clMap);
            }
        }
    }

}
