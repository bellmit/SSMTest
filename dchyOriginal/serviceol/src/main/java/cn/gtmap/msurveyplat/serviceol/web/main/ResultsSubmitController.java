package cn.gtmap.msurveyplat.serviceol.web.main;

import cn.gtmap.msurveyplat.common.annotion.CheckInterfaceAuth;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.serviceol.service.ResultsSubmitService;
import com.google.common.collect.Maps;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2021/1/8
 * @description 成果提交
 */
@RestController
@RequestMapping("/submit")
public class ResultsSubmitController {
    protected final Log logger = LogFactory.getLog(ResultsSubmitController.class);

    @Autowired
    private ResultsSubmitService resultsSubmitService;

    /**
     * @param
     * @return
     * @description 2021/1/25 初始化生成一条sqxxid
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping(value = "/initSqxx")
    @CheckInterfaceAuth
    public ResponseMessage initSqxx() {
        ResponseMessage message = new ResponseMessage();
        try {
            message = resultsSubmitService.initSqxx();
        } catch (Exception e) {
            logger.error("错误原因:{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    /**
     * @param param
     * @return
     * @description 2021/1/28  展示成果信息
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping(value = "/initCgtj")
    public ResponseMessage initCgtj(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        if (null != param && param.containsKey("data")) {
            Map<String, Object> data = MapUtils.getMap(param, "data");
            String sqxxid = CommonUtil.formatEmptyValue(MapUtils.getString(data, "sqxxid"));
            try {
                message = resultsSubmitService.initCgtj(sqxxid);
            } catch (Exception e) {
                logger.error("错误原因:{}", e);
                message = ResponseUtil.wrapExceptionResponse(e);
            }
        } else {
            message = ResponseUtil.wrapParamEmptyFailResponse();
        }
        return message;
    }

    /**
     * @param zipFile
     * @param request
     * @return
     * @description 2021/3/13
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping(value = "/checkZipFiles")
    @CheckInterfaceAuth
    public ResponseMessage checkZipFiles(@RequestParam("files") MultipartFile zipFile, HttpServletRequest request) {
        ResponseMessage message = new ResponseMessage();
        String sqxxid = CommonUtil.ternaryOperator(request.getParameter("sqxxid"));
        if (StringUtils.isNotBlank(sqxxid)) {
            // 获取文件名
            String fileName = formatFileName(zipFile.getOriginalFilename());
            String slbh = fileName.substring(0, fileName.indexOf("."));

            try {
                boolean flag = resultsSubmitService.isMajorProject(slbh);
                if (flag) {
                    message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.RESULT_SUBMIT_IS_MAJOR_PROJECT.getMsg(), ResponseMessage.CODE.RESULT_SUBMIT_IS_MAJOR_PROJECT.getCode());
                } else {
                    message = resultsSubmitService.checkZipFiles(fileName, zipFile, sqxxid);
                }
            } catch (Exception e) {
                logger.error("错误原因:{}", e);
                message = ResponseUtil.wrapExceptionResponse(e);
            }
        } else {
            message = ResponseUtil.wrapParamEmptyFailResponse();
        }
        return message;
    }

    /**
     * @param
     * @return
     * @description 2021/1/11 成果提交
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping("/zipUpload")
    @CheckInterfaceAuth
    public ResponseMessage zipUpload(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        if (null != param && param.containsKey("data")) {
            Map<String, Object> data = MapUtils.getMap(param, "data");
            String glsxid = CommonUtil.formatEmptyValue(MapUtils.getString(data, "sqxxid"));
            List<Map<String, String>> errorInfoModels = (List<Map<String, String>>) data.get("errorInfoModels");
            try {
                message = resultsSubmitService.zipUpload(glsxid, errorInfoModels);
            } catch (Exception e) {
                logger.error("错误原因:{}", e);
                message = ResponseUtil.wrapExceptionResponse(e);
            }
        } else {
            message = ResponseUtil.wrapParamEmptyFailResponse();
        }
        return message;
    }

    //坑爹的ie会直接解析出zip文件的路径而非文件名所以需要处理一下路径
    public static String formatFileName(String fileName) {
        //判断是否为IE浏览器的文件名，IE浏览器下文件名会带有盘符信息
        // Check for Unix-style path
        int unixSep = fileName.lastIndexOf('/');
        // Check for Windows-style path
        int winSep = fileName.lastIndexOf('\\');
        // Cut off at latest possible point
        int pos = (winSep > unixSep ? winSep : unixSep);
        if (pos != -1) {
            // Any sort of path separator found...
            fileName = fileName.substring(pos + 1);
        }
        return fileName;
    }
}
