package org.ehealth.dhis2SmsComp.Models;

import java.util.ArrayList;
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
    public String commandName;

    @Expose
    public String separator;
    
    @Expose
    @SerializedName("smsCode")
    public ArrayList<SmsCode> smsCodes;
    
    public void sortSmsCodes() {
    		smsCodes.sort(Comparator.comparing(SmsCode::getCreated));
    }
}
