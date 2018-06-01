package org.ehealth.dhis2SmsComp.Models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import org.ehealth.dhis2SmsComp.SSPConst;
import org.ehealth.dhis2SmsComp.Utils.BinaryUtils;

/**
 * 
 * @author jaspertimm
 *
 * A class which represents a DHIS2 SMS Submission
 * The actual data values are stored in the HashMap kvPairsMap
 */

public class SmsSubmission {
	public SmsCommand currentSmsCmd;
	public int keyLength;
	public int submDate;	
	public HashMap<String, String> kvPairsMap;
	public String remainVal;	
	public int intLength;
	
	/**
	 * Empty constructor used when decoding an SMS
	 */
	public SmsSubmission() {
		this.currentSmsCmd = null;
		this.keyLength = 0;
		this.submDate = 0;
		this.kvPairsMap = new HashMap<String, String>();
		this.remainVal = "";
		this.intLength = 0;
	}
	
	/**
	 * Create an SmsSubmission from the standard DHIS2 format for an SMS.
	 * @param sms
	 * @param smsCmdList
	 */
	public SmsSubmission(String sms, ArrayList<SmsCommand> smsCmdList) {
		String[] smsSplit = sms.split(" ", 3);
		String smsCmdName = smsSplit[0];
						
		//Find sms command
		this.currentSmsCmd = findSmsCommand(smsCmdName, smsCmdList);		
		Objects.requireNonNull(this.currentSmsCmd);
		
		//Calculate key length
		this.keyLength = BinaryUtils.log2(this.currentSmsCmd.smsCodes.size()) + 1;		
		
		//Check for date, set kvPairs to the rest of the string
		String keyValueStr;
		try {
			int dateNum = Integer.parseInt(smsSplit[1]);
			this.submDate = dateNum; 
			keyValueStr = smsSplit[2];
		} catch (NumberFormatException e) {
			//no date, write out 0 and grab remainder as kvPairs
			this.submDate = 0;
			keyValueStr = smsSplit[1];
			if (smsSplit.length > 2) keyValueStr += smsSplit[2];
		}
		
		//Set other submission params
		setSubmissionParams(keyValueStr);
	}
	
	/**
	 * Look through the string of key value pairs and set up the params of this
	 * sms submission accordingly, i.e. the bit length of ints given the highest value
	 * and the remaining value: the value for all keys which won't be specified 
	 * @param keyValueStr
	 */
    private void setSubmissionParams(String keyValueStr) {
		//TODO: Shouldn't do this, we don't know it needs to be escaped
		String[] kvPairsList = keyValueStr.split("\\" + SSPConst.KVPAIRS_SEPARATOR);
		HashMap<String, String> keyValues = new HashMap<String, String>();
		HashMap<String, Integer> valueCounts = new HashMap<String, Integer>();
		int highestValCount = 0;
		int highestIntVal = 0;
		
		for (String kvPair : kvPairsList) {
			String[] kvSplit = kvPair.split(this.currentSmsCmd.separator, 2);
			String key = kvSplit[0], val = kvSplit[1];
			keyValues.put(key, val);
			
			//TODO: Getting issues in Android with .putIfAbsent so we
			//need to write this out explicitly
			if (!valueCounts.containsKey(val)) valueCounts.put(val, 0);
			
			int valCount = valueCounts.get(val) + 1;
			valueCounts.put(val, valCount);
			
			if (valCount > highestValCount) {
				this.remainVal = val;
				highestValCount = valCount;
			}
			
			//Check for ints, update highestIntVal
			try {
	    			int intVal = Integer.parseInt(val);
	    			highestIntVal = intVal > highestIntVal ? intVal : highestIntVal;
			} catch (NumberFormatException e) {
				//not an integer
			}
		}

		//If we've got more blanks than one specific value, set remainVal as blank
		int numBlanks = this.currentSmsCmd.smsCodes.size() - kvPairsList.length;
		if (numBlanks > highestValCount) this.remainVal = "";
		
		//Set int length		
		this.intLength = highestIntVal > 0 ? BinaryUtils.log2(highestIntVal) + 1 : 1;
		
		//Set key values map
		this.kvPairsMap = keyValues;
		
		return;
    }	
	
    @Override
    public String toString() {
		String sms = "";
    		if (this.currentSmsCmd == null) return sms;
    		
    		sms += this.currentSmsCmd.name + " ";
    		if (this.submDate > 0) sms += String.format("%04d", this.submDate) + " ";
    		
    		for (String key : this.kvPairsMap.keySet()) {
    			String val = this.kvPairsMap.get(key);
    			sms += key;
    			sms += this.currentSmsCmd.separator;
    			sms += val;
    			sms += SSPConst.KVPAIRS_SEPARATOR;
    		}
    		
    		return sms;
    }
    
    /**
     * @param smsCmdName
     * @param smsCmdList
     * @return the SmsCommand if found
     */
    private SmsCommand findSmsCommand(String smsCmdName, ArrayList<SmsCommand> smsCmdList) {    	
		for (SmsCommand cmd : smsCmdList) {
			if (cmd.name.equals(smsCmdName)) {
				return cmd;
			}
		}
		
		return null;
    }
	
    /**
     * @param smsCmdName
     * @param smsCmdList
     * @return the SmsCommand if found
     */
    public static SmsCommand findSmsCommand(int smsCmdHash, ArrayList<SmsCommand> smsCmdList) {
		for (SmsCommand cmd : smsCmdList) {
			if (BinaryUtils.hash(cmd.name, SSPConst.CMD_BITLEN) == smsCmdHash) {
				return cmd;
			}
		}
		
		return null;
    }    
    
    /**
     * @param key
     * @return the SmsCode if found
     */
    public SmsCode findCode(String key) {    		
		for (SmsCode smsCode : this.currentSmsCmd.smsCodes) {
			if (smsCode.code.equals(key)) {
				return smsCode;
			}
		}
		
		return null;
    }
    
}

