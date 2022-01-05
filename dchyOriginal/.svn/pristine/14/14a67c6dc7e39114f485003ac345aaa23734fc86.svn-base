package cn.gtmap.onemap.platform.event;

/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 12-11-1 上午11:33
 */
public class DictException extends RuntimeException {

    // 操作字典异常类型
    public static enum ExceptionType {

        INSERT_DICT("添加字典异常"),
        DELETE_DICT("删除字典异常"),
        UPDATE_DICT("更新字典异常"),
        DICT_NOT_FOUND("字典未找到"),
        GET_DICT("获取字典异常"),
        MODIFY_DICTS("修改所有字典异常");

        private String type;

        ExceptionType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

    private String dictName;
    private ExceptionType exceptionType;
    private String msg;

    /**
     * Constructs a new runtime exception with <code>null</code> as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public DictException(ExceptionType exceptionType, String dictName, String msg) {
        this.dictName = dictName;
        this.exceptionType = exceptionType;
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
        return this.exceptionType.getType() + "【" + dictName + "】，异常原因【" + msg + "】";
    }
}
