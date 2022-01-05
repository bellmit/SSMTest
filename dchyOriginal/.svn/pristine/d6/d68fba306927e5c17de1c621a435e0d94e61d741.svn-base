package cn.gtmap.onemap.server.monitor.service;

import cn.gtmap.onemap.server.monitor.model.Item;
import cn.gtmap.onemap.server.monitor.model.enums.Status;
import com.mysema.query.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-6-20
 */
public interface ItemManager {

    List<Item> getAllItems();

    List<Item> getItems(int hostId);

    Page<Item> getItems(int hostId, Pageable pageable);

    Page<Item> findItems(Predicate predicate, Pageable pageable);

    Item getItem(int id);

    Item getItemByKey(int hostId, String key);

    Item saveItem(Item item);

    void setItemsStatus(Collection<Integer> ids, Status status);

    void removeItem(Item item);
}
