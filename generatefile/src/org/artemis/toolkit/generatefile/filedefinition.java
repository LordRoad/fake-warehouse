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
 * 
 * filedefinition TODO
 * filedefinition.java is written at Jun 15, 2014
 * @author return_jun
 */
public class filedefinition {
	
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
