package com.systex.sop.cvs.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.naming.ConfigurationException;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;

/**
 * Properties reader HELPER
 * <p>
 * 此類別為屬性檔的設定與取得類別，便利設定檔之分類及存取<p>
 * 1. PROPERTY_HOME 讀自環境變數，如果要指定屬性檔的路徑(目錄)請自行設定<br>
 *    PROPERTY_HOME 環境變數若為空值則改為當前預設路徑<br>
 * 2. 讀取方式為呼叫 getProperty 方法，並帶給它一個字串以說明要讀那個屬性檔及值<br>
 *    舉例 getProperty("CVS.SERVERMODE") 則會去讀 CVS.properties 並取得SERVERMODE的設定值<br>
 * 3. 此類別除了可以「GET」亦可以「SET」，無論是直接改檔案或是重新「SET」其屬性檔會自行更新<br>
 *    預設的更新時間(FileChangedReloadingStrategy)大約是 5秒
 * <p>
 * Modify history : <br>
 * ================================================================================<br>
 * 2012/01/02	.[- _"].	Properties reader release (copy version)
 * <p>
 *
 */
public class PropReader {
	private static String PROPERTY_HOME;							// 屬性檔所在目錄
	private static String PROPERTY_EXTENSION;						// 屬性檔之副檔名
	private static Map<String, PropertiesConfiguration> PROP_MAP;	// 屬性檔靜態保存
	private static Pattern PATTERN_PROPERTY_NAME;					// 驗證屬性名稱之型樣
	
	static {
		PATTERN_PROPERTY_NAME = Pattern.compile("^[\\w]+[.][\\w]+$");
		PROPERTY_EXTENSION = ".properties";
		PROP_MAP = new HashMap<String, PropertiesConfiguration>();
		
		// 取得屬性檔所在目錄之環境變數之值
		PROPERTY_HOME = System.getenv("PROPERTY_HOME");
		
		if (StringUtil.isEmpty(PROPERTY_HOME)) {
			PROPERTY_HOME = System.getenv().get("PROPERTY_HOME");
		}
		if (StringUtil.isEmpty(PROPERTY_HOME)) {
			PROPERTY_HOME = System.getProperty("PROPERTY_HOME");
		}

		if (StringUtil.isEmpty(PROPERTY_HOME)) {
//			throw new RuntimeException("Environment PROPERTY_HOME must be set");
			PROPERTY_HOME = ".";
		}
	}
	
	/**
	 * 定義屬性結構
	 */
	private static class PROPERTY {
		public String fileName;		// 屬性主檔名
		public String key;			// 屬性鍵
		public String value;		// 屬性值
		
		public String getPropertyFileName() {
			return new StringBuffer()
					.append(PROPERTY_HOME)
					.append(File.separator)
					.append(fileName)
					.append(PROPERTY_EXTENSION)
					.toString();
		}
	}
	
	/**
	 * 取得屬性檔所在目錄
	 * @return 屬性檔所在目錄
	 */
	public static String getPropertyHome() {
		return PROPERTY_HOME;
	}
	
	/**
	 * 分離屬性名稱<br>
	 * 將屬性名稱分離成屬性檔主檔名及屬性鍵
	 * @param propertyName 屬性名稱
	 * @return
	 */
	public static PROPERTY splitPropertyName(String propertyName) {
		PROPERTY prop = new PROPERTY();
		
		String [] splitProp = propertyName.split("[.]");
		prop.fileName = splitProp[0];
		prop.key = splitProp[1];
		
		return prop;
	}
	
	public static boolean verifyFieldByPattern(String field, Pattern pattern) {
		if (StringUtil.isEmpty(field) || pattern == null)  return false;
		
		return pattern.matcher(field).find();
	}
	
