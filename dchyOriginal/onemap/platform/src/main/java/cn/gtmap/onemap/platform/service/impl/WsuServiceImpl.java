package cn.gtmap.onemap.platform.service.impl;

import com.huawei.wsu.common.Constants;
import com.huawei.wsu.core.WsuPostMethod;
import com.huawei.wsu.service.WsuService;
import com.huawei.wsu.utils.AESEncryptUtil;
import com.huawei.wsu.utils.DigestUtil;
import com.huawei.wsu.utils.MsgUtil;
import com.huawei.wsu.utils.XmlUtil;
import com.huawei.wsu.vo.WsuAuthInfo;
import com.huawei.wsu.vo.WsuUserInfo;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 千里眼平台. wsu
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2016/7/27 19:21
 */
public class WsuServiceImpl extends BaseLogger implements WsuService {

    private String serverAddress;

    private String loginNameAndDomain;

    private String loginName;

    private String loginDomain;

    private String password;

    private String wsuUrl;

    private WsuUserInfo userInfo;

    public WsuServiceImpl(String serverAddress, String loginNameAndDomain, String password) {
        this.serverAddress = serverAddress;
        this.loginNameAndDomain = loginNameAndDomain;
        this.password = password;
        if (StringUtils.contains(loginNameAndDomain, '@')) {
            String[] strings = loginNameAndDomain.split("@");
            if (strings.length > 1) {
                this.loginName = strings[0];
                this.loginDomain = strings[1];
            }
        }
        if (StringUtils.isNotBlank(loginName)) {
            this.wsuUrl = "http://".concat(serverAddress).concat(":").concat(WSU_PORT).concat(WSU_ACTION);
            userInfo = new WsuUserInfo(loginDomain, loginName, AESEncryptUtil.encrypt(password));
        }
    }

