package com.systex.sop.cvs.ui.logic;

import com.systex.sop.cvs.dao.CVSLoginDAO;
import com.systex.sop.cvs.dto.Tbsoptcvslogin;
import com.systex.sop.cvs.helper.CVSLog;
import com.systex.sop.cvs.ui.ResetPage;
import com.systex.sop.cvs.util.HostnameUtil;
import com.systex.sop.cvs.util.StringUtil;

public class ResetPageLogic {
	private CVSLoginDAO dao = new CVSLoginDAO();
	private ResetPage page;
	
	public ResetPageLogic(ResetPage page) {
		this.page = page;
	}
	
	/** 重置登入資訊 **/
	public void doResetLogin() {
		try {
			Tbsoptcvslogin login = dao.doResetLogin(HostnameUtil.getHostname());
			if (login == null) {
				page.getLoginMsg_jL().setText("重置完成");
			}else{
				page.getLoginMsg_jL().setText("無須進行重置");
			}
		}catch(Exception e){
			CVSLog.getLogger().error(this, e);
			page.getLoginMsg_jL().setText("重置發生異常:" + e.getMessage());
		}
	}
	
	/** 更新登入資訊 **/
	public void doRefreshLoginInfo() {
		Tbsoptcvslogin login = dao.retrieveLogin();
		if (login == null) {
			page.getLoginMsg_jL().setText("系統無登入資訊");
		}else{
			String msg = StringUtil.concat("使用者[", login.getCreator(), "], 狀態[", login.getStatus(), "], 登入時間[", login.getCreatetime(), "]");
			page.getLoginMsg_jL().setText(msg);
		}
	}
}
