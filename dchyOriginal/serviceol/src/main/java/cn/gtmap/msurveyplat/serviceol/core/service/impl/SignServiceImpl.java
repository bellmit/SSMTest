package cn.gtmap.msurveyplat.serviceol.core.service.impl;


import cn.gtmap.msurveyplat.common.annotion.SystemLog;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglDbrw;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglSqxx;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.dto.ShxxParamDTO;
import cn.gtmap.msurveyplat.common.util.CalendarUtil;
import cn.gtmap.msurveyplat.common.util.ProLog;
import cn.gtmap.msurveyplat.common.vo.ShxxVO;
import cn.gtmap.msurveyplat.serviceol.core.service.BlsxBjService;
import cn.gtmap.msurveyplat.serviceol.core.service.SignService;
import cn.gtmap.msurveyplat.serviceol.utils.DsxYwljUtil;
import com.google.common.collect.Maps;
import com.gtis.config.AppConfig;
import com.gtis.plat.service.SysSignService;
import com.gtis.plat.service.SysUserService;
import com.gtis.plat.vo.PfSignVo;
import com.gtis.plat.vo.PfUserVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/2/27
 * @description 签名接口
 */
@Service
public class SignServiceImpl implements SignService {

    private static final Logger logger = LoggerFactory.getLogger(SignServiceImpl.class);

    @Autowired
    private SysSignService sysSignService;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private SysUserService sysUserService;


    private static final String SIGN_IMAGE_URL = AppConfig.getPlatFormUrl() + "/tag/signtag!image.action?signVo.signId=";

    @Override
    public ShxxVO getShxxVO(String rwid, String dqjd) {
        ShxxVO shxxVO = new ShxxVO();
        if (!StringUtils.isNoneEmpty(rwid)) {
            return shxxVO;
        }
        List<PfSignVo> pfSignVoList = sysSignService.getSignList(dqjd, rwid);
        DchyXmglDbrw dchyXmglDbrw = entityMapper.selectByPrimaryKey(DchyXmglDbrw.class, rwid);
        return shxxVO;
    }

    @Override
    public ShxxVO updateShxx(ShxxParamDTO shxxParamDTO) {
        ShxxVO shxxVO = new ShxxVO();
        PfSignVo pfSignVo = new PfSignVo();
        pfSignVo.setSignDate(CalendarUtil.getCurHMSDate());
        pfSignVo.setSignId(shxxParamDTO.getShxxid());
        pfSignVo.setProId(shxxParamDTO.getXmid());
        pfSignVo.setUserId(shxxParamDTO.getUserid());
        PfUserVo pfUserVo = sysUserService.getUserVo(shxxParamDTO.getUserid());
        if (pfUserVo != null) {
            pfSignVo.setSignName(pfUserVo.getUserName());
        }
        pfSignVo.setSignType("1");
        pfSignVo.setSignKey(shxxParamDTO.getSignKey());

        String signId = "";
        synchronized (this) {
            List<PfSignVo> pfSignVoList = sysSignService.getSignListByUserId(shxxParamDTO.getDqjdmc(), shxxParamDTO.getXmid(), shxxParamDTO.getUserid());
            if (CollectionUtils.isEmpty(pfSignVoList)) {
                sysSignService.insertAutoSign(pfSignVo);
                pfSignVoList = sysSignService.getSignList(shxxParamDTO.getDqjdmc(), shxxParamDTO.getXmid());
            }
            if (CollectionUtils.isNotEmpty(pfSignVoList)) {
                signId = pfSignVoList.get(0).getSignId();
            }
        }

        shxxVO.setQmid(signId);
        shxxVO.setQmsj(CalendarUtil.formatDateTime(pfSignVo.getSignDate()));
        return shxxVO;
    }

    @Override
    @SystemLog(czmkMc = ProLog.CZMK_SIGN_CODE, czmkCode = ProLog.CZMK_SIGN_MC, czlxMc = ProLog.CZLX_DELETE_MC, czlxCode = ProLog.CZLX_DELETE_CODE)
    public boolean deleteShxxSign(String signId) {
        boolean bo = false;
        if (StringUtils.isNoneBlank(signId)) {
            bo = sysSignService.deleteSign(signId);
        }
        return bo;
    }

    /**
     * @param signId
     * @return byte[]
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: signId
     * @time 2020/12/4 14:41
     * @description 通过签名id获取签名图片
     */


    @Override
    public byte[] getSignPicBySignId(String signId) {

        // 3 使用HttpClient执行httpGet，相当于按回车，发起请求
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 声明URIBuilder
        URIBuilder uriBuilder = null;
        byte[] result = null;
        try {
            uriBuilder = new URIBuilder(SIGN_IMAGE_URL + signId);

            // 2 创建httpGet对象，相当于设置url请求地址
            HttpGet httpGet = new HttpGet(uriBuilder.build());

            // 3 使用HttpClient执行httpGet，相当于按回车，发起请求
            CloseableHttpResponse response = httpClient.execute(httpGet);
            response.getEntity();

            result = EntityUtils.toByteArray(response.getEntity());
        } catch (Exception e) {
            logger.error("签名图片下载失败{}，原因{}", signId, e);
        }
        return result;
    }

    @Override
    @SystemLog(czmkMc = ProLog.CZMK_SH_CODE, czmkCode = ProLog.CZMK_SH_MC, czlxMc = ProLog.CZLX_SAVE_MC, czlxCode = ProLog.CZLX_SAVE_CODE)
    public Map<String, Object> shbj(Map pram) {
        String rwid = MapUtils.getString(pram, "rwid");
        if (StringUtils.isNotBlank(rwid)) {
            /*获取待任务*/
            DchyXmglDbrw dchyXmglDbrw = entityMapper.selectByPrimaryKey(DchyXmglDbrw.class, rwid);
            if (dchyXmglDbrw != null) {
                /*获取申请信息*/
                DchyXmglSqxx dchyXmglSqxx = entityMapper.selectByPrimaryKey(DchyXmglSqxx.class, dchyXmglDbrw.getSqxxid());
                if (dchyXmglSqxx != null) {
                    BlsxBjService blsxBjService = DsxYwljUtil.getBlsxBjServiceByCode(dchyXmglSqxx.getBlsx());
                    if (null != blsxBjService) {
                        /*审核办结*/
                        return blsxBjService.bj(rwid, pram);
                    }
                }
            }
        }
        Map<String, Object> resultMap = Maps.newHashMap();
        resultMap.put("msg", "任务信息缺失，无法办结");
        resultMap.put("code", ResponseMessage.CODE.QUERY_NULL.getCode());
        return resultMap;
    }
}
