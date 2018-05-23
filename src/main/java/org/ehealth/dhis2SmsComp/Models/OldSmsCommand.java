package org.ehealth.dhis2SmsComp.Models;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Purely a JSON parsing class for a DHIS2 SmsCommand
 * @author jaspertimm
 *
 */
public class OldSmsCommand {
    @Expose
    public String commandName;
    @Expose
    public String separator;
    @Expose
    @SerializedName("smsCode")
    public ArrayList<OldSmsCode> smsCodes;
    
    public SmsCommand convertToNew() {
    	SmsCommand smsCmd = new SmsCommand();
    	smsCmd.name = this.commandName;
    	smsCmd.separator = this.separator;
    	
    	if (this.smsCodes != null) {
        	smsCmd.smsCodes = new ArrayList<SmsCode>();
	    	for (OldSmsCode oldCode : this.smsCodes) {
	    		smsCmd.smsCodes.add(oldCode.convertToNew());
	    	}
    	}
    	return smsCmd;
    }
}
