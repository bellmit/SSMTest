package cn.gtmap.onemap.platform.service.impl;

import cn.gtmap.onemap.platform.utils.FileCreateUtil;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @作者 王建明
 * @创建日期 2018/5/8 0008
 * @创建时间 下午 10:08
 * @版本号 V 1.0
 */
public class MainTester {
	private static DecimalFormat Df = new DecimalFormat("############.##");

	public static void main(String[] args) {
		List<String> legendColumns = new ArrayList<String>();
		legendColumns.add("DLMC");
		List<String> statisColumns = new ArrayList<String>();
		statisColumns.add("INTERSECT_AREA");
		List<String> tableLegendColumns = new ArrayList<String>();
		tableLegendColumns.add("ZLDWMC");
		tableLegendColumns.add("DLMC");
		List<String> tableStatisColumns = new ArrayList<String>();
		tableStatisColumns.add("INTERSECT_AREA");
		tableStatisColumns.add("INTERSECT_AREA_Q");
		tableStatisColumns.add("INTERSECT_AREA_M");
		tableStatisColumns.add("XZDWMJ");
		Map<String, Object> resultData = new HashMap<String, Object>();
		List<Map<String, Object>> srcData = new ArrayList<Map<String, Object>>();
		String json = FileCreateUtil.getFileContentUTF8(new File("f:/分析结果数据.txt"));
		srcData = (List<Map<String, Object>>) JSON.parse(json);

		String legendColumn = legendColumns.get(0);
		String statisColumn = statisColumns.get(0);
		double sum = 0;
		Map<String, Double> map = new HashMap<String, Double>();

		Double totalNydDou = 0d;
		Double totalJsydDou = 0d;
		Double totalWlydDou = 0d;
		Double totalGdDou = 0d;
		Double totalStDou = 0d;
		Double totalSjdDou = 0d;
		Double totalHdDou = 0d;
		Double totalTotalDou = 0d;
		Double totalXjDou = 0d;
		Double totalQtDou = 0d;

		Double totalJtNydDou = 0d;
		Double totalJtJsydDou = 0d;
		Double totalJtWlydDou = 0d;
		Double totalJtGdDou = 0d;
		Double totalJtStDou = 0d;
		Double totalJtSjdDou = 0d;
		Double totalJtHdDou = 0d;
		Double totalJtTotalDou = 0d;
		Double totalJtXjDou = 0d;
		Double totalJtQtDou = 0d;

		Double totalGyNydDou = 0d;
		Double totalGyJsydDou = 0d;
		Double totalGyWlydDou = 0d;
		Double totalGyGdDou = 0d;
		Double totalGyStDou = 0d;
		Double totalGySjdDou = 0d;
		Double totalGyHdDou = 0d;
		Double totalGyTotalDou = 0d;
		Double totalGyXjDou = 0d;
		Double totalGyQtDou = 0d;

		double x02 = 0.0;
		double x10 = 0.0;
		double x11 = 0.0;
		double x12 = 0.0;
		Set<String> lx = new HashSet<String>();
		Map<String, Map<String, Object>> detailData = new LinkedHashMap<String, Map<String, Object>>();
		for (int i = 0; i < srcData.size(); i++) {
			Map<String, Object> data = srcData.get(i);
			if (i == 0 && (!data.containsKey(legendColumn) || !data.containsKey(statisColumn)))
				break;
			String dlbm = data.get("DLBM").toString();
			String type = dlbm.substring(0, 2);
			lx.add(type);
			Double columnValue = Double.parseDouble(String.valueOf(data.get(statisColumn)));
			if ("02".equals(type)) {
				x02 += columnValue;
			}
			if ("10".equals(type)) {
				x10 += columnValue;
			}
			if ("11".equals(type)) {
				x11 += columnValue;
			}
			if ("12".equals(type)) {
				x12 += columnValue;
			}
			sum += columnValue;
			if (map.containsKey(type)) {
				double value = map.get(type);
				map.put(type, value + columnValue);
			} else {
				map.put(type, columnValue);
			}

			Map<String, Object> columnData = detailData.get(data.get("ZLDWMC"));
			if (columnData == null) {
				columnData = new HashMap<String, Object>();
				detailData.put(String.valueOf(data.get("ZLDWMC")), columnData);
			}
			columnData.put("tdzl", String.valueOf(data.get("ZLDWMC")));

			String opreaStr = "nyd,jsyd,wlyd,gd,st,sjd,hd";
			String[] types = opreaStr.split(",");
			for (String typeStr : types) {
				Double typeDouble = org.apache.commons.collections.MapUtils.getDouble(columnData, typeStr, 0.0);//Double.parseDouble(typeStrVal);
				if (isExtraType(dlbm, typeStr)) {
					typeDouble += org.apache.commons.collections.MapUtils.getDouble(data, "INTERSECT_AREA", 0.0);//Double.parseDouble(String.valueOf(data.get("INTERSECT_AREA")));
				}
				columnData.put(typeStr, typeDouble);
			}
			Double nydDou = org.apache.commons.collections.MapUtils.getDouble(columnData, "nyd", 0.0);//Double.parseDouble((String) columnData.get("nyd"));
			Double jsydDou = org.apache.commons.collections.MapUtils.getDouble(columnData, "jsyd", 0.0);//Double.parseDouble((String) columnData.get("jsyd"));
			Double wlydDou = org.apache.commons.collections.MapUtils.getDouble(columnData, "wlyd", 0.0);//Double.parseDouble((String) columnData.get("wlyd"));
			Double gdDou = org.apache.commons.collections.MapUtils.getDouble(columnData, "gd", 0.0);//Double.parseDouble((String) columnData.get("gd"));
			Double stDou = org.apache.commons.collections.MapUtils.getDouble(columnData, "st", 0.0);//Double.parseDouble((String) columnData.get("st"));
			Double sjdDou = org.apache.commons.collections.MapUtils.getDouble(columnData, "sjd", 0.0);//Double.parseDouble((String) columnData.get("sjd"));
			Double hdDou = org.apache.commons.collections.MapUtils.getDouble(columnData, "hd", 0.0);//Double.parseDouble((String) columnData.get("hd"));
			Double totalDou = nydDou + jsydDou + wlydDou;
			Double xjDou = stDou + sjdDou + hdDou;
			Double qtDou = nydDou - gdDou;

			totalNydDou += nydDou;
			totalJsydDou += jsydDou;
			totalWlydDou += wlydDou;
			totalGdDou += gdDou;
			totalStDou += stDou;
			totalSjdDou += sjdDou;
			totalHdDou += hdDou;
			totalTotalDou += totalDou;
			totalXjDou += xjDou;
			totalQtDou += qtDou;

			if (isJiTi(String.valueOf(data.get("QSXZ")))) {
				totalJtNydDou += nydDou;
				totalJtJsydDou += jsydDou;
				totalJtWlydDou += wlydDou;
				totalJtGdDou += gdDou;
				totalJtStDou += stDou;
				totalJtSjdDou += sjdDou;
				totalJtHdDou += hdDou;
				totalJtTotalDou += totalDou;
				totalJtXjDou += xjDou;
				totalJtQtDou += qtDou;
			} else {
				totalGyNydDou += nydDou;
				totalGyJsydDou += jsydDou;
				totalGyWlydDou += wlydDou;
				totalGyGdDou += gdDou;
				totalGyStDou += stDou;
				totalGySjdDou += sjdDou;
				totalGyHdDou += hdDou;
				totalGyTotalDou += totalDou;
				totalGyXjDou += xjDou;
				totalGyQtDou += qtDou;
			}

			columnData.put("total", Df.format(totalDou));
			columnData.put("xj", Df.format(xjDou));
			columnData.put("qt", Df.format(qtDou));
		}

		System.out.println(lx);
		System.out.println(lx.size());
		System.out.println(x02);
		System.out.println(x10);
		System.out.println(x11);
		System.out.println(x12);
		Map<String, Object> columnData = new HashMap<String, Object>();
		columnData.put("tdzl", "总计");
		columnData.put("total", Df.format(totalTotalDou));
		columnData.put("xj", Df.format(totalXjDou));
		columnData.put("qt", Df.format(totalQtDou));
		columnData.put("nyd", Df.format(totalNydDou));
		columnData.put("jsyd", Df.format(totalJsydDou));
		columnData.put("wlyd", Df.format(totalWlydDou));
		columnData.put("gd", Df.format(totalGdDou));
		columnData.put("st", Df.format(totalStDou));
		columnData.put("sjd", Df.format(totalSjdDou));
		columnData.put("hd", Df.format(totalHdDou));
		detailData.put("总计", columnData);

		columnData = new HashMap<String, Object>();
		columnData.put("tdzl", "国有合计");
		columnData.put("total", Df.format(totalGyTotalDou));
		columnData.put("xj", Df.format(totalGyXjDou));
		columnData.put("qt", Df.format(totalGyQtDou));
		columnData.put("nyd", Df.format(totalGyNydDou));
		columnData.put("jsyd", Df.format(totalGyJsydDou));
		columnData.put("wlyd", Df.format(totalGyWlydDou));
		columnData.put("gd", Df.format(totalGyGdDou));
		columnData.put("st", Df.format(totalGyStDou));
		columnData.put("sjd", Df.format(totalGySjdDou));
		columnData.put("hd", Df.format(totalGyHdDou));
		detailData.put("国有合计", columnData);

		columnData = new HashMap<String, Object>();
		columnData.put("tdzl", "集体合计");
		columnData.put("total", Df.format(totalJtTotalDou));
		columnData.put("xj", Df.format(totalJtXjDou));
		columnData.put("qt", Df.format(totalJtQtDou));
		columnData.put("nyd", Df.format(totalJtNydDou));
		columnData.put("jsyd", Df.format(totalJtJsydDou));
		columnData.put("wlyd", Df.format(totalJtWlydDou));
		columnData.put("gd", Df.format(totalJtGdDou));
		columnData.put("st", Df.format(totalJtStDou));
		columnData.put("sjd", Df.format(totalJtSjdDou));
		columnData.put("hd", Df.format(totalJtHdDou));
		detailData.put("集体合计", columnData);


		//将原来二级类的分类转化为一级类，并将要求的8个一级类全部显示,有值显示值，无值显示0,并计算合计面积
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		List<String> list1 = new ArrayList<String>();
		list1.add("合计");
		list1.add("01");
		list1.add("02");
		list1.add("03");
		list1.add("04");
		list1.add("10");
		list1.add("11");
		list1.add("12");
		list1.add("20");
		for (int r = 0; r < list1.size(); r++) {
			String dlmc = null;
			if (list1.get(r).equals("合计")) {
				dlmc = "面积";
			}
			if (list1.get(r).equals("01")) {
				dlmc = "耕地";
			}
			if (list1.get(r).equals("02")) {
				dlmc = "园地";
			}
			if (list1.get(r).equals("03")) {
				dlmc = "林地";
			}
			if (list1.get(r).equals("04")) {
				dlmc = "草地";
			}
			if (list1.get(r).equals("10")) {
				dlmc = "交通运输用地";
			}
			if (list1.get(r).equals("11")) {
				dlmc = "水域及水利设施用地";
			}
			if (list1.get(r).equals("12")) {
				dlmc = "其他土地";
			}
			if (list1.get(r).equals("20")) {
				dlmc = "城镇村及工矿用地";
			}
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("DLBM", list1.get(r));
			tempMap.put("DLMC", dlmc);
			tempMap.put("INTERSECT_AREA", 0);
			tempMap.put("INTERSECT_AREA_Q", 0);
			tempMap.put("INTERSECT_AREA_M", 0);
			resultList.add(tempMap);
		}
		double sum1 = 0;
		for (Map.Entry entry : map.entrySet()) {
			String key = (String) entry.getKey();
			double value1 = Double.parseDouble(entry.getValue().toString());
			sum1 += value1;
			for (int l = 0; l < resultList.size(); l++) {
				if (resultList.get(l).get("DLBM").equals(key)) {
					resultList.get(l).put("INTERSECT_AREA", Df.format(value1));
					resultList.get(l).put("INTERSECT_AREA_Q", Df.format(0.0001 * value1));
					resultList.get(l).put("INTERSECT_AREA_M", Df.format(0.0015 * value1));
				}
				if (resultList.get(l).get("DLMC").equals("面积")) {
					resultList.get(l).put("INTERSECT_AREA", Df.format(sum1));
					resultList.get(l).put("INTERSECT_AREA_Q", Df.format(0.0001 * sum1));
					resultList.get(l).put("INTERSECT_AREA_M", Df.format(0.0015 * sum1));
				}
			}
		}
		resultData.put("detailData", detailData.values());

		System.out.println("over");
	}

	public static boolean isExtraType(String code, String type) {
		if (StringUtils.isBlank(code))
			return false;
		String sourceCodes = "";
		// 农用地
		if ("nyd".equals(type)) {
			sourceCodes = "011,012,013,021,022,023,031,032,033,041,042,104,114,117,122,123";
		}
		// 建设用地
		else if ("jsyd".equals(type)) {
			sourceCodes = "101,102,105,106,107,113,118,201,202,203,204,205";
		}
		// 未利用地
		else if ("wlyd".equals(type)) {
			sourceCodes = "111,112,115,116,119,043,124,125,126,127";
		}
		// 耕地
		else if ("gd".equals(type)) {
			sourceCodes = "011,012,013";
		}
		//水田
		else if ("st".equals(type)) {
			sourceCodes = "011";
		}
		//水浇地
		else if ("sjd".equals(type)) {
			sourceCodes = "012";
		}
		//旱地
		else if ("hd".equals(type)) {
			sourceCodes = "013";
		}
		return sourceCodes.contains(code);
	}

	public static boolean isJiTi(String code) {
		if (!StringUtils.isBlank(code)) {
			if (code.startsWith("3") || code.startsWith("4")) {
				return true;
			}
		}
		return false;
	}
}
