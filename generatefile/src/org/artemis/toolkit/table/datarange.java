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

/**
 * datarange data range
 * datarange.java is written at Jun 16, 2014
 * @author junli
 */
public class datarange {
	private String mRangeRegex = "~";
	private String mOriginalDataRange;
	private String[] mRangeOptions;
	
	public datarange(String idatarange) {
		mOriginalDataRange = idatarange == null ? "" : idatarange;
		mRangeOptions = mOriginalDataRange.split(mRangeRegex);
	}

	public String getmRangeRegex() {
		return mRangeRegex;
	}

	public void setmRangeRegex(String mRangeRegex) {
		this.mRangeRegex = mRangeRegex;
	}

	public String getmOriginalDataRange() {
		return mOriginalDataRange;
	}

	public String[] getmRangeOptions() {
		return mRangeOptions;
	}

	public String getOption(int index) {
		return (index >= 0 && index < mRangeOptions.length) ? mRangeOptions[index] : null; 
	}
	
	public int getOptionCount() {
		return mRangeOptions.length;
	}
	
}
