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

import java.util.List;

import org.artemis.toolkit.metadata.columnmd;
import org.artemis.toolkit.metadata.tablemd;
import org.artemis.toolkit.table.analyticsops.order;
import org.artemis.toolkit.table.tabledata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * genlutable TODO
 * genlutable.java is written at Jun 15, 2014
 * @author junli
 */
public class genlutable {
	private static final Logger LOG = LoggerFactory.getLogger(genlutable.class);
			
	private final tablemd mlookuptable;
	private boolean mDone = false;
	private tabledata[] mtabledataset;
	private tablemd[] mjobtablesmd;
	
	public genlutable(tablemd ilookuptable) {
		mlookuptable = ilookuptable;
		int lColumnCount = mlookuptable.getcolcount();
		if (lColumnCount < 1) {
			mDone = true;
		} else {
			int lTableSlice = (int) mlookuptable.getmTableSlice();
			mtabledataset = new tabledata[lTableSlice > 0 ? lTableSlice : 1];
		}
		
	}
	
	protected void initjobtablemd() {
		int lTableSlice = (int) mlookuptable.getmTableSlice();
		mjobtablesmd = new tablemd[lTableSlice > 0 ? lTableSlice : 1]; 
		if (mjobtablesmd.length == 1) {
			mjobtablesmd[0] = mlookuptable;
			return;
		}
		
		long lTotalRowCount = mlookuptable.getmTableRowcount();
		long lOneBatchRC = lTotalRowCount / lTableSlice;
		long lLastBatchRC = lTotalRowCount % lTableSlice;
		
		// set rows
		for (int iter = 0; iter < lTableSlice - 1; ++iter) {
			mjobtablesmd[iter].setmTableRowcount(lOneBatchRC);
		}
		mjobtablesmd[lTableSlice - 1].setmTableRowcount(lLastBatchRC);
		
		// set columns
		List<columnmd> lColsMD = mlookuptable.getmColumns();
		for (columnmd lcolumnmd : lColsMD) {
			columnmd lNewColumnMD = new columnmd();
			lNewColumnMD.setmColName(lcolumnmd.getmColName());
			
			for (int iter = 0; iter < lTableSlice; ++iter) {
				
				if (lcolumnmd.getmColOrder() == order.Random) {
					
				}
			}
			
		}
		
	}
	
	protected void genjobs() {
		
		
	}
	
}
