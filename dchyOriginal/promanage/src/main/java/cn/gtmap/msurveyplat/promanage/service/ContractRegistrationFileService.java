package cn.gtmap.msurveyplat.promanage.service;

import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglClcg;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglClcgpz;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface ContractRegistrationFileService {

    /**
     * 分页多条件查询检索合同登记备案信息
     *
     * @param data
     * @return
     */
    ResponseMessage getContractRegisterFile(Map<String, Object> data);

    /**
     * 检查项目成果状态
     *
     * @param data
     * @return
     */
    Map<String, Object> checkProjectArchStatus(Map<String, Object> data);


    /**
     * 项目办结
     *
     * @param data
     */
    int projectComplete(Map<String, Object> data);

    /**
     * 修改测量事项状态
     *
     * @param data
     * @return
     */
    int alterClsxZt(Map<String, Object> data);

    /**
     * @param map
     * @return
     * @description 2020/12/30 合同登记备案页面测绘事项台账
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    Page<Map<String, Object>> getCzsxList(Map<String, Object> map);

    /**
     * @param map
     * @return
     * @description 2020/12/30 合同登记备案页面测绘事项操作日志台账
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    Page<Map<String, Object>> getCzrzList(Map<String, Object> map);

    /**
     * @param param
     * @return
     * @description 2020/12/30 修改测绘状态并记录操作日志
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    int changeCzzt(Map<String, Object> param);

    /**
     * @param clsxList
     * @return
     * @description 2020/12/31 同步测量事项到线上
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    void getClsxList(List<Map<String, String>> clsxList);

    /**
     * 获取项目管理列表
     *
     * @param data
     * @return
     */
    Page<Map<String, Object>> getProjectManagerList(Map<String, Object> data);

    /**
     * 根据chgcid获取项目工建详情
     *
     * @param data
     * @return
     */
    List<Map<String, Object>> getProjectConstruct(Map<String, Object> data);

    /**
     * 根据chgcbh,clsx获取项目工建详情中测量事项的审核详情
     *
     * @param data
     * @return
     */
    List<Map<String, Object>> getProjectClsxInfo(Map<String, Object> data);

    /**
     * 文件查看
     *
     * @param data
     * @return
     */
    Map<String, Object> viewattachments(Map<String, Object> data);

    /**
     * 文件查看
     *
     * @param data
     * @return
     */
    Map<String, Object> viewChildAttachments(Map data);

    Map<String, Object> onlineBaFilePreview(Map<String, Object> data);

    /**
     * @Description: 测绘工程下所有测绘项目测量成果文件查看
     * @param: data
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @date 2021/6/17 10:13
    */
    Map<String, Object> chgcclcgByChxm(Map data);

    /**
     * 文件查看
     *
     * @param data
     * @return
     */
    Map<String, Object> viewattachments2(Map data);

    /**
     * @param data
     * @return
     * @description 2021/6/3 成果浏览,去掉审核通过这个条件
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    Map<String, Object> viewattachments2ByCgsh(Map data);

    /**
     * @param map
     * @return
     * @description 2021/1/29  过滤成果包
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    Map<String, Object> viewattachmentsByClsx(Map<String, String> map);

    //从配置表中读取zip文件目录  和表中配置文件的目录
    Map<String, Object> generateClml();

    //用递归取出clcgpz表中的成果包的配置
    Map<String, Object> generateDir(DchyXmglClcgpz dchyXmglClcgpz, Map<String, Object> mlMap);

    //生成clcg的目录
    Map<String, Object> generateClcgMl(List<DchyXmglClcg> dchyXmglClcgList);
}
