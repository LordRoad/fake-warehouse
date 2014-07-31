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
package org.artemis.toolkit.table.gen;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import org.artemis.toolkit.common.fileutils;
import org.artemis.toolkit.common.sysconfig;
import org.artemis.toolkit.metadata.columnmd;
import org.artemis.toolkit.metadata.tablemd;
import org.artemis.toolkit.table.newdatatype;
import org.artemis.toolkit.table.tabledata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * genjob generate data
 * genjob.java is written at Jun 17, 2014
 * @author junli
 * @since 0.2
 */
public class genjob implements Runnable {
	private static final Logger LOG = LoggerFactory.getLogger(genjob.class);
	private static long sDafaultBatchRowCount = 10000;
	
	private tabledata mtabledata = null;
	private String mStoragePath = "";
	private tablemd mtablemd;
	private boolean mNeedCache = false;
	private gendata[] mgendata;
	private boolean mNeedLookup = false;
	private int[] mCacheColumnIndexs = null; 
	
	public genjob(tablemd itablemd, String iStoragepath, boolean iNeedCache, int[] iCacheIndex) {
		mtablemd = itablemd;
		mStoragePath = iStoragepath;
		mNeedCache = iNeedCache;
		mCacheColumnIndexs = iCacheIndex;
		
		if (!init()) {
			throw new RuntimeException("genjob init failed");
		}
	}
	
	private boolean init() {
		if (mStoragePath.length() > 0) {
			try {
				fileutils.initfile(mStoragePath);
			} catch (IOException e) {
				LOG.error(e.getLocalizedMessage());
				return false;
			}
		}
		
		int lColumnCount = mtablemd.getcolcount();
		if (lColumnCount < 1) {
			LOG.error("column count should be larger than one for table: " + mtablemd.getmTableName());
			return false;
		}
		
		if (mNeedCache) {
			
		}
		
		mgendata = new gendata[lColumnCount];
		for (int iter = 0; iter < lColumnCount; ++iter) {
			columnmd lcolmd = mtablemd.getColumn(iter);
			newdatatype lnewdatatype = lcolmd.getmColType();
			if (lnewdatatype.value().compareToIgnoreCase(newdatatype.innerDT.sBoolean) == 0) {	
				mgendata[iter] = new genBoolean(
						lcolmd.getmColOrder(), lcolmd.getmColStep(), lcolmd.getmColRange());
			}
			else if (lnewdatatype.value().compareToIgnoreCase(newdatatype.innerDT.sShort) == 0) {
				mgendata[iter] = new genShort(
						lcolmd.getmColOrder(), lcolmd.getmColStep(), lcolmd.getmColRange());
			}
			else if (lnewdatatype.value().compareToIgnoreCase(newdatatype.innerDT.sInt) == 0) {
				mgendata[iter] = new genInteger(
						lcolmd.getmColOrder(), lcolmd.getmColStep(), lcolmd.getmColRange());
			}
			else if (lnewdatatype.value().compareToIgnoreCase(newdatatype.innerDT.sDouble) == 0) {
				mgendata[iter] = new genDouble(
						lcolmd.getmColOrder(), lcolmd.getmColStep(), lcolmd.getmColRange());
			}
			else if (lnewdatatype.value().compareToIgnoreCase(newdatatype.innerDT.sLong) == 0) {
				mgendata[iter] = new genLong(
						lcolmd.getmColOrder(), lcolmd.getmColStep(), lcolmd.getmColRange());
			}
			else if (lnewdatatype.value().compareToIgnoreCase(newdatatype.innerDT.sDate) == 0) {
				mgendata[iter] = new genDate(
						lcolmd.getmColOrder(), lcolmd.getmColStep(), lcolmd.getmColRange());
			}
			else if (lnewdatatype.value().compareToIgnoreCase(newdatatype.innerDT.sString) == 0) {
				mgendata[iter] = new genString(
						lcolmd.getmColOrder(), lcolmd.getmColStep(), lcolmd.getmColRange());
			} else {
				// extra data type
				extradata lextradata = extradatapool.instance().getExtraData(lnewdatatype.value());
				if (lextradata == null) {
					return false;
				}
					
				mgendata[iter] = gendata.getCustomizedGenerator(lextradata, 
						lcolmd.getmColOrder(), lcolmd.getmColStep(), lcolmd.getmColRange());
			}
		}
		
		return true;
	}
	
