package com.systex.sop.cvs.ui.customize.comp;

import java.awt.Font;

import javax.swing.JTextField;
import javax.swing.text.Document;

import com.systex.sop.cvs.ui.customize.SSSPalette;
import com.systex.sop.cvs.ui.customize.ui.SSSBasicTextFieldUI;

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
		setUI(new SSSBasicTextFieldUI());
		setFont(new Font(SSSPalette.fontFamily, Font.BOLD, 12));
	}
}
