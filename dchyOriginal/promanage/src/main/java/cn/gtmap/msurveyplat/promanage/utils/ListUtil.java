package cn.gtmap.msurveyplat.promanage.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2021/5/21 16:46
 * @description
 */
public class ListUtil {

    /**
     * 比较两个list集合的元素是否完全一样
     * @param list1
     * @param list2
     * @return
     */
    public static boolean isDiffrent(List<String> list1, List<String> list2) {
        Map<String, Integer> map = new HashMap<String, Integer>(list1.size() + list2.size());
        List<String> maxList = list1;
        List<String> minList = list2;

        if(list1.size() != list2.size()){
            return false;
        }

        for (String string : maxList) {
            map.put(string, 1);
        }
        for (String string : minList) {
            Integer cc = map.get(string);
            if (cc != null) {
                map.put(string, ++cc);
                continue;
            }
            return false;
        }
        return true;
    }

    /**
     * 求出两个集合的不同元素
     * @param list1
     * @param list2
     * @return
     */
    public static List<String> getDiffrent(List<String> list1, List<String> list2) {
        List<String> diff = new ArrayList<String>();
        List<String> maxList = list1;
        List<String> minList = list2;
        if(list2.size()>list1.size())
        {
            maxList = list2;
            minList = list1;
        }
        Map<String,Integer> map = new HashMap<String,Integer>(maxList.size());
        for (String string : maxList) {
            map.put(string, 1);
        }
        for (String string : minList) {
            if(map.get(string)!=null)
            {
                map.put(string, 2);
                continue;
            }
            diff.add(string);
        }
        for(Map.Entry<String, Integer> entry:map.entrySet())
        {
            if(entry.getValue()==1)
            {
                diff.add(entry.getKey());
            }
        }
        return diff;

    }

}
