package com.systex.sop.cvs.ui.customize.comp;

import java.awt.Color;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;

@SuppressWarnings("serial")
public class SSSJComboBox extends JComboBox {

	public SSSJComboBox() {
		super();
	}

	public SSSJComboBox(ComboBoxModel aModel) {
		super(aModel);
	}

	public SSSJComboBox(Object[] items) {
		super(items);
	}

	public SSSJComboBox(Vector<?> items) {
		super(items);
	}
	
	{
		setBorder(new LineBorder(Color.GRAY));
		setBackground(Color.WHITE);
		setUI(new BasicComboBoxUI());
	}
}
