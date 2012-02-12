package com.systex.sop.cvs.task;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class TaskResult {
	private Map<String, Object> paraMap = new HashMap<String, Object>();
	private Timestamp beginTime;		// 開始時間(LOG)
	private Timestamp endedTime;		// 結束時間(LOG)
	private String errMsg;				// 錯誤訊息
	private Boolean isDone;				// 是否成功
	
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

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public Boolean getIsDone() {
		return isDone;
	}

	public void setIsDone(Boolean isDone) {
		this.isDone = isDone;
	}
	
	public Map<String, Object> getParaMap() {
		return paraMap;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
