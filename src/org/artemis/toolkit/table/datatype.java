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
 * datatype TODO
 * datatype.java is written at Jun 14, 2014
 * @author return_jun
 * @since 0.2
 * @deprecated since 0.2.1, to support extra column type, enum is not suitable
 * @see org.artemis.toolkit.table.newdatatype
 */
@Deprecated
public enum datatype {
	INVALID("invalid"),
	
	// true, false
	BOOLEAN("boolean"),
	
	SHORT("short"),
	INT("int"),
	FLOAT("float"),
	DOUBLE("double"),
	LONG("long"),
	LONGLONG("longlong"),
	
	// 2014-06-14, based on different date type
	DATE("date"),
	
	// 2014-06-14 09:41:18
	DATETIME("datetime"),
	
	// 09:41:18
	TIME("time"),
	
	// char[] as string
	STRING("string"),
	
	/**
	 * although we got this on our product, somebody helped here.
	 * @see http://www.opentaps.org/docs/index.php/How_to_Use_Java_BigDecimal:_A_Tutorial
	 */
	BIGDECIMAL("bigdecimal")
	;
	
	private String mtypename;
	
	private datatype(String itypename) {
		mtypename = itypename;
	}
	
	public String value() {
		return mtypename;
	}
	
	@Override
	public String toString() {
		return mtypename;
	}
	
	public static datatype fromString(String idatatype) {
		if (idatatype.compareTo("boolean") == 0) {
			return BOOLEAN;
		}
		if (idatatype.compareTo("short") == 0) {
			return SHORT;
		}
		if (idatatype.compareTo("int") == 0) {
			return INT;
		}
		if (idatatype.compareTo("float") == 0) {
			return FLOAT;
		}
		if (idatatype.compareTo("double") == 0) {
			return DOUBLE;
		}
		if (idatatype.compareTo("long") == 0) {
			return LONG;
		}
		if (idatatype.compareTo("longlong") == 0) {
			return LONGLONG;
		}
		if (idatatype.compareTo("date") == 0) {
			return DATE;
		}
		if (idatatype.compareTo("datetime") == 0) {
			return DATETIME;
		}
		if (idatatype.compareTo("time") == 0) {
			return TIME;
		}
		if (idatatype.compareTo("string") == 0) {
			return STRING;
		}
		if (idatatype.compareTo("bigdecimal") == 0) {
			return BIGDECIMAL;
		}
		
		return INVALID;
	}
	
}