    /**
     * 登录千里眼
     */
    @Override
    public void login() {
        MsgUtil.MsgTypes msgType = MsgUtil.MsgTypes.GET_SC_INFO;
        Document reqDoc = XmlUtil.createReqDoc(MsgUtil.getMsgHeader(msgType, MsgUtil.MsgMode.req), "1234");
        XmlUtil.setElementValue(reqDoc.getRootElement().addElement("SC_ID"), loginName); //用户id或用户名
        XmlUtil.setElementValue(reqDoc.getRootElement().addElement("SC_TYPE"), "1");   //用户类型 1：PC客户端 2：手机客户端
        try {
            sendMsgToWsu(reqDoc.asXML());
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    /***
     *  登录平台 
     * @param loginNameAndDomain  username
     * @param password            password
     * @param serverAddress       server
     */
    @Override
    public void login(String loginNameAndDomain, String password, String serverAddress) {
        this.loginNameAndDomain = loginNameAndDomain;
        if (StringUtils.isNotBlank(this.loginNameAndDomain) && StringUtils.contains(this.loginNameAndDomain, "@")) {
            String[] strings = this.loginNameAndDomain.split("@");
            this.loginName = strings[0];
            this.loginDomain = strings[1];
        }
        this.password = password;
        this.serverAddress = serverAddress;
        this.wsuUrl = "http://".concat(serverAddress).concat(":").concat(WSU_PORT).concat(WSU_ACTION);
        userInfo = new WsuUserInfo(loginDomain, loginName, AESEncryptUtil.encrypt(password));
        login();
    }

    /**
     * 获取设备列表
     *
     * @return 设备集合
     */
    @Override
    public List<Map> getDeviceList() {
        MsgUtil.MsgTypes msgType = MsgUtil.MsgTypes.GET_VCU_LIST;
        Document reqDoc = XmlUtil.createReqDoc(MsgUtil.getMsgHeader(msgType, MsgUtil.MsgMode.req), "12345678");
        Element userProfile = reqDoc.getRootElement().addElement("USER_PROFILE");
        XmlUtil.addAttribute(userProfile, "ScId", loginName);
        XmlUtil.addAttribute(userProfile, "StampTime", new SimpleDateFormat("yyyyMMddHHmmsssss").format(new Date()));
        XmlUtil.addAttribute(userProfile, "GetProfileFlag", "1");
        XmlUtil.addAttribute(userProfile, "SCType", "1");
        try {
            String rspXml = sendMsgToWsu(reqDoc.asXML());
            Map map = XmlUtil.getRspResult(rspXml);
            if ("0".equals(MapUtils.getString(map, "code"))) {
                List<Map> vcuList = XmlUtil.getVcuList(rspXml);
                return vcuList;
            } else {
                return new ArrayList<Map>();
            }
        } catch (Exception e) {
            return new ArrayList<Map>();
        }
    }

    /**
     * 设置预置位
     *
     * @param indexCode
     * @param index
     * @param name
     */
    @Override
    public void setPreset(String indexCode, String index, String name) {
        MsgUtil.MsgTypes msgType = MsgUtil.MsgTypes.SET_PRESET_PTZ;
        Document reqDoc = XmlUtil.createReqDoc(MsgUtil.getMsgHeader(msgType, MsgUtil.MsgMode.req), "1234");
        Element presetPtz = reqDoc.getRootElement().addElement("PRESET_PTZ");
        XmlUtil.addAttribute(presetPtz, "CameraId", indexCode);
        XmlUtil.addAttribute(presetPtz, "Index", index);
        XmlUtil.addAttribute(presetPtz, "Name", name);
        try {
            String rspXml = sendMsgToWsu(reqDoc.asXML());
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    /**
     * 删除预置位
     *
     * @param indexCode
     * @param index
     */
    @Override
    public void deletePreset(String indexCode, String index) {
        MsgUtil.MsgTypes msgType = MsgUtil.MsgTypes.DELETE_PRESET_PTZ;
        Document reqDoc = XmlUtil.createReqDoc(MsgUtil.getMsgHeader(msgType, MsgUtil.MsgMode.req), "1234");
        Element presetPtz = reqDoc.getRootElement().addElement("PRESET_PTZ");
        XmlUtil.addAttribute(presetPtz, "CameraId", indexCode);
        XmlUtil.addAttribute(presetPtz, "Index", index);
        try {
            String rspXml = sendMsgToWsu(reqDoc.asXML());
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    /**
     * 查询预设位信息
     *
     * @param cameraId
     * @return
     */
    @Override
    public Map getPtz(String cameraId) {
        MsgUtil.MsgTypes msgType = MsgUtil.MsgTypes.GET_PRESET_PTZ;
        Document reqDoc = XmlUtil.createReqDoc(MsgUtil.getMsgHeader(msgType, MsgUtil.MsgMode.req), "1");
        Element pageInfo = reqDoc.getRootElement().addElement("PAGE_INFO");
        XmlUtil.addAttribute(pageInfo, "BeginIndex", "1");
        Element presetPtz = reqDoc.getRootElement().addElement("PRESET_PTZ");
        XmlUtil.addAttribute(presetPtz, "CameraId", cameraId);
        Map map;
        try {
            String rspXml = sendMsgToWsu(reqDoc.asXML());
            map = XmlUtil.getPtz(rspXml);
            logger.info("查询ptz返回信息-------" + rspXml);
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
        return map;
    }

    /**
     * 返回实时浏览 url
     * @param cameraId
     * @return
     */
    @Override
    public String getRtspUrl(String cameraId) {
        MsgUtil.MsgTypes msgType = MsgUtil.MsgTypes.VCU_LIVE;
        Document reqDoc = XmlUtil.createReqDoc(MsgUtil.getMsgHeader(msgType, MsgUtil.MsgMode.req), "1234");
        Element channel = reqDoc.getRootElement().addElement("CHANNEL");
        XmlUtil.addAttribute(channel, "TranscodingFlag", "false");
        XmlUtil.addAttribute(channel, "CameraId", cameraId);
        // 码流类型(多码流时使用) 1表示主码流 2表示子码流1 3表示字码流2
        // 0 或者无该字段，表示使用StreamType获取URL
        XmlUtil.addAttribute(channel, "StreamID", "0");
        // 所请求的码流类型，默认值为3, 1表示手机码流, 2表示非手机码流, 3表示手机码流优先
        // StreamID=0 或者无StreamID字段，StreamType才有效
        XmlUtil.addAttribute(channel, "StreamType", "2");
        // 所请求的URL类型，默认值为1
        // 1表示 rtsp 格式的URL 2表示 http 格式的URL
        XmlUtil.addAttribute(channel, "UrlType", "1");
        try {
            String rspXml = sendMsgToWsu(reqDoc.asXML());
            Document rspDoc = XmlUtil.parseRspDoc(rspXml);
            Element root = rspDoc.getRootElement();
            Element resultEle = (Element) root.selectSingleNode("RESULT");
            String code = XmlUtil.getAttributeValue(resultEle, "ErrorCode");
            if ("0".equals(code)) {
                // 获取实时浏览 URL
                Element rtspUrl = (Element) root.selectSingleNode("Media/RTSP_URL");
                return rtspUrl.getStringValue();
            } else {
                logger.error("获取实时浏览 url 异常： {}, 错误码: {}", XmlUtil.getAttributeValue(resultEle, "Value"), code);
            }

        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
        return StringUtils.EMPTY;
    }

    /**
     * 设置设备的ptz
     *
     * @param cameraId
     * @param p
     * @param t
     * @param z
     * @return
     */
    @Override
    public String setPtz(String cameraId, double p, double t, int z) {
        MsgUtil.MsgTypes msgType = MsgUtil.MsgTypes.SET_PRESET_PTZ;
        Document reqDoc = XmlUtil.createReqDoc(MsgUtil.getMsgHeader(msgType, MsgUtil.MsgMode.req), "1234");
        Element presetPtz = reqDoc.getRootElement().addElement("PRESET_PTZ");
        XmlUtil.addAttribute(presetPtz, "CameraId", cameraId);
        XmlUtil.addAttribute(presetPtz, "Index", "1");
        XmlUtil.addAttribute(presetPtz, "Name", "set001");
        Element curPtzList = reqDoc.getRootElement().addElement("CUR_PTZ_LIST");
        Element curPtz = curPtzList.addElement("CUR_PTZ");
        XmlUtil.addAttribute(curPtz, "CameraId", cameraId);
        XmlUtil.addAttribute(curPtz, "Pan", String.valueOf(p));
        XmlUtil.addAttribute(curPtz, "Tilt", String.valueOf(t));
        XmlUtil.addAttribute(curPtz, "Zoom", String.valueOf(z));
        try {
            String rspXml = sendMsgToWsu(reqDoc.asXML());
            Map res = XmlUtil.getRspResult(rspXml);
            logger.info("cameraId为{},resxml为{}",cameraId,rspXml);
            return MapUtils.getString(res, "code", StringUtils.EMPTY);
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    /**
     * 发送请求消息到 nvs 平台
     *
     * @param msgStr
     * @return
     * @throws Exception
     */
    private String sendMsgToWsu(String msgStr) throws Exception {
        String rspXml = null;
        if (logger.isInfoEnabled()) {
            logger.info("Enter ServiceUtil.sendMsgToWsu()");
        }
        if (logger.isDebugEnabled()) {
            StringBuffer msgBuf = new StringBuffer();
            msgBuf.append("Params: loggerinUserInfo=[").append(userInfo).append("]");
            msgBuf.append(" sendMsgStr=[").append(msgStr).append("]");
            logger.debug(msgBuf.toString());
        }
        try {
            logger.debug("send the request message:[" + msgStr + "]");
            // 向WSU发送业务处理消息
            String returnXml = sendMsgToServer(msgStr);
            // 若响应不为空则打印返回消息日志
            if (isNotNull(returnXml)) {
                logger.debug("sendMsgToWsu:oper with wsu success," + msgStr + "the response xml is [" + returnXml + "]");
                rspXml = returnXml;
            } else {
                logger.error("sendMsgToWsu:oper with wsu fail,the send message is:[" + returnXml
                        + "],but the response xml is null");
            }
        } catch (Exception e) {
            logger.error("Occur an Exception when send http message to wsu", e);
            throw new RuntimeException(e.getLocalizedMessage());
        } finally {
            if (logger.isDebugEnabled()) {
                StringBuffer sb = new StringBuffer();
                sb.append("Return: rspXml=[").append(rspXml + "] ");
                logger.debug(sb.toString());
            }
            if (logger.isInfoEnabled()) {
                logger.info("Exit ServiceUtil.sendMsgToWsu()");
            }
        }
        return rspXml;
    }

    /**
     * 发送消息至服务器
     *
     * @param sendMsgXml
     * @return
     */
    private String sendMsgToServer(String sendMsgXml) {
        String returnXml;
        try {
            // 到 wsu 进行鉴权处理,返回摘要质询参数 返回请求一响应中的参数nonce
            WsuAuthInfo authInfo = authToWsu(sendMsgXml);
            if ("200".equals(authInfo.getStatusCode())) {
                returnXml = authInfo.getNonce();
            }
            // 若鉴权响应的nonce有值，则进行业务处理消息处理
            else if (!isNull(authInfo.getNonce()) && !CONNTION_EXCEPTION.equals(authInfo.getNonce())) {
                returnXml = sendBusiMsgToWsu(sendMsgXml, authInfo.getNonce());
            }
            // 如果通讯异常返回 1006
            else {
                returnXml = CONNTION_EXCEPTION;
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
        return returnXml;
    }

    /**
     * 发送鉴权请求
     *
     * @param sendMsgXml
     * @return
     */
    private WsuAuthInfo authToWsu(String sendMsgXml) {
        // 响应的AuthInfo鉴权信息
        WsuAuthInfo authInfo = new WsuAuthInfo();
        HttpClient httpClient = null;
        try {
            // 初始化连接对象
            httpClient = new HttpClient();
            // 构造PostMethod对象
            PostMethod method = buildMethod(userInfo, DEFAULT_NONCE);
            // 设置发送的消息体
            method.setRequestBody(sendMsgXml);
            method.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 30000);
            httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(30000);
            // 往WSU发送鉴权挑战信息，获取鉴权响应码，第一次鉴权WSU应返回401鉴权失败响应
            int statusCode = httpClient.executeMethod(method);
            authInfo.setStatusCode(String.valueOf(statusCode));
            if (401 == statusCode) {
                // 获得响应消息中的WWW-Authenticate消息头
                Header header = method.getResponseHeader(REQUEST_HEADER_AUTHORIZATION_KEY_RESP_401);
                String authHeadValue = header.getValue();
                // wsu鉴权返回401时带回的摘要质询参数
                String nonce = getNonceFromAuthHeadValue(authHeadValue);
                authInfo.setNonce(nonce);
                // 记录镜头
                logger.debug("auth to wsu success,the return statusCode is [" + statusCode + "],the nonce is:[" + nonce
                        + "]");
            } else if (200 == statusCode) {
                // 获得返回的结果字符串
                String resultXml = method.getResponseBodyAsString();
                authInfo.setNonce(resultXml);
            } else {
                logger.error("auth to wsu fail,the return statusCode is [" + statusCode + "]");
            }
            // 释放方法连接
            method.releaseConnection();
        } catch (Exception ex) {
            logger.error("Occur an Exception when authToWsu to wsu", ex);
            // 如果通讯异常了，返回1006
            authInfo.setNonce(CONNTION_EXCEPTION);
        } finally {
            httpClient = null;
        }
        return authInfo;
    }

    /***
     * 发送业务消息至wsu
     * @param sendMsgXml
     * @param nonce
     * @return
     */
    private String sendBusiMsgToWsu(String sendMsgXml, String nonce) {
        String resultXml = null;
        HttpClient httpClient = null;
        try {
            httpClient = new HttpClient();
            PostMethod method = buildMethod(userInfo, nonce);
            method.setRequestBody(sendMsgXml);
            int resultCode = httpClient.executeMethod(method);
            method.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 3000);
            httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(3000);
            resultXml = method.getResponseBodyAsString();
            if (200 == resultCode) {
                logger.debug("receive the response status:[" + resultCode + "], xmlMessage:[" + resultXml + "]");
            } else {
                logger.error("receive the response status:[" + resultCode + "], xmlMessage:[" + resultXml + "]");
            }
            // 释放方法连接
            method.releaseConnection();
        } catch (Exception ex) {
            logger.error("Occur an Exception when authToWsu to wsu", ex);
        } finally {
            httpClient = null;
        }
        return resultXml;
    }

    /**
     * 从401鉴权失败的响应消息头的鉴权值中截取nonce值
     *
     * @param authHeadValue 401鉴权失败的响应消息头的鉴权值中
     * @return nonce
     */
    private String getNonceFromAuthHeadValue(String authHeadValue) {
        // 摘要质询参数 返回请求一响应中的参数nonce
        String nonce = "";
        String[] authHeaderArr = authHeadValue.split(",");
        String[] authSubArr = null;
        String authSubName = null;
        String authSubValue = null;
        for (int i = 0; i < authHeaderArr.length; i++) {
            authSubArr = authHeaderArr[i].split("=");
            authSubName = authSubArr[0];
            authSubValue = authSubArr[1];
            if (-1 < authSubName.indexOf("nonce")) {
                nonce = authSubValue;
                break;
            }
        }
        return nonce;
    }

    /**
     * @param authUserInfo
     * @param nonce
     * @return
     */
    private PostMethod buildMethod(WsuUserInfo authUserInfo, String nonce) {
        PostMethod method = null;
        method = new WsuPostMethod(wsuUrl, Constants.UTF_8);
        // 用户名客户端ID或者VcuId
        String username = authUserInfo.getLoginName() + "@" + authUserInfo.getLoginDomain();// "public_users@pu.sz.gd";
        // 告知用户使用哪个域的用户名密码登录。这里如“bj.cmcc.cn”
        String realm = authUserInfo.getLoginDomain();
        // 密码 取出库中已加密登录密码，将其解密后再加密
        String password = AESEncryptUtil.decrypt(authUserInfo.getPassword());

        String response =
                DigestUtil.generateResponse(username,
                        realm,
                        nonce,
                        wsuUrl,
                        AUTHORIZATION_QOP_VALUE,
                        DEFAULT_NC,
                        DEFAULT_CNONCE,
                        password,
                        null);

        StringBuffer authorization = new StringBuffer();
        authorization.append("Digest ");
        authorization.append("username=").append(username).append(",");
        authorization.append("realm=").append(realm).append(",");
        authorization.append("nonce=").append(nonce).append(",");
        authorization.append("uri=").append(wsuUrl).append(",");
        authorization.append("response=").append(response).append(",");
        authorization.append("cnonce=").append(DEFAULT_CNONCE).append(",");
        authorization.append("opaque=").append(DEFAULT_OPAQUE).append(",");
        authorization.append("qop=").append(AUTHORIZATION_QOP_VALUE).append(",");
        authorization.append("nc=").append(DEFAULT_NC);

        // HTTP请求消息头 UTHORIZATIO_VALUE
        String requestHeaderAuthorizationValue = authorization.toString();
        // 设置http请求消息头 AUTHORIZATION
        method.addRequestHeader(REQUEST_HEADER_AUTHORIZATION_KEY, requestHeaderAuthorizationValue);
        buildRequestHeaderValue(method);
        return method;
    }

    /**
     * @param method
     */
    private void buildRequestHeaderValue(PostMethod method) {
        // HTTP请求消息头 HOST_VALUE
        String requestHeaderHostValue = wsuUrl.split("/")[2];
        // HTTP请求消息头 USER_AGENT_VALUE
        String requestHeaderUserAgentValue = "Huawei SGW/V100R002C20";
        // HTTP请求消息头 DATE_VALUE
        String requestHeaderDateValue =
                new SimpleDateFormat("EEE,dd MMM yyyy HH:mm:ss", Locale.US).format(new Date()) + " GMT";
        // HTTP请求消息头 CONTENT_LENGTH_VALUE
        // String requestHeaderContentLengthValue = String.valueOf(reqXml.getBytes().length);
        // HTTP请求消息头 CONNECTION_VALUE
        String requestHeaderConnectionValue = "close";
        // HTTP请求消息头 CONTENT_TYPE_VALUE
        String requestHeaderContentTypeValue = "text/html;charset=utf-8";

        // 设置http请求消息头 HOST
        method.addRequestHeader(REQUEST_HEADER_HOST_KEY, requestHeaderHostValue);
        // 设置http请求消息头 USER_AGENT
        method.addRequestHeader(REQUEST_HEADER_USER_AGENT_KEY, requestHeaderUserAgentValue);
        // 设置http请求消息头 DATE
        method.addRequestHeader(REQUEST_HEADER_DATE_KEY, requestHeaderDateValue);
        // 设置http请求消息头 CONTENT_LENGTH
        // method.addRequestHeader(REQUEST_HEADER_CONTENT_LENGTH_KEY, requestHeaderContentLengthValue);
        // 设置http请求消息头 CONNECTION
        method.addRequestHeader(REQUEST_HEADER_CONNECTION_KEY, requestHeaderConnectionValue);
        // 设置http请求消息头 CONTENT_TYPE
        method.addRequestHeader(REQUEST_HEADER_CONTENT_TYPE_KEY, requestHeaderContentTypeValue);
    }
}
