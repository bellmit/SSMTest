package cn.gtmap.msurveyplat.portal.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 2019/7/8
 * @description 用户表模型
 */
@Entity
public class User {

    @Id
    private String id;
    private String name;
    private Integer age;

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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "id:" + this.id + ",name:" + this.name + ",age:" + this.age;
    }
}
