package cn.gtmap.onemap.platform.service.impl;

import cn.gtmap.onemap.platform.Constant;
import cn.gtmap.onemap.platform.entity.Document;
import cn.gtmap.onemap.platform.service.DocumentService;
import cn.gtmap.onemap.platform.service.GISManager;
import cn.gtmap.onemap.platform.service.GeometryService;
import cn.gtmap.onemap.platform.service.TransitService;
import cn.gtmap.onemap.platform.utils.ArrayUtils;
import cn.gtmap.onemap.platform.utils.FilesUtils;
import cn.gtmap.onemap.platform.utils.ZipUtils;
import com.vividsolutions.jts.geom.Geometry;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2015/7/16 10:47
 */
@Service
public class TransitServiceImpl extends BaseLogger implements TransitService {


    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    enum YTDL{
        工业类,居住类,商服类,交通类,其他类
    }

    /***
     * 开工、竣工、未到开工期
     */
    enum TAG{
        kg,wkg,jg,wjg,wdkg,
        kgmj,wkgmj,jgmj,wjgmj,wdkgmj,tdyt,count,sumArea
    }

    enum FIELD{
        EJXZQ,NGZMJ,WKGMJ_FJ,YTDL,XYJGS,XYKGS,SJKGS,SJJGS,WJCMJ_FJ,JSYDMJ_FJ,JSZT

    }

    @Autowired
    private GISManager gisManager;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private GeometryService geometryService;


