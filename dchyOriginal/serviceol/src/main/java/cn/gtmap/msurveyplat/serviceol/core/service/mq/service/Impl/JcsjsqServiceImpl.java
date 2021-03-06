package cn.gtmap.msurveyplat.serviceol.core.service.mq.service.Impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglChxm;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglSjcl;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglSjxx;
import cn.gtmap.msurveyplat.common.dto.DchyXmglJcsjsqDto;
import cn.gtmap.msurveyplat.serviceol.core.service.mq.service.JcsjsqService;
import cn.gtmap.msurveyplat.serviceol.web.util.FileDownoadUtil;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Base64;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2021/3/10
 * @description 基础数据申请实现类
 */
@Service
public class JcsjsqServiceImpl implements JcsjsqService {

    @Resource(name = "entityMapperXSBF")
    private EntityMapper entityMapper;

    @Override
    public DchyXmglJcsjsqDto getSingleData(String chxmid) {
        DchyXmglJcsjsqDto dchyXmglJcsjsqDto = new DchyXmglJcsjsqDto();
        //测绘项目数据
        DchyXmglChxm dchyXmglChxm = entityMapper.selectByPrimaryKey(DchyXmglChxm.class, chxmid);
        if (null != dchyXmglChxm && StringUtils.isNotBlank(dchyXmglChxm.getSlbh())) {
            String babh = dchyXmglChxm.getBabh();
            Example exampleSjxx = new Example(DchyXmglSjxx.class);
            exampleSjxx.createCriteria().andEqualTo("glsxid", babh);
            exampleSjxx.setOrderByClause("sjsj desc");
            List<DchyXmglSjxx> dchyXmglSjxxList = entityMapper.selectByExample(exampleSjxx);
            if (CollectionUtils.isNotEmpty(dchyXmglSjxxList)) {
                DchyXmglSjxx dchyXmglSjxx = dchyXmglSjxxList.get(0);
                String sjxxid = dchyXmglSjxx.getSjxxid();
                Example exampleSjcl = new Example(DchyXmglSjcl.class);
                exampleSjcl.createCriteria().andEqualTo("sjxxid", sjxxid);
                List<DchyXmglSjcl> dchyXmglSjclList = entityMapper.selectByExample(exampleSjcl);
                if (CollectionUtils.isNotEmpty(dchyXmglSjclList)) {
                    DchyXmglSjcl dchyXmglSjcl = dchyXmglSjclList.get(0);
                    String wjzxid = dchyXmglSjcl.getWjzxid();
                    if (StringUtils.isNoneBlank(babh, wjzxid)) {
                        Map<String, Object> sjfw = queryFilesbyWjzxid(babh, Integer.parseInt(wjzxid));
                        dchyXmglJcsjsqDto.setSjfw(sjfw);
                    }
                }
            }
        }
        return dchyXmglJcsjsqDto;
    }

    private Map<String, Object> queryFilesbyWjzxid(String glsxid, int wjzxid) {
        Map<String, Object> map = Maps.newHashMap();
        map.put(glsxid, Base64.getEncoder().encodeToString((byte[]) FileDownoadUtil.downLoadFile(wjzxid).get("wjnr")));
        map.put("wjmc", FileDownoadUtil.downLoadFile(wjzxid).get("wjmc"));
        return map;
    }
}
