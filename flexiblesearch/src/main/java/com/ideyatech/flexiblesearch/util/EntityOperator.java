package com.ideyatech.flexiblesearch.util;

/**
 * Created with IntelliJ IDEA.
 * User: neilnamoro
 * Date: 9/12/13
 * Time: 2:47 PM
 * To change this template use File | Settings | File Templates.
 */
public enum EntityOperator{
    IS("is equal to"),
    IS_NOT("is not equal to");

    private String toString;

    private EntityOperator(String toString){
        this.toString = toString;
    }

    @Override
    public String toString() {
        return toString;
    }
}
