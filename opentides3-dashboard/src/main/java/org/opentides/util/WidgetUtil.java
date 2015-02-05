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
import org.opentides.util.UrlUtil;
import org.opentides.util.StringUtil;

/**
 * @author gino
 *
 */
public class WidgetUtil{
	
	private static Logger _log = Logger.getLogger(WidgetUtil.class);
	private static final int CONNECTION_TIMEOUT = 15000;
	
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
					IPAddress = UrlUtil.convertIPString(IPString);
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
