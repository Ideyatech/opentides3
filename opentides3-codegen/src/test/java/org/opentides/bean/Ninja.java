package org.opentides.bean;

import java.util.Date;

import org.opentides.annotation.GenerateCrudController;
import org.opentides.annotation.GenerateDao;
import org.opentides.annotation.GenerateService;
import org.opentides.annotation.GenerateValidator;
import org.opentides.annotation.field.Checkbox;
import org.opentides.annotation.field.DatePicker;
import org.opentides.annotation.field.Dropdown;
import org.opentides.annotation.field.TextArea;
import org.opentides.annotation.field.TextField;
import org.opentides.annotation.field.Validation;
//import org.opentides.annotation.Secure;

/**
 * This is the master class for testing all annotations
 * and code generation supported by opentides3.
 */ 
@GenerateCrudController
@GenerateValidator
@GenerateDao
@GenerateService
public class Ninja {
	
	
	// Label: specified
	// Validation: required
	// Use: crud, search criteria
	@Validation(isRequired=true, maxLength=128)
	@TextField(label="Name", isSearchCriteria=true)
	private String firstName;

	// TextField
	// Label: default
	// Validation: required
	// Use: crud, search criteria
	@Validation(isRequired=true)
	@TextField(isSearchCriteria=true)
	private String lastName;

	// TextField
	// Label: specified
	// Validation: email, required
	// Use: crud, search criteria, search results
	@Validation(isRequired=true, isEmailFormat=true)
	@TextField(label="Email Address", isSearchCriteria=true, isSearchResult=true)
	private String email;

	// TextArea
	// Label: default
	// Validation: Must be protected from script/html injection
	// Use: crud
	// Store as blob
	@TextArea
	private String description;
	
	@Validation(isNumberFormat=true, maxAllowValue=65, minAllowValue=18)
	private Integer age;
	
	// Display only
	// Label: specified
	// Use: read-only (score is system generated)
	private Long score;
	
	// Date Picker
	// Label: specified
	// Validation: today or past date
	// Use: crud
	@Validation(isRequired=false, rejectFutureDate=true)
	@DatePicker
	private Date joinDate;
	
	// Label: specified
	// Validation: none
	// Use: crud
	// Secured to authz only
	@Checkbox
	//@Secure
	private Boolean active;
	
	// Dropdown
	// Validation: required
	// Default to STATUS_NEW	
	// Secured to authz only
	@Dropdown(category="STATUS")
	private SystemCodes status;
	
	// Hidden
	// Validation: only himself can view/edit
	// Use: secret
	private String secretCode;
	
	// Dropdown (list of all ninjago)
	// Label: default
	// Validation: must not be self
	// Use: 
	private Ninja partner;
	
	// Multiselect
	// Label: Specified
	// Validation: none
//	@JsonView(Views.FormView.class)
//	@ManyToMany(fetch=FetchType.LAZY, cascade={CascadeType.MERGE, CascadeType.REFRESH})
//	@JoinTable(name="NINJA_SKILLS",
//	joinColumns = { 
//			@JoinColumn(name="NINJA_ID", referencedColumnName="ID") 
//	},
//	inverseJoinColumns = {
//			@JoinColumn(name="SKILLS_ID")
//	})
//	private Set<SystemCodes> skillSet;
//	
	// Radiobutton
	// Searchable
	@Dropdown(options={"Male", "Female"})
	@Validation(isRequired=true)
	private String gender;
	
	// Future date
	private Date nextFight;
	
	// Currency
	private Double sellingPrice;
	
	// image/photo
	private String avatar;
	
	// file upload
	private String attachment;

	/**
	 * @return the firstName
	 */
	public final String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public final void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public final String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public final void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the email
	 */
	public final String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public final void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the description
	 */
	public final String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public final void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the age
	 */
	public final Integer getAge() {
		return age;
	}

	/**
	 * @param age the age to set
	 */
	public final void setAge(Integer age) {
		this.age = age;
	}

	/**
	 * @return the score
	 */
	public final Long getScore() {
		return score;
	}

	/**
	 * @param score the score to set
	 */
	public final void setScore(Long score) {
		this.score = score;
	}

	/**
	 * @return the joinDate
	 */
	public final Date getJoinDate() {
		return joinDate;
	}

	/**
	 * @param joinDate the joinDate to set
	 */
	public final void setJoinDate(Date joinDate) {
		this.joinDate = joinDate;
	}

