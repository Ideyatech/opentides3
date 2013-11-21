package com.ideyatech.flexiblesearch.service.impl;

import com.ideyatech.flexiblesearch.bean.SearchProperty;
import com.ideyatech.flexiblesearch.dao.SearchPropertyDao;
import com.ideyatech.flexiblesearch.service.FlexibleSearchPropertyService;
import org.opentides.service.impl.BaseCrudServiceImpl;

/**
 * Created with IntelliJ IDEA.
 * User: neilnamoro
 * Date: 9/11/13
 * Time: 3:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class FlexibleSearchPropertyServiceImpl extends BaseCrudServiceImpl<SearchProperty>
        implements FlexibleSearchPropertyService{

    @Override
    public SearchPropertyDao getDao() {
        return (SearchPropertyDao)super.getDao();
    }
}
