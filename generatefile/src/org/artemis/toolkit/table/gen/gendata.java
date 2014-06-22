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

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.apache.commons.math3.random.RandomDataGenerator;
import org.artemis.toolkit.table.analyticsops;
import org.artemis.toolkit.table.datarange;
import org.artemis.toolkit.table.analyticsops.order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class NoMoreDataException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2554566557290649125L;

}

/**
 * gendata generate data based on columnmd definition
 * gendata.java is written at Jun 16, 2014
 * @author junli
 */
public abstract class gendata {
	protected static final Logger LOG = LoggerFactory.getLogger(gendata.class);
	
	protected order mOrder;
	protected int mStep;
	protected datarange mDataRange;
	
	/**
	 * accelerate generating speed
	 * 0 means random
	 * 1 means ascend
	 * 2 means descend
	 */
	protected int mOrderOps = 0;
	
	public gendata(order iorder, int istep, String irange) {
		mOrder = iorder;
		mStep = istep;
		mDataRange = new datarange(irange);
		if (mOrder == analyticsops.order.Ascend) {
			mOrderOps = 1;
		}
		if (mOrder == analyticsops.order.Descend) {
			mOrderOps = 2;
		}
	}

	public order getmOrder() {
		return mOrder;
	}

	public void setmOrder(order mOrder) {
		this.mOrder = mOrder;
	}

	public int getmStep() {
		return mStep;
	}

	public void setmStep(int mStep) {
		this.mStep = mStep;
	}

	public String getmDataRange() {
		return mDataRange.getmOriginalDataRange();
	}

	/**
	 * generate one item
	 * @return string, item to string
	 * @throws NoMoreDataException, if all data has been fetched from data pool
	 */
	public abstract String generateOneItem();
	
	public abstract Object getLowerBound();
	
	public abstract Object getUpperBound();
	
	public static gendata getGenerator(Class<?> dataTypeName, order iorder, int istep, String irange) {
		if (dataTypeName == Boolean.class) {
			return new genBoolean(iorder, istep, irange);
		} 
		if (dataTypeName == Short.class) {
			return new genShort(iorder, istep, irange);
		}
		if (dataTypeName == Integer.class) {
			return new genInteger(iorder, istep, irange);
		}
		if (dataTypeName == Long.class) {
			return new genLong(iorder, istep, irange);
		}
		if (dataTypeName == Double.class) {
			return new genDouble(iorder, istep, irange);
		}
		if (dataTypeName == Date.class) {
			return new genDate(iorder, istep, irange);
		}
		if (dataTypeName == String.class) {
			return new genString(iorder, istep, irange);
		}
		if (dataTypeName == BigDecimal.class) {
			return new genBigDecimal(iorder, istep, irange);
		}
		LOG.error("now " + dataTypeName.getSimpleName() + " is not supported.");
		return null;
	}

}

/**
 * boolean: ignore order, step, range should be: 0 or 1 or 0~1
 */
class genBoolean extends gendata {
	private int mbooltype = 1;
	private Random mRandom = new Random();
	
	public genBoolean(order iorder, int istep, String irange) {
		super(iorder, istep, irange);
		if (mDataRange.getOptionCount() == 1) {
			try {
				mbooltype = Integer.parseInt(mDataRange.getOption(0));
			} catch (NumberFormatException e) {
				LOG.warn(e.getLocalizedMessage());
			}
		} else {
			mbooltype = 2;
		}
	}

	@Override
	public String generateOneItem() {
		return mbooltype == 2 ? Boolean.toString(mRandom.nextBoolean()) : (mbooltype == 1 ? "true" : "false");
	}

	@Override
	public Object getLowerBound() {
		return mbooltype == 2 ? false : mbooltype == 1 ? true : false;
	}

	@Override
	public Object getUpperBound() {
		return mbooltype == 2 ? true : mbooltype == 1 ? true : false;
	}

}

