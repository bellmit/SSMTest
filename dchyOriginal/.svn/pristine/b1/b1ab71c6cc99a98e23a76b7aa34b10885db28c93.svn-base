package cn.gtmap.onemap.platform.utils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.util.*;

/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 12-12-20 下午5:48
 */
public final class ArrayUtils {


    public static final String DEFAULT_SEPARATOR = ",";

    public enum TYPE {
        key, value
    }

    /**
     * 判断当前数组是否为空
     *
     * @param values
     * @return
     */
    public static final boolean empty(String[] values) {
        if (values == null) return true;
        for (String value : values) {
            if (StringUtils.isNotBlank(value)) return false;
        }
        return true;
    }

    /**
     * 查看数组中是否包含目标字符串
     *
     * @param src
     * @param des
     * @param ignoreCase
     * @return
     */
    public static final boolean contains(String[] src, String des, boolean ignoreCase) {
        for (String item : src) {
            if (StringUtils.isNotBlank(item) && StringUtils.isNotBlank(des)) {
                if (ignoreCase) {
                    if (item.equalsIgnoreCase(des)) {
                        return true;
                    }
                } else {
                    if (item.equals(des)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 向目标数组中拷贝新数据
     *
     * @param des
     * @param src
     * @return
     */
    public static final String[] add2Arrays(String[] des, String src) {
        if (des == null) return new String[]{src};
        String[] tmp = new String[des.length + 1];
        System.arraycopy(des, 0, tmp, 0, des.length);
        tmp[des.length] = src;
        return tmp;
    }

    /**
     * 向目标数组中拷贝新数组
     *
     * @param des
     * @param src
     * @return
     */
    public static final String[] add2Arrays(String[] des, String[] src) {
        if (des == null) return src;
        if (src == null) return des;
        String[] tmp = new String[des.length + src.length];
        System.arraycopy(src, 0, tmp, 0, src.length);
        System.arraycopy(des, 0, tmp, src.length, des.length);
        return tmp;
    }

    /***
     * 将数组转换成string
     * @param ig
     * @param separator
     * @return
     */
    public static String arrayToString(String[] ig, String separator) {
        String str = "";
        if (ig != null && ig.length > 0) {
            for (int i = 0; i < ig.length; i++) {
                str += ig[i] + separator == null ? separator : DEFAULT_SEPARATOR;
            }
        }
        str = str.substring(0, str.length() - separator.length());
        return str;
    }

    /**
     * 将list转换成string
     *
     * @param list
     * @param separator
     * @return
     */
    public static String listToString(List list, String separator) {
        StringBuilder sb = new StringBuilder();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (i <= list.size() - 2) {
                    sb.append(list.get(i));
                    sb.append(separator != null ? separator : DEFAULT_SEPARATOR);
                } else {
                    sb.append(list.get(i));
                }
            }
        }
        return sb.toString();
    }

    /**
     * 将list按照groupKey分组成Map<String,List>
     *
     * @param list
     * @param groupKey
     * @return
     */
    public static Map listConvertMap(List list, String groupKey) {
        assert list != null;
        assert groupKey != null;
        Map<String, List<Map>> map = new HashMap<String, List<Map>>();
        if (list.size() > 0) {
            for (Object obj : list) {
                Map item = (Map) obj;
                if (!item.containsKey(groupKey)) continue;
                String key = MapUtils.getString(item, groupKey);
                if (map.containsKey(key)) {
                    List list1 = map.get(key);
                    list1.add(item);
                    map.put(key, list1);
                } else {
                    List list2 = new ArrayList();
                    list2.add(item);
                    map.put(key, list2);
                }
            }
            return map;
        }
        return null;
    }

    /***
     *
     * @param list
     * @param groupKey
     * @return
     */
    public static LinkedHashMap listConvertLinkedMap(List<Map> list, String groupKey) {
        assert list != null;
        assert groupKey != null;
        LinkedHashMap<String, List<Map>> map = new LinkedHashMap<String, List<Map>>();
        if (list.size() > 0) {
            for (Map item : list) {
                String key = MapUtils.getString(item, groupKey);
                if (map.containsKey(key)) {
                    List list1 = map.get(key);
                    list1.add(item);
                    map.put(key, list1);
                } else {
                    List list2 = new ArrayList();
                    list2.add(item);
                    map.put(key, list2);
                }
            }
            return map;
        }
        return null;
    }

    /**
     * map convert to list
     *
     * @param map
     * @param type
     * @return
     */
    public static List mapConvertList(Map map, TYPE type) {
        assert map != null;
        assert type != null;
        List list = new ArrayList<Object>();
        if (type.equals(TYPE.key)) {
            for (Object key : map.keySet()) {
                list.add(key);
            }
        } else {
            for (Object entry : map.entrySet()) {
                Object val = ((Map.Entry) entry).getValue();
                list.add(val == null ? "" : val);
            }
        }
        return list;
    }


    /**
     * 深拷贝
     *
     * @param src
     * @param <T>
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static <T> List<T> deepCopy(List<T> src) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        if (CollectionUtils.isNotEmpty(src)){
            out.writeObject(src);
        }
        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        @SuppressWarnings("unchecked")
        List<T> dest = (List<T>) in.readObject();
        return dest;
    }


}
