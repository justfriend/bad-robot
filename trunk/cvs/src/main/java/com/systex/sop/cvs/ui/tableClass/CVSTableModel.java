package com.systex.sop.cvs.ui.tableClass;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.apache.commons.beanutils.PropertyUtils;

import com.systex.sop.cvs.helper.CVSLog;

@SuppressWarnings({ "unchecked", "serial" })
public class CVSTableModel extends AbstractTableModel {
	private List<? extends CVSBaseDO> tList;
	private CVSBaseDO t;
	private String [] methodName;
	private Class [] methodType;
	
	public CVSTableModel(List<? extends CVSBaseDO> tList) {
		this.tList = (tList == null)? new ArrayList<CVSBaseDO>(): tList;
		this.t = (tList.size() > 0)? tList.get(0): null;
		if (t != null) {
			Field [] fields = t.getClass().getDeclaredFields();
			methodName = new String [fields.length];
			methodType = new Class [fields.length];
			for (int i=0; i<fields.length; i++) {
				if (Modifier.isStatic(fields[i].getModifiers())) continue;	// skip static field
				methodName[i] = fields[i].getName();
				methodType[i] = getTypeClass(fields[i].getType());
			}
			
		}
	}
	
	private Class<? extends Object> getTypeClass(Type type) {
		if (type.equals(String.class)) {
			return String.class;
		}else
		if (type.equals(Integer.class)) {
			return Integer.class;
		}else
		if (type.equals(BigDecimal.class)){
			return BigDecimal.class;
		}else
		if (type.equals(Timestamp.class)) {
			return Timestamp.class;
		}
		
		throw new RuntimeException ("Type " + type + " do not support");
	}
	
	@Override
	public String getColumnName(int column) {
		return (t == null)? "": t.getColumnName()[column];
	}

	@Override
	public int getColumnCount() {
		return (t == null)? 0: t.getColumnName().length;
	}

	@Override
	public int getRowCount() {
		return tList.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		try {
			return PropertyUtils.getSimpleProperty(tList.get(rowIndex), methodName[columnIndex]);
		} catch (Exception e) {
			CVSLog.getLogger().error("rowIndex is " + rowIndex + ", columnIndex is " + columnIndex);
			throw new RuntimeException(e);
		}
	}

}
