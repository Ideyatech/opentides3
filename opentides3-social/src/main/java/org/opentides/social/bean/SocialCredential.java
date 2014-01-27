package org.opentides.social.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.opentides.bean.BaseEntity;
import org.opentides.social.enums.SocialMediaType;

/**
 * 
 * This holds the social credential details.
 * 
 * @author rabanes
 */
@Entity
@Table(name = "SOCIAL_CREDENTIAL")
public class SocialCredential extends BaseEntity {

	private static final long serialVersionUID = -2303937849404838825L;

	@Column(name="SOCIAL_ID")
	private String socialId;
	
	@Enumerated(EnumType.STRING)
	@Column(name="SOCIAL_TYPE")
	private SocialMediaType socialType;
	
	@Column(name = "SOCIAL_EMAIL")
	private String emailAddress;
	
	@ManyToOne
	@JoinColumn(name = "SOCIAL_BASE_USER")
	private SocialBaseUser socialBaseUser;

	/**
	 * @return the socialId
	 */
	public String getSocialId() {
		return socialId;
	}

	/**
	 * @param socialId the socialId to set
	 */
	public void setSocialId(String socialId) {
		this.socialId = socialId;
	}

	/**
	 * @return the socialType
	 */
	public SocialMediaType getSocialType() {
		return socialType;
	}

	/**
	 * @param socialType the socialType to set
	 */
	public void setSocialType(SocialMediaType socialType) {
		this.socialType = socialType;
	}

	/**
	 * @return the emailAddress
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * @param emailAddress the emailAddress to set
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	/**
	 * @return the socialBaseUser
	 */
	public SocialBaseUser getSocialBaseUser() {
		return socialBaseUser;
	}

	/**
	 * @param socialBaseUser the socialBaseUser to set
	 */
	public void setSocialBaseUser(SocialBaseUser socialBaseUser) {
		this.socialBaseUser = socialBaseUser;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((emailAddress == null) ? 0 : emailAddress.hashCode());
		result = prime * result
				+ ((socialBaseUser == null) ? 0 : socialBaseUser.hashCode());
		result = prime * result
				+ ((socialId == null) ? 0 : socialId.hashCode());
		result = prime * result
				+ ((socialType == null) ? 0 : socialType.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		SocialCredential other = (SocialCredential) obj;
		if (emailAddress == null) {
			if (other.emailAddress != null)
				return false;
		} else if (!emailAddress.equals(other.emailAddress))
			return false;
		if (socialBaseUser == null) {
			if (other.socialBaseUser != null)
				return false;
		} else if (!socialBaseUser.equals(other.socialBaseUser))
			return false;
		if (socialId == null) {
			if (other.socialId != null)
				return false;
		} else if (!socialId.equals(other.socialId))
			return false;
		if (socialType != other.socialType)
			return false;
		return true;
	}

}