	/**
	 * 驗證屬性名稱<br>
	 * 驗證屬性名稱是否符合格式, 其格式為：屬性檔主檔名.屬性鍵, 例：SYNC.COMMAND
	 * @param PropertyName 屬性名稱
	 * @return
	 */
	public static void vrfPropertyName(String propertyName) {
		boolean flag = verifyFieldByPattern(propertyName, PATTERN_PROPERTY_NAME);
		
		if (!flag) {
			throw new RuntimeException (
				new StringBuffer()
					.append("Property name illegal, the string is [")
					.append(propertyName)
					.append("]")
					.append(", and the regular is [")
					.append(PATTERN_PROPERTY_NAME)
					.append("]")
					.toString()
			);
		}
	}
	
	/**
	 * 取得屬性值(字串)
	 * @param propertyName 屬性名稱
	 * @return 屬性值(字串)
	 * @throws ConfigurationException 
	 */
	public static String getProperty(String propertyName) {
		PropertiesConfiguration config = null;
		
		// 驗證屬性名稱 (確保屬性名稱合法)
		vrfPropertyName(propertyName);
		
		// 取得屬性物件
		PROPERTY prop = splitPropertyName(propertyName);
		
		// 判斷屬性物件是否已存在於靜態保存
		if (PROP_MAP.containsKey(propertyName)) {
			
			// 取得屬性物件從靜態保存
			config = PROP_MAP.get(propertyName);
		}
		else {
			
			// 開啟屬性檔案
			try {
				config = new PropertiesConfiguration(new File(prop.getPropertyFileName()));
				
				// 設定屬性檔變動時重載
				config.setReloadingStrategy(new FileChangedReloadingStrategy());
				
				// 判斷屬性檔案是否存在(空)
				if (config.isEmpty()) throw new ConfigurationException ("Property file is empty");
				if (config == null) throw new ConfigurationException ("Read poperty file exception");
			}
			catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException (
					new StringBuffer()
						.append("Read property file exception, the property file is [")
						.append(prop.getPropertyFileName())
						.append("]").toString()
				);
			}
			
			// 保存屬性檔案於靜態儲存
			PROP_MAP.put(propertyName, config);
		}
		
		// 取得屬性值
		prop.value = config.getString(prop.key);

		// 回傳屬性值
		return prop.value;
	}
	
	/**
	 * 取得屬性值(數值)
	 * @param propertyName 屬性名稱
	 * @return 屬性值(數值)
	 * @throws ConfigurationException 
	 */
	public static int getPropertyInt(String propertyName) {
		return Integer.parseInt(getProperty(propertyName));
	}
	
	/**
	 * 設定屬性名稱之屬性值
	 * @param propertyName 屬性名稱
	 * @param value 屬性值
	 */
	public static void setProperty(String propertyName, String value) {
		PropertiesConfiguration config = null;
		
		// 驗證屬性名稱 (確保屬性名稱合法)
		vrfPropertyName(propertyName);
		
		// 取得屬性物件
		PROPERTY prop = splitPropertyName(propertyName);
		if (value == null) value = "";
		prop.value = value;
		
		// 判斷屬性物件是否已存在於靜態保存
		if (PROP_MAP.containsKey(propertyName)) {
			
			// 取得屬性物件從靜態保存
			config = PROP_MAP.get(propertyName);
		}
		else {
			
			// 開啟屬性檔案
			try {
				config = new PropertiesConfiguration(new File(prop.getPropertyFileName()));
			}
			catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException (
					new StringBuffer()
						.append("Read property file exception, the property file is [")
						.append(prop.getPropertyFileName())
						.append("]").toString()
				);
			}
			
			// 保存屬性檔案於靜態儲存
			PROP_MAP.put(propertyName, config);
		}
		
		// 設定屬性值
		config.setReloadingStrategy(new FileChangedReloadingStrategy());
		config.setAutoSave(true);
		config.setProperty(prop.key, prop.value);
	}
	
	public static void main(String [] args) {
		System.out.println(PropReader.getProperty("CACHE.PATH"));
	}
}