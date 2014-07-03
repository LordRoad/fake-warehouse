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
import org.apache.log4j.PropertyConfigurator;
import org.artemis.toolkit.common.sysconfig;
import org.artemis.toolkit.table.gen.extraconfig;
import org.artemis.toolkit.table.gen.genreport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * fakeentrance TODO
 * fakeentrance.java is written at Jun 23, 2014
 * @author junli
 */
public class fakeentrance {
	private static final Logger LOG = LoggerFactory.getLogger(fakeentrance.class);
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String lLog4JConfig = System.getProperty(sysconfig.sLog4J);
		if (lLog4JConfig == null)
		{
			System.setProperty(sysconfig.sLog4J, 
					sysconfig.sDefaultLog4JPath);
			
			PropertyConfigurator.configure(sysconfig.sDefaultLog4JPathInPrograme);
		}
		
		if (args == null || args.length < 1) {
			LOG.error("please input report configuration file");
			return;
		}
		
		try {
			sysconfig lsysconfig = new sysconfig();
			if (!lsysconfig.init()) {
				// ignore
			}
			
			@SuppressWarnings("unused")
			extraconfig lextraconfig = extraconfig.instance();
			
			// generate report
			genreport lreportgenerate = genreport.createReportGenerate(args[0]);
			lreportgenerate.generateReport();
			
		} catch (IOException e) {
			LOG.error(e.getLocalizedMessage());
		}
		
		LOG.info("zhimakaimen is exited");
	}

}
