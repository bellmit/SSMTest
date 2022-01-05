package cn.gtmap.msurveyplat.common.util;


import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:zhangxing@gtmap.cn">zx</a>
 * @version V1.0, 15-4-25
 */
public class CalendarUtil {

    public static final FastDateFormat sdf_China = FastDateFormat.getInstance("yyyy年MM月dd日");
    public static final FastDateFormat sdf_China_Hour = FastDateFormat.getInstance("yyyy年MM月dd日HH时");
    public static final FastDateFormat sdf = FastDateFormat.getInstance("yyyy-MM-dd");
    public static final FastDateFormat sdf_HMS = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
    public static final FastDateFormat sdf_HM = FastDateFormat.getInstance("yyyy-MM-dd HH:mm");
    public static final FastDateFormat sdf_Year = FastDateFormat.getInstance("yyyy");
    public static final FastDateFormat sdf_Other = FastDateFormat.getInstance("yyyy/MM/dd HH:mm");
    public static final FastDateFormat sdf_time = FastDateFormat.getInstance("yyyyMMddHHmmss");
    public static final FastDateFormat sdf_point_YMD = FastDateFormat.getInstance("yyyy.MM.dd");
    public static final FastDateFormat sdf_point_YM = FastDateFormat.getInstance("yyyy.MM");
    public static final FastDateFormat sdf_time_ms = FastDateFormat.getInstance("yyyyMMddHHmmssSSS");
    //zwq gd_td的起始时间
    public static final FastDateFormat sdf_yMd = FastDateFormat.getInstance("yyyyMMdd");

    public static int weeks = 0;// 用来全局控制 上一周，本周，下一周的周数变化
    public static int maxDate; // 一月最大天数
    public static int maxYear; // 一年最大天数

    /**
     * 测试
     *
     * @param args
     */
    public static void main(String[] args) {
        CalendarUtil tt = new CalendarUtil();


        System.out.println("获取当天日期:" + tt.getNowTime("yyyy-MM-dd"));
        System.out.println("获取本周一日期:" + tt.getMondayOFWeek());
        System.out.println("获取本周日的日期~:" + tt.getCurrentWeekday());
        System.out.println("获取上周一日期:" + tt.getPreviousWeekday());
        System.out.println("获取上周日日期:" + tt.getPreviousWeekSunday());
        System.out.println("获取下周一日期:" + tt.getNextMonday());
        System.out.println("获取下周日日期:" + tt.getNextSunday());
        System.out.println("获得相应周的周六的日期:" + tt.getNowTime("yyyy-MM-dd"));
        System.out.println("获取本月第一天日期:" + tt.getFirstDayOfMonth());
        System.out.println("获取本月最后一天日期:" + tt.getDefaultDay());
        System.out.println("获取上月第一天日期:" + tt.getPreviousMonthFirst());
        System.out.println("获取上月最后一天的日期:" + tt.getPreviousMonthEnd());
        System.out.println("获取下月第一天日期:" + tt.getNextMonthFirst());
        System.out.println("获取下月最后一天日期:" + tt.getNextMonthEnd());
        System.out.println("获取本年的第一天日期:" + tt.getCurrentYearFirst());
        System.out.println("获取本年最后一天日期:" + tt.getCurrentYearFirst());
        System.out.println("获取去年的最后一天日期:" + tt.getPreviousYearEnd());
        System.out.println("获取明年第一天日期:" + tt.getNextYearFirst());
        System.out.println("获取明年最后一天日期:" + tt.getNextYearEnd());
        System.out.println("获取本季度第一天:" + tt.getThisSeasonFirstTime(11));
        System.out.println("获取本季度最后一天:" + tt.getThisSeasonFinallyTime(11));
        System.out.println("获取两个日期之间间隔天数2008-12-1~2008-9.29:"
                + CalendarUtil.getTwoDay("2008-12-1", "2008-9-29"));
        System.out.println("获取两个字符串时间段的间隔分钟数2008-12-1 13:12~2008-12-1 13:29:"
                + CalendarUtil.getTwoMinute("2008-12-1 13:12", "2008-12-1 13:29"));
        System.out.println("获取当前月的第几周：" + tt.getWeekOfMonth());
        System.out.println("获取当前年份：" + tt.getYear());
        System.out.println("获取当前月份：" + tt.getMonth());
        System.out.println("获取今天在本年的第几天：" + tt.getDayOfYear());
        System.out.println("获得今天在本月的第几天(获得当前日)：" + tt.getDayOfMonth());
        System.out.println("获得时间分钟数："+tt.getTime("08:30"));
        System.out.println("得到上个月多少天:" + tt.getLastMonthDays());
        System.out.println("获取字符串转日期型:" + tt.formatDate("1992年09月09日"));
        System.out.println("年份相减：" + addYears(CalendarUtil.formatDate("2019-12-2"), 20));
        System.out.println("日期转换字符串，格式是yyyyMMdd："+formatDateStr(new Date()));
        System.out.println("获取毫秒时间："+tt.getTimeMs());
        System.out.println("获取时间，格式yyyy/MM/dd HH:mm："+getPuTongDate(new Date()));
    }


