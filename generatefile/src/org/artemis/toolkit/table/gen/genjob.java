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
import org.artemis.toolkit.table.columnmd;
import org.artemis.toolkit.table.tabledata;
import org.artemis.toolkit.table.tablemd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * genjob TODO
 * genjob.java is written at Jun 17, 2014
 * @author junli
 */
public class genjob implements Runnable {
	private static final Logger LOG = LoggerFactory.getLogger(genjob.class);
	
	private tabledata mtabledata = null;
	private String mStoragePath = "";
	private tablemd mtablemd;
	private boolean mNeedCache = false;
	private gendata[] mgendata;
	
	public genjob(tablemd itablemd, String iStoragepath, boolean iNeedCache) {
		mtablemd = itablemd;
		mStoragePath = iStoragepath;
		mNeedCache = iNeedCache;
		
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
		
		mgendata = new gendata[lColumnCount];
		for (int iter = 0; iter < lColumnCount; ++iter) {
			columnmd lcolmd = mtablemd.getColumn(iter);
			switch (lcolmd.getmColType()) {
			case BOOLEAN :
				mgendata[iter] = new genBoolean(lcolmd.getmColOrder(), lcolmd.getmColStep(), lcolmd.getmColRange());
				break;
			case SHORT :
				mgendata[iter] = new genShort(lcolmd.getmColOrder(), lcolmd.getmColStep(), lcolmd.getmColRange());
				break;
			case INT :
				mgendata[iter] = new genInteger(lcolmd.getmColOrder(), lcolmd.getmColStep(), lcolmd.getmColRange());
				break;
			case FLOAT :
			case DOUBLE :
				mgendata[iter] = new genDouble(lcolmd.getmColOrder(), lcolmd.getmColStep(), lcolmd.getmColRange());
				break;
			case LONG :
				mgendata[iter] = new genLong(lcolmd.getmColOrder(), lcolmd.getmColStep(), lcolmd.getmColRange());
				break;
			case DATE :
				mgendata[iter] = new genDate(lcolmd.getmColOrder(), lcolmd.getmColStep(), lcolmd.getmColRange());
				break;
			case STRING :
				mgendata[iter] = new genString(lcolmd.getmColOrder(), lcolmd.getmColStep(), lcolmd.getmColRange());
				break;
				default :
					LOG.error(lcolmd.getmColType().value() + " is not supported now.");
					return false;
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
		if (mStoragePath.length() < 1 && !mNeedCache) {
			LOG.error("no cache, no storage ? for table: " + mtablemd.getmTableName());
			return;
		}
		
		long lRowCount = mtablemd.getmTableRowcount();
		if (lRowCount < 1) {
			LOG.error("row count should be larger than one for table: " + mtablemd.getmTableName());
			return;
		}
		if (mNeedCache) {
			
			
			
			
		} else {
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
			
			long lFlushCount = ;
			long lCurrentBuffered = 0;
			String lCurrentBatch = "";
			
			for (long index = 0; index < lRowCount; ++index) {
				
				lPrintWriter.println(generaterow());
				if (++lCurrentBuffered >= lFlushCount) {
					lPrintWriter.flush();
					lCurrentBuffered = 0;
				}
			}
			
			lPrintWriter.flush();
			lPrintWriter.close();
		}
		
	}

	private String genOneChunk(long irowcount) {
		
	}
	
}
