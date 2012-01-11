package com.systex.sop.cvs.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.Writer;

/**
 * Stream close HELPER
 * <p>
 * <p>
 * Modify history : <br>
 * ================================ <br>
 * 2012/01/02 .[- _"]. release
 * <p>
 * 
 */
public class StreamCloseHelper {

	/**
	 * Close InputStream
	 * <p>
	 * @param isArray
	 */
	public static void closeInputStream(InputStream... isArray) {
		if (isArray == null)
			return;

		for (InputStream is : isArray) {
			if (is != null) {
				try {
					is.close();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					is = null;
				}
			}
		}
	}

	/**
	 * Close OutputStream
	 * <p>
	 * @param osArray
	 */
	public static void closeOutputStream(OutputStream... osArray) {
		if (osArray == null)
			return;

		for (OutputStream os : osArray) {
			if (os != null) {
				try {
					os.close();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					os = null;
				}
			}
		}
	}

	/**
	 * Close RandomAccessFile
	 * <p>
	 * @param fileArray
	 */
	public static void closeRandomAccessFile(RandomAccessFile... fileArray) {
		if (fileArray == null)
			return;

		for (RandomAccessFile file : fileArray) {
			if (file != null) {
				try {
					file.close();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					file = null;
				}
			}
		}
	}

	/**
	 * Close Reader
	 * <p>
	 * @param reader
	 */
	public static void closeReader(Reader... readerArray) {
		if (readerArray == null)
			return;

		for (Reader reader : readerArray) {
			if (reader != null) {
				try {
					reader.close();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					reader = null;
				}
			}
		}
	}

	/**
	 * Close Writer
	 * <p>
	 * @param writer
	 */
	public static void closeWriter(Writer... writerArray) {
		if (writerArray == null)
			return;

		for (Writer writer : writerArray) {
			if (writer != null) {
				try {
					writer.close();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					writer = null;
				}
			}
		}
	}

}
