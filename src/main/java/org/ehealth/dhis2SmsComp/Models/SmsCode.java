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

package org.ehealth.dhis2SmsComp.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Purely a JSON parsing class for a DHIS2 SmsCode
 * @author jaspertimm
 *
 */
public class SmsCode {
 
	@Expose
	@SerializedName(value="code", alternate={"smsCode"})
    public String code;

	@Expose
	public DataElement dataElement;
	private class DataElement {
		@Expose
		public String created;
	}
	
	public String getCreated() {
		return dataElement.created;
	}

	public String getCode() {
		return code;
	}	
	
    @Override
    public String toString() {
    		return code;
    }	
}
