package com.ideyatech.flexiblesearch.dao;

import com.ideyatech.flexiblesearch.bean.SearchBean;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: neilnamoro
 * Date: 9/11/13
 * Time: 3:53 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DynamicSearchDao {

    /**
     *
     * @param flexibleSearchBean
     * @param start
     * @param total
     * @return
     */
    public List findByFlexibleSearch(SearchBean flexibleSearchBean, int start, int total);

    /**
     *
     * @return
     */
    public long countByFlexibleSearch(SearchBean flexibleSearchBean);

}
