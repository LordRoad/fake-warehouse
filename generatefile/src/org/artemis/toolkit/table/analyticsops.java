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
package org.artemis.toolkit.table;

/**
 * analyticsops TODO
 * analyticsops.java is written at Jun 14, 2014
 * @author return_jun
 */
public class analyticsops {

	public static enum order {
		Descend("desc"),
		Ascend("asc"),
		Random("rand");
		
		private String mOrderName;
		
		private order(String iordername) {
			mOrderName = iordername;
		}
		
		public String value() {
			return mOrderName;
		}
		
		public String toString() {
			return mOrderName;
		}
		
		public static order fromString(String iordername) {
			if (iordername.compareTo("desc") == 0) {
				return Descend;
			}
			if (iordername.compareTo("asc") == 0) {
				return Ascend;
			}
			if (iordername.compareTo("rand") == 0) {
				return Random;
			}
			return null;
		}
		
	}
	
	/**
	 * when user specifies column oder, it should be added one by one or 
	 * odd, even or specified steps (such as 6, -6)
	 */
	public class steps {
		// private 
	}
	
}
