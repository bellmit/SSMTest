package cn.gtmap.msurveyplat.server.service.ywxx;

import cn.gtmap.msurveyplat.common.util.HttpClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

/**
 *  @author: <a href="mailto:huming@gtmap.cn">huming</a>
 *  @version: 1.0, 2020/12/15
 *  @Description: 异步接口调用 测量事项状态
 */
@Service
public class AlterClsxztService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Async
    public Future alterClsxzt(Map head, Map data, String url){
        String response = "";
        try {
            response = invokeAlter(head,data,url);
        }catch (Exception e){
            LOGGER.error(e.getMessage(),e);
        }
        return new AsyncResult(response);
    }

    /**
     * @author: <a href="mailto:huming@gtmap.cn">huming</a>
     * @description 接口调用
     */
    private String invokeAlter(Map head, Map data, String url) throws IOException {
        Map param = new HashMap();
        param.put("head",head);
        param.put("data",data);
        String response = HttpClientUtil.sendHttpClientJson("",url,param);
        return response;
    }

}
