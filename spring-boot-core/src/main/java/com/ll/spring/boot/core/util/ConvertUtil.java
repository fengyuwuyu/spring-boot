package com.ll.spring.boot.core.util;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ConvertUtil<T, R> {
    
    public List<R> convertList(List<T> list, Function<T, R> func) {
        if (list == null || list.size() == 0) {
            return Collections.emptyList();
        }
        return list.stream().map(func).collect(Collectors.toList());
    }

}

