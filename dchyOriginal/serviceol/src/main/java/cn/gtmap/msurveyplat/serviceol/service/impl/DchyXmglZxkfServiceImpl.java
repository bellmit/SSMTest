package cn.gtmap.msurveyplat.serviceol.service.impl;

import cn.gtmap.msurveyplat.common.annotion.SystemLog;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.core.support.mybatis.page.repository.Repository;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.*;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.CalendarUtil;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.common.util.ProLog;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.common.vo.PfUserRoleRel;
import cn.gtmap.msurveyplat.serviceol.core.mapper.DchyXmglZxkfMapper;
import cn.gtmap.msurveyplat.serviceol.service.DchyXmglZxkfService;
import cn.gtmap.msurveyplat.serviceol.utils.Constants;
import cn.gtmap.msurveyplat.serviceol.utils.UserUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Blob;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2021/4/8
 * @description 在线客服逻辑业务层
 */
@Service
public class DchyXmglZxkfServiceImpl implements DchyXmglZxkfService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DchyXmglZxkfServiceImpl.class);

    @Resource(name = "entityMapper")
    private EntityMapper entityMapper;

    @Autowired
    private Repository repository;

    @Autowired
    private DchyXmglZxkfMapper dchyXmglZxkfMapper;

    @Override
    @SystemLog(czmkMc = ProLog.CZMC_ZXKF_MC, czmkCode = ProLog.CZMC_ZXKF_CODE, czlxMc = ProLog.CZLX_CREATE_MC, czlxCode = ProLog.CZLX_CREATE_CODE, ssmkid = ProLog.SSMKID_ZXKF_CODE)
    public ResponseMessage initIssues() {
        ResponseMessage message;
        DchyXmglSqxx dchyXmglSqxx = new DchyXmglSqxx();
        DchyXmglDbrw dchyXmglDbrw = new DchyXmglDbrw();
        DchyXmglZxkfIssues dchyXmglZxkfIssues = new DchyXmglZxkfIssues();
        String userid = UserUtil.getCurrentUserId();
        String roleid = queryRoleidByUserid(userid);
        DchyXmglYhdw dchyXmglYhdw = queryYhdwByUserid(userid);
        if (null != dchyXmglYhdw && StringUtils.isNotBlank(dchyXmglYhdw.getDwmc())) {
            dchyXmglSqxx.setSqjgmc(dchyXmglYhdw.getDwmc());
        }

        //初始化申请信息
        dchyXmglSqxx.setSqxxid(UUIDGenerator.generate18());
        dchyXmglSqxx.setGlsxid(UUIDGenerator.generate18());
        dchyXmglSqxx.setSqzt(Constants.DCHY_XMGL_SQZT_DSH);
        dchyXmglSqxx.setSqsj(CalendarUtil.getCurHMSDate());
        dchyXmglSqxx.setSqr(userid);
        dchyXmglSqxx.setSqrmc(UserUtil.getCurrentUser().getUsername());
        dchyXmglSqxx.setBlsx(Constants.BLSX_ZXKF);
        dchyXmglSqxx.setSqbh(Calendar.getInstance().getTimeInMillis() + dchyXmglSqxx.getSqxxid());

        //初始化待办任务
        dchyXmglDbrw.setDbrwid(UUIDGenerator.generate18());
        dchyXmglDbrw.setBlryid(userid);
        dchyXmglDbrw.setSqxxid(dchyXmglSqxx.getSqxxid());
        dchyXmglDbrw.setZrsj(CalendarUtil.getCurHMSDate());
        dchyXmglDbrw.setBljsid(roleid);

        //初始化提问
        dchyXmglZxkfIssues.setIssuesId(dchyXmglSqxx.getGlsxid());
        dchyXmglZxkfIssues.setCreateTime(CalendarUtil.getCurHMSDate());
        dchyXmglZxkfIssues.setIsOpen(Constants.DCHY_XMGL_ZXKF_SFGK_GK);
        dchyXmglZxkfIssues.setStatus(Constants.DCHY_XMGL_ZXKF_SFHF_DHF);
        dchyXmglZxkfIssues.setUserId(userid);

        int flag1 = entityMapper.saveOrUpdate(dchyXmglDbrw, dchyXmglDbrw.getDbrwid());
        int flag2 = entityMapper.saveOrUpdate(dchyXmglSqxx, dchyXmglSqxx.getSqxxid());
        int flag3 = entityMapper.saveOrUpdate(dchyXmglZxkfIssues, dchyXmglZxkfIssues.getIssuesId());

        if (flag1 > 0 && flag2 > 0 && flag3 > 0) {
            message = ResponseUtil.wrapSuccessResponse();
            message.getData().put("data", dchyXmglZxkfIssues.getIssuesId());
        } else {
            message = ResponseUtil.wrapExceptionResponse();
        }
        return message;
    }

    @Override
    @SystemLog(czmkMc = ProLog.CZMC_ZXKF_MC, czmkCode = ProLog.CZMC_ZXKF_CODE, czlxMc = ProLog.CZLX_DELETE_MC, czlxCode = ProLog.CZLX_DELETE_CODE, ssmkid = ProLog.SSMKID_ZXKF_CODE)
    public ResponseMessage delIssues(Map<String, Object> map) {
        ResponseMessage message;
        String issuesId = CommonUtil.formatEmptyValue(MapUtils.getString(map, "issuesid"));
        if (StringUtils.isNotBlank(issuesId)) {
            entityMapper.deleteByPrimaryKey(DchyXmglZxkfIssues.class, issuesId);
            message = ResponseUtil.wrapSuccessResponse();
        } else {
            message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.PARAMETER_FAIL.getMsg(), ResponseMessage.CODE.PARAMETER_FAIL.getCode());
        }
        return message;
    }

    @Override
    @SystemLog(czmkMc = ProLog.CZMC_ZXKF_MC, czmkCode = ProLog.CZMC_ZXKF_CODE, czlxMc = ProLog.CZLX_SAVE_MC, czlxCode = ProLog.CZLX_SAVE_CODE, ssmkid = ProLog.SSMKID_ZXKF_CODE)
    public ResponseMessage saveIssues(Map<String, Object> map) {
        ResponseMessage message;
        String issuesId = CommonUtil.formatEmptyValue(MapUtils.getString(map, "issuesid"));
        String title = CommonUtil.formatEmptyValue(MapUtils.getString(map, "title"));
        String content = CommonUtil.formatEmptyValue(MapUtils.getString(map, "content"));

        if (StringUtils.isNoneBlank(issuesId, title, content)) {
            DchyXmglZxkfIssues dchyXmglZxkfIssues = entityMapper.selectByPrimaryKey(DchyXmglZxkfIssues.class, issuesId);
            if (null == dchyXmglZxkfIssues) {
                dchyXmglZxkfIssues = new DchyXmglZxkfIssues();
                dchyXmglZxkfIssues.setIssuesId(issuesId);
            }
            dchyXmglZxkfIssues.setTitle(title);
            dchyXmglZxkfIssues.setCreateTime(CalendarUtil.getCurHMSDate());
            dchyXmglZxkfIssues.setSfyx(Constants.VALID);
            dchyXmglZxkfIssues.setIssuesContent(content.getBytes(Charsets.UTF_8));
            entityMapper.saveOrUpdate(dchyXmglZxkfIssues, issuesId);
            message = ResponseUtil.wrapSuccessResponse();
        } else {
            message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.PARAMETER_FAIL.getMsg(), ResponseMessage.CODE.PARAMETER_FAIL.getCode());
        }
        return message;
    }

    @Override
    @SystemLog(czmkMc = ProLog.CZMC_ZXKF_MC, czmkCode = ProLog.CZMC_ZXKF_CODE, czlxMc = ProLog.CZLX_SELECT_MC, czlxCode = ProLog.CZLX_SELECT_CODE, ssmkid = ProLog.SSMKID_ZXKF_CODE)
    public ResponseMessage queryMessageList(Map<String, Object> map) {
        int page = Integer.parseInt(map.get("page") != null ? map.get("page").toString() : Constants.DCHY_XMGL_PAGINATION_PAGE);
        int pageSize = Integer.parseInt(map.get("size") != null ? map.get("size").toString() : Constants.DCHY_XMGL_PAGINATION_SIZE);

        List<String> useridList = queryUserListByUserid();
        //如果当前用户没有用户单位直接查询为空
        if (CollectionUtils.isEmpty(useridList)) {
            useridList.add(Constants.EMPTYPARAM_VALUE);
        }
        map.put("useridList", useridList);
        map.put("isOpen", Constants.DCHY_XMGL_ZXKF_SFGK_GK);
        Page<Map<String, Object>> queryIssuesListByPage = repository.selectPaging("queryMessageListByPage", map, page - 1, pageSize);
        formatBytesToString(queryIssuesListByPage);
        return ResponseUtil.wrapResponseBodyByPage(queryIssuesListByPage);
    }

    @Override
    @SystemLog(czmkMc = ProLog.CZMC_ZXKF_MC, czmkCode = ProLog.CZMC_ZXKF_CODE, czlxMc = ProLog.CZLX_SELECT_MC, czlxCode = ProLog.CZLX_SELECT_CODE, ssmkid = ProLog.SSMKID_ZXKF_CODE)
    public ResponseMessage queryGldwMessageList(Map<String, Object> map) {
        int page = Integer.parseInt(map.get("page") != null ? map.get("page").toString() : Constants.DCHY_XMGL_PAGINATION_PAGE);
        int pageSize = Integer.parseInt(map.get("size") != null ? map.get("size").toString() : Constants.DCHY_XMGL_PAGINATION_SIZE);
        Page<Map<String, Object>> queryIssuesListByPage = repository.selectPaging("queryMessageListByPage", map, page - 1, pageSize);
        formatBytesToString(queryIssuesListByPage);
        return ResponseUtil.wrapResponseBodyByPage(queryIssuesListByPage);
    }

    @Override
    @SystemLog(czmkMc = ProLog.CZMC_ZXKF_MC, czmkCode = ProLog.CZMC_ZXKF_CODE, czlxMc = ProLog.CZLX_SELECT_MC, czlxCode = ProLog.CZLX_SELECT_CODE, ssmkid = ProLog.SSMKID_ZXKF_CODE)
    public ResponseMessage queryMyIssuesList(Map<String, Object> map) {
        int page = Integer.parseInt(map.get("page") != null ? map.get("page").toString() : Constants.DCHY_XMGL_PAGINATION_PAGE);
        int pageSize = Integer.parseInt(map.get("size") != null ? map.get("size").toString() : Constants.DCHY_XMGL_PAGINATION_SIZE);

        List<String> useridList = queryUserListByUserid();
        map.put("useridList", useridList);
        Page<Map<String, Object>> queryMyIssuesListByPage = repository.selectPaging("queryMyIssuesListByPage", map, page - 1, pageSize);
        formatBytesToString(queryMyIssuesListByPage);
        return ResponseUtil.wrapResponseBodyByPage(queryMyIssuesListByPage);
    }

    @Override
    @SystemLog(czmkMc = ProLog.CZMC_ZXKF_MC, czmkCode = ProLog.CZMC_ZXKF_CODE, czlxMc = ProLog.CZLX_SELECT_MC, czlxCode = ProLog.CZLX_SELECT_CODE, ssmkid = ProLog.SSMKID_ZXKF_CODE)
    public ResponseMessage queryIssuesByid(Map<String, Object> map) {
        ResponseMessage message = new ResponseMessage();
        String issuesId = CommonUtil.formatEmptyValue(MapUtils.getString(map, "issuesid"));
        if (StringUtils.isNotBlank(issuesId)) {
            DchyXmglZxkfIssues dchyXmglZxkfIssues = dchyXmglZxkfMapper.queryIssuesById(map);

            String status = dchyXmglZxkfIssues.getStatus();
            if (StringUtils.equals(status, Constants.DCHY_XMGL_ZXKF_SFHF_DHF)) {
                String twr = UserUtil.getUserNameById(dchyXmglZxkfIssues.getUserId());

                entityMapper.saveOrUpdate(dchyXmglZxkfIssues, dchyXmglZxkfIssues.getIssuesId());
                //初始化一条答复信息

                byte[] bytes = dchyXmglZxkfIssues.getIssuesContent();
                String json = JSONObject.toJSONString(dchyXmglZxkfIssues);
                Map<String, Object> resultMap = JSONObject.parseObject(json, Map.class);
                String issuesContent = new String(bytes, Charsets.UTF_8);
                resultMap.put("issuesContent", issuesContent);
                resultMap.put("twr", twr);

                message = ResponseUtil.wrapSuccessResponse();
                message.getData().put("data", resultMap);
            } else {
                message.getHead().setCode(ResponseMessage.CODE.SUCCESS.getCode());
                message.getHead().setMsg("该提问已经被回复!");
            }
        } else {
            message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.PARAMETER_FAIL.getMsg(), ResponseMessage.CODE.PARAMETER_FAIL.getCode());
        }
        return message;
    }

    @Override
    @SystemLog(czmkMc = ProLog.CZMC_ZXKF_MC, czmkCode = ProLog.CZMC_ZXKF_CODE, czlxMc = ProLog.CZLX_SAVE_MC, czlxCode = ProLog.CZLX_SAVE_CODE, ssmkid = ProLog.SSMKID_ZXKF_CODE)
    public ResponseMessage saveAnswer(Map<String, Object> map) {
        ResponseMessage message;
        String issuesid = CommonUtil.formatEmptyValue(MapUtils.getString(map, "issuesid"));
        String answerContent = CommonUtil.formatEmptyValue(MapUtils.getString(map, "content"));
        String isOpen = CommonUtil.formatEmptyValue(MapUtils.getString(map, "isOpen"));

        if (StringUtils.isNoneBlank(issuesid, answerContent)) {
            DchyXmglZxkfAnswer dchyXmglZxkfAnswer = new DchyXmglZxkfAnswer();
            dchyXmglZxkfAnswer.setAnswerId(UUIDGenerator.generate18());
            dchyXmglZxkfAnswer.setIssuesId(issuesid);
            dchyXmglZxkfAnswer.setUserId(UserUtil.getCurrentUserId());

            dchyXmglZxkfAnswer.setAnswerContent(answerContent.getBytes(Charsets.UTF_8));
            dchyXmglZxkfAnswer.setCreateTime(CalendarUtil.getCurHMSDate());
            int flag = entityMapper.saveOrUpdate(dchyXmglZxkfAnswer, dchyXmglZxkfAnswer.getAnswerId());
            if (flag > 0) {
                DchyXmglZxkfIssues dchyXmglZxkfIssues = dchyXmglZxkfMapper.queryIssuesById(map);
                if (null == dchyXmglZxkfIssues) {
                    dchyXmglZxkfIssues = new DchyXmglZxkfIssues();
                    dchyXmglZxkfAnswer.setIssuesId(issuesid);
                }
                dchyXmglZxkfIssues.setIsOpen(isOpen);
                //修改为已回复
                dchyXmglZxkfIssues.setStatus(Constants.DCHY_XMGL_ZXKF_SFHF_YHF);
                entityMapper.saveOrUpdate(dchyXmglZxkfIssues, issuesid);
                delDbrw(issuesid);
            }
            message = ResponseUtil.wrapSuccessResponse();

        } else {
            message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.PARAMETER_FAIL.getMsg(), ResponseMessage.CODE.PARAMETER_FAIL.getCode());
        }
        return message;
    }

    @Override
    @SystemLog(czmkMc = ProLog.CZMC_ZXKF_MC, czmkCode = ProLog.CZMC_ZXKF_CODE, czlxMc = ProLog.CZLX_SELECT_MC, czlxCode = ProLog.CZLX_SELECT_CODE, ssmkid = ProLog.SSMKID_ZXKF_CODE)
    public ResponseMessage queryAnswerList(Map<String, Object> map) {
        int page = Integer.parseInt(map.get("page") != null ? map.get("page").toString() : Constants.DCHY_XMGL_PAGINATION_PAGE);
        int pageSize = Integer.parseInt(map.get("size") != null ? map.get("size").toString() : Constants.DCHY_XMGL_PAGINATION_SIZE);
        Page<Map<String, Object>> queryIssuesListByPage = repository.selectPaging("queryAnswerListByPage", map, page - 1, pageSize);
        formatBytesToString(queryIssuesListByPage);
        return ResponseUtil.wrapResponseBodyByPage(queryIssuesListByPage);
    }

    @Override
    @SystemLog(czmkMc = ProLog.CZMC_ZXKF_MC, czmkCode = ProLog.CZMC_ZXKF_CODE, czlxMc = ProLog.CZLX_SELECT_MC, czlxCode = ProLog.CZLX_SELECT_CODE, ssmkid = ProLog.SSMKID_ZXKF_CODE)
    public DchyXmglYhdw queryYhdwByUserid(String userid) {
        DchyXmglYhdw dchyXmglYhdw = new DchyXmglYhdw();
        if (StringUtils.isNotBlank(userid)) {
            Example exampleYhdw = new Example(DchyXmglYhdw.class);
            exampleYhdw.createCriteria().andEqualTo("yhid", userid);
            List<DchyXmglYhdw> dchyXmglYhdwList = entityMapper.selectByExample(exampleYhdw);
            if (CollectionUtils.isNotEmpty(dchyXmglYhdwList)) {
                dchyXmglYhdw = dchyXmglYhdwList.get(0);
            }
        }

        return dchyXmglYhdw;
    }

    @Override
    @SystemLog(czmkMc = ProLog.CZMC_ZXKF_MC, czmkCode = ProLog.CZMC_ZXKF_CODE, czlxMc = ProLog.CZLX_SELECT_MC, czlxCode = ProLog.CZLX_SELECT_CODE, ssmkid = ProLog.SSMKID_ZXKF_CODE)
    public String queryRoleidByUserid(String userid) {
        String roleid = "";
        if (StringUtils.isNotBlank(userid)) {
            Example exampleRole = new Example(PfUserRoleRel.class);
            exampleRole.createCriteria().andEqualTo("user_id", userid);
            List<PfUserRoleRel> pfUserRoleRels = entityMapper.selectByExample(exampleRole);
            if (CollectionUtils.isNotEmpty(pfUserRoleRels)) {
                roleid = pfUserRoleRels.get(0).getRole_id();
            }
        }
        return roleid;
    }

    @Override
    @SystemLog(czmkMc = ProLog.CZMC_ZXKF_MC, czmkCode = ProLog.CZMC_ZXKF_CODE, czlxMc = ProLog.CZLX_DELETE_MC, czlxCode = ProLog.CZLX_DELETE_CODE, ssmkid = ProLog.SSMKID_ZXKF_CODE)
    public void delDbrw(String issuesid) {
        if (StringUtils.isNotBlank(issuesid)) {
            Example examplesqxx = new Example(DchyXmglSqxx.class);
            examplesqxx.createCriteria().andEqualTo("glsxid", issuesid);
            List<DchyXmglSqxx> dchyXmglSqxxList = entityMapper.selectByExample(examplesqxx);
            if (CollectionUtils.isNotEmpty(dchyXmglSqxxList)) {
                DchyXmglSqxx dchyXmglSqxx = dchyXmglSqxxList.get(0);
                String sqxxid = dchyXmglSqxx.getSqxxid();

                Example exampleDbrw = new Example(DchyXmglDbrw.class);
                exampleDbrw.createCriteria().andEqualTo("sqxxid", sqxxid);
                entityMapper.deleteByExample(exampleDbrw);

                DchyXmglYbrw dchyXmglYbrw = new DchyXmglYbrw();
                dchyXmglYbrw.setYbrwid(UUIDGenerator.generate18());
                dchyXmglYbrw.setSqxxid(sqxxid);
                dchyXmglYbrw.setJssj(CalendarUtil.getCurHMSDate());
                dchyXmglYbrw.setBlryid(UserUtil.getCurrentUserId());

                entityMapper.saveOrUpdate(dchyXmglYbrw, dchyXmglYbrw.getYbrwid());
            }
        }
    }

    @Override
    public void formatBytesToString(Page<Map<String, Object>> pageListByPage) {
        List<Map<String, Object>> pageList = pageListByPage.getContent();
        if (CollectionUtils.isNotEmpty(pageList)) {
            for (Map<String, Object> map : pageList) {
                try {
                    if (null != map.get("ANSWERCONTENT")) {
                        Blob b = (Blob) map.get("ANSWERCONTENT");
                        String blobString = new String(b.getBytes(1, (int) b.length()), Charsets.UTF_8);//blob 转 String
                        map.put("ANSWERCONTENT", blobString);
                    }

                    if (null != map.get("ISSUESCONTENT")) {
                        Blob b = (Blob) map.get("ISSUESCONTENT");
                        String blobString = new String(b.getBytes(1, (int) b.length()), Charsets.UTF_8);//blob 转 String
                        map.put("ISSUESCONTENT", blobString);
                    }
                } catch (Exception e) {
                    LOGGER.error("错误原因{}:", e);
                }
            }
        }
    }

    /**
     * @return
     * @description 2021/5/17 获取当前用户并通过当前用户获取其所在单位的所有用户
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @Override
    @SystemLog(czmkMc = ProLog.CZMC_ZXKF_MC, czmkCode = ProLog.CZMC_ZXKF_CODE, czlxMc = ProLog.CZLX_SELECT_MC, czlxCode = ProLog.CZLX_SELECT_CODE, ssmkid = ProLog.SSMKID_ZXKF_CODE)
    public List<String> queryUserListByUserid() {
        List<String> useridList = Lists.newArrayList();
        DchyXmglYhdw dchyXmglYhdw = queryYhdwByUserid(UserUtil.getCurrentUserId());

        //根据当前用户获取当前用户所在的单位编号,然后获取该单位下所有的提问
        if (null != dchyXmglYhdw && StringUtils.isNotBlank(dchyXmglYhdw.getDwbh())) {
            String dwbh = dchyXmglYhdw.getDwbh();
            Example exampleYhdw = new Example(DchyXmglYhdw.class);
            exampleYhdw.createCriteria().andEqualTo("dwbh", dwbh);
            List<DchyXmglYhdw> dchyXmglYhdws = entityMapper.selectByExample(exampleYhdw);
            if (CollectionUtils.isNotEmpty(dchyXmglYhdws)) {
                for (DchyXmglYhdw yhdw : dchyXmglYhdws) {
                    useridList.add(yhdw.getYhid());
                }
            }
        }
        return useridList;
    }
}
