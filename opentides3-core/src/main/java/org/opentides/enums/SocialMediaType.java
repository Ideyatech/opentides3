package org.opentides.enums;

/**
 * Enum listing the different socia media networks
 * that the framework can support.
 */
public enum SocialMediaType {
	
	FACEBOOK("Facebook"),
	GOOGLE("Google"),
	TWITTER("Twitter");
	
	String stringValue;
	
	private SocialMediaType(String value) {
		this.stringValue = value;
	}

	@Override
	public String toString() {
		return stringValue;
	}
	
}