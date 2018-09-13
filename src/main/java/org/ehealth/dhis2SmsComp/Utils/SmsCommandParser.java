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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import org.ehealth.dhis2SmsComp.Models.SmsCommand;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class SmsCommandParser {

	public static ArrayList<SmsCommand> parse(String smsCmds) {
		Gson gson = new Gson();
		
		//If we have an object, get the smsCommands array
		Object testObj = gson.fromJson(smsCmds, Object.class);
		if (testObj instanceof Map) {
			Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
			Map<String, Object> obj = gson.fromJson(smsCmds, mapType);
			smsCmds = gson.toJson(obj.get("smsCommands"));
		}
		
		ArrayList<SmsCommand> smsList = new ArrayList<SmsCommand>();
		smsList.addAll(Arrays.asList(gson.fromJson(smsCmds, SmsCommand[].class)));
		return smsList;
	}	
}
