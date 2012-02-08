package com.systex.sop.cvs.task;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import com.systex.sop.cvs.util.StringUtil;
import com.systex.sop.cvs.util.TimestampHelper;

public class TaskResult {
	/**
	 * Future task 只在處理結束時才能取得結果，在此將處理中的結果亦儲存起來
	 */
	private static Map<String, TaskResult> resultMap = new HashMap<String, TaskResult>();
	private String module;				// 模組名稱
	private Timestamp beginTime;		// 開始時間(LOG)
	private Timestamp endedTime;		// 結束時間(LOG)
	private Integer currentLine;		// 當前處理行數(LOG)
	private Integer totalLines;			// 總行數(LOG)
	private Timestamp beginTime2;		// 開始時間(WRITE)
	private Timestamp endedTime2;		// 結束時間(WRITE)
	private Integer currentLine2;		// 當前處理行數(WRITE)
	private Integer totalLines2;		// 總行數(WRITE)
	
	/** 清空處理結果 **/
	public static void clearResult() {
		resultMap.clear();
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
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

	public Integer getCurrentLine() {
		return currentLine;
	}

	public void setCurrentLine(Integer currentLine) {
		this.currentLine = currentLine;
	}

	public Integer getTotalLines() {
		return totalLines;
	}

	public void setTotalLines(Integer totalLines) {
		this.totalLines = totalLines;
	}

	public Integer getCurrentLine2() {
		return currentLine2;
	}

	public void setCurrentLine2(Integer currentLine2) {
		this.currentLine2 = currentLine2;
	}

	public Integer getTotalLines2() {
		return totalLines2;
	}

	public void setTotalLines2(Integer totalLines2) {
		this.totalLines2 = totalLines2;
	}

	public static Map<String, TaskResult> getResultMap() {
		return resultMap;
	}

	public String toString() {
		return StringUtil.concat("[Module]", module, " [BEGIN]", TimestampHelper.convertToHHMISS(beginTime), " [ENDED]", TimestampHelper.convertToHHMISS(endedTime));
	}
}
