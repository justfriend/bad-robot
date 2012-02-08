package com.systex.sop.cvs.ui.customize.comp;

import java.util.Hashtable;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;

import com.systex.sop.cvs.ui.customize.ui.SSSTreeUI;

@SuppressWarnings("serial")
public class SSSJTree extends JTree {
	
	{
		this.setUI(new SSSTreeUI(this));
		this.setBorder(new EmptyBorder(6, 0, 6, 0));
		DefaultTreeCellRenderer r = (DefaultTreeCellRenderer) this.getCellRenderer();
		r.setOpenIcon(new ImageIcon(SSSTreeUI.class.getResource("/resource/treeOpen.png")));
		r.setClosedIcon(new ImageIcon(SSSTreeUI.class.getResource("/resource/treeClose.png")));
		r.setLeafIcon(new ImageIcon(SSSTreeUI.class.getResource("/resource/empty.png")));
	}
	
	public SSSJTree() {
		super();
	}
	
	public SSSJTree(Hashtable<?,?> value) {
		super(value);
	}
	
	public SSSJTree(DefaultMutableTreeNode node) {
		super(node);
	}
	
	public SSSJTree(DefaultTreeModel model) {
		super(model);
	}
}
