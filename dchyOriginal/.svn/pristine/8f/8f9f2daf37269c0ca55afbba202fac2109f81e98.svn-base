package cn.gtmap.onemap.platform.event;

/**
 * User: yuhuawen
 * Date: 13-5-13
 * Time: 下午4:09
 */
public class ServiceException extends RuntimeException {

    // 操作服务异常类型
    public static enum ExceptionType {

        INSERT_SERVICE("添加服务异常"),
        DELETE_SERVICE("删除服务异常"),
        UPDATE_SERVICE("更新服务异常"),
        MODIFY_SERVICES("修改所有服务异常");

        private String type;

        ExceptionType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

    private String msg;
    private ExceptionType exceptionType;

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param msg exceptionType
     */
    public ServiceException(ExceptionType exceptionType, String msg) {
        this.msg = msg;
        this.exceptionType = exceptionType;
    }

    /**
     * Returns the detail message string of this throwable.
     *
     * @return the detail message string of this <tt>Throwable</tt> instance
     *         (which may be <tt>null</tt>).
     */
    @Override
    public String getMessage() {
        return this.exceptionType.getType() + "【" + msg + "】";
    }
}
