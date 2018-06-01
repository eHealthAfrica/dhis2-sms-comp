package org.ehealth.dhis2SmsComp;

/**
 * A class of commonly used consts for SMS Submissions
 * @author jaspertimm
 *
 */
public class SSPConst {
	
	// An enum of the various value types in a submission
	public enum SubmValueType {
		INT,
		DATE,
		BOOL,
		STRING,
		BLANK
	}
	
	// The bit lengths of each part of the message are statically set here
	
	// NB: The CMD_BITLEN is important, as it's the size of the hash used to uniquely id each command
	// We use this hash collision calculator: http://everydayinternetstuff.com/2015/04/hash-collision-probability-calculator/
	// At 16-bit, for an env with 25 SMS commands, the probability of collision is < 0.5% 
	public static int CMD_BITLEN = 16;
	public static int SUBM_DATE_BITLEN = 12;
	public static int EPOCH_DATE_BITLEN = 32;
	public static int TYPE_BITLEN = 4;
	public static int CHAR_BITLEN = 8;
	public static int KEYLEN_BITLEN = 6;
	public static int INTLEN_BITLEN = 6;
	
	// TODO: DHIS2 actually supports a number of different separators
	public static String KVPAIRS_SEPARATOR = "|";
	public static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
}
