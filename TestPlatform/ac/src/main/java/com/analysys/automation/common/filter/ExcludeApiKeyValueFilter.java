package com.analysys.automation.common.filter;

import com.alibaba.fastjson.serializer.ValueFilter;

import java.util.ArrayList;
import java.util.List;

public class ExcludeApiKeyValueFilter implements ValueFilter {
    @Override
    public Object process(Object o, String s, Object o1) {
        List<String> excludedKey = new ArrayList<>();
        excludedKey.add("queueNo");
        excludedKey.add("queueIdle");
        excludedKey.add("reportUpdateTime");
        excludedKey.add("taskId");
        excludedKey.add("status");
        //excludedKey.add("");



        if(excludedKey.contains(s)) {
            return "";
        }
        return o1;
    }
}
