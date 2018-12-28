package com.ll.spring.boot.core.util.entity;

public class WeatherEntity {

    private Integer status;
    private WeatherData data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public WeatherData getData() {
        return data;
    }

    public void setData(WeatherData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "WeatherEntity [status=" + status + ", data=" + data + "]";
    }

}
