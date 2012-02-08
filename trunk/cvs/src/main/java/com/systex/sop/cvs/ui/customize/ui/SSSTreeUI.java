package com.systex.sop.cvs.ui.customize.ui;

import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.CellRendererPane;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.TreePath;

import com.systex.sop.cvs.ui.customize.SSSPalette;
import com.systex.sop.cvs.ui.customize.other.SSSCellRendererPane;

public class SSSTreeUI extends BasicTreeUI {
	private JTree tree;
	
	public SSSTreeUI(JTree tree) {
		this.tree = tree;
		Font font = new Font(SSSPalette.fontFamily, Font.BOLD, 12);
		tree.setFont(font);
	}
	
	@Override
	protected CellRendererPane createCellRendererPane() {
        return new SSSCellRendererPane();
    }

	@Override
	protected void paintExpandControl(Graphics g, Rectangle clipBounds, Insets insets, Rectangle bounds, TreePath path,
			int row, boolean isExpanded, boolean hasBeenExpanded, boolean isLeaf) {
		// do not draw
	}

	@Override
	protected void paintVerticalLine(Graphics g, JComponent c, int x, int top, int bottom) {
		// do not draw
	}

	@Override
	protected void paintHorizontalLine(Graphics g, JComponent c, int y, int left, int right) {
		// do not draw
	}

	@Override
	protected void paintRow(Graphics g, Rectangle clipBounds, Insets insets, Rectangle bounds, TreePath path, int row,
			boolean isExpanded, boolean hasBeenExpanded, boolean isLeaf) {
		
		// Don't paint the renderer if editing this row.
		if (editingComponent != null && editingRow == row)
			return;

		int leadIndex;

		if (tree.hasFocus()) {
			// leadIndex = getLeadSelectionRow();
			leadIndex = getRowForPath(tree, tree.getLeadSelectionPath());
		} else
			leadIndex = -1;

		Component component;

		component = currentCellRenderer.getTreeCellRendererComponent(tree, path.getLastPathComponent(), tree
				.isRowSelected(row), isExpanded, isLeaf, row, (leadIndex == row));

		rendererPane.paintComponent(g, component, tree, bounds.x, bounds.y, bounds.width, bounds.height, true);
	}

}
