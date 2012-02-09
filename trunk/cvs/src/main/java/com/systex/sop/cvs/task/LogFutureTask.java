package com.systex.sop.cvs.task;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import javax.swing.SwingUtilities;

import org.apache.commons.beanutils.PropertyUtils;

import com.systex.sop.cvs.helper.CVSLog;
import com.systex.sop.cvs.helper.CVSModuleHelper;
import com.systex.sop.cvs.ui.SyncPage;
import com.systex.sop.cvs.ui.Workspace;
import com.systex.sop.cvs.ui.Workspace.PAGE;
import com.systex.sop.cvs.ui.tableClass.CVSTableModel;
import com.systex.sop.cvs.ui.tableClass.LogResultDO;
import com.systex.sop.cvs.util.PropReader;
import com.systex.sop.cvs.util.TimestampHelper;

/**
 * Future task class that retrieve CVS Log
 * <p>
 * 
 * @author .[- _"].
 *
 */
public class LogFutureTask {
	private Map<String, FutureTask<TaskResult>> taskMap = new HashMap<String, FutureTask<TaskResult>>();
	private Map<String, Boolean> taskDoneMap = new HashMap<String, Boolean>();
	private ExecutorService service = Executors.newCachedThreadPool();
	
	public LogFutureTask() {}
	
	public boolean execute(Timestamp edate) {
		
		/** 執行 **/
		try {
			// 取得所有模組項目
			CVSModuleHelper moduleHelper = new CVSModuleHelper();
			
			// 逐個模組項目進行LOG之取得 (執行CVS.EXE向CVS SERVER要求CVS LOG)
			for (String module : moduleHelper.getMap().keySet()) {
				FutureTask<TaskResult> task = new FutureTask<TaskResult>(new LogCallable(module, moduleHelper.getMap().get(module), edate));
				taskDoneMap.put(module, Boolean.FALSE);		// 初始該模組為尚為完成
				taskMap.put(module, task);					// 將該模組的工作暫存至MAP
				service.submit(task);						// 執行該模組之工作
			}
			
			final List<LogResultDO> tList = new ArrayList<LogResultDO>();			// 所有模組的當前執行結果
			while (true) {
				try {
					Thread.sleep(PropReader.getPropertyInt("CVS.EXEC_INTERVAL"));	// 休息間隔
				} catch (InterruptedException e) {
				}

				tList.clear();														// 清空執行結果 (執行結果每次都重新產生)
				
				// 逐個模組去取得執行結果並轉成輸出至JTABLE之結構 (extends CVSTableClass)
				for (String module : getTaskMap().keySet()) {
					LogResultDO t = new LogResultDO();
					TaskResult tempResult = TaskResult.getResultMap().get(module);	// 取得即時之結果 (可能尚未完成)
					if (tempResult == null) {
						tempResult = new TaskResult();
						tempResult.setModule(module);
					}
					FutureTask<TaskResult> task = getTaskMap().get(module);			// 取得該模組之FUTURE TASK
					PropertyUtils.copyProperties(t, tempResult);
					if (task.isDone()) {
						getTaskDoneMap().put(module, Boolean.TRUE);
						tempResult = task.get();									// 取得已完成之結果 (從TaskResult.getResultMap()也可以)
						t.setEndedTime(tempResult.getEndedTime());
					}
					tList.add(t);
				}
				
				SwingUtilities.invokeLater(new java.lang.Runnable() {
					@Override
					public void run() {
						SyncPage page = (SyncPage) Workspace.getPage(PAGE.SYNC_CVS);
						page.getTable().setModel(new CVSTableModel(tList));
					}
				});
				
				// Check is finish or not
				boolean isFinish = true;
				for (String module : getTaskDoneMap().keySet()) {
					if (!getTaskDoneMap().get(module)) {
						isFinish = false;
						break;
					}
				}
				
				if (isFinish) break;
			} // while

		}catch(Exception e){
			CVSLog.getLogger().error(this, e);
			return false;
		}finally{
			service.shutdown();
			taskMap.clear();
		}
		
		return true;
	}
	
	public Map<String, FutureTask<TaskResult>> getTaskMap() {
		return taskMap;
	}

	public Map<String, Boolean> getTaskDoneMap() {
		return taskDoneMap;
	}

	public static void main(String [] args) {
		LogFutureTask logManager = new LogFutureTask();
		logManager.execute(TimestampHelper.convertToTimestamp("20120129"));
	}
}