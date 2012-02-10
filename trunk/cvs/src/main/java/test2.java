import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.systex.sop.cvs.dao.CVSQueryDAO;
import com.systex.sop.cvs.ui.customize.comp.SSSJTextField;
import com.systex.sop.cvs.ui.tableClass.NewVerNoTagDO;



public class test2 {

	public static void main(String args[]) throws Exception {
		CVSQueryDAO dao = new CVSQueryDAO();
		List<NewVerNoTagDO> objList = dao.queryNewVerNoTag("", false);
		for (NewVerNoTagDO obj : objList) {
//			System.err.println (obj.getDesc_id());
		}
	}

}
