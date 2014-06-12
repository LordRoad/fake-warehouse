/**
 * 
 */
package org.artemis.toolkit.generatefile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * @author return_jun
 *
 */
public class genjob implements Runnable {

	private filedefinition mfiledefinition;
	private Random mRandom;
	private int mColumnCount = 0;
	private Integer[] mColumnType;
	private Long[] mColumnMeta;
	private long mDateBegin = 0;
	private long mDateEnd = 0;
	private long mDurationDate = 0;
	private Random mTFRandom;
	private Random mLongRandom;
	private SimpleDateFormat mDateFormat = null;
	
	private String mFilePath = "";
	private long mRowsCount = 0;
	
	public genjob(filedefinition ifiledefinition) {
		mfiledefinition = ifiledefinition;
		mRandom = new Random(1);
		mColumnType = mfiledefinition.mColumnTypeArray.toArray(new Integer[0]);
		mColumnMeta = mfiledefinition.mColumnTypeMeta.toArray(new Long[0]);
		mColumnCount = mColumnType.length;
		mFilePath = mfiledefinition.mFilePath;
		mRowsCount = mfiledefinition.mRowsCount;
		
		for (int iter = 0; iter < mColumnCount; ++iter) {
			if (mColumnType[iter] == filedefinition.ColumnDataType.MSI_TF) {
				inittf();
			} else if (mColumnType[iter] == filedefinition.ColumnDataType.MSI_DATE) {
				initdateenv();
			} else if (mColumnType[iter] == filedefinition.ColumnDataType.MSI_LONG) {
				initlong(mColumnMeta[iter]);
			}
		}
	}
	
	public void setfilepath(String ifilepath) {
		mFilePath = ifilepath;
	}
	
	public void setrowcount(long irowcount) {
		mRowsCount = irowcount;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		FileOutputStream lFileOutputStream = null;
		PrintWriter lPrintWriter = null;
		try {
			initfile(mFilePath);
			lFileOutputStream = new FileOutputStream(mFilePath, false);
			lPrintWriter = new PrintWriter(lFileOutputStream);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		long lFlushCount = 1000000;
		long lCurrentBuffered = 0;
		for (long index = 0; index < mRowsCount; ++index) {
			lPrintWriter.println(generaterow());
			if (++lCurrentBuffered >= lFlushCount) {
				lPrintWriter.flush();
				lCurrentBuffered = 0;
			}
		}
		
		lPrintWriter.flush();
		lPrintWriter.close();
	}
	
	protected void inittf() {
		mTFRandom = new Random();
	}
	
	protected void initlong(long iseed) {
		mLongRandom = new Random();
		// mLongRandom.setSeed(iseed);
	}
	
	protected void initdateenv() {
		mDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date lBeginDate = mDateFormat.parse("2011-07-01");
			mDateBegin = lBeginDate.getTime();
			Date lEndDate = mDateFormat.parse("2013-10-01");
			mDateEnd = lEndDate.getTime();
			mDurationDate = mDateEnd - mDateBegin;
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	protected String generatedate() {
		Date lRandomDate = new Date((long) (mDateBegin + Math.random() * mDurationDate));
		return mDateFormat.format(lRandomDate);
	}
	
	protected String generaterow() {
		String lCurrentRow = "";
		for (int iter = 0; iter < mColumnCount - 1; ++iter) {
			switch (mColumnType[iter])
			{
			case filedefinition.ColumnDataType.MSI_TF :
				lCurrentRow += (mTFRandom.nextBoolean() == true ? "1" : "0") + ",";
				break;
				
			case filedefinition.ColumnDataType.MSI_INTEGER :
				lCurrentRow += Integer.toString((int)(mRandom.nextInt(mColumnMeta[iter].intValue()))) + ",";
				break;
				
			case filedefinition.ColumnDataType.MSI_DATE :
				lCurrentRow += generatedate() + ",";
				break;
				
			case filedefinition.ColumnDataType.MSI_STRING :
				break;
				
			case filedefinition.ColumnDataType.MSI_LONG :
				long lTest = mLongRandom.nextLong();
				lCurrentRow += Long.toString((lTest < 0 ? -lTest : lTest) % mColumnMeta[iter]) + ",";
				break;
				
			default:
				assert false;
			}
		}
		switch (mColumnType[mColumnCount - 1])
		{
			case filedefinition.ColumnDataType.MSI_TF :
				lCurrentRow += Integer.toString(mTFRandom.nextInt(2));
				break;
				
			case filedefinition.ColumnDataType.MSI_INTEGER :
				lCurrentRow += Integer.toString((int)mRandom.nextInt(mColumnMeta[mColumnCount - 1].intValue()));
				break;
				
			case filedefinition.ColumnDataType.MSI_DATE :
				lCurrentRow += generatedate();
				break;
				
			case filedefinition.ColumnDataType.MSI_STRING :
				break;
				
			case filedefinition.ColumnDataType.MSI_LONG :
				long lTest = mLongRandom.nextLong();
				lCurrentRow += Long.toString((lTest < 0 ? -lTest : lTest) % mColumnMeta[mColumnCount - 1]);
				break;
				
			default:
				assert false;
			}
		return lCurrentRow;
	}
	
	private void initfile(String ifilepath) throws IOException {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(ifilepath);
		} catch (FileNotFoundException e) {
			File lFile = new File(ifilepath);
			if (!lFile.createNewFile()) {
				throw e;
			}
			out = new FileOutputStream(lFile);
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

}
