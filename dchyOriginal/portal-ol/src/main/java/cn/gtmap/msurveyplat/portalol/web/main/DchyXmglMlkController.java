package cn.gtmap.msurveyplat.portalol.web.main;

import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.portalol.core.service.DchyXmglMlkService;
import cn.gtmap.msurveyplat.portalol.core.service.DchyXmglZdService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2020/12/2 16:20
 * @description
 */
@RestController
@RequestMapping("/mlk")
public class DchyXmglMlkController {

    protected final Log logger = LogFactory.getLog(getClass());
    @Autowired
    private DchyXmglMlkService mlkServer;
    @Autowired
    private DchyXmglZdService dchyXmglZdService;

    /**
     * 通过名称获取名录库信息
     */
    @PostMapping(value = "/getmlkbymc")
    public ResponseMessage getMlkByMc(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        try {
            /*条件为空时，进行全局分页查询*/
            Page<Map<String, Object>> mlksList = mlkServer.getMlkLikeMcByPage(data);
            Page<Map<String, Object>> maps = this.pageForMlks(mlksList);
            return ResponseUtil.wrapResponseBodyByPage(maps);

        } catch (Exception e) {
            logger.error("错误原因:{}", e);
            message = ResponseUtil.wrapResponseBodyByMsgCode(e.getMessage(), ResponseMessage.CODE.EXCEPTION_MGS.getCode());
        }
        return message;
    }

    /**
     * 根据mlkid查询mlk详情
     *
     * @param param
     * @return
     */
    @PostMapping(value = "/querymlkdetails")
    public ResponseMessage queryMlkDetails(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        try {
            List<Map<String, Object>> mapList = mlkServer.queryMlkDetails(data);
            return ResponseUtil.wrapResponseBodyByList(mapList);
        } catch (Exception e) {
            logger.error("错误信息:{}", e);
            message = ResponseUtil.wrapResponseBodyByMsgCode(e.getMessage(), ResponseMessage.CODE.EXCEPTION_MGS.getCode());
        }
        return message;
    }

    /**
     * 根据mlkid查询从业人员信息
     *
     * @param param
     * @return
     */
    @PostMapping(value = "/querycyrybymlkid")
    public ResponseMessage queryCyryByMlkid(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        try {
            Page<Map<String, Object>> cyryByMlkid = mlkServer.queryCyryByMlkid(data);
            return ResponseUtil.wrapResponseBodyByPage(cyryByMlkid);
        } catch (Exception e) {
            logger.error("错误信息:{}", e);
            message = ResponseUtil.wrapResponseBodyByMsgCode(e.getMessage(), ResponseMessage.CODE.EXCEPTION_MGS.getCode());
        }
        return message;
    }

    /**
     * 根据mlkid查询从业人员信息
     *
     * @param param
     * @return
     */
    @PostMapping(value = "/querycxjlbymlkid")
    public ResponseMessage querycxjlbymlkid(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        try {
            Page<Map<String, Object>> cyryByMlkid = mlkServer.queryCxjlByMlkid(data);
            return ResponseUtil.wrapResponseBodyByPage(cyryByMlkid);
        } catch (Exception e) {
            logger.error("错误信息:{}", e);
            message = ResponseUtil.wrapResponseBodyByMsgCode(e.getMessage(), ResponseMessage.CODE.EXCEPTION_MGS.getCode());
        }
        return message;
    }

    /**
     * 对结果进行分页
     *
     * @param mlksList
     * @return
     */
    private Page<Map<String, Object>> pageForMlks(Page<Map<String, Object>> mlksList) {
        if (CollectionUtils.isNotEmpty(mlksList.getContent())) {
            List<Map<String, Object>> mlks = mlksList.getContent();
            for (Map<String, Object> mlk : mlks) {
                mlk.put("DWXZMC", dchyXmglZdService.getDchyXmglByZdlxAndDm("DWXZ", (String) mlk.get("DWXZ")).getMc());
                mlk.put("ZZDJMC", dchyXmglZdService.getDchyXmglByZdlxAndDm("ZZDJ", (String) mlk.get("ZZDJ")).getMc());
                mlk.put("PJ", mlkServer.getKpResultByMlkId(MapUtils.getString(mlk, "MLKID")));
            }
        }
        return mlksList;
    }


}
