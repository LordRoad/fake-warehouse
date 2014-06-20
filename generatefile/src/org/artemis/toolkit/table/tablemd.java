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
package org.artemis.toolkit.table;

import java.util.ArrayList;
import java.util.List;

import org.artemis.toolkit.common.ignore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.annotations.SerializedName;

/**
 * tablemd lookup tabledata
 * tablemd.java is written at Jun 13, 2014
 * @author junli
 */
public class tablemd {
	private static final Logger LOG = LoggerFactory.getLogger(tablemd.class);

	@SerializedName("table_name") private String mTableName = null;
	@SerializedName("talbe_rowcount") private long mTableRowcount = 0;
	@SerializedName("table_slice") private long mTableSlice = 1;
	@SerializedName("table_cols") private List<columnmd> mColumns = new ArrayList<columnmd>();
	
	public tablemd(String itblname) {
		mTableName = itblname;
	}
	
	public long getmTableRowcount() {
		return mTableRowcount;
	}

	public void setmTableRowcount(long mTableRowcount) {
		this.mTableRowcount = mTableRowcount;
	}

	public String getmTableName() {
		return mTableName;
	}

	public void setmTableName(String mTableName) {
		this.mTableName = mTableName;
	}

	public long getmTableSlice() {
		return mTableSlice;
	}

	public void setmTableSlice(long mTableSlice) {
		this.mTableSlice = mTableSlice;
	}

	public columnmd getColumn(int icolumnIndex) {
		return mColumns.get(icolumnIndex);
	}
	
	public List<columnmd> getmColumns() {
		return mColumns;
	}

	public void setmColumns(List<columnmd> mColumns) {
		this.mColumns = mColumns;
	}
	
	public int getcolcount() {
		synchronized (mColumns) {
			return mColumns.size();
		}
	}
	
	public void insertnewcolumn(columnmd icolumndef) {
		synchronized (mColumns) {
			mColumns.add(icolumndef);
		}
	}

	public boolean iscolexisted(String icolumnname) {
		for (columnmd icolumn : mColumns) {
			if (icolumn.getmColName().compareTo(icolumnname) == 0) {
				return true;
			}
		}
		return false;
	}
	
}
