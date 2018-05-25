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

public class TestEncoding extends TestCase {	
	
    /**
     * Test the encoding of an SMS
     */
    public void testEncoding()
    {		
		FileReader cmdsJson = null;
		byte[] binarySubm = {};
		try {
			cmdsJson = new FileReader("src/test/resources/smsCommands.json");
			String testSMS = new String(Files.readAllBytes(Paths.get("src/test/resources/testSMS.txt")));
			ArrayList<SmsCommand> smsCmds = SmsCommandParser.parse(IOUtils.toString(cmdsJson));
			SmsEncoder enc = new SmsEncoder(smsCmds);    					
			binarySubm = enc.encode(testSMS);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
				
		String base64Subm = Base64.getEncoder().encodeToString(binarySubm);
		String encodedSMS = "";
		try {
			encodedSMS = new String(Files.readAllBytes(Paths.get("src/test/resources/encodedSMS.txt")));
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		
		Assert.assertEquals(encodedSMS, base64Subm);
    }
    
    
    /**
     * Test the encoding of an SMS using the old SMS command format
     */
    public void testEncodingOld()
    {		
		FileReader cmdsJson = null;
		byte[] binarySubm = {};
		try {
			cmdsJson = new FileReader("src/test/resources/oldSmsCommands.json");
			String testSMS = new String(Files.readAllBytes(Paths.get("src/test/resources/testSMS.txt")));
			ArrayList<SmsCommand> smsCmds = SmsCommandParser.parse(IOUtils.toString(cmdsJson));
			SmsEncoder enc = new SmsEncoder(smsCmds);
			binarySubm = enc.encode(testSMS);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
				
		String base64Subm = Base64.getEncoder().encodeToString(binarySubm);
		String encodedSMS = "";
		try {
			encodedSMS = new String(Files.readAllBytes(Paths.get("src/test/resources/encodedSMS.txt")));
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		
		Assert.assertEquals(encodedSMS, base64Subm);
    }    
}
