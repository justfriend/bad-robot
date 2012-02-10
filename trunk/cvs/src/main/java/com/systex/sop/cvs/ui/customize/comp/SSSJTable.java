package com.systex.sop.cvs.ui.customize.comp;

import javax.swing.JTable;

import com.systex.sop.cvs.ui.tableClass.CVSBaseDO;

@SuppressWarnings("serial")
public class SSSJTable extends JTable {

    public SSSJTable() {
        super();
    }
    
    {
    	setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    }
    
	public static void columnRegular(JTable table, CVSBaseDO baseDO) {
		for (int i=0; i<table.getColumnModel().getColumnCount(); i++) {
			table.getColumnModel().getColumn(i).setPreferredWidth(baseDO.getColumnWidth()[i]);
		}
	}
}
