package com.ideyatech.flexiblesearch.dao.impl;

import com.ideyatech.flexiblesearch.bean.SearchBean;
import com.ideyatech.flexiblesearch.bean.SearchCriteria;
import com.ideyatech.flexiblesearch.bean.SearchProperty;
import com.ideyatech.flexiblesearch.bean.SearchRule;
import com.ideyatech.flexiblesearch.dao.DynamicSearchDao;
import com.ideyatech.flexiblesearch.util.LogicalOperator;
import com.ideyatech.flexiblesearch.util.MatchType;
import com.ideyatech.flexiblesearch.util.SortOrder;
import org.opentides.bean.BaseEntity;
import org.opentides.bean.SystemCodes;
import org.opentides.util.StringUtil;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: neilnamoro
 *
 */
public class DynamicSearchDaoImpl implements DynamicSearchDao {

    // the entity manager
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List findByFlexibleSearch(SearchBean flexibleSearchBean, int start, int total) {
        Class entityType = null;
        try {
            entityType = Class.forName(flexibleSearchBean.getEntityType());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(entityType);

        Root rootEntity = criteriaQuery.from(entityType);

        criteriaQuery.select(rootEntity);

        prepareJoinTables(flexibleSearchBean, rootEntity);
        
        buildCriteria(flexibleSearchBean, criteriaBuilder, criteriaQuery, rootEntity);
        
        //prepareGroupByOption(flexibleSearchBean, criteria);
        
        prepareSortByOption(flexibleSearchBean, criteriaBuilder, criteriaQuery, rootEntity);

        TypedQuery typedQuery = entityManager.createQuery(criteriaQuery);

        if (start > 0)
            typedQuery.setFirstResult(start);

        if (total > 0)
            typedQuery.setMaxResults(total);

        return typedQuery.getResultList();
    }

    /**
     *
     * @param flexibleSearchBean
     * @param root
     */
    private void prepareJoinTables(SearchBean flexibleSearchBean,
                                   Root root) {
        Set<String> joinTableNames = new HashSet<String>();
        /*for (SearchCriteria searchCriteria : flexibleSearchBean.getCriteriaList()) {
            for (SearchRule searchRule : searchCriteria.getRules()) {
                SearchProperty searchProperty = searchRule
                        .getFlexibleSearchProperty();
                String joinTableName = searchProperty.getJoinTable();
                if (!StringUtil.isEmpty(joinTableName)) {
                    joinTableNames.add(joinTableName);
                }
            }
        }*/
        for (String joinTableName : joinTableNames) {
            root.join(joinTableName, JoinType.INNER);
        }
    }

    /**
     *
     * @param flexibleSearchBean
     * @param criteriaBuilder
     * @param criteriaQuery
     * @param rootEntity
     */
    private void buildCriteria(SearchBean flexibleSearchBean,
                               CriteriaBuilder criteriaBuilder,
                               CriteriaQuery criteriaQuery,
                               Root rootEntity){

        List<SearchCriteria> searchCriterias = flexibleSearchBean
                .getCriteriaList();



        if (searchCriterias.size() == 1) {
            try{
                SearchCriteria firstSearchCriteria = searchCriterias.get(0);

                Predicate predicate = prepareSearchRules(firstSearchCriteria, criteriaBuilder, rootEntity);

                criteriaQuery.where(predicate);
            }catch (ClassNotFoundException e){
                e.printStackTrace();
            }
        } else {

            Predicate currentPredicate = null;

            for (int i = searchCriterias.size() - 1; i >= 0; i--) {
                SearchCriteria searchCriteria = searchCriterias.get(i);

                Predicate predicate;

                try{
                    predicate = prepareSearchRules(searchCriteria, criteriaBuilder, rootEntity);
                }catch (ClassNotFoundException e){
                    continue;
                }

                if (currentPredicate != null) {

                    LogicalOperator operator = searchCriterias.get(i + 1).getLogicalOperator();

                    if (operator.equals(LogicalOperator.JUNCTION_AND)) {
                        predicate = criteriaBuilder.and(predicate, currentPredicate);
                    } else {
                        predicate = criteriaBuilder.or(predicate, currentPredicate);
                    }
                    currentPredicate = predicate;
                } else {
                    currentPredicate = predicate;
                }
            }

            criteriaQuery.where(currentPredicate);
        }
    }

    /**
     *
     * @param searchCriteria
     * @param criteriaBuilder
     * @param rootEntity
     * @return
     * @throws ClassNotFoundException
     */
    private Predicate prepareSearchRules(SearchCriteria searchCriteria,
                                    CriteriaBuilder criteriaBuilder,
                                    Root rootEntity) throws ClassNotFoundException{


        List<Predicate> predicates = new ArrayList<Predicate>();

        for (SearchRule searchRule : searchCriteria.getRules()) {

            SearchProperty searchProperty = searchRule.getFlexibleSearchProperty();

            String attribute = searchProperty.getAttributeName();

            Class propertyClass = Class.forName(searchProperty.getPropertyClassType());

            LogicalOperator operator = searchRule.getLogicalOperator();

            if (String.class.isAssignableFrom(propertyClass)){
                String propertyValue = searchRule.getStringPropertyValue();
                Path path = rootEntity.get(attribute);
                switch (operator){
                    case STRING_IS:
                        predicates.add(criteriaBuilder.equal(path, propertyValue));
                        break;
                    case STRING_NOT:
                        predicates.add(criteriaBuilder.notEqual(path, propertyValue));
                        break;
                    case STRING_LIKE:
                        predicates.add(criteriaBuilder.like(path, "%" + propertyValue + "%"));
                        break;
                    case STRING_NOT_LIKE:
                        predicates.add(criteriaBuilder.notLike(path, "%" + propertyValue + "%"));
                        break;
                }
            } else if (Integer.class.isAssignableFrom(propertyClass)){
                Integer parameterValue = searchRule.getIntegerPropertyValue();
                Path path = rootEntity.get(attribute);

                switch (operator){
                    case NUMERIC_EQUAL:
                        predicates.add(criteriaBuilder.equal(path, parameterValue));
                        break;
                    case NUMERIC_NOT_EQUAL:
                        predicates.add(criteriaBuilder.notEqual(path, parameterValue));
                        break;
                    case NUMERIC_GREATER_THAN:
                        predicates.add(criteriaBuilder.greaterThan(path, parameterValue));
                        break;
                    case NUMERIC_GREATER_THAN_OR_EQUAL_TO:
                        predicates.add(criteriaBuilder.greaterThanOrEqualTo(path, parameterValue));
                        break;
                    case NUMERIC_LESS_THAN:
                        predicates.add(criteriaBuilder.lessThan(path, parameterValue));
                        break;
                    case NUMERIC_LESS_THAN_OR_EQUAL_TO:
                        predicates.add(criteriaBuilder.lessThanOrEqualTo(path, parameterValue));
                        break;
                }
            }else if (Float.class.isAssignableFrom(propertyClass)){
                Float parameterValue = searchRule.getFloatPropertyValue();
                Path path = rootEntity.get(attribute);
                switch (operator){
                    case NUMERIC_EQUAL:
                        predicates.add(criteriaBuilder.equal(path, parameterValue));
                        break;
                    case NUMERIC_NOT_EQUAL:
                        predicates.add(criteriaBuilder.notEqual(path, parameterValue));
                        break;
                    case NUMERIC_GREATER_THAN:
                        predicates.add(criteriaBuilder.greaterThan(path, parameterValue));
                        break;
                    case NUMERIC_GREATER_THAN_OR_EQUAL_TO:
                        predicates.add(criteriaBuilder.greaterThanOrEqualTo(path, parameterValue));
                        break;
                    case NUMERIC_LESS_THAN:
                        predicates.add(criteriaBuilder.lessThan(path, parameterValue));
                        break;
                    case NUMERIC_LESS_THAN_OR_EQUAL_TO:
                        predicates.add(criteriaBuilder.lessThanOrEqualTo(path, parameterValue));
                        break;
                }
            }else if (Long.class.isAssignableFrom(propertyClass)){
                Long parameterValue = searchRule.getLongPropertyValue();
                Path path = rootEntity.get(attribute);
                switch (operator){
                    case NUMERIC_EQUAL:
                        predicates.add(criteriaBuilder.equal(path, parameterValue));
                        break;
                    case NUMERIC_NOT_EQUAL:
                        predicates.add(criteriaBuilder.notEqual(path, parameterValue));
                        break;
                    case NUMERIC_GREATER_THAN:
                        predicates.add(criteriaBuilder.greaterThan(path, parameterValue));
                        break;
                    case NUMERIC_GREATER_THAN_OR_EQUAL_TO:
                        predicates.add(criteriaBuilder.greaterThanOrEqualTo(path, parameterValue));
                        break;
                    case NUMERIC_LESS_THAN:
                        predicates.add(criteriaBuilder.lessThan(path, parameterValue));
                        break;
                    case NUMERIC_LESS_THAN_OR_EQUAL_TO:
                        predicates.add(criteriaBuilder.lessThanOrEqualTo(path, parameterValue));
                        break;
                }
            }else if (Double.class.isAssignableFrom(propertyClass)){
                Double parameterValue = searchRule.getDoublePropertyValue();
                Path path = rootEntity.get(attribute);
                switch (operator){
                    case NUMERIC_EQUAL:
                        predicates.add(criteriaBuilder.equal(path, parameterValue));
                        break;
                    case NUMERIC_NOT_EQUAL:
                        predicates.add(criteriaBuilder.notEqual(path, parameterValue));
                        break;
                    case NUMERIC_GREATER_THAN:
                        predicates.add(criteriaBuilder.greaterThan(path, parameterValue));
                        break;
                    case NUMERIC_GREATER_THAN_OR_EQUAL_TO:
                        predicates.add(criteriaBuilder.greaterThanOrEqualTo(path, parameterValue));
                        break;
                    case NUMERIC_LESS_THAN:
                        predicates.add(criteriaBuilder.lessThan(path, parameterValue));
                        break;
                    case NUMERIC_LESS_THAN_OR_EQUAL_TO:
                        predicates.add(criteriaBuilder.lessThanOrEqualTo(path, parameterValue));
                        break;
                }
            }else if (BigDecimal.class.isAssignableFrom(propertyClass)){
                BigDecimal parameterValue = searchRule.getBigDecimalPropertyValue();
                Path path = rootEntity.get(attribute);
                switch (operator){
                    case NUMERIC_EQUAL:
                        predicates.add(criteriaBuilder.equal(path, parameterValue));
                        break;
                    case NUMERIC_NOT_EQUAL:
                        predicates.add(criteriaBuilder.notEqual(path, parameterValue));
                        break;
                    case NUMERIC_GREATER_THAN:
                        predicates.add(criteriaBuilder.greaterThan(path, parameterValue));
                        break;
                    case NUMERIC_GREATER_THAN_OR_EQUAL_TO:
                        predicates.add(criteriaBuilder.greaterThanOrEqualTo(path, parameterValue));
                        break;
                    case NUMERIC_LESS_THAN:
                        predicates.add(criteriaBuilder.lessThan(path, parameterValue));
                        break;
                    case NUMERIC_LESS_THAN_OR_EQUAL_TO:
                        predicates.add(criteriaBuilder.lessThanOrEqualTo(path, parameterValue));
                        break;
                }
            }else if (SystemCodes.class.isAssignableFrom(propertyClass)){
                SystemCodes systemCodes = searchRule.getSystemCodesPropertyValue();
                Path path = rootEntity.get(attribute + ".key");
                switch (operator){
                    case ENTITY_IS:
                        predicates.add(criteriaBuilder.equal(path, systemCodes.getKey()));
                        break;
                    case ENTITY_IS_NOT:
                        predicates.add(criteriaBuilder.notEqual(path, systemCodes.getKey()));
                        break;
                }
            }else if (BaseEntity.class.isAssignableFrom(propertyClass)){
                Long baseEntityId = searchRule.getBaseEntityIdValue();
                Path path = rootEntity.get(attribute + ".id");
                switch (operator){
                    case ENTITY_IS:
                        predicates.add(criteriaBuilder.equal(path, baseEntityId));
                        break;
                    case ENTITY_IS_NOT:
                        predicates.add(criteriaBuilder.notEqual(path, baseEntityId));
                        break;
                }
            }else if (Boolean.class.isAssignableFrom(propertyClass)){
                Boolean parameterValue = searchRule.getBooleanPropertyValue();
                Path path = rootEntity.get(attribute);
                switch (operator){
                    case BOOLEAN_IS:
                        predicates.add(criteriaBuilder.equal(path, parameterValue));
                        break;
                    case BOOLEAN_NOT:
                        predicates.add(criteriaBuilder.notEqual(path, parameterValue));
                        break;
                }
            }else if (Date.class.isAssignableFrom(propertyClass)){
                Date startDate = searchRule.getStartDate();
                Date endDate = searchRule.getEndDate();
                Path path = rootEntity.get(attribute);
                switch (operator){
                    case DATE_EQUAL:
                        predicates.add(criteriaBuilder.equal(path, startDate));
                        break;
                    case DATE_NOT_EQUAL:
                        predicates.add(criteriaBuilder.notEqual(path, startDate));
                        break;
                    case DATE_GREATER_THAN:
                        predicates.add(criteriaBuilder.greaterThan(path, startDate));
                        break;
                    case DATE_LESS_THAN:
                        predicates.add(criteriaBuilder.lessThan(path, startDate));
                        break;
                    case DATE_BETWEEN:
                        predicates.add(criteriaBuilder.between(path, startDate, endDate));
                        break;
                }
            }
        }

        Predicate predicate;
        if (searchCriteria.getMatchType().equals(MatchType.CONJUNCTION)) {
            predicate = criteriaBuilder.conjunction();
            predicate = criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        } else {
            predicate = criteriaBuilder.disjunction();
            predicate = criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()]));
        }

        return predicate;
    }


    private void prepareSortByOption(SearchBean flexibleSearchBean,
                                     CriteriaBuilder criteriaBuilder,
                                     CriteriaQuery criteriaQuery,
                                     Root rootEntity) {
        String sortBy = flexibleSearchBean.getSortBy();
        if (!StringUtil.isEmpty(sortBy)) {
            SortOrder sortOrder = flexibleSearchBean.getSortOrder();

            if (sortOrder.equals(SortOrder.ASCENDING)) {
                criteriaQuery.orderBy(criteriaBuilder.asc(rootEntity.get(sortBy)));
            } else {
                criteriaQuery.orderBy(criteriaBuilder.desc(rootEntity.get(sortBy)));
            }
        }
    }

    @Override
    public long countByFlexibleSearch(SearchBean flexibleSearchBean) {

        Class entityType = null;
        try {
            entityType = Class.forName(flexibleSearchBean.getEntityType());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(entityType);

        Root rootEntity = criteriaQuery.from(entityType);

        criteriaQuery.select(criteriaBuilder.count(rootEntity));

        prepareJoinTables(flexibleSearchBean, rootEntity);

        buildCriteria(flexibleSearchBean, criteriaBuilder, criteriaQuery, rootEntity);

        return ((Integer)entityManager.createQuery(criteriaQuery).getSingleResult()).longValue();
	    
    }
}
