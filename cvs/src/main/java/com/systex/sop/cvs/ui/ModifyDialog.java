package com.systex.sop.cvs.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import com.systex.sop.cvs.dao.CVSQueryDAO;
import com.systex.sop.cvs.dto.Tbsoptcvsver;
import com.systex.sop.cvs.helper.CVSLog;
import com.systex.sop.cvs.task.ModifyCallable;
import com.systex.sop.cvs.task.TaskResult;
import com.systex.sop.cvs.task.VerifyLogCallable;
import com.systex.sop.cvs.task.VerifyWriteCallable;
import com.systex.sop.cvs.ui.customize.SSSPalette;
import com.systex.sop.cvs.ui.customize.comp.SSSJButton;
import com.systex.sop.cvs.ui.customize.comp.SSSJDialogBase;
import com.systex.sop.cvs.ui.customize.comp.SSSJLabel;
import com.systex.sop.cvs.ui.customize.comp.SSSJTextField;
import com.systex.sop.cvs.ui.customize.other.QueryActionListener;
import com.systex.sop.cvs.util.PropReader;
import com.systex.sop.cvs.util.StringUtil;

@SuppressWarnings("serial")
public class ModifyDialog extends SSSJDialogBase {
	private Tbsoptcvsver currentVer = null;
	private SSSJTextField filepath_jTxtF;
	private SSSJTextField workdir_jTxtF;
	private JTextArea oldComment_jTxtF;
	private JTextArea newComment_jTxtF;
	private SSSJTextField filename_jTxtF;
	private SSSJTextField version_jTxtF;
	private JTextField module_jTxtF;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			ModifyDialog dialog = new ModifyDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/** 進行驗證 **/
	private void doVerifyComment() {
		String workdir = getWorkdir_jTxtF().getText();
		String filepath = getFilepath_jTxtF().getText();
		String version = getVersion_jTxtF().getText();
		String filename = getFilename_jTxtF().getText();
		String module = getModule_jTxtF().getText();
		
		if (StringUtil.anyEmpty(workdir, filepath, version, module)) {
			StartUI.getInstance().getFrame().showMessageBox(this, "模組名稱、檔案名稱、工作目錄或版號無法取得...無法進行更新");
			return;
		}
		
		// 取得LOG
		VerifyLogCallable vLog = null;
		try {
			vLog = new VerifyLogCallable(workdir, filepath, version , filename);
			vLog.call();
			CVSLog.getLogger().info("取得LOG成功");
		}catch(Exception ex){
			CVSLog.getLogger().error(this, ex);
			String msg = "取得LOG失敗:" + ex.getMessage();
			StartUI.getInstance().getFrame().setMessage(msg);
			StartUI.getInstance().getFrame().showMessageBox(this, msg);
			return;
		}
		
		// 寫入LOG
		VerifyWriteCallable vWrite = null;
		try {
			vWrite = new VerifyWriteCallable(version, filename, module);
			vWrite.call();
			CVSLog.getLogger().info("寫入LOG成功");
		}catch(Exception ex){
			CVSLog.getLogger().error(this, ex);
			String msg = "寫入LOG失敗:" + ex.getMessage();
			StartUI.getInstance().getFrame().setMessage(msg);
			StartUI.getInstance().getFrame().showMessageBox(this, msg);
			return;
		}
		
		// 重新查詢
		CVSQueryDAO dao = new CVSQueryDAO();
		Tbsoptcvsver ver = null;
		try {
			ver = dao.retrieveVER(currentVer.getId().getMSid(), version);
			String oldStr = getOldComment_jTxtF().getText();
			String newStr = ver.getFulldesc();
			oldStr = (oldStr == null)? "": oldStr;
			newStr = (newStr == null)? "": newStr;
			getNewComment_jTxtF().setText(newStr);
			if (oldStr.trim().compareTo(newStr.trim()) == 0) {
				StartUI.getInstance().getFrame().showMessageBox(this, "資料一致");
			}else{
				StartUI.getInstance().getFrame().showMessageBox(this, "資料不一致");
			}
		}catch(Exception e){
			CVSLog.getLogger().error(this, e);
			StartUI.getInstance().getFrame().showMessageBox(this, "查詢版本資料失敗:" + e.getMessage());
			return;
		}
	}
	
