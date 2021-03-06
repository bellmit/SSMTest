package cn.gtmap.msurveyplat.serviceol.service.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.core.support.mybatis.page.repository.Repository;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglDbrw;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglMlk;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglSqxx;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.serviceol.core.service.DchyXmglZdService;
import cn.gtmap.msurveyplat.serviceol.core.service.SignService;
import cn.gtmap.msurveyplat.serviceol.service.DchyGldwDblbService;
import cn.gtmap.msurveyplat.serviceol.utils.Constants;
import cn.gtmap.msurveyplat.serviceol.utils.UserUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2020/12/1
 * @description 多测合一管理单位待办列表
 */
@Service
public class DchyGldwDblbServiceImpl implements DchyGldwDblbService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "entityMapper")
    EntityMapper entityMapper;

    @Autowired
    private Repository repository;

    @Autowired
    private SignService signService;

    @Autowired
    private DchyXmglZdService dchyXmglZdService;

    /**
     * @param map
     * @return
     * @description 2020/12/1 管理单位待办列表台账
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @Override
    public Page<Map<String, Object>> getDblbByDwmcAndSqsj(Map<String, Object> map) {
        int page = Integer.parseInt(map.get("page") != null ? CommonUtil.formatEmptyValue(map.get("page")) : Constants.DCHY_XMGL_PAGINATION_PAGE);
        int size = Integer.parseInt(map.get("size") != null ? CommonUtil.formatEmptyValue(map.get("size")) : Constants.DCHY_XMGL_PAGINATION_SIZE);

        return repository.selectPaging("queryDblbByPage", map, page - 1, size);
    }

    /**
     * @param mlkid
     * @return
     * @description 2020/12/1 管理单位待办列表审核按钮,通过名录库id获取名录库信息
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @Override
    public List<Map<String, Object>> queryMlkxxByMlkid(String mlkid) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        if (StringUtils.isNotBlank(mlkid)) {
            DchyXmglMlk dchyXmglMlk = entityMapper.selectByPrimaryKey(DchyXmglMlk.class, mlkid);
            if (dchyXmglMlk != null) {
                Map<String, Object> map = JSON.parseObject(JSON.toJSONString(dchyXmglMlk), Map.class);
                map.put("dwxzMc", dchyXmglZdService.getDchyXmglByZdlxAndDm(Constants.DCHY_XMGL_CHXM_DWXZ, dchyXmglMlk.getDwxz()).getMc());
                map.put("zzdjMc", dchyXmglZdService.getDchyXmglByZdlxAndDm(Constants.DCHY_XMGL_CHXM_ZZDJ, dchyXmglMlk.getZzdj()).getMc());
                mapList.add(map);
            }
        }
        return mapList;
    }

    /**
     * @param map
     * @return
     * @description 2020/12/1 管理单位待办列表审核按钮,判断能否进行审核
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @Override
    public ResponseMessage isEnterPage(Map<String, Object> map) {
        ResponseMessage message = ResponseUtil.wrapSuccessResponse();
        List<Map<String, Object>> mapList = new ArrayList<>();
        Map<String, Object> objectMap = new HashMap<>();
        String dbrwid = CommonUtil.formatEmptyValue(map.get("dbrwid"));
        if (StringUtils.isNotBlank(dbrwid)) {
            //获取当前用户的userid
            String useridCurrent = UserUtil.getCurrentUserId();

            DchyXmglDbrw dchyXmglDbrw = entityMapper.selectByPrimaryKey(DchyXmglDbrw.class, dbrwid);
            String userid = dchyXmglDbrw.getBlryid();
            if (userid == null) {
                dchyXmglDbrw.setBlryid(useridCurrent);
                entityMapper.saveOrUpdate(dchyXmglDbrw, dchyXmglDbrw.getDbrwid());
                objectMap.put("isEnter", "true");
            } else {
                //判断待办列表中的办理人员id是否和当前用户一致
                if (userid.equals(useridCurrent)) {
                    objectMap.put("isEnter", "true");
                } else {
                    message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.USERVER_FAIL.getMsg(), ResponseMessage.CODE.USERVER_FAIL.getCode());
                    objectMap.put("isEnter", "false");
                }
            }
        } else {
            message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.USERVER_FAIL.getMsg(), ResponseMessage.CODE.USERVER_FAIL.getCode());
            objectMap.put("isEnter", "false");
        }

        mapList.add(objectMap);
        message.getData().put("dataList", mapList);

        return message;
    }

    /**
     * @param zrryid
     * @param sqxxid
     * @param glsxid
     * @return
     * @description 2020/12/1 管理单位待办列表 取回
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public synchronized Map<String, Object> moveDblbxxByzrryid(String zrryid, String sqxxid, String glsxid) {
        Map<String, Object> map = Maps.newHashMap();
        String code = "";
        String msg = "";
        if (StringUtils.isNoneBlank(zrryid, sqxxid)) {
            try {
                //删除待办任务
                Example exampleDbry = new Example(DchyXmglDbrw.class);
                exampleDbry.createCriteria().andEqualTo("sqxxid", sqxxid).andEqualTo("zrryid", zrryid);
                List<DchyXmglDbrw> dchyXmglDbrwList = entityMapper.selectByExample(exampleDbry);
                if (CollectionUtils.isNotEmpty(dchyXmglDbrwList)) {
                    DchyXmglDbrw dchyXmglDbrw = dchyXmglDbrwList.get(0);
                    if (StringUtils.isNotBlank(dchyXmglDbrw.getBlryid())) {
                        msg = ResponseMessage.CODE.PROCESS_FAIL.getMsg();
                        code = ResponseMessage.CODE.PROCESS_FAIL.getCode();
                    } else {
                        //申请信息重置为未审核状态
                        DchyXmglSqxx dchyXmglSqxx = new DchyXmglSqxx();
                        dchyXmglSqxx.setSqzt(Constants.DCHY_XMGL_SQZT_DSH);
                        dchyXmglSqxx.setSqxxid(sqxxid);
                        entityMapper.saveOrUpdate(dchyXmglSqxx, dchyXmglSqxx.getSqxxid());
                        entityMapper.deleteByPrimaryKey(DchyXmglDbrw.class, dchyXmglDbrw.getDbrwid());
                        msg = ResponseMessage.CODE.SUCCESS.getMsg();
                        code = ResponseMessage.CODE.SUCCESS.getCode();
                    }
                } else {
                    msg = ResponseMessage.CODE.NOTFIND_FAIL.getMsg();
                    code = ResponseMessage.CODE.NOTFIND_FAIL.getCode();
                }
            } catch (Exception e) {
                logger.error("待办任务取回失败 sqxxid {} {}", sqxxid, e);
                code = ResponseMessage.CODE.DELETE_FAIL.getCode();
                msg = ResponseMessage.CODE.DELETE_FAIL.getMsg();
            }
        } else {
            code = ResponseMessage.CODE.PARAMETER_FAIL.getCode();
            msg = ResponseMessage.CODE.PARAMETER_FAIL.getMsg();
        }
        map.put("msg", msg);
        map.put("code", code);
        return map;
    }

    @Override
    public synchronized Map<String, Object> insertDbrw(String zrryid, String sqxxid, String sqzt) {
        Map<String, Object> shbjResult = Maps.newHashMap();
        String code = ResponseMessage.CODE.DELETE_FAIL.getCode();
        String msg = ResponseMessage.CODE.DELETE_FAIL.getMsg();
        if (StringUtils.isBlank(sqzt)) {
            sqzt = Constants.DCHY_XMGL_SQZT_DSH;
        }
        if (StringUtils.isNoneEmpty(zrryid, sqxxid)) {
            /*获取配置项*/
            String flag = AppConfig.getProperty("entry.audit");
            Example example = new Example(DchyXmglDbrw.class);
            example.createCriteria().andEqualTo("sqxxid", sqxxid);
            List<DchyXmglDbrw> dchyXmglDbrwList = entityMapper.selectByExampleNotNull(example);
            if (CollectionUtils.isNotEmpty(dchyXmglDbrwList)) {
                /*常州地区无需审批，直接入驻*/
                if (StringUtils.isNotBlank(flag)) {
                /*
                true: 无需审核
                false:需要审核
                * */
                    if (StringUtils.equals("true", flag)) {
                        /*无需审核，直接执行办结任务*/
                        Map<String, Object> param = new HashMap<>();
                        param.put("rwid", dchyXmglDbrwList.get(0).getDbrwid());
                        param.put("sfth", "0");//是否退回
                        param.put("shyj", "");
                        /*自动办结*/
                        shbjResult = signService.shbj(param);
                    }
                } else {
                    msg = "该事项已申请";
                }
            } else {
                DchyXmglDbrw dchyXmglDbrw = new DchyXmglDbrw();
                dchyXmglDbrw.setDbrwid(UUIDGenerator.generate18());
                dchyXmglDbrw.setSqxxid(sqxxid);
                dchyXmglDbrw.setZrryid(zrryid);
                dchyXmglDbrw.setZrsj(new Date());
                dchyXmglDbrw.setDqjd("1");
                /*插入待办列表*/
                entityMapper.insertSelective(dchyXmglDbrw);

                DchyXmglSqxx dchyXmglSqxx = new DchyXmglSqxx();
                dchyXmglSqxx.setSqxxid(sqxxid);
                dchyXmglSqxx.setSqzt(sqzt);
                /*保存申请信息*/
                entityMapper.updateByPrimaryKeySelective(dchyXmglSqxx);
                code = ResponseMessage.CODE.SUCCESS.getCode();
                msg = ResponseMessage.CODE.SUCCESS.getMsg();

                if (StringUtils.isNotBlank(flag)) {
                /*
                true: 无需审核
                false:需要审核
                * */
                    if (StringUtils.equals("true", flag)) {
                        /*无需审核，直接执行办结任务*/
                        Map<String, Object> param = new HashMap<>();
                        param.put("rwid", dchyXmglDbrw.getDbrwid());
                        param.put("sfth", "0");//是否退回
                        param.put("shyj", "");
                        /*自动办结*/
                        shbjResult = signService.shbj(param);
                    }
                }
            }
        }
        shbjResult.put("bjMsg", msg);
        shbjResult.put("bjCode", code);
        return shbjResult;
    }
}
