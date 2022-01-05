package cn.gtmap.onemap.platform.event;

/**
 * User: yuhuawen
 * Date: 13-5-13
 * Time: 下午4:03
 */
public class ConfigException extends RuntimeException {

    // 模板配置操作异常 类型
    public static enum ExceptionType{

        REQUEST_MAP_CONFIG("获取地图配置异常"),
        SAVE_SERVICE_LEGEND_CONFIG("保存服务图例配置异常");

        private String type;

        ExceptionType(String type) {
            this.type=type;
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
     * @param msg
     */
    public ConfigException(ExceptionType exceptionType,String msg) {
        this.exceptionType=exceptionType;
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
        return this.exceptionType.getType()+"," + msg;
    }
}
