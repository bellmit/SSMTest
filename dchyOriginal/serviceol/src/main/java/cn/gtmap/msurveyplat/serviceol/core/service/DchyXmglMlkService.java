package cn.gtmap.msurveyplat.serviceol.core.service;

import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglSqxx;
import cn.gtmap.msurveyplat.common.dto.DchyXmglMlkDto;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface DchyXmglMlkService {
    /**
     * 获取所有的从业人员集合
     *
     * @return
     */
    List<Map<String, Object>> getAllCyrysList();

    /**
     * 根据mlkid获取对应从业人员信息
     *
     * @param mlkid
     * @return
     */
    List<Map<String, Object>> queryCyryByMlkId(String mlkid);

    /**
     * 注销名录库
     *
     * @param data
     * @return
     */
    void logoutMlk(Map<String, Object> data);

    /**
     * 变更名录库信息
     *
     * @param data
     * @return
     */
    DchyXmglMlkDto alterMlkInfo(Map<String, Object> data);

    boolean saveChangeAfterMlkxx(DchyXmglMlkDto dchyXmglMlkDto);

    /**
     * 根据所属模块id获取对应需要上传的材料信息
     *
     * @param ssmkid
     * @return
     */
    List<Map<String, Object>> queryUploadFileBySsmkId(String ssmkid);


    /**
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: userid
     * @time 2020/12/3 19:48
     * @description 通过用户id获取名录库状态
     */
    Map<String, Object> queryMlkXxZtByUserid(String userid);


    /**
     * 获取到收件材料信息
     *
     * @param mlkid
     * @return
     */
    List<Map<String, Object>> getSjclXx(String mlkid, String ssmkid);

    /**
     * 获取名录库注销的移除原因
     * @param mlkid
     * @return
     */
    String getYcyyByMlkid(String mlkid);

    /**
     * 获取到收件材料信息
     *
     * @param mlkid
     * @return
     */
    List<Map<String, Object>> getSjclXx2(String mlkid, String ssmkid);

    List<Map<String, Object>> getSjclXxFromSxbf(String mlkid, String ssmkid);

    /**
     * 获取到收件材料信息
     *
     * @param glsxid
     * @return
     */
    List<Map<String, Object>> getSjclXxByGlsxid(String glsxid);

    /**
     * 获取到收件材料信息
     *
     * @param mlkid
     * @return
     */
    List<Map<String, Object>> getXsbfSjclXx(String mlkid);

    /**
     * 初始化材料信息与收件信息
     *
     * @param glsxId
     * @param ssmkId
     * @param mapList
     */
    void initSjxxAndClxx(String glsxId, String ssmkId, List<Map<String, Object>> mapList);

    List<Map<String, Object>> getMlkXxById(String mlkId);

    /**
     * 根据名录库id获取对名录库信息
     *
     * @param mlkId
     * @return
     */
    List<Map<String, Object>> getchdwbymlkid(String mlkId);

    Page<Map<String, Object>> getMlkDbrw(Map<String, Object> data);

    Page<Map<String, Object>> getMlkYbrw(Map<String, Object> data);

    /**
     * 初始化申请信息
     *
     * @param param
     * @return
     */
    DchyXmglSqxx initSqxx(Map<String, Object> param);

    boolean checkBeforeLogoutMlk(Map<String, Object> map);

    /**
     * 初始化名录
     *
     * @param param
     * @return
     */
    boolean initMlk(Map<String, Object> param);

    ResponseMessage delFile(String sjclId, String wjzxId);

    /**
     * 名录库入驻时，向页面返回信息填入
     *
     * @return
     */
    Map<String, Object> getInitMlkParam();

    /**
     * 文件查看
     *
     * @param data
     * @return
     */
    Map<String, Object> viewattachments(Map<String, Object> data);

    /**
     * @param map
     * @return
     * @description 2021/2/23 首页名录库台账
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    Page<Map<String, Object>> mlkck(Map<String, Object> map);


    /**
     * 多条件查询测绘单位信息
     *
     * @param data
     * @return
     */
    Page<Map<String, Object>> queryChdwsByMultConditions(Map<String, Object> data);

    /**
     * 根据mlkid获取对应考评信息
     */
    int getKpResultByMlkId(String mlkId);

    /**
     * 备案上传合同前，初始化htxxid，用于获取上传的材料信息
     *
     * @param chxmid
     * @return
     */
    String initHtxx(String chxmid);

    /**
     * 获取上传文件的材料信息
     *
     * @param chxmid
     * @return
     */
    String getHtxxIdByChxmid(String chxmid);

    List<String> getHtxxIdByChxmid2(String chxmid);


    /**
     * 获取上传文件的材料信息
     *
     * @param chxmid
     * @return
     */
    List<String> getXsbfHtxxIdByChxmid(String chxmid);

    /**
     * 在线备案后获取所有备案上传文件的材料信息
     *
     * @param data
     * @return
     */

    List<Map<String, Object>> afterBaxxForSjcl(Map<String, Object> data);

    /**
     * @param mlkid
     * @return
     * @description 2021/3/20 通过mlkid获取相关的测量事项
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    Map<String, Object> queryClsxByMlkid(String mlkid);

    /**
     * @param map
     * @return
     * @description 2021/3/26  保存名录库图片
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    ResponseMessage uploadMlktp(Map<String, Object> map);

    /**
     * get Sjcl By GlsxId
     *
     * @param glsxid String
     * @return List<Map < String, Object>>
     */
    List<Map<String, Object>> getSjclByGlsxId(String glsxid);

    /**
     * 名录库注销审核
     * @param data
     * @return
     */
    boolean mlkLogoutAudit(Map<String, Object> data);
}
