package com.vcredit.vmc.rules.drools.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Description:时间工具类
 * <p>
 */
public class DateUtils {
    public static final String DATE_PATTERN_YYMM = "yyyyMM";
    public static final String DATE_PATTERN_YYMMDD = "yyyyMMdd";
    public static final String DATE_PATTERN_YYYY_MM = "yyyy-MM";
    public static final String DATE_PATTERN_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String DATE_PATTERN_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static final String DATE_PATTERN_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_PATTERN_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";


    /**
     * 获取中国日历
     */
    public static Calendar calendar() {
        Calendar cal = GregorianCalendar.getInstance(Locale.CHINESE);
        cal.setFirstDayOfWeek(2);
        return cal;
    }

    /**
     * 新增天数
     */
    public static Date addDay(Date date, int intValueExact) {
        Calendar cal = calendar();
        cal.setTime(date);
        cal.add(Calendar.DATE, intValueExact);
        return cal.getTime();
    }

    /**
     * 新增分钟
     */
    public static Date addMinute(Date date, int intValueExact) {
        Calendar cal = calendar();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, intValueExact);
        return cal.getTime();
    }

    /**
     * 获取年
     *
     * @param date 参数
     *
     * @return 年
     */
    public static String getYear(Date date) {
        if (date == null) {
            throw new RuntimeException("参数date不能为空！");
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        return sdf.format(date);
    }

    /**
     * 获取月
     *
     * @param date 参数
     *
     * @return 月
     */
    public static String getMonth(Date date) {
        if (date == null) {
            throw new RuntimeException("参数date不能为空！");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("MM");
        return sdf.format(date);
    }

    /**
     * 获取日
     *
     * @param date 参数
     *
     * @return 日
     */
    public static String getDay(Date date) {
        if (date == null) {
            throw new RuntimeException("参数date不能为空！");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        return sdf.format(date);
    }

    /**
     * 按照指定的格式转换日期
     *
     * @param sourceDate 日期
     * @param pattern    格式
     *
     * @return 日期字符串
     */
    public static String dateToString(Date sourceDate, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(sourceDate);
    }

    /**
     * 按照默认的格式转换日期
     *
     * @param sourceDate 日期
     *
     * @return 日期字符串
     */
    public static String dateToString(Date sourceDate) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN_YYYY_MM_DD_HH_MM_SS);
        return sdf.format(sourceDate);
    }

    /**
     * 字符串转Date，按照指定pattern格式
     *
     * @param dateStr 字符串格式时间
     * @param pattern 转换模板
     *
     * @return 转换后的Date实例
     * @throws ParseException
     */
    public static Date stringToDate(String dateStr, String pattern) {
        if (StringUtils.isBlank(dateStr)) {
            throw new RuntimeException("Parameters cannot be [null] , give them a appropriate value");
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }


    /**
     * 返回此刻时间字符串
     *
     * @return 当前时间字符串格式
     */
    public static String now() {
        return dateToString(new Date());
    }

    /**
     * 返回此刻时间字符串
     *
     * @return 当前时间字符串格式
     */
    public static String nowDate() {
        return dateToString(new Date(), DATE_PATTERN_YYYY_MM_DD);
    }

    /**
     * 格式化时间
     *
     * @param time
     *
     * @return
     */
    public static String formatConsumeTime(long time) {
        int sec = 1000;
        if (time <= sec) {
            return time + "毫秒";
        }
        int minute = 60 * sec;
        if (time <= minute) {
            return time / sec + "秒" + time % sec + "毫秒";
        }
        int hour = 60 * minute;
        if (time <= hour) {
            return time / minute + "分" + time / sec % 60 + "秒" + time % sec + "毫秒";
        }
        int day = 12 * hour;
        if (time <= 12 * 60 * 60 * 1000) {
            return time / hour + "小时" + time / minute % 60 + "分" + time / sec % 60 + "秒" + time % sec + "毫秒";
        }
        return time / day + "天" + time / hour % 12 + "小时" + time / minute % 60 + "分" + time / sec % 60 + "秒" + time % sec + "毫秒";
    }

}