package com.ideyatech.flexiblesearch.util;

/**
 * Created with IntelliJ IDEA.
 * User: neilnamoro
 *
 */
public enum MatchType {
    CONJUNCTION("and"),
    DISJUNCTION("or");

    private String toString;

    private MatchType(String toString){
        this.toString = toString;
    }

    @Override
    public String toString() {
        return toString;
    }
}
