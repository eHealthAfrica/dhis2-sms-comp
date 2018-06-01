package org.ehealth.dhis2SmsComp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.ehealth.dhis2SmsComp.Models.SmsCode;
import org.ehealth.dhis2SmsComp.Models.SmsCommand;
import org.ehealth.dhis2SmsComp.Models.SmsSubmission;
import org.ehealth.dhis2SmsComp.Utils.BinaryUtils;
import org.ehealth.dhis2SmsComp.Utils.BitOutputStream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Class used to encode a DHIS2 formatted SMS into a compressed binary form.
 * Requires a JSON of the SMS Commands used in that DHIS2 environment to be
 * instantiated.
 * @author jaspertimm
 *
 */
public class SmsEncoder {
	private ArrayList<SmsCommand> smsCmdList;
	
	/**
	 * Instantiates and SMSEncoder to be used to encode DHIS2 SMSs
	 * @param smsCmds, the JSON of SMS commands from the DHIS2 API
	 */
	public SmsEncoder(ArrayList<SmsCommand> smsCmds) {		
		this.smsCmdList = new ArrayList<SmsCommand>(smsCmds);
		for (SmsCommand smsCmd : this.smsCmdList) {
			smsCmd.sortSmsCodes();
		}
	}
	
	/**
	 * Wrapper which instantiates an SmsSubmission from the given
	 * sms string, then encodes it using encodeSMS
	 * @param sms
	 * @return
	 * @throws IOException
	 */
	public byte[] encode(String sms) throws IOException {
		SmsSubmission subm = new SmsSubmission(sms, this.smsCmdList);
		return encodeSMS(subm);
	}
	
	/**
	 * Encodes an SmsSubmission into a compressed binary format
	 * @param subm The SmsSubmission to be encoded
	 * @return
	 * @throws IOException
	 */
    public byte[] encodeSMS(SmsSubmission subm) throws IOException
    {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		BitOutputStream bitStream = new BitOutputStream(byteStream);
								
		//Write cmd hash
		int cmdHash = BinaryUtils.hash(subm.currentSmsCmd.name, SSPConst.CMD_BITLEN);
		bitStream.write(cmdHash, SSPConst.CMD_BITLEN);

		//Write out date
		bitStream.write(subm.submDate, SSPConst.SUBM_DATE_BITLEN);
		
		//Write out key length
		bitStream.write(subm.keyLength, SSPConst.KEYLEN_BITLEN);

		//Write out int length
		bitStream.write(subm.intLength, SSPConst.INTLEN_BITLEN);
		
		//Write out remain val
		detectTypeAndWriteValue(subm.remainVal, bitStream, subm);
		
		//Write out the key, value pairs
		ArrayList<SmsCode> remainCodes = new ArrayList<SmsCode>(subm.currentSmsCmd.smsCodes);
		for (String key : subm.kvPairsMap.keySet()) {
			String val = subm.kvPairsMap.get(key);
			SmsCode code = subm.findCode(key);
			remainCodes.remove(code);
			
			//Skip if it's the remaining value
			if (!val.equals(subm.remainVal) || subm.remainVal.isEmpty()) {
				int keyId = subm.currentSmsCmd.smsCodes.indexOf(code);
				if (keyId < 0) throw new RuntimeException("Code not present in smsCodes list: " + key);
				bitStream.write(keyId, subm.keyLength);
				detectTypeAndWriteValue(val, bitStream, subm);
			}
		}
				
		//If the remainVal isn't blank we need to set others to blank
		if (!subm.remainVal.isEmpty()) {
			for (SmsCode code : remainCodes) {
				int keyId = subm.currentSmsCmd.smsCodes.indexOf(code);
				bitStream.write(keyId, subm.keyLength);
				detectTypeAndWriteValue("", bitStream, subm);
			}
		}
		
		bitStream.close();
		return byteStream.toByteArray();
    }
    
    /**
     * Given a value, will detect its type and write it out to bitStream in binary
     * @param value
     * @param bitStream
     * @param subm
     * @throws IOException
     */
    private void detectTypeAndWriteValue(String value, BitOutputStream bitStream, SmsSubmission subm) throws IOException {
		SimpleDateFormat sdf = new SimpleDateFormat(SSPConst.DATE_FORMAT);

		//BLANK
		if (value.isEmpty()) {
			bitStream.write(SSPConst.SubmValueType.BLANK.ordinal(), SSPConst.TYPE_BITLEN);
			return;
		}
		
		//BOOL
    		if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
    			int val = value.equalsIgnoreCase("true") ? 1 : 0;
    			bitStream.write(SSPConst.SubmValueType.BOOL.ordinal(), SSPConst.TYPE_BITLEN);
    			bitStream.writeBit(val);
    			return;
    		}
    		
    		//DATE
		try {
			Date dateVal = sdf.parse(value);
			long epochSecs = dateVal.getTime() / 1000;			
			bitStream.write(SSPConst.SubmValueType.DATE.ordinal(), SSPConst.TYPE_BITLEN);
			bitStream.write((int)epochSecs, SSPConst.EPOCH_DATE_BITLEN);
			return;
		} catch (ParseException e) {
			//not a date
		}
    		
		//INT
		try {
	    		int intVal = Integer.parseInt(value);
	    		bitStream.write(SSPConst.SubmValueType.INT.ordinal(), SSPConst.TYPE_BITLEN);
	    		bitStream.write(intVal, subm.intLength);
	    		return;
		} catch (NumberFormatException e) {
			//not an integer
		}
    		
    		//STRING
		bitStream.write(SSPConst.SubmValueType.STRING.ordinal(), SSPConst.TYPE_BITLEN);
		for (char c : value.toCharArray()) {
			bitStream.write(c, SSPConst.CHAR_BITLEN);
		}
		//NULL TERMINATOR
		bitStream.write(0, SSPConst.CHAR_BITLEN);
		
		return;
    }
}
