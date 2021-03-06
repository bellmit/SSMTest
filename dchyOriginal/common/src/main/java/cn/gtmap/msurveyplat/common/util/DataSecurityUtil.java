package cn.gtmap.msurveyplat.common.util;

import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglChdw;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglChgc;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglMlk;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglYhdw;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2020/12/15
 * @description TODO
 */
public class DataSecurityUtil {

    //数据加密
    public static <T> void encryptObjectList(List<T> objectList) {

        if (CollectionUtils.isNotEmpty(objectList)) {
            for (Object object : objectList) {
                encryptSingleObject(object);
            }
        }
    }

    public static <T> void encryptSingleObject(T object) {
        if (null != object) {
            //名录库数据加密
            if (object instanceof DchyXmglMlk) {
                ((DchyXmglMlk) object).setLxr(SM4Util.encryptData_ECB(((DchyXmglMlk) object).getLxr()));
                ((DchyXmglMlk) object).setLxdh(SM4Util.encryptData_ECB(((DchyXmglMlk) object).getLxdh()));
                ((DchyXmglMlk) object).setTyshxydm(SM4Util.encryptData_ECB(((DchyXmglMlk) object).getTyshxydm()));
            }

            //测绘工程数据加密
            if (object instanceof DchyXmglChgc) {
                ((DchyXmglChgc) object).setLxr(SM4Util.encryptData_ECB(((DchyXmglChgc) object).getLxr()));
                ((DchyXmglChgc) object).setLxdh(SM4Util.encryptData_ECB(((DchyXmglChgc) object).getLxdh()));

            }

            //测绘单位数据加密
            if (object instanceof DchyXmglChdw) {
                ((DchyXmglChdw) object).setLxr(SM4Util.encryptData_ECB(((DchyXmglChdw) object).getLxr()));
                ((DchyXmglChdw) object).setLxdh(SM4Util.encryptData_ECB(((DchyXmglChdw) object).getLxdh()));
                ((DchyXmglChdw) object).setTyshxydm(SM4Util.encryptData_ECB(((DchyXmglChdw) object).getTyshxydm()));

            }

            //用户单位数据加密
            if (object instanceof DchyXmglYhdw) {
                DchyXmglYhdw dchyXmglYhdw = (DchyXmglYhdw) object;
                dchyXmglYhdw.setTyshxydm(SM4Util.encryptData_ECB(dchyXmglYhdw.getTyshxydm()));
                dchyXmglYhdw.setYhzjhm(SM4Util.encryptData_ECB(dchyXmglYhdw.getYhzjhm()));
                dchyXmglYhdw.setFrzjhm(SM4Util.encryptData_ECB(dchyXmglYhdw.getFrzjhm()));
            }
        }
    }

    public static void encryptMapList(List<Map<String, Object>> mapList) {
        if (CollectionUtils.isNotEmpty(mapList)) {
            for (Map map : mapList) {
                if (map.containsKey("LXR") || map.containsKey("LXDH") || map.containsKey("TYSHXYDM")) {
                    String lxr = CommonUtil.formatEmptyValue(map.get("LXR"));
                    String lxdh = CommonUtil.formatEmptyValue(map.get("LXDH"));
                    String tyshxydm = CommonUtil.formatEmptyValue(map.get("TYSHXYDM"));

                    if (StringUtils.isNoneBlank(lxr)){
                        map.put("LXR", SM4Util.encryptData_ECB(lxr));
                    }
                    if (StringUtils.isNoneBlank(lxdh)){
                        map.put("LXDH", SM4Util.encryptData_ECB(lxdh));
                    }
                    if (StringUtils.isNoneBlank(tyshxydm)){
                        map.put("TYSHXYDM", SM4Util.encryptData_ECB(tyshxydm));
                    }
                }else if (map.containsKey("lxr") || map.containsKey("lxdh") || map.containsKey("tyshxydm")){
                    String lxr = CommonUtil.formatEmptyValue(map.get("lxr"));
                    String lxdh = CommonUtil.formatEmptyValue(map.get("lxdh"));
                    String tyshxydm = CommonUtil.formatEmptyValue(map.get("tyshxydm"));

                    if (StringUtils.isNoneBlank(lxr)){
                        map.put("lxr", SM4Util.encryptData_ECB(lxr));
                    }
                    if (StringUtils.isNoneBlank(lxdh)){
                        map.put("lxdh", SM4Util.encryptData_ECB(lxdh));
                    }
                    if (StringUtils.isNoneBlank(tyshxydm)){
                        map.put("tyshxydm", SM4Util.encryptData_ECB(tyshxydm));
                    }
                }
            }
        }
    }

