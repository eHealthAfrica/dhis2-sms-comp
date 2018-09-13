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

package org.ehealth.dhis2SmsComp.Utils;

public class BinaryUtils {

	/**
	 * @return an array of bytes as a binary string
	 */
	public static String print(byte[] byteArray) {
		String output = "";
		for (byte b : byteArray) {
			output += print(b);
		}
		
		return output;
	}

	/**
	 * @return a single byte as a binary string
	 */
	public static String print(byte b) {
		return Integer.toBinaryString((b & 0xFF) + 0x100).substring(1);
	}

	/**
	 * @return the log2 of n
	 */
	public static int log2(int n){
	    if(n <= 0) throw new IllegalArgumentException();
	    return 31 - Integer.numberOfLeadingZeros(n);
	}
	
	/**
	 * @return a unique hash for a string for a given bit length
	 */
	public static int hash(String s, int bitlen) {
		return Math.abs(s.hashCode()) % (int)Math.pow(2, bitlen);
	}
}
