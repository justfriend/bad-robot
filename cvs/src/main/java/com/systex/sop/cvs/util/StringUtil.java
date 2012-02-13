package com.systex.sop.cvs.util;

import java.sql.Timestamp;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

/**
 * String Utility
 * <p>
 * <p>
 * Modify history : <br>
 * ================================ <br>
 * 2012/01/02 .[- _"]. release
 * <p>
 * 
 */
public class StringUtil {
	
	public static Long convertLong(String num) {
		if (isEmpty(num)) return null;
		
		try {
			return Long.parseLong(num);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static boolean allEmpty(Object ...objs) {
		if (objs == null) return true;
		
		for (Object obj : objs) {
			if (obj != null) {
				if (!isEmpty(obj)) return false;
			}
		}
		
		return true;
	}
	
	public static boolean anyEmpty(Object ...objs) {
		if (objs == null) return true;
		
		for (Object obj : objs) {
			if (obj == null) {
				return true;
			}else{
				if (isEmpty(obj))
					return true;
			}
		}
		
		return false;
	}

	public static boolean anyEqual(Object src, Object ...compares) {
		boolean isMatch = false;
		for (Object compare : compares) {
			if (compare.equals(src)) {
				isMatch = true;
				break;
			}
		}
		
		return isMatch;
	}
	
	public static String concat(Object... strArray) {
		StringBuffer strbuf = concatBuf(strArray);
		
		return (strbuf == null)? null: strbuf.toString();
	}
	
	public static StringBuffer concatBuf(Object... strArray) {
		if (strArray == null)
			return null;

		StringBuffer str = new StringBuffer();

		for (int i = 0; i < strArray.length; i++) {
			if (strArray[i] == null)
				continue;

			str.append(strArray[i].toString());
		}

		return str;
	}
	
	public static boolean isEmpty(Object str) {
		boolean isEmpty = false;

		if (str == null) {
			isEmpty = true;
		} else {
			if (str.toString().trim().length() < 1) {
				isEmpty = true;
			}
		}

		return isEmpty;
	}
	
	public static boolean isNotEmpty(Object str) {
		return !isEmpty(str);
	}
	
	public static String genDateBetweenSQL(String column, Timestamp beginDate, Timestamp endedDate) {
		if (beginDate != null && endedDate != null) {
			return StringUtil.concat(
					" AND ", column, " BETWEEN",
					" to_date('", TimestampHelper.convertToyyyyMMdd(beginDate), "', 'yyyymmdd') AND",
					" to_date('", TimestampHelper.convertToyyyyMMdd(TimestampHelper.addTime(endedDate, 0, 0, 1)), "', 'yyyymmdd')"
			);
		}else
		if (beginDate != null) {
			return StringUtil.concat(
					" AND ", column, " >=",
					" to_date('", TimestampHelper.convertToyyyyMMdd(beginDate), "', 'yyyymmdd')"
			);
		}else
		if (endedDate != null){
			return StringUtil.concat(
					" AND ", column, " <",
					" to_date('", TimestampHelper.convertToyyyyMMdd(TimestampHelper.addTime(endedDate, 0, 0, 1)), "', 'yyyymmdd')"
			);
		}else{
			return "";
		}
	}
	
}
