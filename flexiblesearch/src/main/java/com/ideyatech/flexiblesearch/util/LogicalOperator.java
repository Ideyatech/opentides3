package com.ideyatech.flexiblesearch.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: neilnamoro
 *
 */
public enum LogicalOperator {
    STRING_IS("is equal to"),
    STRING_NOT("is not equal to"),
    STRING_LIKE("is like"),
    STRING_NOT_LIKE("is not like"),
    NUMERIC_EQUAL("=="),
    NUMERIC_NOT_EQUAL("<>"),
    NUMERIC_GREATER_THAN(">"),
    NUMERIC_GREATER_THAN_OR_EQUAL_TO(">="),
    NUMERIC_LESS_THAN("<"),
    NUMERIC_LESS_THAN_OR_EQUAL_TO("<="),
    DATE_EQUAL("=="),
    DATE_NOT_EQUAL("<>"),
    DATE_GREATER_THAN(">"),
    DATE_LESS_THAN("<"),
    DATE_BETWEEN("between"),
    ENTITY_IS("is equal to"),
    ENTITY_IS_NOT("is not equal to"),
    JUNCTION_AND("and"),
    JUNCTION_OR("or"),
    BOOLEAN_IS("is"),
    BOOLEAN_NOT("not");

    private String toString;

    private LogicalOperator(String toString){
        this.toString = toString;
    }

    public String stringValue(){
        return toString;
    }

    public enum LikePattern{
        PERCENT_BEFORE,
        PERCENT_AFTER,
        PERCENT_AROUND
    }

    public static List<LogicalOperator> getStringOperators(){
        List<LogicalOperator> operators = new ArrayList<LogicalOperator>();
        for (LogicalOperator o : LogicalOperator.values()) {
            if (o.toString().contains("STRING_")){
                operators.add(o);
            }
        }
        return  operators;
    }

    public static List<LogicalOperator> getNumericOperators(){
        List<LogicalOperator> operators = new ArrayList<LogicalOperator>();
        for (LogicalOperator o : LogicalOperator.values()) {
            if (o.toString().contains("NUMERIC_")){
                operators.add(o);
            }
        }
        return  operators;
    }

    public static List<LogicalOperator> getEntityOperators(){
        List<LogicalOperator> operators = new ArrayList<LogicalOperator>();
        for (LogicalOperator o : LogicalOperator.values()) {
            if (o.toString().contains("ENTITY_")){
                operators.add(o);
            }
        }
        return  operators;
    }

    public static List<LogicalOperator> getJunctionOperators(){
        List<LogicalOperator> operators = new ArrayList<LogicalOperator>();
        for (LogicalOperator o : LogicalOperator.values()) {
            if (o.toString().contains("JUNCTION_")){
                operators.add(o);
            }
        }
        return  operators;
    }

    public static List<LogicalOperator> getDateOperators(){
        List<LogicalOperator> operators = new ArrayList<LogicalOperator>();
        for (LogicalOperator o : LogicalOperator.values()) {
            if (o.toString().contains("DATE_")){
                operators.add(o);
            }
        }
        return  operators;
    }

    public static List<LogicalOperator> getBooleanOperators(){
        List<LogicalOperator> operators = new ArrayList<LogicalOperator>();
        for (LogicalOperator o : LogicalOperator.values()) {
            if (o.toString().contains("BOOLEAN_")){
                operators.add(o);
            }
        }
        return  operators;
    }

    public static String adjustLikeValue(String value, LikePattern pattern){
        switch (pattern) {
            case PERCENT_AFTER:
                return value + "%";
            case PERCENT_BEFORE:
                return "%" + value;
            case PERCENT_AROUND:
                return "%" + value + "%";
        }
        return value;
    }
}
