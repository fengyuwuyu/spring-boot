package com.ll.spring.boot.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ll.spring.boot.core.util.entity.ForecastEntiy;
import com.ll.spring.boot.core.util.entity.WeatherEntity;


public class WeatherUtil implements Runnable {

    private static WeatherUtil instance = new WeatherUtil();
    private WeatherEntity weatherEntity;
    private ForecastEntiy forecastEntiy;
    private Logger log = LoggerFactory.getLogger(getClass());

    private WeatherUtil() {
    }

    public static WeatherUtil getInstance() {
        return instance;
    }
    
    

    public void getWeatherJson() {
        // https://www.sojson.com/open/api/weather/json.shtml?city=北京
        String api = "https://www.sojson.com/open/api/weather/json.shtml?city=北京";
        String result = HttpUtils.sendGet(api, null);

        try {
            weatherEntity = JsonUtil.strToJson(result, WeatherEntity.class);
            if (weatherEntity != null && weatherEntity.getData() != null
                    && weatherEntity.getData().getForecast() != null
                    && weatherEntity.getData().getForecast().size() > 0) {
                forecastEntiy = weatherEntity.getData().getForecast().get(0);
            }
        } catch (Exception e) {
            log.error("获取天气失败！", e);
            if (forecastEntiy == null) {
                forecastEntiy = new ForecastEntiy("", "0℃", "0℃", "晴");
            }
        }
    }

    public ForecastEntiy getForecastEntiy() {
        return forecastEntiy;
    }

    @Override
    public void run() {
        
    }

}
