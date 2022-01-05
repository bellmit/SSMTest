package cn.gtmap.onemap.platform.event;

/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-8-6 下午3:49
 */
public class SearchException extends RuntimeException {

    public static enum Type {
        PARSE_QUERY("解析查询语句异常"),
        INDEX_DIR("索引目录异常");

        String label;

        private Type(String value) {
            this.label = value;
        }

        public String getType() {
            return this.label;
        }
    }

    private String msg;
    private Type exceptionType;

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param msg exceptionType
     */
    public SearchException(Type exceptionType, String msg) {
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
