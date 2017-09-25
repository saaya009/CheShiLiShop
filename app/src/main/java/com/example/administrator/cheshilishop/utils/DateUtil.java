package com.example.administrator.cheshilishop.utils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.TimeZone;

/**
 * @描述 时间工具类
 * 
 * @author txiuqi
 * 
 */
public class DateUtil {
	public static final SimpleDateFormat simpleFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm");

	public static final SimpleDateFormat yearFormat = new SimpleDateFormat(
			"yyyy-MM-dd");

	public static final SimpleDateFormat sequenceFormat = new SimpleDateFormat(
			"yyyyMMddHHmmss");

	public static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public static final TimeZone tz = TimeZone.getTimeZone("GMT+8:00");

	private static final long ONEDAY = 86400000;

	public static final int SHOW_TYPE_SIMPLE = 0;

	public static final int SHOW_TYPE_COMPLEX = 1;

	public static final int SHOW_TYPE_ALL = 2;

	public static final int SHOW_TYPE_CALL_LOG = 3;

	public static final int SHOW_TYPE_CALL_DETAIL = 4;

	static {
		simpleFormat.setTimeZone(tz);
		sequenceFormat.setTimeZone(tz);
	}

	/**
	 * 获取现在时间
	 * 
	 * @return 返回时间类型 yyyy-MM-dd HH:mm:ss
	 */
	public static Date getNowDate() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		ParsePosition pos = new ParsePosition(8);
		Date currentTime_2 = formatter.parse(dateString, pos);
		return currentTime_2;
	}

	/**
	 * 获取现在时间
	 * 
	 * @return返回短时间格式 yyyy-MM-dd
	 */
	public static Date getNowDateShort() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(currentTime);
		ParsePosition pos = new ParsePosition(8);
		Date currentTime_2 = formatter.parse(dateString, pos);
		return currentTime_2;
	}

	/**
	 * 得到现在时间
	 * 
	 * @return
	 */
	public static Date getNow() {
		Date currentTime = new Date();
		return currentTime;
	}

	/**
	 * 获取现在时间
	 * 
	 * @return返回字符串格式 yyyy-MM-dd HH:mm:ss
	 */
	public static String getStringDate() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * 获取现在时间
	 * 
	 * @return 返回短时间字符串格式yyyy-MM-dd
	 */
	public static String getStringDateShort() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * 获取当前时间 小时:分:秒 HH:mm:ss
	 * 
	 * @return
	 */
	public static String getTimeShort() {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		Date currentTime = new Date();
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * 得到现在小时
	 */
	public static String getHour() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		String hour;
		hour = dateString.substring(11, 13);
		return hour;
	}

	/**
	 * 得到现在分钟
	 * 
	 * @return
	 */
	public static String getTime() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		String min;
		min = dateString.substring(14, 16);
		return min;
	}

	/**
	 * 将long类型转换成short时间
	 * 
	 * @return 返回短时间字符串格式yyyy-MM-dd
	 */
	public static String getStringDateShort(long date) {
		Date currentTime = new Date(date);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(currentTime);
		return dateString;
	}
	/**
	 * 将long类型转换成short时间
	 *
	 * @return 返回短时间字符串格式yyyy.MM.dd
	 */
	public static String getStringDateShort2(long date) {
		Date currentTime = new Date(date);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
		String dateString = formatter.format(currentTime);
		return dateString;
	}
	/**
	 * 将long类型转换成short时间
	 * 
	 * @return 返回短时间字符串格式yyyy-MM-dd
	 */
	public static String getStringDateLong(long date) {
		Date currentTime = new Date(date);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}
	/**
	 * 将long类型转换成short时间
	 * 
	 * @return 返回短时间字符串格式yyyy-MM-dd
	 */
	public static String getStringDateShortCN(long date) {
		Date currentTime = new Date(date);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/*
    * 将时间戳转换为时间
    */
	public static String stampToDate(String time){
		Long Time = new Long(time);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = sdf.format(new Date(Time*1000));
		return date;
	}

	/*
    * 将时间戳转换为时间
    */
	public static String stampToDate3(String time){
		Long Time = new Long(time);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd ");
		String date = sdf.format(new Date(Time*1000))+getDuringDay(new Date(Time*1000).getHours());
		return date;
	}


	/*
    * 将时间戳转换为时间
    */
	public static String stampToDate2(String time){
		Long Time = new Long(time);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String date = sdf.format(new Date(Time*1000));
		return date;
	}

	/**
	 * 根据小时判断是否为上午、中午、下午
	 */
	public static String getDuringDay(int hour){
		if (hour >= 0 && hour < 11) {
			return "上午";
		}if (hour >= 11 && hour <= 13) {
			return "中午";
		}if (hour >= 14 && hour <= 24) {
			return "下午";
		}
		return null;
	}


	/**
	 * 将长时间格式字符串 yyyy-MM-dd HH:mm:ss转换为时间Date
	 * 
	 * @param strDate
	 * @return
	 */
	public static Date strToDateLong(String strDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(strDate, pos);
		return strtodate;
	}

	/**
	 * 将长时间格式Date时间转换为字符串 yyyy-MM-dd HH:mm:ss
	 * 
	 * @param dateDate
	 * @return
	 */
	public static String dateToStrLong(Date dateDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String dateString = formatter.format(dateDate);
		return dateString;
	}

	/**
	 * 将短时间格式时间转换为字符串 yyyy-MM-dd
	 */
	public static String dateToStr(Date dateDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(dateDate);
		return dateString;
	}

	/**
	 * 将短时间格式字符串转换为时间 yyyy-MM-dd
	 * 
	 * @param strDate
	 * @return
	 */
	public static Date strToDate(String strDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(strDate, pos);
		return strtodate;
	}
	/**
	 * 将短时间格式字符串 yyyy-MM-dd转换为long
	 * 
	 * @param strDate
	 * @return
	 */
	public static Long shortStrToLong(String strDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(strDate, pos);
		
		return strtodate.getTime();
	}
	/**
	 * 将短时间格式字符串 yyyy-MM-dd转换为long
	 * 
	 * @param strDate
	 * @return
	 */
	public static Long shortStrToLong2(String strDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(strDate, pos);
		
		return strtodate.getTime();
	}

	/**
	 * 根据用户传入的时间表示格式，返回当前时间的格式 如果是yyyyMMdd，注意字母y不能大写。
	 * 
	 * @param sformat
	 *            yyyyMMddhhmmss
	 * @return
	 */
	public static String getUserDate(String sformat) {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(sformat);
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * 将yyyy-MM-dd HH:mm:ss 转换成yyyy-MM-dd
	 * 
	 * @param longDate
	 * @return
	 */
	public static String getShortDate(String longDate) {
		return dateToStr(strToDateLong(longDate));
	}

	/**
	 * 输入年月日返回 年-月-日
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @return 年-月-日
	 */

	public static String formatSpecifyDate(int year, int month, int day) {
		return year + "-" + month + "-" + day;
	}

	/**
	 * 将时间转换成 MM：SS 如01：01
	 * 
	 * @param time
	 * @return
	 */
	public static String getTime(long time) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(time);
		int minute = c.get(Calendar.MINUTE);
		int second = c.get(Calendar.SECOND);
		String times = "";
		if (minute < 10) {
			times = "0" + minute;
		} else {
			times = "" + minute;
		}
		if (second < 10) {
			times += ":0" + second;
		} else {
			times += ":" + second;
		}
		return times;
	}

	/**
	 * 将时间long获取小时 分钟 01：01
	 * 
	 * @param time
	 * @return
	 */
	public static String getHourMinute(long time) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(time);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		String times = "" + hour;
		if (hour < 10) {
			times += "0" + hour;
		} else {
			times += "" + hour;
		}
		if (minute < 10) {
			times += ":0" + minute;
		} else {
			times += ":" + minute;
		}
		return times;
	}

	/**
	 * 判断时间是否当年
	 * 
	 * @param time
	 * @return
	 */
	public static boolean checkCurrentYear(long time) {
		Calendar c = Calendar.getInstance();
		c = Calendar.getInstance(tz);
		c.setTimeInMillis(time);
		long currentTime = System.currentTimeMillis();
		Calendar current_c = Calendar.getInstance();
		current_c = Calendar.getInstance(tz);
		current_c.setTimeInMillis(currentTime);

		int currentYear = current_c.get(Calendar.YEAR);
		int y = c.get(Calendar.YEAR);

		return y == currentYear;
	}

	public static String getDateString(long time, int type) {
		// long time = Long.parseLong(temptime);
		Calendar c = Calendar.getInstance();
		c = Calendar.getInstance(tz);
		c.setTimeInMillis(time);
		long currentTime = System.currentTimeMillis();
		Calendar current_c = Calendar.getInstance();
		current_c = Calendar.getInstance(tz);
		current_c.setTimeInMillis(currentTime);

		int currentYear = current_c.get(Calendar.YEAR);
		int y = c.get(Calendar.YEAR);
		int m = c.get(Calendar.MONTH) + 1;
		int d = c.get(Calendar.DAY_OF_MONTH);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		int second = c.get(Calendar.SECOND);

		long t = System.currentTimeMillis() - time;
		long t2 = System.currentTimeMillis() % ONEDAY;
		String dateStr = "";
		if (t < t2 && t > 0) {
			if (type == SHOW_TYPE_SIMPLE) {
				dateStr = (hour < 10 ? "0" + hour : hour) + ":"
						+ (minute < 10 ? "0" + minute : minute);
			} else if (type == SHOW_TYPE_COMPLEX) {
				dateStr = "今天  " + (hour < 10 ? "0" + hour : hour) + ":"
						+ (minute < 10 ? "0" + minute : minute);
			} else if (type == SHOW_TYPE_CALL_LOG) {
				dateStr = "今天  " + (hour < 10 ? "0" + hour : hour) + ":"
						+ (minute < 10 ? "0" + minute : minute);
			} else if (type == SHOW_TYPE_CALL_DETAIL) {
				dateStr = "今天  ";
			} else {
				dateStr = (hour < 10 ? "0" + hour : hour) + ":"
						+ (minute < 10 ? "0" + minute : minute) + ":"
						+ (second < 10 ? "0" + second : second);
			}
		} else if (t < (t2 + ONEDAY) && t > 0) {
			if (type == SHOW_TYPE_SIMPLE || type == SHOW_TYPE_CALL_DETAIL) {
				dateStr = "昨天  ";
			} else if (type == SHOW_TYPE_COMPLEX) {
				dateStr = "昨天  " + (hour < 10 ? "0" + hour : hour) + ":"
						+ (minute < 10 ? "0" + minute : minute);
			} else if (type == SHOW_TYPE_CALL_LOG) {
				dateStr = "昨天  " + (hour < 10 ? "0" + hour : hour) + ":"
						+ (minute < 10 ? "0" + minute : minute);
			} else {
				dateStr = "昨天  " + (hour < 10 ? "0" + hour : hour) + ":"
						+ (minute < 10 ? "0" + minute : minute) + ":"
						+ (second < 10 ? "0" + second : second);
			}
		} else if (y == currentYear) {
			if (type == SHOW_TYPE_SIMPLE) {
				dateStr = (m < 10 ? "0" + m : m) + "/" + (d < 10 ? "0" + d : d);
			} else if (type == SHOW_TYPE_COMPLEX) {
				dateStr = (m < 10 ? "0" + m : m) + "月" + (d < 10 ? "0" + d : d)
						+ "日";
			} else if (type == SHOW_TYPE_CALL_LOG || type == SHOW_TYPE_COMPLEX) {
				dateStr = (m < 10 ? "0" + m : m) + /* 月 */"/"
						+ (d < 10 ? "0" + d : d) + /* 日 */" "
						+ (hour < 10 ? "0" + hour : hour) + ":"
						+ (minute < 10 ? "0" + minute : minute);
			} else if (type == SHOW_TYPE_CALL_DETAIL) {
				dateStr = y + "/" + (m < 10 ? "0" + m : m) + "/"
						+ (d < 10 ? "0" + d : d);
			} else {
				dateStr = (m < 10 ? "0" + m : m) + "月" + (d < 10 ? "0" + d : d)
						+ "日 " + (hour < 10 ? "0" + hour : hour) + ":"
						+ (minute < 10 ? "0" + minute : minute) + ":"
						+ (second < 10 ? "0" + second : second);
			}
		} else {
			if (type == SHOW_TYPE_SIMPLE) {
				dateStr = y + "/" + (m < 10 ? "0" + m : m) + "/"
						+ (d < 10 ? "0" + d : d);
			} else if (type == SHOW_TYPE_COMPLEX) {
				dateStr = y + "年" + (m < 10 ? "0" + m : m) + "月"
						+ (d < 10 ? "0" + d : d) + "日";
			} else if (type == SHOW_TYPE_CALL_LOG || type == SHOW_TYPE_COMPLEX) {
				dateStr = y + /* 年 */"/" + (m < 10 ? "0" + m : m) + /* 月 */"/"
						+ (d < 10 ? "0" + d : d) + /* 日 */"  "/*
																 * + (hour < 10
																 * ? "0" + hour
																 * : hour) + ":"
																 * + (minute <
																 * 10 ? "0" +
																 * minute :
																 * minute)
																 */;
			} else if (type == SHOW_TYPE_CALL_DETAIL) {
				dateStr = y + "/" + (m < 10 ? "0" + m : m) + "/"
						+ (d < 10 ? "0" + d : d);
			} else {
				dateStr = y + "年" + (m < 10 ? "0" + m : m) + "月"
						+ (d < 10 ? "0" + d : d) + "日 "
						+ (hour < 10 ? "0" + hour : hour) + ":"
						+ (minute < 10 ? "0" + minute : minute) + ":"
						+ (second < 10 ? "0" + second : second);
			}
		}
		return dateStr;
	}

	/**
	 * 时间转换成2012 08 21 15 03 88格式
	 * 
	 * @param date
	 * @return
	 */
	public static String TransfTime(long date) {
		Calendar c = Calendar.getInstance();
		c = Calendar.getInstance(tz);
		//System.currentTimeMillis() - date
		c.setTimeInMillis(date);
		StringBuilder builder = new StringBuilder();

		int y = c.get(Calendar.YEAR);
		builder.append(y);
		int m = c.get(Calendar.MONTH) + 1;
		if (m < 10) {
			builder.append("0").append(m);
		} else {
			builder.append(m);
		}
		int d = c.get(Calendar.DAY_OF_MONTH);
		if (d < 10) {
			builder.append("0").append(d);
		} else {
			builder.append(d);
		}
		int hour = c.get(Calendar.HOUR_OF_DAY);
		if (hour < 10) {
			builder.append("0").append(hour);
		} else {
			builder.append(hour);
		}
		int minute = c.get(Calendar.MINUTE);
		if (minute < 10) {
			builder.append("0").append(minute);
		} else {
			builder.append(minute);
		}
		int second = c.get(Calendar.SECOND);
		if (second < 10) {
			builder.append("0").append(second);
		} else {
			builder.append(second);
		}
		Log.d("[DateUtils ] current time " + builder.toString());
		return builder.toString();
	}

	/**
	 * 列表时间
	 * 
	 * @param duration
	 *            // 2012 05 17 18 49 03
	 * @return
	 */
	public static String getSpeakDuration(String duration) {
		// 2012 05 17 18 49 03
		String time = String.valueOf(duration);
		String year = time.substring(0, 4);
		String month = time.substring(4, 6);
		String day = time.substring(6, 8);
		String hour = time.substring(8, 10);
		String minute = time.substring(10, 12);

		long currentTime = System.currentTimeMillis();
		Calendar current_c = Calendar.getInstance();
		current_c = Calendar.getInstance(tz);
		current_c.setTimeInMillis(currentTime);

		int currenty = current_c.get(Calendar.YEAR);
		int currentm = current_c.get(Calendar.MONTH) + 1;
		int currentd = current_c.get(Calendar.DAY_OF_MONTH);
		int currenthour = current_c.get(Calendar.HOUR_OF_DAY);
		int currentminute = current_c.get(Calendar.MINUTE);
		int currentsecond = current_c.get(Calendar.SECOND);

		StringBuffer sBuffer = new StringBuffer("");
		sBuffer.append(currenty);
		sBuffer.append(currentm < 10 ? "0" + currentm : currentm);
		sBuffer.append(currentd < 10 ? "0" + currentd : currentd);
		sBuffer.append(currenthour < 10 ? "0" + currenthour : currenthour);
		sBuffer.append(currentminute < 10 ? "0" + currentminute : currentminute);
		sBuffer.append(currentsecond < 10 ? "0" + currentsecond : currentsecond);

		long currentdate = Long.parseLong(sBuffer.toString());
		long date = Long.parseLong(duration);

		String dateStr = "";
		long gapd = 1000000;

		if ((currenty - Integer.parseInt(year)) > 0) {
			dateStr = year + "-" + month + "-" + day + " " + hour + ":"
					+ minute;
		} else if ((currenty - Integer.parseInt(year)) < 0
				|| (currentdate - date) < 0 /* ||(currentdate - date) == 0 */) {
			dateStr = "刚刚";
		} else {
			if (currentm - Integer.parseInt(month) < 0) {
				dateStr = "刚刚";
			} else {
				if (currentm - Integer.parseInt(month) > 0) {
					date += (100 - days(Integer.parseInt(year),
							Integer.parseInt(month)))
							* gapd;
				}
				if ((currentdate / gapd - date / gapd) >= 3) {
					dateStr = month + "-" + day + " " + hour + ":" + minute;
				} else if ((currentdate / gapd - date / gapd) >= 2) {
					dateStr = "前天  " + hour + ":" + minute;
				} else if ((currentdate / gapd - date / gapd) >= 1) {
					dateStr = "昨天  " + hour + ":" + minute;
				} else if ((currentdate / gapd - date / gapd) < 1
						&& (currentdate - date) >= 0) {
					if ((currenthour - Integer.valueOf(hour)) >= 1) {
						dateStr = hour + ":" + minute;
					} else if ((currentminute - Integer.valueOf(minute)) == 0) {
						dateStr = "刚刚";
					} else {
						dateStr = (currentminute - Integer.valueOf(minute))
								+ "分钟前";
					}
				}
			}
		}
		return dateStr;
	}

	public static int days(int year, int month) {
		int days = 0;
		if (month != 2) {
			switch (month) {
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
				days = 31;
				break;
			case 4:
			case 6:
			case 9:
			case 11:
				days = 30;
			}
		} else {
			if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0)
				days = 29;
			else
				days = 28;

		}
		return days;
	}

	/**
	 * 获取当前当天日期的毫秒数 2012-03-21的毫秒数
	 * 
	 * @return
	 */
	public static long getCurrentDayTime() {
		Date d = new Date(System.currentTimeMillis());
		String formatDate = yearFormat.format(d);
		try {
			return (yearFormat.parse(formatDate)).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 由2012-3-21获得20120321样式的日期字符串
	 * 
	 * @param s
	 *            要格式化的日期字符串，如不符合规则，将不做处理，直接返回原值
	 * @return
	 */
	public static String getFormatedDateString(String s) {
		if (s == null || s.trim().length() == 0) {
			return s;
		}
		String[] split = s.trim().split("-");
		if (split.length != 3) {
			return s;
		}
		StringBuilder sb = new StringBuilder();
		if (split[0].length() == 4) {
			sb.append(split[0]);
		} else {
			return s;
		}
		if (split[1].length() == 1) {
			sb.append("0").append(split[1]);
		} else if (split[1].length() == 2) {
			sb.append(split[1]);
		} else {
			return s;
		}

		if (split[2].length() == 1) {
			sb.append("0").append(split[2]);
		} else if (split[2].length() == 2) {
			sb.append(split[2]);
		} else {
			return s;
		}

		return sb.toString();
	}

	/**
	 * 由20120321获得2012-3-21样式的日期字符串
	 * 
	 * @param s
	 *            要格式化的日期字符串，如不符合规则，将不做处理，直接返回原值
	 * @return
	 */
	public static String formatSpecifyDate(String s) {
		if (s == null || s.trim().length() != 8 || s.contains("-")) {
			return s;
		}

		String s1 = s.substring(0, 4);
		String s2 = s.substring(4, 6);
		String s3 = s.substring(6, 8);

		return new StringBuilder().append(s1).append("-").append(s2)
				.append("-").append(s3).toString();
	}
	/**  
    * 获取指定日后 后 dayAddNum 天的 日期  
    * @param day  日期，格式为String："2013-9-3";  
    * @param dayAddNum 增加天数 格式为int;  
    * @return  
    */  
   public static String getDateStr(String day, int dayAddNum) {
       SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
       Date nowDate = null;
       try {  
           nowDate = df.parse(day);  
       } catch (ParseException e) {
           e.printStackTrace();  
       }  
       Date newDate2 = new Date(nowDate.getTime() + dayAddNum * 24 * 60 * 60 * 1000);
       SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
       String dateOk = simpleDateFormat.format(newDate2);
       return dateOk;  
   }



}
