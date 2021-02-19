package com.analysys.automation.modules.app.service;

import org.apache.commons.io.FileUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 带有参数的Post请求
 * NameValuePair
 */
public class testPost {
    public static void main(String[] args) throws Exception {
        // 创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建http POST请求
        HttpPost httpPost = new HttpPost("http://192.168.8.67:4005/ark/event/analysis");
        // 设置2个post参数，一个是scope、一个是q
        List<NameValuePair> parameters = new ArrayList<NameValuePair>(0);
        parameters.add(new BasicNameValuePair("content", "{\"measures\":[{\"eventCode\":\"$Anything\",\"aggregator\":\"unique\",\"$uid\":\"7dn\",\"filter\":{\"conditions\":[],\"relation\":\"and\"}}],\"byFields\":[],\"filter\":{\"conditions\":[],\"relation\":\"and\"},\"id\":\"\",\"crowd\":[\"$ALL\"],\"samplingFactor\":1,\"fromDate\":\"\",\"toDate\":\"\",\"dateValue\":\"过去7日\",\"relativeTimeRange\":\"7,1,day\",\"compareFromDate\":\"\",\"compareToDate\":\"\",\"compareDateValue\":null,\"compareRelativeTimeRange\":\"\",\"unit\":\"day\",\"chartsType\":\"line\"}"));
        // 构造一个form表单式的实体
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters);
        // 将请求实体设置到httpPost对象中
        httpPost.setEntity(formEntity);
        //伪装浏览器
        httpPost.setHeader("User-Agent",
                "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
        httpPost.setHeader("cookie", "JSESSIONID=B7A366AD7A4D0CA12E5CED66F482FADB; _ga=GA1.2.1709731984.1548224196; ARK_ID=JSd45eecfee8f508fe95ca6f87aa124685d45e; Hm_lvt_a4dea2b41d114aa99128974e1c910c62=1555988309; mp_96b84420a1a32e448f73e7b9ffccebdb_mixpanel=%7B%22distinct_id%22%3A%20%2216a5279784177-0e3a24cb63ff3a-38395f03-1fa400-16a5279784296b%22%2C%22%24initial_referrer%22%3A%20%22https%3A%2F%2Fdocs.analysys.cn%2Fark%2Fintegration%2Fsdk%22%2C%22%24initial_referring_domain%22%3A%20%22docs.analysys.cn%22%7D; mp_a37bac8664a332481726ae49603f9f63_mixpanel=%7B%22distinct_id%22%3A%20%22424340ae-8ea5-4dbe-af32-f3ad0128723e%22%7D; Hm_lvt_d981851bd0d388f5b0fa75295b96745d=1557129441,1559705130; csrftoken=Gz0kmA3AQDTmXRQgJ13wDlcokuTWVU9u7iBymxpGU3501q7MG6AKj0QmsrV68APm; mp_cba3e0b25b88d270fbde3afdfb3a706f_mixpanel=%7B%22distinct_id%22%3A%20%2216bc062aef71e5-0f25719731afaf-3e385b04-1fa400-16bc062aef842c%22%2C%22%24device_id%22%3A%20%2216bc062aef71e5-0f25719731afaf-3e385b04-1fa400-16bc062aef842c%22%2C%22%24initial_referrer%22%3A%20%22%24direct%22%2C%22%24initial_referring_domain%22%3A%20%22%24direct%22%7D; _hjid=e249f9da-3c36-41b3-ae4a-00026263f90c; TGT=eyJhbGciOiJIUzUxMiJ9.ZXlKaGJHY2lPaUprYVhJaUxDSmxibU1pT2lKQk1USTRRMEpETFVoVE1qVTJJbjAuLmpaaGtvbWs4d3l6amR0RlFDNjAyU0EuVTlUcVNMUERRTm5TSnllY1BPVFNOdFc4S25kaDVGWDJGZVlsdWhvUWhYRjUzVWVQQWtBZGZLZkJCalNVd25NMUcwSjRGQ2xFMDdfOFVNVlFpbFV5bkt3cEFBdm1pZUNsT3VvLUJkOUoySXhUYVdRcEUxSkRxTVotdVA1SXlBNlQ4ZFFMOFJkV3RTRnBBcWZRZzB1RWxlYU9YbEc2RC15WEdHQ2lOZVNBMVV5X0NxNzZBemNqcEc1QTFhclFUOFFtV2JDSHo5Vk1rSW1vWGZTZzIzbjV0WkNtSDNDTEhGQnFrODZZYTRVN3F6WS44ak5DVHVoUmxCTzNxVVpmSGdXRXVR.fsXYm-TlQelRbVMCcueOeJLCzz7vKBRT6dDn0wc1DzT95DNKc4O5GcnKRi23Nyu9WZdgxCcvW4IeBMmjXRXiHw; mp_0e16d5f07cb56dd0829a5e2458964e6f_mixpanel=%7B%22distinct_id%22%3A%20%22168795ab897102-0e5cc307cc1768-5c11301c-1fa400-168795ab89833a%22%2C%22%24device_id%22%3A%20%22168795ab897102-0e5cc307cc1768-5c11301c-1fa400-168795ab89833a%22%2C%22%24initial_referrer%22%3A%20%22%24direct%22%2C%22%24initial_referring_domain%22%3A%20%22%24direct%22%7D; amplitude_id_9124ac4ed73cfbe52f3c620bfbc3ae61analysys.cn=eyJkZXZpY2VJZCI6ImY5Mjk5YTQ4LTU2YjctNDcxMC04NGY2LWFmNWM5ZTRkMWM3ZFIiLCJ1c2VySWQiOm51bGwsIm9wdE91dCI6ZmFsc2UsInNlc3Npb25JZCI6MTU2MjM4ODg3MTg0NSwibGFzdEV2ZW50VGltZSI6MTU2MjM4OTEyMDY3MCwiZXZlbnRJZCI6MjM0LCJpZGVudGlmeUlkIjowLCJzZXF1ZW5jZU51bWJlciI6MjM0fQ==; FZ_STROAGE=eyJTRUVTSU9OSUQiOiJjZTMxOTM1ZjIxMTcwNDc4IiwiU0VFU0lPTkRBVEUiOjE1NjMzMjU2MjM5NTEsIkFSS19JRCI6IkpTZDQ1ZWVjZmVlOGY1MDhmZTk1Y2E2Zjg3YWExMjQ2ODVkNDVlIiwiQVJLREVCVUciOjIsIkFOU0FQUElEIjoiMDEzMzE4YWM5MDI1MTMxYiIsIk5QUyI6eyJ1bmRlZmluZWQiOnsicG9wTnVtIjoxfX0sIkFOU1VQTE9BRFVSTCI6Imh0dHBzOi8vdWF0LmFuYWx5c3lzLmNuOjQwODkvdXAiLCJBUktGUklTVElNRSI6ZmFsc2UsIkFSS1VQTE9BRFVSTCI6Imh0dHBzOi8vdWF0LmFuYWx5c3lzLmNuOjQwODkvdXAiLCJGUklTVERBWSI6IjIwMTkwNzE3IiwiRlJJU1RJTUUiOmZhbHNlLCJBUktfTE9HSU5JRCI6InpBOHpzcmM3MTAwMDAzMSIsIkFSS19TVVBFUiI6eyJjb21wYW55Ijoi5piT6KeCIiwiY29tcGFueV9jb2RlIjoiekE4enNyYzciLCJwcm9qZWN0X25hbWUiOiLmmJPop4LmlrnoiJ8ifX0=; ARK_PROJECT_USERNAME=MzFhYmQ5NTkzZTk5ODNlY3xhZG1pbg==; ARK_TOKEN=MzFhYmQ5NTkzZTk5ODNlY3xhZG1pbnwxNTY0NDUzOTYyOTc5fDhhNmEyZGFjYTNiZjhjMDBjMDIxMTYxYjUwNzU1OTcxfDE=; ARK_CONNECT_TIMEOUT=420000; ark_project=31abd9593e9983ec; isOne=1");
        httpPost.setHeader("Accept", "application/json, text/plain, */*");
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        CloseableHttpResponse response = null;
        try {
            // 执行请求
            response = httpclient.execute(httpPost);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                String content = EntityUtils.toString(response.getEntity(), "UTF-8");
                //内容写入文件
                FileUtils.writeStringToFile(new File("E:\\devtest\\oschina-param.html"), content, "UTF-8");
                System.out.println("内容长度："+content.length());
            }
        } finally {
            if (response != null) {
                response.close();
            }
            httpclient.close();
        }
    }
}