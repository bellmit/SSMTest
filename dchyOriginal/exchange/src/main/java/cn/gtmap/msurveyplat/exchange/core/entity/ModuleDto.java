package cn.gtmap.msurveyplat.exchange.core.entity;

import java.io.Serializable;

/**
 * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
 * @version 2020/3/7
 * @description 模块管理 DTO
 */
public class ModuleDto  implements Serializable {
    /*
     *  主键
     * */
    private String id;

    /*
     *  是否禁用， ztree 显示使用
     * */
    private int enabled = 1;

    /*
     *  模块编码
     * */
    private String code;

    /*
     *  名称
     * */
    private String name;

    /*
     *  描述
     * */
    private String description;

    /*
     *  类型编码
     * */
    private String type;

    /*
     *  类型名称
     * */
    private String typeName;

    /*
     *  url
     * */
    private String url;

    /*
     *  请求方法类型
     * */
    private String method;

    /*
     *  应用id
     * */
    private String clientId;

    /*
     *  排序编号
     * */
    private Integer sequenceNumber;

    /*
     *  上一级模块主键
     * */
    private String parentId;

    /*
     *  上一级模块名称
     * */
    private String parentName;

    /*
     *  是否拥有子集， 根据ztree 定义
     * */
    private boolean isParent;

    /*
     *  设置节点的 checkbox / radio 是否禁用， 根据ztree 定义
     * */
    private boolean chkDisabled;

    /*
     *  节点的 checkBox / radio 的 勾选状态， 根据ztree 定义
     * */
    private boolean checked;

    /**
     * 图标*/
    private String icon;

    /**
     * 是否在浏览器新打开页面
     */
    private boolean popupWindow;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public boolean getIsParent() {
        return isParent;
    }

    public void setIsParent(boolean isParent) {
        this.isParent = isParent;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public boolean getChkDisabled() {
        return chkDisabled;
    }

    public void setChkDisabled(boolean chkDisabled) {
        this.chkDisabled = chkDisabled;
    }

    public boolean getChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public boolean isPopupWindow() {
        return popupWindow;
    }

    public void setPopupWindow(boolean popupWindow) {
        this.popupWindow = popupWindow;
    }
}
