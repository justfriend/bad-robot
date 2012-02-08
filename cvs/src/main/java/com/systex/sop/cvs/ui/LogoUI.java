package com.systex.sop.cvs.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.LineBorder;

import com.sun.awt.AWTUtilities;
import com.systex.sop.cvs.util.ScreenSize;

/**
 * CVS Project 歡迎畫面
 * <p>
 * 系統啟動時將 LOGO 秀出來並進行淡入及淡出。
 * 
 */
@SuppressWarnings("serial")
public class LogoUI extends JFrame {
	private Dimension size = new Dimension(481, 239);
	private JPanel contentPane;
	private static LogoUI me;
	private Timer ti;
	private int i = 0;
	private int j = 0;
	private int k = 0;
	private int m = 9;
	float transparent[] = { 0.1f, 0.2f, 0.3f, 0.4f, 0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1.0f };

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) throws Exception {
		EventQueue.invokeAndWait(new Runnable() {
			public void run() {
				try {
					me = new LogoUI();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private void initial() {
		contentPane = new JPanel();
		contentPane.setBorder(new LineBorder(new Color(0, 0, 0)));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(LogoUI.class.getResource("/resource/sopaCP.gif")));
		contentPane.add(lblNewLabel, BorderLayout.CENTER);
	}

	/**
	 * Create the frame.
	 */
	public LogoUI() {
		initial();
		
		// 取消醜醜的外框及系統按鈕
		this.setUndecorated(true);
		
		// 框架定位
		setBounds(ScreenSize.getAdjustScreenWidth(size.width), ScreenSize.getAdjustScreenHeight(size.height),
				size.width, size.height);
		
		// 定時淡入及淡出
		ti = new Timer(60, new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				j = j + 10;
				i = i + 10;
				if (k < 10) {
					AWTUtilities.setWindowOpacity(LogoUI.this, transparent[k++]);
					if (k == 1) me.setVisible(true);
				} else {
					ti.stop();
					try {
						Thread.sleep(3000);
					} catch (InterruptedException ex) {
						ex.printStackTrace();
					}
					for (int i = 0; i < 8; i++) {
						AWTUtilities.setWindowOpacity(LogoUI.this, transparent[m]);
						m--;
						try {
							Thread.sleep(100);
						} catch (InterruptedException ex) {
							ex.printStackTrace();
						}
					}
					LogoUI.this.dispose();
				}
			}
		});
		ti.start();
	}

}
