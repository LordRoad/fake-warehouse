/**
 * 
 */
package org.artemis.toolkit.generatefile;

import java.util.ArrayList;
import java.util.List;

/**
 * @author return_jun
 *
 */
public class filedefinition {
	
	public class ColumnDataType {
		public static final int MSI_TF = 3;
		public static final int MSI_SHORT = 21;
		public static final int MSI_INTEGER = 1;
		public static final int MSI_LONG = 22;
		public static final int MSI_FLOAT = 7;
		public static final int MSI_DOUBLE = 6;
		public static final int MSI_DATE = 14;
		public static final int MSI_TIME = 15;
		public static final int MSI_DATETIME = 16;
		public static final int MSI_STRING = 9;
		public static final int MSI_MBCS_STRING = 23;
		public static final int MSI_BINARY = 11;
		public static final int MSI_BIGDECIMAL = 30;
		public static final int MSI_CELLFMTDATA = 31;
		public static final int MSI_INVALID = -1;
		public static final int MSI_UNSIGNED_INTEGER = 2;
		public static final int MSI_UTF8_STRING = 33;
		public static final int MSI_LONGLONG = 34;
	}
	
	// public String mBasicFileName = "";
	public int mChunks = 1;
	public String mSeperator = ",";
	public long mRowsCount = 0;
	public String mFilePath = "";
	public List<Integer> mColumnTypeArray = new ArrayList<Integer>();
	public List<Long> mColumnTypeMeta = new ArrayList<Long>();
	
	public void InsertNewColumn(int iColumnType, long iColumnMeta) {
		synchronized (this) {
			mColumnTypeArray.add(new Integer(iColumnType));
			mColumnTypeMeta.add(new Long(iColumnMeta));
		}
	}
	
	public void SetFilePath(String iFilePath) {
		mFilePath = iFilePath;
	}
	
	public void SetRowCount(long iRowCount) {
		mRowsCount = iRowCount;
	}
	
	public void SetSeperator(String iSeperator) {
		mSeperator = iSeperator;
	}
	
	
}
