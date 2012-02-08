package com.systex.sop.cvs.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Hashtable;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import com.systex.sop.cvs.helper.CVSLog;
import com.systex.sop.cvs.ui.Workspace.PAGE;
import com.systex.sop.cvs.ui.customize.comp.SSSFrame;
import com.systex.sop.cvs.ui.customize.comp.SSSJButton;
import com.systex.sop.cvs.ui.customize.comp.SSSJSplitPane;
import com.systex.sop.cvs.util.PropReader;
import com.systex.sop.cvs.util.ScreenSize;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * CVS 主程式
 * <P>
 * 1. CVS 程式之進入點 <BR>
 * 2. 僅含畫面UI及元件事件 <BR>
 * 3. 事件之處理委派由--Logic來處理<BR>
 * 4. 此為主畫面包含框架及選單 <BR>
 *
 */
public class StartUI {
	private static StartUI instance;
	
	private final float frameWidthRate = 0.8f;	// 框架起始的大小比率 (寬)
	private final float frameHeightRate = 0.8f;	// 框架起始的大小比率 (高)
	private SSSFrame frame;
	private SSSJButton qryNormal_jBtn;			// 一般查詢按鈕
	private SSSJButton sync_jBtn;				// 同步執行按鈕
	private SSSJButton sysInfo_jBtn;			// 系統說明按鈕
	private SSSJButton qryClassic_jBtn;			// 典型查詢按鈕
	private SSSJButton reset_jBtn;				// 登入重制按鈕
	private SSSJButton envChk_jBtn;				// 環境檢查按鈕
	private SSSJSplitPane contentSplitPane;		// 主要之分割窗(*)(點選左方按鈕時利用此來進行套用不同的頁面)
	private JPanel body_jPanel;
	ServerSocket socket;

