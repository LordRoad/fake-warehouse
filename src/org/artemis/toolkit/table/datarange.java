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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * datarange data range
 * datarange.java is written at Jun 16, 2014
 * @author junli
 * @since 0.2
 */
public class datarange {
	private static final Logger LOG = LoggerFactory.getLogger(datarange.class);
	
	private String mRangeRegex = sysconfig.sDataRangeSplit;
	private String mOriginalDataRange;
	private String[] mRangeOptions;
	
	private long mStandardSliceRowCount = -1;
	private long mLastSliceRowCount = -1;
	
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
	 * -1 means current data range is not split
	 * @return
	 */
	public long getmStandardSliceRowCount() {
		return mStandardSliceRowCount;
	}

	/**
	 * -1 means current data range is not split
	 * @return
	 */
	public long getmLastSliceRowCount() {
		return mLastSliceRowCount;
	}

	/**
	 * split range based on slice count.
	 * @param dataTypeClass
	 * @param irowcount, total row count to adapter data range
	 * @param iorder
	 * @param islice
	 * @param istep
	 * @return
	 */
	public <T> String[] splitRange(T dataTypeClass, long irowcount, order iorder, int islice, int istep) {
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
		if (irowcount > 0 && iorder != order.Random && lUpperBound - lLowerBound > irowcount) {
			if (iorder == order.Ascend) {
				lUpperBound = lLowerBound + irowcount;
			} else {
				lLowerBound = lUpperBound - irowcount;
			}
		}
		
		if (dataTypeClass.getClass() == Date.class.getClass()) {
			
		} else {
			// short, int, double, long
			long lOneRange = (long) ((lUpperBound - lLowerBound) / islice);
			
			LOG.debug("split range: ");
			++lOneRange;
			if (iorder == order.Ascend) {
				long lCurrentSliceLowerBound = lLowerBound;
				for (int iter = 0; iter < islice - 1; ++iter) {
					long lCurrentSliceUpperBound = lCurrentSliceLowerBound + lOneRange;
					
					lSplitRange[iter] = Long.toString(lCurrentSliceLowerBound) + mRangeRegex
							+ Long.toString(lCurrentSliceUpperBound);
					LOG.debug("range: " + lSplitRange[iter]);
					
					lCurrentSliceLowerBound = lCurrentSliceUpperBound;
				}
				lSplitRange[islice - 1] = Long.toString(lCurrentSliceLowerBound) + mRangeRegex
						+ Long.toString(lUpperBound);
				
				mLastSliceRowCount = lUpperBound - lCurrentSliceLowerBound;
				
				LOG.debug("range: " + lSplitRange[islice - 1]);
				
			} else if (iorder == order.Descend) {
				long lCurrentSliceUpperBound = lUpperBound;
				for (int iter = 0; iter < islice - 1; ++iter) {
					long lCurrentSliceLowerBound = lCurrentSliceUpperBound - lOneRange;
					
					lSplitRange[iter] = Long.toString(lCurrentSliceLowerBound) + mRangeRegex
							+ Long.toString(lCurrentSliceUpperBound);
					LOG.debug("range: " + lSplitRange[iter]);
					
					lCurrentSliceUpperBound = lCurrentSliceLowerBound;
				}
				lSplitRange[islice - 1] = Long.toString((Long)lLowerBound) + mRangeRegex
						+ Long.toString(lCurrentSliceUpperBound);
				
				mLastSliceRowCount = lCurrentSliceUpperBound - lLowerBound;
				
				LOG.debug("range: " + lSplitRange[islice - 1]);
			}
			
			mStandardSliceRowCount = lOneRange;
		}
		
		return lSplitRange;
	}
	
}

