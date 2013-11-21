package com.ideyatech.flexiblesearch.util;

/**
 * Created with IntelliJ IDEA.
 * User: neilnamoro
 * Date: 9/12/13
 * Time: 2:45 PM
 * To change this template use File | Settings | File Templates.
 */
public enum SortOrder {
    ASCENDING("Ascending"),
    DESCENDING("Descending");

    private String name;

    private SortOrder(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
