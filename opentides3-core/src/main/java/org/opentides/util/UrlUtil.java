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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.opentides.bean.UrlResponseObject;

/**
 * Utility class for handling Url strings.
 * 
 * @author ideyatech
 * 
 */
public class UrlUtil {
	
	private static final Logger _log = Logger.getLogger(UrlUtil.class);
	private static final int CONNECTION_TIMEOUT = 15000;
	
	/**
	 * Hide the constructor.
	 */
	private UrlUtil() {		
	}
	
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
	public static String getRefererURI(HttpServletRequest request) {
		String refererURI = "";
		try {
			refererURI = new URI(request.getHeader("referer")).getPath();
		} catch (URISyntaxException e) {
			_log.log(Level.ERROR, "Failed to get Referer URI.", e);
		}
		return refererURI;
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
	
	/**
	 * Returns the HTML code of the original engine. Takes the URL to connect to
	 * the engine. Also takes encoding type that overrides default if not null
	 * "UTF8" is typical encoding type
	 * 
	 * @param queryURL
	 *            - URL of engine to retrieve
	 * @param request
	 *            - request object
	 * @param param
	 *            - additional parameters
	 *         	  - Valid parameters are:
	 *         	  - methodName - Either "POST" or "GET". Default is "POST"   
	 *            - forwardCookie - if true, will forward cookies found on request object
	 *            - IPAddress - if specified, this IP will be used for the request
	 *  
	 * Note: This method required commons httpclient which is NOT defined in this project's
	 * POM. Please MANUALLY include httpclient in your application if you need this function.
	 * 
	 */
	public static final UrlResponseObject getPage(final String queryURL,
								  	   final HttpServletRequest request,
									   final Map<String, Object> param) {
		// determine if get or post method
		HttpMethodBase httpMethodBase;
		Boolean forwardCookie = false;
		InetAddress IPAddress = null;
		
		if (param!=null) {
			if (param.get("forwardCookie")!=null)
				forwardCookie = (Boolean) param.get("forwardCookie");
			
			if (param.get("IPAddress")!=null) {
				String IPString = (String) param.get("IPAddress");
				if (!StringUtil.isEmpty(IPString)) {
					IPAddress = convertIPString(IPString);
				}
			}
		}
		if (param!=null && "GET".equals((String) param.get("methodName"))) {
			httpMethodBase = new GetMethod(queryURL);
		} else {
			httpMethodBase = new PostMethod(queryURL);
		}

		try {
			// declare the connection objects
			HttpClient client = new HttpClient();
			HostConfiguration hostConfig = new HostConfiguration();
			String userAgent = request.getHeader("User-Agent");
				
			// for debugging
			if (_log.isDebugEnabled())
				_log.debug("Retrieving page from " + queryURL);

			// initialize the connection settings
			client.getHttpConnectionManager().getParams()
					.setConnectionTimeout(CONNECTION_TIMEOUT);
			client.getParams().setBooleanParameter(
					HttpClientParams.ALLOW_CIRCULAR_REDIRECTS, true);
			httpMethodBase.addRequestHeader("accept", "*/*");
			httpMethodBase.addRequestHeader("accept-language", "en-us");
			httpMethodBase.addRequestHeader("user-agent", userAgent);

			if (forwardCookie) {
				// get cookies from request
				Cookie[] cookies = request.getCookies();
				String cookieString = "";
				for (Cookie c : cookies) {
					cookieString += c.getName() + "=" + c.getValue() + "; ";
				}
				
				// forward cookies to httpMethod
				httpMethodBase.setRequestHeader("Cookie", cookieString);
			}
			
			if (IPAddress!=null) {
				hostConfig.setLocalAddress(IPAddress);
			}

			// Setup for 3 retry  
			httpMethodBase.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
					new DefaultHttpMethodRetryHandler(3, false));

			// now let's retrieve the data
			client.executeMethod(hostConfig, httpMethodBase);
			
			// Read the response body.
			UrlResponseObject response = new UrlResponseObject();
			response.setResponseBody(httpMethodBase.getResponseBody());
			Header contentType = httpMethodBase.getResponseHeader("Content-Type");
			if (contentType!=null)
				response.setResponseType(contentType.getValue());
			else
				response.setResponseType("html");
			return response;

		} catch (Exception ex) {
			_log.error("Failed to request from URL: ["+queryURL+"]", ex);
			return null;
		} finally {
			try {
				httpMethodBase.releaseConnection();
			} catch (Exception ignored) {
			}
		}
	}
    
}
