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

import org.artemis.toolkit.common.ignore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.annotations.SerializedName;

/**
 * report TODO
 * report.java is written at Jun 14, 2014
 * @author return_jun
 */
public class reportmd {
	private static final Logger LOG = LoggerFactory.getLogger(reportmd.class);
	
	@SerializedName("LU_table") private List<tablemd> mlookuptableset = new ArrayList<tablemd>();
	@SerializedName("fact_table") private tablemd mfacttable = null;
	@SerializedName("LU_fact_relation") private List<Integer> mLUColsIndex = new ArrayList<Integer>();
	
	//@SerializedName("relationship") private Map<Integer, ArrayList<Integer>> mRelationMap = new HashMap<Integer, ArrayList<Integer>>();
	
	public reportmd(tablemd ifacttable, List<tablemd> ilookuptableset) {
		mfacttable = ifacttable;
		mlookuptableset = ilookuptableset;
	}
	
	public tablemd getmfacttable() {
		return mfacttable;
	}
	
	public void setmfacttable(tablemd mfacttable) {
		this.mfacttable = mfacttable;
	}
	
	public List<tablemd> getmlookuptableset() {
		return mlookuptableset;
	}
	
	public void setmlookuptableset(List<tablemd> mlookuptableset) {
		this.mlookuptableset = mlookuptableset;
	}
	
	/**
	 * columnmd mapping from fact tabledata
	 * @param idependency
	 */
	public void adddependency(int[] idependency) {
		if (idependency != null) {
			int lColsCountInFactTable = mfacttable.getcolcount();
			for (int iter = 0; iter < idependency.length; ++iter) {
				if (idependency[iter] >= lColsCountInFactTable ) {
					LOG.error("columnmd mapping is invalid. columnmd " + Integer.toString(idependency[iter]) 
							+ " is out of range " + Integer.toString(lColsCountInFactTable));
					throw new RuntimeException("columnmd mapping is invalid");
				}
				mLUColsIndex.add(idependency[iter]);
			}
		}
	}
	
	
}
