package cn.gtmap.msurveyplat.server.core.service.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.domain.DchyXtMryjDO;
import cn.gtmap.msurveyplat.common.util.UUID;
import cn.gtmap.msurveyplat.server.core.mapper.DchyXtMryjMapper;
import cn.gtmap.msurveyplat.server.core.service.DchyXtMryjService;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/3/26
 * @description 多测合一系统默认意见
 */
@Service
public class DchyXtMryjServiceImpl implements DchyXtMryjService {
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private DchyXtMryjMapper dchyXtMryjMapper;

    @Override
    public List<DchyXtMryjDO> getDchyXtMryjDOListByGzldyidAndGzljdmc(String gzldyid, String gzljdmc) {
        List<DchyXtMryjDO> dchyXtMryjDOList = null;
        if(StringUtils.isNotBlank(gzldyid)&&StringUtils.isNotBlank(gzljdmc)) {
            Example example = new Example(DchyXtMryjDO.class);
            example.createCriteria().andEqualTo("gzldyid", gzldyid).andEqualTo("gzljdmc",gzljdmc);
            dchyXtMryjDOList = entityMapper.selectByExample(example);
        }
        return dchyXtMryjDOList;
    }

    @Override
    public List<DchyXtMryjDO> getDchyXtMryjDOListByXmidAndGzljdmc(String xmid, String gzljdmc) {
        List<DchyXtMryjDO> dchyXtMryjDOList = null;
        if(StringUtils.isNotBlank(xmid)&&StringUtils.isNotBlank(gzljdmc)) {
            dchyXtMryjDOList = dchyXtMryjMapper.getDchyXtMryjDOListByXmidAndGzljdmc(xmid,gzljdmc);
        }
        return dchyXtMryjDOList;
    }

    @Override
    public List<DchyXtMryjDO> getYjpz(Map<String, String> map) {
        List<DchyXtMryjDO> yjpzList = null;
        try {
            yjpzList = dchyXtMryjMapper.getYjpz(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return yjpzList;
    }

    @Override
    public int addMryj(DchyXtMryjDO dchyXtMryjDO) {
        int addFlag = 0;
        if(null != dchyXtMryjDO){
            dchyXtMryjDO.setYjid(UUIDGenerator.generate18());
            addFlag = entityMapper.insert(dchyXtMryjDO);
        }
        return addFlag;
    }

    @Override
    public int delMryjByYjid(String yjid) {
        int delFlag = 0;
        if (StringUtils.isNotBlank(yjid)) {
            delFlag = entityMapper.deleteByPrimaryKey(DchyXtMryjDO.class, yjid);
        }
        return delFlag;
    }

    @Override
    public int delMryj(List<String> yjidList) {
        int delFlag = 0  ;
        if (CollectionUtils.isNotEmpty(yjidList)) {
            delFlag =1 ;
            for (int i = 0; i < yjidList.size(); i++) {
                delFlag = delMryjByYjid(yjidList.get(i));
                if(delFlag<0){
                    break;
                }
            }
        }
        return delFlag;
    }

    @Override
    public int updMryjByYjid(DchyXtMryjDO dchyXtMryjDO) {
        int updFlag = 0;
        if(null != dchyXtMryjDO){
            updFlag = entityMapper.updateByPrimaryKey(dchyXtMryjDO);
        }
        return updFlag;
    }

    @Override
    public String getYjid(Map<String, String> map) {
        String yjid = null;
        try {
            yjid = dchyXtMryjMapper.getYjid(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return yjid;
    }
}