/**
 * short: range: –32,768 to 32,767
 * sample: 1~ (means 1~32,767), ~1000 (means –32,768~1000), 1~1000
 */
class genShort extends gendata {

	private short mLowestValue = Short.MIN_VALUE;
	private short mBoundaryValue = Short.MAX_VALUE;
	private short mCurrentValue = 0;
	
	private Random mRandom = new Random();
	
	public genShort( order iorder, int istep, String irange) {
		super(iorder, istep, irange);
		String lOption = mDataRange.getOption(0);
		if (lOption != null && lOption.length() > 0) {
			try {
				mLowestValue = Short.parseShort(lOption);
			} catch (NumberFormatException e) {
				LOG.warn(e.getLocalizedMessage());
			}	
		}
		lOption = mDataRange.getOption(1);
		if (lOption != null && lOption.length() > 0) {
			try {
				mBoundaryValue = Short.parseShort(lOption);
			} catch (NumberFormatException e) {
				LOG.warn(e.getLocalizedMessage());
			}	
		}
		if (mOrder == analyticsops.order.Ascend) {
			mCurrentValue = mLowestValue;
		} else if (mOrder == analyticsops.order.Descend) {
			mCurrentValue = mBoundaryValue;
		}
	}

	@Override
	public String generateOneItem() {
		if (mOrderOps == 0) {
			return Short.toString((short)(mRandom.nextInt(mBoundaryValue) % Short.MAX_VALUE));
		}
		if (mOrderOps == 1) {
			if (mCurrentValue >= mBoundaryValue) throw new NoMoreDataException();
			short lCurrentValue = mCurrentValue;
			mCurrentValue += mStep;
			return Short.toString(lCurrentValue);
		}
		if (mCurrentValue <= mLowestValue) throw new NoMoreDataException();
		short lCurrentValue = mCurrentValue;
		mCurrentValue -= mStep;
		return Short.toString(lCurrentValue);
	}

	@Override
	public Object getLowerBound() {
		return mLowestValue;
	}

	@Override
	public Object getUpperBound() {
		return mBoundaryValue;
	}

}


class genInteger extends gendata {

	private int mLowestValue = Integer.MIN_VALUE;
	private int mBoundaryValue = Integer.MAX_VALUE;
	private int mCurrentValue = 0;
	private RandomDataGenerator mRandomData = new RandomDataGenerator();
	
	public genInteger( order iorder, int istep, String irange) {
		super(iorder, istep, irange);
		String lOption = mDataRange.getOption(0);
		if (lOption != null && lOption.length() > 0) {
			try {
				mLowestValue = Integer.parseInt(lOption);
			} catch (NumberFormatException e) {
				LOG.warn(e.getLocalizedMessage());
			}	
		}
		lOption = mDataRange.getOption(1);
		if (lOption != null && lOption.length() > 0) {
			try {
				mBoundaryValue = Integer.parseInt(lOption);
			} catch (NumberFormatException e) {
				LOG.warn(e.getLocalizedMessage());
			}	
		}
		if (mOrder == analyticsops.order.Ascend) {
			mCurrentValue = mLowestValue;
		} else if (mOrder == analyticsops.order.Descend) {
			mCurrentValue = mBoundaryValue;
		}
	}

	@Override
	public String generateOneItem() {
		if (mOrderOps == 0) {
			return Integer.toString(mRandomData.nextInt(mLowestValue, mBoundaryValue));
		}
		if (mOrderOps == 1) {
			if (mCurrentValue >= mBoundaryValue) {
				throw new NoMoreDataException();
			}
			int lCurrentValue = mCurrentValue;
			mCurrentValue += mStep;
			return Integer.toString(lCurrentValue);
		}
		if (mCurrentValue <= mLowestValue) {
			throw new NoMoreDataException();
		}
		int lCurrentValue = mCurrentValue;
		mCurrentValue -= mStep;
		return Integer.toString(lCurrentValue);
	}

