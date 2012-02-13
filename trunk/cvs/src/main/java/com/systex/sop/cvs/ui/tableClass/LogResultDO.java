package com.systex.sop.cvs.ui.tableClass;

import java.sql.Timestamp;
import java.util.Calendar;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.systex.sop.cvs.helper.CVSFunc;
import com.systex.sop.cvs.util.StringUtil;

public class LogResultDO extends CVSBaseDO {
	private String module;
	private Integer currentLine;	// log process current line
	private Timestamp beginTime;	// log process begin time
	private Timestamp endedTime;	// log process ended time
	private String elapseTime;		// log process elapse time
	private Integer currentLine2;	// write process current line
	private Integer totalLines2;	// write process total lines
	private Timestamp beginTime2;	// write process begin time
	private Timestamp endedTime2;	// write process ended time
	private String elapseTime2;		// write process elapse time
	
	private static final String [] columnName;
	
	static {
		columnName = new String [] {
				"模組名稱",
				"處理行數(L)", "起始時間(L)", "結束時間(L)", "耗時(L)",
				"處理行數(W)", "總行數(W)", "起始時間(W)", "結束時間(W)", "耗時(W)"
		};
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public Integer getCurrentLine() {
		return currentLine;
	}

	public void setCurrentLine(Integer currentLine) {
		this.currentLine = currentLine;
	}

	public Timestamp getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Timestamp beginTime) {
		this.beginTime = beginTime;
	}

	public Timestamp getEndedTime() {
		return endedTime;
	}

	public void setEndedTime(Timestamp endedTime) {
		this.endedTime = endedTime;
	}

	public void setElapseTime(String elapseTime) {
		this.elapseTime = elapseTime;
	}

	public String getElapseTime() {
		if (endedTime == null) {
			elapseTime = "";
		}else{
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(endedTime.getTime() - beginTime.getTime());
			elapseTime = StringUtil.concat(
					String.format("%02d", c.get(Calendar.MINUTE)), ":", 
					String.format("%02d", c.get(Calendar.SECOND)), ".",
					String.format("%03d", c.get(Calendar.MILLISECOND)) );
		}
		
		return elapseTime;
	}

	public Integer getCurrentLine2() {
		return currentLine2;
	}

	public void setCurrentLine2(Integer currentLine2) {
		this.currentLine2 = currentLine2;
	}

	public Timestamp getBeginTime2() {
		return beginTime2;
	}

	public void setBeginTime2(Timestamp beginTime2) {
		this.beginTime2 = beginTime2;
	}

	public Timestamp getEndedTime2() {
		return endedTime2;
	}

	public void setEndedTime2(Timestamp endedTime2) {
		this.endedTime2 = endedTime2;
	}

	public String getElapseTime2() {
		if (endedTime2 == null) {
			elapseTime2 = "";
		}else{
			elapseTime2 = CVSFunc.fxElapseTime(endedTime2.getTime() - beginTime2.getTime());
		}
		
		return elapseTime2;
	}

	public void setElapseTime2(String elapseTime2) {
		this.elapseTime2 = elapseTime2;
	}

	public Integer getTotalLines2() {
		return totalLines2;
	}

	public void setTotalLines2(Integer totalLines2) {
		this.totalLines2 = totalLines2;
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	@Override
	public String[] getColumnFormat() {
		return new String[] {
				null,
				null, "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss", null,
				null, null, "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss", null};
	}

	@Override
	public String[] getColumnName() {
		return columnName;
	}

	@Override
	public int[] getColumnWidth() {
//		"模組名稱",
//		"處理行數(L)", "起始時間(L)", "結束時間(L)", "耗時(L)",
//		"處理行數(W)", "總行數(W)", "起始時間(W)", "結束時間(W)", "耗時(W)"
		return new int[] {
				130,
				80, 145, 145, 70,
				80, 80, 145, 145, 70};
	}

}
