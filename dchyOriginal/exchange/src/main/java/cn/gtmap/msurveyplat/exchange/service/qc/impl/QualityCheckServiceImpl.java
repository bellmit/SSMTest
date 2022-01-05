package cn.gtmap.msurveyplat.exchange.service.qc.impl;

import cn.gtmap.msurveyplat.common.util.CalendarUtil;
import cn.gtmap.msurveyplat.exchange.service.qc.QualityCheckService;
import cn.gtmap.msurveyplat.exchange.util.Constants;
import cn.gtmap.msurveyplat.exchange.util.HttpClientUtil;
import cn.gtmap.msurveyplat.exchange.util.PlatformUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.gtis.config.AppConfig;
import com.gtis.fileCenter.ex.NodeNotFoundException;
import com.gtis.fileCenter.model.Node;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;


/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/2/27
 * @description 成果数据质检服务
 */
@Service
public class QualityCheckServiceImpl implements QualityCheckService {
    private static final Logger LOGGER = LoggerFactory.getLogger(QualityCheckServiceImpl.class);

    @Autowired
    private PlatformUtil platformUtil;

    @Override
    public Object startQualityCheck(String slbh, String cgsjlx) throws IOException {
        Map qualityCheckParam = getQualityCheckParam(slbh, cgsjlx);
        String result = "";
        LOGGER.info("成果数据质检参数:{} {} ", CalendarUtil.getCurHMSStrDate(), JSON.toJSONString(qualityCheckParam));
        String dataQualityCheckUrl = AppConfig.getProperty("data.quality.check.url");
        if (StringUtils.isNotEmpty(dataQualityCheckUrl)) {
            result = HttpClientUtil.sendPostRequest(dataQualityCheckUrl, qualityCheckParam);
            LOGGER.info("成果数据质检结果:{} {} ", CalendarUtil.getCurHMSStrDate(), result);
            // 调接口之后 删除zip包
            Integer nodeId = Integer.parseInt(qualityCheckParam.get("nodeId").toString()) ;
            platformUtil.deleteNodeById(nodeId);

        }
        return result;
    }

    @Override
    public Map<String, Object> check(String slbh) {
        Map<String, Object> resultMap = Maps.newHashMap();
        String dchyCgsjMlm = AppConfig.getProperty("dchy.cgsj.mlm");
        List<Node> childNodeList = null;
        try {
            childNodeList = platformUtil.getChildNodeByClmc(slbh, dchyCgsjMlm);
            if (CollectionUtils.isNotEmpty(childNodeList)) {
                resultMap.put("code", true);
            } else {
                resultMap.put("code", false);
            }
        } catch (NodeNotFoundException e) {
            resultMap.put("code", false);
        }
        return resultMap;
    }

    @Override
    public Object importDatabase(String slbh, String cgsjlx) throws IOException {
        String result = "";
        Map qualityCheckParam = getQualityCheckParam(slbh,cgsjlx);
        LOGGER.info("成果数据入库参数:{} {} ", CalendarUtil.getCurHMSStrDate(),JSON.toJSONString(qualityCheckParam));
        String dataQualityCheckUrl = AppConfig.getProperty("data.import.database.url");
        if(StringUtils.isNotEmpty(dataQualityCheckUrl)) {
            result = HttpClientUtil.sendPostRequest(dataQualityCheckUrl, qualityCheckParam);
            LOGGER.info("成果数据入库结果:{} {} ", CalendarUtil.getCurHMSStrDate(),result);
            // 调接口之后 删除zip包
            Integer nodeId = Integer.parseInt(qualityCheckParam.get("nodeId").toString()) ;
            platformUtil.deleteNodeById(nodeId);
        }
        return result;
    }

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param slbh 受理编号
     * @param cgsjlx 成果数据类型
     * @return
     * @description 获取质检接口参数
     */
    private Map getQualityCheckParam(String slbh, String cgsjlx){
        Map qualityCheckParam = Maps.newHashMap();
        Integer parentId = platformUtil.creatXmNode(slbh);
        String dchyCgsjMlm = AppConfig.getProperty("dchy.cgsj.mlm");
        Integer dchyCgsjMlNodeId = platformUtil.createFileFolderByClmc(parentId,dchyCgsjMlm,"");
        List<Node> childNodeList = platformUtil.getChildNodeListByParentId(dchyCgsjMlNodeId);
        if(CollectionUtils.isNotEmpty(childNodeList)) {
            boolean flag = false;
            for (Node node : childNodeList) {
                if (StringUtils.equals(Constants.CHSJ_ZIP_NAME,node.getName())) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                // 将文件夹压缩成zip包
                Integer[] childIdArray = new Integer[childNodeList.size()];
                for (int i=0; i< childNodeList.size(); i++) {
                    childIdArray[i] = childNodeList.get(i).getId();
                }
                platformUtil.zip(childIdArray, dchyCgsjMlNodeId, Constants.CHSJ_ZIP_NAME);
                childNodeList = platformUtil.getChildNodeListByParentId(dchyCgsjMlNodeId);
            }
            for (Node node : childNodeList) {
                if (StringUtils.equals(Constants.CHSJ_ZIP_NAME,node.getName())) {
                    qualityCheckParam.put("f", "pjson");
                    qualityCheckParam.put("cgsjlx", cgsjlx);
                    qualityCheckParam.put("xmbh", slbh);
                    qualityCheckParam.put("downloadUrl", AppConfig.getProperty("fileCenter.url") + "/file/get.do?fid=" + node.getId() + "&token=whosyourdaddy" + "/" + node.getName());
                    qualityCheckParam.put("nodeId", String.valueOf(node.getId()));
                    break;
                }
            }
        }
        return qualityCheckParam;
    }

}
