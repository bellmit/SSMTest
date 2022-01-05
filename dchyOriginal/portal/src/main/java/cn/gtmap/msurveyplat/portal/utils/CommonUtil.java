package cn.gtmap.msurveyplat.portal.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonUtil {

    public static synchronized String getCurrentTimeMillisId() {
        try {
            Thread.sleep(1L);
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        String id = "";
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        id = simpleDateFormat.format(date);
        return id;
    }
}
