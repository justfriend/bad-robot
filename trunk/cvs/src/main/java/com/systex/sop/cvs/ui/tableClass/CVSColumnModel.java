package com.systex.sop.cvs.ui.tableClass;

import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;

@SuppressWarnings("serial")
public class CVSColumnModel extends DefaultTableColumnModel {
	private CVSBaseDO base;
	
	public CVSColumnModel(CVSBaseDO base) {
		this.base = base;
	}
	
	public void addColumn(TableColumn aColumn) {
		super.addColumn(aColumn);
		aColumn.setPreferredWidth(base.getColumnWidth()[getColumnCount() - 1]);
	}
	
}
