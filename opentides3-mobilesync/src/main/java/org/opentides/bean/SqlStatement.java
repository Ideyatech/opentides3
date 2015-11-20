package org.opentides.bean;


/**
 * Container holder sql statement for execution during sync.
 * 
 * @author allantan
 *
 */
public class SqlStatement {
	
	private Long   syncId;
	private Long   lookupId;
	private String stmt;
	private String value;
	
	public SqlStatement(Long syncId, String stmt, String value) {
		super();
		this.syncId = syncId;
		this.stmt = stmt;
		this.value = value;
	}
	
	public SqlStatement(Long syncId, String stmt, String value, Long lookupId) {
		super();
		this.syncId = syncId;
		this.stmt = stmt;
		this.value = value;
		this.lookupId = lookupId;
	}
	

	/**
	 * @return the syncId
	 */
	public Long getSyncId() {
		return syncId;
	}


	/**
	 * @param syncId the syncId to set
	 */
	public void setSyncId(Long syncId) {
		this.syncId = syncId;
	}


	/**
	 * @return the stmt
	 */
	public String getStmt() {
		return stmt;
	}


	/**
	 * @param stmt the stmt to set
	 */
	public void setStmt(String stmt) {
		this.stmt = stmt;
	}


	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}


	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the lookupId
	 */
	public Long getLookupId() {
		return lookupId;
	}

	/**
	 * @param lookupId the lookupId to set
	 */
	public void setLookupId(Long lookupId) {
		this.lookupId = lookupId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SqlStatement [syncId=" + syncId + ", lookupId=" + lookupId
				+ ", stmt=" + stmt + ", value=" + value + "]";
	}

}
