/*
# Copyright (C) 2018 by eHealth Africa : http://www.eHealthAfrica.org
#
# See the NOTICE file distributed with this work for additional information
# regarding copyright ownership.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#   http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.
*/

package org.ehealth.dhis2SmsComp;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

import org.ehealth.dhis2SmsComp.SSPConst.Version;
import org.ehealth.dhis2SmsComp.Models.SmsCode;
import org.ehealth.dhis2SmsComp.Models.SmsCommand;
import org.ehealth.dhis2SmsComp.Models.SmsSubmission;
import org.ehealth.dhis2SmsComp.Utils.BitInputStream;

/**
 * A class used to decode an SMS which was previously encoded using SMSEncoder
 * @see SMSEncoder
 * @author jaspertimm
 *
 */
public class SmsDecoder {
	private ArrayList<SmsCommand> smsCmdList;

	/**
	 * Instantiate a decoder given a list of the SmsCommands in this
	 * DHIS2 environment
	 * @param smsCmds
	 */
	public SmsDecoder(ArrayList<SmsCommand> smsCmds) {
		this.smsCmdList = new ArrayList<SmsCommand>(smsCmds);
		for (SmsCommand smsCmd : this.smsCmdList) {
			smsCmd.sortSmsCodes();
		}
	}
	
	/**
	 * A wrapper which simply prints the decoded SmsSubmission
	 * from decodeSMS
	 * @param smsBytes
	 * @return
	 * @throws Exception 
	 */
	public String decode(byte[] smsBytes) throws Exception {
		SmsSubmission subm = null;
		try {
			subm = decodeSMS(smsBytes, SSPConst.CUR_VERSION);			
		} catch (Exception e) {
			subm = decodeSMS(smsBytes, Version.ONE);
		}
		
		return subm.toString();
	}
	
	/**
	 * Decodes an SMS as a byte array previously encoded by SmsEncoder
	 * @param smsBytes
	 * @return
	 * @throws Exception 
	 */
	public SmsSubmission decodeSMS(byte[] smsBytes, Version decodeVer) throws Exception
    {
		ByteArrayInputStream byteStream = new ByteArrayInputStream(smsBytes);
		BitInputStream bitStream = new BitInputStream(byteStream);
		SmsSubmission subm = new SmsSubmission();
		
		//Read version number
		if (decodeVer == Version.ONE) {
			//Read command with OLD_CMD_BITLEN
			/*
			 * There's a bug where we cannot use this old way of looking up commands via index
			 * anymore, as the index doesn't stay the same when we add new commands.
			 * For now, IDSR is the only thing reported for Version.ONE, so we are simply
			 * hardcoding a lookup for this in the cmdList.
			 */
			for (SmsCommand smsCmd : smsCmdList) {
				if (smsCmd.name.contains("idsr")) {
					subm.currentSmsCmd = smsCmd;
				}
			}
		} else {
			int intVers = bitStream.read(SSPConst.VER_BITLEN);
			Version msgVers = Version.values()[intVers];
			if (msgVers != decodeVer) {
				bitStream.close();
				throw new Exception("Unsupported version: " + msgVers + ", expected: " + decodeVer);
			}
			//Read command
			int cmdHash = bitStream.read(SSPConst.CMD_BITLEN);
			subm.currentSmsCmd = SmsSubmission.findSmsCommand(cmdHash, smsCmdList);			
		}

		//Ensure we have a valid SMS command now
		Objects.requireNonNull(subm.currentSmsCmd);
		if (subm.currentSmsCmd.smsCodes == null || subm.currentSmsCmd.smsCodes.isEmpty()) {
			bitStream.close();
			throw new Exception("No SMS Codes for the given SMS Command: " + subm.currentSmsCmd.name);
		}
		
		//Read date of submission
		int submDate = bitStream.read(SSPConst.SUBM_DATE_BITLEN);
		if (submDate > 0) subm.submDate = submDate;
		
		//Read key length
		subm.keyLength = bitStream.read(SSPConst.KEYLEN_BITLEN);		
		
		//Read int length
		subm.intLength = bitStream.read(SSPConst.INTLEN_BITLEN);
		
		//Read remaining val
		subm.remainVal = decodeValue(bitStream, subm);
		
		//Read key value pairs
		ArrayList<SmsCode> remainCodes = new ArrayList<SmsCode>(subm.currentSmsCmd.smsCodes);
		subm.kvPairsMap = new HashMap<String, String>();
		while(true) {
			try {
				int keyId = bitStream.read(subm.keyLength);
				SmsCode smsCode = subm.currentSmsCmd.smsCodes.get(keyId);
				remainCodes.remove(smsCode);
				String val = decodeValue(bitStream, subm);
				if (val.isEmpty()) continue;
				subm.kvPairsMap.put(smsCode.code, val);
				
			} catch (EOFException e) {
				break;				
			}
		}
		
		//Write out the remaining codes as the remain val if it's not blank
		if (!subm.remainVal.isEmpty()) {
			for (SmsCode code : remainCodes) {
				subm.kvPairsMap.put(code.code, subm.remainVal);
			}
		}
		
		bitStream.close();
		return subm;
    }
	
	/**
	 * Detects the type of the next value from the bitStream and 
	 * returns it as a string
	 * @param bitStream
	 * @param subm
	 * @return
	 * @throws IOException
	 */
	private String decodeValue(BitInputStream bitStream, SmsSubmission subm) throws IOException {
		SimpleDateFormat sdf = new SimpleDateFormat(SSPConst.DATE_FORMAT);
		int typeId = bitStream.read(SSPConst.TYPE_BITLEN);
		SSPConst.SubmValueType valType = SSPConst.SubmValueType.values()[typeId] ;
		
		switch(valType) {
			case BLANK:
				return "";
				
			case BOOL:
				return bitStream.readBit() == 1 ? "true" : "false";
				
			case DATE:
				long epochSecs = bitStream.read(SSPConst.EPOCH_DATE_BITLEN);
				Date dateVal = new Date(epochSecs * 1000);
				return sdf.format(dateVal);
				
			case INT:
				int intVal = bitStream.read(subm.intLength);
				return Integer.toString(intVal);
				
			case STRING:
				String strVal = "";
				while (true) {
					int nextChar = bitStream.read(SSPConst.CHAR_BITLEN);
					if (nextChar == 0) break;
					strVal += (char) nextChar;
				}
				return strVal;
	
			default:
				throw new IOException("Unknown value type");
		}
		
	}
}
