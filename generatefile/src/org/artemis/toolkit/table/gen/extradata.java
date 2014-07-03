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

import com.google.gson.annotations.SerializedName;

/**
 * extradata extra data pool
 * extradata.java is written at Jun 30, 2014
 * @author junli
 */
public final class extradata {
	@SerializedName("data") private String[] mRowData = null;
	
	public extradata(int icellcount) {
		if (icellcount < 1) {
			throw new RuntimeException("cell count should be an positive number");
		}
		mRowData = new String[icellcount];
	}
	
	public extradata(String[] irowdata) {
		if (irowdata == null) {
			throw new RuntimeException("row data should not be null");
		}
		mRowData = irowdata;
	}
	
	public final int size() {
		return mRowData.length;
	}
	
	public String[] getRow() {
		return mRowData;
	}
	
	public final void setcell(int index, String icell) {
//		if (index < 0 || index >= mRowData.length) {
//			throw new RuntimeException("index is out of range");
//		}
		mRowData[index] = icell;
	}
	
	public final String getcell(int index) {
//		if (index < 0 || index >= mRowData.length) {
//			throw new RuntimeException("index is out of range");
//		}
		return mRowData[index];
	}
}
