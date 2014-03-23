/*
 * Copyright 2002-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opentides.util;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class StringUtil {
    
	private static String zeros = "0000000000";
	private static Random random = new Random((new Date()).getTime());
	
	/**
	 * Checks if a given string is empty or null.
	 * 
	 * @param obj
	 * @return
	 */
    public static boolean isEmpty(String obj) {
    	if ((obj==null) || (obj.trim().length()==0))
    		return true;
    	else
    		return false;
    }
    
    /**
     * <p>Replaces all occurrences of a String within another String.</p>
     * 
     * <p>A {@code null} reference passed to this method is a no-op.</p>
     * 
     * <p>Copied from Apache Commons StringUtils. </p>
     * <pre>
     * StringUtils.replace(null, *, *)        = null
     * StringUtils.replace("", *, *)          = ""
     * StringUtils.replace("any", null, *)    = "any"
     * StringUtils.replace("any", *, null)    = "any"
     * StringUtils.replace("any", "", *)      = "any"
     * StringUtils.replace("aba", "a", null)  = "aba"
     * StringUtils.replace("aba", "a", "")    = "b"
     * StringUtils.replace("aba", "a", "z")   = "zbz"
     * </pre>
     *
     * @see #replace(String text, String searchString, String replacement, int max)
     * @param text  text to search and replace in, may be null
     * @param searchString  the String to search for, may be null
     * @param replacement  the String to replace it with, may be null
     * @return the text with any replacements processed,
     *  {@code null} if null String input
     */
    public static String replace(String text, String searchString, String replacement) {
        return replace(text, searchString, replacement, -1);
    }

    /**
     * <p>Replaces a String with another String inside a larger String,
     * for the first {@code max} values of the search String.</p>
     *
     * <p>A {@code null} reference passed to this method is a no-op.</p>
     *
     * <p>Copied from Apache Commons StringUtils. </p>
     * <pre>
     * StringUtils.replace(null, *, *, *)         = null
     * StringUtils.replace("", *, *, *)           = ""
     * StringUtils.replace("any", null, *, *)     = "any"
     * StringUtils.replace("any", *, null, *)     = "any"
     * StringUtils.replace("any", "", *, *)       = "any"
     * StringUtils.replace("any", *, *, 0)        = "any"
     * StringUtils.replace("abaa", "a", null, -1) = "abaa"
     * StringUtils.replace("abaa", "a", "", -1)   = "b"
     * StringUtils.replace("abaa", "a", "z", 0)   = "abaa"
     * StringUtils.replace("abaa", "a", "z", 1)   = "zbaa"
     * StringUtils.replace("abaa", "a", "z", 2)   = "zbza"
     * StringUtils.replace("abaa", "a", "z", -1)  = "zbzz"
     * </pre>
     *
     * @param text  text to search and replace in, may be null
     * @param searchString  the String to search for, may be null
     * @param replacement  the String to replace it with, may be null
     * @param max  maximum number of values to replace, or {@code -1} if no maximum
     * @return the text with any replacements processed,
     *  {@code null} if null String input
     */
    public static String replace(String text, String searchString, String replacement, int max) {
        if (isEmpty(text) || isEmpty(searchString) || replacement == null || max == 0) {
            return text;
        }
        int start = 0;
        int end = text.indexOf(searchString, start);
        if (end == -1) {
            return text;
        }
        int replLength = searchString.length();
        int increase = replacement.length() - replLength;
        increase = increase < 0 ? 0 : increase;
        increase *= max < 0 ? 16 : max > 64 ? 64 : max;
        StringBuilder buf = new StringBuilder(text.length() + increase);
        while (end != -1) {
            buf.append(text.substring(start, end)).append(replacement);
            start = end + replLength;
            if (--max == 0) {
                break;
            }
            end = text.indexOf(searchString, start);
        }
        buf.append(text.substring(start));
        return buf.toString();
    }


    /**
     * Escapes special characters for HQL.
     * @param str
     * @param escapeForLike
     */
    public static String escapeSql(String str, boolean escapeForLike){
		if (str == null) {
			return null;
		} else {
			str = StringUtil.replace(str, "'", "''");
			if(escapeForLike) {
				//escape special characters for LIKE
				str = StringUtil.replace(str, "\\", "\\\\\\\\");
				str = StringUtil.replace(str, "%", "\\%");
				str = StringUtil.replace(str, "_", "\\_");
			} else {
				//escape backslash for EQUALS (=) 
				str = StringUtil.replace(str, "\\", "\\\\");
			}
		}
    	return str;
    }
    
    /**
     * Converts the given string in hex to a byte array.
     * 
     * For example: 
     *     byte[] CDRIVES = convertHexToArray("e04fd020ea3a6910a2d808002b30309d");
	 *
     * @param s
     * @return
     */
    public static byte[] convertHexToArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                                 + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
    
    /**
     * Converts the given number to a fixed length.
     * Note: max length is 10.
     * 
     * @param value
     * @param length
     * @return
     */
    public static String toFixedString(int value, int length) {
    	String val = Integer.toString(value);
    	int diff = length-val.length();
    	if (diff>0)
    		return (zeros.substring(10-diff)+val);
    	else
    		return val;
    }
    
    /**
     * Removes HTML tags in an HTML string using regex.
     * 
     * @param html
     * @return
     */
    public static String removeHTMLTags(String html) {
    	return  html.replaceAll("<(.*?)>"," ")
    				.replaceAll("\\s+"," ");
    }
    
    /** 
     * Converts the given string to int. 
     * If string is not a number, defValue is used.
     * 
     * @param str
     * @param defValue
     * @return
     */
    public static int convertToInt(String str, int defValue) {
		int value = defValue;
		try {
			value = Integer.parseInt(str);
		} catch (NumberFormatException nfe) {
			// do nothing...
		}
		return value;
    }
    
    /** 
     * Converts the given string to long. 
     * If string is not a number, defValue is used.
     * 
     * @param str
     * @param defValue
     * @return
     */
    public static long convertToLong(String str, long defValue) {
		long value = defValue;
		try {
			value = Long.parseLong(str);
		} catch (NumberFormatException nfe) {
			// do nothing...
		}
		return value;
    }
    
    /** 
     * Converts the given string to double. 
     * If string is not a number, defValue is used.
     * 
     * @param str
     * @param defValue
     * @return
     */
    public static double convertToDouble(String str, double defValue){
    	double doub = defValue;
		try {
			doub = Double.parseDouble(str);			
		} catch (NumberFormatException nfe) {
			// do nothing...
		}
    	return doub;
    }
    
    public static Float convertToFloat(String str, Float defValue) {
		Float value = null;
		try {
			value = Float.parseFloat(str);
		} catch (NumberFormatException nfe) {
			// do nothing...
		}
		return value;
    }

    /**
     * generates an alphanumeric string based on specified length.
     * @param length # of characters to generate
     * @return random string
     */
    public static String generateRandomString(int length) {
    	char[] values = {'a','b','c','d','e','f','g','h','i','j',
    					 'k','l','m','n','o','p','q','r','s','t',
    					 'u','v','w','x','y','z','0','1','2','3',
    					 '4','5','6','7','8','9','A','B','C','D',
    					 'E','F','G','H','I','J','K','L','M','N',
    					 'O','P','Q','R','S','T','U','V','W','X',
    					 'Y','Z'};
    	String out = "";
    	for (int i=0;i<length;i++) {
        	int idx=random.nextInt(values.length);
    		out += values[idx];
    	}
    	return out;
    }
    
 
	/**
	 * Encrypt password by using SHA-256 algorithm, encryptedPassword length is 32 bits
	 * @param clearTextPassword
	 * @return
	 * @throws NoSuchAlgorithmException
	 * reference http://java.sun.com/j2se/1.4.2/docs/api/java/security/MessageDigest.html
	 */
	@SuppressWarnings("restriction")
	public static String getEncryptedPassword(String clearTextPassword)	throws NoSuchAlgorithmException {	
		// handler for empty string
		if (StringUtil.isEmpty(clearTextPassword))
			return"";
		
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(clearTextPassword.getBytes());
		return new sun.misc.BASE64Encoder().encode(md.digest());
	}

    /**
     * Encrypts the string along with salt 
     * @param userId
     * @return
     * @throws Exception
     */
	@SuppressWarnings("restriction")	
	public static String encrypt(String userId) {	
		sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();

		// let's create some dummy salt
		byte[] salt = new byte[8];
		random.nextBytes(salt);
		return encoder.encode(salt)+
			encoder.encode(userId.getBytes());
	}

    	
	/**
	 * Decrypts the string and removes the salt
	 * @param encryptKey
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("restriction")
	public static String decrypt(String encryptKey) throws Exception {
		// let's ignore the salt
		if (!StringUtil.isEmpty(encryptKey) &&
				encryptKey.length() > 12) {
			String cipher = encryptKey.substring(12);
			sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
			try {
				return new String(decoder.decodeBuffer(cipher));
			} catch (IOException e) {
				throw new Exception(
						"Failed to perform decryption for key ["+encryptKey+"]",e);
			}
		} else
			return null;				
	}
	
	/**
	 * Parse a line of text in standard CSV format and returns array of Strings
	 * @param csvLine
	 * @return
	 */
    public static List<String> parseCsvLine(String csvLine) {
    	return StringUtil.parseCsvLine(csvLine, ',', '"', '\\', false);
    }
    
	/**
	 * Parse a line of text in CSV format and returns array of Strings
	 * Implementation of parsing is extracted from open-csv.
	 * http://opencsv.sourceforge.net/
	 * 
	 * @param csvLine
	 * @param separator
	 * @param quotechar
	 * @param escape
	 * @param strictQuotes
	 * @return
	 * @throws IOException
	 */
    public static List<String> parseCsvLine(String csvLine, 
    									char separator, char quotechar, 
    									char escape, boolean strictQuotes) {
    	
        List<String>tokensOnThisLine = new ArrayList<String>();
        StringBuilder sb = new StringBuilder(50);
        boolean inQuotes = false;
        for (int i = 0; i < csvLine.length(); i++) {
        	char c = csvLine.charAt(i);
        	if (c == escape) {
    		    boolean isNextCharEscapable = inQuotes  // we are in quotes, therefore there can be escaped quotes in here.
    		    						    && csvLine.length() > (i+1)  // there is indeed another character to check.
    		    						    && ( csvLine.charAt(i+1) == quotechar || csvLine.charAt(i+1) == escape);

        		if( isNextCharEscapable ){
        			sb.append(csvLine.charAt(i+1));
        			i++;
        		} 
        	} else if (c == quotechar) {
        		boolean isNextCharEscapedQuote = inQuotes  // we are in quotes, therefore there can be escaped quotes in here.
								&& csvLine.length() > (i+1)  // there is indeed another character to check.
								&& csvLine.charAt(i+1) == quotechar;
        		if( isNextCharEscapedQuote ){
        			sb.append(csvLine.charAt(i+1));
        			i++;
        		}else{
        			inQuotes = !inQuotes;
        			// the tricky case of an embedded quote in the middle: a,bc"d"ef,g
                    if (!strictQuotes) {
                        if(i>2 //not on the beginning of the line
                                && csvLine.charAt(i-1) != separator //not at the beginning of an escape sequence
                                && csvLine.length()>(i+1) &&
                                csvLine.charAt(i+1) != separator //not at the end of an escape sequence
                        ){
                            sb.append(c);
                        }
                    }
        		}
        	} else if (c == separator && !inQuotes) {
        		tokensOnThisLine.add(sb.toString());
        		sb = new StringBuilder(50); // start work on next token
        	} else {
                if (!strictQuotes || inQuotes)
                    sb.append(c);
        	}
        }
        // line is done - check status
        if (inQuotes) {
        	// _log.warn("Un-terminated quoted field at end of CSV line. \n ["+csvLine+"]");
        }
        if (sb != null) {
        	tokensOnThisLine.add(sb.toString());
        }
        return tokensOnThisLine;
    }
    
    /**
     * Combines an array of string into one string using the specified separator.
     * @param separator
     * @param input
     * @return
     */
    public static final String explode(char separator, String[] input) {
    	if (input==null) 
    		return null;
    	int count=0;
    	StringBuffer out = new StringBuffer();
    	for (String word:input) {
    		if (count++ > 0)
    			out.append(separator);
    		out.append(word);
    	}
    	return out.toString();
    }

}
