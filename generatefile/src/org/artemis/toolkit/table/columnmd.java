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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.annotations.SerializedName;

/**
 * columndef TODO
 * columndef.java is written at Jun 14, 2014
 * @author return_jun
 */
public class columnmd {
	private static final Logger LOG = LoggerFactory.getLogger(columnmd.class);
	
	@SerializedName("colname") private String mColName = null;
	@SerializedName("coltype") private datatype mColType = datatype.INVALID;
	
	/**
	 * if order is descend or ascend, it will NOT ignore range, or 5-10000, 5,6,7...
	 * otherwise, data will be generated based on range
	 */
	@SerializedName("colorder") private analyticsops.order mColOrder = analyticsops.order.Random;
	
	/**
	 * when user specifies columnmd oder, it should be added one by one or 
	 * odd, even or specified steps (such as 6, -6)
	 */
	@SerializedName("colstep") private int mColStep = 1;

	/**
	 * columnmd range: [1-10000), [2012-06-28~2014-06-15)
	 */
	@SerializedName("colrange") private String mColRange = null;
	
	public columnmd() {
		
	}
	
	public columnmd(String icolname, datatype idatatype) {
		this(icolname, idatatype, analyticsops.order.Random, null);
	}
	
	public columnmd(String icolname, datatype idatatype, analyticsops.order iorder, 
			String irange) {
		this(icolname, idatatype, iorder, irange, 1);
	}

	public columnmd(String icolname, datatype idatatype, analyticsops.order iorder, 
			String irange, int istep) {
		mColName = icolname;
		mColType = idatatype;
		mColOrder = iorder;
		mColRange = irange;
		mColStep = istep;
	}
	
	public String getmColName() {
		return mColName;
	}

	public void setmColName(String mColName) {
		this.mColName = mColName;
	}

	public datatype getmColType() {
		return mColType;
	}

	public void setmColType(datatype mColType) {
		this.mColType = mColType;
	}

	public analyticsops.order getmColOrder() {
		return mColOrder;
	}

	public void setmColOrder(analyticsops.order mColOrder) {
		this.mColOrder = mColOrder;
	}

	public String getmColRange() {
		return mColRange;
	}

	public void setmColRange(String mColRange) {
		this.mColRange = mColRange;
	}
	
	public int getmColStep() {
		return mColStep;
	}

	public void setmColStep(int mColStep) {
		this.mColStep = mColStep;
	}
	
}
