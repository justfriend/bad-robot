package com.systex.sop.cvs.task;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import javax.swing.SwingUtilities;

import org.apache.commons.beanutils.PropertyUtils;

import com.systex.sop.cvs.helper.CVSLog;
import com.systex.sop.cvs.helper.CVSModuleHelper;
import com.systex.sop.cvs.message.CxtMessageQueue;
import com.systex.sop.cvs.ui.SyncPage;
import com.systex.sop.cvs.ui.Workspace;
import com.systex.sop.cvs.ui.Workspace.PAGE;
import com.systex.sop.cvs.ui.tableClass.LogResultDO;
import com.systex.sop.cvs.util.PropReader;
import com.systex.sop.cvs.util.TableUtil;

/**
 * Future task class that write CVS Log into DB
 * <p>
 * 
 * @author .[- _"].
 *
 */
public class WriteFutureTask {
	private static WriteFutureTask instance;
	private Map<String, FutureTask<TaskSyncResult>> taskMap;
	private Map<String, Boolean> taskDoneMap;
	private ExecutorService service;
	private int previousTotal = 0;
	
	{
		taskMap = new LinkedHashMap<String, FutureTask<TaskSyncResult>>();
		taskDoneMap = new HashMap<String, Boolean>();
		newService();
	}
	
	private WriteFutureTask(){}
	
	/** Singleton **/
	public static WriteFutureTask getInstance() {
		if (instance == null) {
			synchronized (WriteFutureTask.class) {
				if (instance == null)
					instance = new WriteFutureTask();
			}
		}
		
		return instance;
	}
	
	public boolean execute(Timestamp edate, boolean isFullSync) {
		try {
			CVSModuleHelper moduleHelper = new CVSModuleHelper();
			for (String module : moduleHelper.getMap().keySet()) {
				FutureTask<TaskSyncResult> task = new FutureTask<TaskSyncResult>(new WriteCallable(module, edate, isFullSync));
				service.submit(task);														// 模組工作加入執行服務
				taskMap.put(module, task);													// 模組工作暫存至MAP
				taskDoneMap.put(module, Boolean.FALSE);										// 模組工作完成度MAP
			}
			
			final List<LogResultDO> tList = new ArrayList<LogResultDO>();					// 所有模組的執行結果 (當前)
			while (true) {
				try {
					Thread.sleep(PropReader.getPropertyInt("CVS.EXEC_INTERVAL"));
				} catch (InterruptedException e) {
					CVSLog.getLogger().warn(this, e);
				}
				
				if (service.isShutdown() || service.isTerminated()) return false;			// 執行服務中斷則離開

				tList.clear();																// 清空所有模組的執行結果 (每次循環重新更新)
				
				for (String module : taskMap.keySet()) {
					TaskSyncResult tempResult = TaskSyncResult.getTaskResult(module);		// 取得模組的當前結果 (可能尚未完成)
					if (tempResult == null) {
						tempResult = new TaskSyncResult();	// fake
						tempResult.setModule(module);
					}
					
					FutureTask<TaskSyncResult> task = taskMap.get(module);					// 取得模組工作並檢查工作是否已完成
					LogResultDO t = new LogResultDO();
					PropertyUtils.copyProperties(t, tempResult);
					if (task.isDone()) {
						taskDoneMap.put(module, Boolean.TRUE);
						tempResult = task.get();
						t.setEndedTime2(tempResult.getEndedTime2());
					}
					tList.add(t);
				}
				
				int currentTotal = 0;														// 計算當前總處理行數
				for (LogResultDO t : tList) {
					currentTotal += t.getCurrentLine2();
				}
				
				SwingUtilities.invokeAndWait(new java.lang.Runnable() {						// 呈現處理結果
					@Override
					public void run() {
						SyncPage page = (SyncPage) Workspace.getPage(PAGE.SYNC_CVS);
						TableUtil.addRows(page.getTable(), tList);
					}
				});
				
				final int plusTotal = currentTotal - previousTotal;							// 計算此次循環處理行數並呈現於畫面
				previousTotal = currentTotal;
				SwingUtilities.invokeAndWait(new java.lang.Runnable() {
					@Override
					public void run() {
						if (PAGE.SYNC_CVS.equals(Workspace.getCurrentPageEnum())) {
							CxtMessageQueue.addCxtMessage(
									((plusTotal >= 0)? "+": "") + plusTotal );
						}
					}
				});
				
				boolean isFinish = true;													// 檢查所有模組是否已完成，若都完成則結果
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
			previousTotal = 0;
		}
		
		return true;
	}
	
	public void newService() {
		if (service == null || (service.isShutdown() && service.isTerminated()) ) {
			service = Executors.newFixedThreadPool(PropReader.getPropertyInt("CVS.THREAD_WRITE"));
		}
	}
	
	public ExecutorService getService() {
		return service;
	}

	public static void main(String [] args) {
		
	}
}
