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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.annotations.SerializedName;

/**
 * facttable TODO
 * facttable.java is written at Jun 14, 2014
 * @author return_jun
 */
public class facttable {
	private static final Logger LOG = LoggerFactory.getLogger(facttable.class);

	@SerializedName("facttbl_name") private String mFactTableName = null;
	@SerializedName("facttbl_cols") private List<column> mColumns = new ArrayList<column>();
	
	public facttable(String itblname) {
		mFactTableName = itblname;
	}
	
	public void insertnewcolumn(column icolumndef) {
		synchronized (mColumns) {
			mColumns.add(icolumndef);
		}
	}
	
	public String getmFactTableName() {
		return mFactTableName;
	}

	public void setmFactTableName(String mFactTableName) {
		this.mFactTableName = mFactTableName;
	}

	public boolean iscolexisted(String icolumnname) {
		for (column icolumn : mColumns) {
			if (icolumn.getmColName().compareTo(icolumnname) == 0) {
				return true;
			}
		}
		return false;
	}
	
	public int getcolcount() {
		synchronized (mColumns) {
			return mColumns.size();
		}
	}
	
}
