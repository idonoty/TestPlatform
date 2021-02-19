package com.analysys.automation.modules.app.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.analysys.automation.common.utils.HttpUtils;
import com.analysys.automation.common.utils.Ret;
import com.analysys.automation.modules.sys.controller.AbstractController;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * 
 *
 * @author analysys QA Team
 * @email qa@analysys.com.cn
 * @date 2019-03-18 15:40:27
 */
@RestController
@RequestMapping("app/cohort")
public class CohortController extends AbstractController {
    @Value("${automation.cohort.url}")
    private String url;
    @Value("${automation.cohort.appKey}")
    private String appKey;
    @Value("${automation.cohort.token}")
    private String token;
    @Value("${automation.cohort.cohortCode}")
    private String cohortCode;

    @GetMapping("/getCohortUsers")
    public Ret getCohortUsers(){
        List<String> xwhoList = new ArrayList<>();
        try {
            String response = HttpUtils.doPost(url, appKey, token, cohortCode);
            if(StringUtils.isBlank(response)) {
                return Ret.error("获取用户分群出错了");
            }
            JSONObject jsonObject = JSON.parseObject(response);
            for(Object user : JSONArray.parseArray(jsonObject.get("users").toString())) {
                JSONObject userObject = JSONObject.parseObject(user.toString());
                xwhoList.add(userObject.get("xwho").toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Ret.ok().put("list", xwhoList);
    }
}
