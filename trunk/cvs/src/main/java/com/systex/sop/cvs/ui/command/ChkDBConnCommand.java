package com.systex.sop.cvs.ui.command;

import org.hibernate.Criteria;
import org.hibernate.Session;

import com.systex.sop.cvs.constant.CVSConst.CMD_RESULT;
import com.systex.sop.cvs.dto.Tbsoptcvslogin;
import com.systex.sop.cvs.helper.CVSLog;
import com.systex.sop.cvs.ui.customize.comp.SSSTrafficLabel;
import com.systex.sop.cvs.util.SessionUtil;

public class ChkDBConnCommand extends BaseCommand {

	public ChkDBConnCommand(SSSTrafficLabel c) {
		super(c);
	}

	@Override
	public CMD_RESULT execute() {
		Session session = null;

		try {
			session = SessionUtil.openSession(); // 此階段若有失敗不會收到 EXCEPTION(未被扔出)，必須真的做查詢才會發現
			Criteria cri = session.createCriteria(Tbsoptcvslogin.class);
			cri.list();
			return CMD_RESULT.SUCCESS;
		} catch (Exception e) {
			CVSLog.getLogger().error(this, e);
			return CMD_RESULT.FAILURE;
		} finally {
			SessionUtil.closeSession(session);
		}
	}

}
