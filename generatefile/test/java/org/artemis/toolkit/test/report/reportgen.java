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
package org.artemis.toolkit.test.report;

import java.io.IOException;

import org.artemis.toolkit.common.configparser;
import org.artemis.toolkit.table.columnmd;
import org.artemis.toolkit.table.datatype;
import org.artemis.toolkit.table.tabledata;
import org.artemis.toolkit.table.tablemd;
import org.artemis.toolkit.table.report;
import org.artemis.toolkit.table.reportbuilder;
import org.artemis.toolkit.table.analyticsops.order;
import org.artemis.toolkit.table.gen.genreport;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * reportgen TODO
 * reportgen.java is written at Jun 15, 2014
 * @author return_jun
 */
@RunWith(JUnit4.class)
public class reportgen {

	private String mReportGenConfigure = System.getProperty("user.dir") +
			"/tmp/testReportGen.json";
	
	@Test
	public void testReportGen() throws IOException {
		
		tabledata lfacttable = new tabledata("fact_table");
		
		lfacttable.setmFacttblRowcount(1000000);
		lfacttable.setmFacttblSlice(32);
		lfacttable.insertnewcolumn(new columnmd("index", datatype.LONG, order.Ascend, "0-"));
		lfacttable.insertnewcolumn(new columnmd("gender", datatype.SHORT, order.Random, "0-1"));
		lfacttable.insertnewcolumn(new columnmd("date", datatype.DATE, order.Random, "2012-06-28~2014-06-15"));
		lfacttable.insertnewcolumn(new columnmd("description", datatype.STRING, order.Random, "0-255"));
		lfacttable.insertnewcolumn(new columnmd("metric", datatype.LONG));
		
		tablemd llookuptable = new tablemd("LU_date", lfacttable);
		llookuptable.setmLUTableRowcount(1000);
		llookuptable.adddependency(new int[] {2});
		
		tablemd llookuptable2 = new tablemd("LU_index", lfacttable);
		llookuptable2.setmLUTableRowcount(512);
		llookuptable2.adddependency(new int[] {0,1});
		
		report lreport = new reportbuilder().addFactTable(lfacttable)
				.addLUTable(llookuptable)
				.addLUTable(llookuptable2)
				.build();
	
		genreport lreportgenerate = new genreport();
		lreportgenerate.setmReport(lreport);
		lreportgenerate.setmReportName("testReportGen");
		lreportgenerate.setmStoragePath(System.getProperty("user.dir") +
				"/tmp");
		
		configparser lconfigparser = new configparser(mReportGenConfigure);
		
		lconfigparser.serialize(lreportgenerate);
	}
	
	@Test
	public void testReportGeninit() throws IOException {
		genreport lreportgenerate = genreport.createReportGenerate(mReportGenConfigure);
		
		System.out.println("report name: " + lreportgenerate.getmReportName());
		System.out.println("storage path: " + lreportgenerate.getmStoragePath());
		
	}
}