	/** 進行修改提交註記 **/
	private void doUpdateComment() {
		String workdir = getWorkdir_jTxtF().getText();
		String filepath = getFilepath_jTxtF().getText();
		String version = getVersion_jTxtF().getText();
		String comment = getOldComment_jTxtF().getText();
		
		if (StringUtil.anyEmpty(workdir, filepath, version)) {
			StartUI.getInstance().getFrame().showMessageBox(this, "檔案名稱、工作目錄或版號無法取得...無法進行更新");
			return;
		}
		
		ModifyCallable m = null;
		TaskResult result = null;
		try {
			m = new ModifyCallable(currentVer.getId().getMSid(), workdir, filepath, version, comment);
			result = m.call();
			if (result.getIsDone() == null) {
				StartUI.getInstance().getFrame().showMessageBox(this, "更新成功但回寫資料庫失敗..請再執行一次 (資料不同步)");
			}else if (result.getIsDone()) {
				StartUI.getInstance().getFrame().showMessageBox(this, "更新成功");
			}else{
				StartUI.getInstance().getFrame().showMessageBox(this, "更新失敗");
			}
		}catch(Exception ex){
			CVSLog.getLogger().error(this, ex);
			StartUI.getInstance().getFrame().showMessageBox(this, "更新失敗:" + ex.getMessage());
		}
	}
	
	private void initUI() {
		setTitle("");
		setBounds(100, 100, 725, 600);
//		setModal(true);
		// 增加座標記憶功能
		getTitleBar().addMouseListener(new MouseAdapter() {
					public void mouseReleased(MouseEvent e) {
						PropReader.setProperty("CVS.MODIFY_X", Integer.toString(ModifyDialog.this.getX()));	// 記憶座標X
						PropReader.setProperty("CVS.MODIFY_Y", Integer.toString(ModifyDialog.this.getY()));	// 記憶座標Y
					}
		});
	}
	
