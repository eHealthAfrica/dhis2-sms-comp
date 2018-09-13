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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Purely a JSON parsing class for a DHIS2 SmsCommand
 * @author jaspertimm
 *
 */
public class SmsCommand {
    @Expose
    @SerializedName(value="name", alternate={"commandName"})
    public String name;

    @Expose
    public String separator;
    
    @Expose
    @SerializedName(value="smsCodes", alternate={"smsCode"})
    public ArrayList<SmsCode> smsCodes;
    
    public void sortSmsCodes() {
    		if (smsCodes != null && !smsCodes.isEmpty()) {
    			Collections.sort(smsCodes, new Comparator<SmsCode>() {
    			    @Override
    			    public int compare(SmsCode code1, SmsCode code2) {
    			    		int result = code1.getCreated().compareTo(code2.getCreated());
    			    		if (result == 0) result = code1.getCode().compareTo(code2.getCode());
    			        return result;
    			    }
    			});     			
    		}
    }
}
