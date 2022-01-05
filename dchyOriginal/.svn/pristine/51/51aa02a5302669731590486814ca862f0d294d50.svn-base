package cn.gtmap.onemap.platform.utils;

import cn.gtmap.onemap.platform.Constant;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-6-3 下午1:49
 */
public final class Utils {

    public static final int REGION_CODE_MAX_LENGTH = 6;

    private static final Map<String, String> wellKnownSimplePrefixes = new HashMap<String, String>(4);

    private static final Map<String, String> wellKnownSimpleRegexs = new HashMap<String, String>(4);

    static String regExOfchinese = "[\\u4e00-\\u9fa5]";
    static Pattern pattern = Pattern.compile(regExOfchinese);

    static {
        wellKnownSimplePrefixes.put("}", "{");
        wellKnownSimplePrefixes.put("]", "[");
        wellKnownSimplePrefixes.put(")", "(");

        wellKnownSimpleRegexs.put("${", "\\$\\{+|\\}");
        wellKnownSimpleRegexs.put("{", "\\{+|\\}");
    }


    /**
     * formate region code
     *
     * @param value
     * @return
     */
    public static final String formatRegionCode(String value) {
        Assert.notNull(value, "region code can't be null");
        final int maxLength = REGION_CODE_MAX_LENGTH;
        StringBuilder sb = new StringBuilder();
        sb.append(value);
        if (StringUtils.startsWith(sb.toString(), "3")) {
            while (sb.length() < maxLength) {
                sb.append("0");
            }
        }
        return sb.toString();
    }


    /**
     * 替换占位符${}
     *
     * @param data
     * @param params
     * @return
     * @throws Exception
     */
    public static String replaceHolder(String data, Map<String, Object> params) throws Exception {
        String result = data;
        String[] values = data.split("\\$\\{+|\\}");
        for (int i = 0; i < values.length; i++) {
            if (params.containsKey(values[i]))
                result = result.replaceAll("\\$\\{" + values[i] + "\\}", String.valueOf(params.get(values[i])));
        }
        return result;
    }

    /***
     * 替换占位符${0}..
     * @param data
     * @param params  不定参数 对应 字符串中的　0,1,2..
     * @return
     * @throws Exception
     */
    public static String replaceHolder(String data, Object... params) throws Exception {
        String result = data;
        String[] values = data.split("\\$\\{+|\\}");
        for (int i = 0; i < values.length; i++) {
            if (params[i] != null)
                result = result.replaceAll("\\$\\{" + values[i] + "\\}", String.valueOf(params[i]));
        }
        return result;
    }

    /**
     * replace placeholder with custom prefix /suffix
     *
     * @param placeholderPrefix
     * @param placeholderSuffix
     * @param data
     * @param params
     * @return
     * @throws Exception
     */
    public static String replaceHolder(String placeholderPrefix, String placeholderSuffix, String data, Map<String, Object> params) throws Exception {
        String result = data;
        String simplePrefixForSuffix = wellKnownSimplePrefixes.get(placeholderSuffix);
        if (simplePrefixForSuffix != null && placeholderPrefix.endsWith(simplePrefixForSuffix)) {
            placeholderPrefix = simplePrefixForSuffix;
        }
        String[] values = data.split(wellKnownSimpleRegexs.get(placeholderPrefix) == null ? "\\$\\{+|\\}" : wellKnownSimpleRegexs.get(placeholderPrefix));
        for (int i = 0; i < values.length; i++) {
            if (params.containsKey(values[i]))
                result = result.replaceAll("\\{" + values[i] + "\\}", String.valueOf(params.get(values[i])));
        }
        return result;
    }


    /***
     * 替换url占位符${} 并对替换的参数值进行编码
     * @param data
     * @param params
     * @return
     * @throws Exception
     */
    public static String urlReplaceHolder(String data, Map<String, Object> params) throws Exception {
        String result = data;
        String[] values = data.split("\\$\\{+|\\}");
        for (int i = 0; i < values.length; i++) {
            if (params.containsKey(values[i]))
                result = result.replaceAll("\\$\\{" + values[i] + "\\}", URLEncoder.encode(MapUtils.getString(params, values[i]), Constant.UTF_8));
        }
        return result;
    }

