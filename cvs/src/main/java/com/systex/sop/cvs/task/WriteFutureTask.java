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
import com.systex.sop.cvs.ui.StartUI;
import com.systex.sop.cvs.ui.SyncPage;
import com.systex.sop.cvs.ui.Workspace;
import com.systex.sop.cvs.ui.Workspace.PAGE;
import com.systex.sop.cvs.ui.tableClass.CVSTableModel;
import com.systex.sop.cvs.ui.tableClass.LogResultDO;
import com.systex.sop.cvs.util.PropReader;
import com.systex.sop.cvs.util.TimestampHelper;

/**
 * Future task class that write CVS Log into DB
 * <p>
 * 
 * @author .[- _"].
 *
 */
public class WriteFutureTask {
	private Map<String, FutureTask<TaskResult>> taskMap = new HashMap<String, FutureTask<TaskResult>>();
	private Map<String, Boolean> taskDoneMap = new HashMap<String, Boolean>();
	private ExecutorService service = Executors.newFixedThreadPool(PropReader.getPropertyInt("CVS.THREAD_WRITE"));
	private int previousTotal = 0;		// 上次同步的總數
	
	public WriteFutureTask() {}
	
	public boolean execute(String hostname, Timestamp edate, boolean isFullSync) {
		
		/** 執行 **/
		try {
			// 取得所有模組項目
			CVSModuleHelper moduleHelper = new CVSModuleHelper();
			
			// 逐個模組項目進行LOG之取得 (執行CVS.EXE向CVS SERVER要求CVS LOG)
			for (String module : moduleHelper.getMap().keySet()) {
				FutureTask<TaskResult> task = new FutureTask<TaskResult>(new WriteCallable(hostname, module, edate, isFullSync));
				service.submit(task);						// 執行該模組之工作
				taskMap.put(module, task);					// 將該模組的工作暫存至MAP
				taskDoneMap.put(module, Boolean.FALSE);		// 初始該模組為尚為完成
			}
			
			final List<LogResultDO> tList = new ArrayList<LogResultDO>();			// 所有模組的當前執行結果
			while (true) {
				try {
					Thread.sleep(PropReader.getPropertyInt("CVS.EXEC_INTERVAL"));	// 休息間隔
				} catch (InterruptedException e) {
					CVSLog.getLogger().warn(this, e);
				}
				
				if (service.isShutdown() || service.isTerminated()) return false;	// 供中斷後之判斷

				tList.clear();														// 清空執行結果 (執行結果每次都重新產生)
				
				// 逐個模組去取得執行結果並轉成輸出至JTABLE之結構 (extends CVSTableClass)
				for (String module : taskMap.keySet()) {
					TaskResult tempResult = TaskResult.getTaskResult(module);		// 取得即時之結果 (可能尚未完成)
					if (tempResult == null) {
						tempResult = new TaskResult();	// fake
						tempResult.setModule(module);
					}
					LogResultDO t = new LogResultDO();
					PropertyUtils.copyProperties(t, tempResult);
					FutureTask<TaskResult> task = taskMap.get(module);				// 取得該模組之FUTURE TASK
					if (task.isDone()) {
						taskDoneMap.put(module, Boolean.TRUE);						// 標記該模組為完成
						tempResult = task.get();									// 取得已完成之結果 (從TaskResult.getResultMap()也可以)
						t.setEndedTime2(tempResult.getEndedTime2());
					}
					tList.add(t);
				}
				
				// 更新至Table
				SwingUtilities.invokeAndWait(new java.lang.Runnable() {
					@Override
					public void run() {
						SyncPage page = (SyncPage) Workspace.getPage(PAGE.SYNC_CVS);
						page.getTable().setModel(new CVSTableModel(page.getTable(), tList));
					}
				});
				
				// 計算此次總計
				int currentTotal = 0;
				for (LogResultDO t : tList) {
					currentTotal += t.getCurrentLine2();
				}
				
				// 將此次新增的數量彈至畫面上
				final int plusTotal = currentTotal - previousTotal;
				previousTotal = currentTotal;
				SwingUtilities.invokeLater(new java.lang.Runnable() {
					@Override
					public void run() {
						if (PAGE.SYNC_CVS.equals(Workspace.getCurrentPage())) {
							StartUI.getInstance().getFrame().setCxtMessage("+" + plusTotal);
						}
					}
				});
				
				// Check is finish or not
				boolean isFinish = true;
				for (String module : taskDoneMap.keySet()) {
					if (!taskDoneMap.get(module)) {
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
	
	public ExecutorService getService() {
		return service;
	}

	public static void main(String [] args) {
		WriteFutureTask logManager = new WriteFutureTask();
		logManager.execute("1000073David", TimestampHelper.convertToTimestamp("20000105"), true);
	}
}
