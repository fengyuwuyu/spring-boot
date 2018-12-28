package com.ll.spring.boot.core.util;

import java.util.List;

import com.alibaba.fastjson.JSON;

/**
 * Title: developerportal_guangdong<br>
 * Description: <br>
 * 
 * @date: Jul 4, 2012 1:40:43 PM <br>
 * @author lufei
 */

public class JsonUtil {
	
	public static <T> List<T> jsonToArray(String json, Class<T> clazz) {
		return JSON.parseArray(json, clazz);
	}
    
    public static <T> String objToJson(T t) {
        if (t == null) {
            return null;
        }
        return JSON.toJSONString(t);
    }
    
	public static <T> T strToJson(String json, Class<T> clazz) {
    	return JSON.parseObject(json, clazz);
    }
    
    
}
