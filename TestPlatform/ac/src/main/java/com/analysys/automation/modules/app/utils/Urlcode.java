package com.analysys.automation.modules.app.utils;

import java.io.UnsupportedEncodingException;

public class Urlcode {

    public static String getURLEncoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String URLDecoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {
        String param="content: {\"measures\":[{\"eventCode\":\"$startup\",\"aggregator\":\"general\",\"$uid\":\"7dn\",\"filter\":{\"conditions\":[],\"relation\":\"and\"}}],\"byFields\":[],\"filter\":{\"conditions\":[],\"relation\":\"and\"},\"crowd\":[\"$ALL\"],\"samplingFactor\":1,\"fromDate\":\"\",\"toDate\":\"\",\"dateValue\":\"过去7日\",\"relativeTimeRange\":\"7,1,day\",\"compareFromDate\":\"\",\"compareToDate\":\"\",\"compareDateValue\":null,\"compareRelativeTimeRange\":\"\",\"dateValueMerge\":[[\"过去7日\"]],\"unit\":\"day\",\"chartsType\":\"line\",\"filterHolidayData\":false,\"compareType\":\"lastPeriod\",\"displayType\":\"default\"}";
        System.out.println(getURLEncoderString(param));
        System.out.println(URLDecoderString(getURLEncoderString(param)));
    }
}
