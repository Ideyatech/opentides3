package com.ideyatech.flexiblesearch.service.impl;

import com.ideyatech.flexiblesearch.bean.SearchRule;
import com.ideyatech.flexiblesearch.dao.SearchRuleDao;
import com.ideyatech.flexiblesearch.service.FlexibleSearchRuleService;
import org.opentides.service.impl.BaseCrudServiceImpl;

/**
 * Created with IntelliJ IDEA.
 * User: neilnamoro
 * Date: 9/11/13
 * Time: 3:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class FlexibleSearchRuleServiceImpl extends BaseCrudServiceImpl<SearchRule>
        implements FlexibleSearchRuleService{

    @Override
    public SearchRuleDao getDao() {
        return (SearchRuleDao)super.getDao();
    }
}
