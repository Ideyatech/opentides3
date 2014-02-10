package com.ideyatech.flexiblesearch.util;

/**
 * Created with IntelliJ IDEA.
 * User: neilnamoro
 * Date: 9/12/13
 * Time: 2:47 PM
 * To change this template use File | Settings | File Templates.
 */
public enum NumericOperator {
    EQUAL("=="),
    NOT_EQUAL("<>"),
    GREATER_THAN(">"),
    GREATER_THAN_OR_EQUAL_TO(">="),
    LESS_THAN("<"),
    LESS_THAN_OR_EQUAL_TO("<="),
    BETWEEN("between");

    private String toString;

    private NumericOperator(String toString){
        this.toString = toString;
    }

    @Override
    public String toString() {
        return toString;
    }
}