	@Override
	public Object getLowerBound() {
		return mLowestValue;
	}

	@Override
	public Object getUpperBound() {
		return mBoundaryValue;
	}

}

class genDouble extends gendata {

	public genDouble(order iorder, int istep, String irange) {
		super(iorder, istep, irange);
		
	}

	@Override
	public String generateOneItem() {
		return null;
	}

	@Override
	public Object getLowerBound() {
		
		return null;
	}

	@Override
	public Object getUpperBound() {
		
		return null;
	}
	
}

class genLong extends gendata {
	
	private long mLowestValue = Long.MIN_VALUE;
	private long mBoundaryValue = Long.MAX_VALUE;
	private long mCurrentValue = 0;
	private RandomDataGenerator mRandomData = new RandomDataGenerator(); 
	
	public genLong(order iorder, int istep, String irange) {
		super(iorder, istep, irange);
		String lOption = mDataRange.getOption(0);
		if (lOption != null && lOption.length() > 0) {
			try {
				mLowestValue = Long.parseLong(lOption);
			} catch (NumberFormatException e) {
				LOG.warn(e.getLocalizedMessage());
			}	
		}
		lOption = mDataRange.getOption(1);
		if (lOption != null && lOption.length() > 0) {
			try {
				mBoundaryValue = Long.parseLong(lOption);
			} catch (NumberFormatException e) {
				LOG.warn(e.getLocalizedMessage());
			}	
		}
		if (mOrder == analyticsops.order.Ascend) {
			mCurrentValue = mLowestValue;
		} else if (mOrder == analyticsops.order.Descend) {
			mCurrentValue = mBoundaryValue;
		}
	}

	@Override
	public String generateOneItem() {
		if (mOrderOps == 0) {
			return Long.toString(mRandomData.nextLong(mLowestValue, mBoundaryValue));
		}
		if (mOrderOps == 1) {
			if (mCurrentValue >= mBoundaryValue) {
				throw new NoMoreDataException();
			}
			long lCurrentValue = mCurrentValue;
			mCurrentValue += mStep;
			return Long.toString(lCurrentValue);
		}
		if (mCurrentValue <= mLowestValue) {
			throw new NoMoreDataException();
		}
		long lCurrentValue = mCurrentValue;
		mCurrentValue -= mStep;
		
		return Long.toString(lCurrentValue);
	}

	@Override
	public Object getLowerBound() {
		return mLowestValue;
	}

	@Override
	public Object getUpperBound() {
		return mBoundaryValue;
	}

}

class genLongLong extends gendata {

	public genLongLong( order iorder, int istep,
			String irange) {
		super( iorder, istep, irange);
	}

	@Override
	public String generateOneItem() {
		
		return null;
		
	}

	@Override
	public Object getLowerBound() {
		
		return null;
	}

	@Override
	public Object getUpperBound() {
		
		return null;
	}
	
}

class genDate extends gendata {

	private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private Calendar mCalendar = Calendar.getInstance(); 
	private long mLowestValue = 0;
	private long mBoundaryValue = 0;
	private long mDurationValue = 0;
	
	public genDate( order iorder, int istep, String irange) {
		super(iorder, istep, irange);
		
		try {
			mLowestValue = mDateFormat.parse("1970-01-01").getTime();
		} catch (ParseException e1) {
			// ignore
		}
		
		String lOption = mDataRange.getOption(0);
		if (lOption != null && lOption.length() > 0) {
			try {
				mLowestValue = mDateFormat.parse(lOption).getTime();
			} catch (NumberFormatException e) {
				LOG.warn(e.getLocalizedMessage());
			} catch (ParseException e) {
				LOG.warn(e.getLocalizedMessage());
			}	
		}
		
		mBoundaryValue = (new Date()).getTime(); // today
		
		lOption = mDataRange.getOption(1);
		if (lOption != null && lOption.length() > 0) {
			try {
				mBoundaryValue = mDateFormat.parse(lOption).getTime();
			} catch (NumberFormatException e) {
				LOG.warn(e.getLocalizedMessage());
			} catch (ParseException e) {
				LOG.warn(e.getLocalizedMessage());
			}	
		}
		
		mDurationValue = mBoundaryValue - mLowestValue;
		
		if (mOrder == analyticsops.order.Ascend) {
			mCalendar.setTimeInMillis(mLowestValue);
		} else if (mOrder == analyticsops.order.Descend) {
			mCalendar.setTimeInMillis(mBoundaryValue);
		}
		
	}

