package com.ideyatech.flexiblesearch.bean;

import com.ideyatech.flexiblesearch.util.ShrinkableLazyList;
import com.ideyatech.flexiblesearch.util.SortOrder;
import org.apache.commons.collections.FactoryUtils;
import org.opentides.bean.BaseEntity;
import org.opentides.bean.SystemCodes;
import org.opentides.bean.user.BaseUser;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: neilnamoro
 * Date: 9/10/13
 * Time: 3:23 PM
 *
 * Main flexible search class that contains the query properties created
 * for a specific Entity
 */
@Entity
@Table(name="FLEXIBLE_SEARCH_BEAN")
public class SearchBean extends BaseEntity implements Serializable{
    private static final long serialVersionUID = 1840829780164526217L;

    @Column(name="ENTITY_TYPE")
    private String entityType;

    @Column(name="JOIN_CLAUSE")
    private String joinClause;

    @Column(name = "QUERY_NAME")
    private String queryName;

    @SuppressWarnings("unchecked")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "flexibleSearchBean", cascade = {
            CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE })
    private List<SearchCriteria> criteriaList = ShrinkableLazyList
            .decorate(new ArrayList<SearchCriteria>(), FactoryUtils
                    .instantiateFactory(SearchCriteria.class));

    @Column(name="GROUP_BY")
    private String groupBy;

    @Column(name="SORT_BY")
    private String sortBy;

    @Enumerated(EnumType.STRING)
    @Column(name="SORT_ORDER")
    private SortOrder sortOrder;

    @ManyToOne
    @JoinColumn(name="SEARCH_OWNER")
    private BaseUser searchOwner;

    @ElementCollection(targetClass=String.class)
    @CollectionTable(name = "FLEXIBLE_SEARCH_SELECTED_COLUMNS", joinColumns = @JoinColumn(name = "SEARCH_BEAN_ID"))
    @Column(name = "SELECTED_COLUMN", nullable = false)
    private List<String> selectedColumns = new ArrayList<String>();

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getJoinClause() {
        return joinClause;
    }

    public void setJoinClause(String joinClause) {
        this.joinClause = joinClause;
    }

    public String getQueryName() {
        return queryName;
    }

    public void setQueryName(String queryName) {
        this.queryName = queryName;
    }

    public List<SearchCriteria> getCriteriaList() {
        return criteriaList;
    }

    public void setCriteriaList(List<SearchCriteria> criteriaList) {
        this.criteriaList = criteriaList;
    }

    public String getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public SortOrder getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(SortOrder sortOrder) {
        this.sortOrder = sortOrder;
    }

    public BaseUser getSearchOwner() {
        return searchOwner;
    }

    public void setSearchOwner(BaseUser searchOwner) {
        this.searchOwner = searchOwner;
    }

    public List<String> getSelectedColumns() {
        return selectedColumns;
    }

    public void setSelectedColumns(List<String> selectedColumns) {
        this.selectedColumns = selectedColumns;
    }
}
