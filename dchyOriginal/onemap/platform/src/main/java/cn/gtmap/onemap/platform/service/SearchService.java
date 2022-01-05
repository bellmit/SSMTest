package cn.gtmap.onemap.platform.service;

import cn.gtmap.onemap.platform.event.SearchException;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;

/**
 * .快速检索
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-8-6 下午2:08
 */
public interface SearchService {

    /**
     * 创建索引
     *
     * @return
     */
    boolean createIndex();

    /**
     * 检索
     *
     * @param query
     * @param limit
     * @param simple
     * @return
     * @throws SearchException
     */
    Set search(String query, int limit, boolean simple) throws SearchException;

    /***
     * 检索 分页
     * @param query
     * @param page
     * @param size
     * @param simple
     * @return
     * @throws SearchException
     */
    Page search(String query, int page, int size, boolean simple) throws SearchException;

    /**
     * get search group
     *
     * @return
     */
    String[] groups();

}
