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

import java.util.Date;

/**
 * newdatatype  
 * 
 * in order to support customized data type, {@link org.artemis.toolkit.table.datatype  datatype} is deprecated.
 * 
 *  INVALID("invalid"),
 *	BOOLEAN("boolean"),
 *	SHORT("short"),
 *	INT("int"),
 *	FLOAT("float"), not supported
 *	DOUBLE("double"),
 *	LONG("long"),
 *	DATE("date"),
 *	DATETIME("datetime"), not supported
 *	TIME("time"), not supported
 *	STRING("string"),
 *	BIGDECIMAL("bigdecimal"), not supported
 *  
 * newdatatype.java is written at Jun 30, 2014
 * @author junli
 * @since 0.2.1
 */
public class newdatatype {
	
	public class innerDT {
		public static final String sBoolean = "boolean";
		public static final String sShort = "short";
		public static final String sInt = "int";
		public static final String sFloat = "float";
		public static final String sDouble = "double";
		public static final String sLong = "long";
		public static final String sDate = "date";
		public static final String sDateTime = "datetime";
		public static final String sTime = "time";
		public static final String sString = "string";
		public static final String sBigDecimal = "bigdecimal";
	}
	
	private String mtypename;
	
	private newdatatype(String itypename) {
		mtypename = itypename;
	}
	
	public String value() {
		return mtypename;
	}
	
	@Override
	public String toString() {
		return mtypename;
	}
	
	public static newdatatype fromString(String idatatype) {
		return new newdatatype(idatatype);
	}
	
	public boolean isInnerDT() {
		if (mtypename.compareToIgnoreCase(newdatatype.innerDT.sBoolean) == 0 ||
			mtypename.compareToIgnoreCase(newdatatype.innerDT.sShort) == 0 ||
			mtypename.compareToIgnoreCase(newdatatype.innerDT.sInt) == 0 ||
			mtypename.compareToIgnoreCase(newdatatype.innerDT.sDouble) == 0 ||
			mtypename.compareToIgnoreCase(newdatatype.innerDT.sLong) == 0 ||
			mtypename.compareToIgnoreCase(newdatatype.innerDT.sDate) == 0 ||
			mtypename.compareToIgnoreCase(newdatatype.innerDT.sString) == 0) {
			return true;
		} 
		return false;
	}
	
}
