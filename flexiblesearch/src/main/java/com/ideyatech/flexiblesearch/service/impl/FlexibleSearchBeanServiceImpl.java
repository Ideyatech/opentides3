package com.ideyatech.flexiblesearch.service.impl;

import com.ideyatech.flexiblesearch.bean.SearchBean;
import com.ideyatech.flexiblesearch.dao.SearchBeanDao;
import com.ideyatech.flexiblesearch.service.FlexibleSearchBeanService;
import org.opentides.service.impl.BaseCrudServiceImpl;

/**
 * Created with IntelliJ IDEA.
 * User: neilnamoro
 * Date: 9/11/13
 * Time: 3:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class FlexibleSearchBeanServiceImpl extends BaseCrudServiceImpl<SearchBean>
        implements FlexibleSearchBeanService{

    @Override
    public SearchBeanDao getDao() {
        return (SearchBeanDao)super.getDao();
    }
}
