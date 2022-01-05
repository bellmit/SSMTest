package cn.gtmap.onemap.platform.service;

import org.springframework.data.domain.Page;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: zhangbixi
 * Date: 13-4-19
 * Time: 下午2:25
 * To change this template use File | Settings | File Templates.
 */
public interface MapQueryService {


    /**
     * ArcGIS Server 的方式查询
     *
     * @param url 携带查询参数的地址
     * @return
     */
    String execute(String url);

    /***
     * 分页查询
     * @param layerUrl  地图服务图层url
     * @param where     属性查询条件
     * @param geo       空间查询图形 geojson
     * @param outFields 返回字段
     * @param pageNum      查询页数
     * @param size      分页记录数
     * @return
     */
    Page execute(String layerUrl, String where, String geo, String outFields, int pageNum, int size);

    /***
     * 分页查询(属性)
     * @param layerUrl
     * @param where
     * @param outFields
     * @param pageNum
     * @param size
     * @return
     */
    Map execute(String layerUrl, String where, String outFields, int pageNum, int size);

    /***
     * 空间/属性 查询 不分页
     * @param layerUrl  地图服务图层url
     * @param where     属性查询条件
     * @param geo       空间查询图形 geojson
     * @param outFields 返回字段
     * @return
     */
    String execute(String layerUrl, String where, String geo, String outFields, String extraParams);

}
