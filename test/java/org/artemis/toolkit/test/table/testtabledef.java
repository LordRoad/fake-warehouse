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
import org.artemis.toolkit.metadata.columnmd;
import org.artemis.toolkit.metadata.tablemd;
import org.artemis.toolkit.table.newdatatype;
import org.artemis.toolkit.table.analyticsops.order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * tabledef TODO
 * tabledef.java is written at Jun 15, 2014
 * @author return_jun
 */
@RunWith(JUnit4.class)
public class testtabledef {

	@Test
	public void testfacttbl() throws IOException {
		tablemd lfacttable = new tablemd("fact_table");
		
		lfacttable.insertnewcolumn(new columnmd("index", 
				newdatatype.fromString(newdatatype.innerDT.sLong), order.Ascend, "0-"));
		lfacttable.insertnewcolumn(new columnmd("gender", 
				newdatatype.fromString(newdatatype.innerDT.sShort), order.Random, "0-1"));
		lfacttable.insertnewcolumn(new columnmd("date", 
				newdatatype.fromString(newdatatype.innerDT.sDate), order.Random, "2012-06-28~2014-06-15"));
		lfacttable.insertnewcolumn(new columnmd("description", 
				newdatatype.fromString(newdatatype.innerDT.sString), order.Random, "0-255"));
		lfacttable.insertnewcolumn(new columnmd("metric", 
				newdatatype.fromString(newdatatype.innerDT.sLong)));
		
		configparser lconfigparser = new configparser(System.getProperty("user.dir") +
				"/tmp/testfacttbl.json");
		
		lconfigparser.serialize(lfacttable);
	}
	
	@Test
	public void testfacttblinit() throws IOException {
		
		configparser lconfigparser = new configparser(System.getProperty("user.dir") +
				"/tmp/testfacttbl.json");
		
		tablemd lfacttable = lconfigparser.deserialize(tablemd.class);
		System.out.println(lfacttable.getmTableName());
	}
	
	@Test
	public void testLUtbl() throws IOException {
		tablemd lfacttable = new tablemd("fact_table");
		
		lfacttable.insertnewcolumn(new columnmd("index", 
				newdatatype.fromString(newdatatype.innerDT.sLong), order.Ascend, "0-"));
		lfacttable.insertnewcolumn(new columnmd("gender", 
				newdatatype.fromString(newdatatype.innerDT.sShort), order.Random, "0-1"));
		lfacttable.insertnewcolumn(new columnmd("date", 
				newdatatype.fromString(newdatatype.innerDT.sDate), order.Random, "2012-06-28~2014-06-15"));
		lfacttable.insertnewcolumn(new columnmd("description", 
				newdatatype.fromString(newdatatype.innerDT.sString), order.Random, "0-255"));
		lfacttable.insertnewcolumn(new columnmd("metric", 
				newdatatype.fromString(newdatatype.innerDT.sLong)));
		
		tablemd llookuptable = new tablemd("LU_date");
		
		configparser lconfigparser = new configparser(System.getProperty("user.dir") +
				"/tmp/testLUtbl.json");
		
		lconfigparser.serialize(llookuptable);
	}
	
}
