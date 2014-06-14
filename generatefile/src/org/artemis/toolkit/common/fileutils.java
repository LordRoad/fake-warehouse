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
package org.artemis.toolkit.common;

import java.io.File;
import java.io.IOException;

/**
 * fileutils TODO
 * fileutils.java is written at Jun 13, 2014
 * @author junli
 */
public class fileutils {
	
	public static boolean exists(String ifilepath) {
		File lFile = new File(ifilepath);
		return lFile.exists();
	}
	
	/**
	 * check if file is existed, if not, create new one
	 * @param ifilepath
	 * @throws IOException
	 */
	public static void initfile(String ifilepath) throws IOException {
		File lTestFile = null;
		lTestFile = new File(ifilepath);
		if (!lTestFile.exists()) {
			lTestFile.createNewFile();
		}
	}
	
}
