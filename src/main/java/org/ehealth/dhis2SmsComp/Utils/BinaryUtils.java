package org.ehealth.dhis2SmsComp.Utils;

public class BinaryUtils {

	/**
	 * @return an array of bytes as a binary string
	 */
	public static String print(byte[] byteArray) {
		String output = "";
		for (byte b : byteArray) {
			output += print(b);
		}
		
		return output;
	}

	/**
	 * @return a single byte as a binary string
	 */
	public static String print(byte b) {
		return Integer.toBinaryString((b & 0xFF) + 0x100).substring(1);
	}

	/**
	 * @return the log2 of n
	 */
	public static int log2(int n){
	    if(n <= 0) throw new IllegalArgumentException();
	    return 31 - Integer.numberOfLeadingZeros(n);
	}
}
