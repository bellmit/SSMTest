package cn.gtmap.msurveyplat.common.util;

import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglZd;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2020/12/2 9:10
 * @description
 */
public class ResponseUtil {

    /**
     * 包装分页查询结果集
     *
     * @param dataPaging
     * @return
     */
    public static ResponseMessage wrapResponseBodyByPage(Page<Map<String, Object>> dataPaging) {
        ResponseMessage message = new ResponseMessage();
        if (null != dataPaging) {
            if (CollectionUtils.isNotEmpty(dataPaging.getContent())) {
                message = wrapSuccessResponse();
                message.getData().put("dataList", dataPaging.getContent());
                message.getData().put("totalPage", dataPaging.getTotalPages());
                message.getData().put("totalNum", dataPaging.getTotalElements());
            } else {
                message.getHead().setMsg("未查询到数据");
                message.getHead().setCode(ResponseMessage.CODE.SUCCESS.getCode());
            }
        } else {
            message.getHead().setMsg("未查询到数据");
            message.getHead().setCode(ResponseMessage.CODE.SUCCESS.getCode());
        }

        return message;
    }

    public static ResponseMessage wrapResponseLogPage(Map dataPaging) {
        ResponseMessage message = new ResponseMessage();
        if (MapUtils.isNotEmpty(dataPaging)) {
            message = wrapSuccessResponse();
            message.getData().put("dataList", MapUtils.getObject(dataPaging, "content"));
            message.getData().put("totalPage", MapUtils.getObject(dataPaging, "totalPages"));
            message.getData().put("totalNum", MapUtils.getObject(dataPaging, "totalElements"));
        } else {
            message.getHead().setMsg("未查询到数据");
            message.getHead().setCode(ResponseMessage.CODE.SUCCESS.getCode());
        }
        return message;
    }

    /**
     * 包装新增对象返回操作
     *
     * @param result 通过entityMapper新增后的返回值
     * @return
     */
    public static ResponseMessage wrapResponseBody(int result, Object... data) {
        ResponseMessage message = new ResponseMessage();
        if (result > 0) {
            message = wrapSuccessResponse();
            message.getData().put("dataList", data);
        } else {
            message.getHead().setMsg("未查询到数据");
            message.getHead().setCode(ResponseMessage.CODE.SUCCESS.getCode());
        }
        return message;
    }

    /**
     * 包装返回集合对象
     *
     * @param resultMap
     * @return
     */
    public static ResponseMessage wrapResponseBodyByList(List<Map<String, Object>> resultMap) {
        ResponseMessage message = new ResponseMessage();
        if (CollectionUtils.isNotEmpty(resultMap)) {
            message = wrapSuccessResponse();
            message.getData().put("dataList", resultMap);
        } else {
            message.getHead().setMsg("未查询到数据");
            message.getHead().setCode(ResponseMessage.CODE.SUCCESS.getCode());
        }
        return message;
    }

    /**
     * 包装字典项返回对象
     *
     * @param dchyXmglZdList
     * @return
     */
    public static ResponseMessage wrapResponseBodyByZdList(List<DchyXmglZd> dchyXmglZdList) {
        ResponseMessage message = new ResponseMessage();
        if (CollectionUtils.isNotEmpty(dchyXmglZdList)) {
            message = wrapSuccessResponse();
            message.getData().put("dataList", dchyXmglZdList);
        } else {
            message.getHead().setMsg(ResponseMessage.CODE.QUERY_NULL.getMsg());
            message.getHead().setCode(ResponseMessage.CODE.QUERY_NULL.getCode());
        }
        return message;
    }

    /**
     * 包装返回集合对象
     *
     * @param resultMap
     * @return
     */
    public static ResponseMessage wrapResponseBodyByObjectList(List<Object> resultMap) {
        ResponseMessage message = new ResponseMessage();
        if (CollectionUtils.isNotEmpty(resultMap)) {
            message = wrapSuccessResponse();
            message.getData().put("dataList", resultMap);
        } else {
            message.getHead().setMsg(ResponseMessage.CODE.QUERY_NULL.getMsg());
            message.getHead().setCode(ResponseMessage.CODE.QUERY_NULL.getCode());
        }
        return message;
    }

