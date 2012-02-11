package com.systex.sop.cvs.util;

import java.util.List;

import javax.swing.JTable;
import javax.swing.SwingUtilities;

import com.systex.sop.cvs.ui.tableClass.CVSBaseDO;
import com.systex.sop.cvs.ui.tableClass.CVSTableModel;

public class TableUtil {
	
	public static void addRows(final JTable table, final List<? extends CVSBaseDO> tList) {
		if (!SwingUtilities.isEventDispatchThread()) {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					@Override
					public void run() {
						table.setModel(new CVSTableModel(table, tList));
					}
				});
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}else{
			table.setModel(new CVSTableModel(table, tList));
		}
	}
	
}
