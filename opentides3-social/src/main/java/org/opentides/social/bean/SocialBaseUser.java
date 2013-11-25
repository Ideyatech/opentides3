package org.opentides.social.bean;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.opentides.bean.user.BaseUser;
import org.opentides.social.enums.SocialMediaType;

/**
 * Entity that holds the connection to all Social Credentials (e.g. Facebook, Google, and Twitter)
 * 
 * @author rabanes
 */
@Entity
@Table(name = "SOCIAL_BASE_USER")
public class SocialBaseUser extends BaseUser {

	private static final long serialVersionUID = 1442054970542597426L;

	@OneToMany(mappedBy = "socialBaseUser", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<SocialCredential> socialCredentials;

	/**
	 * @return the socialCredentials
	 */
	public List<SocialCredential> getSocialCredentials() {
		return socialCredentials;
	}

	/**
	 * @param socialCredentials the socialCredentials to set
	 */
	public void setSocialCredentials(List<SocialCredential> socialCredentials) {
		this.socialCredentials = socialCredentials;
	}
	
	/**
	 * Get a specific Social Credential by type.
	 * @param type 
	 */
	public SocialCredential getCredentialByType(SocialMediaType type) {
		for(SocialCredential credential : this.socialCredentials) {
			if(type.equals(credential.getSocialType())) 
				return credential;
		}
		return null;
	}
	
}
