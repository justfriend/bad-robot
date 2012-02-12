package com.systex.sop.cvs.constant;

import com.badrobot.ui.containers.BadTrafficLabel.LIGHT;

public class CVSConst {
	public static final String ENCODING_IN = "MS950";
	public static final String ENCODING_OUT = "UTF-8";
	public static final String SPLIT_DESC = "----------------------------";
	public static final String BLOCK_END = "===========================================================================";
	
	public enum CMDTYPE {
		CHK_CVS, CHK_MODULE, CHK_LOGPATH, CHK_DBCONN, CHK_CRON,
		CHK_SCHEDULAR
	};
	
	public enum CMD_RESULT {
		SUCCESS(LIGHT.GREEN), FAILURE(LIGHT.RED), WARNING(LIGHT.YELLOW);
		private LIGHT light;

		private CMD_RESULT(LIGHT light) {
			this.light = light;
		}

		public LIGHT getLight() {
			return light;
		}
	}

	public enum LOGIN_FLAG {
		SESSION('S'), LOG('L');
		private char text;

		private LOGIN_FLAG(char flag) {
			this.text = flag;
		}

		public char getText() {
			return text;
		}

		public void setText(char text) {
			this.text = text;
		}
	}
	
	public enum LOGIN_STATUS {
		LOGIN('I', "登入中"), LOGOUT('O', "已登出" ), CANCEL('C', "已取消");

		private char flag;
		private String desc;

		private LOGIN_STATUS(char flag, String desc) {
			this.flag = flag;
			this.desc = desc;
		}

		public char getFlag() {
			return flag;
		}
		
		public String getDesc() {
			return desc;
		}
		
		public static LOGIN_STATUS findDesc(char flag) {
			for (LOGIN_STATUS e : LOGIN_STATUS.values()) {
				if (e.getFlag() == flag) return e;
			}
			
			return null;
		}
	}

}
