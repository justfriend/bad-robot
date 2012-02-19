package com.systex.sop.cvs.ui.tableClass;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class TagDO extends CVSBaseDO implements Comparable<TagDO> {
	private String tagname;
	
	private static final String [] columnName;
	
	static {
		columnName = new String [] {
				"TAGNAME"
		};
	}
	
	public TagDO() {}
	
	public TagDO(String tagName) {
		this.tagname = tagName;
	}
	
	public String getTagname() {
		return tagname;
	}

	public void setTagname(String tagname) {
		this.tagname = tagname;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	@Override
	public String[] getColumnFormat() {
		return new String[] { null };
	}

	@Override
	public String[] getColumnName() {
		return columnName;
	}

	@Override
	public int[] getColumnWidth() {
//		"TAGNAME"
		return new int[] { 120 };
	}

	@Override
	public int compareTo(TagDO o) {
		return getTagname().compareTo(o.getTagname());
	}

}
