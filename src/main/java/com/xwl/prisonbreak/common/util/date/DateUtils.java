package com.xwl.prisonbreak.common.util.date;

import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Auther: xuwulin
 * @Date: 2019/5/15 17:02
 * @Description: 时间工具
 */
@Slf4j
public class DateUtils {

    /**
     * 日期格式：yyyyMMdd
     */
    public final static String FORMAT_DATE_YYYYMMDD = "yyyyMMdd";

    /**
     * 日期格式：yyyy-MM-dd
     */
    public final static String FORMAT_DATE_YYYY_MM_DD = "yyyy-MM-dd";

    /**
     * 时间格式：hhmmss
     */
    public final static String FORMAT_DATE_hhmmss = "hhmmss";

    /**
     * 时间格式：yyyy/MM/dd HH:mm:ss
     */
    public final static String DEFAULT_PATTERN = "yyyy/MM/dd HH:mm:ss";

    /**
     * 格式化日期
     * @param date 日期
     * @param pattern 格式化
     * @return 输出字符串格式
     */
    public static String format(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    /**
     * 得到当前系统日期，日期格式为："yyyy-MM-dd"
     *
     * @return String
     */
    public static String getCurrentDate() {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        return df.format(new Date());
    }

    /**
     * 获取当前系统日期，格式："yyyyMMdd" 如:20180608
     *
     * @return
     */
    public static String getCurrentDateString() {
        String pattern = FORMAT_DATE_YYYYMMDD;
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        return df.format(new Date());
    }

    public static SimpleDateFormat getDateTimeFormat() {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat();
        dateTimeFormat.applyPattern("yyyyMMddHHmmssSSS");
        return dateTimeFormat;
    }

    /**
     * 获取当前系统时间戳
     *
     * @return
     */
    public static Date getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * 根据时间返回时间戳
     *
     * @param date
     * @return
     */
    public static Timestamp getTimestamp(Date date) {
        return new Timestamp(date.getTime());
    }

    /**
     * 根据时间返回时间戳字符串
     *
     * @param date
     * @return
     */
    public static String getCurrentTimeStamp(Date date) {
        return getDateTimeFormat().format(date);
    }

    /**
     * 获取当前系统时间戳字符串
     *
     * @return
     */
    public static String getCurrentTimestampString() {
        String timestamp = new Timestamp(System.currentTimeMillis()).toString();
        return "to_timestamp('" + timestamp + "','yyyy-mm-dd hh24:mi:ss.ff')";
    }

    /**
     * 输入符合格式要求的日期字符串，返回日期类型变量
     *
     * @param dateString
     * @return Date
     * @throws ParseException
     */
    public static Date getDate(String dateString) throws ParseException {
        Date date = null;
        if (dateString != null && !"".equals(dateString)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                    "yyyy-MM-dd");
            date = simpleDateFormat.parse(dateString);
            log.info("date:" + date.toString());
        }
        log.info("dateString:" + dateString);

        return date;
    }

    /**
     * 输入日期类型变量，返回日期字符串
     * 如:2018-06-08 15:46:09
     *
     * @param date
     * @return String
     */
    public static String getDateString(Date date) {
        String dateString = null;
        if (date != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateString = simpleDateFormat.format(date);
        }
        return dateString;
    }

    /**
     * 输入日期类型变量，返回日期int
     *
     * @param date
     * @return String
     */
    public static Integer getDateInteger(Date date) {
        String dateString = getDateString(date);
        Integer dateNum = Integer.parseInt(dateString.replace("-", ""));
        return dateNum;
    }

    /**
     * 最小时间
     * @param calendar
     */
    private static void setMinTime(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    /**
     * 最大时间
     * @param calendar
     */
    private static void setMaxTime(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getActualMaximum(Calendar.MILLISECOND));
    }

    /**
     * 获取输入date的月份的时间范围
     *
     * @param date
     * @return
     */
    public static DateRange getMonthRange(Date date) {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(date);
        startCalendar.set(Calendar.DAY_OF_MONTH, 1);
        setMaxTime(startCalendar);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(date);
        endCalendar.set(Calendar.DAY_OF_MONTH, endCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        setMaxTime(endCalendar);

        return new DateRange(startCalendar.getTime(), endCalendar.getTime());
    }

    /**
     * 获取昨天的时间范围
     *
     * @return
     */
    public static DateRange getYesterdayRange() {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.add(Calendar.DAY_OF_MONTH, -1);
        setMinTime(startCalendar);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.add(Calendar.DAY_OF_MONTH, -1);
        setMaxTime(endCalendar);

        return new DateRange(startCalendar.getTime(), endCalendar.getTime());
    }

    /**
     * 获取上个月的时间范围
     *
     * @return
     */
    public static DateRange getLastMonth() {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.add(Calendar.MONTH, -1);
        startCalendar.set(Calendar.DAY_OF_MONTH, 1);
        setMinTime(startCalendar);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.add(Calendar.MONTH, -1);
        endCalendar.set(Calendar.DAY_OF_MONTH, endCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        setMaxTime(endCalendar);

        return new DateRange(startCalendar.getTime(), endCalendar.getTime());
    }

    /**
     * 获取当前月份的时间范围
     *
     * @return
     */
    public static DateRange getThisMonth() {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(Calendar.DAY_OF_MONTH, 1);
        setMinTime(startCalendar);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(Calendar.DAY_OF_MONTH, endCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        setMaxTime(endCalendar);

        return new DateRange(startCalendar.getTime(), endCalendar.getTime());
    }

    /**
     * 获取上个季度的时间范围
     *
     * @return
     */
    public static DateRange getLastQuarter() {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(Calendar.MONTH, ((int) startCalendar.get(Calendar.MONTH) / 3 - 1) * 3);
        startCalendar.set(Calendar.DAY_OF_MONTH, 1);
        setMinTime(startCalendar);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(Calendar.MONTH, ((int) endCalendar.get(Calendar.MONTH) / 3 - 1) * 3 + 2);
        endCalendar.set(Calendar.DAY_OF_MONTH, endCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        setMaxTime(endCalendar);

        return new DateRange(startCalendar.getTime(), endCalendar.getTime());
    }

    /**
     * 获取当前季度的时间范围
     *
     * @return current quarter
     */
    public static DateRange getThisQuarter() {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(Calendar.MONTH, ((int) startCalendar.get(Calendar.MONTH) / 3) * 3);
        startCalendar.set(Calendar.DAY_OF_MONTH, 1);
        setMinTime(startCalendar);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(Calendar.MONTH, ((int) startCalendar.get(Calendar.MONTH) / 3) * 3 + 2);
        endCalendar.set(Calendar.DAY_OF_MONTH, endCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        setMaxTime(endCalendar);

        return new DateRange(startCalendar.getTime(), endCalendar.getTime());
    }



}