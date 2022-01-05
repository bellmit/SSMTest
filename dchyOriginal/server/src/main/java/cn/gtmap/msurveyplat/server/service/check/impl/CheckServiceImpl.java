package cn.gtmap.msurveyplat.server.service.check.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.domain.DchyXtBdbtzdDO;
import cn.gtmap.msurveyplat.server.core.mapper.CheckMapper;
import cn.gtmap.msurveyplat.server.service.check.CheckService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
 * @version 2020/4/9
 * @description 表单验证
 */
@Service
public class CheckServiceImpl implements CheckService {

    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private CheckMapper checkMapper;

    @Override
    public String checkBdbtx(String gzldyid, String gzljdid, String xmid) {
        String msg = "以下表单有为空项：";
        List<String> bdList = Lists.newArrayList();
        List<Map<String, String>> list = checkMapper.getSql(gzldyid, gzljdid);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        for (Map<String, String> map : list) {
            if (bdList.contains(map.get("BDMC"))) {
                continue;
            }
            String sql = map.get("SQL");
            if (sql.indexOf("${xmid}") !=-1) {
                sql = sql.replace("${xmid}","'"+xmid+"'");
                List<Map<String, Object>> resultList = checkMapper.executeSql(sql);
                if (CollectionUtils.isNotEmpty(resultList)) {
                    for (Map<String, Object> result: resultList) {
                        if (result ==null) {
                            bdList.add(map.get("BDMC"));
                            break;
                        }
                        Object value = result.get(map.get("SJKBZD"));
                        if (value ==null || StringUtils.isBlank(result.get(map.get("SJKBZD")).toString())) {
                            bdList.add(map.get("BDMC"));
                            break;
                        }
                    }
                } else {
                    bdList.add(map.get("BDMC"));
                }
            }
        }
        if (CollectionUtils.isNotEmpty(bdList)) {
            for (String bd: bdList) {
                msg += bd + ",";
            }
            return msg.substring(0,msg.length()- 1);
        } else {
            return null;
        }
    }

    @Override
    public Map<String,List<DchyXtBdbtzdDO>> getBtxZd(String gzldyid, String gzljdid) {
        Map<String,List<DchyXtBdbtzdDO>> result = Maps.newHashMap();
        if (StringUtils.isNotBlank(gzldyid)) {
            Example example = new Example(DchyXtBdbtzdDO.class);
            example.createCriteria().andEqualTo("gzldyid",gzldyid).andEqualTo("gzljdid",gzljdid);
            List<DchyXtBdbtzdDO> dchyXtBdbtzdDOList = entityMapper.selectByExample(example);
            if (CollectionUtils.isEmpty(dchyXtBdbtzdDOList)) {
                return result;
            }
            List<String> dbmcList = new ArrayList<>();
            for (DchyXtBdbtzdDO dchyXtBdbtzdDO: dchyXtBdbtzdDOList) {
                if (!dbmcList.contains(dchyXtBdbtzdDO.getBdid())) {
                    dbmcList.add(dchyXtBdbtzdDO.getBdid());
                }
            }
            List<DchyXtBdbtzdDO> bdList;
            for (String bdmc : dbmcList) {
                bdList = new ArrayList<>();
                for (DchyXtBdbtzdDO dchyXtBdbtzdDO: dchyXtBdbtzdDOList) {
                    if (StringUtils.equals(bdmc,dchyXtBdbtzdDO.getBdid())) {
                        bdList.add(dchyXtBdbtzdDO);
                    }
                }
                result.put(bdmc, bdList);
            }

        }
        return result;
    }



}
