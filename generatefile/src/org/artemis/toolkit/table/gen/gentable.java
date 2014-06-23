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

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.artemis.toolkit.common.jobexecutor;
import org.artemis.toolkit.common.sysconfig;
import org.artemis.toolkit.metadata.columnmd;
import org.artemis.toolkit.metadata.tablemd;
import org.artemis.toolkit.table.datarange;
import org.artemis.toolkit.table.tabledata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * gentable basic generate table process
 * gentable.java is written at Jun 19, 2014
 * @author junli
 */
public class gentable {
	private static final Logger LOG = LoggerFactory.getLogger(genlutable.class);
	
	private final tablemd mlookuptable;
	private String mStoragePath;
	private boolean mDone = false;
	private boolean mNeedCache = false;
	
	private tabledata[] mtabledataset;
	private tablemd[] mjobtablesmd;
	
	private jobexecutor mjobexecutor;
	private genjob[] mgenjob;
	private int[] mNeedCacheIndexs;
	
	public gentable(tablemd ilookuptable, String iStoragePath, boolean iNeedCache) {
		mlookuptable = ilookuptable;
		int lColumnCount = mlookuptable.getcolcount();
		if (lColumnCount < 1) {
			mDone = true;
		} else {
			int lTableSlice = (int) mlookuptable.getmTableSlice();
			mtabledataset = new tabledata[lTableSlice > 0 ? lTableSlice : 1];
		}
		mStoragePath = iStoragePath + File.separator + mlookuptable.getmTableName();
		mjobexecutor = new jobexecutor();
		mNeedCache = iNeedCache;
	}
	
	protected boolean initjobtablemd() {
		int lTableSlice = (int) mlookuptable.getmTableSlice();
		mjobtablesmd = new tablemd[lTableSlice > 0 ? lTableSlice : 1];
		for (int iter = 0; iter < mjobtablesmd.length; ++iter) {
			mjobtablesmd[iter] = new tablemd(mlookuptable.getmTableName());
		}
		if (mjobtablesmd.length == 1) {
			mjobtablesmd[0] = mlookuptable;
			return true;
		}
		
		long lTotalRowCount = mlookuptable.getmTableRowcount();
		long lOneBatchRC = lTotalRowCount / lTableSlice;
		long lLastBatchRC = lOneBatchRC + lTotalRowCount % lTableSlice;
		
		// set rows
		for (int iter = 0; iter < lTableSlice - 1; ++iter) {
			mjobtablesmd[iter].setmTableRowcount(lOneBatchRC);
		}
		mjobtablesmd[lTableSlice - 1].setmTableRowcount(lLastBatchRC);
		
		// set columns
		List<columnmd> lColsMD = mlookuptable.getmColumns();
		if (lColsMD == null || lColsMD.size() == 0) {
			LOG.error("column is empty? r u kidding me?");
			return false;
		}
		
		String[][] lColumnsSet = new String[lColsMD.size()][];
		for (int iter = 0; iter < lColsMD.size(); ++iter) {
			columnmd lcolumnmd = lColsMD.get(iter);
			datarange ldatarange = new datarange(lcolumnmd.getmColRange());
			
			switch (lcolumnmd.getmColType()) {
			case BOOLEAN :
				lColumnsSet[iter] = ldatarange.splitRange(new Boolean(true), lcolumnmd.getmColOrder(), lTableSlice, lcolumnmd.getmColStep());
				break;
			case SHORT :
				lColumnsSet[iter] = ldatarange.splitRange(new Short((short) 0), lcolumnmd.getmColOrder(), lTableSlice, lcolumnmd.getmColStep());
				break;
			case INT :
				lColumnsSet[iter] = ldatarange.splitRange(new Integer(0), lcolumnmd.getmColOrder(), lTableSlice, lcolumnmd.getmColStep());
				break;
			case DOUBLE :
				lColumnsSet[iter] = ldatarange.splitRange(new Double(2.0), lcolumnmd.getmColOrder(), lTableSlice, lcolumnmd.getmColStep());
				break;
			case LONG :
				lColumnsSet[iter] = ldatarange.splitRange(new Long(1), lcolumnmd.getmColOrder(), lTableSlice, lcolumnmd.getmColStep());
				break;
			case DATE :
				lColumnsSet[iter] = ldatarange.splitRange(new Date(), lcolumnmd.getmColOrder(), lTableSlice, lcolumnmd.getmColStep());
				break;
			case STRING :
				lColumnsSet[iter] = ldatarange.splitRange(new String(), lcolumnmd.getmColOrder(), lTableSlice, lcolumnmd.getmColStep());
				break;
			default:
				lColumnsSet[iter] = null;
				LOG.error("column type " + lcolumnmd.getmColType().value() + " is not supported.");
				return false;
			}
		}
		for (int jter = 0; jter < lTableSlice; ++jter) {
			for (int iter = 0; iter < lColsMD.size(); ++iter) {
				columnmd lcolumnmd = lColsMD.get(iter);			
				columnmd lNewColumnMD = new columnmd();
	
				lNewColumnMD.setmColName(lcolumnmd.getmColName());
				lNewColumnMD.setmColOrder(lcolumnmd.getmColOrder());
				lNewColumnMD.setmColType(lcolumnmd.getmColType());
				lNewColumnMD.setmColStep(lcolumnmd.getmColStep());
				lNewColumnMD.setmColRange(lColumnsSet[iter] != null ? lColumnsSet[iter][jter] : "");
				mjobtablesmd[jter].insertColumn(lNewColumnMD);
			}
		}
		
		return true;
	}
	
	protected boolean genjobs() {
		if (mDone || !initjobtablemd()) {
			return false;
		}
		
		int lJobs = mjobtablesmd.length;
		String[] lStoragepath = new String[lJobs];
		if (lJobs == 1) {
			lStoragepath[0] = mStoragePath + sysconfig.sFileExtension;
		} else {
			for (int iter = 0; iter < lJobs; ++iter) {
				lStoragepath[iter] = mStoragePath + "_" + Integer.toString(iter) + sysconfig.sFileExtension;
			}
		}
		
		mgenjob = new genjob[lJobs];
		for (int iter = 0; iter < lJobs; ++iter) {
			mgenjob[iter] = new genjob(mjobtablesmd[iter], lStoragepath[iter], mNeedCache, mNeedCacheIndexs);
		}
		return true;
	}
	
	public void genTableData() {
		if (!genjobs()) {
			return;
		}
		
		LOG.trace("start generating table data");
		for (genjob igenjob : mgenjob) {
			mjobexecutor.execute(igenjob);
		}
		
	}
	
	public boolean wait(long timeout, TimeUnit unit) {
		return mjobexecutor.wait(timeout, unit);
	}
	
}
