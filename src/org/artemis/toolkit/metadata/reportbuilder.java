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
package org.artemis.toolkit.metadata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.annotations.SerializedName;

/**
 * reportbuilder build report
 * reportbuilder.java is written at Jun 14, 2014
 * @author return_jun
 * @since 0.2
 */
public class reportbuilder {
	private static final Logger LOG = LoggerFactory.getLogger(reportbuilder.class);
	
	private tablemd mfacttable = null;
	private List<tablemd> mlookuptableset = new ArrayList<tablemd>();
	// private Map<Integer, ArrayList<Integer>> mRelationMap = new HashMap<Integer, ArrayList<Integer>>();
	
	public reportbuilder() {
		
	}
	
	public reportbuilder addFactTable(tablemd ifacttable) {
		mfacttable = ifacttable;
		return this;
	}
	
	public reportbuilder addLUTable(tablemd ilookuptable) {
		synchronized (mlookuptableset) {
			mlookuptableset.add(ilookuptable);
		}
		return this;
	}
	
	public reportmd build() {
		return new reportmd(mfacttable, mlookuptableset);
	}
	
}
