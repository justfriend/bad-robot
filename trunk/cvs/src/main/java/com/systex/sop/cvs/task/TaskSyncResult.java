package com.systex.sop.cvs.task;

import java.sql.Timestamp;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class TaskSyncResult {
	/**
	 * Future task 只在處理結束時才能取得結果，在此將處理中的結果亦儲存起來
	 */
	private static ConcurrentHashMap<String, TaskSyncResult> resultMap = new ConcurrentHashMap<String, TaskSyncResult>();
	private String module;				// 模組名稱
	private Timestamp beginTime;		// 開始時間(LOG)
	private Timestamp endedTime;		// 結束時間(LOG)
	private Integer currentLine;		// 當前處理行數(LOG)
	private Integer totalLines;			// 總行數(LOG)
	private Timestamp beginTime2;		// 開始時間(WRITE)
	private Timestamp endedTime2;		// 結束時間(WRITE)
	private Integer currentLine2;		// 當前處理行數(WRITE)
	private Integer totalLines2;		// 總行數(WRITE)
	private Integer successFiles;		// 成功寫入的檔案數量
	private Integer failureFiles;		// 失敗寫入的檔案數量
	
	{
		currentLine = 0;
		totalLines = 0;
		currentLine2 = 0;
		totalLines2 = 0;
		successFiles = 0;
		failureFiles = 0;
	}

	/** 清空處理結果 **/
	public static void clearResult() {
		resultMap.clear();
	}
	
	/** 取得處理結果 **/
	public static TaskSyncResult getTaskResult(String module) {
		return resultMap.get(module);
	}
	
	/** 放入處理結果 **/
	public static void putTaskResult(String module, TaskSyncResult result) {
		resultMap.put(module, result);
	}

	public Timestamp getBeginTime() {
		return beginTime;
	}

	public Timestamp getBeginTime2() {
		return beginTime2;
	}

	public Integer getCurrentLine() {
		return currentLine;
	}

	public Integer getCurrentLine2() {
		return currentLine2;
	}

	public Timestamp getEndedTime() {
		return endedTime;
	}

	public Timestamp getEndedTime2() {
		return endedTime2;
	}

	public String getModule() {
		return module;
	}

	public Integer getTotalLines() {
		return totalLines;
	}

	public Integer getTotalLines2() {
		return totalLines2;
	}

	public void setBeginTime(Timestamp beginTime) {
		this.beginTime = beginTime;
	}

	public void setBeginTime2(Timestamp beginTime2) {
		this.beginTime2 = beginTime2;
	}

	public void setCurrentLine(Integer currentLine) {
		this.currentLine = currentLine;
	}

	public void setCurrentLine2(Integer currentLine2) {
		this.currentLine2 = currentLine2;
	}

	public void setEndedTime(Timestamp endedTime) {
		this.endedTime = endedTime;
	}

	public void setEndedTime2(Timestamp endedTime2) {
		this.endedTime2 = endedTime2;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public void setTotalLines(Integer totalLines) {
		this.totalLines = totalLines;
	}

	public void setTotalLines2(Integer totalLines2) {
		this.totalLines2 = totalLines2;
	}
	
	public Integer getSuccessFiles() {
		return successFiles;
	}

	public void setSuccessFiles(Integer successFiles) {
		this.successFiles = successFiles;
	}

	public Integer getFailureFiles() {
		return failureFiles;
	}

	public void setFailureFiles(Integer failureFiles) {
		this.failureFiles = failureFiles;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
