package com.systex.sop.cvs.constant;

public class CVSConst {
	public static final String ENCODING_IN = "MS950";
	public static final String ENCODING_OUT = "UTF-8";

	public enum FLAG {
		SESSION('S'), LOG('L');

		private char text;

		private FLAG(char flag) {
			this.text = flag;
		}

		public char getText() {
			return text;
		}

		public void setText(char text) {
			this.text = text;
		}
	}
	
	public enum STATUS {
		LOGIN('I'), LOGOUT('O'), CANCEL('C');

		private char text;

		private STATUS(char flag) {
			this.text = flag;
		}

		public char getText() {
			return text;
		}

		public void setText(char text) {
			this.text = text;
		}
	}

}
