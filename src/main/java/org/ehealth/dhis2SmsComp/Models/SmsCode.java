package org.ehealth.dhis2SmsComp.Models;

import com.google.gson.annotations.Expose;

/**
 * Purely a JSON parsing class for a DHIS2 SmsCode
 * @author jaspertimm
 *
 */
public class SmsCode {
 
	@Expose
    public String code;

	public String getCreated() {
		return dataElement.created;
	}

	public String getCode() {
		return code;
	}	
	
	public DataElement dataElement;
		
    @Override
    public String toString() {
    		return code;
    }	
}
