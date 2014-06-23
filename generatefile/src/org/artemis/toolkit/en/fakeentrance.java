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
package org.artemis.toolkit.en;

import java.io.IOException;

import org.artemis.toolkit.common.sysconfig;
import org.artemis.toolkit.table.gen.genreport;

/**
 * fakeentrance TODO
 * fakeentrance.java is written at Jun 23, 2014
 * @author junli
 */
public class fakeentrance {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String lLog4JConfig = System.getProperty(sysconfig.sLog4J);
		if (lLog4JConfig == null)
		{
			System.setProperty(sysconfig.sLog4J, 
					sysconfig.sDefaultLog4JPath);
		}
		
		if (args == null || args.length < 1) {
			System.out.println("please input report configuration file");
			return;
		}
		
		try {
			genreport lreportgenerate = genreport.createReportGenerate(args[0]);
			
			lreportgenerate.generateReport();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("");
	}

}