    /***
     * update/create hash map
     * @param key
     * @param value
     * @param map
     * @return
     */
    public static Map updateMap(Object key, Object value, Map map) {
        Map result = new HashMap();
        result.put(key, value);
        if (map != null && !map.isEmpty()) {
            result.putAll(map);
        }
        return result;
    }

    /***
     * contain chinese char
     * @param value
     * @return
     */
    public static boolean isContainChinese(String value) {
        Matcher matcher = pattern.matcher(value);
        boolean flg = false;
        if (matcher.find()) {
            flg = true;
        }
        return flg;
    }

    /***
     *
     * @param orig
     * @param dest
     * @return
     * @throws Exception
     */
    public static Object copyPropertyForSave(Object orig, Object dest) throws Exception {
        Field[] fieldArr = orig.getClass().getDeclaredFields();
        for (int i = 0; i < fieldArr.length; i++) {
            Field field = fieldArr[i];
            String type = fieldArr[i].getGenericType().toString(); // 获取属性的类型
            String fieldName = field.getName();
            String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            String setMethodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            if (type.equals("class java.lang.String")) { // 如果type是类类型，则前面包含"class "，后面跟类名
                Method method = orig.getClass().getMethod(getMethodName);
                String value = (String) method.invoke(orig); // 调用getter方法获取属性值
                if (StringUtils.isNotBlank(value)) {
                    method = dest.getClass().getMethod(setMethodName, String.class);
                    method.invoke(dest, value);
                }
            }

            if (type.equals("class java.math.BigDecimal")) { // 如果type是类类型，则前面包含"class "，后面跟类名
                Method method = orig.getClass().getMethod(getMethodName);
                BigDecimal value = (BigDecimal) method.invoke(orig); // 调用getter方法获取属性值
                if (value != null) {
                    method = dest.getClass().getMethod(setMethodName, BigDecimal.class);
                    method.invoke(dest, value);
                }
            }
            if (type.equals("class java.util.Date")) { // 如果type是类类型，则前面包含"class "，后面跟类名
            	Method method = orig.getClass().getMethod(getMethodName);
            	Date value = (Date) method.invoke(orig); // 调用getter方法获取属性值
            	if (value != null) {
            		method = dest.getClass().getMethod(setMethodName, Date.class);
            		method.invoke(dest, value);
            	}
            }

        }
        return dest;
    }

    /***
     * url encode
     * @param url
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String urlEncode(String url) {
        try {
            return URLEncoder.encode(url, Constant.UTF_8);
        } catch (UnsupportedEncodingException e) {

        }
        return url;
    }


    /**
     * Double 相加
     *
     * @return Double
     * @应用
     */
    public static Double doubleAdd(Double v1, Double v2) {
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return new Double(b1.add(b2).doubleValue());
    }


    /**
     * Double 相加
     *
     * @return Double
     * @应用
     */
    public static Double doubleAddThree(Double v1, Double v2, Double v3) {
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        BigDecimal b3 = new BigDecimal(v3.toString());
        Double add1 = new Double(b1.add(b2).doubleValue());
        BigDecimal addB1 = new BigDecimal(add1.toString());
        return new Double(addB1.add(b3).doubleValue());
    }

    /**
     * double 相减
     *
     * @param d1
     * @param d2
     * @return
     */
    public static double sub(double d1, double d2) {
        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));
        return bd1.subtract(bd2).doubleValue();
    }

    /**
     * double 乘法
     *
     * @param d1
     * @param d2
     * @return
     */
    public static double mul(double d1, double d2) {
        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));
        return bd1.multiply(bd2).doubleValue();
    }


    /**
     * double 除法
     *
     * @param d1
     * @param d2
     * @param scale 四舍五入 小数点位数
     * @return
     */
    public static double div(double d1, double d2, int scale) {
        //  当然在此之前，你要判断分母是否为0，
        //  为0你可以根据实际需求做相应的处理
        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));
        return bd1.divide(bd2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /***
     *list 对某个字段进行求和
     *
     * @param list
     * @return
     */
    public static double getSumAreaByList(List<Map> list, String key) {
        double sum = 0;
        if (list != null && list.size() > 0) {
            for (Map map : list) {
                sum += MapUtils.getDoubleValue(map, key, 0.0);
            }
        }
        return sum;
    }

}
