package com.ideyatech.flexiblesearch.bean;

import com.ideyatech.flexiblesearch.util.LogicalOperator;
import org.opentides.bean.BaseEntity;
import org.opentides.bean.SystemCodes;
import org.opentides.bean.user.BaseUser;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: neilnamoro
 * Date: 9/10/13
 * Time: 3:33 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name="FLEXIBLE_SEARCH_RULE")
public class SearchRule extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -6547586115862540125L;

    public static final int PROPERTY_TYPE_STRING = 0;
    public static final int PROPERTY_TYPE_SYSTEM_CODES = 1;
    public static final int PROPERTY_TYPE_DATE = 2;

    @ManyToOne(targetEntity=SearchCriteria.class)
    @JoinColumn(name="FLEXIBLE_SEARCH_CRITERIA")
    private SearchCriteria flexibleSearchCriteria;

    @ManyToOne
    @JoinColumn(name="FLEXIBLE_SEARCH_PROPERTY")
    private SearchProperty flexibleSearchProperty;

    @Column(name="PROPERTY_CRITERIA")
    private String propertyCriteria;

    @Column(name="CONDITIONAL_STATEMENT")
    private LogicalOperator logicalOperator;

    @Column(name="STRING_PROPERTY_VALUE")
    private String stringPropertyValue;

    @Column(name="LONG_PROPERTY_VALUE")
    private Long longPropertyValue;

    @Column(name="DOUBLE_PROPERTY_VALUE")
    private Double doublePropertyValue;

    @Column(name="INTEGER_PROPERTY_VALUE")
    private Integer integerPropertyValue;

    @Column(name="FLOAT_PROPERTY_VALUE")
    private Float floatPropertyValue;

    @Column(name="BIGDECIMAL_PROPERTY_VALUE")
    private BigDecimal bigDecimalPropertyValue;

    @ManyToOne
    @JoinColumn(name="SYSTEM_CODES_PROPERTY_VALUE")
    private SystemCodes systemCodesPropertyValue;

    @Temporal(TemporalType.DATE)
    @Column(name="START_DATE")
    private Date startDate;

    @Temporal(value = TemporalType.DATE)
    @Column(name="END_DATE")
    private Date endDate;

    @Column(name="BOOLEAN_PROPERTY")
    private Boolean booleanPropertyValue;

    @Column(name="BASE_ENTITY_ID_VALUE")
    private Long baseEntityIdValue;

    @Transient
    private transient Boolean isDeleted = Boolean.valueOf(false);

    @Transient
    private transient int propertyType;

    public int getPropertyStringType(){
        return SearchRule.PROPERTY_TYPE_STRING;
    }

    public int getPropertySystemCodesType(){
        return SearchRule.PROPERTY_TYPE_SYSTEM_CODES;
    }

    public int getPropertyDateType(){
        return SearchRule.PROPERTY_TYPE_DATE;
    }

    public int getPropertyType() {
        return propertyType;
    }

    public SearchCriteria getFlexibleSearchCriteria() {
        return flexibleSearchCriteria;
    }

    public void setFlexibleSearchCriteria(SearchCriteria flexibleSearchCriteria) {
        this.flexibleSearchCriteria = flexibleSearchCriteria;
    }

    public SearchProperty getFlexibleSearchProperty() {
        return flexibleSearchProperty;
    }

    public void setFlexibleSearchProperty(SearchProperty flexibleSearchProperty) {
        this.flexibleSearchProperty = flexibleSearchProperty;
    }

    public String getPropertyCriteria() {
        return propertyCriteria;
    }

    public void setPropertyCriteria(String propertyCriteria) {
        this.propertyCriteria = propertyCriteria;
    }

    public String getStringPropertyValue() {
        return stringPropertyValue;
    }

    public void setStringPropertyValue(String stringPropertyValue) {
        this.stringPropertyValue = stringPropertyValue;
    }

    public SystemCodes getSystemCodesPropertyValue() {
        return systemCodesPropertyValue;
    }

    public void setSystemCodesPropertyValue(SystemCodes systemCodesPropertyValue) {
        this.systemCodesPropertyValue = systemCodesPropertyValue;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Boolean getBooleanPropertyValue() {
        return booleanPropertyValue;
    }

    public void setBooleanPropertyValue(Boolean booleanPropertyValue) {
        this.booleanPropertyValue = booleanPropertyValue;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public LogicalOperator getLogicalOperator() {
        return logicalOperator;
    }

    public void setLogicalOperator(LogicalOperator logicalOperator) {
        this.logicalOperator = logicalOperator;
    }

    public Long getLongPropertyValue() {
        return longPropertyValue;
    }

    public void setLongPropertyValue(Long longPropertyValue) {
        this.longPropertyValue = longPropertyValue;
    }

    public Double getDoublePropertyValue() {
        return doublePropertyValue;
    }

    public void setDoublePropertyValue(Double doublePropertyValue) {
        this.doublePropertyValue = doublePropertyValue;
    }

    public Integer getIntegerPropertyValue() {
        return integerPropertyValue;
    }

    public void setIntegerPropertyValue(Integer integerPropertyValue) {
        this.integerPropertyValue = integerPropertyValue;
    }

    public Float getFloatPropertyValue() {
        return floatPropertyValue;
    }

    public void setFloatPropertyValue(Float floatPropertyValue) {
        this.floatPropertyValue = floatPropertyValue;
    }

    public BigDecimal getBigDecimalPropertyValue() {
        return bigDecimalPropertyValue;
    }

    public void setBigDecimalPropertyValue(BigDecimal bigDecimalPropertyValue) {
        this.bigDecimalPropertyValue = bigDecimalPropertyValue;
    }

    public Long getBaseEntityIdValue() {
        return baseEntityIdValue;
    }

    public void setBaseEntityIdValue(Long baseEntityIdValue) {
        this.baseEntityIdValue = baseEntityIdValue;
    }
}