	/**
	 * @return the active
	 */
	public final Boolean getActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public final void setActive(Boolean active) {
		this.active = active;
	}

//	/**
//	 * @return the status
//	 */
//	public final SystemCodes getStatus() {
//		return status;
//	}
//
//	/**
//	 * @param status the status to set
//	 */
//	public final void setStatus(SystemCodes status) {
//		this.status = status;
//	}

	/**
	 * @return the secretCode
	 */
	public final String getSecretCode() {
		return secretCode;
	}

	/**
	 * @param secretCode the secretCode to set
	 */
	public final void setSecretCode(String secretCode) {
		this.secretCode = secretCode;
	}

	/**
	 * @return the partner
	 */
	public final Ninja getPartner() {
		return partner;
	}

	/**
	 * @param partner the partner to set
	 */
	public final void setPartner(Ninja partner) {
		this.partner = partner;
	}

	/**
	 * @return the skillSet
	 */
//	public final Set<SystemCodes> getSkillSet() {
//		return skillSet;
//	}
//
//	/**
//	 * @param skillSet the skillSet to set
//	 */
//	public final void setSkillSet(Set<SystemCodes> skillSet) {
//		this.skillSet = skillSet;
//	}

	/**
	 * @return the gender
	 */
	public final String getGender() {
		return gender;
	}

	/**
	 * @param gender the gender to set
	 */
	public final void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * @return the nextFight
	 */
	public final Date getNextFight() {
		return nextFight;
	}

	/**
	 * @param nextFight the nextFight to set
	 */
	public final void setNextFight(Date nextFight) {
		this.nextFight = nextFight;
	}

	/**
	 * @return the sellingPrice
	 */
	public final Double getSellingPrice() {
		return sellingPrice;
	}

	/**
	 * @param sellingPrice the sellingPrice to set
	 */
	public final void setSellingPrice(Double sellingPrice) {
		this.sellingPrice = sellingPrice;
	}

	/**
	 * @return the avatar
	 */
	public final String getAvatar() {
		return avatar;
	}

	/**
	 * @param avatar the avatar to set
	 */
	public final void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	/**
	 * @return the attachment
	 */
	public final String getAttachment() {
		return attachment;
	}

	/**
	 * @param attachment the attachment to set
	 */
	public final void setAttachment(String attachment) {
		this.attachment = attachment;
	}
	
//	@JsonView(Views.SearchView.class)
//	@PrimaryField(label="Name")
//	public final String getCompleteName() {
//		String name = "";
//		if (!StringUtil.isEmpty(getFirstName())) {
//			name += getFirstName() + " ";
//		}
//		if (!StringUtil.isEmpty(getLastName())) {
//			name += getLastName() + " ";
//		}
//		return name.trim();
//	}
//	
//	// ImageUploadable requirements
//	
//	@OneToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
//	@JoinTable(name = "NINJA_PHOTO", 
//			joinColumns = { @JoinColumn(name = "NINJA_ID", referencedColumnName = "ID") }, 
//			inverseJoinColumns = @JoinColumn(name = "PHOTO_ID")
//	)
//	private List<ImageInfo> images;
//	private transient MultipartFile image;
//	
//	@Override
//	public List<ImageInfo> getImages() {
//		return images;
//	}
//	
//	public void setImages(List<ImageInfo> images) {
//		this.images = images;
//	}
//	
//	@Override
//	public MultipartFile getImage() {
//		return image;
//	}
//	
//	@Override
//	public ImageInfo getPrimaryImage() {
//		return null;
//	}
//	
//	public void setImage(MultipartFile image) {
//		this.image = image;
//	}
//	
//	public void addImage(ImageInfo imageInfo){
//		synchronized (imageInfo) {
//			if (images == null){
//				images = new ArrayList<ImageInfo>();
//			}
//			images.add(imageInfo);
//		}
//	}
//	
//	// End of ImageUploadable requirements
//	
//	// Commentable requirements
//	
//	@OneToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
//	@JoinTable(name = "NINJA_COMMENT", 
//			joinColumns = { @JoinColumn(name = "NINJA_ID", referencedColumnName = "ID") }, 
//			inverseJoinColumns = @JoinColumn(name = "COMMENT_ID")
//	)
//	private List<Comment> comments;
//
//	@Override
//	public List<Comment> getComments() {
//		return comments;
//	}
//	
//	@Override
//	public void setComments(List<Comment> comments) {
//		this.comments = comments;
//	}
	
 	// End of Commentable requirements 
}