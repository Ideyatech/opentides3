package com.ideyatech.flexiblesearch.bean;

import com.ideyatech.flexiblesearch.util.LogicalOperator;
import com.ideyatech.flexiblesearch.util.MatchType;
import com.ideyatech.flexiblesearch.util.ShrinkableLazyList;
import org.apache.commons.collections.FactoryUtils;
import org.opentides.bean.BaseEntity;
import org.opentides.bean.SystemCodes;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: neilnamoro
 * Date: 9/10/13
 * Time: 3:33 PM
 *
 * FlexibleSearchCriteria holds the a collection of 'where clauses' that will
 * be used in building the flexible search query
 */
@Entity
@Table(name="FLEXIBLE_SEARCH_CRITERIA")
public class SearchCriteria extends BaseEntity implements Serializable{
    private static final long serialVersionUID = 407922708734543128L;

    @ManyToOne
    @JoinColumn(name = "FLEXIBLE_SEARCH_BEAN")
    private SearchBean flexibleSearchBean;

    @Enumerated(EnumType.STRING)
    @Column(name = "LOGICAL_OPERATOR")
    private LogicalOperator logicalOperator;

    @Enumerated(EnumType.STRING)
    @Column(name = "MATCH_TYPE")
    private MatchType matchType;

    @SuppressWarnings("unchecked")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "flexibleSearchCriteria", cascade = {
            CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE })
    private List<SearchRule> rules = ShrinkableLazyList.decorate(
            new ArrayList<SearchRule>(),
            FactoryUtils.instantiateFactory(SearchRule.class));

    @Transient
    private transient Boolean isDeleted = Boolean.valueOf(false);

    public SearchBean getFlexibleSearchBean() {
        return flexibleSearchBean;
    }

    public void setFlexibleSearchBean(SearchBean flexibleSearchBean) {
        this.flexibleSearchBean = flexibleSearchBean;
    }

    public LogicalOperator getLogicalOperator() {
        return logicalOperator;
    }

    public void setLogicalOperator(LogicalOperator logicalOperator) {
        this.logicalOperator = logicalOperator;
    }

    public MatchType getMatchType() {
        return matchType;
    }

    public void setMatchType(MatchType matchType) {
        this.matchType = matchType;
    }

    public List<SearchRule> getRules() {
        return rules;
    }

    public void setRules(List<SearchRule> rules) {
        this.rules = rules;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}
