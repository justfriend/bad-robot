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
import javax.swing.border.BevelBorder;

import org.apache.commons.lang.time.StopWatch;

import com.systex.sop.cvs.dao.CVSQueryDAO;
import com.systex.sop.cvs.dto.Tbsoptcvsmap;
import com.systex.sop.cvs.dto.Tbsoptcvsver;
import com.systex.sop.cvs.helper.CVSFunc;
import com.systex.sop.cvs.helper.CVSLog;
import com.systex.sop.cvs.helper.CVSModuleHelper;
import com.systex.sop.cvs.message.CxtMessageQueue;
import com.systex.sop.cvs.ui.StartUI;
import com.systex.sop.cvs.ui.customize.comp.SSSJTable;
import com.systex.sop.cvs.ui.tableClass.TagDO;
import com.systex.sop.cvs.ui.tableClass.VerMapDO;
import com.systex.sop.cvs.ui.tableClass.VerTreeDO;
import com.systex.sop.cvs.util.PropReader;
import com.systex.sop.cvs.util.StringUtil;
import com.systex.sop.cvs.util.TableUtil;

public class QueryPageLogic {
	private CVSQueryDAO dao = new CVSQueryDAO();
	
	private void displayResult(JTable table, List<VerMapDO> result, StopWatch s) {
		// 更新至Table
		TableUtil.addRows(table, result);
		
		// 顯示統計
		if (result.size() == 0) {
			CxtMessageQueue.addCxtMessage("No DATA");
		}else if (result.size() >= PropReader.getPropertyInt("CVS.MAX_RESULT_COUNT")) {
			CxtMessageQueue.addCxtMessage(StringUtil.concat("資料量超過 (", PropReader.getPropertyInt("CVS.MAX_RESULT_COUNT"), ") 筆"));
		}
		StartUI.getInstance().getFrame().setMessage(StringUtil.concat("查詢筆數：", result.size(), ", 耗時：" + CVSFunc.fxElapseTime(s.getTime())));
	}
	
	/**  查詢提交註記錯誤或遺漏 **/
	public void doQueryCommentMiss(String author, boolean isIgnoreDel, Timestamp date, JTable table) {
		try {
			// 進行查詢
			StopWatch s = new StopWatch();
			s.start();
			List<VerMapDO> result = dao.queryCommentMiss(author, isIgnoreDel, date);
			s.stop();
			
			displayResult(table, result, s);
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
			
			displayResult(table, result, s);
		} catch (Exception e) {
			CVSLog.getLogger().error(this, e);
			StartUI.getInstance().getFrame().setMessage("查詢發生異常：" + e.getMessage());
		}
	}
	
	
	/** 一般查詢 **/
	public  void doQueryNormal(JTable table, String author, boolean isIgnoreDel, Timestamp beginTime, Timestamp endedTime,
			String filename, String program, String id, String desc, String tagname) {
		try {
			// 進行查詢
			StopWatch s = new StopWatch();
			s.start();
			List<VerMapDO> result = dao.queryNormal(
					author, isIgnoreDel, beginTime, endedTime, filename, program, id, desc, tagname
			);
			s.stop();
			
			displayResult(table, result, s);
		} catch (Exception e) {
			CVSLog.getLogger().error(this, e);
			StartUI.getInstance().getFrame().setMessage("查詢發生異常：" + e.getMessage());
		}
	}

	
	/** 檢示版本樹 **/
	public void doQueryVerTree(Long rcsid, String filename) {
		if (StringUtil.anyEmpty(rcsid, filename)) return;
		
		try {
			// 進行查詢
			StopWatch s = new StopWatch();
			s.start();
			List<VerTreeDO> result = dao.queryVerTree(rcsid);
			s.stop();
			
			// 初始對話框
			Rectangle size = StartUI.getInstance().getVersionDialog().getBounds();
			StartUI.getInstance().getVersionDialog().setBounds(
					PropReader.getPropertyInt("CVS.VERTREE_X"),
					PropReader.getPropertyInt("CVS.VERTREE_Y"),
					(int) size.getWidth(), (int) size.getHeight() );
			StartUI.getInstance().getVersionDialog().getFilename_jTxtF().setText(filename);
			StartUI.getInstance().getVersionDialog().setVisible(true);
			
			// 顯示統計
			StartUI.getInstance().getFrame().setMessage(StringUtil.concat("查詢筆數：", result.size(), ", 耗時：" + CVSFunc.fxElapseTime(s.getTime())));
			
			// 去除重覆欄位
			String version = null;
			for (VerTreeDO obj : result) {
				if (version == null) {
					version = obj.getVersion();
				}else{
					if (version.compareTo(obj.getVersion()) != 0) {
						version = obj.getVersion();
					}else{
						obj.setVersion("");
						obj.setAuthor("");
						obj.setVerdate(null); 
					}
				}
			}
			
			// 更新至Table
			TableUtil.addRows(StartUI.getInstance().getVersionDialog().getTable(), result);
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
			Rectangle size = StartUI.getInstance().getTagDialog().getBounds();
			StartUI.getInstance().getTagDialog().setBounds(
					PropReader.getPropertyInt("CVS.TAG_X"),
					PropReader.getPropertyInt("CVS.TAG_Y"),
					(int) size.getWidth(), (int) size.getHeight() );
			StartUI.getInstance().getTagDialog().setTitle(rcsid, ver, filename, 20);
			StartUI.getInstance().getTagDialog().setVisible(true);
			
			// 顯示統計
			if (tList.size() == 0) {
				TagDO nodata = new TagDO();
				nodata.setTagname("NO DATA FOUND");
				tList.add(nodata);
			}
			StartUI.getInstance().getFrame().setMessage(StringUtil.concat("查詢筆數：", tList.size(), ", 耗時：" + CVSFunc.fxElapseTime(s.getTime())));
			
			// 更新至Table
			TableUtil.addRows(StartUI.getInstance().getTagDialog().getTable(), tList);
		}catch(Exception e){
			CVSLog.getLogger().error(this, e);
			StartUI.getInstance().getFrame().setMessage("查詢發生異常：" + e.getMessage());
		}
	}
	
