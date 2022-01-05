package cn.gtmap.onemap.platform.entity;

import cn.gtmap.onemap.platform.utils.UUIDGenerator;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;

/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 12-10-22 下午3:27
 */
public final class Function {

    public static enum Type {

        SEARCH(1, "信息查询"), IDENTIFY(2, "属性查询"), LOCATION(3, "定位"), EDIT(4, "编辑"), ANALYSIS(5, "分析"), STATISTIC(7, "统计"), TOOLS(6, "常用工具");

        private int id;
        private String name;

        Type(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getNameById(int id) {
            for (Type type : Type.values()) {
                if (type.getId() == id) return type.getName();
            }
            return null;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * 主键
     */
    private String id;

    /**
     * 功能名称
     */
    private String name;

    /**
     * 功能类型
     */
    private int type;

    /**
     * 配置内容
     */
    private String config;


    public Function() {
        this.id = UUIDGenerator.generate();
    }


    public JSONArray getConfigLayers() {
        if (StringUtils.isBlank(config)) return null;
        try {
            JSONObject o = JSON.parseObject(config);
            return (JSONArray) o.get("layers");
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

}
