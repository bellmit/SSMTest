package cn.gtmap.onemap.platform.event;

/**
 * User: yuhuawen
 * Date: 13-5-13
 */
public class FunctionException extends RuntimeException{

    // 操作功能异常类型
    public static enum ExceptionType{

        INSERT_FUNCTION("添加功能异常"),
        UPDATE_FUNCTION("更新功能异常"),
        GET_FUNCTION("获取功能异常");

        private String type;
        ExceptionType(String type) {
            this.type=type;
        }

        public String getType() {
            return type;
        }
    }
    private String msg;
    private ExceptionType exceptioin;

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param msg
     */
    public FunctionException(ExceptionType exceptioin,String msg) {
        this.exceptioin=exceptioin;
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
        return this.exceptioin.getType()+"【" + msg + "】";
    }
}

