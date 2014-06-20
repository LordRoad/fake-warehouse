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

import org.artemis.toolkit.table.datatype;
import org.artemis.toolkit.table.analyticsops.order;
import org.artemis.toolkit.table.gen.NoMoreDataException;
import org.artemis.toolkit.table.gen.genBoolean;
import org.artemis.toolkit.table.gen.genDate;
import org.artemis.toolkit.table.gen.genInteger;
import org.artemis.toolkit.table.gen.genLong;
import org.artemis.toolkit.table.gen.genString;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.hamcrest.CoreMatchers.*;

/**
 * testdata functionality testing for basic cell types, it's just basic testing
 * testdata.java is written at Jun 16, 2014
 * @author junli
 */
@RunWith(JUnit4.class)
public class testdata {

	@Test
	public void testRandomBoolean() {
		genBoolean lgenBoolean = new genBoolean(datatype.BOOLEAN, order.Random, 0, "0~1");
		
		for (int iter = 0; iter < 32; ++iter) {
			org.junit.Assert.assertThat(lgenBoolean.generateOneItem(), anyOf(is("true"), is("false")));
		}
		
	}
	
	@Test
	public void testAscBoolean() {
		genBoolean lgenBoolean = new genBoolean(datatype.BOOLEAN, order.Ascend, 0, "0");
		
		for (int iter = 0; iter < 32; ++iter) {
			org.junit.Assert.assertEquals("", lgenBoolean.generateOneItem(), "false");
		}
		
	}
	
	@Test
	public void testDescBoolean() {
		genBoolean lgenBoolean = new genBoolean(datatype.BOOLEAN, order.Descend, 0, "1");
		
		for (int iter = 0; iter < 32; ++iter) {
			org.junit.Assert.assertEquals("", lgenBoolean.generateOneItem(), "true");
		}
	}
	
	@Test
	public void testRandomInt() {
		genInteger lgenInteger = new genInteger(datatype.INT, order.Random, 0, "0~34952");
		
		for (int iter = 0; iter < 32; ++iter) {
			int lRandomValue = Integer.parseInt(lgenInteger.generateOneItem());
			org.junit.Assert.assertTrue(lRandomValue >= 0 && lRandomValue < 34952);
		}
		
	}
	
	@Test
	public void testAscInt() {
		for (int step = 1; step < 3; ++step) {
			genInteger lgenInteger = new genInteger(datatype.INT, order.Ascend, step, "0~256");
			
			// no more data
			int lOldValue = 0 - step;
			for (int iter = 0; iter < 260; ++iter) {
				try {	
					int lValue = Integer.parseInt(lgenInteger.generateOneItem());
					org.junit.Assert.assertEquals("", lValue, lOldValue + step);
					lOldValue = lValue;
				} catch (NoMoreDataException e) {
					org.junit.Assert.assertTrue(iter >= (int)(256 / step));
					break;
				}
			}
		}
	}
	
	@Test
	public void testDescInt() {
		for (int step = 1; step < 3; ++step) {
			genInteger lgenInteger = new genInteger(datatype.INT, order.Descend, step, "-1000~256");
			
			// no more data
			int lOldValue = 256 + step;
			for (int iter = 0; iter < 1260; ++iter) {
				try {	
					int lValue = Integer.parseInt(lgenInteger.generateOneItem());
					org.junit.Assert.assertEquals("", lValue, lOldValue - step);
					lOldValue = lValue;
				} catch (NoMoreDataException e) {
					org.junit.Assert.assertTrue(iter >= (int)(1256 / step));
					break;
				}
			}
		}
	}
	
	@Test
	public void testRandomLong() {
		genLong lgenLong = new genLong(datatype.LONG, order.Random, 0, "0~20140616");
		
		for (int iter = 0; iter < 2048; ++iter) {
			long lRandomValue = Long.parseLong(lgenLong.generateOneItem());
			org.junit.Assert.assertTrue(lRandomValue >= 0 && lRandomValue < 20140616);
		}
		
	}
	
	@Test
	@Ignore
	public void testRandomDate1() {
		genDate lgenDate = new genDate(datatype.DATE, order.Random, 1, "2012-12-25~");
		
		for (int iter = 0; iter < 32; ++iter) {
			String lRandomValue = lgenDate.generateOneItem();
			System.out.println(lRandomValue);
		}
		
	}
	
	@Test
	@Ignore
	public void testRandomDate2() {
		genDate lgenDate = new genDate(datatype.DATE, order.Random, 1, "~");
		
		for (int iter = 0; iter < 32; ++iter) {
			String lRandomValue = lgenDate.generateOneItem();
			System.out.println(lRandomValue);
		}
		
	}
	
	@Test
	@Ignore
	public void testRandomDate3() {
		genDate lgenDate = new genDate(datatype.DATE, order.Random, 1, "~2005-05-05");
		
		for (int iter = 0; iter < 32; ++iter) {
			String lRandomValue = lgenDate.generateOneItem();
			System.out.println(lRandomValue);
		}
		
	}
	
	@Test
	@Ignore
	public void testRandomDate4() {
		genDate lgenDate = new genDate(datatype.DATE, order.Random, 1, "2011-11-11~2014-11-11");
		
		for (int iter = 0; iter < 32; ++iter) {
			String lRandomValue = lgenDate.generateOneItem();
			System.out.println(lRandomValue);
		}
		
	}
	
	@Test
	@Ignore
	public void testAscDate() {
		genDate lgenDate = new genDate(datatype.DATE, order.Ascend, 1, "2011-12-11~2012-01-01");
		
		for (int iter = 0; iter < 48; ++iter) {
			try {
				String lRandomValue = lgenDate.generateOneItem();
				System.out.println(lRandomValue);
			} catch (NoMoreDataException e) {
				// ignore
			}
		}
	}
	
	@Test
	@Ignore
	public void testDescDate() {
		genDate lgenDate = new genDate(datatype.DATE, order.Descend, 1, "2011-12-11~2012-01-01");
		
		for (int iter = 0; iter < 48; ++iter) {
			try {
				String lRandomValue = lgenDate.generateOneItem();
				System.out.println(lRandomValue);
			} catch (NoMoreDataException e) {
				// ignore
			}
		}
	}
	
	@Test
	public void testRandomString() {
		genString lgenString = new genString(datatype.STRING, order.Random, 1, "~12");
		
		for (int iter = 0; iter < 16; ++iter) {
			try {
				String lRandomValue = lgenString.generateOneItem();
				System.out.println(lRandomValue);
			} catch (NoMoreDataException e) {
				// ignore
			}
		}
	}
	
}
