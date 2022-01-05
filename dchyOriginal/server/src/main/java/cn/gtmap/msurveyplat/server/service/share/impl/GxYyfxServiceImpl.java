package cn.gtmap.msurveyplat.server.service.share.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.page.repository.Repository;
import cn.gtmap.msurveyplat.common.dto.GxyyfxDto;
import cn.gtmap.msurveyplat.server.core.mapper.DchyCgglGxYyfxMapper;
import cn.gtmap.msurveyplat.server.core.mapper.GxcgtjMapper;
import cn.gtmap.msurveyplat.server.service.share.GxYyfxService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:zhaobiqiang@gtmap.cn">zhaobiqiang</a>
 * @version 1.0, 2021/4/12
 * @description
 */
@Service
public class GxYyfxServiceImpl implements GxYyfxService {

    @Autowired
    private DchyCgglGxYyfxMapper dchyCgglGxYyfxMapper;

    @Autowired
    private Repository repository;

    @Override
    public List<Map<String, String>> getJsdwAndChdw() {
        return dchyCgglGxYyfxMapper.getJsdw();
    }

    @Override
    public List<Map> getJsdwWtxmsl() {
        return dchyCgglGxYyfxMapper.getJsdwWtxmsl();
    }

    @Override
    public List<Map> getChdwCjsl() {
        return dchyCgglGxYyfxMapper.getChdwCjsl();
    }

    @Override
    public Page<Map> getXmbajlByPage(int page, int size, GxyyfxDto gxyyfxDto) {
        Map param = new HashMap();
        if (gxyyfxDto != null) {
            if (StringUtils.isNotBlank(gxyyfxDto.getKssj())) {
                param.put("kssj", gxyyfxDto.getKssj());
            }
            if (StringUtils.isNotBlank(gxyyfxDto.getJssj())) {
                param.put("jssj", gxyyfxDto.getJssj());
            }
            if (StringUtils.isNotBlank(gxyyfxDto.getJsdw())) {
                param.put("jsdw", gxyyfxDto.getJsdw());
            }
            if (StringUtils.isNotBlank(gxyyfxDto.getChdw())) {
                param.put("chdw", gxyyfxDto.getChdw());
            }
        }
        return repository.selectPaging("getXmbajlByPage", param, page - 1, size);
    }
}
