package cn.gtmap.onemap.platform.utils;

import org.apache.commons.collections.MapUtils;

import java.util.*;

/**
 * 分析辅助类
 * @version 1.0
 * @Author Lenovo
 * @Time 2014/10/27 下午12:04
 */
public final class AnalysisUtils {

    /**
     * 土地利用现状等级 A--一级分类
     * B--二级分类
     * C---三大类
     */
   public enum TDLYXZ_GRADE{
        A,B,C
    }
    /**
     * 根据不同分类组织土地利用现状结果
     * @param rawList
     * @param dictList
     * @param grade
     * @return
     */
    public static List<Map> getTdlyCategoryByGrade(List<Map> rawList,List<Map> dictList,TDLYXZ_GRADE grade)
    {
        List<Map> result = new ArrayList<Map>();
        try {
            switch (grade)
            {
                case A: {
                    for (Map dictMap : dictList) {

                        Map categoryMap = new HashMap();
                        List<String> dlbms = Arrays.asList(MapUtils.getString(dictMap, "dlbm").split(","));

                        double area = 0;
                        double area_jt = 0;
                        double area_gy = 0;

                        if (dlbms.size() > 1) {
                            for (Map rawMap : rawList) {
                                if (dlbms.contains(MapUtils.getString(rawMap, "dlbm"))) {
                                    area += MapUtils.getDoubleValue(rawMap, "area", 0.0);
                                    area_gy += MapUtils.getDoubleValue(rawMap, "area_gy", 0.0);
                                    area_jt += MapUtils.getDoubleValue(rawMap, "area_jt", 0.0);
                                }
                            }
                        } else {
                            for (Map rawMap : rawList) {
                                if (MapUtils.getString(rawMap, "dlbm").startsWith(dlbms.get(0))) {
                                    area += MapUtils.getDoubleValue(rawMap, "area", 0.0);
                                    area_gy += MapUtils.getDoubleValue(rawMap, "area_gy", 0.0);
                                    area_jt += MapUtils.getDoubleValue(rawMap, "area_jt", 0.0);
                                }

                            }
                        }
                        categoryMap.put("dlmc", MapUtils.getString(dictMap, "dlmc"));
                        categoryMap.put("dlbm",MapUtils.getString(dictMap, "dlbm"));
                        categoryMap.put("area",area);
                        categoryMap.put("area_gy",area_gy);
                        categoryMap.put("area_jt",area_jt);
                        result.add(categoryMap);
                    }
                    break;
                }
                case B:
                {
                    Map categoryB = new HashMap();
                    List<Map> categoryA = getTdlyCategoryByGrade(rawList, dictList, TDLYXZ_GRADE.A);
                    for(Map item:categoryA)
                    {
                        List<String> dlbms = Arrays.asList(MapUtils.getString(item, "dlbm").split(","));
                        if(dlbms.size()>1)
                        {
                            categoryB.put(item.get("dlmc"),MapUtils.getDoubleValue(item,"area",0.0));
                            categoryB.put(MapUtils.getString(item,"dlmc").concat("_jt"),MapUtils.getDoubleValue(item,"area_jt",0.0));
                            categoryB.put(MapUtils.getString(item,"dlmc").concat("_gy"),MapUtils.getDoubleValue(item,"area_gy",0.0));

                        }else
                        {
                            categoryB.put(dlbms.get(0),MapUtils.getDoubleValue(item,"area",0.0));
                            categoryB.put(dlbms.get(0).concat("_jt"),MapUtils.getDoubleValue(item,"area_jt",0.0));
                            categoryB.put(dlbms.get(0).concat("_gy"),MapUtils.getDoubleValue(item,"area_gy",0.0));
                            for(Map rawMap:rawList)
                            {
                                if (MapUtils.getString(rawMap, "dlbm").startsWith(dlbms.get(0))) {
                                    categoryB.put(MapUtils.getString(rawMap, "dlbm"),MapUtils.getDoubleValue(rawMap,"area",0.0));
                                    categoryB.put(MapUtils.getString(rawMap, "dlbm").concat("_jt"),MapUtils.getDoubleValue(rawMap,"area_jt",0.0));
                                    categoryB.put(MapUtils.getString(rawMap, "dlbm").concat("_gy"),MapUtils.getDoubleValue(rawMap,"area_gy",0.0));
                                }
                            }
                        }
                        result.add(categoryB);
                    }
                    break;
                }
                case C:
                {
                    break;
                }
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    /***
     * 解析分析的where子句表达式
     * @param condition eg. objectid>100
     * @return
     */
    public static Map parseAnalysisCondition(String condition) {
        Map result = new HashMap();
        String operator = "=";
        if (condition.contains("="))
            operator = "=";
        else if (condition.contains("<"))
            operator = "<";
        else if (condition.contains(">"))
            operator = ">";
        else if (condition.contains("like"))
            operator = "like";
        result.put("operator", operator);
        String[] array = condition.split("=|<|>|like");
        result.put("key", array[0].trim());
        result.put("value", array[1].replaceAll("'|%", "").trim());
        return result;
    }
}
