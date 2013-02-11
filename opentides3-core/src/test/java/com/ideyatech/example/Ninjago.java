package com.ideyatech.example;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.opentides.annotation.Secure;
import org.opentides.annotation.field.CheckBox;
import org.opentides.annotation.field.TextArea;
import org.opentides.annotation.field.TextField;
import org.opentides.annotation.field.Validation;
import org.opentides.bean.BaseEntity;
import org.opentides.bean.SystemCodes;

/**
 * This is the master class for testing all annotations
 * and code generation supported by opentides3.
 */
@Entity
@Table(name="NINJA_GO")
@Secure
public class Ninjago extends BaseEntity{
	
	private static final long serialVersionUID = -4142599915292096152L;
	
	// Label: specified
	// Validation: required
	// Use: crud, search criteria
	@Validation({"required", "maxLength=128"})
	@TextField(label="Nama-e", isSearchCriteria=true)
	@Column(name = "FIRST_NAME", nullable=false)
	private String firstName;

	// TextField
	// Label: default
	// Validation: required
	// Use: crud, search criteria
	@Validation({"required"})
	@TextField(isSearchCriteria=true)
	@Column(name = "LAST_NAME", nullable=false)
	private String lastName;

	// TextField
	// Label: specified
	// Validation: email, required
	// Use: crud, search criteria, search results
	@Validation({"required", "email"})
	@TextField(label="Email Address", isSearchCriteria=true, isSearchResult=true)
	@Column(name="EMAIL", nullable=false)	
	private String email;

	// TextArea
	// Label: default
	// Validation: Must be protected from script/html injection
	// Use: crud
	// Store as blob
	@TextArea
	private String description;
	
	@Validation({"number","min=17","max=65"})
	private Integer age;
	
	// Display only
	// Label: specified
	// Use: read-only (score is system generated)
	@DisplayOnly
	private Long score;
	
	// Date Picker
	// Label: specified
	// Validation: today or past date
	// Use: crud
	@Validation({"required","past"})
	@DatePicker()
	private Date joinDate;
	
	// Label: specified
	// Validation: none
	// Use: crud
	// Secured to authz only
	@CheckBox
	@Secure
	private Boolean active;
	
	// 
	private Clan mainClan;
	
	private Set<Clan> subClans;
	
	// Dropdown
	// Validation: required
	// Default to STATUS_NEW	
	// Secured to authz only
	private SystemCodes status;

	// Hidden
	// Validation: only himself can view/edit
	// Use: secret	
	private String secretCode;
	
	// Dropdown (list of all ninjago)
	// Label: default
	// Validation: must not be self
	// Use: 
	private Ninjago partner;
	
	// Multiselect
	// Label: Specified
	// Validation: none
	private Set<Skills> skillSet;
	
	// Radiobutton
	// Searchable
	private String gender;
	
	// Future date
	private Date nextFight;
	
	// Currency
	private Double sellingPrice;
	
	// image/photo
	private String avatar;
	
	// file upload
	private String attachment;

}