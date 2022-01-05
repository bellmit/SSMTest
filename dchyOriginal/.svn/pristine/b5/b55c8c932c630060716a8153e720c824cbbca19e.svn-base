package cn.gtmap.msurveyplat.server.service.share;

import cn.gtmap.msurveyplat.common.dto.DchyCgglCgtjDto;
import cn.gtmap.msurveyplat.common.dto.DchyXmglChxmDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:zhaobiqiang@gtmap.cn">zhaobiqiang</a>
 * @version 1.0, 2021/4/8
 * @description 成果统计信息接口
 */
public interface GxcgtjService {

    /**
     * @author <a href="mailto:zhaobiqiang@gtmap.cn">zhaobiqiang</a>
     * @return
     * @description 获取测绘单位信息
     */
    List<Map<String, String>> getChdwxx();


    /**
     * @author <a href="mailto:zhaobiqiang@gtmap.cn">zhaobiqiang</a>
     * @param dchyCgglCgtjDto 成果管理成果统计查询参数信息
     * @return
     * @description 获取成果满意度
     */
    List<Map> getCgmyd(DchyCgglCgtjDto dchyCgglCgtjDto);


    /**
     * @author <a href="mailto:zhaobiqiang@gtmap.cn">zhaobiqiang</a>
     * @param dchyCgglCgtjDto 成果管理成果统计查询参数信息
     * @return
     * @description 获取成果质量
     */
    List<Map> getCgZl(DchyCgglCgtjDto dchyCgglCgtjDto);

    /**
    * @author <a href="mailto:zhaobiqiang@gtmap.cn">zhaobiqiang</a>
    * @param page 第几页
    * @param size 每页大小
    * @param dchyCgglCgtjDto 成果管理成果统计查询参数信息
    * @return
    * @description 建设单位评价记录分页信息
    */
    Page<Map> getJsdwPjjlBypage(int page, int size, DchyCgglCgtjDto dchyCgglCgtjDto);


    /**
     * @author <a href="mailto:zhaobiqiang@gtmap.cn">zhaobiqiang</a>
     * @param page 第几页
     * @param size 每页大小
     * @param dchyCgglCgtjDto 成果管理成果统计查询参数信息
     * @return
     * @description 建设单位评价记录分页信息
     */
    Page<Map> getGldwCcjgBypage(int page, int size, DchyCgglCgtjDto dchyCgglCgtjDto);
}
