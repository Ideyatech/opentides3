package com.ideyatech.flexiblesearch.bean;

import org.opentides.bean.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: neilnamoro
 * Date: 9/10/13
 * Time: 3:33 PM
 *
 * Bean to hold the values related to a entity property that will be used
 * in building the flexible search query
 */
@Entity
@Table(name="FLEXIBLE_SEARCH_PROPERTY")
public class SearchProperty extends BaseEntity implements Serializable{
    private static final long serialVersionUID = -6809551077030181492L;

    @Column(name="CLASS_NAME")
    private String className;

    @Column(name="ATTRIBUTE_NAME")
    private String attributeName;

    @Column(name="ATTRIBUTE_LABEL")
    private String attributeLabel;

    @Column(name="PROPERTY_CLASS_TYPE")
    private String propertyClassType;

    @Column(name="NEEDS_JOIN_QUERY")
    private boolean needsJoinQuery;

    @Column(name="JOIN_TABLE")
    private String joinTable;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeLabel() {
        return attributeLabel;
    }

    public void setAttributeLabel(String attributeLabel) {
        this.attributeLabel = attributeLabel;
    }

    public String getPropertyClassType() {
        return propertyClassType;
    }

    public void setPropertyClassType(String propertyClassType) {
        this.propertyClassType = propertyClassType;
    }

    public boolean isNeedsJoinQuery() {
        return needsJoinQuery;
    }

    public void setNeedsJoinQuery(boolean needsJoinQuery) {
        this.needsJoinQuery = needsJoinQuery;
    }

    public String getJoinTable() {
        return joinTable;
    }

    public void setJoinTable(String joinTable) {
        this.joinTable = joinTable;
    }
}
