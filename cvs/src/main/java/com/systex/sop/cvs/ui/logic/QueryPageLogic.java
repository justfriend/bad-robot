package com.systex.sop.cvs.ui.logic;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

import org.apache.commons.lang.time.StopWatch;

import com.systex.sop.cvs.dao.CVSQueryDAO;
import com.systex.sop.cvs.dto.Tbsoptcvsmap;
import com.systex.sop.cvs.dto.Tbsoptcvsver;
import com.systex.sop.cvs.helper.CVSFunc;
import com.systex.sop.cvs.helper.CVSLog;
import com.systex.sop.cvs.helper.CVSModuleHelper;
import com.systex.sop.cvs.message.CxtMessageQueue;
import com.systex.sop.cvs.ui.ModifyDialog;
import com.systex.sop.cvs.ui.StartUI;
import com.systex.sop.cvs.ui.TagDialog;
import com.systex.sop.cvs.ui.VersionDialog;
import com.systex.sop.cvs.ui.customize.comp.SSSJTable;
import com.systex.sop.cvs.ui.tableClass.TagDO;
import com.systex.sop.cvs.ui.tableClass.VerMapDO;
import com.systex.sop.cvs.ui.tableClass.VerTreeDO;
import com.systex.sop.cvs.util.PropReader;
import com.systex.sop.cvs.util.StringUtil;
import com.systex.sop.cvs.util.TableUtil;

public class QueryPageLogic {
	private CVSQueryDAO dao = new CVSQueryDAO();
	
