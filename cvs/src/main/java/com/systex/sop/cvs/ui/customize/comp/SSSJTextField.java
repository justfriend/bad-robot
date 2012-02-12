package com.systex.sop.cvs.ui.customize.comp;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.text.Document;

import com.systex.sop.cvs.ui.customize.SSSPalette;

@SuppressWarnings("serial")
public class SSSJTextField extends JTextField {

	public SSSJTextField() {
		super();
	}

	public SSSJTextField(Document doc, String text, int columns) {
		super(doc, text, columns);
	}

	public SSSJTextField(int columns) {
		super(columns);
	}

	public SSSJTextField(String text, int columns) {
		super(text, columns);
	}

	public SSSJTextField(String text) {
		super(text);
	}
	
	{
		setPreferredSize(new Dimension(2, 22));
		setBorder(new LineBorder(new Color(171, 173, 179), 1));
		setFont(new Font(SSSPalette.fontFamily, Font.BOLD, 12));
	}
}
