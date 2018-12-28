package com.ll.spring.boot.core.util.entity;

import java.util.List;

public class WeatherData {

    private List<ForecastEntiy> forecast;

    public List<ForecastEntiy> getForecast() {
        return forecast;
    }

    public void setForecast(List<ForecastEntiy> forecast) {
        this.forecast = forecast;
    }

    @Override
    public String toString() {
        return "WeatherData [forecast=" + forecast + "]";
    }

}
