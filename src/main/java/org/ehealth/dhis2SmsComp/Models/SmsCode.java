package org.ehealth.dhis2SmsComp.Models;

import com.google.gson.annotations.Expose;

/**
 * Purely a JSON parsing class for a DHIS2 SmsCode
 * @author jaspertimm
 *
 */
public class SmsCode {
 
	@Expose
    public String smsCode;

	public String getCreated() {
		return dataElement.created;
	}
	
	public DataElement dataElement;
	
	private class DataElement {
		@Expose
		public String created;
	}
}
