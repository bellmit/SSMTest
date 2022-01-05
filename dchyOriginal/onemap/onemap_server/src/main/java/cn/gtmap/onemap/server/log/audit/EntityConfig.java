package cn.gtmap.onemap.server.log.audit;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-6-17
 */
public class EntityConfig {
    private String catalog;
    private String insertTpl;
    private String updateTpl;
    private String deleteTpl;

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getInsertTpl() {
        return insertTpl;
    }

    public void setInsertTpl(String insertTpl) {
        this.insertTpl = insertTpl;
    }

    public String getUpdateTpl() {
        return updateTpl;
    }

    public void setUpdateTpl(String updateTpl) {
        this.updateTpl = updateTpl;
    }

    public String getDeleteTpl() {
        return deleteTpl;
    }

    public void setDeleteTpl(String deleteTpl) {
        this.deleteTpl = deleteTpl;
    }
}
