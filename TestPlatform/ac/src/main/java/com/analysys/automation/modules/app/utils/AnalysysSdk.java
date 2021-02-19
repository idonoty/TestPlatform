package com.analysys.automation.modules.app.utils;

import cn.com.analysys.javasdk.AnalysysJavaSdk;
import cn.com.analysys.javasdk.Collecter;

import java.util.Map;

public class AnalysysSdk extends AnalysysJavaSdk {
    private final Map<String, Object> egBaseProperties;


    public AnalysysSdk(Collecter collecter, String appId) {
        super(collecter, appId);
        egBaseProperties = null;
    }

    @Override
    public void initBaseProperties() {
    }


}