    /**
     * 包装分页查询结果集
     *
     * @param resList
     * @return
     */
    public static ResponseMessage wrapResponseBodyByPageNew(Page<List<Map<String, String>>> resList) {
        ResponseMessage message = new ResponseMessage();
        if (CollectionUtils.isNotEmpty(resList.getContent())) {
            message = wrapSuccessResponse();
            message.getData().put("dataList", resList.getContent());
            message.getData().put("totalNum", resList.getContent().size());
        } else {
            message.getHead().setMsg(ResponseMessage.CODE.QUERY_NULL.getMsg());
            message.getHead().setCode(ResponseMessage.CODE.QUERY_NULL.getCode());
        }
        return message;
    }

    /**
     * 包装增删改返回对象
     *
     * @param flag
     * @return
     */
    public static ResponseMessage wrapResponseBodyByCRUD(int flag) {
        ResponseMessage message = new ResponseMessage();
        if (flag > 0) {
            message = wrapSuccessResponse();
        } else {
            message.getHead().setMsg(ResponseMessage.CODE.QUERY_NULL.getMsg());
            message.getHead().setCode(ResponseMessage.CODE.QUERY_NULL.getCode());
        }
        return message;
    }

    /**
     * 包装返回集合对象
     *
     * @return
     */
    public static ResponseMessage wrapResponseBodyByObject(String objName, List<Map<String, Object>> resultMap) {
        ResponseMessage message = new ResponseMessage();
        if (CollectionUtils.isNotEmpty(resultMap)) {
            message = wrapSuccessResponse();
            message.getData().put(objName, resultMap);
        } else {
            message.getHead().setMsg(ResponseMessage.CODE.QUERY_NULL.getMsg());
            message.getHead().setCode(ResponseMessage.CODE.QUERY_NULL.getCode());
        }
        return message;
    }

    public static void main(String[] args) {
        List<Map<String, Map>> list = new ArrayList<>();


        System.out.println(CollectionUtils.isNotEmpty(list));
        System.out.println(list.size());
    }

    public static ResponseMessage wrapResponseBodyByMsgCode(String msg, String code) {
        ResponseMessage message = new ResponseMessage();
        message.getHead().setMsg(msg);
        message.getHead().setCode(code);
        return message;
    }

    public static ResponseMessage wrapResponseBodyByCodeMap(Map<String, String> result) {
        ResponseMessage message = new ResponseMessage();
        message.getHead().setMsg(MapUtils.getString(result, "msg"));
        message.getHead().setCode(MapUtils.getString(result, "code"));
        return message;
    }

    public static ResponseMessage wrapSuccessResponse() {
        ResponseMessage message = new ResponseMessage();
        message.getHead().setMsg(ResponseMessage.CODE.SUCCESS.getMsg());
        message.getHead().setCode(ResponseMessage.CODE.SUCCESS.getCode());
        return message;
    }

    public static ResponseMessage wrapParamEmptyFailResponse() {
        ResponseMessage message = new ResponseMessage();
        message.getHead().setMsg(ResponseMessage.CODE.PARAMEMPTY_FAIL.getMsg());
        message.getHead().setCode(ResponseMessage.CODE.PARAMEMPTY_FAIL.getCode());
        return message;
    }

    public static ResponseMessage wrapExceptionResponse(Exception e) {
        ResponseMessage message = new ResponseMessage();
        if (StringUtils.isNoneBlank(e.getMessage())) {
            message.getHead().setMsg(e.getMessage());
        } else {
            message.getHead().setMsg(ResponseMessage.CODE.EXCEPTION_MGS.getMsg());
        }
        message.getHead().setCode(ResponseMessage.CODE.EXCEPTION_MGS.getCode());
        return message;
    }

    public static ResponseMessage wrapExceptionResponse() {
        ResponseMessage message = new ResponseMessage();
        message.getHead().setMsg(ResponseMessage.CODE.EXCEPTION_MGS.getMsg());
        message.getHead().setCode(ResponseMessage.CODE.EXCEPTION_MGS.getCode());
        return message;
    }
}
