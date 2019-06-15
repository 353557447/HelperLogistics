package com.shuiwangzhijia.wuliu.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by wangsuli on 2017/8/9.
 */

public class DateUtils {
    /**
     * 返回给定时间搓。
     */
    public static Long getMonthTime(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        Date da = null;
        try {
            da = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.i("tag", date);
        return da.getTime();
    }

    /**
     * 返回给定时间搓。
     *
     * @param date
     */
    public static Long getTime(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date da = null;
        try {
            da = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return da.getTime() / 1000;
    }

    /**
     * 返回当前时间字符串。
     * <p>
     * 格式：yyyy-MM-dd
     *
     * @return String 指定格式的日期字符串.
     */
    public static String getCurrentDate() {
        return getFormatDateTime(new Date(), "yyyy-MM-dd HH:mm:ss");
    }


    /**
     * 根据给定的格式与时间(Date类型的)，返回时间字符串<br>
     *
     * @param date   指定的日期
     * @param format 日期格式字符串
     * @return String 指定格式的日期字符串.
     */
    public static String getFormatDateTime(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 根据给定的格式与时间(Date类型的)，返回时间字符串
     *
     * @param date 指定的日期
     * @return String 指定格式的日期字符串.
     */
    public static String getFormatDateTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    public static String getFormatDateStr(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(new Date(time));
    }
    public static String getFormatDate(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(time));
    }

    public static Date getDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date mDate = null;
        try {
            mDate = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return mDate;
    }

    //获取 年月日
    public static String getYMDTime(long time) {
        String brith_StrTime = null;
        SimpleDateFormat sdf = null;
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        brith_StrTime = sdf.format(new Date(time));
        return brith_StrTime;
    }

    //获取 月日
    public static String getMDTime(long time) {
        String brith_StrTime = null;
        SimpleDateFormat sdf = null;
        sdf = new SimpleDateFormat("MM-dd");
        brith_StrTime = sdf.format(new Date(time));
        return brith_StrTime;
    }

    /**
     * 获取month后秒值
     *
     * @param month
     * @return
     */
    public static long addMonthDateTime(long date, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(date));
        calendar.add(Calendar.MONTH, month);
        return calendar.getTime().getTime();
    }

    public static long addHoursDateTime(Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, hours);
        return calendar.getTime().getTime();
    }

    /**
     * 增加days天
     *
     * @param days
     * @return
     */
    public static String addDaysDateTime(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return getFormatDateTime(calendar.getTime());
    }

    /**
     * 增加days天
     *
     * @param days
     * @return
     */
    public static Date getDateTime(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    public static String getWeekandTime(Date date) {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("E");
        str = sdf.format(date);
        sdf = new SimpleDateFormat("HH:mm");
        str = str + "   " + sdf.format(date);
        return str;
    }

    /**
     * 获取星期的数字角标
     *
     * @return
     */
    public static int getWeekIndex() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar.get(Calendar.DAY_OF_WEEK) - 1;
    }

    /**
     * 获取时间戳
     *
     * @param yymmdd
     * @param hhmm
     * @return
     */
    public static long getTimeRubbing(String yymmdd, String hhmm) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = null;
        try {
            date = sdr.parse(yymmdd + " " + hhmm);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    /**
     * 转化
     *
     * @param yymmdd
     * @param hhmm
     * @return
     */
    public static Date parseDate(String yymmdd, String hhmm) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = null;
        try {
            date = sdr.parse(yymmdd + " " + hhmm);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 判断是否为今天(效率比较高)
     *
     * @param date 传入的 时间
     * @return true今天 false不是
     */
    public static boolean IsToday(long date) {
        String today = getYMDTime(System.currentTimeMillis());
        return getYMDTime(date).equals(today);
    }

}
