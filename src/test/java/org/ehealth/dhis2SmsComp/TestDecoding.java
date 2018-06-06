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
