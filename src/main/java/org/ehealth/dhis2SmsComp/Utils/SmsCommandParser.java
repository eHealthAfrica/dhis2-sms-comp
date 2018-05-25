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
