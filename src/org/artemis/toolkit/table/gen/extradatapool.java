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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.artemis.toolkit.common.configparser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * extradatapool TODO
 * extradatapool.java is written at Jul 1, 2014
 * @author junli
 */
public class extradatapool {
	private static final Logger LOG = LoggerFactory.getLogger(extradatapool.class);
	private Map<String, extradata> mExtraDataMap = new HashMap<String, extradata>();
	private static extradatapool sextradatapool = new extradatapool();
	
	private extradatapool() {
		
	}
	
	public static extradatapool instance() {
		return sextradatapool;
	}
	
	public boolean initdata(String icolumname, String ipath) {
		configparser lconfigparser;
		try {
			lconfigparser = new configparser(ipath);
			
			extradata lExtraData = lconfigparser.deserialize(extradata.class);
			if (lExtraData == null) {
				return false;
			}
			mExtraDataMap.put(icolumname, lExtraData);
			return true;
		} catch (IOException e) {
			LOG.error(e.getLocalizedMessage());
		}
		return false;
	}
	
	public void releasedata(String icolumname) {
		if (mExtraDataMap.containsKey(icolumname)) {
			mExtraDataMap.remove(icolumname);
		}
	}
	
	public void removeall() {
		mExtraDataMap.clear();
	}
	
	public final extradata getExtraData(String icolumname) {
		return mExtraDataMap.get(icolumname);
	}
	
}
