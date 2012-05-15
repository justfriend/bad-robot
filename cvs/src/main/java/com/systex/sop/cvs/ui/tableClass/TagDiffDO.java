package com.systex.sop.cvs.ui.tableClass;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 兩tag間的差異資訊
 * 
 * @author wmy
 * 
 */
/*@Entity
@Table(name = "TagDiffDO")*/
public class TagDiffDO extends CVSBaseDO implements java.io.Serializable {

	private static final long serialVersionUID = -2972984268126090912L;
	private String DESC_ID;
	private String FULLDESC;
	private String AUTHOR;
	private Timestamp VERDATE;
	private String FILENAME;
	private String PROGRAMID;
	private String MODULE;
	private String VERSION;
	private String VERSIONHEAD;

	public String getDESC_ID() {
		return DESC_ID;
	}

	public void setDESC_ID(String dESC_ID) {
		DESC_ID = dESC_ID;
	}

	public String getFULLDESC() {
		return FULLDESC;
	}

	public void setFULLDESC(String fULLDESC) {
		FULLDESC = fULLDESC;
	}

	public String getAUTHOR() {
		return AUTHOR;
	}

	public void setAUTHOR(String aUTHOR) {
		AUTHOR = aUTHOR;
	}

	public Timestamp getVERDATE() {
		return VERDATE;
	}

	public void setVERDATE(Timestamp vERDATE) {
		VERDATE = vERDATE;
	}

	public String getFILENAME() {
		return FILENAME;
	}

	public void setFILENAME(String fILENAME) {
		FILENAME = fILENAME;
	}

	public String getPROGRAMID() {
		return PROGRAMID;
	}

	public void setPROGRAMID(String pROGRAMID) {
		PROGRAMID = pROGRAMID;
	}

	public String getMODULE() {
		return MODULE;
	}

	public void setMODULE(String mODULE) {
		MODULE = mODULE;
	}

	public String getVERSION() {
		return VERSION;
	}

	public void setVERSION(String vERSION) {
		VERSION = vERSION;
	}

	public String getVERSIONHEAD() {
		return VERSIONHEAD;
	}

	public void setVERSIONHEAD(String vERSIONHEAD) {
		VERSIONHEAD = vERSIONHEAD;
	}

	private static final String[] columnName;

	static {
		columnName = new String[] { "提交ID", "完整提交內容", "作者", "提交時間", "檔案名稱",
				"程式名稱", "模組名稱", "版號", "最新版號" };
	}



	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}

	@Override
	public String[] getColumnFormat() {
		return new String[] { null, null, null, "yyyy-MM-dd HH:mm:ss", null,
				null, null, null, null };
	}

	@Override
	public String[] getColumnName() {
		return columnName;
	}

	@Override
	public int[] getColumnWidth() {
		/*
		 * "提交ID", "完整提交內容", "作者", "提交時間", "檔案名稱", "程式名稱", "模組名稱", "版號", "最新版號"
		 */
		return new int[] { 60, 240, 60, 145, 130, 130, 60, 60, 60 };
	}

}
