package cn.gtmap.msurveyplat.common.util;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.BeanUtilsEx;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by trr on 2016/9/26.
 */
public class CommonUtil extends com.gtis.common.util.CommonUtil {

    private static Logger logger = Logger.getLogger(CommonUtil.class);

    public static String formatEmptyValue(Object object) {
        return object != null ? object.toString() : "";
    }

    public static String formatEmptyValue(Object object,String defalut) {
        return object != null ? object.toString() : defalut;
    }

    public static String getCurrStrDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String str = sdf.format(new Date());
        return str;
    }


    /**
     * @author <a href="mailto:liangqing@gtmap.cn;">liangqing</a>
     * @version 1.0, 2017-06-08
     * @description 封装三目运算符转换公共方法
     */
    public static String ternaryOperator(Object object) {
        return object != null ? object.toString() : "";
    }

    /**
     * @author <a href="mailto:liangqing@gtmap.cn;">liangqing</a>
     * @version 1.0, 2017-06-08
     * @description 封装三目运算符转换公共方法
     */
    public static String ternaryOperatorNotBlank(Object object) {
        String str = ternaryOperator(object);
        return StringUtils.isNotBlank(str) ? str : "/";
    }

    /**
     * @author <a href="mailto:xiejianan@gtmap.cn;">xiejianan</a>
     * @version 1.0, 2017-06-08
     * @description 封装三目运算符转换公共方法, 添加默认值写入
     */
    public static String ternaryOperator(Object object, String defaultValue) {
        String str = object != null ? object.toString() : ternaryOperator(defaultValue);
        return StringUtils.isNotBlank(str) ? str : ternaryOperator(defaultValue);
    }

    /**
     * @author <a href="mailto:xiejianan@gtmap.cn;">xiejianan</a>
     * @version 1.0, 2017-06-08
     * @description 封装三目运算符转换公共方法，适用于获取非字符串数据
     */
    public static <T> T ternaryOperator(Object object, Object defaultValue) {
        return object != null ? (T) object : (T) defaultValue;
    }

    /**
     * @author <a href="mailto:liangqing@gtmap.cn;">liangqing</a>
     * @version 1.0, 2017-08-30
     * @description 判断某个字符串在某一字符串中出现多少次
     */
    public static int getDisplayTimes(String str, String s) {
        int count = str.length() - str.replace(s, "").length();
        return count;
    }

    /**
     * @author <a href="mailto:liangqing@gtmap.cn;">liangqing</a>
     * @version 1.0, 2017-08-30
     * @description 获取某个字符在某个
     */
    public static int getCharacterPosition(String string, String s, int psition) {
        //这里是获取"-"符号的位置
        Matcher slashMatcher = Pattern.compile(s).matcher(string);
        int mIdx = 0;
        while (slashMatcher.find()) {
            mIdx++;
            if (mIdx == psition) {
                break;
            }
        }
        return slashMatcher.start();
    }


    @SuppressWarnings("unchecked")
    public static <T> T cast(Object obj) {
        return (T) obj;
    }

    /**
     * @param a
     * @param b
     * @return boolean
     * @author <a href ="mailto:liangqing@gtmap.cn"></a>
     * @version 1.3
     * @date 10:00 2017/11/21
     * @description 验证某个字符串是否包含在数组中
     */
    public static boolean indexOfStrs(String[] a, String b) {
        boolean msg = false;
        if (a != null) {
            for (String temp : a) {
                if (StringUtils.equals(temp, b)) {
                    msg = true;
                    break;
                }
            }
        }
        return msg;
    }

    /**
     * @param str String
     * @return boolean
     * @author <a href ="mailto:xiejianan@gtmap.cn"></a>
     * @date: Created in 17:03 2017/12/6
     * description 严格检测字符串匹配true，大小写不限
     * @version 1.3
     */
    public static boolean equalsExcatTrueIgnoreCase(String str) {
        return StringUtils.equalsIgnoreCase(str, String.valueOf(Boolean.TRUE));
    }

    /**
     * @param str String
     * @return boolean
     * @author <a href ="mailto:xiejianan@gtmap.cn"></a>
     * @date: Created in 17:03 2017/12/6
     * description 严格检测字符串匹配false，大小写不限
     * @version 1.3
     */
    public static boolean equalsExcatFalseIgnoreCase(String str) {
        return StringUtils.equalsIgnoreCase(str, String.valueOf(Boolean.FALSE));
    }

    public static String removeDuplicateStr(String str, String splitStr) {
        if (StringUtils.isNotBlank(str)) {
            LinkedHashSet<String> set = new LinkedHashSet();
            String[] strs = str.split(splitStr);
            StringBuilder finalStr = new StringBuilder();
            for (int i = 0; i < strs.length; i++) {
                set.add(strs[i]);
            }
            for (Object strTemp : set.toArray()) {
                if (finalStr.length() > 0) {
                    finalStr.append(splitStr);
                }
                finalStr.append(strTemp.toString());
            }
            return finalStr.toString();
        } else {
            return ternaryOperator(str);
        }
    }

    /**
     * @param param 查询参数 exceptions 不需要删除空格的参数条件
     * @return
     * @author <a href ="mailto:xiejianan@gtmap.cn"></a>
     * @version 1.3
     * @date 16:19 2018/4/18 0018
     * @description 处理查询参数中的空参数
     */
    public static Map<String, Object> removeEmptyParam(Map<String, Object> param, String... exceptions) {
        Iterator it = param.entrySet().iterator();
        Map<String, Object> newParam = new HashMap<String, Object>();
        List<String> exceptionList = new ArrayList<String>();
        if (exceptions != null) {
            exceptionList = Arrays.asList(exceptions);
        }
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            if (StringUtils.isNotBlank(String.valueOf(entry.getValue())) && !exceptionList.contains(String.valueOf(entry.getKey()))) {
                newParam.put(String.valueOf(entry.getKey()), StringUtils.deleteWhitespace(String.valueOf(entry.getValue())));
            } else if (StringUtils.isNotBlank(String.valueOf(entry.getValue())) && exceptionList.contains(String.valueOf(entry.getKey()))) {
                newParam.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
            }
        }
        return newParam;
    }

    /**
     * @param list      待分割的list
     * @param sizeLimit 每个分割后的列表的大小
     * @return 分割后的列表
     * @description 按列表大小将大列表分割为小列表
     */
    public static Map<String, List> seperateBigListSamllList(List list, int sizeLimit) {
        return seperateBigListSamllList(list, sizeLimit, "list");
    }

    /**
     * @param list      待分割的list
     * @param sizeLimit 每个分割后的列表的大小
     * @return 分割后的列表
     * @description 按列表大小将大列表分割为小列表
     */
    public static Map<String, List> seperateBigListSamllList(List list, int sizeLimit, String prefix) {
        Map<String, List> map = new LinkedHashMap<String, List>();
        if (CollectionUtils.isNotEmpty(list)) {
            // 获取列表长度
            int size = CollectionUtils.size(list);
            // 计算列表分割次数
            int times = size / sizeLimit + (size % sizeLimit > 0 ? 1 : 0);
            for (int i = 0; i < times; i++) {
                map.put(prefix + i, list.subList(i * sizeLimit, (i + 1) * sizeLimit < size ? (i + 1) * sizeLimit : size));
            }
        }
        return map;
    }

    /**
     * @Description: 获取URL中参数值
     * @Param:
     * @return:
     * @Author: ww
     * @Date: 2018/10/24
     */
    public static String getUrlParameters(String url, String para) {
        if (StringUtils.isBlank(url) || StringUtils.isBlank(para)) {
            return null;
        }
        String paraStr = url.contains("?") ? url.substring(url.indexOf("?") + 1) : "";
        String[] paraArr = StringUtils.split(paraStr, "&");
        if (paraArr.length > 0) {
            for (int i = 0; i < paraArr.length; i++) {
                String[] p = StringUtils.split(paraArr[i], "=");
                if (p.length > 0 && StringUtils.equals(p[0], para)) {
                    return p[1];
                }
            }
        }
        return null;
    }

    /**
     * 组织查询条件
     *
     * @param keyId
     * @param param
     * @return
     */
    public static String conbineQueryCondition(String keyId, Map<String, List> param) {
        StringBuilder sql = new StringBuilder("(");
        // 计数使用，用来判断是否需要拼接or
        int i = 0;
        // 拼接查询条件
        for (String key : param.keySet()) {
            if (i > 0) {
                sql.append(" or");
            }
            // 查询条件使用别名
            sql.append(keyId).append(" in (:").append(key).append(")");
            i++;
        }
        sql.append(")");
        return sql.toString();
    }

    /**
     * 将list 用key值分割为 key - list形式，简化后期的循环匹配过程，提高效率
     *
     * @param keyId
     * @param list
     * @return
     */
    public static Map<String, List<Map<String, Object>>> divideListToMap(String keyId, List<Map<String, Object>> list) {
        Map<String, List<Map<String, Object>>> map = new LinkedHashMap<String, List<Map<String, Object>>>();
        List<Map<String, Object>> tempList;
        String keyValue;
        if (CollectionUtils.isNotEmpty(list)) {
            for (Map<String, Object> row : list) {
                keyValue = CommonUtil.ternaryOperator(row.get(keyId));
                tempList = CommonUtil.ternaryOperator(map.get(keyValue), new ArrayList<Map<String, Object>>());
                tempList.add(row);
                map.put(keyValue, tempList);
            }
        }
        return map;
    }

    /***
     * 删除最后一个逗号
     * @param param
     * @return
     */
    public static void deleteLastComma(StringBuilder param) {
        if (param != null && param.indexOf(",") != -1) {
            param.deleteCharAt(param.lastIndexOf(","));
        }
    }


    /**
     * version 1.0
     *
     * @param
     * @return
     * @description 判断字符串是否为JSON格式字符串
     * @date 2019/3/14
     * @author <a href ="mailto:wangwei2@gtmap.cn">wangwei2</a>
     */
    public static boolean isJSONObject(String text) {
        if (StringUtils.isBlank(text)) {
            return false;
        }
        try {
            JSONObject.parseObject(text);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * version 1.0
     *
     * @param
     * @return
     * @description 判断字符串是否为JSON数组格式字符串
     * @date 2019/3/14
     * @author <a href ="mailto:wangwei2@gtmap.cn">wangwei2</a>
     */
    public static boolean isJSONOArray(String text) {
        if (StringUtils.isBlank(text)) {
            return false;
        }
        try {
            JSONArray.parseArray(text);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //获取字符串中的数字部分
    public static int getNumberByStr(String str) {
        str = str.trim();  // 删除字符串头尾空格
        if (str.length() == 0) return 0;
        int flag = 1;  // 符号位标识
        int rev = 0;  // 数值（无符号）
        int edge = Integer.MAX_VALUE / 10;  // 判断数值是否超过范围的边界线，这样写可以节省时间
        if (str.charAt(0) == '-') {
            flag = -1;
            str = str.substring(1, str.length());  // 跳过符号位，可不写第二参数
        } else if (str.charAt(0) == '+') {
            str = str.substring(1, str.length());  // 跳过符号位，可不写第二参数
        } else if (!(str.charAt(0) >= '0' && str.charAt(0) <= '9')) {  // 如果开始非空字符不为符号或数字，则直接返回 0
            return 0;
        }
        for (char s : str.toCharArray()) {
            if (s >= '0' && s <= '9') {
                int n = s - '0';  // 计算字符代表值
                if (rev >= edge) {  // 超过边界情况较少，故该判断写于外侧
                    if (flag == 1) {
                        if (rev > edge || n > 7) return Integer.MAX_VALUE;
                    } else {
                        if (rev > edge || n > 8) return Integer.MIN_VALUE;
                    }
                }
                rev = rev * 10 + n;
            } else {
                break;
            }
        }
        return rev * flag;
    }

    /**
     * @param src 源集合
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 深拷贝
     */
    public static <T> List<T> deepCopy(List<T> src) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(src);
        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        @SuppressWarnings("unchecked")
        List<T> dest = (List<T>) in.readObject();
        return dest;
    }

    public static String formatYNRSFM(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    //坑爹的ie会直接解析出zip文件的路径而非文件名所以需要处理一下路径
    public static String formatFileName(String fileName) {
        if (StringUtils.isNotBlank(fileName)) {
            //判断是否为IE浏览器的文件名，IE浏览器下文件名会带有盘符信息
            // Check for Unix-style path
            int unixSep = fileName.lastIndexOf('/');
            // Check for Windows-style path
            int winSep = fileName.lastIndexOf('\\');
            // Cut off at latest possible point
            int pos = (winSep > unixSep ? winSep : unixSep);
            if (pos != -1) {
                // Any sort of path separator found...
                fileName = fileName.substring(pos + 1);
            }
        }
        return fileName;
    }

    public static <T> Map<String, List<T>> divideObjByKey(List<T> list, String key) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        String keyValue;
        Map<String, List<T>> resultMap = Maps.newHashMap();
        List<T> dividedMapList;
        for (T obj : list) {
            keyValue = BeanUtilsEx.getProperty(obj, key);
            if (StringUtils.isNotBlank(keyValue)) {
                dividedMapList = (List<T>) MapUtils.getObject(resultMap, keyValue, Lists.<T>newArrayList());
                dividedMapList.add(obj);
                resultMap.put(keyValue, dividedMapList);
            }
        }
        return resultMap;
    }

    public static <T> Map<String, T> divideObjByPrimaryKey(List<T> list, String primaryKey) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        String keyValue;
        Map<String, T> resultMap = Maps.newHashMap();
        for (T obj : list) {
            keyValue = BeanUtilsEx.getProperty(obj, primaryKey);
            resultMap.put(keyValue, obj);
        }
        return resultMap;
    }

    public static boolean isJson(String jsonString) {
        if (StringUtils.isNotBlank(jsonString)) {
            try {
                JSONObject jsonObject = JSON.parseObject(jsonString);
                return true;
            } catch (Exception var2) {
                logger.info("字符串不是json串", var2);
                return false;
            }
        } else {
            return false;
        }
    }
}
