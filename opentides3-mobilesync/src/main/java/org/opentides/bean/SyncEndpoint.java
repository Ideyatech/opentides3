/**
 * 
 */
package org.opentides.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.opentides.annotation.GenerateDao;
import org.opentides.annotation.GenerateService;

/**
 * @author allantan
 *
 */

@Entity
@GenerateService
@GenerateDao
@Table(name = "SYNC_ENDPOINT")
public class SyncEndpoint extends BaseEntity {

	private static final long serialVersionUID = 2382958514106458435L;

	@Column(name="CLIENT_CODE")
	private String clientCode;
	
	@Column(name="SYNC_VERSION")
	private Long syncVersion;

	/**
	 * @return the clientCode
	 */
	public final String getClientCode() {
		return clientCode;
	}

	/**
	 * @param clientCode the clientCode to set
	 */
	public final void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}

	/**
	 * @return the syncVersion
	 */
	public final Long getSyncVersion() {
		return syncVersion;
	}

	/**
	 * @param syncVersion the syncVersion to set
	 */
	public final void setSyncVersion(Long syncVersion) {
		this.syncVersion = syncVersion;
	}
	
}
