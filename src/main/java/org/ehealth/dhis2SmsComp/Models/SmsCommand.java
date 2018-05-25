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
