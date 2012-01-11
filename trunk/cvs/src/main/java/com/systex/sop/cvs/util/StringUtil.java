package com.systex.sop.cvs.util;

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
	
	public static boolean isEmpty(String str) {
		boolean isEmpty = false;

		if (str == null) {
			isEmpty = true;
		} else {
			if (str.trim().length() < 1) {
				isEmpty = true;
			}
		}

		return isEmpty;
	}
	
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}
	
}
