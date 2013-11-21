package com.ideyatech.flexiblesearch.service.impl;

import com.ideyatech.flexiblesearch.bean.SearchCriteria;
import com.ideyatech.flexiblesearch.dao.SearchCriteriaDao;
import com.ideyatech.flexiblesearch.service.FlexibleSearchCriteriaService;
import org.opentides.service.impl.BaseCrudServiceImpl;

/**
 * Created with IntelliJ IDEA.
 * User: neilnamoro
 * Date: 9/11/13
 * Time: 3:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class FlexibleSearchCriteriaServiceImpl extends BaseCrudServiceImpl<SearchCriteria>
        implements FlexibleSearchCriteriaService{

    @Override
    public SearchCriteriaDao getDao() {
        return (SearchCriteriaDao)super.getDao();
    }
}
