package cn.gtmap.msurveyplat.exchange.core.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
 * @version 2020/3/7
 * @description
 */
public class MessageDto  implements Serializable {
    private String id;
    private String clientId;
    private String msgCode;
    private String msgType;
    private String msgTypeName;
    private String msgTitle;
    private String msgContent;
    private int read;
    private String producer;
    private String producerType;
    private String recUsername;
    private String recUserAlias;
    private String options;
    private Date optStartTime;
    private Date optEndTime;
    private Date optTime;
    private String notifyType;
    private String httpNotifyUrl;
    private String httpLoadType;

    public MessageDto() {
    }

    public String getId() {
        return this.id;
    }

    public MessageDto setId(String id) {
        this.id = id;
        return this;
    }

    public String getClientId() {
        return this.clientId;
    }

    public MessageDto setClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public String getMsgCode() {
        return this.msgCode;
    }

    public MessageDto setMsgCode(String msgCode) {
        this.msgCode = msgCode;
        return this;
    }

    public String getMsgType() {
        return this.msgType;
    }

    public MessageDto setMsgType(String msgType) {
        this.msgType = msgType;
        return this;
    }

    public String getMsgTypeName() {
        return this.msgTypeName;
    }

    public MessageDto setMsgTypeName(String msgTypeName) {
        this.msgTypeName = msgTypeName;
        return this;
    }

    public String getMsgTitle() {
        return this.msgTitle;
    }

    public MessageDto setMsgTitle(String msgTitle) {
        this.msgTitle = msgTitle;
        return this;
    }

    public String getMsgContent() {
        return this.msgContent;
    }

    public MessageDto setMsgContent(String msgContent) {
        this.msgContent = msgContent;
        return this;
    }

    public int getRead() {
        return this.read;
    }

    public MessageDto setRead(int read) {
        this.read = read;
        return this;
    }

    public String getProducer() {
        return this.producer;
    }

    public MessageDto setProducer(String producer) {
        this.producer = producer;
        return this;
    }

    public String getProducerType() {
        return this.producerType;
    }

    public MessageDto setProducerType(String producerType) {
        this.producerType = producerType;
        return this;
    }

    public String getRecUsername() {
        return this.recUsername;
    }

    public MessageDto setRecUsername(String recUsername) {
        this.recUsername = recUsername;
        return this;
    }

    public String getOptions() {
        return this.options;
    }

    public MessageDto setOptions(String options) {
        this.options = options;
        return this;
    }

    public String getRecUserAlias() {
        return this.recUserAlias;
    }

    public MessageDto setRecUserAlias(String recUserAlias) {
        this.recUserAlias = recUserAlias;
        return this;
    }

    public Date getOptStartTime() {
        return this.optStartTime;
    }

    public MessageDto setOptStartTime(Date optStartTime) {
        this.optStartTime = optStartTime;
        return this;
    }

    public Date getOptEndTime() {
        return this.optEndTime;
    }

    public MessageDto setOptEndTime(Date optEndTime) {
        this.optEndTime = optEndTime;
        return this;
    }

    public Date getOptTime() {
        return this.optTime;
    }

    public MessageDto setOptTime(Date optTime) {
        this.optTime = optTime;
        return this;
    }

    public String getNotifyType() {
        return this.notifyType;
    }

    public MessageDto setNotifyType(String notifyType) {
        this.notifyType = notifyType;
        return this;
    }

    public String getHttpNotifyUrl() {
        return this.httpNotifyUrl;
    }

    public MessageDto setHttpNotifyUrl(String httpNotifyUrl) {
        this.httpNotifyUrl = httpNotifyUrl;
        return this;
    }

    public String getHttpLoadType() {
        return this.httpLoadType;
    }

    public MessageDto setHttpLoadType(String httpLoadType) {
        this.httpLoadType = httpLoadType;
        return this;
    }
}
