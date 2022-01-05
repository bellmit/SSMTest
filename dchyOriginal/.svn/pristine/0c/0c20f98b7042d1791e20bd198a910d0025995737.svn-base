package cn.gtmap.msurveyplat.server.service.share.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.page.repository.Repository;
import cn.gtmap.msurveyplat.common.dto.DchyCgglCgtjDto;
import cn.gtmap.msurveyplat.server.core.mapper.GxcgtjMapper;
import cn.gtmap.msurveyplat.server.service.share.GxcgtjService;
import feign.Param;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:zhaobiqiang@gtmap.cn">zhaobiqiang</a>
 * @version 1.0, 2021/4/8
 * @description 成果统计信息服务
 */
@Service
public class GxcgtjServiceImpl implements GxcgtjService {
    @Autowired
    private GxcgtjMapper gxcgtjMapper;

    @Autowired
    private Repository repository;

    @Override
    public List<Map<String, String>> getChdwxx() {
        return gxcgtjMapper.getChdwxx();
    }

    @Override
    public List<Map> getCgmyd(DchyCgglCgtjDto dchyCgglCgtjDto) {
        Map param = new HashMap();
        if (StringUtils.isNotBlank(dchyCgglCgtjDto.getPjkssj())) {
            param.put("kssj", dchyCgglCgtjDto.getPjkssj());
        }
        if (StringUtils.isNotBlank(dchyCgglCgtjDto.getPjjssj())) {
            param.put("jssj", dchyCgglCgtjDto.getPjjssj());
        }
        if (StringUtils.isNotBlank(dchyCgglCgtjDto.getMlkid())) {
            param.put("mlkid", dchyCgglCgtjDto.getMlkid());
        }
        return gxcgtjMapper.getCgmyd(param);
    }

    @Override
    public List<Map> getCgZl(DchyCgglCgtjDto dchyCgglCgtjDto) {
        Map param = new HashMap();
        if (StringUtils.isNotBlank(dchyCgglCgtjDto.getPjkssj())) {
            param.put("kssj", dchyCgglCgtjDto.getPjkssj());
        }
        if (StringUtils.isNotBlank(dchyCgglCgtjDto.getPjjssj())) {
            param.put("jssj", dchyCgglCgtjDto.getPjjssj());
        }
        if (StringUtils.isNotBlank(dchyCgglCgtjDto.getChdw())) {
            param.put("chdw", dchyCgglCgtjDto.getChdw());
        }
        return gxcgtjMapper.getCgzl(param);
    }


    @Override
    public Page<Map> getJsdwPjjlBypage(int page, int size, DchyCgglCgtjDto dchyCgglCgtjDto) {
        Map param = new HashMap();
        if (dchyCgglCgtjDto != null) {
            if (StringUtils.isNotBlank(dchyCgglCgtjDto.getPjkssj())) {
                param.put("pjkssj", dchyCgglCgtjDto.getPjkssj());
            }
            if (StringUtils.isNotBlank(dchyCgglCgtjDto.getPjjssj())) {
                param.put("pjjssj", dchyCgglCgtjDto.getPjjssj());
            }
            if (StringUtils.isNotBlank(dchyCgglCgtjDto.getMlkid())) {
                param.put("mlkid", dchyCgglCgtjDto.getMlkid());
            }
        }
        return repository.selectPaging("getJsdwPjjlByPage", param, page - 1, size);
    }

    @Override
    public Page<Map> getGldwCcjgBypage(int page, int size, DchyCgglCgtjDto dchyCgglCgtjDto) {
        Map param = new HashMap();
        if (dchyCgglCgtjDto != null) {
            if (StringUtils.isNotBlank(dchyCgglCgtjDto.getPjkssj())) {
                param.put("pjkssj", dchyCgglCgtjDto.getPjkssj());
            }
            if (StringUtils.isNotBlank(dchyCgglCgtjDto.getPjjssj())) {
                param.put("pjjssj", dchyCgglCgtjDto.getPjjssj());
            }
            if (StringUtils.isNotBlank(dchyCgglCgtjDto.getChdw())) {
                param.put("chdw", dchyCgglCgtjDto.getChdw());
            }
        }
        return repository.selectPaging("getGldwCcjgByPage", param, page - 1, size);
    }


}