	public static StartUI getInstance() {
		if (instance == null) {
			instance = new StartUI();
		}
		return instance;
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) throws Exception {
		try {
			StartUI window = StartUI.getInstance();
			
			if ("true".equalsIgnoreCase(PropReader.getProperty("CVS.WELCOMELOGO"))) {
				// 呈現歡迎畫面 (淡入後即關閉)
				window.welcomeLogo();
				
				// 必須的等待 (等待歡迎畫面之時間，再秀出主畫面)
				Thread.sleep(5000);
			}
			
			// 檢查是否重覆啟動
			window.checkSingleApp();
			
			// 指定主容器 (用來切換頁面)
			Workspace.registerBody(window.getBody_jPanel());
			
			// 定位主畫面
			window.frame.setBounds(100, 100,
					ScreenSize.getDynamicScreenWidth(window.frameWidthRate),
					ScreenSize.getDynamicScreenHeight(window.frameHeightRate) );
			
			// 呈現主畫面
			window.frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void checkSingleApp() {
		try {
			socket = new ServerSocket(PropReader.getPropertyInt("CVS.PORT"));
		} catch (IOException e) {
			CVSLog.getLogger().warn("系統重覆執行");
			System.exit(0);
		}
	}

	private StartUI() {
		initialize();
	}
	
	public SSSJSplitPane getContentSplitPane() {
		return contentSplitPane;
	}

	public SSSJButton getEnvChk_jBtn() {
		return envChk_jBtn;
	}

	public SSSFrame getFrame() {
		return frame;
	}

	public SSSJButton getQryClassic_jBtn() {
		return qryClassic_jBtn;
	}

	public SSSJButton getQryNormal_jBtn() {
		return qryNormal_jBtn;
	}

	public SSSJButton getReset_jBtn() {
		return reset_jBtn;
	}

	public SSSJButton getSync_jBtn() {
		return sync_jBtn;
	}

	public SSSJButton getSysInfo_jBtn() {
		return sysInfo_jBtn;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings("unchecked")
	private void initialize() {
		Icon titleIcon = new ImageIcon(getClass().getResource("/resource/symbol.png"));
		Image frameIcon = null;
		try {
			frameIcon = ImageIO.read(getClass().getResource("/resource/symbolLarge.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		frame = new SSSFrame(frameIcon, titleIcon, PropReader.getProperty("CVS.TITLE"), true, true, true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		UIManager.put("SplitPane.dividerSize", 3);
		contentSplitPane = new SSSJSplitPane();
		contentSplitPane.setDividerLocation(150);
		contentSplitPane.setOneTouchExpandable(false);
		frame.getPanel().add(contentSplitPane, BorderLayout.CENTER);

		JScrollPane menu_jScrP = new JScrollPane();
		menu_jScrP.setBorder(null);
		contentSplitPane.setLeftComponent(menu_jScrP);

		JPanel menuPanel = new JPanel();
		menuPanel.setBackground(Color.WHITE);
		menu_jScrP.setViewportView(menuPanel);
		menuPanel.setLayout(new FormLayout(new ColumnSpec[] { ColumnSpec.decode("default:grow"), }, new RowSpec[] {
				RowSpec.decode("25dlu"), FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("25dlu"), FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("25dlu"), FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("25dlu"), FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC, }));

		SSSJButton button = new SSSJButton(SSSJButton.SUBJECT);
		button.setText("伺服端功能");
		menuPanel.add(button, "1, 1, default, fill");

		sync_jBtn = new SSSJButton(SSSJButton.TITLE);
		if (!"true".equalsIgnoreCase(PropReader.getProperty("CVS.SERVERMODE"))) {
			sync_jBtn.setEnabled(false);
		}
		sync_jBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Workspace.changePage(PAGE.SYNC_CVS);
			}
		});
		sync_jBtn.setIcon(new ImageIcon(StartUI.class.getResource("/resource/syncIcon.gif")));
		sync_jBtn.setText("同步功能");
		menuPanel.add(sync_jBtn, "1, 3");

		reset_jBtn = new SSSJButton(SSSJButton.TITLE);
		if (!"true".equalsIgnoreCase(PropReader.getProperty("CVS.SERVERMODE"))) {
			reset_jBtn.setEnabled(false);
		}
		reset_jBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Workspace.changePage(PAGE.RESET_LOGIN);
			}
		});
		reset_jBtn.setIcon(new ImageIcon(StartUI.class.getResource("/resource/doorIcon.gif")));
		reset_jBtn.setText("登入重制");
		menuPanel.add(reset_jBtn, "1, 5");

		SSSJButton button_2 = new SSSJButton(SSSJButton.SUBJECT);
		button_2.setText("查詢功能");
		menuPanel.add(button_2, "1, 7, default, fill");

		qryClassic_jBtn = new SSSJButton(SSSJButton.TITLE);
		qryClassic_jBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Workspace.changePage(PAGE.QRY_CLASSIC);
			}
		});
		qryClassic_jBtn.setIcon(new ImageIcon(StartUI.class.getResource("/resource/qryClassicIcon.gif")));
		qryClassic_jBtn.setText("典型查詢");
		menuPanel.add(qryClassic_jBtn, "1, 9");

		qryNormal_jBtn = new SSSJButton(SSSJButton.TITLE);
		qryNormal_jBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Workspace.changePage(PAGE.QRY_NORMAL);
			}
		});
		qryNormal_jBtn.setIcon(new ImageIcon(StartUI.class.getResource("/resource/qryNormalIcon.gif")));
		qryNormal_jBtn.setText("一般查詢");
		menuPanel.add(qryNormal_jBtn, "1, 11");

		SSSJButton button_5 = new SSSJButton(SSSJButton.SUBJECT);
		button_5.setText("環境");
		menuPanel.add(button_5, "1, 13, default, fill");

		envChk_jBtn = new SSSJButton(SSSJButton.TITLE);
		envChk_jBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Workspace.changePage(PAGE.ENV_CHK);
			}
		});
		envChk_jBtn.setIcon(new ImageIcon(StartUI.class.getResource("/resource/envChkIcon.gif")));
		envChk_jBtn.setText("環境檢查");
		menuPanel.add(envChk_jBtn, "1, 15");

		SSSJButton button_7 = new SSSJButton(SSSJButton.SUBJECT);
		button_7.setText("說明");
		menuPanel.add(button_7, "1, 17, default, fill");

		sysInfo_jBtn = new SSSJButton(SSSJButton.TITLE);
		sysInfo_jBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Workspace.changePage(PAGE.SYS_INFO);
			}
		});
		sysInfo_jBtn.setIcon(new ImageIcon(StartUI.class.getResource("/resource/sysinfoIcon.gif")));
		sysInfo_jBtn.setText("系統說明");
		menuPanel.add(sysInfo_jBtn, "1, 19");
		
		body_jPanel = new JPanel();
		contentSplitPane.setRightComponent(body_jPanel);

		Hashtable srvTitle1 = new Hashtable();
		srvTitle1.put("同步", "");

		Hashtable qryTitle1 = new Hashtable();
		qryTitle1.put("一般查詢", "");
		qryTitle1.put("典型查詢", new String[] { "查詢TAG未正確", "查詢TAG版號未在最新版" });
	}

	private void welcomeLogo() {
		try {
			LogoUI.main(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public JPanel getBody_jPanel() {
		return body_jPanel;
	}
}
