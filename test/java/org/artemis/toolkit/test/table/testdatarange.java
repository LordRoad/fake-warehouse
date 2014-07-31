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
package org.artemis.toolkit.test.table;

import org.artemis.toolkit.table.analyticsops.order;
import org.artemis.toolkit.table.datarange;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * testdatarange TODO
 * testdatarange.java is written at Jun 22, 2014
 * @author junli
 */
@RunWith(JUnit4.class)
public class testdatarange {

	@Test
	@Ignore
	public void booleanrange() {
		
		datarange ldatarange = new datarange("0~1");
		String[] lRangeSet = ldatarange.splitRange(new Boolean(true), 100, order.Ascend, 32, 1);
		for (String lCurrentRange : lRangeSet) {
			System.out.println(lCurrentRange);
		}
	}
	
	@Test
	@Ignore
	public void shortrange() {
		
		datarange ldatarange = new datarange("-256~255");
		String[] lRangeSet = ldatarange.splitRange(new Short((short) 0), 300, order.Descend, 10, 1);
		for (String lCurrentRange : lRangeSet) {
			System.out.println(lCurrentRange);
		}
	}
	
	@Test
	//@Ignore
	public void intrange() {
		
		datarange ldatarange = new datarange("1~10000");
		String[] lRangeSet = ldatarange.splitRange(new Integer(0), 10000, order.Ascend, 32, 1);
		for (String lCurrentRange : lRangeSet) {
			System.out.println(lCurrentRange);
		}
	}
	
	@Test
	@Ignore
	public void longrange() {
		
		datarange ldatarange = new datarange("-1000000~1000000");
		String[] lRangeSet = ldatarange.splitRange(new Long((long) 0), 1000000, order.Ascend, 64, 1);
		for (String lCurrentRange : lRangeSet) {
			System.out.println(lCurrentRange);
		}
	}
	
	@Test
	@Ignore
	public void longrange2() {
		
		datarange ldatarange = new datarange("-1000000~1000000");
		String[] lRangeSet = ldatarange.splitRange(new Long((long) 0), 1000000, order.Random, 32, 1);
		for (String lCurrentRange : lRangeSet) {
			System.out.println(lCurrentRange);
		}
	}
	
	@Test
	@Ignore
	public void stringrange() {
		
		datarange ldatarange = new datarange("6");
		String[] lRangeSet = ldatarange.splitRange(new String(), 1000000, order.Ascend, 32, 1);
		for (String lCurrentRange : lRangeSet) {
			System.out.println(lCurrentRange);
		}
	}
	
}
