package com.systex.sop.cvs.ui.tableClass;

import java.sql.Timestamp;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.systex.sop.cvs.util.StringUtil;

public class VerTreeDO extends CVSBaseDO implements Comparable<VerTreeDO>{
	private String version;
	private Timestamp verdate;
	private String author;
	private String tagname;
	
	private static final String [] columnName;
	
	static {
		columnName = new String [] {
				"版號", "提交日期", "作者", "標籤名稱"
		};
	}
	
	public VerTreeDO() {}
	
	public VerTreeDO(String tagName) {
		this.tagname = tagName;
	}
	
	public String getTagname() {
		return tagname;
	}

	public void setTagname(String tagname) {
		this.tagname = tagname;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Timestamp getVerdate() {
		return verdate;
	}

	public void setVerdate(Timestamp verdate) {
		this.verdate = verdate;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	@Override
	public String[] getColumnFormat() {
		return new String[] { null, "yyyy-MM-dd HH:mm:ss", null, null};
	}

	@Override
	public String[] getColumnName() {
		return columnName;
	}

	@Override
	public int[] getColumnWidth() {
//		"版號", "提交日期", "作者", "標籤名稱"
		return new int[] { 65, 155, 65, 220 };
	}

	@Override
	public int compareTo(VerTreeDO o) {
		return StringUtil.concat(getVerdate(), getTagname()).compareTo(StringUtil.concat(o.getVerdate(), o.getTagname()));
	}

}
