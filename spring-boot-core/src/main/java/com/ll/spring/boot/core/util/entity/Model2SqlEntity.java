package com.ll.spring.boot.core.util.entity;

import java.util.Arrays;
import java.util.List;

import com.ll.spring.boot.core.model.EnumOperator;

public class Model2SqlEntity {

    private String columnName;
    private List<Object> values;
    private EnumOperator operator;

    public Model2SqlEntity() {
    }


    public Model2SqlEntity(String columnName, Object value) {
        this(columnName, value, EnumOperator.EQ);
    }
    
    public Model2SqlEntity(String columnName, Object value, EnumOperator operator) {
        this(columnName, Arrays.asList(value), operator);
    }
    
    public Model2SqlEntity(String columnName, List<Object> values) {
        this(columnName, values, EnumOperator.EQ);
    }

    public Model2SqlEntity(String columnName, List<Object> values, EnumOperator operator) {
        this.columnName = columnName;
        this.values = values;
        this.operator = operator;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public List<Object> getValues() {
        return values;
    }

    public void setValues(List<Object> values) {
        this.values = values;
    }

    public EnumOperator getOperator() {
        return operator;
    }

    public void setOperator(EnumOperator operator) {
        this.operator = operator;
    }

    @Override
    public String toString() {
        return "Model2SqlEntity [columnName=" + columnName + ", values=" + values + ", operator=" + operator + "]";
    }

}
