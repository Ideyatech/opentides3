package com.ideyatech.bean;

import java.util.ArrayList;
import java.util.List;

import org.opentides.annotation.Auditable;
import org.opentides.annotation.AuditableFields;
import org.opentides.annotation.SearchableFields;
import org.opentides.bean.AuditableField;
import org.opentides.bean.SystemCodes;
import org.opentides.bean.user.BaseUser;


/**
 * This is a test class used to check if CrudUtil.retrieveObjectValue
 * and retrieveObjectType are working
 * @author allantan
 *
 */
@Auditable
public class UserCriteria extends BaseUser {

	private static final long serialVersionUID = 7735151314199318745L;
	
	private List<SystemCodes> favorites;
	private List<String> alias;
	
	private SystemCodes status;
	
	private BaseUser supervisor;
	
	/* (non-Javadoc)
	 * @see com.ideyatech.core.bean.user.BaseUser#getSearchProperties()
	 */
	@SearchableFields
	public List<String> getSearchableFields() {
		List<String> props = new ArrayList<String>();
		props.add("firstName");
		props.add("lastName");
		props.add("emailAddress");
		props.add("status");
		props.add("supervisor");
		props.add("favorites.value");
		props.add("credential.username");
		props.add("credential.enabled");
		props.add("credential.id");
		return props;
	}
	
	/* (non-Javadoc)
	 * @see org.opentides.bean.BaseEntity#getAuditableFields()
	 */
	@AuditableFields
	public List<AuditableField> getAuditableFields() {
		List<AuditableField> fields = new ArrayList<AuditableField>();
		fields.add(new AuditableField("favorites.value","Favorites"));
		return fields;
	}

	/**
	 * Getter method for favorites.
	 *
	 * @return the favorites
	 */
	public final List<SystemCodes> getFavorites() {
		return favorites;
	}
	/**
	 * Setter method for favorites.
	 *
	 * @param favorites the favorites to set
	 */
	public final void setFavorites(List<SystemCodes> favorites) {
		this.favorites = favorites;
	}
	/**
	 * Getter method for alias.
	 *
	 * @return the alias
	 */
	public final List<String> getAlias() {
		return alias;
	}
	/**
	 * Setter method for alias.
	 *
	 * @param alias the alias to set
	 */
	public final void setAlias(List<String> alias) {
		this.alias = alias;
	}
	/**
	 * @return the status
	 */
	public final SystemCodes getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public final void setStatus(SystemCodes status) {
		this.status = status;
	}

	/**
	 * @return the supervisor
	 */
	public final BaseUser getSupervisor() {
		return supervisor;
	}

	/**
	 * @param supervisor the supervisor to set
	 */
	public final void setSupervisor(BaseUser supervisor) {
		this.supervisor = supervisor;
	}
	
}
