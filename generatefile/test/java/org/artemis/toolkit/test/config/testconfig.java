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
package org.artemis.toolkit.test.config;

import java.io.IOException;
import org.artemis.toolkit.common.configparser;
import org.artemis.toolkit.metadata.tablemd;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * configtest TODO
 * configtest.java is written at Jun 13, 2014
 * @author junli
 */
@RunWith(JUnit4.class)
public class testconfig {
	
	@Test
	//@Ignore
	public void writetest() throws IOException {
		configparser lconfigparser = new configparser(System.getProperty("user.dir") +
				"/tmp/lookuptblconfig.json");
		
		tablemd llookuptable = new tablemd("LU_table");
		llookuptable.setmTableName("LU_table");
		
		lconfigparser.serialize(llookuptable);
	}
	
	@Test
	//@Ignore
	public void readtest() throws IOException {
		configparser lconfigparser = new configparser(System.getProperty("user.dir") +
				"/lookuptblconfig.json");
		
		tablemd llookuptable = lconfigparser.deserialize(tablemd.class);
		if (llookuptable != null) {
			System.out.println(llookuptable.getmTableName());
		}
		
	}
	
}
