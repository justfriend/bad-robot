package com.systex.sop.cvs.ui.tableClass;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.apache.commons.beanutils.PropertyUtils;

import com.systex.sop.cvs.helper.CVSLog;

@SuppressWarnings({ "unchecked", "serial" })
public class CVSTableModel extends AbstractTableModel {
	private List<? extends CVSBaseDO> tList;	// DATA LIST
	private CVSBaseDO t;						// JUST A BASEDO TEMPLATE
	private String [] methodName;				// 欄位名稱
	private Class [] methodType;				// 欄位型態
	private String [] dateFormat;				// 資料格式 (NULL 代表不進行格式；若有值則依型態進行轉換並以字串呈現)
	private static Map<String, SimpleDateFormat> sdfMap = new HashMap<String, SimpleDateFormat>();
	
	public CVSTableModel(JTable table, List<? extends CVSBaseDO> tList) {
		this.tList = (tList == null)? new ArrayList<CVSBaseDO>(): tList;
		this.t = (tList.size() > 0)? tList.get(0): null;
		if (t != null) {
			Field [] fields = t.getClass().getDeclaredFields();
			methodName = new String [getColumnCount()];
			methodType = new Class [getColumnCount()];
			dateFormat = t.getColumnFormat();
			int i = 0;
			for (Field field : fields) {
				if (Modifier.isStatic(field.getModifiers())) continue;	// skip static field
				methodName[i] = field.getName();
				methodType[i] = getTypeClass(field.getType());
				i++;
			}
			
			// TABLE SUPPORT SORTING
			table.setRowSorter(new TableRowSorter<TableModel>(this));
		}
	}
	
	private static SimpleDateFormat genSimpleDateFormat(String format) {
		if (!sdfMap.containsKey(format)) {
			sdfMap.put(format, new SimpleDateFormat(format));
		}
		
		return sdfMap.get(format);
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
		}else
		if (type.equals(Long.class)) {
			return Long.class;
		}else
		if (type.equals(char.class)) {
			return Character.class;
		}
		
		throw new RuntimeException ("Type " + type + " do not support");
	}
	
	public List<? extends CVSBaseDO> getDataList() {
		return tList;
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
			Object value = PropertyUtils.getSimpleProperty(tList.get(rowIndex), methodName[columnIndex]);
			if (value != null) {
				/** 日期格式 **/
				if (Timestamp.class == methodType[columnIndex]) {
					if (dateFormat[columnIndex] != null) {
						Date d = new Date( ((Timestamp) value).getTime());
						return genSimpleDateFormat(dateFormat[columnIndex]).format(d).toString();
					}
				}
				// TODO 數字格式
			}
			
			return value;
		} catch (Exception e) {
			CVSLog.getLogger().error("rowIndex is " + rowIndex + ", columnIndex is " + columnIndex);
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (dateFormat[columnIndex] != null) {
			return String.class;
		}else{
			return methodType[columnIndex];
		}
	}
}
