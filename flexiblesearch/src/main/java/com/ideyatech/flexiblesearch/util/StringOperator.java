package com.ideyatech.flexiblesearch.util;

/**
 * Created with IntelliJ IDEA.
 * User: neilnamoro
 * Date: 9/12/13
 * Time: 2:46 PM
 * To change this template use File | Settings | File Templates.
 */
public enum StringOperator implements LogicalOperator{
    IS("is equal to"),
    NOT("is not equal to"),
    LIKE("is like"),
    NOT_LIKE("is not like");

    private String toString;

    private StringOperator(String toString){
        this.toString = toString;
    }


    @Override
    public String toString() {
        return toString;
    }
}
