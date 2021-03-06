package cn.gtmap.msurveyplat.serviceol.utils;

import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Created by trr on 2016/9/26.
 */
public class CommonUtil extends DateUtils {

    /**
     * 将list 用key值分割为 key - list形式，简化后期的循环匹配过程，提高效率
     *
     * @param keyId
     * @param list
     * @return
     */
    public static Map<String, List<Map<String, Object>>> divideListToMap(String keyId, List list) {
        Map<String, List<Map<String, Object>>> map = Maps.newHashMap();
        List<Map<String, Object>> tempList;
        String keyValue;
        if (CollectionUtils.isNotEmpty(list)) {
            Map rowMap;
            for (Object row : list) {
                rowMap = (Map) row;
                keyValue = formatEmptyValue(rowMap.get(keyId));
                tempList = formatEmptyValue(map.get(keyValue), new ArrayList<Map<String, Object>>());
                tempList.add(rowMap);
                map.put(keyValue, tempList);
            }
        }
        return map;
    }

    /**
     * 把Blob类型转换为byte数组类型
     *
     * @param blob
     * @return
     */
    public static byte[] blobToBytes(Blob blob) {
        if (null != blob) {
            BufferedInputStream is = null;

            try {
                is = new BufferedInputStream(blob.getBinaryStream());

                byte[] bytes = new byte[(int) blob.length()];

                int len = bytes.length;

                int offset = 0;

                int read = 0;

                if (null != is) {
                    while (offset < len && (read = is.read(bytes, offset, len - offset)) >= 0) {
                        offset += read;

                    }
                }
                return bytes;

            } catch (Exception e) {
                return null;

            } finally {
                try {
                    if (null != is) {
                        is.close();

                        is = null;
                    }

                } catch (IOException e) {
                    return null;
                }
            }
        } else {
            return null;
        }
    }

    public static String formatEmptyValue(Object object) {
        return object != null ? object.toString() : "";
    }

    public static <T> T formatEmptyValue(T object, T defaultObj) {
        return object != null ? object : defaultObj;
    }


    public static List<Integer> getNdkp(){
        List<Integer> dataList  = new ArrayList<>();
        Calendar date = Calendar.getInstance();
        Integer year = Integer.valueOf(date.get(Calendar.YEAR));
        Integer start = 2020;
        while ((start <= year)){
            dataList.add(start);
            start += 1;
        }
        return dataList;
    }

}
