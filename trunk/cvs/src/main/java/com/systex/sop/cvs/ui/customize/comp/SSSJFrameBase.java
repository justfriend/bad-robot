package com.systex.sop.cvs.ui.customize.comp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.hibernate.HibernateException;

import com.ibm.iwt.IFrame;
import com.ibm.iwt.event.WindowChangeEvent;
import com.ibm.iwt.layout.GroupFlowLayoutConstraints;
import com.ibm.iwt.util.IWTUtilities;
import com.ibm.iwt.window.IWindowTitleBar;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import com.systex.sop.cvs.helper.CVSLog;
import com.systex.sop.cvs.ui.customize.SSSPalette;
import com.systex.sop.cvs.util.PropReader;
import com.systex.sop.cvs.util.SessionUtil;
import com.systex.sop.cvs.util.StringUtil;

@SuppressWarnings({ "unchecked", "serial" })
public class SSSJFrameBase extends IFrame {
	private SSSJLabel msgjL = new SSSJLabel();	// 框架最下方左下角的訊息提示
	private SSSJLabel cxtjL = new SSSJLabel();	// 隱藏的訊息區塊 (畫面中間的明顯警示-字大)
	private JPanel panel;						// 主要區塊
	private TitleBar titleBar;					// 標題區
	private JProgressBar pBar_jPBar;			// 進度條
	
	{
		cxtjL.setFont(new Font(SSSPalette.fontFamily, Font.BOLD, PropReader.getPropertyInt("CVS.WARNINGSIZE")));
		cxtjL.setOpaque(false);
		cxtjL.setForeground(Color.PINK);
	}
	
	/** 標題區 : 建立系統圖示、標題圖示、標題及視窗按鈕 **/
	public class TitleBar extends IWindowTitleBar implements ChangeListener {
		private Image frameIcon;
		private JButton titleIcon;
		private JLabel title;
		
		public TitleBar(Image frameIcon, Icon titleIcon, String title, boolean minButton, boolean maxButton, boolean closeButton) {
			setPreferredSize(new Dimension(0, 34));
			setWindowButtonColors(SSSPalette.FRAME_BG, SSSPalette.FRAME_FG_DARK);
			
			/** ADD FRAME ICON **/
			if (frameIcon != null) {
				setIconImage(frameIcon);
				this.frameIcon = frameIcon;
			}
			
			/** ADD TITLE ICON **/
			if (titleIcon != null) {
				JButton iconBtn = new JButton();
				iconBtn.setIcon(titleIcon);
				iconBtn.setBorder(null);
				iconBtn.setBackground(SSSPalette.FRAME_BG);
				add(iconBtn, new GroupFlowLayoutConstraints(SwingConstants.LEFT, new Insets(3, 12, 3, 5)));
				this.titleIcon = iconBtn;
			}
			
			/** ADD TITLE TEXT **/
			JLabel textLabel = new JLabel();
			textLabel.setText( (title == null)? "": title );
			add(textLabel, new GroupFlowLayoutConstraints(SwingConstants.LEFT, new Insets(3, 26, 3, 5)));
			this.title = textLabel;
		}

		@Override
		public void addWindowButton(int buttonType, Color foreground, Color background) {
			SSSIWindowButton iButton = new SSSIWindowButton(buttonType);
			iButton.setForeground(foreground);
			iButton.setBackground(background);
			iButton.addActionListener(this);
			vctWindowButtons.add(iButton);
			add(iButton, new GroupFlowLayoutConstraints(SwingConstants.RIGHT, new Insets(3, 4, 3, 5)));
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
		}

		public void stateChanged(ChangeEvent e) {
			repaint();
		}

		public Image getFrameIcon() {
			return frameIcon;
		}

		public JButton getTitleIcon() {
			return titleIcon;
		}

		public JLabel getTitle() {
			return title;
		}
		
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SSSJFrameBase frame = new SSSJFrameBase();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public SSSJFrameBase() {
		this(null, null, "", true, true, true);
	}

