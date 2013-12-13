package org.opentides.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.opentides.bean.user.BaseUser;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "USER_WIDGETS")
public class UserWidgets extends BaseEntity {
	
	private static final long serialVersionUID = -300267552372161026L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID", nullable = false)
	private BaseUser user;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "WIDGET_ID", nullable = false)
	@JsonBackReference
	private Widget widget;
	
	@Column(name="COLUMN_")
	private Integer column;

	@Column(name="ROW_")
	private Integer row;
	
	@Column(name="STATUS")
	private Integer status;
	
	@Transient
	private transient Boolean checked;

	/**
	 * @return the user
	 */
	public BaseUser getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(BaseUser user) {
		this.user = user;
	}

	/**
	 * @return the widget
	 */
	public Widget getWidget() {
		return widget;
	}

	/**
	 * @param widget the widget to set
	 */
	public void setWidget(Widget widget) {
		this.widget = widget;
	}

	/**
	 * @return the column
	 */
	public Integer getColumn() {
		return column;
	}

	/**
	 * @param column the column to set
	 */
	public void setColumn(Integer column) {
		this.column = column;
	}

	/**
	 * @return the row
	 */
	public Integer getRow() {
		return row;
	}

	/**
	 * @param row the row to set
	 */
	public void setRow(Integer row) {
		this.row = row;
	}

	/**
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * @return the checked
	 */
	public Boolean getChecked() {
		return checked;
	}

	/**
	 * @param checked the checked to set
	 */
	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((column == null) ? 0 : column.hashCode());
		result = prime * result + ((row == null) ? 0 : row.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		result = prime * result + ((widget == null) ? 0 : widget.hashCode());
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
		UserWidgets other = (UserWidgets) obj;
		if (column == null) {
			if (other.column != null)
				return false;
		} else if (!column.equals(other.column))
			return false;
		if (row == null) {
			if (other.row != null)
				return false;
		} else if (!row.equals(other.row))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		if (widget == null) {
			if (other.widget != null)
				return false;
		} else if (!widget.equals(other.widget))
			return false;
		return true;
	}
	
}
