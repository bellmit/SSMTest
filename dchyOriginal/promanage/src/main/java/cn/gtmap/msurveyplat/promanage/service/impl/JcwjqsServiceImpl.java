package cn.gtmap.msurveyplat.promanage.service.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglClcg;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglZd;
import cn.gtmap.msurveyplat.promanage.core.service.DchyXmglZdService;
import cn.gtmap.msurveyplat.promanage.model.ErrorInfoModel;
import cn.gtmap.msurveyplat.promanage.service.CheckZipFileService;
import cn.gtmap.msurveyplat.promanage.utils.Constants;
import cn.gtmap.msurveyplat.promanage.web.utils.EhcacheUtil;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2021/4/21
 * @description 成果检查-检查文件缺失
 */
@Service("jcwjqsServiceImpl")
public class JcwjqsServiceImpl implements CheckZipFileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JcwjqsServiceImpl.class);

    @Autowired
    private EntityMapper entityMapper;

    @Autowired
    private DchyXmglZdService dchyXmglZdService;

    @Override
    public List<ErrorInfoModel> checkZipFiles(byte[] zipBytes, String bh, Map<String, Object> mlMap, String babh, String gcbh, String chxmid, String glsxid) throws IOException {
        return checkMissingFile(glsxid, gcbh, chxmid);
    }

    @Override
    public String getCode() {
        return Constants.DCHY_XMGL_YWYZXX_CGTJ_WJQS;
    }

    //获取配置中缺失的文件名称
    public List<ErrorInfoModel> checkMissingFile(String glsxid, String gcbh, String chxmid) {
        Cache.ValueWrapper valueWrapper = EhcacheUtil.getDataFromEhcache(glsxid);
        Map<String, Object> mycache = ( Map<String, Object>) valueWrapper.get();
        List<ErrorInfoModel> fileListPz = (List<ErrorInfoModel>) mycache.get("fileListPz");
        List<ErrorInfoModel> fileListSc = (List<ErrorInfoModel>) mycache.get("fileListSc");
        List<String> clsxListShow = (List<String>) mycache.get("clsxListShow");

        List<String> clsxDmListShow = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(clsxListShow)) {
            for (String string : clsxListShow) {
                DchyXmglZd dchyXmglZd = dchyXmglZdService.getDchyXmglByZdlxAndMc(Constants.DCHY_XMGL_CHXM_CLSX, string);
                if (null != dchyXmglZd) {
                    clsxDmListShow.add(dchyXmglZd.getDm());
                }
            }
        }
        List<ErrorInfoModel> missFileList = Lists.newArrayList();
        ErrorInfoModel errorInfoModel = null;
        if (CollectionUtils.isNotEmpty(fileListPz)) {
            for (ErrorInfoModel errorInfoModelPz : fileListPz) {
                if (clsxDmListShow.contains(errorInfoModelPz.getClsx())) {
                    ErrorInfoModel errorInfoModelTemp = null;
                    for (ErrorInfoModel errorInfoModelSc : fileListSc) {
                        String wjmcpz = errorInfoModelPz.getWjmc();
                        int index = wjmcpz.indexOf(".");//获取第一个"."的位置
                        wjmcpz = wjmcpz.substring(0, index);

                        String wjmcSc = errorInfoModelSc.getWjmc();
                        int index1 = wjmcSc.indexOf(".");//获取第一个"."的位置
                        wjmcSc = wjmcSc.substring(0, index1);
                        if (StringUtils.equals(wjmcpz, wjmcSc) &&
                                StringUtils.equals(errorInfoModelPz.getMlmc().substring(0, errorInfoModelPz.getMlmc().lastIndexOf("/")),
                                        errorInfoModelSc.getMlmc().substring(0, errorInfoModelPz.getMlmc().lastIndexOf("/")))) {
                            errorInfoModelTemp = new ErrorInfoModel();
                            errorInfoModelTemp.setClsx(errorInfoModelSc.getClsx());
                            errorInfoModelTemp.setWjmc(errorInfoModelSc.getWjmc());
                            errorInfoModelTemp.setMlmc(errorInfoModelSc.getMlmc());
                            break;
                        }
                    }
                    if (null == errorInfoModelTemp) {
                        errorInfoModel = new ErrorInfoModel();
                        errorInfoModel.setMsxx(Constants.DCHY_XMGL_CGSC_MSXX_WJQS);
                        errorInfoModel.setWjmc(errorInfoModelPz.getWjmc());
                        errorInfoModel.setClsx(getClsxMcByDm(errorInfoModelPz.getClsx()));
                        errorInfoModel.setMlmc(errorInfoModelPz.getMlmc());
                        errorInfoModel.setWjzt(queryShztByClcg(errorInfoModel.getClsx(), gcbh, chxmid));
                        missFileList.add(errorInfoModel);
                    } else if (!StringUtils.equals(errorInfoModelPz.getWjmc(), errorInfoModelTemp.getWjmc())) {
                        continue;
                    }
                }
            }
        }
        return missFileList;
    }

    //通过clsx获取clsxmc
    private String getClsxMcByDm(String dm) {
        String mc = "";
        if (StringUtils.isNotBlank(dm)) {
            DchyXmglZd dchyXmglZd = dchyXmglZdService.getDchyXmglByZdlxAndDm(Constants.DCHY_XMGL_CHXM_CLSX, dm);
            if (null != dchyXmglZd) {
                return dchyXmglZd.getMc();
            }
        }
        return mc;
    }

    //根据工程编号和测量事项,获取当前测量成果的审核状态
    public String queryShztByClcg(String clsx, String gcbh, String chxmid) {
        String shzt = Constants.DCHY_XMGL_SQZT_DSH;
        if (StringUtils.isNotBlank(clsx) && (StringUtils.isNotBlank(gcbh) || StringUtils.isNotBlank(chxmid))) {
            DchyXmglZd dchyXmglZd = dchyXmglZdService.getDchyXmglByZdlxAndMc(Constants.DCHY_XMGL_CHXM_CLSX, clsx);
            if (null != dchyXmglZd) {
                String clsxDm = dchyXmglZd.getDm();
                Example example = new Example(DchyXmglClcg.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("clsx", clsxDm);
                if (StringUtils.isNotBlank(gcbh)) {
                    criteria.andEqualTo("chgcbh", gcbh);
                }
                if (StringUtils.isNotBlank(chxmid)) {
                    criteria.andEqualTo("chxmid", chxmid);
                }
                List<DchyXmglClcg> dchyXmglClcgList = entityMapper.selectByExampleNotNull(example);
                if (CollectionUtils.isNotEmpty(dchyXmglClcgList)) {
                    DchyXmglClcg dchyXmglClcg = dchyXmglClcgList.get(0);
                    shzt = dchyXmglClcg.getShzt();
                }
            }
        }
        return shzt;
    }
}