	/**
	 * if need cache is set, the generated data will be cached into memory table
	 * @return table data, or null
	 */
	public tabledata getTableData() {
		return mtabledata;
	}
	
	@Override
	public void run() {
		LOG.trace("job ( table name: " + this.mtablemd.getmTableName() + 
				", storage path: " + this.mStoragePath + ") is beginning");
		
		if (mStoragePath.length() < 1 && !mNeedCache) {
			LOG.error("no cache, no storage ? for table: " + mtablemd.getmTableName());
			return;
		}
		
		long lRowCount = mtablemd.getmTableRowcount();
		if (lRowCount < 1) {
			LOG.error("row count should be larger than one for table: " + mtablemd.getmTableName());
			return;
		}
		
		FileOutputStream lFileOutputStream = null;
		PrintWriter lPrintWriter = null;
		try {
			fileutils.initfile(mStoragePath);
			lFileOutputStream = new FileOutputStream(mStoragePath, false);
			lPrintWriter = new PrintWriter(lFileOutputStream);
		} catch (IOException e) {
			LOG.error(e.getLocalizedMessage());
			return;
		} 
		
		String lOneBatchSetting = System.getProperty(sysconfig.sGenOneBatchRowCount);
		
		long lOneBatch = sDafaultBatchRowCount;
		if (lOneBatchSetting != null) {
			try {
				lOneBatch =  Long.parseLong(lOneBatchSetting);
				lOneBatch = lOneBatch < 1 ? sDafaultBatchRowCount : lOneBatch;
			} catch (NumberFormatException e) {
				LOG.warn(e.getLocalizedMessage());
			}
		}
		
		long lBatchCount = (long)(lRowCount / lOneBatch);
		long lLastBatchRowCount = lRowCount % lOneBatch;
		
		if (mNeedCache) {
			
			
			// need cache
			
		} else {
			
			for (long index = 0; index < lBatchCount; ++index) {
				lPrintWriter.print(genOneChunk(lOneBatch));
				lPrintWriter.flush();
			}
			if (lLastBatchRowCount > 0) {
				lPrintWriter.print(genOneChunk(lLastBatchRowCount));
			}
			
			lPrintWriter.flush();
			lPrintWriter.close();
		}
		
		LOG.trace("job ( table name: " + this.mtablemd.getmTableName() + 
				", storage path: " + this.mStoragePath + ") is done successfully.");
	}

	private String genOneChunkAndCache(long irowcount) {
		return null;
	}
	
	private String[] genOneColumn(int iColumnIndex) {
		int lColCount = mgendata.length;
		if (iColumnIndex < 0 || iColumnIndex >= lColCount) {
			return null;
		}
		
		//String[] lWholeColumnData = new String[mtablemd.getmTableRowcount()];
		
		return null;
	}
	
	private String genOneChunk(long irowcount) {
		int lColCount = mgendata.length;
		
		//long lBegin = System.currentTimeMillis();
		
		while (true) {
			StringBuilder lChunkData = new StringBuilder();
			
			/**
			 * DO NOT use str1 += str2; it will be str1 = str1 + str2; -> new StringBuilder(str1).append(str2).toString();
			 * it will create one string and append one and create string object, then copy it to str1. it's bad.
			 * so should use append (byte copy)
			 */
			try {
				if (!mNeedLookup) {
					int iter = 0;
					int jter = 0;
					for ( iter = 0; iter < irowcount; ++iter) {
						for (jter = 0; jter < lColCount - 1; ++jter) {
							try {
								lChunkData.append(mgendata[jter].generateOneItem()).append(",");
							} catch (NoMoreDataException e) {
								lChunkData.append(",");
							}
						}
						try {
							lChunkData.append(mgendata[(int) (lColCount - 1)].generateOneItem());
						} catch (NoMoreDataException e) {
						}
						lChunkData.append("\n");
					}
				}
			} catch (OutOfMemoryError e) {
				lChunkData = null;
				System.gc();
				try {
					Thread.sleep(300);
				} catch (InterruptedException e1) {
					// ignore
				}
				System.gc();
				continue;
			}
			
			/*
			if (irowcount >= sDafaultBatchRowCount) {
				long lEnd = System.currentTimeMillis();
				LOG.info("one batch time: " + Long.toString(lEnd - lBegin) + " millis");
				System.out.println("one batch time: " + Long.toString(lEnd - lBegin) + " millis");
			}
			*/
			
			return lChunkData.toString();
		}
	}
	
}