	/** 取得特定版號資料 **/
	public void doRetrieveVerInfo(Long rcsid, String ver) {
		if (StringUtil.anyEmpty(rcsid, ver)) return;

		try {
			// 查詢資料
			Tbsoptcvsver v = dao.retrieveVER(rcsid, ver);
			if (v == null) return;
			Tbsoptcvsmap m = v.getTbsoptcvsmap();
			
			// 初始對話框
			CVSModuleHelper mHelper = new CVSModuleHelper();
			Rectangle size = StartUI.getInstance().getModifyDialog().getBounds();
			StartUI.getInstance().getModifyDialog().setTitle(StringUtil.concat("Author is ", v.getAuthor()) );
			StartUI.getInstance().getModifyDialog().setBounds(
					PropReader.getPropertyInt("CVS.MODIFY_X"),
					PropReader.getPropertyInt("CVS.MODIFY_Y"),
					(int) size.getWidth(), (int) size.getHeight() );
			StartUI.getInstance().getModifyDialog().setCurrentVer(v);
			StartUI.getInstance().getModifyDialog().getFilename_jTxtF().setText(m.getFilename());
			StartUI.getInstance().getModifyDialog().getVersion_jTxtF().setText(ver);
			StartUI.getInstance().getModifyDialog().getWorkdir_jTxtF().setText(mHelper.getPath(CVSFunc.fxModule(m.getModule(), m.getClientserver())));
			StartUI.getInstance().getModifyDialog().getFilepath_jTxtF().setText(CVSFunc.fxExtraSource(m.getRcsfile()));
			StartUI.getInstance().getModifyDialog().getOldComment_jTxtF().setText(v.getFulldesc());
			StartUI.getInstance().getModifyDialog().getNewComment_jTxtF().setText("");	// clear
			StartUI.getInstance().getModifyDialog().setVisible(true);
		}catch(Exception e){
			CVSLog.getLogger().error(this, e);
			StartUI.getInstance().getFrame().setMessage("查詢發生異常：" + e.getMessage());
		}
	}
	
	/** 註冊 POPUP MENU **/
	public  void registerPopupMenu(final SSSJTable table)
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
				doRetrieveTagInfo(rcsid, ver, filename);
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
				doQueryVerTree(rcsid, filename);
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
				doRetrieveVerInfo(rcsid, ver);
			} });
		popup.add(item);
		
		Dimension size = popup.getPreferredSize();
		popup.setPreferredSize(new Dimension((int) (size.width * 1.4), size.height));
	}
}
