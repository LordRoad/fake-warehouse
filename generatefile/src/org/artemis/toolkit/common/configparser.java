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

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.artemis.toolkit.table.analyticsops.order;
import org.artemis.toolkit.table.datatype;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

/**
 * configparser TODO
 * configparser.java is written at Jun 13, 2014
 * @author junli
 */
public class configparser {
	private static final Logger LOG = LoggerFactory.getLogger(configparser.class);
	
	private Gson mGson = null;
	private String mConfigfile = null;
	
	public configparser(String iconfigfile) throws IOException {
		/**
		 * pretty print, serialize null fields, ignore static, transient
		 */
		mGson = new GsonBuilder()
				.setPrettyPrinting()
				.serializeNulls()
				.setExclusionStrategies(new ignorestrategy(null))
				.registerTypeAdapter(datatype.class, new dtSerializer())
				.registerTypeAdapter(datatype.class, new dtDeserializer())
				.registerTypeAdapter(order.class, new orderSerializer())
				.registerTypeAdapter(order.class, new orderDeserializer())
				.create();
		
		fileutils.initfile(iconfigfile);
		mConfigfile = iconfigfile;
	}
	
	public <T> T deserialize(Class<T> iclass) {
		try {
			T lReadObj = mGson.fromJson(new FileReader(mConfigfile), iclass);
			return lReadObj;
		} catch (JsonSyntaxException e) {
			LOG.error(e.getLocalizedMessage());
		} catch (JsonIOException e) {
			LOG.error(e.getLocalizedMessage());
		} catch (FileNotFoundException e) {
			LOG.error(e.getLocalizedMessage());
		}
		return null;
	}
	
	public <T> void serialize(T iObj) throws IOException {
		Writer lConfigWriter = new FileWriter(mConfigfile);
		lConfigWriter.write(mGson.toJson(iObj));
		lConfigWriter.close();
	}
	
}
