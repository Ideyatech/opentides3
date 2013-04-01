/*
   Licensed to the Apache Software Foundation (ASF) under one
   or more contributor license agreements.  See the NOTICE file
   distributed with this work for additional information
   regarding copyright ownership.  The ASF licenses this file
   to you under the Apache License, Version 2.0 (the
   "License"); you may not use this file except in compliance
   with the License.  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing,
   software distributed under the License is distributed on an
   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
   KIND, either express or implied.  See the License for the
   specific language governing permissions and limitations
   under the License.    
 */

package org.opentides.util;

import java.net.InetAddress;

import org.apache.log4j.Logger;

/**
 * Utility class for handling Url strings.
 * 
 * @author ideyatech
 * 
 */
public class UrlUtil {
	
	private static Logger _log = Logger.getLogger(UrlUtil.class);
	
	/**
	 * Checks if the given url starts with protocol (e.g.
	 * "http://www.ideyatech.com")
	 * 
	 * @param url
	 * @return
	 */
	public static boolean hasProtocol(String url) {
		return url.contains("://");
	}

	/**
	 * Ensures url has a protocol. If none, the protocol is prepended on the url
	 * and returned as String.
	 * 
	 * @param url
	 * @param protocol
	 * @return
	 */
	public static String ensureProtocol(String url, String protocol) {
		if (hasProtocol(url))
			return url;
		else
			return protocol + "://" + url;
	}

	/**
	 * Ensures url has protocol. If none, http:// is prepended on the url and
	 * returned as String.
	 * 
	 * @param url
	 * @return
	 */
	public static String ensureProtocol(String url) {
		return ensureProtocol(url, "http");
	}

	/**
	 * Returns the hostname of the given url
	 * 
	 * @param url
	 * @return
	 */
	public static String getHostname(String url) {
		int startIndex = 0;
		if (hasProtocol(url)) {
			startIndex = url.lastIndexOf("://") + 3;
		}
		int endIndex = url.indexOf("/", startIndex);
		if (endIndex < 0)
			endIndex = url.length();
		return url.substring(startIndex, endIndex);
	}

	/**
	 * Returns the hostname of the given url
	 * 
	 * @param url
	 * @return
	 */
	public static String getProtocol(String url) {
		if (hasProtocol(url)) {
			return url.substring(0, url.indexOf("://"));
		} else
			return "";
	}

	/**
	 * Returns the correct absolute path of the given src relative to the url.
	 * For example: url=http://hostname/sample.html, src=/test.gif ->
	 * http://hostname/test.gif src=http://hostname/folder/sample.html,
	 * src=test.gif -> http://hostname/folder/test.gif
	 * 
	 * @param url
	 *            - url location where src is specified
	 * @param src
	 *            - src reference
	 * @return
	 */
	public static String getAbsoluteUrl(String url, String src) {
		// if src has protocol (e.g. http://...), then return as is.
		if (hasProtocol(src))
			return src;
		// if src is from root context.
		if (src.startsWith("/")) {
			String protocol = getProtocol(url);
			String hostname = getHostname(url);
			return protocol + "://" + hostname + src;
		}
		// if src is from folder
		String protocol = getProtocol(url);
		int startIndex = (url.indexOf("://") > 0) ? url.indexOf("://") + 3 : 0;
		String tmpString = url.substring(startIndex);
		int endIndex = tmpString.lastIndexOf("/");
		if (endIndex < 0)
			endIndex = url.length();
		else
			endIndex += startIndex;

		return protocol + "://" + url.substring(startIndex, endIndex) + "/"
				+ src;
	}

	/**
	 * Return the URL parameter value of the given parameter name.
	 * 
	 * @param {String} url The url source
	 * @param {String} paramName The name of the parameter.
	 * @return {String} strReturn extracted value
	 */
	public static String getURLParam(String url, String paramName) {
		String strReturn = "";
		String strHref = url;
		if (strHref.indexOf("?") > -1) {
			String strQueryString = strHref.substring(strHref.indexOf("?"));

			String[] aQueryString = strQueryString.split("&");

			for (int iParam = 0; iParam < aQueryString.length; iParam++) {
				if (aQueryString[iParam].indexOf(paramName + "=") > -1) {
					String[] aParam = aQueryString[iParam].split("=");
					strReturn = aParam[1];
					strReturn = strReturn.split("#")[0];
					break;
				}
			}
		}
		return strReturn;
	}

	/**
	 * Helper function that converts string of IP Address 
	 * into an InetAddress
	 * 
	 * @param IPString
	 * @return InetAddress
	 */
	public static InetAddress convertIPString(String IPString) {
		try {
			// convert IP String to bytes
			String[] IPAddress = IPString.split("\\.");
			byte[] IPBytes = { (byte) Integer.parseInt(IPAddress[0]),
					(byte) Integer.parseInt(IPAddress[1]),
					(byte) Integer.parseInt(IPAddress[2]),
					(byte) Integer.parseInt(IPAddress[3]) };
			return InetAddress.getByAddress(IPBytes);
		} catch (Exception uhe) {
			_log.error("FAILED to convert IP address  [" + IPString + "]",
					uhe);
			return null;
		}
	}
    
}
