package com.analysys.automation.common.filter;

import com.alibaba.fastjson.serializer.ValueFilter;

import java.util.ArrayList;
import java.util.List;

public class ExcludeKeyValueFilter implements ValueFilter {
    @Override
    public Object process(Object o, String s, Object o1) {
        List<String> excludedKey = new ArrayList<>();
        excludedKey.add("uniqueId");
        excludedKey.add("times");
        excludedKey.add("requestId");
        excludedKey.add("curReqId");
        excludedKey.add("reportUpdateTime");
        excludedKey.add("queryIds");
        excludedKey.add("queryId");
        excludedKey.add("exception");
        excludedKey.add("drillingKey");
        excludedKey.add("content");
        excludedKey.add("leave");
        excludedKey.add("excludeDate");
        excludedKey.add("taskId");
        excludedKey.add("calcStatus");
        excludedKey.add("byValueDic");
        excludedKey.add("byValue");
        excludedKey.add("legend");
        excludedKey.add("legendFormat");
        //excludedKey.add("");



        if(excludedKey.contains(s)) {
            return "";
        }
        return o1;
    }
}
