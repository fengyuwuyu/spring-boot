package com.ll.spring.boot.core.util;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.ll.spring.boot.core.consts.Consts;

public class DateUtil {

    public static final long ONE_DAY_TIME = 24 * 3600 * 1000L;
	private static final String TIME_FORMAT = "HH:mm:ss";
    public static ThreadLocal<SimpleDateFormat> timer = ThreadLocal.withInitial(() -> {
    	return new SimpleDateFormat(TIME_FORMAT);
    });

    /**
     * 获取YYYY格式
     *
     * @return
     */
    public static String getYear() {
        return formatDate(new Date(), "yyyy");
    }

    /**
     * 获取YYYY格式
     *
     * @return
     */
    public static String getYear(Date date) {
        return formatDate(date, "yyyy");
    }

    /**
     * 获取YYYY-MM-DD格式
     *
     * @return
     */
    public static String getDay() {
        return formatDate(new Date(), Consts.DATE_PATTERN);
    }

    /**
     * 获取YYYY-MM-DD格式
     *
     * @return
     */
    public static String getDayTime() {
        return formatDate(new Date(), Consts.DATE_TIME_PATTERN_FILE);
    }

    public static int getDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public static String getDayStrOfWeek(Date date) {
        int day = getDayOfWeek(date);
        switch (day) {
        case 1:
            return "星期天";
        case 2:
            return "星期一";
        case 3:
            return "星期二";
        case 4:
            return "星期三";
        case 5:
            return "星期四";
        case 6:
            return "星期五";
        case 7:
            return "星期六";
        default:
            return "";
        }
    }

    /**
     * 获取YYYY-MM-DD格式
     *
     * @return
     */
    public static String getDayAndTime() {
        return formatDate(new Date(), Consts.DATE_TIME_PATTERN);
    }
    
