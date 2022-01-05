package cn.gtmap.msurveyplat.server.service.share;

import cn.gtmap.msurveyplat.common.dto.GxyyfxDto;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:zhaobiqiang@gtmap.cn">zhaobiqiang</a>
 * @version 1.0, 2021/4/12
 * @description 应用分析信息接口
 */
public interface GxYyfxService {

    /**
     * @author <a href="mailto:zhaobiqiang@gtmap.cn">zhaobiqiang</a>
     * @return
     * @description 获取建设单位和测绘单位信息
     */
     List<Map<String, String>> getJsdwAndChdw();


    /**
     * @author <a href="mailto:zhaobiqiang@gtmap.cn">zhaobiqiang</a>
     * @return
     * @description 获取建设单位委托项目数量
     */
    List<Map> getJsdwWtxmsl();

    /**
     * @author <a href="mailto:zhaobiqiang@gtmap.cn">zhaobiqiang</a>
     * @return
     * @description 获取测绘单位承接项目数量
     */

    List<Map> getChdwCjsl();

    /**
     * @return
     * @author <a href="mailto:zhaobiqiang@gtmap.cn">zhaobiqiang</a>
     * @description 获取项目备案记录分页信息
     */
    Page<Map> getXmbajlByPage(int page,int size, @RequestBody GxyyfxDto gxyyfxDto);


}
