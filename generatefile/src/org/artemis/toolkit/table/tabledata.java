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
 * tabledata TODO
 * tabledata.java is written at Jun 14, 2014
 * @author return_jun
 */
public class tabledata {
	private static final Logger LOG = LoggerFactory.getLogger(tabledata.class);

	private String[][] mColumnsData;
	
	public tabledata(int iColumnCount) {
		mColumnsData = new String[iColumnCount][];
	}

	public void flushDataIntoDisk(String iStoragePath) {
		
	}
	
	public void putColumnData(int iColumnIndex, String [] iColumnData) {
		if (iColumnData == null) {
			throw new RuntimeException();
			LOG.error("column data is null for lookup tabledata: " + mlookuptable.getmTableName());
			return;
		}
		if (iColumnIndex < 0 || iColumnIndex >= mlookuptable.getColumnCount()) {
			LOG.error("column data is null for lookup tabledata: " + mlookuptable.getmTableName());
			return;
		}
		mColumnsData[iColumnIndex] = iColumnData;
	}
	
}