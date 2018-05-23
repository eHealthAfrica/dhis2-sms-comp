package org.ehealth.dhis2SmsComp.Utils;

import java.util.ArrayList;
import java.util.Arrays;

import org.ehealth.dhis2SmsComp.Models.OldSmsCommand;
import org.ehealth.dhis2SmsComp.Models.SmsCommand;

import com.google.gson.Gson;

public class SmsCommandParser {

	public static ArrayList<SmsCommand> parse(String smsCmds) {
		Gson gson = new Gson();
		ArrayList<SmsCommand> smsList = new ArrayList<SmsCommand>();
		smsList.addAll(Arrays.asList(gson.fromJson(smsCmds, SmsCommand[].class)));
		return smsList;
	}
	
	public static ArrayList<SmsCommand> parseOld(String smsCmds) {
		Gson gson = new Gson();
		ArrayList<OldSmsCommand> oldSmsList = new ArrayList<OldSmsCommand>();		
		oldSmsList.addAll(Arrays.asList(gson.fromJson(smsCmds, OldSmsCommand[].class)));
		
		ArrayList<SmsCommand> smsList = new ArrayList<SmsCommand>();
		for(OldSmsCommand smsCmd : oldSmsList) {
			smsList.add(smsCmd.convertToNew());
		}
		
		return smsList;
	}	
}
