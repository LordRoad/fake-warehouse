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
import java.util.List;

import org.artemis.toolkit.common.configparser;
import org.artemis.toolkit.table.report;
import org.artemis.toolkit.table.tablemd;

import com.google.gson.annotations.SerializedName;

/**
 * reportgenerate generate report with data
 * reportgenerate.java is written at Jun 15, 2014
 * @author return_jun
 */
public class genreport {
	/**
	 * the folder is used to store generated report
	 */
	@SerializedName("storage_path") private String mStoragePath = null;
	@SerializedName("report_name") private String mReportName = null;
	@SerializedName("report") private report mReport = null;

	public genreport() {
		
	}
	
	public static genreport createReportGenerate(String iconfigurefile) throws IOException {
		configparser lconfigparser = new configparser(iconfigurefile);
		return lconfigparser.deserialize(genreport.class);
	}
	
	public String getmStoragePath() {
		return mStoragePath;
	}

	public void setmStoragePath(String mStoragePath) {
		this.mStoragePath = mStoragePath;
	}

	public String getmReportName() {
		return mReportName;
	}

	public void setmReportName(String mReportName) {
		this.mReportName = mReportName;
	}

	public report getmReport() {
		return mReport;
	}

	public void setmReport(report mReport) {
		this.mReport = mReport;
	}
	
	/**
	 * generate report based on schema information
	 */
	public void generateReport() {
		if (mReport == null) {
			throw new RuntimeException("fatal error, there is no report instance in current generating environment.");
		}
		
		List<tablemd> lLUTables = mReport.getmlookuptableset();
		for (tablemd lLUTable : lLUTables) {
			
		}
		
	}
}
