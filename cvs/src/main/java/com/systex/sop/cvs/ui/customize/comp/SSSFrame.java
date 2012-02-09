package com.systex.sop.cvs.ui.customize.comp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.ibm.iwt.IFrame;
import com.ibm.iwt.layout.GroupFlowLayoutConstraints;
import com.ibm.iwt.util.IWTUtilities;
import com.ibm.iwt.window.IWindowTitleBar;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import com.systex.sop.cvs.ui.customize.SSSPalette;
import com.systex.sop.cvs.util.PropReader;
import com.systex.sop.cvs.util.StringUtil;

@SuppressWarnings({ "unchecked", "serial" })
public class SSSFrame extends IFrame {
	private SSSJLabel msgjL = new SSSJLabel();	// message label (框架最下方左下角的訊息提示)
	private SSSJLabel cxtjL = new SSSJLabel();	// content warning message (畫面中間的明顯警示-字大)
	private Timer cxtTimer = null;
	private JPanel panel;						// container panel
	private TitleBar titleBar;
	
	{
		cxtjL.setFont(new Font(SSSPalette.fontFamily, Font.BOLD, PropReader.getPropertyInt("CVS.WARNINGSIZE")));
		cxtjL.setOpaque(false);
		cxtjL.setForeground(Color.PINK);
		cxtTimer = new Timer(1500, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setCxtMessage(null);
			}
		});
	}
	
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
					SSSFrame frame = new SSSFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public SSSFrame() {
		this(null, null, "", true, true, true);
	}

	/**
	 * Create the frame.
	 */
	public SSSFrame(Image frameIcon, Icon titleIcon, String title, boolean minButton, boolean maxButton, boolean closeButton) {
		/** TITLE BAR 及最外圍邊框 **/
		IWTUtilities.setApplicationBorderSize(this, new Insets(1, 1, 1, 1));
		titleBar = new TitleBar(frameIcon, titleIcon, title, minButton, maxButton, closeButton);
		setTitleBar(titleBar);
		setTitleBarBorder(new MatteBorder(1, 1, 0, 1, SSSPalette.FRAME_BD));
		setTitleBarBackground(SSSPalette.FRAME_BG);
		setIContentPaneBorder(new MatteBorder(0, 1, 1, 1, SSSPalette.FRAME_BD));
		
		setBounds(100, 100, 800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		/** 下方美化 (訊息狀態 + 拖拉圖示) **/
		JPanel sPanel = new JPanel();
		sPanel.setBorder(null);
		sPanel.setSize(-1, 100);
		sPanel.setBackground(SSSPalette.FRAME_BG);
		iContentPane.add(sPanel, BorderLayout.SOUTH);
		sPanel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("20px"),
				ColumnSpec.decode("177px:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				ColumnSpec.decode("3px"),},
			new RowSpec[] {
				RowSpec.decode("14dlu"),
				RowSpec.decode("2px"),}));
		
		sPanel.add(msgjL, "2, 1, default, center");
		
		JButton drawBtn = new JButton("");
		drawBtn.setVerticalAlignment(SwingConstants.BOTTOM);
		drawBtn.setBorder(null);
		drawBtn.setBackground(SSSPalette.FRAME_BG);
		drawBtn.setIcon(new ImageIcon(SSSFrame.class.getResource("/resource/drawIcon.png")));
		drawBtn.setMargin(new Insets(0, 0, 0, 0));
		sPanel.add(drawBtn, "4, 1, center, bottom");
		
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
	}
	
	public void setCxtMessage(String msg) {
		if (StringUtil.isEmpty(msg)) {
			cxtjL.setText("");
		}else{
			cxtTimer.stop();
			
			// 動態重新定位
			Rectangle size = this.getBounds();
			cxtjL.setHorizontalAlignment(SwingConstants.CENTER);
			cxtjL.setVerticalAlignment(SwingConstants.CENTER);
			cxtjL.setBounds( (int) (size.width * 0.2), (int) (size.height * 0.2), (int) (size.width * 0.6), (int) (size.height * 0.6));
			cxtjL.setText(msg);
			
			// 自動消逝
			cxtTimer.restart();
		}
		cxtjL.repaint();
	}

	public TitleBar getFrameTitleBar() {
		return titleBar;
	}
	
}
