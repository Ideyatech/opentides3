package org.opentides.enums;

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