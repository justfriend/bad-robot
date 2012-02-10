package com.systex.sop.cvs.ui.tableClass;

import java.sql.Timestamp;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class NewVerNoTagDO extends CVSBaseDO {
	private Long MSid;
	private String filename;
	private String versionhead;
	private String module;
	private String programid;
	private String author;
	private Timestamp verdate;
	private Timestamp lastupdate;
	private char state;
	private String descId;
	private String descDesc;
	private String descStep;
	private String fulldesc;
	private String rcsfile;
	
	private static final String [] columnName;
	
	static {
		columnName = new String [] {
				"RCSID",
				"檔案名稱", "最新版號", "模組名稱", "程式名稱", "作者", "提交時間", "同步時間", "狀態",
				"提交ID", "提交說明", "提交步驟", "完整提交內容", "完整檔案名稱"
		};
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getVersionhead() {
		return versionhead;
	}

	public void setVersionhead(String versionhead) {
		this.versionhead = versionhead;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getProgramid() {
		return programid;
	}

	public void setProgramid(String programid) {
		this.programid = programid;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Timestamp getVerdate() {
		return verdate;
	}

	public void setVerdate(Timestamp verdate) {
		this.verdate = verdate;
	}

	public Timestamp getLastupdate() {
		return lastupdate;
	}

	public void setLastupdate(Timestamp lastupdate) {
		this.lastupdate = lastupdate;
	}

	public Long getMSid() {
		return MSid;
	}

	public void setMSid(Long mSid) {
		MSid = mSid;
	}

	public String getDescId() {
		return descId;
	}

	public void setDescId(String descId) {
		this.descId = descId;
	}

	public String getDescDesc() {
		return descDesc;
	}

	public void setDescDesc(String descDesc) {
		this.descDesc = descDesc;
	}

	public String getDescStep() {
		return descStep;
	}

	public void setDescStep(String descStep) {
		this.descStep = descStep;
	}

	public String getFulldesc() {
		return fulldesc;
	}

	public void setFulldesc(String fulldesc) {
		this.fulldesc = fulldesc;
	}

	public String getRcsfile() {
		return rcsfile;
	}

	public void setRcsfile(String rcsfile) {
		this.rcsfile = rcsfile;
	}

	public char getState() {
		return state;
	}

	public void setState(char state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	@Override
	public String[] getColumnFormat() {
		return new String [] {
				null,
				null, null, null, null, null, "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss", null,
				null, null, null, null, null
		};
	}

	@Override
	public String[] getColumnName() {
		return columnName;
	}

	@Override
	public int[] getColumnWidth() {
//		"RCSID",
//		"檔案名稱", "最新版號", "模組名稱", "程式名稱", "作者", "提交時間", "同步時間", "狀態",
//		"提交ID", "提交說明", "提交步驟", "完整提交內容", "完整檔案名稱"
		return new int[] {
				60,
				240, 70, 70, 120, 60, 130, 130, 40,
				60, 300, 60, 200, 800
		};
	}

}