    /***
     * 组织报表的数据(for mas)
     * @param rId
     * @param layerName
     * @param ds
     * @return
     */
    @Override
    public Map generateReportDataFromSde(int rId, String layerName, String queryCondition, String ds,boolean isXls) {
        assert queryCondition != null;
        try {
            LinkedHashMap reportData = new LinkedHashMap();
            Map data = new HashMap();
            if (rId == 6) {
                String folderPath = System.getProperty("java.io.tmpdir").concat("\\TMP_" + System.currentTimeMillis());
                List<String> conditions = Arrays.asList(queryCondition.split(","));
                Map xlsData;
                for (int i = 0; i < conditions.size(); i++) {
                    String condition = conditions.get(i);
                    boolean isYPZJWG=condition.indexOf("YPZJWG")>-1;
                    try {
                        xlsData = generateReportDataFromSde(i, isYPZJWG?"TEST.YDDK_CH_YPZJWG":layerName, condition, ds, true);
                        String fileName = getXlsName(i);
                        logger.info("rID: " + String.valueOf(i) + "  name:" + fileName + "layerName:" + (isYPZJWG ? "TEST.YDDK_CH_YPZJWG" : layerName) + "   condition:" + condition);
                        Document document = documentService.renderAnalysisExcel(xlsData, fileName.concat(Document.DOTS.concat(Document.Type.xml.name())), Document.Type.xls);
                        FilesUtils.generateFile(document.getContent(), folderPath, fileName.concat(Document.DOTS.concat(Document.Type.xls.name())));
                    } catch (Exception e) {
                        throw new RuntimeException(e.getLocalizedMessage());
                    }
                }
                File zipFile = ZipUtils.doZip(folderPath, null);
                reportData.put("file", zipFile);
            } else {
                @SuppressWarnings("unchecked")
                ArrayList<Map> queryResult = (ArrayList<Map>) gisManager.getGISService().query(layerName, queryCondition, null, true, ds);
                if(queryResult!=null&&queryResult.size()>=0){
                    if (rId != 0 && !isXls) {
                        for (Map _r : queryResult) {
                            Geometry geometry = geometryService.readWKT(MapUtils.getString(_r, Constant.SE_SHAPE_FIELD));
                            if (!geometry.isSimple())
                                geometry = geometryService.simplify(geometry, geometryService.getSimplifyTolerance());
                            _r.put("GEOJSON", geometryService.toGeoJSON(geometry));
                        }
                    }
                    reportData.put("count", queryResult.size() > 0 ? queryResult.size() + 1 : queryResult.size());
                    switch (rId) {
                        case 0:
                            Map ejxzqMap = ArrayUtils.listConvertMap(queryResult, FIELD.EJXZQ.name());
                            for (Object k : ejxzqMap.keySet()) {
                                List<Map> list = (List<Map>) ejxzqMap.get(k);         //xzq list
                                List<Map> nList = new ArrayList<Map>();                //存放组织后的新的list
                                Map sum = new HashMap();                                //每个xzq下的小计
                                Map ytdlMap = ArrayUtils.listConvertMap(list, FIELD.YTDL.name()); //用途分类
                                for (int i = 0; i < YTDL.values().length; i++) {
                                    String ytdl = YTDL.values()[i].name();
                                    Map tmp = new HashMap();
                                    tmp.put("tdyt", ytdl);
                                    if (ytdlMap.containsKey(ytdl)) {
                                        List<Map> ytList = (List<Map>) ytdlMap.get(ytdl);//获取属于当前土地用途的集合
                                        for (Map _y : ytList) {
                                            Date now = new Date();
                                            Date sjkgsj = null;
                                            Date sjjgsj = null;
                                            Date xykgsj = null;
                                            Date xyjgsj = null;

                                            double mj = MapUtils.getDoubleValue(_y, FIELD.NGZMJ.name(), 0.0);

                                            //协议竣工时
                                            if (StringUtils.isNotBlank(MapUtils.getString(_y, "XYJGS")))
                                                xyjgsj = sdf.parse(MapUtils.getString(_y, "XYJGS"));
                                            //协议开工时
                                            if (StringUtils.isNotBlank(MapUtils.getString(_y, "XYKGS")))
                                                xykgsj = sdf.parse(MapUtils.getString(_y, "XYKGS"));

                                            //实际开工时
                                            if (StringUtils.isNotBlank(MapUtils.getString(_y, "SJKGS")))
                                                sjkgsj = sdf.parse(MapUtils.getString(_y, "SJKGS"));
                                            //实际竣工时
                                            if (StringUtils.isNotBlank(MapUtils.getString(_y, "SJJGS")))
                                                sjjgsj = sdf.parse(MapUtils.getString(_y, "SJJGS"));

                                            if (sjkgsj != null && sjjgsj == null) {
                                                _y.put("TAG", TAG.kg);
                                                _y.put("AREA", mj);
                                                continue;
                                            } else if (xykgsj != null && now.compareTo(xykgsj) > 0 && sjkgsj == null) {
                                                _y.put("TAG", TAG.wkg);
                                                _y.put("AREA", mj);
                                            } else if (xyjgsj != null && now.compareTo(xyjgsj) > 0 && sjjgsj != null) {
                                                _y.put("TAG", TAG.jg);
                                                _y.put("AREA", mj);
                                                continue;
                                            } else if (xyjgsj != null && now.compareTo(xyjgsj) > 0 && sjjgsj == null) {
                                                _y.put("TAG", TAG.wjg);
                                                _y.put("AREA", mj);
                                                continue;
                                            } else if (xyjgsj != null && now.compareTo(xyjgsj) < 0) {
                                                _y.put("TAG", TAG.wdkg);
                                                _y.put("AREA", mj);
                                            }
                                        }
                                        Map tagMap = ArrayUtils.listConvertMap(ytList, "TAG");
                                        for (Object key : tagMap.keySet()) {
                                            List<Map> tagList = (List<Map>) tagMap.get(key);
                                            try {
                                                switch (TAG.valueOf(String.valueOf(key))) {
                                                    case kg:
                                                        tmp.put(TAG.kg.name(), getCount(tagList));
                                                        tmp.put(TAG.kgmj.name(), getDoubleSumByField(tagList, "AREA"));
                                                        break;
                                                    case wkg:
                                                        tmp.put(TAG.wkg.name(), getCount(tagList));
                                                        tmp.put(TAG.wkgmj.name(), getDoubleSumByField(tagList, "AREA"));
                                                        break;
                                                    case jg:
                                                        tmp.put(TAG.jg.name(), getCount(tagList));
                                                        tmp.put(TAG.jgmj.name(), getDoubleSumByField(list, "AREA"));
                                                        break;
                                                    case wjg:
                                                        tmp.put(TAG.wjg.name(), getCount(tagList));
                                                        tmp.put(TAG.wjgmj.name(), getDoubleSumByField(list, "AREA"));
                                                        break;
                                                    case wdkg:
                                                        tmp.put(TAG.wdkg.name(), getCount(tagList));
                                                        tmp.put(TAG.wdkgmj.name(), getDoubleSumByField(list, "AREA"));
                                                        break;
                                                }
                                            } catch (IllegalArgumentException e) {
                                                throw new RuntimeException(e.getLocalizedMessage());
                                            }
                                        }
                                    }
                                    tmp.put(TAG.count.name(), getIntSumByField(tmp, TAG.kg.name(), TAG.jg.name(), TAG.wkg.name(), TAG.wjg.name(), TAG.wdkg.name()));
                                    tmp.put(TAG.sumArea.name(), getDoubleSumByField2(tmp, TAG.kgmj.name(), TAG.jgmj.name(), TAG.wkgmj.name(), TAG.wjgmj.name(), TAG.wdkgmj.name()));
                                    nList.add(tmp);
                                }

                                sum.put(TAG.tdyt.name(), "小计");
                                sum.put(TAG.kg.name(), getIntSumByField(nList, TAG.kg.name()));
                                sum.put(TAG.jg.name(), getIntSumByField(nList, TAG.jg.name()));
                                sum.put(TAG.wkg.name(), getIntSumByField(nList, TAG.wkg.name()));
                                sum.put(TAG.wjg.name(), getIntSumByField(nList, TAG.wjg.name()));
                                sum.put(TAG.wdkg.name(), getIntSumByField(nList, TAG.wdkg.name()));

                                sum.put(TAG.kgmj.name(), getDoubleSumByField(nList, TAG.kgmj.name()));
                                sum.put(TAG.jgmj.name(), getDoubleSumByField(nList, TAG.jgmj.name()));
                                sum.put(TAG.wkgmj.name(), getDoubleSumByField(nList, TAG.wkgmj.name()));
                                sum.put(TAG.wjgmj.name(), getDoubleSumByField(nList, TAG.wjgmj.name()));
                                sum.put(TAG.wdkgmj.name(), getDoubleSumByField(nList, TAG.wdkgmj.name()));
                                sum.put(TAG.count.name(), getIntSumByField(nList, TAG.count.name()));
                                sum.put(TAG.sumArea.name(), getDoubleSumByField(nList, TAG.sumArea.name()));
                                nList.add(sum);

                                data.put(k, nList);
                            }
                            reportData.put("data", data);
                            break;
                        case 1:
                            Calendar now = Calendar.getInstance();
                            reportData.put("year", String.valueOf(now.get(Calendar.YEAR)));
                            Map speciesMap = ArrayUtils.listConvertMap(queryResult, FIELD.YTDL.name());
                            for (Object key : speciesMap.keySet()) {
                                int count = 0;
                                List<Map> list = (List<Map>) speciesMap.get(key);
                                LinkedHashMap xzqMap = ArrayUtils.listConvertLinkedMap(list, FIELD.EJXZQ.name());
                                for (Object _k : xzqMap.keySet()) {
                                    List<Map> xzqList = (List<Map>) xzqMap.get(_k);
                                    xzqList.add(getSumMap(xzqList));   //添加小计
                                    count += xzqList.size();
                                }
                                xzqMap.put("总计", getSumList(list, FIELD.WJCMJ_FJ.name()));  //该大分类下总计
                                Map categoryData = new HashMap();
                                categoryData.put("data", xzqMap);
                                categoryData.put("count", count + 1);
                                data.put(key, categoryData);
                            }
                            reportData.put("data", data);
                            break;
                        case 2:
                            queryResult.add(getSumMap(queryResult));
                            reportData.put("data", queryResult);
                            break;
                        case 3:
                        case 4:
                        case 5:
                            data = ArrayUtils.listConvertLinkedMap(queryResult, FIELD.EJXZQ.name());
                            data.put("合计", getSumList(queryResult, rId == 4 ? FIELD.WJCMJ_FJ.name() : FIELD.WKGMJ_FJ.name()));
                            reportData.put("data", data);
                            break;
                        case 7:
                            Map groupMap = ArrayUtils.listConvertLinkedMap(queryResult, FIELD.JSZT.name());
                            for (Object key : groupMap.keySet()) {
                                List<Map> list = (List<Map>) groupMap.get(key);
                                //按照ND进行排序 然后进行合计
                                Collections.sort(list, new Comparator<Map>() {
                                    public int compare(Map arg0, Map arg1) {
                                        String nd0=MapUtils.getString(arg0,"ND","0");
                                        String nd1=MapUtils.getString(arg1,"ND","0");
                                        return Integer.valueOf(nd0)-Integer.valueOf(nd1);
                                    }
                                });
                                Map totalMap=new HashMap();
                                totalMap.put("XZQ","合计");
                                totalMap.put("ZDMJ",getSumByField(list,"ZDMJ"));
                                totalMap.put("CFMJ",getSumByField(list,"CFMJ"));
                                totalMap.put("YSYMJ",getSumByField(list,"YSYMJ"));
                                list.add(totalMap);
                            }
                            reportData.put("data", groupMap);
                            break;
                    }
                }else
                    logger.warn("查询无结果");

            }
            return reportData;
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    /***
     *
     * @param id
     * @return
     */
    private String getXlsName(int id){
        String fileName="";
        switch (id){
            case 0:
                fileName="全市项目用地开竣工情况汇总表";
                break;
            case 1:
                fileName="2015年全市项目用地开竣工情况周报表";
                break;
            case 2:
                fileName="慈湖高新区已批在建(未供)项目";
                break;
            case 3:
                fileName="市本级涉嫌土地闲置开发建设项目一览表";
                break;
            case 4:
                fileName="重点监管建设项目一览表";
                break;
            case 5:
                fileName="市本级用而未尽开发建设项目一览表";
                break;
            case 7:
                fileName="标准化厂房建设情况汇总表";
                break;
        }
        return fileName;
    }

    /***
     *
     * @param list
     * @param sumField
     * @return
     */
    public double getDoubleSumByField(List<Map> list, String sumField) {
        double sum = 0;
        List<String> crbhList = new ArrayList<String>();
        for (Map map : list) {
            if (map.containsKey("CRBH")) {
                String crbh = MapUtils.getString(map, "CRBH");
                if (crbhList.contains(crbh)) continue;
                crbhList.add(crbh);
            }
            double area = MapUtils.getDoubleValue(map, sumField, 0.0);
            sum += area;
        }
        return sum;
    }

    /***
     *
     * @param list
     * @param sumField
     * @return
     */
    public double getSumByField(List<Map> list, String sumField) {
        double sum = 0;
        for (Map map : list) {
            double area = MapUtils.getDoubleValue(map, sumField, 0.0);
            sum += area;
        }
        return sum;
    }

    /***
     *
     * @param list
     * @return
     */
    public int getCount(List<Map> list) {
        int count = 0;
        List<String> crbhList = new ArrayList<String>();
        for (Map map : list) {
            String crbh = MapUtils.getString(map, "CRBH");
            if (crbhList.contains(crbh)) continue;
            crbhList.add(crbh);
            count++;
        }
        return count;
    }
    /***
     * 统计int值之和
     * @param list
     * @param sumField
     * @return
     */
    public int getIntSumByField(List<Map> list, String sumField) {
        int count = 0;
        for (Map map : list) {
            if (map.get(sumField) instanceof Integer)
                count += MapUtils.getIntValue(map, sumField);
        }
        return count;
    }

    /***
     * 根据sumField统计值
     * @param srcObj    可为list<Map>或Map
     * @param sumField  统计字段key
     * @return
     */
    public int getIntSumByField(Object srcObj, String... sumField) {
        int count = 0;
        Map map = new HashMap();
        List<Map> list = new ArrayList<Map>();
        if (srcObj instanceof Map) {
            map = (Map) srcObj;
            for (String f : sumField) {
                count += MapUtils.getIntValue(map, f);
            }
        } else if (srcObj instanceof ArrayList) {
            list = (List<Map>) srcObj;
            for (Map item : list) {
                for (String f : sumField) {
                    count += MapUtils.getIntValue(item, f);
                }
            }
        }
        return count;
    }

    /***
     *
     * @param srcObj
     * @param sumField
     * @return
     */
    public double getDoubleSumByField2(Object srcObj, String... sumField) {
        double sum = 0;
        List<String> crbhList = new ArrayList<String>();
        Map map;
        List<Map> list;
        String crbh;
        if (srcObj instanceof Map) {
            map = (Map) srcObj;
            for (String f : sumField) {
                sum += MapUtils.getDoubleValue(map, f,0.0);
            }
        } else if (srcObj instanceof ArrayList) {
            list = (List<Map>) srcObj;
            for (Map item : list) {
                crbh = MapUtils.getString(item, "CRBH");
                if (crbhList.contains(crbh)) continue;
                crbhList.add(crbh);
                for (String f : sumField) {
                    sum += MapUtils.getDoubleValue(item, f,0.0);
                }
            }
        }
        return sum;
    }

    /***
     *
     * @param list
     * @return
     */
    private Map getSumMap(List<Map> list) {
        Map result = new HashMap();
        double sumMj = 0;
        double sumWjcmj = 0;
        double sumJsydmj = 0;
        List<String> crbhList = new ArrayList<String>();
        for (Map item : list) {
            double mj = MapUtils.getDoubleValue(item, FIELD.NGZMJ.name(), 0.0);
            double wjcmj = MapUtils.getDoubleValue(item, FIELD.WJCMJ_FJ.name(), 0.0);
            double jsydmj = MapUtils.getDoubleValue(item, FIELD.JSYDMJ_FJ.name(), 0.0);
            String crbh = MapUtils.getString(item, "CRBH");
            if (crbhList.contains(crbh)) continue;
            crbhList.add(crbh);
            sumMj += mj;
            sumWjcmj += wjcmj;
            sumJsydmj +=jsydmj;
        }
        result.put(FIELD.NGZMJ.name(), sumMj);
        result.put(FIELD.WJCMJ_FJ.name(), sumWjcmj);
        result.put(FIELD.JSYDMJ_FJ.name(), sumJsydmj);
        result.put(FIELD.EJXZQ.name(), "合计");
        return result;
    }

    /***
     * 面积合计
     * @param list
     * @return
     */
    private List<Map> getSumList(List<Map> list,String areaField) {
        List<Map> arrayList = new ArrayList<Map>();
        Map result = new HashMap();
        double sumMj = 0;
        double sumWjcmj = 0;
        List<String> crbhList = new ArrayList<String>();
        for (Map item : list) {
            double mj = MapUtils.getDoubleValue(item, FIELD.NGZMJ.name(), 0.0);
            double wjcmj = MapUtils.getDoubleValue(item, areaField, 0.0);
            String crbh = MapUtils.getString(item, "CRBH");
            if (crbhList.contains(crbh)) continue;
            crbhList.add(crbh);
            sumMj += mj;
            sumWjcmj += wjcmj;
        }
        result.put(FIELD.NGZMJ.name(), sumMj);
        result.put(areaField, sumWjcmj);
        result.put(FIELD.EJXZQ.name(), "合计");
        arrayList.add(result);
        return arrayList;
    }


}
