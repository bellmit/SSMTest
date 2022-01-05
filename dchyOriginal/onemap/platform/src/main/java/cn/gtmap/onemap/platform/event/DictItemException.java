package cn.gtmap.onemap.platform.event;

/**
 * User: yuhuawen
 * Date: 13-5-20
 * Time: 下午4:04
 */
public class DictItemException extends RuntimeException{

    // 操作字典项项异常类型
    public static enum ExceptionType {

        INSERT_ITEM("添加字典项异常"),
        DELETE_ITEM("删除字典项异常"),
        UPDATE_ITEM("更新字典项异常"),
        GET_ITEM("获取字典项异常"),
        MODIFY_ITEMS("修改所有字典项异常");

        private String type;

        ExceptionType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }
    
    private String itemName;
    private ExceptionType exceptionType;
    private String msg;

    /**
     * Constructs a new runtime exception with <code>null</code> as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public DictItemException(ExceptionType exceptionType, String itemName, String msg) {
        this.itemName = itemName;
        this.exceptionType=exceptionType;
        this.msg=msg;
    }

    /**
     * Returns the detail message string of this throwable.
     *
     * @return the detail message string of this <tt>Throwable</tt> instance
     *         (which may be <tt>null</tt>).
     */
    @Override
    public String getMessage() {
        return this.exceptionType.getType()+"【"+itemName+"】，异常原因【"+msg+"】";
    }
}

