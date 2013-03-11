package org.opentides.bean;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 
 * @author AJ
 *
 */
@Entity
@Table(name = "PHOTO_INFO")
public class PhotoInfo extends FileInfo implements Serializable {

	private static final long serialVersionUID = 4727824117459616776L;
	
}
