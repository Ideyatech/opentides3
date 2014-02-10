package com.ideyatech.flexiblesearch.dao.impl;

import com.ideyatech.flexiblesearch.bean.SearchBean;
import com.ideyatech.flexiblesearch.bean.SearchCriteria;
import com.ideyatech.flexiblesearch.bean.SearchProperty;
import com.ideyatech.flexiblesearch.bean.SearchRule;
import com.ideyatech.flexiblesearch.dao.FlexibleSearchDao;
import org.opentides.bean.SystemCodes;
import org.opentides.util.StringUtil;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: neilnamoro
 * Date: 9/11/13
 * Time: 3:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class FlexibleSearchDaoImpl implements FlexibleSearchDao {

    // the entity manager
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List findByFlexibleSearch(SearchBean flexibleSearchBean, int start, int total) {
        Class<?> entityType = flexibleSearchBean.getEntityType();

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(entityType);

        Root rootEntity = criteriaQuery.from(entityType);

        criteriaQuery.select(rootEntity);

        prepareJoinTables(flexibleSearchBean, rootEntity);
        
        buildCriteria(flexibleSearchBean, start, total, criteriaBuilder);
        
        prepareGroupByOption(flexibleSearchBean, criteria);
        
        prepareSortByOption(flexibleSearchBean, criteriaBuilder, criteriaQuery, rootEntity);

        TypedQuery q = entityManager.createQuery(c);
        return q.getResultList();
    }

    /**
     *
     * @param flexibleSearchBean
     * @param root
     */
    private void prepareJoinTables(SearchBean flexibleSearchBean,
                                   Root root) {
        Set<String> joinTableNames = new HashSet<String>();
        for (SearchCriteria searchCriteria : flexibleSearchBean
                .getCriteriaList()) {
            for (SearchRule searchRule : searchCriteria.getRules()) {
                SearchProperty searchProperty = searchRule
                        .getFlexibleSearchProperty();
                String joinTableName = searchProperty.getJoinTable();
                if (!StringUtil.isEmpty(joinTableName)) {
                    joinTableNames.add(joinTableName);
                }
            }
        }
        for (String joinTableName : joinTableNames) {
            root.join(joinTableName, JoinType.INNER);
        }
    }

    /**
     *
     * @param flexibleSearchBean
     * @param start
     * @param total
     * @param criteria
     */
    private void buildCriteria(SearchBean flexibleSearchBean,
                               int start, int total, CriteriaBuilder criteriaBuilder) {

        List<SearchCriteria> searchCriterias = flexibleSearchBean
                .getCriteriaList();

        SearchCriteria firstSearchCriteria = searchCriterias.get(0);

        if (searchCriterias.size() == 1) {
            Predicate predicate =
                    prepareJunction(firstSearchCriteria.getMatchType(), criteriaBuilder);

            prepareSearchRules(junction, firstSearchCriteria);
            criteria.add(junction);
        } else {

            Criterion currentCriterion = null;

            for (int i = searchCriterias.size() - 1; i >= 0; i--) {
                Criterion criterion = null;

                SearchCriteria searchCriteria = searchCriterias.get(i);
                Junction junction = prepareJunction(searchCriteria
                        .getMatchType());
                prepareSearchRules(junction, searchCriteria);

                if (currentCriterion != null) {

                    String currentLogicalOperator = searchCriterias.get(i + 1)
                            .getLogicalOperator().getKey();

                    if (currentLogicalOperator.equals("LOGICAL_OPERATOR_AND")) {
                        criterion = Restrictions
                                .and(junction, currentCriterion);
                    } else {
                        criterion = Restrictions.or(junction, currentCriterion);
                    }
                    currentCriterion = criterion;
                } else {
                    currentCriterion = junction;
                }
            }
            criteria.add(currentCriterion);
        }


        if (start > -1) {
            criteria.setFirstResult(start);
        }

        if (total > -1) {
            criteria.setMaxResults(total);
        }
    }

    /**
     *
     * @param matchType
     * @return
     */
    private Predicate prepareJunction(SystemCodes matchType, CriteriaBuilder criteriaBuilder) {
        Predicate predicate = null;
        if (matchType.getKey().equals("MATCH_TYPE_ALL")) {
            predicate = criteriaBuilder.conjunction();
        } else {
            predicate = criteriaBuilder.disjunction();
        }
        return predicate;
    }

    /**
     *
     * @param junction
     * @param searchCriteria
     */
    private void prepareSearchRules(Junction junction,
                                    SearchCriteria searchCriteria) {
        for (SearchRule searchRule : searchCriteria.getRules()) {
            SearchProperty searchProperty = searchRule
                    .getFlexibleSearchProperty();
            String attribute = searchProperty.getAttributeName();
            String propertyClassType = searchProperty.getPropertyClassType();
            String conditionalStatement = "";
            if (searchRule.getConditionalStatement() != null) {
                conditionalStatement = searchRule.getConditionalStatement()
                        .getKey();
            }
            if (conditionalStatement.equals("CONDITION_STRING_IS")) {
                junction.add(Restrictions.eq(attribute,
                        searchRule.getStringPropertyValue()));
            } else if (conditionalStatement.equals("CONDITION_STRING_LIKE")) {
                junction.add(Restrictions.like(attribute,
                        "%" + searchRule.getStringPropertyValue() + "%"));
            } else if (conditionalStatement.equals("CONDITION_STRING_NOT")) {
                junction.add(Restrictions.not(Restrictions.eq(attribute,
                        searchRule.getStringPropertyValue())));
            } else if (conditionalStatement.equals("CONDITION_DATE_BETWEEN")) {
                junction.add(Restrictions.between(attribute,
                        searchRule.getStartDate(), searchRule.getEndDate()));
            } else if (conditionalStatement.equals("CONDITION_DATE_BEFORE")) {
                junction.add(Restrictions.lt(attribute,
                        searchRule.getStartDate()));
            } else if (conditionalStatement.equals("CONDITION_DATE_AFTER")) {
                junction.add(Restrictions.gt(attribute,
                        searchRule.getStartDate()));
            } else if (conditionalStatement.equals("CONDITION_DATE_EXACTLY")) {
                junction.add(Restrictions.eq(attribute,
                        searchRule.getStartDate()));
            } else if (conditionalStatement.equals("CONDITION_BOOLEAN_IS")) {
                junction.add(Restrictions.eq(attribute,
                        searchRule.getBooleanPropertyValue()));
            } else if (conditionalStatement.equals("CONDITION_DEFAULT_IS")) {
                if (propertyClassType.equals("CmisUser")) {
                    if (searchRule.getJusticePropertyValue() != null) {
                        junction.add(Restrictions.eq(attribute,
                                searchRule.getJusticePropertyValue()));
                    } else {
                        junction.add(Restrictions.isNull(attribute));
                    }
                } else if (propertyClassType.equals("Division")) {
                    if (searchRule.getDivisionPropertyValue() != null) {
                        junction.add(Restrictions.eq(attribute,
                                searchRule.getDivisionPropertyValue()));
                    } else {
                        junction.add(Restrictions.isNull(attribute));
                    }
                } else if (propertyClassType.equals("SystemCodes")) {
                    if (searchRule.getSystemCodesPropertyValue() != null) {
                        junction.add(Restrictions.eq(attribute,
                                searchRule.getSystemCodesPropertyValue()));
                    } else {
                        junction.add(Restrictions.isNull(attribute));
                    }
                }
            } else if (conditionalStatement.equals("CONDITION_DEFAULT_NOT")) {
                if (propertyClassType.equals("CmisUser")) {
                    if (searchRule.getJusticePropertyValue() != null) {
                        junction.add(Restrictions.ne(attribute,
                                searchRule.getJusticePropertyValue()));
                    } else {
                        junction.add(Restrictions.isNotNull(attribute));
                    }
                } else if (propertyClassType.equals("Division")) {
                    if (searchRule.getDivisionPropertyValue() != null) {
                        junction.add(Restrictions.ne(attribute,
                                searchRule.getDivisionPropertyValue()));
                    } else {
                        junction.add(Restrictions.isNotNull(attribute));
                    }
                } else if (propertyClassType.equals("SystemCodes")) {
                    if (searchRule.getSystemCodesPropertyValue() != null) {
                        junction.add(Restrictions.ne(attribute,
                                searchRule.getSystemCodesPropertyValue()));
                    } else {
                        junction.add(Restrictions.isNotNull(attribute));
                    }
                }
            }
        }
    }


    private void prepareSortByOption(SearchBean flexibleSearchBean,
                                     CriteriaBuilder criteriaBuilder,
                                     CriteriaQuery criteriaQuery,
                                     Root rootEntity) {
        String sortBy = flexibleSearchBean.getSortBy();
        if (!StringUtil.isEmpty(sortBy)) {
            SystemCodes sortDirection = flexibleSearchBean.getSortDirection();

            if (sortDirection == null
                    || sortDirection.getKey()
                    .equals("SORT_DIRECTION_ASCENDING")) {
                criteriaQuery.orderBy(criteriaBuilder.asc(rootEntity.get(sortBy)));
            } else {
                criteriaQuery.orderBy(criteriaBuilder.desc(rootEntity.get(sortBy)));
            }
        }
    }

    @Override
    public long countByFlexibleSearch(SearchBean flexibleSearchBean) {
        
		 Criteria criteria =
		 getHibernateSession().createCriteria(getEntityBeanType());
		  
		 criteria.setProjection(Projections.rowCount());
		  
		  prepareJoinTables(flexibleSearchBean, criteria);
		  
		  buildCriteria(flexibleSearchBean, -1, -1, criteria);
		  
        return ((Integer)criteria.uniqueResult()).longValue();
	    
    }
}
