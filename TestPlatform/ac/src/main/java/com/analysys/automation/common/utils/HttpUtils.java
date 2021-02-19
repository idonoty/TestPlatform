package com.analysys.automation.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

public class HttpUtils {
    /**
     * 如果请求返回的结果不是JSON格式，需要设置backJson为false，否则会导致默认JSON解析失败而导致错误
     *
     * @param restTemplate
     * @param url
     * @param appKey
     * @param content
     * @param offset
     * @param backJson
     */
    public static String sendPostRequest(RestTemplate restTemplate, String url, String appKey, String content, int offset, boolean backJson) {
        if (!backJson) {
            //由于FastJsonHttpMessageConverter在最前面,导致返回结果不是json格式时会报错,因此需要删除该转换器
            List<HttpMessageConverter<?>> converterList = restTemplate.getMessageConverters();
            int index = -1;
            for (int i = 0; i < converterList.size(); i++) {
                if (converterList.get(i) instanceof FastJsonHttpMessageConverter) {
                    index = i;
                    break;
                }
            }
            if (index > -1)
                converterList.remove(index);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Connection", "keep-alive");
        headers.add("Content-Type", "text/plain;charset=UTF-8");
        headers.add("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
        headers.add("Accept", "text/plain;charset=UTF-8");
        HttpEntity<String> entity = new HttpEntity<String>(content, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        String body = response.getBody().toString();
        if (response != null && response.getStatusCodeValue() == 200) {
//            System.out.println(body);
        } else {
            System.out.println("请求失败");
        }

        return body;
    }

    public static String doPost(String url, String appKey, String token, String cohortCode) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);// 创建httpPost
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(30000).build();
        httpPost.setConfig(requestConfig);
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("appKey", appKey);
        httpPost.setHeader("token", token);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cohortCode", cohortCode);
        StringEntity entity = new StringEntity(JSON.toJSONString(jsonObject), "UTF-8");
        httpPost.setEntity(entity);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpPost);
            StatusLine status = response.getStatusLine();
            int state = status.getStatusCode();
            if (state == HttpStatus.SC_OK) {
                org.apache.http.HttpEntity responseEntity = response.getEntity();
                String jsonString = EntityUtils.toString(responseEntity);
                return jsonString;
            } else {
                System.out.println(state);
                org.apache.http.HttpEntity responseEntity = response.getEntity();
                String jsonString = EntityUtils.toString(responseEntity);
                System.out.println("http请求内容失败, 返回内容为 " + jsonString);
            }
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
