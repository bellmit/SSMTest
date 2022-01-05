package cn.gtmap.msurveyplat.common.dto;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2021/3/14
 * @description TODO
 */
public class DchyXmglCgJcDto {

    String clcgFiles;

    String uploadFileName;

    String glsxid;

    //线下成果检查的返回值
    ResponseMessage message;

    public String getClcgFiles() {
        return clcgFiles;
    }

    public void setClcgFiles(String clcgFiles) {
        this.clcgFiles = clcgFiles;
    }

    public String getUploadFileName() {
        return uploadFileName;
    }

    public void setUploadFileName(String uploadFileName) {
        this.uploadFileName = uploadFileName;
    }

    public String getGlsxid() {
        return glsxid;
    }

    public void setGlsxid(String glsxid) {
        this.glsxid = glsxid;
    }

    public ResponseMessage getMessage() {
        return message;
    }

    public void setMessage(ResponseMessage message) {
        this.message = message;
    }
}
