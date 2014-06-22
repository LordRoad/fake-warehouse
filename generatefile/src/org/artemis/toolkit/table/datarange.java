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

import java.util.Date;

import org.artemis.toolkit.common.sysconfig;
import org.artemis.toolkit.table.analyticsops.order;
import org.artemis.toolkit.table.gen.gendata;

/**
 * datarange data range
 * datarange.java is written at Jun 16, 2014
 * @author junli
 */
public class datarange {
	private String mRangeRegex = sysconfig.sDataRangeSplit;
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
	
	/**
	 * split range based on slice count.
	 * @param dataTypeClass
	 * @param iorder
	 * @param islice
	 * @param istep
	 * @return
	 */
	public <T> String[] splitRange(T dataTypeClass, order iorder, int islice, int istep) {
		if (islice < 1) {
			return null;
		}
		
		String[] lSplitRange = new String[islice];
		if (iorder == order.Random ||
				dataTypeClass.getClass() == Boolean.class ||
				dataTypeClass.getClass() == String.class) {
			for (int iter = 0; iter < islice; ++iter) {
				lSplitRange[iter] = this.getmOriginalDataRange();
			}
			return lSplitRange;
		} 
		
		gendata lgendata = 
				gendata.getGenerator(dataTypeClass.getClass(), iorder, istep, this.getmOriginalDataRange());
		if (lgendata == null) {
			return null;
		}
		
		long lLowerBound = Long.parseLong(lgendata.getLowerBound().toString());
		long lUpperBound = Long.parseLong(lgendata.getUpperBound().toString());
		if (dataTypeClass.getClass() == Date.class.getClass()) {
			
		} else {
			// short, int, double, long
			long lOneRange = (long) ((lUpperBound - lLowerBound) / islice);
			
			if (iorder == order.Ascend) {
				++lOneRange;
				long lCurrentSliceLowerBound = lLowerBound;
				for (int iter = 0; iter < islice - 1; ++iter) {
					long lCurrentSliceUpperBound = lCurrentSliceLowerBound + lOneRange;
					
					lSplitRange[iter] = Long.toString(lCurrentSliceLowerBound) + mRangeRegex
							+ Long.toString(lCurrentSliceUpperBound);
					
					lCurrentSliceLowerBound = lCurrentSliceUpperBound;
				}
				lSplitRange[islice - 1] = Long.toString(lCurrentSliceLowerBound) + mRangeRegex
						+ Long.toString(lUpperBound);
				
			} else if (iorder == order.Descend) {
				--lOneRange;
				long lCurrentSliceUpperBound = lUpperBound;
				for (int iter = 0; iter < islice - 1; ++iter) {
					long lCurrentSliceLowerBound = lCurrentSliceUpperBound - lOneRange;
					
					lSplitRange[iter] = Long.toString(lCurrentSliceLowerBound) + mRangeRegex
							+ Long.toString(lCurrentSliceUpperBound);
					
					lCurrentSliceUpperBound = lCurrentSliceLowerBound;
				}
				lSplitRange[islice - 1] = Long.toString((Long)lLowerBound) + mRangeRegex
						+ Long.toString(lCurrentSliceUpperBound);
			}
		}
		
		return lSplitRange;
	}
	
}

