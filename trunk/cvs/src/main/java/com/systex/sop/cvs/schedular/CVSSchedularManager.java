package com.systex.sop.cvs.schedular;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.systex.sop.cvs.util.PropReader;

public class CVSSchedularManager {
	private volatile static CVSSchedularManager instance = null;
	private Scheduler s = null;
	
	{
		try {
			s = new StdSchedulerFactory().getScheduler();
			JobDetail job = JobBuilder.newJob()
			.ofType(CVSJob.class)
			.withIdentity("CVSJob", "DefaultGroup")
			.build();
			
			Trigger trigger = TriggerBuilder.newTrigger()
	        .withIdentity("CVSTrigger", "Group1")
	        .withSchedule(CronScheduleBuilder.cronSchedule(PropReader.getProperty("CVS.CRONTAB")))
	        .build();
			
			s.scheduleJob(job, trigger);
		}catch(Exception e){
			throw new RuntimeException (e);
		}
	}
	
	/** Singleton **/
	public static CVSSchedularManager getInstance() {
		if (instance == null) {
			synchronized (CVSSchedularManager.class) {
				if (instance == null)
					instance = new CVSSchedularManager();
			}
		}
		
		return instance;
	}
	
	public void start() throws SchedulerException {
		getInstance().s.start();
	}
	
	public void suspend() throws SchedulerException {
		getInstance().s.standby();
	}
	
	public void shutdown() throws SchedulerException {
		getInstance().s.shutdown();
		instance = null;
	}
	
	public boolean isStarted() throws SchedulerException {
		return getInstance().s.isStarted();
	}
	
	public boolean isStandby() throws SchedulerException {
		return getInstance().s.isInStandbyMode();
	}
	
	public boolean isShutdown() throws SchedulerException {
		return getInstance().s.isShutdown();
	}
	
}
