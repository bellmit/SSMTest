package cn.gtmap.onemap.platform.utils;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 12-11-1 上午10:41
 */
public final class DateUtils {

    public static final String DEFAULT_TIME_FORMATE = "yyyy-MM-dd HH:mm:ss";

    public static final String DEFAULT_DATE_FORMATE = "yyyy-MM-dd";

    /**
     * 当前年份
     *
     * @return
     */
    public static final String getCurrentYear() {
        Calendar calendar = Calendar.getInstance();
        return String.valueOf(calendar.get(Calendar.YEAR));
    }

    /**
     * 格式化当前时间
     *
     * @param formate yyyyMMdd HHmmss
     * @return
     */
    public static final String getCurrentTime(String formate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(StringUtils.isNotBlank(formate) ? formate : DEFAULT_TIME_FORMATE);
        return dateFormat.format(new Date());
    }

    /***
     * 格式化日期
     * @param date
     * @return
     */
    public static final String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMATE);
        return dateFormat.format(date);
    }

    /***
     * 格式化时间
     * @param datetime
     * @param formate
     * @return
     */
    public static final String formatDateTime(Date datetime,String formate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(StringUtils.isNotBlank(formate) ? formate : DEFAULT_TIME_FORMATE);
        return dateFormat.format(datetime);
    }

    /**
     * 获取当月第一天
     * @return
     */
    public static final Date getFistDayCurrentMonth(){
        return  DateTime.now().dayOfMonth().withMinimumValue().secondOfDay().withMinimumValue().toDate();
    }

    /**
     * 获取当月最后一天
     * @return
     */
    public static final Date getLastDayCurrentMonth(){
        return  DateTime.now().dayOfMonth().withMaximumValue().secondOfDay().withMaximumValue().toDate();
    }

    /**
     * 判断时间是否是小于等于今天
     * @param date
     * @return
     */
    public static final boolean isLessOrEqualToday(Date date){
        DateTime d1 = new DateTime(date).hourOfDay().withMinimumValue();
        DateTime d2 = DateTime.now();
        if(Days.daysBetween(d1, d2).getDays()>0){
            return true;
        }
        return false;
    }
}
