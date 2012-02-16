package com.systex.sop.cvs.util;

import java.sql.Timestamp;
import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

public class CriteriaUtil {
	
	public static Criteria between(Criteria cri, String column , Object v1, Object v2) {
		if (StringUtil.allEmpty(v1, v2)) {
			return cri;
		}else{
			if (StringUtil.isNotEmpty(v2)) {
				if (v2 instanceof Timestamp) {
					v2 = TimestampHelper.addTime( (Timestamp) v2, 0, 0, 1);
				}else
				if (v2 instanceof Date) {
					v2 = TimestampHelper.addTime( new Timestamp( ((Date) v2).getTime() ), 0, 0, 1);
				}
			}
			
			if (StringUtil.isEmpty(v2)) {
				return cri.add(Restrictions.ge(column, v1));
			}else
			if (StringUtil.isEmpty(v1)){
				return cri.add(Restrictions.lt(column, v2));
			}else{
				return cri.add(Restrictions.between(column, v1, v2));
			}
		}
	}
	
	public static Criteria eq(Criteria cri, String column, Object value) {
		if (StringUtil.isNotEmpty(value)) {
			return cri.add(Restrictions.eq(column, value));
		}else{
			return cri;
		}
	}
	
	public static Criteria eqIgnoreCase(Criteria cri, String column, Object value) {
		if (StringUtil.isNotEmpty(value)) {
			return cri.add(Restrictions.eq(column, value).ignoreCase());
		}else{
			return cri;
		}
	}
	
	public static Criteria like(Criteria cri, String column, String value) {
		if (StringUtil.isNotEmpty(value)) {
			return cri.add(Restrictions.like(column, StringUtil.concat("%", value, "%")));
		}else{
			return cri;
		}
	}
	
	public static Criteria likeIgnoreCase(Criteria cri, String column, String value) {
		if (StringUtil.isNotEmpty(value)) {
			return cri.add(Restrictions.like(column, StringUtil.concat("%", value, "%")).ignoreCase());
		}else{
			return cri;
		}
	}
}
