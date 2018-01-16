package org.ehealth.dhis2SmsComp;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

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
			String encodedSMS = new String(Files.readAllBytes(Paths.get("src/test/resources/encodedSMS.txt")));
			byte[] binEncSMS = Base64.getDecoder().decode(encodedSMS);
			SmsDecoder dec = new SmsDecoder(cmdsJson);    					
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
