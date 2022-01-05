package cn.gtmap.onemap.platform.event;

import com.esri.sde.sdk.client.SeError;

/**
 * .GIS 操作异常
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-1-10 下午2:03
 */
public class GISDaoException extends RuntimeException {

    public static enum Type {
        ARC_SDE, ORACLE_SPATIAL
    }

    public static enum Method {
        QUERY, INSERT, UPDATE, DELETE, INTERSECT, DS_CONNECTION, GET_LAYER, SPATIAL_QUERY, SDE_VERSION, SDE_STATE,
        GET_SDE_SPATIAL_REFERENCE, GET_TABLE_COLUMNS, DS_RETURN,DETECT_LAYER
    }

    private Type type;
    private Method method;
    private String msg;


    /**
     * Constructs a new runtime exception with <code>null</code> as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public GISDaoException(Method method, String message, Type type) {
        this.type = type;
        this.method = method;
        this.msg = message;
    }

    /**
     * Constructs a new runtime exception with <code>null</code> as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public GISDaoException(Method method, SeError error) {
        this.type = Type.ARC_SDE;
        this.method = method;
        this.msg = formateSeError(error);
    }

    /**
     * 格式化SeError
     *
     * @param error
     * @return
     */
    public static String formateSeError(SeError error) {
        if (error == null) return "";
        return "\nArcSDE Error \n" + " ArcSDE Error Number \t: " + error.getSdeError() +
                "\n Error Description \t: " + error.getErrDesc() +
                "\n Ext Error Number \t: " + error.getExtError() +
                "\n Message Detail \t: " + error.getSdeErrMsg() +
                "\n Message Detail \t:  " + error.getExtErrMsg();
    }

    /**
     * Returns the detail message string of this throwable.
     *
     * @return the detail message string of this <tt>Throwable</tt> instance
     *         (which may be <tt>null</tt>).
     */
    @Override
    public String getMessage() {
        return type.name() + "操作【" + method.name() + "】异常，异常原因【" + msg + "】";
    }
}
