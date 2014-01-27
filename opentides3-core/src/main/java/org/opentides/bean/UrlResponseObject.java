package org.opentides.bean;

/**
 * Container of request by requesting for results of 
 * http. Used by UrlUtil.getPage(...)
 * 
 * @author allantan
 *
 */
public class UrlResponseObject {
	
	private String responseType;
	
	private byte[] responseBody;

	/**
	 * @return the responseType
	 */
	public String getResponseType() {
		return responseType;
	}

	/**
	 * @param responseType the responseType to set
	 */
	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}

	/**
	 * @return the responseBody
	 */
	public byte[] getResponseBody() {
		return responseBody;
	}

	/**
	 * @param responseBody the responseBody to set
	 */
	public void setResponseBody(byte[] responseBody) {
		this.responseBody = responseBody;
	}
	
}