	@Override
	public String generateOneItem() {
		if (mOrderOps == 0) {
			Date lRandomDate = new Date((long) (mLowestValue + Math.random() * mDurationValue));
			return mDateFormat.format(lRandomDate);
		}
		if (mOrderOps == 1) {
			if (mCalendar.getTimeInMillis() >= mBoundaryValue) {
				throw new NoMoreDataException();
			}
			String lDate = mDateFormat.format(new Date(mCalendar.getTimeInMillis()));
			mCalendar.add(Calendar.DAY_OF_YEAR, mStep);
			return lDate;
		}
		if (mCalendar.getTimeInMillis() <= mLowestValue) {
			throw new NoMoreDataException();
		}
		String lDate = mDateFormat.format(new Date(mCalendar.getTimeInMillis()));
		mCalendar.add(Calendar.DAY_OF_YEAR, -mStep);
		return lDate;
	}

	@Override
	public Object getLowerBound() {
		return mLowestValue;
	}

	@Override
	public Object getUpperBound() {
		return mBoundaryValue;
	}

}

class genDateTime extends gendata {
	
	public genDateTime(order iorder, int istep,
			String irange) {
		super(iorder, istep, irange);
		
	}

	@Override
	public String generateOneItem() {
		
		return null;
		
	}

	@Override
	public Object getLowerBound() {
		
		return null;
	}

	@Override
	public Object getUpperBound() {
		
		return null;
	}

}

class genTime extends gendata {

	public genTime(order iorder, int istep, String irange) {
		super(iorder, istep, irange);
	}

	@Override
	public String generateOneItem() {
		
		return null;
		
	}

	@Override
	public Object getLowerBound() {
		
		return null;
	}

	@Override
	public Object getUpperBound() {
		
		return null;
	}

}

/**
 * fixed random string: range: len~, ~len, len. if len1~len2, it will take len1 as fixed length
 */
class genString extends gendata {
	private int mStringLength = 1;
	private RandomDataGenerator mRandomData = new RandomDataGenerator(); 
	
	public genString(order iorder, int istep, String irange) {
		super(iorder, istep, irange);
		
		String lOption = mDataRange.getOption(0);
		if (lOption != null && lOption.length() > 0) {
			try {
				mStringLength = Integer.parseInt(lOption);
			} catch (NumberFormatException e) {
				LOG.warn(e.getLocalizedMessage());
			}	
		} else {
			lOption = mDataRange.getOption(1);
			if (lOption != null && lOption.length() > 0) {
				try {
					mStringLength = Integer.parseInt(lOption);
				} catch (NumberFormatException e) {
					LOG.warn(e.getLocalizedMessage());
				}
			}
		}
	}

	@Override
	public String generateOneItem() {
		return mRandomData.nextHexString(mStringLength);
	}

	@Override
	public Object getLowerBound() {
		return mStringLength;
	}

	@Override
	public Object getUpperBound() {
		return mStringLength;
	}

}

class genBigDecimal extends gendata {

	private BigDecimal mBigDecimal;
	
	public genBigDecimal(order iorder, int istep,
			String irange) {
		super(iorder, istep, irange);
	}

	@Override
	public String generateOneItem() {
		return null;
		
	}

	@Override
	public Object getLowerBound() {
		
		return null;
	}

	@Override
	public Object getUpperBound() {
		
		return null;
	}

}

