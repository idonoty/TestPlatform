package com.analysys.automation.modules.app.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.analysys.automation.common.utils.*;
import com.analysys.automation.modules.app.dao.ProfileRecordDao;
import com.analysys.automation.modules.app.entity.ProfileRecordEntity;
import com.analysys.automation.modules.app.service.ProfileRecordService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Service("profileRecordService")
public class ProfileRecordServiceImpl extends ServiceImpl<ProfileRecordDao, ProfileRecordEntity> implements ProfileRecordService {
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<ProfileRecordEntity> page = this.selectPage(
                new Query<ProfileRecordEntity>(params).getPage(),
                new EntityWrapper<ProfileRecordEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void sendRequests(String groupName, String url, String appKey, int offset, int threadNumber) throws InterruptedException {
        List<ProfileRecordEntity> profileList = selectList(new EntityWrapper<ProfileRecordEntity>().eq("group_name", groupName));

        ExecutorService es = Executors.newFixedThreadPool(threadNumber);
        CountDownLatch countDownLatch = new CountDownLatch(threadNumber);
        List<List<?>> list = ListUtils.splitList(profileList, threadNumber);

        for (int i = 0; i < list.size(); i++) {
            List<?> tempList = list.get(i);
            es.execute(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < tempList.size(); j++) {
                        String content = createPostContent(((ProfileRecordEntity) tempList.get(j)).getContent(), appKey, offset);
                        HttpUtils.sendPostRequest(restTemplate, url, appKey, content, offset, false);
                    }
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
        es.shutdown();
    }

    private String createPostContent(String originalContent, String appKey, int offset) {
        JSONArray array = JSON.parseArray(originalContent);
        JSONObject object = array.getJSONObject(0);
        object.put("xwhen", object.getLongValue("xwhen") + offset * 24 * 3600 * 1000L);
        object.put("appid", appKey);
        JSONObject xcontext = (JSONObject) object.get("xcontext");
        if (xcontext.containsKey("$first_visit_time")) {
            xcontext.put("$first_visit_time", DateUtils.format(DateUtils.addDateDays(xcontext.getDate("$first_visit_time"), offset), "yyyy-MM-dd HH:mm:ss.SSS"));
        }
        return JSONArray.toJSONString(array);
    }


}
