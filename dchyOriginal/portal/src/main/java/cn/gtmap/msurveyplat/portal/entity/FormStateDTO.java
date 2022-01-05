package cn.gtmap.msurveyplat.portal.entity;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
 * @version 2020/3/4
 * @description 用户表信息接口
 */
public class FormStateDTO {
    @ApiModelProperty("表单状态id")
    private String formStateId;
    @ApiModelProperty("表单状态名称")
    private String formStateName;
    @ApiModelProperty("表单状态描述")
    private String formStateDesc;
    @ApiModelProperty("表单状态内容")
    private String formStateContent;
    @ApiModelProperty("表单状态源代码")
    private String formStateSource;
    @ApiModelProperty("表单模型id")
    private String formModelId;
    @ApiModelProperty("表单类型")
    private String formType;
    @ApiModelProperty("外接表单地址")
    private String thirdPath;
    @ApiModelProperty("表单状态顺序号")
    private String relOrder;
    @ApiModelProperty("校验必填项sql")
    private String validSql;
    private List roleRecords;
    private List operations;
//    private List<FormElementConfigDTO> formElementConfigs;
//    private List<ValidSqlDetailDTO> validSqlDetailList;
    private boolean show = true;
    private String splitScreen;

    public FormStateDTO() {
    }

    public String getFormStateId() {
        return this.formStateId;
    }

    public void setFormStateId(String formStateId) {
        this.formStateId = formStateId;
    }

    public String getFormStateName() {
        return this.formStateName;
    }

    public void setFormStateName(String formStateName) {
        this.formStateName = formStateName;
    }

    public String getFormStateDesc() {
        return this.formStateDesc;
    }

    public void setFormStateDesc(String formStateDesc) {
        this.formStateDesc = formStateDesc;
    }

    public String getFormStateContent() {
        return this.formStateContent;
    }

    public void setFormStateContent(String formStateContent) {
        this.formStateContent = formStateContent;
    }

    public String getFormStateSource() {
        return this.formStateSource;
    }

    public void setFormStateSource(String formStateSource) {
        this.formStateSource = formStateSource;
    }

    public String getFormModelId() {
        return this.formModelId;
    }

    public void setFormModelId(String formModelId) {
        this.formModelId = formModelId;
    }

    public List getRoleRecords() {
        return this.roleRecords;
    }

    public void setRoleRecords(List roleRecords) {
        this.roleRecords = roleRecords;
    }

    public List getOperations() {
        return this.operations;
    }

    public void setOperations(List operations) {
        this.operations = operations;
    }

    public String getFormType() {
        return this.formType;
    }

    public void setFormType(String formType) {
        this.formType = formType;
    }

    public String getThirdPath() {
        return this.thirdPath;
    }

    public void setThirdPath(String thirdPath) {
        this.thirdPath = thirdPath;
    }

/*    public List<FormElementConfigDTO> getFormElementConfigs() {
        return this.formElementConfigs;
    }*/

    public String getRelOrder() {
        return this.relOrder;
    }

    public void setRelOrder(String relOrder) {
        this.relOrder = relOrder;
    }

/*    public void setFormElementConfigs(List<FormElementConfigDTO> formElementConfigs) {
        this.formElementConfigs = formElementConfigs;
    }*/

    public String getValidSql() {
        return this.validSql;
    }

    public void setValidSql(String validSql) {
        this.validSql = validSql;
    }

/*    public List<ValidSqlDetailDTO> getValidSqlDetailList() {
        return this.validSqlDetailList;
    }

    public void setValidSqlDetailList(List<ValidSqlDetailDTO> validSqlDetailList) {
        this.validSqlDetailList = validSqlDetailList;
    }*/

    public boolean isShow() {
        return this.show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public String getSplitScreen() {
        return this.splitScreen;
    }

    public void setSplitScreen(String splitScreen) {
        this.splitScreen = splitScreen;
    }
}
