package com.systex.sop.cvs.schedular;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.systex.sop.cvs.util.PropReader;

public class CVSJob implements Job {

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// TODO Auto-generated method stub
		System.err.println ("[" + PropReader.getProperty("CACHE.PATH") + "]");
//		System.err.println ("[dfdsfds]");
	}

}