	/**
	 * Create the frame.
	 */
	public SSSJFrameBase(Image frameIcon, Icon titleIcon, String title, boolean minButton, boolean maxButton, boolean closeButton) {
		/** TITLE BAR 及最外圍邊框 **/
		IWTUtilities.setApplicationBorderSize(this, new Insets(1, 1, 1, 1));
		titleBar = new TitleBar(frameIcon, titleIcon, title, minButton, maxButton, closeButton);
		setTitleBar(titleBar);
		setTitleBarBorder(new MatteBorder(1, 1, 0, 1, SSSPalette.FRAME_BD));
		setTitleBarBackground(SSSPalette.FRAME_BG);
		setIContentPaneBorder(new MatteBorder(0, 1, 1, 1, SSSPalette.FRAME_BD));
		
		setBounds(100, 100, 800, 600);
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		/** 下方美化 (訊息狀態 + 拖拉圖示) **/
		JPanel sPanel = new JPanel();
		sPanel.setBorder(null);
		sPanel.setSize(-1, 100);
		sPanel.setBackground(SSSPalette.FRAME_BG);
		iContentPane.add(sPanel, BorderLayout.SOUTH);
		sPanel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("20px"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("100dlu"),
				ColumnSpec.decode("12dlu"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				ColumnSpec.decode("3px"),},
			new RowSpec[] {
				RowSpec.decode("14dlu:grow"),
				RowSpec.decode("2px"),}));
		
		sPanel.add(msgjL, "3, 1, default, center");
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(SSSPalette.FRAME_BG);
		sPanel.add(panel_1, "5, 1, fill, fill");
		panel_1.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.NARROW_LINE_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),}));
		
		pBar_jPBar = new JProgressBar();
		pBar_jPBar.setVisible(false);	// default hide
		panel_1.add(pBar_jPBar, "2, 2, default, center");
		
		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon(SSSJFrameBase.class.getResource("/resource/drawIcon.png")));
		sPanel.add(label, "8, 1, right, bottom");
		
		/** 左邊邊界美化 (上底色) **/
		JPanel wPanel = new JPanel();
		iContentPane.add(wPanel, BorderLayout.WEST);
		wPanel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("6px"),},
			new RowSpec[] {
				RowSpec.decode("23px"),}));
		wPanel.setBorder(null);
		wPanel.setBackground(SSSPalette.FRAME_BG);
		
		/** 右邊邊界美化 (上底色) **/
		JPanel ePanel = new JPanel();
		ePanel.setBorder(null);
		iContentPane.add(ePanel, BorderLayout.EAST);
		ePanel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("6px"),},
			new RowSpec[] {
				FormFactory.LINE_GAP_ROWSPEC,
				RowSpec.decode("23px"),}));
		ePanel.setBackground(SSSPalette.FRAME_BG);
		
		panel = new JPanel();
		iContentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));
		
		/** 添加一層用來於畫面中顯示訊息 (超顯眼) **/
		JLayeredPane layerPane = this.getLayeredPane();
		layerPane.add(cxtjL, 101);	// 最上層顯示
	}

	public JPanel getPanel() {
		return panel;
	}
	
	/** 設定左下角訊息文字 **/
	public void setMessage(String msg) {
		msgjL.setText(msg);
		CVSLog.getLogger().info("[Message]" + msg);
	}
	
	/** 呈現框架訊息 **/
	public void setCxtMessage(String msg) {
		if (StringUtil.isEmpty(msg)) {
			cxtjL.setText("");
		}else{
			// 動態重新定位
			Rectangle size = this.getBounds();
			cxtjL.setHorizontalAlignment(SwingConstants.CENTER);
			cxtjL.setVerticalAlignment(SwingConstants.CENTER);
			cxtjL.setBounds( (int) (size.width * 0.2), (int) (size.height * 0.2), (int) (size.width * 0.6), (int) (size.height * 0.6));
			cxtjL.setText(msg);
			CVSLog.getLogger().info("[CxtMsg]" + msg);
		}
		cxtjL.repaint();
	}

	public TitleBar getFrameTitleBar() {
		return titleBar;
	}
	
	public JProgressBar getpBar() {
		return pBar_jPBar;
	}

	/** 呈現訊息對話框 **/
	public void showMessageBox(String msg) {
		showMessageBox(this, msg);
	}
	
	/** 呈現訊息對話框 (指定父視窗) **/
	public void showMessageBox(Component comp, String msg) {
		CVSLog.getLogger().info("[MessageBox]" + msg);
		JOptionPane.showMessageDialog(comp, msg);
	}
	
	public JProgressBar getPBar_jPBar() {
		return pBar_jPBar;
	}
	
	public void beginProcess() {
		getpBar().setIndeterminate(true);
		getpBar().setVisible(true);
	}
	
	public void endProcess() {
		getpBar().setVisible(false);
	}
	
	@Override
	public void windowClosed(WindowChangeEvent event) {
		try {
			SessionUtil.closeSessionFactory();
		} catch (HibernateException e) {
			CVSLog.getLogger().error(this, e);
		} finally {
			System.exit(0);
		}
	}
}
