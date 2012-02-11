package com.systex.sop.cvs.ui.customize.comp;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import com.systex.sop.cvs.ui.customize.SSSPalette;
import com.systex.sop.cvs.ui.tableClass.CVSBaseDO;
import com.systex.sop.cvs.ui.tableClass.CVSColumnModel;
import com.systex.sop.cvs.ui.tableClass.CVSTableModel;

@SuppressWarnings( { "serial", "unchecked" })
public class SSSJTable extends JTable implements MouseListener {
	private JPopupMenu popup = new JPopupMenu();

//	public SSSJTable() {
//		super();
//	}
	
	public SSSJTable(CVSBaseDO baseDO) {
		setColumnModel(new CVSColumnModel(baseDO));
		
		List<CVSBaseDO> tList = new ArrayList<CVSBaseDO>();
		tList.add(baseDO);
		setModel(new CVSTableModel(this, tList));
	}

	public SSSJTable(int numRows, int numColumns) {
		super(numRows, numColumns);
	}

	public SSSJTable(Object[][] rowData, Object[] columnNames) {
		super(rowData, columnNames);
	}

	public SSSJTable(TableModel dm, TableColumnModel cm, ListSelectionModel sm) {
		super(dm, cm, sm);
	}

	public SSSJTable(TableModel dm, TableColumnModel cm) {
		super(dm, cm);
	}

	public SSSJTable(TableModel dm) {
		super(dm);
	}

	public SSSJTable(Vector rowData, Vector columnNames) {
		super(rowData, columnNames);
	}

	{
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		getTableHeader().setBackground(SSSPalette.FRAME_BG);
		getTableHeader().setReorderingAllowed(false);
		
		/** POPUP MENU **/
		popup.addPopupMenuListener(new PopupMenuListener() {

			@Override
			public void popupMenuCanceled(PopupMenuEvent e) {
			}

			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
			}

			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
			}
			
		});
		addMouseListener(this);
	}

	public Object getValueAt(int row, String columnName) {
		int column = -1;
		for (int i = 0; i < getModel().getColumnCount(); i++) {
			if (getModel().getColumnName(i).equals(columnName)) {
				column = i;
				break;
			}
		}

		return getValueAt(row, column);
	}
	
	public Object getSelectValueAt(String columnName) {
		return getValueAt(getSelectedRow(), columnName);
	}

	public JPopupMenu getPopup() {
		return popup;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.isPopupTrigger()) {
			popup.show(e.getComponent(), e.getX(), e.getY());
		}
	}
}
