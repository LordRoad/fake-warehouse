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

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.annotations.SerializedName;

/**
 * sysconfig TODO
 * sysconfig.java is written at Jun 21, 2014
 * @author junli
 * @since 0.2
 */
public class sysconfig {
	private static final Logger LOG = LoggerFactory.getLogger(sysconfig.class);
			
	@SerializedName("one_batch") private String mGenOneBatchRowCount;
	@SerializedName("range_split") private String mDataRangSplit;
	@SerializedName("file_ext") private String mFileExtension;
	@SerializedName("extra_data") private String mExtraDataConfigurePath;
	@SerializedName("log") private String mDefaultLog4JPath;
	
	public boolean init() {
		try {
			configparser lconfigparser = new configparser(sSysConfigPath);
			sysconfig lsysconfig = lconfigparser.deserialize(this.getClass());
			if (lsysconfig == null) {
				LOG.error("failed to init system configure.");
				return false;
			}
			if (lsysconfig.mDataRangSplit != null) {
				sysconfig.sDataRangeSplit = lsysconfig.mDataRangSplit;
			}
			if (lsysconfig.mGenOneBatchRowCount != null) {
				sysconfig.sGenOneBatchRowCount = lsysconfig.mGenOneBatchRowCount;
			}
			if (lsysconfig.mFileExtension != null) {
				sysconfig.sFileExtension = lsysconfig.mFileExtension;
			}
			if (lsysconfig.mDefaultLog4JPath != null && lsysconfig.mDefaultLog4JPath.length() > 0) {
				sysconfig.sSysConfigPath = lsysconfig.mDefaultLog4JPath;
			}
			if (lsysconfig.mExtraDataConfigurePath != null) {
				sysconfig.sExtraDataConfigurePath = lsysconfig.mExtraDataConfigurePath;
			}
			
			return true;
		} catch (IOException e) {
			LOG.error(e.getLocalizedMessage());
		}
		return false;
	}
	
	public void save() throws IOException {
		configparser lconfigparser = new configparser(sSysConfigPath);
		
		mDataRangSplit = sysconfig.sDataRangeSplit;
		mGenOneBatchRowCount = sysconfig.sGenOneBatchRowCount;
		mFileExtension = sysconfig.sFileExtension;
		mDefaultLog4JPath = sysconfig.sSysConfigPath;
		mExtraDataConfigurePath = sysconfig.sExtraDataConfigurePath;
		
		lconfigparser.serialize(this);
	}
	
	public static String sGenOneBatchRowCount = "org.artemis.toolkit.gen.onebatch";
	
	public static String sDataRangeSplit = "~";

	public static String sFileExtension = ".csv";
	
	public static String sLog4J = "log4j.configuration";
	
	public static String sDefaultLog4JPathInPrograme = 
			System.getProperty("user.dir") + "/config/log4j.properties";
	
	public static String sDefaultLog4JPath = 
			"file:" + System.getProperty("user.dir") + "/config/log4j.properties";
	
	public static String sExtraDataConfigurePath =  
			System.getProperty("user.dir") + "/extra/extra.json";
	
	public static String sSysConfigPath = 
			System.getProperty("user.dir") + "/config/sysconfig.json";
	
}
