package com.systex.sop.cvs.ui.tableClass;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;

import com.systex.sop.cvs.ui.customize.SSSPalette;

@SuppressWarnings("serial")
public class CVSColumnModel extends DefaultTableColumnModel {
	private CVSBaseDO base;
	private DefaultTableCellRenderer cellRenderer;
	
	{
		cellRenderer = new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				if (row % 2 == 0) {
					setBackground(SSSPalette.TBL_ROW_EVEN_BG);
				}else{
					setBackground(SSSPalette.TBL_ROW_ODD_BG);
				}
				
				return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			}
		};
	}
	
	public CVSColumnModel(CVSBaseDO base) {
		this.base = base;
	}
	
	public void addColumn(TableColumn aColumn) {
		super.addColumn(aColumn);
		
		// 設定各欄寬度 (by baseDO)
		aColumn.setPreferredWidth(base.getColumnWidth()[getColumnCount() - 1]);
		
		// 設定渲染器 (斑馬線)
		aColumn.setCellRenderer(cellRenderer);
	}
	
}
