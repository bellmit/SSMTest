package cn.gtmap.onemap.platform.event;

/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-3-27 下午1:48
 */
public class JSONMessageException extends RuntimeException {

    private String msg;

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param msg the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public JSONMessageException(String msg) {
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
        return this.msg;
    }
}
