package com.systex.sop.cvs.util;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Timestamp HELPER
 * <p>
 * <p>
 * Modify history : <br>
 * ================================ <br>
 * 2012/01/02 .[- _"]. release
 * <p>
 * 
 */
public class TimestampHelper {
	
	public static Timestamp now() {
		return new Timestamp(System.currentTimeMillis());
	}
	
	public static Timestamp addTime(Timestamp ts, int year, int month, int day) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(ts.getTime());
		cal.add(Calendar.YEAR, year);
		cal.add(Calendar.MONTH, month);
		cal.add(Calendar.DAY_OF_MONTH, day);
		
		return new Timestamp(cal.getTimeInMillis());
	}
	
	/**
	 * 轉換yyyyMMdd格式宇串為時戳<p>
	 * 其中時分秘及毫秒都設為0, 若傳入參數為空則回傳null
	 * @param yyyyMMdd yyyyMMdd格式字串
	 * @return 時戳 (時分秒毫秒都為0)
	 */
	public static Timestamp convertToTimestamp(String yyyyMMdd) {
		if (yyyyMMdd == null || yyyyMMdd.length() != 8) return null;
		if ("00000000".equals(yyyyMMdd)) return null;
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, Integer.parseInt(yyyyMMdd.substring(0, 4)));
		cal.set(Calendar.MONTH, Integer.parseInt(yyyyMMdd.substring(4, 6)) -1 );
		cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(yyyyMMdd.substring(6, 8)));
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.AM_PM, Calendar.AM); // for SOPA必須為AM
		
		return new Timestamp(cal.getTimeInMillis());
	}
	
	/**
	 * 轉換yyyy/MM/dd格式宇串為時戳<p>
	 * 其中時分秘及毫秒都設為0, 若傳入參數為空則回傳null
	 * @param yyyyMMdd yyyy/MM/dd格式字串
	 * @return 時戳 (時分秒毫秒都為0)
	 */
	public static Timestamp convertToTimestamp2(String yyyyMMdd) {
		return convertToTimestamp3(yyyyMMdd);
	}
	
	/**
	 * 轉換yyyy-MM-dd格式宇串為時戳<p>
	 * 其中時分秘及毫秒都設為0, 若傳入參數為空則回傳null
	 * @param yyyyMMdd yyyy-MM-dd格式字串
	 * @return 時戳 (時分秒毫秒都為0)
	 */
	public static Timestamp convertToTimestamp3(String yyyyMMdd) {
		if (yyyyMMdd == null || yyyyMMdd.length() != 10) return null;
		if ("00000000".equals(yyyyMMdd.replaceAll("-", ""))) return null;
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, Integer.parseInt(yyyyMMdd.substring(0, 4)));
		cal.set(Calendar.MONTH, Integer.parseInt(yyyyMMdd.substring(5, 7)) -1 );
		cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(yyyyMMdd.substring(8, 10)));
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.AM_PM, Calendar.AM); // for SOPA必須為AM
		
		return new Timestamp(cal.getTimeInMillis());
	}
	
	/**
	 * 轉換時戳為yyyyMMdd格式字串<p>
	 * 不列入時分秒及毫秒之數據, 若傳入參數為空則回傳null
	 * @param ts 時戳
	 * @return yyyyMMdd格式字串
	 */
	public static String convertToyyyyMMdd(Timestamp ts) {
		if (ts == null) return null;
		
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(ts.getTime());
		StringBuffer dateTime = new StringBuffer();
		dateTime.append(String.format("%04d", cal.get(Calendar.YEAR)))
		        .append(String.format("%02d", cal.get(Calendar.MONTH) + 1))
		        .append(String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)));
		
		return dateTime.toString();
	}
	
	/**
	 * 轉換時戳為yyyy/MM/dd格式字串<p>
	 * 不列入時分秒及毫秒之數據, 若傳入參數為空則回傳null
	 * @param ts 時戳
	 * @return yyyy/MM/dd格式字串
	 */
	public static String convertToyyyyMMdd2(Timestamp ts) {
		if (ts == null) return null;
		
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(ts.getTime());
		StringBuffer dateTime = new StringBuffer();
		dateTime.append(String.format("%04d", cal.get(Calendar.YEAR))).append("/")
		        .append(String.format("%02d", cal.get(Calendar.MONTH) + 1)).append("/")
		        .append(String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)));
		
		return dateTime.toString();
	}
	
	/**
	 * 轉換時戳為yyyy-MM-dd格式字串<p>
	 * 不列入時分秒及毫秒之數據, 若傳入參數為空則回傳null
	 * @param ts 時戳
	 * @return yyyy-MM-dd格式字串
	 */
	public static String convertToyyyyMMdd3(Timestamp ts) {
		if (ts == null) return null;
		
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(ts.getTime());
		StringBuffer dateTime = new StringBuffer();
		dateTime.append(String.format("%04d", cal.get(Calendar.YEAR))).append("-")
		        .append(String.format("%02d", cal.get(Calendar.MONTH) + 1)).append("-")
		        .append(String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)));
		
		return dateTime.toString();
	}
	
	public static Timestamp cvsdate2Ts(String cvsdate) {
		if (StringUtil.isEmpty(cvsdate)) return null;
		
		Pattern ptn = Pattern.compile("^([0-9]{4})/([0-9]{1,2})/([0-9]{1,2}) ([0-9]{1,2}):([0-9]{1,2}):([0-9]{1,2})$");
		Matcher matcher = ptn.matcher(cvsdate);
		if (matcher.find()) {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, Integer.parseInt(matcher.group(1)));
			cal.set(Calendar.MONTH, Integer.parseInt(matcher.group(2)) -1 );
			cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(matcher.group(3)));
			cal.set(Calendar.AM_PM, Calendar.AM);
			cal.set(Calendar.HOUR, Integer.parseInt(matcher.group(4)));
			cal.set(Calendar.MINUTE, Integer.parseInt(matcher.group(5)));
			cal.set(Calendar.SECOND, Integer.parseInt(matcher.group(6)));
			cal.set(Calendar.MILLISECOND, 0);
			
			return new Timestamp(cal.getTimeInMillis());
		}
		
		return null;
	}
	
}
