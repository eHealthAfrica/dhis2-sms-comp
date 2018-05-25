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

	public String getCreated() {
		return dataElement.created;
	}

	public String getCode() {
		return code;
	}	
	
	public DataElement dataElement;
	private class DataElement {
		@Expose
		public String created;
	}
	
	
    @Override
    public String toString() {
    		return code;
    }	
}
