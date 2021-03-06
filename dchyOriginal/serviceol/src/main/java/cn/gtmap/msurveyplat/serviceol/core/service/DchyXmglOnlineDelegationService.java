package cn.gtmap.msurveyplat.serviceol.core.service;

import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface DchyXmglOnlineDelegationService {


    /**
     * 多条件查询项目委托信息
     *
     * @param data
     * @return
     */
    Page<Map<String, Object>> queryProjectEntrustMultipleConditionsByPage(Map<String, Object> data);

    /**
     * 合同变更前获取合同文件信息
     * @param
     * @return
     */
    List<Map<String, Object>> getBeforeHtChange4Sjxx(String chxmid,String ssmkid);

    /**
     * 在线项目进度查看
     *
     * @param data
     * @return
     */
    List<Map<String, Object>> onlineRecordProcess(Map<String, Object> data);

    Map<String, Object> beforeVerificationInfo();

    Map<String, Object> onlineBaFilePreview(Map<String, Object> data);

    Page<Map<String, Object>> getHtbgRecord(Map<String,Object> data);

    /**
     * 测绘单位在线核验
     *
     * @param data
     * @return
     */
    int verification(Map<String, Object> data);

    /**
     * 获取待接受的委托项目数量
     *
     * @return
     */
    int getToBeAcceptedNum();

    /**
     * 获取核验意见
     *
     * @param data
     * @return
     */
    String getHyyjByChxmid(Map<String, Object> data);

    /**
     * 根据chxmid修改核验状态
     *
     * @param data
     * @return
     */
    int alterHtzyByChxmid(Map<String, Object> data);

    /**
     * 在线备案
     *
     * @param data
     * @return
     */
    int keepOnRecord(Map<String, Object> data);

    /**
     * 取消合同信息、合同删除
     * @param data
     * @return
     */
    int delHtwjAndSjxx(Map<String,Object> data);

    /**
     * 合同变更
     * @param data
     * @return
     */
    int contractChangeSyn(Map<String,Object> data);

    /**
     * 从线上备份库获取审核信息
     *
     * @param data
     * @return
     */
    String getAuditOpinion(Map<String, Object> data);

    /**
     * 备案上传合同前，初始化htxxid，用于获取上传的材料信息
     *
     * @param data
     * @return
     */
    String initHtxx(Map<String, Object> data);

    /**
     * 在线办结
     *
     * @param data
     * @return
     */
    ResponseMessage onlineComplete(Map<String, Object> data);

    /**
     * 在线办结前的成查状态检查
     *
     * @param data
     * @return
     */
    ResponseMessage onlineCompleteCheck(Map<String, Object> data);

    /**
     * 在线成果预览
     *
     * @param data
     * @return
     */
    ResponseMessage onlineGcPreview(Map<String, Object> data);

    /**
     * 在线成果预览子结点
     *
     * @param data
     * @return
     */
    ResponseMessage onlineGcPreviewById(Map<String, Object> data);

    /**
     * 线上下载前获取文件个数
     *
     * @param data
     * @return
     */
    ResponseMessage onlineGetUploadFileNums(Map<String, Object> data);

    /**
     * 线上下载成果包
     *
     * @return
     */
    ResponseEntity<byte[]> onlineGcDownload(HttpServletRequest request, HttpServletResponse response);
}
