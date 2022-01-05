package cn.gtmap.msurveyplat.server.service.share;

import cn.gtmap.msurveyplat.common.dto.GxsqtjxxDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2021/6/10
 * @description 共享申请统计信息接口
 */
public interface GxsqtjxxService {

    /**
     * @param gxsqtjxxDto
     * @returng
     * @description 2021/6/10 共享申请统计信息台账
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    Page<Map<String, Object>> gxsqtjxxByPage(GxsqtjxxDto gxsqtjxxDto);

    /**
     * @param gxsqtjxxDto
     * @returng
     * @description 2021/6/10 共享申请统计信息echarts图数据byShjg
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    List<Map<String, Object>> gxsqtjxxDataByShjg(GxsqtjxxDto gxsqtjxxDto);

    /**
     * @param gxsqtjxxDto
     * @returng
     * @description 2021/6/10 共享申请统计信息echarts图数据byYwmc
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    List<Map<String, Object>> gxsqtjxxDataByYwmc(GxsqtjxxDto gxsqtjxxDto);
}
