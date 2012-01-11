package com.systex.sop.cvs.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Connection and Hibernate resource HELPER
 * <p>
 * <p>
 * Modify history : <br>
 * ======================================== <br>
 * 2012/01/04	.[- _"].	release
 * <p>
 *
 */
public class JDBCResCloseHelper {
	
	public static void closeStatement (Statement ... stmtArray) {
		if (stmtArray == null) return;
		
		for (Statement stmt : stmtArray) {
			if (stmt != null) {
				try {
					stmt.close();
				}catch(SQLException e){
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void closeResultSet (ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
	}

}
