package com.xwl.prisonbreak.util;

import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    public static String FORMAT_DATE_YYYYMMDD = "yyyyMMdd";

    /**
     * 日期格式：yyyy-MM-dd
     */
    public static String FORMAT_DATE_YYYY_MM_DD = "yyyy-MM-dd";

    /**
     * 时间格式：hhmmss
     */
    public static String FORMAT_DATE_hhmmss = "hhmmss";

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

    public static SimpleDateFormat getDateTimeFormat() {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat();
        dateTimeFormat.applyPattern("yyyyMMddHHmmssSSS");
        return dateTimeFormat;
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
}