    public static long getCurrTime2OneDayEnd() {
    	Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);      
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis() - System.currentTimeMillis();
    }
    
    /**
     * 获取YYYY-MM-DD格式
     *
     * @return
     */
    public static String getDay(Date date) {
        return formatDate(date, Consts.DATE_PATTERN);
    }

    /**
     * 获取YYYYMMDD格式
     *
     * @return
     */
    public static String getDays() {
        return formatDate(new Date(), "yyyyMMdd");
    }

    /**
     * 获取YYYYMMDD格式
     *
     * @return
     */
    public static String getDays(Date date) {
        return formatDate(date, "yyyyMMdd");
    }

    /**
     * 获取java.sql.Time类
     *
     * @return
     */
    public static Time getSqlTime(Date date) {
    	SimpleDateFormat format = timer.get();
    	String tmp = format.format(date);
        return new Time(parse(tmp, TIME_FORMAT).getTime());
    }
    
    /**
     * 获取YYYY-MM-DD HH:mm:ss格式
     *
     * @return
     */
    public static String getTime() {
        return formatDate(new Date(), Consts.DATE_TIME_PATTERN);
    }

    /**
     * 获取YYYY-MM-DD HH:mm:ss.SSS格式
     *
     * @return
     */
    public static String getMsTime() {
        return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss.SSS");
    }

    /**
     * 获取YYYYMMDDHHmmss格式
     *
     * @return
     */
    public static String getAllTime() {
        return formatDate(new Date(), "yyyyMMddHHmmss");
    }

    /**
     * 获取YYYY-MM-DD HH:mm:ss格式
     *
     * @return
     */
    public static String getTime(Date date) {
        return formatDate(date, Consts.DATE_TIME_PATTERN);
    }

    public static String formatDate(Date date, String pattern) {
        String formatDate = null;
        if (StringUtils.isNotBlank(pattern)) {
            formatDate = DateFormatUtils.format(date, pattern);
        } else {
            formatDate = DateFormatUtils.format(date, Consts.DATE_PATTERN);
        }
        return formatDate;
    }

    /**
     * @Title: compareDate
     * @Description:(日期比较，如果s>=e 返回true 否则返回false)
     * @param s
     * @param e
     * @return boolean
     * @throws @author
     *             luguosui
     */
    public static boolean compareDate(String s, String e) {
        if (parseDate(s) == null || parseDate(e) == null) {
            return false;
        }
        return parseDate(s).getTime() >= parseDate(e).getTime();
    }

    /**
     * 格式化日期
     *
     * @return
     */
    public static Date parseDate(String date) {
        return parse(date, Consts.DATE_PATTERN);
    }

    /**
     * 格式化日期
     *
     * @return
     */
    public static Date parseTime(String date) {
        return parse(date, Consts.DATE_TIME_PATTERN);
    }

    /**
     * 格式化日期
     *
     * @return
     */
    public static Date parse(String date, String pattern) {
        try {
            return DateUtils.parseDate(date, pattern);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 格式化日期
     *
     * @return
     */
    public static String format(Date date, String pattern) {
        return DateFormatUtils.format(date, pattern);
    }

    /**
     * 把日期转换为Timestamp
     *
     * @param date
     * @return
     */
    public static Timestamp format(Date date) {
        return new Timestamp(date.getTime());
    }

    /**
     * 校验日期是否合法
     *
     * @return
     */
    public static boolean isValidDate(String s) {
        return parse(s, Consts.DATE_TIME_PATTERN) != null;
    }

    /**
     * 校验日期是否合法
     *
     * @return
     */
    public static boolean isValidDate(String s, String pattern) {
        return parse(s, pattern) != null;
    }

    public static int getDiffYear(String startTime, String endTime) {
        DateFormat fmt = new SimpleDateFormat(Consts.DATE_PATTERN);
        try {
            int years = (int) (((fmt.parse(endTime).getTime() - fmt.parse(startTime).getTime()) / ONE_DAY_TIME) / 365);
            return years;
        } catch (Exception e) {
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            return 0;
        }
    }

    /**
     * <li>功能描述：时间相减得到天数
     *
     * @param beginDateStr
     * @param endDateStr
     * @return long
     * @author Administrator
     */
    public static long getDaySub(String beginDateStr, String endDateStr) {
        SimpleDateFormat format = new SimpleDateFormat(Consts.DATE_PATTERN);
        Date beginDate = null;
        Date endDate = null;

        try {
            beginDate = format.parse(beginDateStr);
            endDate = format.parse(endDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return getDaySub(beginDate, endDate);
    }

    /**
     * <li>功能描述：时间相减得到天数
     *
     * @param beginDateStr
     * @param endDateStr
     * @return long
     * @author Administrator
     */
    public static long getDaySub(Date beginDate, Date endDate) {
        return (endDate.getTime() - beginDate.getTime()) / ONE_DAY_TIME;
    }

    public static Date[] getQueryDate(Date date) {
        Date[] result = new Date[2];
        long time = date.getTime() / ONE_DAY_TIME * ONE_DAY_TIME - 8 * 3600 * 1000L;
        result[0] = new Date(time);
        result[1] = new Date(time + ONE_DAY_TIME);
        return result;
    }

    /**
     * 得到n天之后的日期
     *
     * @param days
     * @return
     */
    public static String getAfterDayDate(String days) {
        int daysInt = Integer.parseInt(days);

        Calendar canlendar = Calendar.getInstance(); // java.util包
        canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
        Date date = canlendar.getTime();

        SimpleDateFormat sdfd = new SimpleDateFormat(Consts.DATE_TIME_PATTERN);
        String dateStr = sdfd.format(date);

        return dateStr;
    }

    /**
     * 得到n天之后是周几
     *
     * @param days
     * @return
     */
    public static String getAfterDayWeek(String days) {
        int daysInt = Integer.parseInt(days);

        Calendar canlendar = Calendar.getInstance(); // java.util包
        canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
        Date date = canlendar.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("E");
        String dateStr = sdf.format(date);

        return dateStr;
    }

    public static java.sql.Date[] getCurrMonthDay(int month) {
        java.sql.Date[] result = new java.sql.Date[2];
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + month);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        result[0] = new java.sql.Date(calendar.getTimeInMillis() / ONE_DAY_TIME * ONE_DAY_TIME - 8 * 3600 * 1000L);
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
        result[1] = new java.sql.Date(calendar.getTimeInMillis() / ONE_DAY_TIME * ONE_DAY_TIME - 8 * 3600 * 1000L);
        return result;
    }
    
    public static Date getFirstMonthDate(Date date) {
    	date = date == null ? new Date() : date;
    	 Calendar calendar = Calendar.getInstance();
    	 calendar.setTime(date);
    	 calendar.set(Calendar.DAY_OF_MONTH, 1);
    	 calendar.set(Calendar.HOUR_OF_DAY, 0);
    	 calendar.set(Calendar.MINUTE, 0);
    	 calendar.set(Calendar.SECOND, 0);
    	 calendar.set(Calendar.MILLISECOND, 0);
    	 return calendar.getTime();
    }
    
    /**
     * 获取最近n个月的起止时间
     * @param n
     * @return
     */
    public static java.sql.Date[] getRecentlyMonthDay(int n) {
        java.sql.Date[] result = new java.sql.Date[2];
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        result[1] = new java.sql.Date(calendar.getTimeInMillis());
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1 - n);
        result[0] = new java.sql.Date(calendar.getTimeInMillis());
        return result;
    }

    
    /**
     * 获取最近n个月的起止时间
     * @param n
     * @return
     */
    public static List<String> getRecentlyMonthDayStr(int n, String pattern) {
        pattern = StringUtil.isNullEmpty(pattern) ? "yyyy-MM" : pattern;
        java.sql.Date[] result = getRecentlyMonthDay(n);
        return Arrays.asList(format(result[0], "yyyy-MM"), format(result[1], "yyyy-MM"));
    }

	public static Date getOneDayBeginTime(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	public static Date getOneDayEndTime(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	public static Date getDateAdd(Date date, int amount) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, amount);
		return calendar.getTime();
	}
    
    /**
     * 格式化Oracle Date
     * 
     * @param value
     * @return
     */
    // public static String buildDateValue(Object value){
    // if(Func.isOracle()){
    // return "to_date('"+ value +"','yyyy-mm-dd HH24:MI:SS')";
    // }else{
    // return Func.toStr(value);
    // }
    // }

}
