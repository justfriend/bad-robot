package com.systex.sop.cvs.ui.logic;

import java.util.List;

import com.systex.sop.cvs.dao.CVSQueryDAO;
import com.systex.sop.cvs.helper.CVSLog;
import com.systex.sop.cvs.ui.QueryClassicPage;
import com.systex.sop.cvs.ui.StartUI;
import com.systex.sop.cvs.ui.tableClass.CVSTableModel;
import com.systex.sop.cvs.ui.tableClass.NewVerNoTagDO;

public class QueryClassicPageLogic {
	private CVSQueryDAO dao = new CVSQueryDAO();
	private QueryClassicPage page;
	
	public QueryClassicPageLogic(QueryClassicPage page) {
		this.page = page;
	}
	
	/** 查詢最新版本未上TAG **/
	public void doQueryNewVerNoTag(String author, boolean isIgnoreDel) {
		final List<NewVerNoTagDO> objList = dao.queryNewVerNoTag(author, isIgnoreDel);
		
		try {
			// 更新至Table
			page.getTable().setModel(new CVSTableModel(page.getTable(), objList));
		} catch (Exception e) {
			CVSLog.getLogger().error(this, e);
			StartUI.getInstance().getFrame().setMessage("查詢發生異常：" + e.getMessage());
		}
	}
}
