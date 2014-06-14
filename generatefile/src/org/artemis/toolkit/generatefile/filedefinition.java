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
package org.artemis.toolkit.generatefile;

import java.util.ArrayList;
import java.util.List;

/**
 * @author return_jun
 *
 */
public class filedefinition {
	
	public class ColumnDataType {
		public static final int MSI_TF = 3;
		public static final int MSI_SHORT = 21;
		public static final int MSI_INTEGER = 1;
		public static final int MSI_LONG = 22;
		public static final int MSI_FLOAT = 7;
		public static final int MSI_DOUBLE = 6;
		public static final int MSI_DATE = 14;
		public static final int MSI_TIME = 15;
		public static final int MSI_DATETIME = 16;
		public static final int MSI_STRING = 9;
		public static final int MSI_MBCS_STRING = 23;
		public static final int MSI_BINARY = 11;
		public static final int MSI_BIGDECIMAL = 30;
		public static final int MSI_CELLFMTDATA = 31;
		public static final int MSI_INVALID = -1;
		public static final int MSI_UNSIGNED_INTEGER = 2;
		public static final int MSI_UTF8_STRING = 33;
		public static final int MSI_LONGLONG = 34;
	}
	
	// public String mBasicFileName = "";
	public int mChunks = 1;
	public String mSeperator = ",";
	public long mRowsCount = 0;
	public String mFilePath = "";
	public List<Integer> mColumnTypeArray = new ArrayList<Integer>();
	public List<Long> mColumnTypeMeta = new ArrayList<Long>();
	
	public void InsertNewColumn(int iColumnType, long iColumnMeta) {
		synchronized (this) {
			mColumnTypeArray.add(new Integer(iColumnType));
			mColumnTypeMeta.add(new Long(iColumnMeta));
		}
	}
	
	public void SetFilePath(String iFilePath) {
		mFilePath = iFilePath;
	}
	
	public void SetRowCount(long iRowCount) {
		mRowsCount = iRowCount;
	}
	
	public void SetSeperator(String iSeperator) {
		mSeperator = iSeperator;
	}
	
	
}
