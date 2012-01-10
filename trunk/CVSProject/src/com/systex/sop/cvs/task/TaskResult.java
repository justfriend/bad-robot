package com.systex.sop.cvs.task;

import java.sql.Timestamp;

import com.systex.sop.cvs.util.StringUtil;
import com.systex.sop.cvs.util.TimestampHelper;

public class TaskResult {
	private String module;
	private String errMsg;
	private Timestamp beginTime;
	private Timestamp endedTime;

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
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
	
	public String toString() {
		return StringUtil.concat("[Module]", module, " [BEGIN]", TimestampHelper.convertToHHMISS(beginTime), " [ENDED]", TimestampHelper.convertToHHMISS(endedTime), " [SPEND]" + (endedTime.getTime() - beginTime.getTime()));
	}
}
