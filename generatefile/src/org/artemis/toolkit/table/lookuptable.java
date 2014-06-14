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
 * lookuptable TODO
 * lookuptable.java is written at Jun 13, 2014
 * @author junli
 */
public class lookuptable {
	private static final Logger LOG = LoggerFactory.getLogger(lookuptable.class);

	@SerializedName("lookuptbl_name") private String mLUTableName = null;
	@SerializedName("lookuptbl_cols_index") private List<Integer> mLUColsIndex = new ArrayList<Integer>();
	@ignore private facttable mfacttable = null;
	
	public lookuptable(String itblname, facttable ifacttable) {
		mLUTableName = itblname;
		mfacttable = ifacttable;
	}
	
	public void settblname(String iLUTblName) {
		mLUTableName = iLUTblName;
	}
	
	public String gettblname() {
		return mLUTableName;
	}
	
	public void setfactable(facttable ifacttable) {
		mfacttable = ifacttable;
	}
	
	public List<Integer> getmLUColsIndex() {
		return mLUColsIndex;
	}

	public void setmLUColsIndex(List<Integer> mLUColsIndex) {
		this.mLUColsIndex = mLUColsIndex;
	}

	/**
	 * column mapping from fact table
	 * @param idependency
	 */
	public void adddependency(int[] idependency) {
		if (idependency != null) {
			int lColsCountInFactTable = mfacttable.getcolcount();
			for (int iter = 0; iter < idependency.length; ++iter) {
				if (idependency[iter] >= lColsCountInFactTable ) {
					LOG.error("column mapping is invalid. column " + Integer.toString(idependency[iter]) 
							+ " is out of range " + Integer.toString(lColsCountInFactTable));
					throw new RuntimeException("column mapping is invalid");
				}
				mLUColsIndex.add(idependency[iter]);
			}
		}
	}
	
}
