package cn.gtmap.msurveyplat.promanage.core.service;

import cn.gtmap.msurveyplat.common.dto.DchyXmglChxmDto;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
 * @version 1.o, 2020-12-09
 * description
 */
public interface XcsldjService {

    //初始化测绘工程ID
    Map<String, Object> initXcsldj();

    //初始化加载测绘单位信息
    List<Map<String, Object>> queryChdwList();

    //手动录入测绘单位信息
    Map<String, Object> saveChdw(Map<String, Object> paramMap);

    //新增现场受理登记
    boolean saveXcsldj(DchyXmglChxmDto dchyXmglChxmDto);

    //现场受理查询
    List<Map<String, Object>> queryXcsldj(Map<String, Object> paramMap);

    //删除现场受理登记
    Map<String, Object> deleteXcsldjByChxmid(Map<String, Object> paramMap);

    //根据工程编号查询测绘信息
    Map<String, Object> queryChxxByChgcbh(Map<String, Object> paramMap);

    //根据需求发布编号查询测绘项目信息
    Map<String, Object> queryChxmByXqfbbh(Map<String, Object> paramMap);

    //根据项目编号查询备案信息
    Map<String, Object> queryBaxxByChxmid(Map<String, Object> paramMap);

    /**
     * @return java.lang.String
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: xqfbbh
     * @time 2020/12/16 8:57
     * @description 根据需求发布编号，获取需求发布人
     */
    String getXschxmFbrByXqfbbh(String xqfbbh);

    /**
     * @param gcbh
     * @return
     * @description 2021/2/2 通过工程编号获取已经办结的测量事项
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    List< Map<String, Object>> queryBaClsxListByGcbh(String gcbh);

    String obtainSlbh();

    /**
     * @param paramMap
     * @return
     * @description 2021/5/8 线下备案录入建设单位
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    ResponseMessage saveJsdw(Map<String, Object> paramMap);

    /**
     * @param key
     * @param value
     * @return
     * @description 2021/5/8 判断是否存在
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    boolean isExist(String key, String value);
}
