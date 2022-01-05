package cn.gtmap.msurveyplat.portalol.model.token;

/**
 * @author <a href="mailto:xuchao@gtmap.cn">xuchao</a>
 * @version 1.0, 2017/1/20
 * @description 接口请求头对象
 */
public class ResponseHeadEntity{

    //响应代码
    private String returncode;
    //响应信息
    private String msg;
    //总页数
    private Integer total;
    //总记录数
    private Integer records;
    //page
    private Integer page;
    //每页记录数
    private Integer pageSize;

    public String getReturncode() {
        return returncode;
    }

    public void setReturncode(String returncode) {
        this.returncode = returncode;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getRecords() {
        return records;
    }

    public void setRecords(Integer records) {
        this.records = records;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
