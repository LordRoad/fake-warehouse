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
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.artemis.toolkit.common.jobexecutor;
import org.artemis.toolkit.common.sysconfig;
import org.artemis.toolkit.metadata.columnmd;
import org.artemis.toolkit.metadata.tablemd;
import org.artemis.toolkit.table.analyticsops.order;
import org.artemis.toolkit.table.datarange;
import org.artemis.toolkit.table.newdatatype;
import org.artemis.toolkit.table.tabledata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * gentable basic generate table process
 * gentable.java is written at Jun 19, 2014
 * @author junli
 * @since 0.2
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
		
		List<columnmd> lColsMD = mlookuptable.getmColumns();
		if (lColsMD == null || lColsMD.size() == 0) {
			LOG.error("column is empty? r u kidding me?");
			return false;
		}
		
		if (mjobtablesmd.length == 1) {
			mjobtablesmd[0] = mlookuptable;
			
			// check if contains any extra data type
			for (int iter = 0; iter < lColsMD.size(); ++iter) {
				columnmd lcolumnmd = lColsMD.get(iter);
				newdatatype lnewdatatype = lcolumnmd.getmColType();
				if (!lnewdatatype.isInnerDT()) {
					try {
						String lExtraDataPath = extraconfig.instance().getExtraDataPath(lnewdatatype.value());
						extradatapool.instance().initdata(lnewdatatype.value(), lExtraDataPath);
						extradata lextradata = extradatapool.instance().getExtraData(lnewdatatype.value());
						
						assert lextradata != null;
					} catch (IOException e) {
						LOG.error(e.getLocalizedMessage());
						return false;
					}
				}
			}

			return true;
		}
		
		// get max data range
		/**
		 * for several linear columns, i think it will be ok to only check primitive type
		 * (short, int, double, long) and find out the largest range to split the rows. but
		 * 
		 * here is a example:
		 * int: 1~365
		 * long: 1~10000
		 * slice: 32
		 * 
		 * int: 11 number range for first slice
		 * long: 312 number range for first slice which means slice one has 312 rows.
		 * but column int will be 1,2,3,4,5,6,7,8,9,10,null,null...
		 * 
		 * that's just handle it this way
		 */
		long lTotalRowCount = mlookuptable.getmTableRowcount();
		long lMaxRange = 0;
		long lCurrentRange = 0;
		int lBenchColumnIndex = -1;
		for (int iter = 0; iter < lColsMD.size(); ++iter) {
			columnmd lcolumnmd = lColsMD.get(iter);
			if (lcolumnmd.getmColOrder() == order.Random) {
				continue;
			}
			newdatatype lnewdatatype = lcolumnmd.getmColType();
			
			Class<?> lClass = null;
			if (lnewdatatype.value().compareToIgnoreCase(newdatatype.innerDT.sShort) == 0) {
				lClass = Short.class;
			}
			else if (lnewdatatype.value().compareToIgnoreCase(newdatatype.innerDT.sInt) == 0) {
				lClass = Integer.class;
			}
			else if (lnewdatatype.value().compareToIgnoreCase(newdatatype.innerDT.sDouble) == 0) {
				lClass = Double.class;
			}
			else if (lnewdatatype.value().compareToIgnoreCase(newdatatype.innerDT.sLong) == 0) {
				lClass = Long.class;
			} else {
				LOG.error("only support short, int, double, long as non random column");
				return false;
			}
			
			gendata lgendata = 
					gendata.getGenerator(lClass, 
							lcolumnmd.getmColOrder(), lcolumnmd.getmColStep(), lcolumnmd.getmColRange());
			lCurrentRange = Long.parseLong(lgendata.getUpperBound().toString());
			if (lCurrentRange > lMaxRange) {
				lMaxRange = lCurrentRange;
				lBenchColumnIndex = iter;
			}
		}
		
		
		// set columns
		String[][] lColumnsSet = new String[lColsMD.size()][];
		int lLinearColumnCount = 0;
		datarange lBenchDataRange = null;
		for (int iter = 0; iter < lColsMD.size(); ++iter) {
			columnmd lcolumnmd = lColsMD.get(iter);
			if (lcolumnmd.getmColOrder() != order.Random) {
				lLinearColumnCount++;
			}
			
			datarange ldatarange = new datarange(lcolumnmd.getmColRange());
			newdatatype lnewdatatype = lcolumnmd.getmColType();
			if (lnewdatatype.value().compareToIgnoreCase(newdatatype.innerDT.sBoolean) == 0) {
				lColumnsSet[iter] = ldatarange.splitRange(
						new Boolean(true), lTotalRowCount, lcolumnmd.getmColOrder(), lTableSlice, lcolumnmd.getmColStep());
			}
			else if (lnewdatatype.value().compareToIgnoreCase(newdatatype.innerDT.sShort) == 0) {
				lColumnsSet[iter] = ldatarange.splitRange(
						new Short((short) 0), lTotalRowCount, lcolumnmd.getmColOrder(), lTableSlice, lcolumnmd.getmColStep());
			}
			else if (lnewdatatype.value().compareToIgnoreCase(newdatatype.innerDT.sInt) == 0) {
				lColumnsSet[iter] = ldatarange.splitRange(
						new Integer(0), lTotalRowCount, lcolumnmd.getmColOrder(), lTableSlice, lcolumnmd.getmColStep());
			}
			else if (lnewdatatype.value().compareToIgnoreCase(newdatatype.innerDT.sDouble) == 0) {
				lColumnsSet[iter] = ldatarange.splitRange(
						new Double(2.0), lTotalRowCount, lcolumnmd.getmColOrder(), lTableSlice, lcolumnmd.getmColStep());
			}
			else if (lnewdatatype.value().compareToIgnoreCase(newdatatype.innerDT.sLong) == 0) {
				lColumnsSet[iter] = ldatarange.splitRange(
						new Long(1), lTotalRowCount, lcolumnmd.getmColOrder(), lTableSlice, lcolumnmd.getmColStep());
			}
			else if (lnewdatatype.value().compareToIgnoreCase(newdatatype.innerDT.sDate) == 0) {
				lColumnsSet[iter] = ldatarange.splitRange(
						new Date(), lTotalRowCount, lcolumnmd.getmColOrder(), lTableSlice, lcolumnmd.getmColStep());
			}
			else if (lnewdatatype.value().compareToIgnoreCase(newdatatype.innerDT.sString) == 0) {
				lColumnsSet[iter] = ldatarange.splitRange(
						new String(), lTotalRowCount, lcolumnmd.getmColOrder(), lTableSlice, lcolumnmd.getmColStep());
			} else {
				// check if user defined
				LOG.info("maybe user defined data set: " + lcolumnmd.getmColName());
				
				try {
					String lExtraDataPath = extraconfig.instance().getExtraDataPath(lnewdatatype.value());
					if (!extradatapool.instance().initdata(lnewdatatype.value(), lExtraDataPath)) {
						return false;
					}
				} catch (IOException e) {
					LOG.error(e.getLocalizedMessage());
					LOG.error("column type " + lcolumnmd.getmColType().value() + " is not supported.");
					return false;
				}
				
				// we will not split any range for extra data
				lColumnsSet[iter] = null;
			}
			
			if (iter == lBenchColumnIndex) {
				lBenchDataRange = ldatarange;
			}
		}
		
		if (lLinearColumnCount > 1) {
			LOG.warn("more than one column is linear not random, which may cause some incorrect column data" +
					"under some circumstance ");
		}
		
		// all random, set rows
		long lOneBatchRC = lTotalRowCount / lTableSlice;
		long lLastBatchRC = lOneBatchRC + lTotalRowCount % lTableSlice;
		
		if (lBenchDataRange != null) {
			lOneBatchRC = lBenchDataRange.getmStandardSliceRowCount();
			lLastBatchRC = lBenchDataRange.getmLastSliceRowCount();
		}
		for (int iter = 0; iter < lTableSlice - 1; ++iter) {
			mjobtablesmd[iter].setmTableRowcount(lOneBatchRC);
		}
		mjobtablesmd[lTableSlice - 1].setmTableRowcount(lLastBatchRC);	
		
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
