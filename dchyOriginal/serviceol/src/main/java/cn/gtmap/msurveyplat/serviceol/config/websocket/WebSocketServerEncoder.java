package cn.gtmap.msurveyplat.serviceol.config.websocket;

import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import com.alibaba.fastjson.JSONObject;

import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class WebSocketServerEncoder implements Encoder.Text<ResponseMessage> {

    /**
     * This method is called with the endpoint configuration object of the
     * endpoint this encoder is intended for when
     * it is about to be brought into service.
     *
     * @param config the endpoint configuration object when being brought into
     *               service
     */
    @Override
    public void init(EndpointConfig config) {

    }

    /**
     * This method is called when the encoder is about to be removed
     * from service in order that any resources the encoder used may
     * be closed gracefully.
     */
    @Override
    public void destroy() {

    }

    /**
     * Encode the given object into a String.
     *
     * @param responseMessage the object being encoded.
     * @return the encoded object as a string.
     */
    @Override
    public String encode(ResponseMessage responseMessage) {
        return JSONObject.toJSONString(responseMessage);
    }
}
