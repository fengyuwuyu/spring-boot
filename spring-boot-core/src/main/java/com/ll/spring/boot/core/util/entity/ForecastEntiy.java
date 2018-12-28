package com.ll.spring.boot.core.util.entity;

public class ForecastEntiy {

    private String date;
    private String high;
    private String low;
    private String type;

    public ForecastEntiy() {
    }

    public ForecastEntiy(String date, String high, String low, String type) {
        this.date = date;
        this.high = high;
        this.low = low;
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ForecastEntiy [date=" + date + ", high=" + high + ", low=" + low + ", type=" + type + "]";
    }

}
