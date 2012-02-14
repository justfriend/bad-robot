package com.systex.sop.cvs.ui.customize.other;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;

import com.systex.sop.cvs.ui.StartUI;

public abstract class QueryActionListener implements ActionListener {
	private int percent = -1;		// 執行百分筆
	private JComponent comp = null;
	
	public QueryActionListener(JComponent btn) {
		this.comp = btn;
	}
	
	public abstract void actPerformed(ActionEvent event);

	@Override
	public void actionPerformed(final ActionEvent event) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					StartUI.getInstance().getFrame().beginProcess();
					if (comp instanceof JButton) ((JButton) comp).setEnabled(false);
					actPerformed(event);
				}catch(RuntimeException e) {
					throw e;
				}finally{
					StartUI.getInstance().getFrame().endProcess();
					if (comp instanceof JButton) ((JButton) comp).setEnabled(true);
				}
			}
		}).start();
	}
	
	public void setProgress(int percent) {
		this.percent = percent;
	}
	
	public int getProgress() {
		return percent;
	}
}