    /**
     * 获取毫秒时间
     *
     * @return
     */
    public static String getTimeMs() {
        return sdf_time_ms.format(new Date());
    }

    /**
     * 获得当前年份
     *
     * @return
     */
    public static int getYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    /**
     * 获得当前月份
     *
     * @return
     */
    public static int getMonth() {
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }

    /**
     * 获得今天在本年的第几天
     *
     * @return
     */
    public static int getDayOfYear() {
        return Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
    }

    /**
     * 获得今天在本月的第几天(获得当前日)
     *
     * @return
     */
    public static int getDayOfMonth() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }


    /**
     * 获得今天是这个月的第几周
     *
     * @return
     */
    public static int getWeekOfMonth() {
        return Calendar.getInstance().get(Calendar.DAY_OF_WEEK_IN_MONTH);
    }

    //*************************比较日期*******************************

    /**
     * 得到二个日期间的间隔天数
     * sj1 - sj2
     *
     * @param sj1
     * @param sj2
     * @return
     */
    public static String getTwoDay(String sj1, String sj2) {
        long day = 0;
        try {
            Date date = sdf.parse(sj1);
            Date mydate = sdf.parse(sj2);
            day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
        } catch (Exception e) {
            return "";
        }
        return day + "";
    }

    /**
     * 获取两个时间段的间隔分钟数
     * sj1 - sj2
     *
     * @param sj1
     * @param sj2
     * @return
     */
    public static String getTwoMinute(String sj1, String sj2) {
        long day = 0;
        try {
            Date date = sdf_HM.parse(sj1);
            Date mydate = sdf_HM.parse(sj2);
            day = (date.getTime() - mydate.getTime()) / (60 * 1000);
        } catch (Exception e) {
            return "";
        }
        return day + "";
    }

    /**
     * 获取两个时间段的间隔分钟数
     * sj1 - sj2
     *
     * @param sj1
     * @param sj2
     * @return
     */
    public static long getTwoMinute(Date sj1, Date sj2) {
        long day = 0;
        try {
            day = (sj1.getTime() - sj2.getTime()) / (60 * 1000);
        } catch (Exception e) {
            return 0;
        }
        return day;
    }

    /**
     * 两个时间之间的天数
     * date1-date2
     *
     * @param date1
     * @param date2
     * @return
     */
    public static long getDays(String date1, String date2) {
        if (date1 == null || date1.equals(""))
            return 0;
        if (date2 == null || date2.equals(""))
            return 0;
        // 转换为标准时间
        Date date = null;
        Date mydate = null;
        try {
            date = sdf.parse(date1);
            mydate = sdf.parse(date2);
        } catch (Exception e) {
        }
        long day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
        return day;
    }

    /**
     * 获得当前日期与本周日相差的天数
     *
     * @return
     */
    public static int getMondayPlus() {
        Calendar cd = Calendar.getInstance();
        // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; // 因为按中国礼拜一作为第一天所以这里减1
        if (dayOfWeek == 0) {
            return -6;
        } else if (dayOfWeek == 1) {
            return 0;
        } else {
            return 1 - dayOfWeek;
        }
    }
    //*****************处理星期格式***********************

    /**
     * 根据一个日期，返回是星期几的字符串
     *
     * @param sdate
     * @return
     */
    public static String getWeek(String sdate) {
        // 再转换为时间
        Date date = CalendarUtil.formatDate(sdate);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        // int hour=c.get(Calendar.DAY_OF_WEEK);
        // hour中存的就是星期几了，其范围 1~7
        // 1=星期日 7=星期六，其他类推
        return new SimpleDateFormat("EEEE").format(c.getTime());
    }

    /**
     * 获得本周一的日期
     *
     * @return
     */
    public static String getMondayOFWeek() {
        weeks = 0;
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus);
        Date monday = currentDate.getTime();

        //		DateFormat df = DateFormat.getDateInstance();
        String preMonday = sdf.format(monday);
        return preMonday;
    }

    /**
     * 获得相应周的周六的日期
     *
     * @return
     */
    public static String getSaturday() {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks + 6);
        Date monday = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }

    /**
     * 获得上周星期日的日期
     *
     * @return
     */
    public static String getPreviousWeekSunday() {
        weeks = 0;
        weeks--;
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + weeks);
        Date monday = currentDate.getTime();
        String preMonday = sdf.format(monday);
        return preMonday;
    }

    /**
     * 获得上周星期一的日期
     *
     * @return
     */
    public static String getPreviousWeekday() {
        weeks = 0;
        weeks--;
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks);
        Date monday = currentDate.getTime();
        String preMonday = sdf.format(monday);
        return preMonday;
    }

    /**
     * 获得下周星期一的日期
     */
    public static String getNextMonday() {
        weeks++;
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7);
        Date monday = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }

    /**
     * 获得下周星期日的日期
     */
    public static String getNextSunday() {

        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 + 6);
        Date monday = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }
    //****************获取第一天、最后一天类型数据*****************************

    /**
     * 计算当月最后一天,返回字符串
     *
     * @return
     */
    public static String getDefaultDay() {
        String str = "";
        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
        lastDate.add(Calendar.MONTH, 1);// 加一个月，变为下月的1号
        lastDate.add(Calendar.DATE, -1);// 减去一天，变为当月最后一天
        str = sdf.format(lastDate.getTime());
        return str;
    }

    /**
     * 上月第一天
     *
     * @return
     */
    public static String getPreviousMonthFirst() {
        String str = "";
        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
        lastDate.add(Calendar.MONTH, -1);// 减一个月，变为下月的1号
        // lastDate.add(Calendar.DATE,-1);//减去一天，变为当月最后一天
        str = sdf.format(lastDate.getTime());
        return str;
    }

    /**
     * 获取当月第一天
     *
     * @return
     */
    public static String getFirstDayOfMonth() {
        String str = "";
        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
        str = sdf.format(lastDate.getTime());
        return str;
    }

    /**
     * 获得本周星期日的日期
     *
     * @return
     */
    public static String getCurrentWeekday() {
        weeks = 0;
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 6);
        Date monday = currentDate.getTime();

        //		DateFormat df = DateFormat.getDateInstance();
        String preMonday = sdf.format(monday);
        return preMonday;
    }

    /**
     * 获取当天时间
     *
     * @param dateformat
     * @return
     */
    public static String getNowTime(String dateformat) {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateformat);// 可以方便地修改日期格式
        String hehe = dateFormat.format(now);
        return hehe;
    }

    private static int getMonthPlus() {
        Calendar cd = Calendar.getInstance();
        int monthOfNumber = cd.get(Calendar.DAY_OF_MONTH);
        cd.set(Calendar.DATE, 1);// 把日期设置为当月第一天
        cd.roll(Calendar.DATE, -1);// 日期回滚一天，也就是最后一天
        maxDate = cd.get(Calendar.DATE);
        if (monthOfNumber == 1) {
            return -maxDate;
        } else {
            return 1 - monthOfNumber;
        }
    }

    /**
     * 获得上月最后一天的日期
     *
     * @return
     */
    public static String getPreviousMonthEnd() {
        String str = "";
        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.MONTH, -1);// 减一个月
        lastDate.set(Calendar.DATE, 1);// 把日期设置为当月第一天
        lastDate.roll(Calendar.DATE, -1);// 日期回滚一天，也就是本月最后一天
        str = sdf.format(lastDate.getTime());
        return str;
    }

    /**
     * 获得下个月第一天的日期
     *
     * @return
     */
    public static String getNextMonthFirst() {
        String str = "";
        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.MONTH, 1);// 减一个月
        lastDate.set(Calendar.DATE, 1);// 把日期设置为当月第一天
        str = sdf.format(lastDate.getTime());
        return str;
    }

    /**
     * 获得下个月最后一天的日期
     *
     * @return
     */
    public static String getNextMonthEnd() {
        String str = "";
        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.MONTH, 1);// 加一个月
        lastDate.set(Calendar.DATE, 1);// 把日期设置为当月第一天
        lastDate.roll(Calendar.DATE, -1);// 日期回滚一天，也就是本月最后一天
        str = sdf.format(lastDate.getTime());
        return str;
    }

    /**
     * 获得明年最后一天的日期
     *
     * @return
     */
    public static String getNextYearEnd() {
        String str = "";
        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.YEAR, 1);// 加一个年
        lastDate.set(Calendar.DAY_OF_YEAR, 1);
        lastDate.roll(Calendar.DAY_OF_YEAR, -1);
        str = sdf.format(lastDate.getTime());
        return str;
    }

    /**
     * 获得明年第一天的日期
     *
     * @return
     */
    public static String getNextYearFirst() {
        String str = "";
        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.YEAR, 1);// 加一个年
        lastDate.set(Calendar.DAY_OF_YEAR, 1);
        str = sdf.format(lastDate.getTime());
        return str;

    }

    /**
     * 获得本年有多少天
     *
     * @return
     */
    public static int getMaxYear() {
        Calendar cd = Calendar.getInstance();
        cd.set(Calendar.DAY_OF_YEAR, 1);// 把日期设为当年第一天
        cd.roll(Calendar.DAY_OF_YEAR, -1);// 把日期回滚一天。
        int MaxYear = cd.get(Calendar.DAY_OF_YEAR);
        return MaxYear;
    }

    public static int getYearPlus() {
        Calendar cd = Calendar.getInstance();
        int yearOfNumber = cd.get(Calendar.DAY_OF_YEAR);// 获得当天是一年中的第几天
        cd.set(Calendar.DAY_OF_YEAR, 1);// 把日期设为当年第一天
        cd.roll(Calendar.DAY_OF_YEAR, -1);// 把日期回滚一天。
        int MaxYear = cd.get(Calendar.DAY_OF_YEAR);
        if (yearOfNumber == 1) {
            return -MaxYear;
        } else {
            return 1 - yearOfNumber;
        }
    }

    /**
     * 获得本年第一天的日期
     *
     * @return
     */
    public static String getCurrentYearFirst() {
        int yearPlus = getYearPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, yearPlus);
        Date yearDay = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preYearDay = df.format(yearDay);
        return preYearDay;
    }

    // 获得上年最后一天的日期
    public static String getPreviousYearEnd() {
        weeks--;
        int yearPlus = getYearPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, yearPlus + maxYear * weeks
                + (maxYear - 1));
        Date yearDay = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preYearDay = df.format(yearDay);
        return preYearDay;
    }

    /**
     * 获得本季度第一天
     *
     * @param month
     * @return
     */
    public static String getThisSeasonFirstTime(int month) {
        int array[][] = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}, {10, 11, 12}};
        int season = 1;
        if (month >= 1 && month <= 3) {
            season = 1;
        }
        if (month >= 4 && month <= 6) {
            season = 2;
        }
        if (month >= 7 && month <= 9) {
            season = 3;
        }
        if (month >= 10 && month <= 12) {
            season = 4;
        }
        int start_month = array[season - 1][0];

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");// 可以方便地修改日期格式
        String years = dateFormat.format(date);
        int years_value = Integer.parseInt(years);

        int start_days = 1;// years+"-"+String.valueOf(start_month)+"-1";//getLastDayOfMonth(years_value,start_month);
        String seasonDate = years_value + "-" + start_month + "-" + start_days;
        return seasonDate;

    }

    /**
     * 获得本季度最后一天
     *
     * @param month
     * @return
     */
    public static String getThisSeasonFinallyTime(int month) {
        int array[][] = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}, {10, 11, 12}};
        int season = 1;
        if (month >= 1 && month <= 3) {
            season = 1;
        }
        if (month >= 4 && month <= 6) {
            season = 2;
        }
        if (month >= 7 && month <= 9) {
            season = 3;
        }
        if (month >= 10 && month <= 12) {
            season = 4;
        }
        int end_month = array[season - 1][2];

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");// 可以方便地修改日期格式
        String years = dateFormat.format(date);
        int years_value = Integer.parseInt(years);

        int start_days = 1;// years+"-"+String.valueOf(start_month)+"-1";//getLastDayOfMonth(years_value,start_month);
        int end_days = getLastDayOfMonth(years_value, end_month);
        String seasonDate = years_value + "-" + end_month + "-" + end_days;
        return seasonDate;

    }

    /**
     * 获取某年某月的最后一天
     *
     * @param year  年
     * @param month 月
     * @return 最后一天
     */
    public static int getLastDayOfMonth(int year, int month) {
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8
                || month == 10 || month == 12) {
            return 31;
        }
        if (month == 4 || month == 6 || month == 9 || month == 11) {
            return 30;
        }
        if (month == 2) {
            if (isLeapYear(year)) {
                return 29;
            } else {
                return 28;
            }
        }
        return 0;
    }

    /**
     * 是否闰年
     *
     * @param year 年
     * @return
     */
    public static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    /**
     * 是否闰年
     *
     * @param year
     * @return
     */
    public static boolean isLeapYear2(int year) {
        return new GregorianCalendar().isLeapYear(year);
    }

    /**
     * 获取当前年度，如：2010
     */
    public static String getCurrYear() {
        GregorianCalendar today = new GregorianCalendar();
        String curYear = today.get(GregorianCalendar.YEAR) + "";
        return curYear;
    }

    /**
     * 获取年月星期，如2009043
     */
    public static String getCurrYearMonthWeek() {
        GregorianCalendar today = new GregorianCalendar();
        String curYear = today.get(GregorianCalendar.YEAR) + "";
        String curMonth = today.get(GregorianCalendar.MONTH + 1) + "";
        String curWeek = today.get(GregorianCalendar.WEEK_OF_MONTH) + "";
        String CurrYearMonthWeek = curYear + curMonth + curWeek;
        return CurrYearMonthWeek;
    }

    /**
     * 计算两个日期天数之差date2-date1
     *
     * @param date1
     * @param date2
     * @return
     */
    public static Long getDaysByTwoDate(Date date1, Date date2) {
        //return date1.getTime() / (24*60*60*1000) - date2.getTime() / (24*60*60*1000);
        return date2.getTime() / 86400000 - date1.getTime() / 86400000;  //用立即数，减少乘法计算的开销
    }

    //-----------------"yyyy-MM-dd"格式处理方法----------------------

    /**
     * 将日期转换成yyyy-MM-dd字符串
     *
     * @param dateTime
     * @return
     */
    public static String formateDatetoStr(Date dateTime) {
        if (dateTime != null)
            return sdf.format(dateTime);
        else
            return "";
    }

    /**
     * 获取当前日期字符串，格式为：2010-11-08
     * yyyy-MM-dd
     *
     * @return
     */
    public static String getCurStrDate() {
        GregorianCalendar today = new GregorianCalendar();
        String str = sdf.format(today.getTime());
        return str;
    }

    /**
     * 获取当前日期的Date对象，格式：yyyy-MM-dd
     *
     * @return
     */
    public static Date getCurDate() {
        Date date = null;
        GregorianCalendar today = new GregorianCalendar();
        String str = sdf.format(today.getTime());
        try {
            date = sdf.parse(str);
        } catch (Exception e) {
            return null;
        }
        return date;
    }

    /**
     * 将日期进行格式化（参数类型java.util.Date），格式：yyyy-MM-dd
     *
     * @param date
     * @return
     */
    public static Date formatDate(Date date) {
        String str = sdf.format(date);
        try {
            date = sdf.parse(str);
        } catch (Exception e) {
            return null;
        }
        return date;
    }

    /**
     * 将日期进行格式化（参数类型java.util.Date），
     *
     * @param str
     * @return
     */
    public static Date formatDate(String str) {
        Date date = null;
        try {
            if (StringUtils.isNotBlank(str))
                date = sdf.parse(str);

        } catch (Exception e) {
            try {
                date = sdf_yMd.parse(str);
            } catch (ParseException e1) {
                try {
                    date = sdf_China.parse(str);
                } catch (ParseException e2) {
                    return null;
                }
            }
        }
        return date;
    }

    /**
     * 格式化日期yyMMdd成字符串
     * @param date
     * @return
     */
    public static String formatDateStr(Date date) {
        String str = sdf_yMd.format(date);

        return str;
    }
    /**
     * 格式化日期yyMMdd成字符串
     * @param date
     * @return
     */
    public static String formatYMdDateStr(Date date) {
        String str = sdf_yMd.format(date);

        return str;
    }
    /**
     * 将java.sql.Date日期格式化成'yyyy-MM-dd'格式字符串
     *
     * @param date
     * @return
     */
    public static String formateDate(java.sql.Date date) {
        String strDate = "";
        if (date != null) {
            strDate = sdf.format(date);
        }
        return strDate;
    }
    //-----------------"yyyy年MM月dd日"格式处理方法----------------------

    /**
     * 获取当前日期的字符串，格式："yyyy年MM月dd日"
     *
     * @return
     */
    public static String getCurChinaYMDStrDate() {
        GregorianCalendar today = new GregorianCalendar();
        String str = sdf_China.format(today.getTime());
        return str;
    }

    /**
     * 将java.util.Date日期格式化成'yyyy年MM月dd日'格式字符串
     *
     * @param date
     * @return
     */
    public static String formateToStrChinaYMDDate(Date date) {
        String strDate = "";
        if (date != null) {
            strDate = sdf_China.format(date);
        }
        return strDate;
    }


    /**
     * 将'(yyyy/MM/dd hh:mm)'的特殊日期字符串转换成'yyyy年MM月dd日'格式
     *
     * @param date
     * @return
     */
    public static String getStrDatefromSignDate(String date) {
        try {
            Date formatDate = sdf_Other.parse(date);
            return formateToStrChinaYMDDate(formatDate);
        } catch (Exception e) {
            return "年&nbsp;&nbsp;&nbsp;月&nbsp;&nbsp;&nbsp;日";
        }
    }
    //-----------------"yyyy-MM-dd HH:mm:ss"格式处理方法----------------------

    /**
     * 获取当前日期含时分秒字符串，格式为：yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getCurHMSStrDate() {
        GregorianCalendar today = new GregorianCalendar();
        String str = sdf_HMS.format(today.getTime());
        return str;
    }
    //-----------------"yyyy-MM-dd HH:mm"格式处理方法----------------------

    /**
     * 获取当前日期的Date对象（含时分），格式：yyyy-MM-dd HH:mm
     *
     * @return
     */
    public static Date getCurHMDate() {
        Date date = null;
        GregorianCalendar today = new GregorianCalendar();
        String str = sdf_HM.format(today.getTime());
        try {
            date = sdf_HM.parse(str);
        } catch (Exception e) {
            return null;
        }
        return date;
    }

    /**
     * 将日期进行格式化（参数类型java.util.Date），格式：yyyy-MM-dd HH:mm
     *
     * @param date
     * @return
     */
    public static Date formateToHMDate(Date date) {
        String str = sdf_HM.format(date);
        try {
            date = sdf_HM.parse(str);
        } catch (Exception e) {
            return null;
        }
        return date;
    }

    /**
     * 将java.util.Date日期格式化成'yyyy-MM-dd HH:mm'格式字符串
     *
     * @param date
     * @return
     */
    public static String formateToStrHMDate(Date date) {
        String strDate = "";
        if (date != null) {
            strDate = sdf_HM.format(date);
        }
        return strDate;
    }

    /**
     * 将'yyyy-MM-dd HH:mm'格式字符串格式化成Date对象
     *
     * @param dateString
     * @return
     */
    public static Date formateDateByHMDate(String dateString) {
        Date formateDate = null;
        try {
            formateDate = sdf_HM.parse(dateString);
        } catch (ParseException e) {
            formateDate = getCurDate();
        }
        return formateDate;
    }
    //-----------------""EEE, d MMM yyyy HH:mm:ss z""格式处理方法----------------------

    /**
     * 获取通用的日期Date对象，格式：Mon Nov 08 02:11:13 GMT 2010，用于javascript脚本中解析日期
     * "yyyy/MM/dd HH:mm"
     *
     * @param date
     * @return
     */
    public static Date getPuTongDate(Date date) {
        String str = sdf_Other.format(date);
        try {
            return sdf_Other.parse(str);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            return null;
        }
    }
    //-----------------"yyyy"格式处理方法----------------------

    /**
     * 获得本年最后一天的日期 *
     *
     * @return
     */
    public static String getCurYearEndStrDate() {
        Date date = new Date();
        String years = sdf_Year.format(date);
        return years + "-12-31";
    }

    /**
     * 获取日期的年度字符串，格式：yyyy
     *
     * @param date
     * @return
     */
    public static String formatYearToStr(Date date) {
        if (date == null) {
            return sdf_Year.format(new Date());
        }
        return sdf_Year.format(date);
    }

    /**
     * 获取当前日期的年度字符串，格式：2010
     */
    public static String getCurStrYear() {
        return sdf_Year.format(new Date());
    }

    public static String date2China(Date d) {
        String date = "";
        // Date t=new Date();
        Date t = d;
        SimpleDateFormat fmat = new SimpleDateFormat("yyyy-MM-dd");
        String s = String.format(fmat.format(t));
        String[] str = s.split("-");
        for (int i = 0; i < str.length; i++) {
            System.out.println(str[i]);
            char[] c = str[i].toCharArray();
            for (int j = 0; j < c.length; j++) {
                switch (c[j]) {
                    case '0':
                        if (i > 0) {
                            date += "";
                        } else {
                            date += "〇";
                        }
                        break;
                    case '1':
                        if (j == 0 && i != 0) {
                            date += "十";
                        } else {
                            date += "一";
                        }
                        break;
                    case '2':
                        if (j == 0 && i != 0) {
                            date += "二十";
                        } else {
                            date += "二";
                        }
                        break;
                    case '3':
                        if (j == 0 && i != 0) {
                            date += "三十";
                        } else {
                            date += "三";
                        }
                        break;
                    case '4':
                        date += "四";
                        break;
                    case '5':
                        date += "五";
                        break;
                    case '6':
                        date += "六";
                        break;
                    case '7':
                        date += "七";
                        break;
                    case '8':
                        date += "八";
                        break;
                    case '9':
                        date += "九";
                        break;
                    default:
                        break;
                }
            }
            if (i == 0)
                date += "年";
            if (i == 1)
                date += "月";
            if (i == 2)
                date += "日";
        }
        return date;
    }

    /**
     * 格式化日期格式为 yyyy-MM-dd-
     *
     * @param date
     * @return
     */
    public static String formatDateToStyle(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (date != null) {
            return dateFormat.format(date);
        }
        return "";
    }

    /**
     * 比较是否在日期之前，true:date1在date2之前。false:date1在date2之后
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean beforeDate(Date date1, Date date2) {
        if (date1 != null && date2 != null) {
            return date1.before(date2);
        }
        return false;
    }

    /**
     * 连接日期格式是 yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @param time
     * @return
     */
    public static Date concatDate(Date date, String time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (date != null && time != null) {
            try {
                return dateFormat.parse(formatDateToString(date) + " " + time);
            } catch (ParseException e) {

            }
        }
        return null;
    }

    /**
     * 格式化日期格式为 yyyy-MM-dd
     *
     * @param date
     * @return
     */
    public static String formatDateToString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (date != null) {
            return dateFormat.format(date);
        }
        return "";
    }

    /**
     * 格式化时间格式为HH:mm:ss
     *
     * @param date
     * @return
     */
    public static String formatTime(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        if (date != null) {
            return dateFormat.format(date);
        }
        return "";
    }

    /**
     * 比较日期，true:date1在date2之前。false:date1在date2之后
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int compareToDate(Date date1, Date date2) {
        if (date1 != null && date2 != null) {
            return date1.compareTo(date2);
        }
        return 0;
    }

    /**
     * 获取日期年份
     *
     * @param date
     * @return
     */
    public static String getDateYear(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        if (date != null) {
            return dateFormat.format(date);
        }
        return "";
    }

    /**
     * 获取日期月份
     *
     * @param date
     * @return
     */
    public static String getDateMonth(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM");
        if (date != null) {
            return dateFormat.format(date);
        }
        return "";
    }

    /**
     * 获取日期小时
     *
     * @param date
     * @return
     */
    public static String getDateHour(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH");
        if (date != null) {
            return dateFormat.format(date);
        }
        return "";
    }

    /**
     * 获取日期天数
     *
     * @param date
     * @return
     */
    public static String getDateDay(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
        if (date != null) {
            return dateFormat.format(date);
        }
        return "";
    }

    /**
     * 格式化日期格式为 yyyy-MM-dd  HH:mm:ss
     *
     * @param date
     * @return
     */
    public static String formatDateTime(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (date != null) {
            return dateFormat.format(date);
        }
        return "";
    }

    /**
     * 根据两个日期算天数
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    public static int stateDays(String beginDate, String endDate) {
        int days = 0;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate1 = null;
        try {
            beginDate1 = format.parse(beginDate);
            Date endDate1 = format.parse(endDate);
            long day = (endDate1.getTime() - beginDate1.getTime()) / (24 * 60 * 60 * 1000);
            days = (int) day;
            //            System.out.println("相隔的天数="+days);
        } catch (ParseException e) {
        }

        return days;
    }

    /*
    * 年份加减
    *
    * */
    public static Date addYears(Date date, int years) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, years);
        return calendar.getTime();
    }

    /**
     * 日期加天
     *
     * @param date
     * @param days
     * @return
     */
    public static String addDay(String date, int days) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = formatDate(date);
        return df.format(new Date(d1.getTime() + ((long) days * 24 * 60 * 60 * 1000)));
    }

    /**
     * 日期减天
     *
     * @param date
     * @param days
     * @return
     */
    public static String subDay(String date, int days) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = formatDate(date);
        return df.format(new Date(d1.getTime() - ((long) days * 24 * 60 * 60 * 1000)));
    }

    /**
     * 将字符窜形式的日期转换成 年 月 号
     */
    public static String forTime(String time) {
        String[] a = time.split("-");
        String year = a[0];
        String month = a[1];
        String date = a[2];
        String da = year + "年" + month + "月" + date + "号";
        return da;
    }

    /**
     * 将字符窜形式的日期转换成 年 月 日
     */
    public static String forNYRTime(String time){
        if(StringUtils.isNotBlank(time)){
            String[] a = time.split("-");
            if(a.length == 3) {
                String year = a[0];
                String month = a[1];
                String date = a[2];
                String da = year + "年" + month + "月" + date + "日";
                return da;
            }
        }
        return time;
    }

    /**
     * 得到时间日期的分钟数
     *
     * @param sj1
     * @return
     */
    public static long getTime(String sj1) {
        long min = 0;
        try {
            GregorianCalendar currentDate = new GregorianCalendar();
            Date date = sdf_HMS.parse(formatDateToString(currentDate.getTime()) + " " + sj1 + ":00");
            Date mydate = sdf_HMS.parse(formatDateToString(currentDate.getTime()) + " 00:00:00");
            min = (date.getTime() - mydate.getTime()) / (60 * 1000);
        } catch (Exception e) {
            return min;
        }
        return min;
    }

    /**
     * 得到上个月多少天
     *
     * @return
     */
    public static int getLastMonthDays() {
        int dateOfMonth = 0;
        try {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, getYear());
            cal.set(Calendar.MONTH, getMonth() - 1);//Java月份才0开始算
            dateOfMonth = cal.getActualMaximum(Calendar.DATE);
        } catch (Exception e) {
            return dateOfMonth;
        }
        return dateOfMonth;
    }

    /**
     * 获取几天后日期
     *
     * @return
     */
    public static Date addDays(String days) {
        //当前N天后的日期
        Date date = new Date();//取时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE, Integer.parseInt(days));//把日期往后增加N天.整数往后推,负数往前移动
        date = calendar.getTime();   //这个时间就是日期往后推N天的结果
        return date;
    }

    /**
     * 得到几天前的时间
     *
     * @param d
     * @param day
     * @return
     */
    public static Date getDateBefore(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
        return now.getTime();
    }

    /**
     * 得到几天后的时间
     *
     * @param d
     * @param day
     * @return
     */
    public static Date getDateAfter(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
        return now.getTime();
    }

    /**
     * 将日期进行格式化对象（参数类型java.util.Date），格式：yyyy-MM-dd HH:mm:ss"
     *
     * @param str
     * @return
     */
    public static Date formatObjectToDate(Object str) {
        Date date = null;
        try {
            if (str != null && StringUtils.isNotBlank(str.toString()))
                date = sdf_HMS.parse(str.toString());

        } catch (Exception e) {
            try {
                date = sdf_HMS.parse(str.toString());
            } catch (ParseException e1) {
                try {
                    date = sdf_HMS.parse(str.toString());
                } catch (ParseException e2) {
                    return null;
                }
            }
        }
        return date;
    }

    /**
     * 获得上年第一天的日期 *
     *
     * @return
     */
    public String getPreviousYearFirstStrDate() {
        Date date = new Date();
        String years = sdf_Year.format(date);
        int years_value = Integer.parseInt(years);
        years_value--;
        return years_value + "-1-1";
    }

	/**
	 * @author <a href="mailto:yinyao@gtmap.cn">yinyao</a>
	 * @param
	 * @rerutn
	 * @description 获取当前日期 时分秒 返回日期型  yyyy-mm-dd hh:mm:ss
	 */
	public static Date getCurHMSDate() {
		Date date = null;
		GregorianCalendar today = new GregorianCalendar();
		String str = sdf_HMS.format(today.getTime());
		try {
			date = sdf_HMS.parse(str);
		} catch (Exception e) {
			return null;
		}
		return date;
	}

	/**
	 * @author <a href="mailto:yinyao@gtmap.cn">yinyao</a>
	 * @param
	 * @rerutn
	 * @description 将String类型值变为Date类型 精确到时分秒
	 */
	public static Date getHMSDateByString(String time) {
		Date date = null;
		try {
			date = sdf_HMS.parse(time);
		} catch (Exception e) {
		}
		return date ;
	}

    /**
     * 根据老日期和年月日参数得出新的日期,"+"表示加，“-”表示减
     *
     * @param SourceDate
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static Date getNewDay(Date SourceDate, int year,int month,int day) {
        Date newDate=null;
        Calendar newCalendar = Calendar.getInstance();
        newCalendar.setTime(SourceDate);
        newCalendar.add(Calendar.YEAR,year);//-1日期减1年
        newCalendar.add(Calendar.MONTH,month);//+3日期加3个月
        newCalendar.add(Calendar.DAY_OF_YEAR,day);//+10日期加10天
        newDate=newCalendar.getTime();
        return newDate;
    }
}
