package cn.gtmap.msurveyplat.server.util;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.io.Serializable;

@ApiModel(
        description = "返回结果"
)

/**
 * 统一返回包装类
 *
 * @author LHW
 */
public class R<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("返回码：成功=1，失败=0")
    private int code;

    @ApiModelProperty("返回文本信息")
    private String msg;

    @ApiModelProperty("业务数据")
    private T data;

    public R() {
        this.code = CommonConstants.SUCCESS;
        this.msg = "success";
    }

    public R(T data) {
        this.code = CommonConstants.SUCCESS;
        this.msg = "success";
        this.data = data;
    }

    public R(T data, String msg) {
        this.code = CommonConstants.SUCCESS;
        this.msg = "success";
        this.data = data;
        this.msg = msg;
    }

    public R(Throwable e) {
        this.code = CommonConstants.SUCCESS;
        this.msg = "success";
        this.msg = e.getMessage();
        this.code = CommonConstants.FAIL;
    }

    public static <T> R<T> ok(T data) {
        return R.<T>builder().code(CommonConstants.SUCCESS).data(data).build();

    }

    public static R ok() {
        return ok((Object)null);
    }

    public static R fail(String msg) {
        return builder().code(CommonConstants.FAIL).msg(msg).build();
    }

    public static R fail(int code) {
        return builder().code(code).build();
    }

    public static R fail(int code, String msg) {
        return builder().code(code).msg(msg).build();
    }

    public static <T> R fail(int code, String msg, T data) {
        return builder().code(code).msg(msg).data(data).build();
    }

    public static R fail(BindingResult bindingResult) {
        FieldError fieldError = bindingResult.getFieldError();

        assert fieldError != null;

        return fail(CommonConstants.FAIL, fieldError.getField() + fieldError.getDefaultMessage());
    }

    public static <T> R.RBuilder<T> builder() {
        return new R.RBuilder();
    }

    public String toString() {
        return "R(code=" + this.getCode() + ", msg=" + this.getMsg() + ", data=" + this.getData() + ")";
    }

    public R(int code, String msg, T data) {
        this.code = CommonConstants.SUCCESS;
        this.msg = "success";
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public int getCode() {
        return this.code;
    }

    public R<T> setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return this.msg;
    }

    public R<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public T getData() {
        return this.data;
    }

    public R<T> setData(T data) {
        this.data = data;
        return this;
    }

    public static class RBuilder<T> {
        private int code;
        private String msg;
        private T data;

        RBuilder() {
        }

        public R.RBuilder<T> code(int code) {
            this.code = code;
            return this;
        }

        public R.RBuilder<T> msg(String msg) {
            this.msg = msg;
            return this;
        }

        public R.RBuilder<T> data(T data) {
            this.data = data;
            return this;
        }

        public R<T> build() {
            return new R(this.code, this.msg, this.data);
        }

        public String toString() {
            return "R.RBuilder(code=" + this.code + ", msg=" + this.msg + ", data=" + this.data + ")";
        }
    }

}
