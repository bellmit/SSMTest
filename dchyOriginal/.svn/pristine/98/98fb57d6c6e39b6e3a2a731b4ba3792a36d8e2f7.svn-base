package cn.gtmap.onemap.platform.entity.dict;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-3-26 下午4:17
 */
@Entity
@Table(name = "omp_dict")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Dict {

    @Id
    @GeneratedValue(generator = "sort-uuid")
    @GenericGenerator(name = "sort-uuid", strategy = "cn.gtmap.onemap.core.support.hibernate.UUIDHexGenerator")
    @Column(length = 32)
    private String id;

    @Column(length = 128, nullable = false, unique = true)
    private String name;

    @Column(length = 128)
    private String title;

    @Column(nullable = true)
    private String value;

    @Column(length = 256)
    private String sql;

    @Column(name = "order_number", columnDefinition = "number(3,0)")
    private int order = 0;

    @OneToMany(mappedBy = "dict", cascade = {CascadeType.ALL}, orphanRemoval = true)
    @OrderBy(value = "order")
    private Set<Item> items = new HashSet<Item>();



    public Dict() {
    }

    public Dict(String name, String title) {
        this.name = name;
        this.title = title;
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

    public String getSql()
    {
        return sql;
    }

    public void setSql(String sql){
        this.sql=sql;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public Set<Item> getItems() {
        return items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }

    /**
     * add dict
     *
     * @param item
     * @return
     */
    public Dict addDictItem(Item item) {
        item.setDict(this);
        this.items.add(item);
        return this;
    }

    public void deleteItem(Item item) {
        this.items.remove(item);
    }

    /**
     * get DictItem by name
     *
     * @param name
     * @return
     */
    public Item getDictItem(String name) {
        for (Item item : items) {
            if (item.getName().equals(name)) return item;
        }
        throw new RuntimeException(" dict [ " + name + " ] not found");
    }

}
