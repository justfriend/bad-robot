package com.systex.sop.cvs.task;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import com.systex.sop.cvs.helper.CVSLog;
import com.systex.sop.cvs.helper.CVSModuleHelper;
import com.systex.sop.cvs.util.TimestampHelper;

public class WriteFutureTask {
	private List<FutureTask<TaskResult>> taskList = new ArrayList<FutureTask<TaskResult>>();
	private ExecutorService service = Executors.newCachedThreadPool();
	
	public WriteFutureTask() {}
	
	public void execute(Timestamp edate) {
		try {
			CVSModuleHelper moduleHelper = new CVSModuleHelper();
			for (String module : moduleHelper.getMap().keySet()) {
				FutureTask<TaskResult> task = new FutureTask<TaskResult>(new WriteCallable(module, edate));
				taskList.add(task);
				service.submit(task);
			}
			
			for (FutureTask<TaskResult> task : taskList) {
				try {
					TaskResult result = task.get();
					System.err.println (result);
				} catch (Exception e) {
					CVSLog.getLogger().error(this, e);
				}
			}
		}catch(Exception e){
			CVSLog.getLogger().error(this, e);
		}finally{
			service.shutdown();
			taskList.clear();
		}
	}
	
	public static void main(String [] args) {
		WriteFutureTask logManager = new WriteFutureTask();
		logManager.execute(TimestampHelper.convertToTimestamp("20120105"));
	}
}
