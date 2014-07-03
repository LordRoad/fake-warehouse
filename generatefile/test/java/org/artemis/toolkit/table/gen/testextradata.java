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

import org.artemis.toolkit.common.configparser;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * testextradata TODO
 * testextradata.java is written at Jul 1, 2014
 * @author junli
 */
@RunWith(JUnit4.class)
public class testextradata {

	@Test
	@Ignore
	public void testextra() throws IOException {
		String lColumnName = "week";
		
		extraconfig lextraconfig = extraconfig.instance();
		String lWeek = lextraconfig.getExtraDataPath(lColumnName);
		
		extradatapool lextradatapool = extradatapool.instance();
		org.junit.Assert.assertTrue(lextradatapool.initdata(lColumnName, lWeek));
		
		extradata lWeekColumn = lextradatapool.getExtraData(lColumnName);
		System.out.println(Integer.toString(lWeekColumn.size()));
	}
	
	@Test
	@Ignore
	public void testweekdata() throws IOException {
		String[] lWeekList = {"Monday", 
				"Tuesday", 
				"Wednesday", 
				"Thursday", 
				"Friday", 
				"Saturday", 
				"Sunday"
				};
		extradata lextradata = new extradata(lWeekList);
		
		configparser lconfigparser = new configparser(System.getProperty("user.dir") +
				"/extra/week.json");
		lconfigparser.serialize(lextradata);
	}
	
	@Test
	public void testmonthdata() throws IOException {
		String[] lWeekList = {"January", 
				"February", 
				"March", 
				"April", 
				"May", 
				"June", 
				"July", 
				"August",
				"September", 
				"October", 
				"November", 
				"December"};
		
		extradata lextradata = new extradata(lWeekList);
		
		configparser lconfigparser = new configparser(System.getProperty("user.dir") +
				"/extra/month.json");
		lconfigparser.serialize(lextradata);
	}
	
}
