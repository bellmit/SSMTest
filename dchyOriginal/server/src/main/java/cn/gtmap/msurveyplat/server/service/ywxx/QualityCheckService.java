package cn.gtmap.msurveyplat.server.service.ywxx;

import cn.gtmap.msurveyplat.common.domain.DchyCgglZjqtcwDO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
 * @version 2020/3/10
 * @description 质检信息接口
 */
public interface QualityCheckService {
    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param  cwList 其他错误
     * @return
     * @description 新增其他错误
     * */
    void xzqtcw(List<DchyCgglZjqtcwDO> cwList);



    /**
     * @author <a href="mailto:wangyang@gtmap.cn">wangyang</a>
     * @param  cwid 其他错误
     * @return
     * @description 新增其他错误
     * */
    Map<String,Object> getqtcwxx(Map<String,String> cwid);


    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param cwidList 错误id
     * @return
     * @description
     * */
    void delQtcw(List<String> cwidList);

    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @return 检查类型
     * @description 获取检查类型
     * */
    List<Map<String, Object>> getJclx(String xmbh);

    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param
     * @return
     * @description 更新其他错误
     * */
    void updateQtcw(DchyCgglZjqtcwDO cwList);

    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param
     * @return
     * @description 获取字典表  错误级别
     * */
    List<Map<String, String>> getCwjb();

    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param xmbh  项目编号
     * @return 检查内容
     * @description 获取检查内容
     * */
    Map<String, String> getJcnr(String xmbh);

    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @return 检查类型
     * @description 获取检查类型
     * */
    List<Map<String, Object>> getJclxTotal(String xmbh);

    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param xmbh 项目编号
     * @return
     * @description 统计检查结果数量
     * */
    Map<String, Object> countJcjgTotal(String xmbh);

    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param xmid 项目id
     * @return
     * @description 根据xmid删除其他错误
     * */
    void deletDchyCgglZjqtcwDOByXmid(String xmid);



    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param xmid 项目ID
     * @param slbh  受理编号
     * @return
     * @description 开始质检
     * */
    Object startQualityCheck(String xmid, String slbh);

    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param xmid 项目id
     * @param slbh 受理编号
     * @param type 类型
     * @description 导出质检报告
     * */
    void export(HttpServletRequest request, HttpServletResponse response, String xmid, String slbh, String type) throws IOException;


    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param slbh 受理编号
     * @return
     * @description 检查成果包是否存在
     **/
    Map<String, Object> check(String slbh);

    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param xmid 项目id
     * @param slbh 受理编号
     * @return
     * @description 获取文件信息
     * */
    List<Map<String, String>> getFileInfo(String xmid , String slbh);

    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">wangyang</a>
     * @param
     * @return
     * @description 查询cwxx是否完全删除
     **/
     Integer countcwxx(Map<String,String>xmidMap);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param xmid 项目ID
     * @param slbh  受理编号
     * @return
     * @description 成果数据入库
     * */
    Object importDatabase(String xmid, String slbh,String chgcbh, String gzldyid);


}
