package cn.gtmap.onemap.platform.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import java.util.Locale;

/**
 * User: yuhuawen
 * Date: 13-5-8
 */
public class TemplateException extends RuntimeException {

    // 操作模板异常类型
    public static enum Type {

        TPL_EXIST("已存在"),
        TPL_NOT_EXIST("不存在"),
        TPL_DELETE_ERROR("删除异常"),
        TPL_READ_ERROR("读取异常"),
        TPL_WRITE_ERROR("写入异常"),
        TPL_CREATE_ERROR("创建异常"),
        FOLDER_LIST_ERROR("下获取文件列表名称异常");

        private String type;

        Type(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

    private String tplName;
    private Type type;
    private String msg;

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param tplName exceptionType  msg
     */
    public TemplateException(String tplName, Type type, String msg) {
        this.tplName = tplName;
        this.type = type;
        this.msg = msg;
    }

    /**
     * Returns the detail message string of this throwable.
     *
     * @return the detail message string of this <tt>Throwable</tt> instance
     *         (which may be <tt>null</tt>).
     */
    @Override
    public String getMessage() {
        return "模板『" + tplName + "』" + type.type + "，异常原因『" + msg + "』";
    }


}
