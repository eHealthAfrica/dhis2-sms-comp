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

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;

import org.apache.commons.io.IOUtils;
import org.ehealth.dhis2SmsComp.Models.SmsCommand;
import org.ehealth.dhis2SmsComp.Utils.SmsCommandParser;

import junit.framework.Assert;
import junit.framework.TestCase;

public class TestDecoding extends TestCase {
	
    /**
     * Test the decoding of an SMS
     */
    public void testDecoding()
    {		
		FileReader cmdsJson = null;
		String decodedSMS = "";
		try {
			cmdsJson = new FileReader("src/test/resources/smsCommands.json");
			String encodedSMS = new String(Files.readAllBytes(Paths.get("src/test/resources/encodedSMS_v2.txt")));
			byte[] binEncSMS = Base64.getDecoder().decode(encodedSMS);
			ArrayList<SmsCommand> smsCmds = SmsCommandParser.parse(IOUtils.toString(cmdsJson));
			SmsDecoder dec = new SmsDecoder(smsCmds);
			decodedSMS = dec.decode(binEncSMS);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
						
		String expectedSMS = "";
		try {
			expectedSMS = new String(Files.readAllBytes(Paths.get("src/test/resources/decodedSMS.txt")));
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		
		Assert.assertEquals(expectedSMS, decodedSMS);
    }
    
    /**
     * Test the decoding of an SMS using the old SMS command format
     */
    public void testDecodingOldSMSCommands()
    {		
		FileReader cmdsJson = null;
		String decodedSMS = "";
		try {
			cmdsJson = new FileReader("src/test/resources/oldSmsCommands.json");
			String encodedSMS = new String(Files.readAllBytes(Paths.get("src/test/resources/encodedSMS_v2.txt")));
			byte[] binEncSMS = Base64.getDecoder().decode(encodedSMS);
			ArrayList<SmsCommand> smsCmds = SmsCommandParser.parse(IOUtils.toString(cmdsJson));
			SmsDecoder dec = new SmsDecoder(smsCmds);
			decodedSMS = dec.decode(binEncSMS);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
						
		String expectedSMS = "";
		try {
			expectedSMS = new String(Files.readAllBytes(Paths.get("src/test/resources/decodedSMS.txt")));
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		
		Assert.assertEquals(expectedSMS, decodedSMS);
    }

    /**
     * Test the decoding of an SMS using the old SMS command format
     */
    public void testDecoding_v1Msg()
    {		
		FileReader cmdsJson = null;
		String decodedSMS = "";
		try {
			cmdsJson = new FileReader("src/test/resources/smsCommands.json");
			String encodedSMS = new String(Files.readAllBytes(Paths.get("src/test/resources/encodedSMS_v1.txt")));
			byte[] binEncSMS = Base64.getDecoder().decode(encodedSMS);
			ArrayList<SmsCommand> smsCmds = SmsCommandParser.parse(IOUtils.toString(cmdsJson));
			SmsDecoder dec = new SmsDecoder(smsCmds);
			decodedSMS = dec.decode(binEncSMS);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
						
		String expectedSMS = "";
		try {
			expectedSMS = new String(Files.readAllBytes(Paths.get("src/test/resources/decodedSMS.txt")));
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		
		Assert.assertEquals(expectedSMS, decodedSMS);
    }
    
    /**
     * Test the decoding of an SMS using the old SMS command format
     */
    public void testDecoding_v1Msg_OldSMSCommands()
    {		
		FileReader cmdsJson = null;
		String decodedSMS = "";
		try {
			cmdsJson = new FileReader("src/test/resources/oldSmsCommands.json");
			String encodedSMS = new String(Files.readAllBytes(Paths.get("src/test/resources/encodedSMS_v1.txt")));
			byte[] binEncSMS = Base64.getDecoder().decode(encodedSMS);
			ArrayList<SmsCommand> smsCmds = SmsCommandParser.parse(IOUtils.toString(cmdsJson));
			SmsDecoder dec = new SmsDecoder(smsCmds);
			decodedSMS = dec.decode(binEncSMS);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
						
		String expectedSMS = "";
		try {
			expectedSMS = new String(Files.readAllBytes(Paths.get("src/test/resources/decodedSMS.txt")));
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		
		Assert.assertEquals(expectedSMS, decodedSMS);
    }
}
