package cn.gtmap.onemap.platform.entity.dict;

import java.util.HashMap;
import java.util.Map;

import cn.gtmap.onemap.platform.utils.UUIDGenerator;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-3-26 下午4:17
 */
@Entity
@Table(name = "omp_dict_item")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Item {

    @Id
    @GeneratedValue(generator = "sort-uuid")
    @GenericGenerator(name = "sort-uuid", strategy = "cn.gtmap.onemap.core.support.hibernate.UUIDHexGenerator")
    @Column(length = 32)
    private String id;

    @Column(length = 128, nullable = false)
    private String name;

    @Column(length = 128)
    private String title;

    @Column(nullable = true)
    private String value;

    @Column(name = "order_number", columnDefinition = "number(3,0)")
    private int order = 0;

    @ManyToOne()
    @JoinColumn(name = "dict_id")
    private Dict dict;

    public Item() {

    }

    public Item(String name, String title) {
        this.name = name;
        this.title = title;
    }

    public Item(String name, String title, String value) {
        this.name = name;
        this.title = title;
        this.value = value;
    }

    public Item(String name, String title, String value, Dict dict) {
        this.name = name;
        this.title = title;
        this.value = value;
        this.dict = dict;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Dict getDict() {
        return dict;
    }

    public void setDict(Dict dict) {
        this.dict = dict;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public Map toMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", getName());
        map.put("title", getTitle());
        map.put("value", getValue());
        map.put("on", getOrder());
        return map;
    }

}
