/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.artemis.toolkit.genfile;

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

import org.artemis.toolkit.common.configparser;
import org.artemis.toolkit.common.fileutils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * genjob TODO
 * genjob.java is written at Jun 15, 2014
 * @author return_jun
 */
public class genjob implements Runnable {
	private static final Logger LOG = LoggerFactory.getLogger(genjob.class);
	
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
	
	private boolean mInvalidColumnChecked = false;
	
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
	
	/** (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		FileOutputStream lFileOutputStream = null;
		PrintWriter lPrintWriter = null;
		try {
			fileutils.initfile(mFilePath);
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
				if (!mInvalidColumnChecked) {
					mInvalidColumnChecked = true;
					LOG.warn("current columnmd type " + mColumnType[iter] + " is not supported.");
				}
				break;
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

}
