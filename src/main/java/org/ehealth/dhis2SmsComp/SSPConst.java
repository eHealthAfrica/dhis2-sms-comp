/*
# Copyright (C) 2018 by eHealth Africa : http://www.eHealthAfrica.org
#
# See the NOTICE file distributed with this work for additional information
# regarding copyright ownership.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#   http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.
*/

package org.ehealth.dhis2SmsComp;

/**
 * A class of commonly used consts for SMS Submissions
 * @author jaspertimm
 *
 */
public class SSPConst {
	
	// An enum of the various value types in a submission
	public enum SubmValueType {
		INT,
		DATE,
		BOOL,
		STRING,
		BLANK
	}
	
	public enum Version {
		ONE, // Initial version, with no version information
		TWO  // Current release, includes versioning
	}

	public static Version CUR_VERSION = Version.TWO;
	
	// The bit lengths of each part of the message are statically set here
	public static int VER_BITLEN = 6;
	// NB: The CMD_BITLEN is important, as it's the size of the hash used to uniquely id each command
	// We use this hash collision calculator: http://everydayinternetstuff.com/2015/04/hash-collision-probability-calculator/
	// At 16-bit, for an env with 25 SMS commands, the probability of collision is < 0.5% 
	public static int CMD_BITLEN = 16;
	// We have to backwards support old CMD handling
	public static int OLD_CMD_BITLEN = 3;
	public static int SUBM_DATE_BITLEN = 12;
	public static int EPOCH_DATE_BITLEN = 32;
	public static int TYPE_BITLEN = 4;
	public static int CHAR_BITLEN = 8;
	public static int KEYLEN_BITLEN = 6;
	public static int INTLEN_BITLEN = 6;
	
	// TODO: DHIS2 actually supports a number of different separators
	public static String KVPAIRS_SEPARATOR = "|";
	public static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
}
