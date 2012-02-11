package com.systex.sop.cvs.ui.logic;

import java.awt.Rectangle;
import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang.time.StopWatch;

import com.systex.sop.cvs.dao.CVSQueryDAO;
import com.systex.sop.cvs.helper.CVSLog;
import com.systex.sop.cvs.ui.QueryClassicPage;
import com.systex.sop.cvs.ui.StartUI;
import com.systex.sop.cvs.ui.TAGDialog;
import com.systex.sop.cvs.ui.tableClass.CommentMissDO;
import com.systex.sop.cvs.ui.tableClass.NewVerNoTagDO;
import com.systex.sop.cvs.ui.tableClass.TagDO;
import com.systex.sop.cvs.util.PropReader;
import com.systex.sop.cvs.util.StringUtil;
import com.systex.sop.cvs.util.TableUtil;

public class QueryClassicPageLogic {
	private CVSQueryDAO dao = new CVSQueryDAO();
	private QueryClassicPage page;
	
	public QueryClassicPageLogic(QueryClassicPage page) {
		this.page = page;
	}
	
	/**  查詢提交註記錯誤或遺漏 **/
	public void doQueryCommentMiss(String author, boolean isIgnoreDel, Timestamp date) {
		try {
			// 進行查詢
			StopWatch s = new StopWatch();
			s.start();
			List<CommentMissDO> result = dao.queryCommentMiss(author, isIgnoreDel, date);
			s.stop();
			
			// 顯示統計
			if (result.size() == 0) StartUI.getInstance().getFrame().setCxtMessage("No DATA");
			StartUI.getInstance().getFrame().setMessage(StringUtil.concat("查詢筆數：", result.size(), ", 耗時：" + s.getTime()));
			
			// 更新至Table
			TableUtil.addRows(page.getTable_1(), result);
		} catch (Exception e) {
			CVSLog.getLogger().error(this, e);
			StartUI.getInstance().getFrame().setMessage("查詢發生異常：" + e.getMessage());
		}
	}
	
	/** 查詢最新版本未上TAG **/
	public void doQueryNewVerNoTag(String author, boolean isIgnoreDel) {
		try {
			// 進行查詢
			StopWatch s = new StopWatch();
			s.start();
			List<NewVerNoTagDO> result = dao.queryNewVerNoTag(author, isIgnoreDel);
			s.stop();
			
			// 顯示統計
			if (result.size() == 0) StartUI.getInstance().getFrame().setCxtMessage("No DATA");
			StartUI.getInstance().getFrame().setMessage(StringUtil.concat("查詢筆數：", result.size(), ", 耗時：" + s.getTime()));
			
			// 更新至Table
			TableUtil.addRows(page.getTable(), result);
		} catch (Exception e) {
			CVSLog.getLogger().error(this, e);
			StartUI.getInstance().getFrame().setMessage("查詢發生異常：" + e.getMessage());
		}
	}
	
	/** 查詢TAG資料 **/
	public void doRetrieveTagInfo(Long rcsid, String ver, String filename) {

		if (StringUtil.anyEmpty(rcsid, ver, filename)) return;
		
		List<TagDO> tList = null;
		try {
			// 查詢資料
			StopWatch s = new StopWatch();
			s.start();
			tList = dao.retrieveTAG(rcsid, ver);
			s.stop();
			
			// 初始對話框
			TAGDialog tagDialog = page.getTagDialog();
			Rectangle size = tagDialog.getBounds();
			page.getTagDialog().setBounds(
					PropReader.getPropertyInt("CVS.TAG_X"),
					PropReader.getPropertyInt("CVS.TAG_Y"),
					(int) size.getWidth(), (int) size.getHeight() );
			tagDialog.setTitle(rcsid, ver, filename);
			tagDialog.setVisible(true);
			
			// 顯示統計
			if (tList.size() == 0) StartUI.getInstance().getFrame().setCxtMessage("No DATA");
			StartUI.getInstance().getFrame().setMessage(StringUtil.concat("查詢筆數：", tList.size(), ", 耗時：" + s.getTime()));
			
			// 更新至Table
			TableUtil.addRows(tagDialog.getTable(), tList);
		}catch(Exception e){
			CVSLog.getLogger().error(this, e);
			StartUI.getInstance().getFrame().setMessage("查詢發生異常：" + e.getMessage());
		}
	}
}
