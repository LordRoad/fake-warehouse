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

import java.io.IOException;

import org.artemis.toolkit.common.configparser;
import org.artemis.toolkit.table.analyticsops.order;
import org.artemis.toolkit.table.datatype;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * enumtest test customized Gson adapter
 * enumtest.java is written at Jun 15, 2014
 * @author return_jun
 */
@RunWith(JUnit4.class)
public class enumtest {
	
	@Test
	public void dttest() throws IOException {
		datatype ldatatype = datatype.DATETIME;
		
		configparser lconfigparser = new configparser(System.getProperty("user.dir") +
				"/tmp/testdatatype.json");
		
		lconfigparser.serialize(ldatatype);
	}
	
	@Test
	public void dtdestest() throws IOException {
		
		configparser lconfigparser = new configparser(System.getProperty("user.dir") +
				"/tmp/testdatatype.json");
		
		datatype ldatatype = lconfigparser.deserialize(datatype.class);
		System.out.println("data type: " + ldatatype.value());
	}
	
	@Test
	public void ordertest() throws IOException {
		order lorder = order.Ascend;
		
		configparser lconfigparser = new configparser(System.getProperty("user.dir") +
				"/tmp/testorder.json");
		
		lconfigparser.serialize(lorder);
	}
	
}
