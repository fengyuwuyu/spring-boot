package com.ll.spring.boot.core.wrapper;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;

public class MybatisCustomWrapper<T> extends EntityWrapper<T> {

    /**
     * 
     */
    private static final long serialVersionUID = -7458057490945166994L;

    
    public Wrapper<T> bit(String column, Object params) {
        return bit(true, column, params);
    }
    
    public Wrapper<T> bit(boolean condition, String column, Object params) {
        if (condition) {
            sql.WHERE(formatSql(String.format("%s & {0} = {0}", column), params));
        }
        return this;
    }
    
    public Wrapper<T> unbit(String column, Object params) {
        return unbit(true, column, params);
    }
    
    public Wrapper<T> unbit(boolean condition, String column, Object params) {
        if (condition) {
            sql.WHERE(formatSql(String.format("%s & {0} != {0}", column), params));
        }
        return this;
    }
    
    public static void main(String[] args) {
        MybatisCustomWrapper<Object> wrapper = new MybatisCustomWrapper<>();
        wrapper.unbit("handle_type", 8L);
        System.out.println(wrapper.getSqlSegment());
    }
}
