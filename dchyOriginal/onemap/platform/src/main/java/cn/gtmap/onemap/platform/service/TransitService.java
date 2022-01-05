package cn.gtmap.onemap.platform.service;

import java.util.Map;

/**
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2015/7/14 9:07
 */
public interface TransitService {

    /***
     * 组织报表的数据(for mas)  取自空间图层
     * @param rId
     * @param layerName
     * @param queryCondition 查询的条件
     * @param ds
     * @return
     */
    Map generateReportDataFromSde(int rId, String layerName, String queryCondition, String ds, boolean isXls);


}
