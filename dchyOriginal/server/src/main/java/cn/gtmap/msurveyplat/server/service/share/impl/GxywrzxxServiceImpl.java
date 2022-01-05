package cn.gtmap.msurveyplat.server.service.share.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.page.repository.Repository;
import cn.gtmap.msurveyplat.common.dto.GxywrzxxDTO;
import cn.gtmap.msurveyplat.server.core.mapper.DchyCgglGxywrzMapper;
import cn.gtmap.msurveyplat.server.service.share.GxywrzxxService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2021/1/12
 * @description
 */
@Service
public class GxywrzxxServiceImpl implements GxywrzxxService {
    @Autowired
    private DchyCgglGxywrzMapper dchyCgglGxywrzMapper;
    @Autowired
    private Repository repository;

    @Override
    public Page<Map> getGxywrzxxByPage(int page, int size, GxywrzxxDTO gxywrzxxDTO) {
        Map param = new HashMap();
        if(gxywrzxxDTO != null) {
            if(StringUtils.isNotBlank(gxywrzxxDTO.getGxkssj())) {
                param.put("gxkssj",gxywrzxxDTO.getGxkssj());
            }
            if(StringUtils.isNotBlank(gxywrzxxDTO.getGxjssj())) {
                param.put("gxjssj",gxywrzxxDTO.getGxjssj());
            }
            if(StringUtils.isNotBlank(gxywrzxxDTO.getGxbmid())) {
                param.put("gxbmid",gxywrzxxDTO.getGxbmid());
            }
            if(StringUtils.isNotBlank(gxywrzxxDTO.getGxjsid())) {
                param.put("gxjsid",gxywrzxxDTO.getGxjsid());
            }
        }
        return repository.selectPaging("getGxywrzxxByPage",param, page -1, size);
    }

    @Override
    public List<Map> getGxywrzTjxx(GxywrzxxDTO gxywrzxxDTO) {
        List<Map> resultMapList = null;
        if(gxywrzxxDTO != null) {
            Map param = new HashMap();
            param.put("gxkssj",gxywrzxxDTO.getGxkssj());
            param.put("gxjssj",gxywrzxxDTO.getGxjssj());
            param.put("gxbmid",gxywrzxxDTO.getGxbmid());
            param.put("gxjsid",gxywrzxxDTO.getGxjsid());
            resultMapList = dchyCgglGxywrzMapper.getGxywrzTjxx(param);
        }
        return resultMapList;
    }


    @Override
    public Page<Map> getGxywrzjlByPage(int page, int size, GxywrzxxDTO gxywrzxxDTO) {
        Map param = new HashMap();
        if(gxywrzxxDTO != null) {
            if(StringUtils.isNotBlank(gxywrzxxDTO.getGxkssj())) {
                param.put("gxkssj",gxywrzxxDTO.getGxkssj());
            }
            if(StringUtils.isNotBlank(gxywrzxxDTO.getGxjssj())) {
                param.put("gxjssj",gxywrzxxDTO.getGxjssj());
            }
            if(StringUtils.isNotBlank(gxywrzxxDTO.getGxbmid())) {
                param.put("gxbmid",gxywrzxxDTO.getGxbmid());
            }
            if(StringUtils.isNotBlank(gxywrzxxDTO.getGxjsid())) {
                param.put("gxjsid",gxywrzxxDTO.getGxjsid());
            }
            if(StringUtils.isNotBlank(gxywrzxxDTO.getGxywmc())) {
                param.put("gxywmc",gxywrzxxDTO.getGxywmc());
            }
        }
        return repository.selectPaging("getGxywrzjlByPage",param, page -1, size);
    }
}
