package com.systex.sop.cvs.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import com.systex.sop.cvs.ui.customize.comp.SSSImgJPanel;
import com.systex.sop.cvs.ui.customize.comp.SSSJButton;
import com.systex.sop.cvs.ui.customize.comp.SSSJLabel;
import com.systex.sop.cvs.ui.logic.ResetPageLogic;
import java.awt.Font;

@SuppressWarnings("serial")
public class ResetPage extends JPanel {
	// 控制變數
	
	// 幫助類別
	private ResetPageLogic logic = new ResetPageLogic(this);
	
	// 元件項目
	private SSSImgJPanel imgJPanel;
	private SSSJLabel loginMsg_jL;
	
	/** Constructor **/
	public ResetPage() {
		initial();
		initUI();
	}

	private void initUI() {
		
	}

	private void initial() {
		setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(null);
		add(scrollPane, BorderLayout.CENTER);
		
		SSSImgJPanel panel = new SSSImgJPanel();
		scrollPane.setViewportView(panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(105, 101, 93));
		panel.add(panel_1, BorderLayout.NORTH);
		panel_1.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("89px:grow"),},
			new RowSpec[] {
				RowSpec.decode("40px"),}));
		
		SSSJButton reset_jBtn = new SSSJButton(SSSJButton.ITEM_DARK);

		// XXX 重置登入資訊
		reset_jBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logic.doResetLogin();
			}
		});
		reset_jBtn.setBackground(new Color(105, 101, 93));
		reset_jBtn.setText("確定重置");
		panel_1.add(reset_jBtn, "2, 1, default, center");
		
		SSSJButton refreshLogin_jbtn = new SSSJButton(SSSJButton.ITEM_DARK);
		
		// XXX 更新登入資訊
		refreshLogin_jbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logic.doRefreshLoginInfo();
			}
		});
		refreshLogin_jbtn.setBackground(new Color(105, 101, 93));
		refreshLogin_jbtn.setText("更新資訊");
		panel_1.add(refreshLogin_jbtn, "4, 1");
		
		imgJPanel = new SSSImgJPanel();
		panel.add(imgJPanel, BorderLayout.CENTER);
		imgJPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("center:80dlu"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("center:default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("center:80dlu"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		loginMsg_jL = new SSSJLabel();
		loginMsg_jL.setFont(new Font(loginMsg_jL.getFont().getFamily(), Font.BOLD, 16));
		loginMsg_jL.setText("※請點擊「更新資訊」查看登入資訊。");
		loginMsg_jL.setForeground(Color.WHITE);
		imgJPanel.add(loginMsg_jL, "4, 2, center, default");
		
		SSSJLabel label = new SSSJLabel();
		label.setFont(new Font(label.getFont().getFamily(), Font.BOLD, 14));
		label.setForeground(Color.WHITE);
		label.setText("※請小心進行重置，需確保同步作業非處理中，若系統停止請重新啟動。");
		imgJPanel.add(label, "4, 4, 3, 1, left, default");
		
		SSSJLabel label_2 = new SSSJLabel();
		label_2.setForeground(Color.WHITE);
		label_2.setFont(new Font(label_2.getFont().getFamily(), Font.BOLD, 14));
		label_2.setText("※執行「確定重置」將刪除登入資訊。");
		imgJPanel.add(label_2, "4, 6, 3, 1");
		
		JLabel lblNewLabel_1 = new JLabel("New label");
		lblNewLabel_1.setIcon(new ImageIcon(ResetPage.class.getResource("/resource/reset.gif")));
		imgJPanel.add(lblNewLabel_1, "6, 8");
	}

	public SSSImgJPanel getImgJPanel() {
		return imgJPanel;
	}

	public SSSJLabel getLoginMsg_jL() {
		return loginMsg_jL;
	}
}
