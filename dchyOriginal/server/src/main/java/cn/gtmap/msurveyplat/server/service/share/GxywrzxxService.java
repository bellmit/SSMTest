package cn.gtmap.msurveyplat.server.service.share;

import cn.gtmap.msurveyplat.common.dto.GxywrzxxDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;


/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2021/1/11
 * @description 共享业务日志信息接口
 */
public interface GxywrzxxService {

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param page 页数
     * @param size 每页大小
     * @param gxywrzxxDTO 共享业务日志参数
     * @return
     * @description
     */
    Page<Map> getGxywrzxxByPage(int page, int size, GxywrzxxDTO gxywrzxxDTO);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param gxywrzxxDTO 共享业务日志参数
     * @return
     * @description 获取共享日志统计信息
     */
    List<Map> getGxywrzTjxx(GxywrzxxDTO gxywrzxxDTO);

    /**
     * @author <a href="mailto:zhaobiqiang@gtmap.cn">zhaobiqiang</a>
     * @param page 页数
     * @param size 每页大小
     * @param gxywrzxxDTO 共享业务日志参数
     * @return
     * @description
     */
    Page<Map> getGxywrzjlByPage(int page, int size, GxywrzxxDTO gxywrzxxDTO);


}