	/** 註記POPUP MENU **/
	public  void registerPopupMenu(final SSSJTable table,
			final TagDialog tagDialog, final ModifyDialog modifyDialog, final VersionDialog versionDialog)
	{
		JPopupMenu popup = table.getPopup();
		popup.setBorder(new BevelBorder(BevelBorder.RAISED));
		
		// 檢示標籤
		JMenuItem item = new JMenuItem("檢示標籤");
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Long rcsid = (Long) table.getSelectValueAt("RCSID");
				String ver = (String) table.getSelectValueAt("最新版號");
				String filename = (String) table.getSelectValueAt("檔案名稱");
				doRetrieveTagInfo(rcsid, ver, filename, tagDialog);
			}
		});
		popup.add(item);
		popup.addSeparator();
		
		// 檢示版本樹
		item = new JMenuItem("檢示版本樹");
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Long rcsid = (Long) table.getSelectValueAt("RCSID");
				String filename = (String) table.getSelectValueAt("檔案名稱");
				doQueryVerTree(rcsid, filename, versionDialog);
			} });
		popup.add(item);
		popup.addSeparator();
		
		// 修改註記
		item = new JMenuItem("修改註記");
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Long rcsid = (Long) table.getSelectValueAt("RCSID");
				String ver = (String) table.getSelectValueAt("最新版號");
				doRetrieveVerInfo(rcsid, ver, modifyDialog);
			} });
		popup.add(item);
		
		Dimension size = popup.getPreferredSize();
		popup.setPreferredSize(new Dimension((int) (size.width * 1.4), size.height));
	}
	
	/** 檢示版本樹 **/
	public void doQueryVerTree(Long rcsid, String filename, VersionDialog versionDialog) {
		try {
			// 進行查詢
			StopWatch s = new StopWatch();
			s.start();
			List<VerTreeDO> result = dao.queryVerTree(rcsid);
			s.stop();
			
			// 初始對話框
			Rectangle size = versionDialog.getBounds();
			versionDialog.setBounds(
					PropReader.getPropertyInt("CVS.VERTREE_X"),
					PropReader.getPropertyInt("CVS.VERTREE_Y"),
					(int) size.getWidth(), (int) size.getHeight() );
			versionDialog.getFilename_jTxtF().setText(filename);
			versionDialog.setVisible(true);
			
			// 顯示統計
			if (result.size() == 0) CxtMessageQueue.addCxtMessage("No DATA");
			StartUI.getInstance().getFrame().setMessage(StringUtil.concat("查詢筆數：", result.size(), ", 耗時：" + CVSFunc.fxElapseTime(s.getTime())));
			
			// 更新至Table
			TableUtil.addRows(versionDialog.getTable(), result);
		} catch (Exception e) {
			CVSLog.getLogger().error(this, e);
			StartUI.getInstance().getFrame().setMessage("查詢發生異常：" + e.getMessage());
		}
	}
	
	
	/** 一般查詢 **/
	public  void doQueryNormal(JTable table, String author, boolean isIgnoreDel, Timestamp beginTime, Timestamp endedTime,
			String filename, String program, String id, String desc, String tagname)
	{
		try {
			// 進行查詢
			StopWatch s = new StopWatch();
			s.start();
			List<VerMapDO> result = dao.queryNormal(
					author, isIgnoreDel, beginTime, endedTime, filename, program, id, desc, tagname
			);
			s.stop();
			
			// 顯示統計
			if (result.size() == 0) CxtMessageQueue.addCxtMessage("No DATA");
			StartUI.getInstance().getFrame().setMessage(StringUtil.concat("查詢筆數：", result.size(), ", 耗時：" + CVSFunc.fxElapseTime(s.getTime())));
			
			// 更新至Table
			TableUtil.addRows(table, result);
		} catch (Exception e) {
			CVSLog.getLogger().error(this, e);
			StartUI.getInstance().getFrame().setMessage("查詢發生異常：" + e.getMessage());
		}
	}

	
	/**  查詢提交註記錯誤或遺漏 **/
	public void doQueryCommentMiss(String author, boolean isIgnoreDel, Timestamp date, JTable table) {
		try {
			// 進行查詢
			StopWatch s = new StopWatch();
			s.start();
			List<VerMapDO> result = dao.queryCommentMiss(author, isIgnoreDel, date);
			s.stop();
			
			// 顯示統計
			if (result.size() == 0) CxtMessageQueue.addCxtMessage("No DATA");
			StartUI.getInstance().getFrame().setMessage(StringUtil.concat("查詢筆數：", result.size(), ", 耗時：" + CVSFunc.fxElapseTime(s.getTime())));
			
			// 更新至Table
			TableUtil.addRows(table, result);
		} catch (Exception e) {
			CVSLog.getLogger().error(this, e);
			StartUI.getInstance().getFrame().setMessage("查詢發生異常：" + e.getMessage());
		}
	}
	
	/** 查詢最新版本未上TAG **/
	public void doQueryNewVerNoTag(String author, boolean isIgnoreDel, JTable table) {
		try {
			// 進行查詢
			StopWatch s = new StopWatch();
			s.start();
			List<VerMapDO> result = dao.queryNewVerNoTag(author, isIgnoreDel);
			s.stop();
			
			// 顯示統計
			if (result.size() == 0) CxtMessageQueue.addCxtMessage("No DATA");
			StartUI.getInstance().getFrame().setMessage(StringUtil.concat("查詢筆數：", result.size(), ", 耗時：" + CVSFunc.fxElapseTime(s.getTime())));
			
			// 更新至Table
			TableUtil.addRows(table, result);
		} catch (Exception e) {
			CVSLog.getLogger().error(this, e);
			StartUI.getInstance().getFrame().setMessage("查詢發生異常：" + e.getMessage());
		}
	}
	
	/** 查詢TAG資料 **/
	public void doRetrieveTagInfo(Long rcsid, String ver, String filename, TagDialog tagDialog) {

		if (StringUtil.anyEmpty(rcsid, ver, filename)) return;
		
		List<TagDO> tList = null;
		try {
			// 查詢資料
			StopWatch s = new StopWatch();
			s.start();
			tList = dao.retrieveTAG(rcsid, ver);
			s.stop();
			
			// 初始對話框
			Rectangle size = tagDialog.getBounds();
			tagDialog.setBounds(
					PropReader.getPropertyInt("CVS.TAG_X"),
					PropReader.getPropertyInt("CVS.TAG_Y"),
					(int) size.getWidth(), (int) size.getHeight() );
			tagDialog.setTitle(rcsid, ver, filename, 20);
			tagDialog.setVisible(true);
			
			// 顯示統計
			if (tList.size() == 0) {
				TagDO nodata = new TagDO();
				nodata.setTagname("NO DATA FOUND");
				tList.add(nodata);
			}
			StartUI.getInstance().getFrame().setMessage(StringUtil.concat("查詢筆數：", tList.size(), ", 耗時：" + CVSFunc.fxElapseTime(s.getTime())));
			
			// 更新至Table
			TableUtil.addRows(tagDialog.getTable(), tList);
		}catch(Exception e){
			CVSLog.getLogger().error(this, e);
			StartUI.getInstance().getFrame().setMessage("查詢發生異常：" + e.getMessage());
		}
	}
	
	/** 取得特定版號資料 **/
	public void doRetrieveVerInfo(Long rcsid, String ver, ModifyDialog modifyDialog) {

		if (StringUtil.anyEmpty(rcsid, ver)) return;
		System.err.println (SwingUtilities.isEventDispatchThread());
		try {
			// 查詢資料
			Tbsoptcvsver v = dao.retrieveVER(rcsid, ver);
			if (v == null) return;
			Tbsoptcvsmap m = v.getTbsoptcvsmap();
			
			// 初始對話框
			CVSModuleHelper mHelper = new CVSModuleHelper();
			Rectangle size = modifyDialog.getBounds();
			modifyDialog.setTitle(StringUtil.concat("Author is ", v.getAuthor()) );
			modifyDialog.setBounds(
					PropReader.getPropertyInt("CVS.MODIFY_X"),
					PropReader.getPropertyInt("CVS.MODIFY_Y"),
					(int) size.getWidth(), (int) size.getHeight() );
			modifyDialog.setCurrentVer(v);
			modifyDialog.getFilename_jTxtF().setText(m.getFilename());
			modifyDialog.getVersion_jTxtF().setText(ver);
			modifyDialog.getWorkdir_jTxtF().setText(mHelper.getPath(CVSFunc.fxModule(m.getModule(), m.getClientserver())));
			modifyDialog.getFilepath_jTxtF().setText(CVSFunc.fxExtraSource(m.getRcsfile()));
			modifyDialog.getOldComment_jTxtF().setText(v.getFulldesc());
			modifyDialog.getNewComment_jTxtF().setText("");	// clear
			modifyDialog.setVisible(true);
			
			// 更新至Table
		}catch(Exception e){
			CVSLog.getLogger().error(this, e);
			StartUI.getInstance().getFrame().setMessage("查詢發生異常：" + e.getMessage());
		}
	}
}
