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

import org.artemis.toolkit.metadata.columnmd;
import org.artemis.toolkit.metadata.reportbuilder;
import org.artemis.toolkit.metadata.reportmd;
import org.artemis.toolkit.metadata.tablemd;
import org.artemis.toolkit.table.datatype;
import org.artemis.toolkit.table.analyticsops.order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * testbasictable TODO
 * testbasictable.java is written at Jun 22, 2014
 * @author junli
 */
@RunWith(JUnit4.class)
public class testbasictable {
	 
	@Test
	public void testbasictableGen() {
		
		tablemd lfacttable = new tablemd("fact_table");
		
		lfacttable.insertnewcolumn(new columnmd("index", datatype.LONG, order.Ascend, "0~10000000"));
		lfacttable.insertnewcolumn(new columnmd("gender", datatype.SHORT, order.Random, "0~1"));
		lfacttable.insertnewcolumn(new columnmd("date", datatype.DATE, order.Random, "2012-06-28~2014-06-15"));
		lfacttable.insertnewcolumn(new columnmd("description", datatype.STRING, order.Random, "10"));
		lfacttable.insertnewcolumn(new columnmd("metric", datatype.LONG, order.Random, "1~10000"));
		lfacttable.setmTableSlice(32);
		lfacttable.setmTableRowcount(10000000);
		
		tablemd llookuptable = new tablemd("LU_date");
		llookuptable.insertnewcolumn(new columnmd("date", datatype.DATE, order.Random, "2012-06-28~2014-06-15"));
		llookuptable.setmTableRowcount(365);
		
		tablemd llookuptable2 = new tablemd("LU_index");
		llookuptable2.insertnewcolumn(new columnmd("index", datatype.INT, order.Ascend, "1~100000"));
		llookuptable2.setmTableRowcount(10000);
		llookuptable2.setmTableSlice(32);
		
		reportmd lreport = new reportbuilder().addFactTable(lfacttable)
				.addLUTable(llookuptable)
				.addLUTable(llookuptable2)
				.build();
	
		genreport lreportgenerate = new genreport();
		lreportgenerate.setmReport(lreport);
		lreportgenerate.setmReportName("testReportGen");
		lreportgenerate.setmStoragePath(System.getProperty("user.dir") +
				"/tmp");
		
		lreportgenerate.generateReport();
		
	}
}