    //数据解密
    public static <T> void decryptObjectList(List<T> objectList) {
        if (CollectionUtils.isNotEmpty(objectList)) {
            for (Object object : objectList) {
                decryptSingleObject(object);
            }
        }
    }

    public static <T> void decryptSingleObject(T object) {
        if (null != object) {
            //名录库数据加密
            if (object instanceof DchyXmglMlk) {
                ((DchyXmglMlk) object).setLxr(SM4Util.decryptData_ECB(((DchyXmglMlk) object).getLxr()));
                ((DchyXmglMlk) object).setLxdh(SM4Util.decryptData_ECB(((DchyXmglMlk) object).getLxdh()));
                ((DchyXmglMlk) object).setTyshxydm(SM4Util.decryptData_ECB(((DchyXmglMlk) object).getTyshxydm()));
            }

            //测绘工程数据加密
            if (object instanceof DchyXmglChgc) {
                ((DchyXmglChgc) object).setLxr(SM4Util.decryptData_ECB(((DchyXmglChgc) object).getLxr()));
                ((DchyXmglChgc) object).setLxdh(SM4Util.decryptData_ECB(((DchyXmglChgc) object).getLxdh()));

            }

            //测绘单位数据加密
            if (object instanceof DchyXmglChdw) {
                ((DchyXmglChdw) object).setLxr(SM4Util.decryptData_ECB(((DchyXmglChdw) object).getLxr()));
                ((DchyXmglChdw) object).setLxdh(SM4Util.decryptData_ECB(((DchyXmglChdw) object).getLxdh()));
                ((DchyXmglChdw) object).setTyshxydm(SM4Util.decryptData_ECB(((DchyXmglChdw) object).getTyshxydm()));

            }

            //用户单位数据加密
            if (object instanceof DchyXmglYhdw) {
                DchyXmglYhdw dchyXmglYhdw = (DchyXmglYhdw) object;
                dchyXmglYhdw.setTyshxydm(SM4Util.decryptData_ECB(dchyXmglYhdw.getTyshxydm()));
                dchyXmglYhdw.setYhzjhm(SM4Util.decryptData_ECB(dchyXmglYhdw.getYhzjhm()));
                dchyXmglYhdw.setFrzjhm(SM4Util.decryptData_ECB(dchyXmglYhdw.getFrzjhm()));
            }
        }
    }

    public static void decryptMapList(List<Map<String, Object>> mapList) {
        if (CollectionUtils.isNotEmpty(mapList)) {
            for (Map map : mapList) {
                if(null != map){
                    if (map.containsKey("LXR") || map.containsKey("LXDH") || map.containsKey("TYSHXYDM") ||  map.containsKey("YHZJHM") ||  map.containsKey("FRZJHM")) {
                        String lxr = CommonUtil.formatEmptyValue(map.get("LXR"));
                        String lxdh = CommonUtil.formatEmptyValue(map.get("LXDH"));
                        String tyshxydm = CommonUtil.formatEmptyValue(map.get("TYSHXYDM"));
                        String yhzjhm = CommonUtil.formatEmptyValue(map.get("YHZJHM"));
                        String frzjhm = CommonUtil.formatEmptyValue(map.get("FRZJHM"));
                        map.put("LXR", SM4Util.decryptData_ECB(lxr));
                        map.put("LXDH", SM4Util.decryptData_ECB(lxdh));
                        map.put("TYSHXYDM", SM4Util.decryptData_ECB(tyshxydm));
                        map.put("YHZJHM", SM4Util.decryptData_ECB(yhzjhm));
                        map.put("FRZJHM", SM4Util.decryptData_ECB(frzjhm));
                    }else if (map.containsKey("lxr") || map.containsKey("lxdh") || map.containsKey("tyshxydm") ||  map.containsKey("yhzjhm") ||  map.containsKey("frzjhm")){
                        String lxr = CommonUtil.formatEmptyValue(map.get("lxr"));
                        String lxdh = CommonUtil.formatEmptyValue(map.get("lxdh"));
                        String tyshxydm = CommonUtil.formatEmptyValue(map.get("tyshxydm"));
                        String yhzjhm = CommonUtil.formatEmptyValue(map.get("yhzjhm"));
                        String frzjhm = CommonUtil.formatEmptyValue(map.get("frzjhm"));
                        map.put("lxr", SM4Util.decryptData_ECB(lxr));
                        map.put("lxdh", SM4Util.decryptData_ECB(lxdh));
                        map.put("tyshxydm", SM4Util.decryptData_ECB(tyshxydm));
                        map.put("yhzjhm", SM4Util.decryptData_ECB(yhzjhm));
                        map.put("frzjhm", SM4Util.decryptData_ECB(frzjhm));
                    }
                }
            }
        }
    }
}
