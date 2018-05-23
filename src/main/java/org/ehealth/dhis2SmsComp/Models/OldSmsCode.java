package org.ehealth.dhis2SmsComp.Models;

import com.google.gson.annotations.Expose;

/**
 * Purely a JSON parsing class for a DHIS2 SmsCode
 * @author jaspertimm
 *
 */
public class OldSmsCode {
	@Expose
    public String smsCode;
	public DataElement dataElement;	
	
	public SmsCode convertToNew() {
		SmsCode newSmsCode = new SmsCode();
		newSmsCode.code = this.smsCode;
		newSmsCode.dataElement = this.dataElement;
		return newSmsCode;
	}
}