	private void initial() {
		getMainPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBorder(new MatteBorder(1, 0, 0, 0, new Color(207, 207, 207)));
		getMainPane().add(panel, BorderLayout.SOUTH);
		panel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("50dlu:grow"),},
			new RowSpec[] {
				RowSpec.decode("40px"),}));
		
		SSSJButton update_jBtn = new SSSJButton();
		update_jBtn.setBackground(Color.WHITE);
		
		// XXX 進行修改提交註記
		update_jBtn.addActionListener(new QueryActionListener(update_jBtn) {
			public void actPerformed(ActionEvent e) {
				doUpdateComment();
			}
		});
		update_jBtn.setText("進行更新");
		panel.add(update_jBtn, "1, 1, center, default");
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBorder(null);
		getMainPane().add(scrollPane, BorderLayout.CENTER);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		scrollPane.setViewportView(panel_1);
		panel_1.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("6dlu"),
				ColumnSpec.decode("40dlu"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("300dlu"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(80dlu;default)"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("30dlu"),
				FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("10dlu"),
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("100dlu"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("100dlu"),}));
		
		SSSJLabel lblModifyCvsComment = new SSSJLabel();
		lblModifyCvsComment.setFont(new Font("Arial", Font.BOLD, 26));
		lblModifyCvsComment.setText("Modify CVS Comment");
		panel_1.add(lblModifyCvsComment, "2, 2, 3, 1, center, default");
		
		SSSJLabel lblcommit = new SSSJLabel();
		lblcommit.setText("提供提交(Commit)時註記之更改，註記更改並非提交，僅做為註記更新。若註記之修改而影響包版，建議進行完整同步。");
		panel_1.add(lblcommit, "2, 3, 3, 1, center, default");
		
		SSSJLabel label_3 = new SSSJLabel();
		label_3.setText("檔案名稱");
		panel_1.add(label_3, "2, 5, right, default");
		
		JPanel panel_3 = new JPanel();
		panel_3.setBackground(Color.WHITE);
		panel_1.add(panel_3, "4, 5, fill, fill");
		panel_3.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("200dlu"),
				ColumnSpec.decode("61dlu"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("35dlu"),},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,}));
		
		filename_jTxtF = new SSSJTextField();
		filename_jTxtF.setEditable(false);
		filename_jTxtF.setForeground(Color.DARK_GRAY);
		filename_jTxtF.setBackground(new Color(207, 199, 188));
		filename_jTxtF.setBorder(new LineBorder(SystemColor.activeCaptionBorder));
		panel_3.add(filename_jTxtF, "1, 1, fill, default");
		
		SSSJLabel label_4 = new SSSJLabel();
		label_4.setText("版號");
		panel_3.add(label_4, "2, 1, right, default");
		
		version_jTxtF = new SSSJTextField();
		version_jTxtF.setEditable(false);
		version_jTxtF.setForeground(Color.DARK_GRAY);
		version_jTxtF.setBackground(new Color(207, 199, 188));
		version_jTxtF.setBorder(new LineBorder(SystemColor.activeCaptionBorder));
		panel_3.add(version_jTxtF, "4, 1, fill, default");
		
		module_jTxtF = new JTextField();
		module_jTxtF.setVisible(false);
		module_jTxtF.setEnabled(false);
		module_jTxtF.setEditable(false);
		panel_1.add(module_jTxtF, "6, 5, fill, default");
		module_jTxtF.setColumns(10);
		
		SSSJLabel label = new SSSJLabel();
		label.setText("工作目錄");
		panel_1.add(label, "2, 7, right, default");
		
		workdir_jTxtF = new SSSJTextField();
		workdir_jTxtF.setEditable(false);
		workdir_jTxtF.setForeground(Color.DARK_GRAY);
		workdir_jTxtF.setBackground(new Color(207, 199, 188));
		workdir_jTxtF.setBorder(new LineBorder(SystemColor.activeCaptionBorder));
		panel_1.add(workdir_jTxtF, "4, 7, fill, default");
		
		SSSJLabel label_1 = new SSSJLabel();
		label_1.setText("檔案路徑");
		panel_1.add(label_1, "2, 9, right, default");
		
		filepath_jTxtF = new SSSJTextField();
		filepath_jTxtF.setEditable(false);
		filepath_jTxtF.setForeground(Color.DARK_GRAY);
		filepath_jTxtF.setBackground(new Color(207, 199, 188));
		filepath_jTxtF.setBorder(new LineBorder(SystemColor.activeCaptionBorder));
		panel_1.add(filepath_jTxtF, "4, 9, fill, default");
		
		SSSJLabel label_2 = new SSSJLabel();
		label_2.setText("註記");
		panel_1.add(label_2, "2, 11, right, top");
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBorder(null);
		panel_1.add(scrollPane_1, "4, 11, fill, fill");
		
		oldComment_jTxtF = new JTextArea();
		oldComment_jTxtF.setFont(new Font(SSSPalette.fontFamily, Font.PLAIN, 12));
		scrollPane_1.setViewportView(oldComment_jTxtF);
		oldComment_jTxtF.setBorder(new LineBorder(SystemColor.activeCaptionBorder));
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(new Color(0, 206, 209));
		panel_1.add(panel_2, "2, 13, fill, fill");
		panel_2.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		SSSJLabel lblCvs = new SSSJLabel();
		panel_2.add(lblCvs, "1, 2, center, default");
		lblCvs.setText("伺服端註記");
		
		SSSJButton refresh_jBtn = new SSSJButton(SSSJButton.ITEM_DARK);
		
		// XXX 進行驗證
		refresh_jBtn.addActionListener(new QueryActionListener(refresh_jBtn) {
			public void actPerformed(ActionEvent e) {
				doVerifyComment();
			}
		});
		panel_2.add(refresh_jBtn, "1, 6");
		refresh_jBtn.setText("驗證");
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBorder(null);
		panel_1.add(scrollPane_2, "4, 13, fill, fill");
		
		newComment_jTxtF = new JTextArea();
		newComment_jTxtF.setEditable(false);
		newComment_jTxtF.setFont(new Font(SSSPalette.fontFamily, Font.PLAIN, 12));
		scrollPane_2.setViewportView(newComment_jTxtF);
		newComment_jTxtF.setBackground(new Color(218, 238, 243));
		newComment_jTxtF.setBorder(new LineBorder(SystemColor.activeCaptionBorder));
	}
	
	public void setTitle(Long id, String version, String filename, int maxLengths) {
		filename = (filename.length() > maxLengths)? filename.substring(0, maxLengths - 2) + "..": filename;
		setTitle(StringUtil.concat("[", version, "] ", filename));
	}

	/**
	 * Create the dialog.
	 */
	public ModifyDialog() {
		super();
		initial();
		initUI();
	}

	public SSSJTextField getFilepath_jTxtF() {
		return filepath_jTxtF;
	}

	public SSSJTextField getWorkdir_jTxtF() {
		return workdir_jTxtF;
	}

	public JTextArea getOldComment_jTxtF() {
		return oldComment_jTxtF;
	}

	public JTextArea getNewComment_jTxtF() {
		return newComment_jTxtF;
	}

	public SSSJTextField getFilename_jTxtF() {
		return filename_jTxtF;
	}

	public SSSJTextField getVersion_jTxtF() {
		return version_jTxtF;
	}

	public Tbsoptcvsver getCurrentVer() {
		return currentVer;
	}

	public void setCurrentVer(Tbsoptcvsver currentVer) {
		this.currentVer = currentVer;
	}
	
	public JTextField getModule_jTxtF() {
		return module_jTxtF;
	}
}
