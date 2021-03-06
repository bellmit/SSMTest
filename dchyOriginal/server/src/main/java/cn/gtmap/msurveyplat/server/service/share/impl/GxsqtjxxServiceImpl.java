package cn.gtmap.msurveyplat.server.service.share.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.page.repository.Repository;
import cn.gtmap.msurveyplat.common.dto.GxsqtjxxDto;
import cn.gtmap.msurveyplat.server.core.mapper.DchyXmglGxsqtj;
import cn.gtmap.msurveyplat.server.service.share.GxsqtjxxService;
import cn.gtmap.msurveyplat.server.util.Constants;
import cn.gtmap.msurveyplat.server.util.ExchangeFeignUtil;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2021/6/10
 * @description 共享申请统计信息接口
 */
@Service
public class GxsqtjxxServiceImpl implements GxsqtjxxService {

    @Autowired
    private Repository repository;
    @Autowired
    private DchyXmglGxsqtj dchyXmglGxsqtj;

    /**
     * @param gxsqtjxxDto
     * @return
     * @description 2021/6/10 共享申请统计信息台账
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @Override
    public Page<Map<String, Object>> gxsqtjxxByPage(GxsqtjxxDto gxsqtjxxDto) {
        Map<String, Object> param = Maps.newHashMap();
        int page = Constants.DCHY_XMGL_PAGINATION_PAGE;
        int size = Constants.DCHY_XMGL_PAGINATION_SIZE;
        if (gxsqtjxxDto != null) {
            if (StringUtils.isNotBlank(gxsqtjxxDto.getGxkssj())) {
                param.put("gxkssj", gxsqtjxxDto.getGxkssj());
            }
            if (StringUtils.isNotBlank(gxsqtjxxDto.getGxjssj())) {
                param.put("gxjssj", gxsqtjxxDto.getGxjssj());
            }
            if (StringUtils.isNotBlank(gxsqtjxxDto.getShzt())) {
                param.put("shzt", gxsqtjxxDto.getShzt());
            }
            if (null != gxsqtjxxDto.getPage()) {
                page = gxsqtjxxDto.getPage();
            }
            if (null != gxsqtjxxDto.getSize()) {
                size = gxsqtjxxDto.getSize();
            }
        }
        return repository.selectPaging("getGxsqtjByPage", param, page - 1, size);
    }

    /**
     * @param gxsqtjxxDto
     * @returng
     * @description 2021/6/10 共享申请统计信息echarts图数据byShjg
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @Override
    public List<Map<String, Object>> gxsqtjxxDataByShjg(GxsqtjxxDto gxsqtjxxDto) {
        Map<String, Object> param = Maps.newHashMap();
        if (gxsqtjxxDto != null) {
            if (StringUtils.isNotBlank(gxsqtjxxDto.getGxkssj())) {
                param.put("gxkssj", gxsqtjxxDto.getGxkssj());
            }
            if (StringUtils.isNotBlank(gxsqtjxxDto.getGxjssj())) {
                param.put("gxjssj", gxsqtjxxDto.getGxjssj());
            }
            if (StringUtils.isNotBlank(gxsqtjxxDto.getShzt())) {
                param.put("shzt", gxsqtjxxDto.getShzt());
            }
        }
        return dchyXmglGxsqtj.getGxsqtjByShjg(param);
    }

    /**
     * @param gxsqtjxxDto
     * @returng
     * @description 2021/6/10 共享申请统计信息echarts图数据byYwmc
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @Override
    public List<Map<String, Object>> gxsqtjxxDataByYwmc(GxsqtjxxDto gxsqtjxxDto) {
        Map<String, Object> param = Maps.newHashMap();
        if (gxsqtjxxDto != null) {
            if (StringUtils.isNotBlank(gxsqtjxxDto.getGxkssj())) {
                param.put("gxkssj", gxsqtjxxDto.getGxkssj());
            }
            if (StringUtils.isNotBlank(gxsqtjxxDto.getGxjssj())) {
                param.put("gxjssj", gxsqtjxxDto.getGxjssj());
            }
            if (StringUtils.isNotBlank(gxsqtjxxDto.getGxywmc())) {
                param.put("gxywmc", gxsqtjxxDto.getGxywmc());
            }
        }
        return dchyXmglGxsqtj.getGxsqtjByYwmc(param);
    }


}
