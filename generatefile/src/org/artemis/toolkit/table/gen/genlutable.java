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

import org.artemis.toolkit.table.tabledata;
import org.artemis.toolkit.table.tablemd;
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
	
	protected void genjobs() {
		int lTableSlice = (int) mlookuptable.getmTableSlice();
		tablemd[] ljobtablesmd = new tablemd[lTableSlice > 0 ? lTableSlice : 1]; 
		
		for () {
			
		}
	}
	
}
