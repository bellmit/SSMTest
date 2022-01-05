package cn.gtmap.onemap.platform.support.http;

import java.io.Serializable;

/**
 * .
 *
 * @author <a href="mailto:yingxiufeng@gtmap.cn">alex.y</a>
 * @version v1.0, 2017/9/29 (c) Copyright gtmap Corp.
 */
public class MultiPartFileItem implements Serializable {

    private static final long serialVersionUID = -4730052576029223181L;

    /**
     * 表单i field name eg. file
     */
    private String formFieldName;

    /**
     * 上传文件名
     */
    private String fileName;

    /**
     * 文件内容
     */
    private byte[] content;


    public MultiPartFileItem(String formFieldName, String fileName, byte[] content) {
        this.formFieldName = formFieldName;
        this.fileName = fileName;
        this.content = content;
    }

    public String getFormFieldName() {
        return formFieldName;
    }

    public void setFormFieldName(String formFieldName) {
        this.formFieldName = formFieldName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
