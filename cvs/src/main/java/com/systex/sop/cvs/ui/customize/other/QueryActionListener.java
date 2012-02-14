package com.systex.sop.cvs.ui.customize.other;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import com.systex.sop.cvs.ui.StartUI;

public abstract class QueryActionListener implements ActionListener {
	private int percent = -1;		// 執行百分筆
	private JButton btn = null;
	
	public QueryActionListener(JButton btn) {
		this.btn = btn;
	}
	
	public abstract void actPerformed(ActionEvent event);

	@Override
	public void actionPerformed(final ActionEvent event) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					StartUI.getInstance().getFrame().beginProcess();
					btn.setEnabled(false);
					actPerformed(event);
				}catch(RuntimeException e) {
					throw e;
				}finally{
					StartUI.getInstance().getFrame().endProcess();
					btn.setEnabled(true);
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
