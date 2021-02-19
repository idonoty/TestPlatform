package com.analysys.automation.modules.app.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.analysys.automation.common.utils.Constant;
import com.analysys.automation.common.utils.DateUtils;
import com.analysys.automation.modules.app.entity.EventRecordEntity;
import com.analysys.automation.modules.app.entity.ProfileLogEntity;
import com.analysys.automation.modules.app.entity.ProfileRecordEntity;
import com.analysys.automation.modules.app.service.EventRecordService;
import com.analysys.automation.modules.app.service.ProfileLogService;
import com.analysys.automation.modules.app.service.ProfileRecordService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

@Component
public class ProfileEventJob {
    @Autowired
    private ProfileLogService profileLogService;

    @Autowired
    private ProfileRecordService profileRecordService;

    @Autowired
    private EventRecordService eventRecordService;

    @Scheduled(cron = "0/5 * * * * *")
    public void parseDataFile() {
        List<ProfileLogEntity> list = profileLogService.queryProfileLogByStatus(Constant.Status.NORMAL.getValue());
        for (ProfileLogEntity profileLogEntity : list) {
            Map<String, ProfileRecordEntity> map = parseProfileFile(profileLogEntity);
            parseEventFile(profileLogEntity, map);
            profileLogEntity.setStatus(Constant.Status.FINISHED.getValue());
            profileLogService.updateById(profileLogEntity);
        }
    }

    private Map<String, ProfileRecordEntity> parseProfileFile(ProfileLogEntity profileLogEntity) {
        Map<String, ProfileRecordEntity> map = new HashMap<>();
        List<ProfileRecordEntity> list = new ArrayList<>();

        try {
            FileReader reader = new FileReader(profileLogEntity.getProfileDir());
            BufferedReader br = new BufferedReader(reader);
            String preLine = "";
            String line = "";
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                if (!preLine.equals("") && preLine.equals(line)) {
                    continue;
                }
                JSONArray arr = (JSONArray) JSON.parse(line);
                JSONObject object = (JSONObject) arr.get(0);
                JSONObject xcontext = object.getJSONObject("xcontext");

                String email = "";
                String phone = "";

                ProfileRecordEntity profileRecordEntity = new ProfileRecordEntity();
                profileRecordEntity.setGroupName(profileLogEntity.getGroupName());
                profileRecordEntity.setXwho(object.getString("xwho"));
                profileRecordEntity.setXwhen(object.getLong("xwhen"));
                profileRecordEntity.setTfXwhen(DateUtils.parse(new Date(object.getLong("xwhen")), DateUtils.DATE_TIME_PATTERN));
                profileRecordEntity.setCreateUserId(profileLogEntity.getCreateUserId());
                profileRecordEntity.setCreateTime(profileLogEntity.getCreateTime());
                String originalId = xcontext.getString("$original_id");

                if (object.getString("xwhat").equals("$profile_set")) {
//                    System.out.println(line);
                    if (xcontext.containsKey("phone") && StringUtils.isNotBlank(xcontext.getString("phone"))) {
                        phone = String.valueOf(Long.parseLong(xcontext.getString("phone")) + new Random().nextInt(100000));
                        xcontext.put("phone", phone);
                        ProfileRecordEntity tempRecord = new ProfileRecordEntity();
                        if (map.get(object.getString("xwho")) != null) {
                            tempRecord = map.get(object.getString("xwho"));
                        }
                        tempRecord.setPhone(phone);
                        map.put(object.getString("xwho"), tempRecord);
                    }
                    if (xcontext.containsKey("$email") && StringUtils.isNotBlank(xcontext.getString("$email"))) {
                        String[] emailSuffix = {"@qq.com", "@163.com", "@126.com", "@gmail.com"};
                        int index = new Random().nextInt(12) % emailSuffix.length;
                        email = phone + emailSuffix[index];
                        xcontext.put("$email", email);

                        ProfileRecordEntity tempRecord = new ProfileRecordEntity();
                        if (map.get(object.getString("xwho")) != null) {
                            tempRecord = map.get(object.getString("xwho"));
                        }
                        tempRecord.setEmail(email);
                        map.put(object.getString("xwho"), tempRecord);
                    }
                    if (xcontext.containsKey("name")) {
                        if (map.get(object.getString("xwho")) != null) {
                            ProfileRecordEntity tempRecord = map.get(object.getString("xwho"));
                            xcontext.put("name", tempRecord.getPhone());
                        }
                    }

                    if (xcontext.containsKey("company")) {
                        if (map.get(object.getString("xwho")) != null) {
                            ProfileRecordEntity tempRecord = map.get(object.getString("xwho"));
                            xcontext.put("company", tempRecord.getPhone());
                        }
                    }
                    System.out.println(arr.toJSONString());
                }

                if (StringUtils.isNotBlank(originalId)) {
                    profileRecordEntity.setOrginalId(originalId);
                    if (map.get(object.getString("xwho")) != null) {
                        ProfileRecordEntity tempRecord = map.get(object.getString("xwho"));
                        map.put(originalId, tempRecord);
                    }
                }
                profileRecordEntity.setEmail(email);
                profileRecordEntity.setPhone(phone);
                profileRecordEntity.setContent(arr.toJSONString());
                list.add(profileRecordEntity);

                preLine = line;
            }

            profileRecordService.insertBatch(list);
        } catch (IOException |
                ParseException e) {
            profileLogEntity.setStatus(11);
            profileLogService.updateById(profileLogEntity);
            e.printStackTrace();
        }

        return map;
    }

    private void parseEventFile(ProfileLogEntity profileLogEntity, Map<String, ProfileRecordEntity> map) {
        List<EventRecordEntity> list = new ArrayList<>();

        try {
            FileReader reader = new FileReader(profileLogEntity.getEventDir());
            BufferedReader br = new BufferedReader(reader);
            String preLine = "";
            String line = "";
            while ((line = br.readLine()) != null) {
//                System.out.println(line);
                if (!preLine.equals("") && preLine.equals(line)) {
                    continue;
                }
                JSONArray arr = (JSONArray) JSON.parse(line);
                JSONObject object = (JSONObject) arr.get(0);
                String xwho = object.getString("xwho");
                ProfileRecordEntity profileRecordEntity = map.get(xwho);
                EventRecordEntity eventRecordEntity = new EventRecordEntity();

                JSONObject xcontext = object.getJSONObject("xcontext");
                if (xcontext.containsKey("phone")) {
                    if(profileRecordEntity == null) {
                        System.out.println("用户信息不存在，不入库");
                        continue;
                    }
                    xcontext.put("phone", profileRecordEntity.getPhone());
                }
                if (xcontext.containsKey("mail")) {
                    if(profileRecordEntity == null) {
                        System.out.println("用户信息不存在，不入库");
                        continue;
                    }
                    xcontext.put("mail", profileRecordEntity.getEmail());
                }
                if (xcontext.containsKey("name")) {
                    if(profileRecordEntity == null) {
                        System.out.println("用户信息不存在，不入库");
                        continue;
                    }
                    xcontext.put("name", profileRecordEntity.getPhone());
                }
                if (xcontext.containsKey("corporate_name")) {
                    if(profileRecordEntity == null) {
                        System.out.println("用户信息不存在，不入库");
                        continue;
                    }
                    xcontext.put("corporate_name", profileRecordEntity.getPhone());
                }
                System.out.println(arr.toJSONString());
                eventRecordEntity.setContent(arr.toJSONString());
                eventRecordEntity.setXwho(xwho);
                eventRecordEntity.setXwhen(object.getLong("xwhen"));
                eventRecordEntity.setTfXwhen(DateUtils.parse(new Date(object.getLong("xwhen")), DateUtils.DATE_TIME_PATTERN));
                eventRecordEntity.setXwhat(object.getString("xwhat"));
                eventRecordEntity.setGroupName(profileLogEntity.getGroupName());
                eventRecordEntity.setCreateUserId(profileLogEntity.getCreateUserId());
                eventRecordEntity.setCreateTime(profileLogEntity.getCreateTime());
                preLine = line;
                list.add(eventRecordEntity);
            }

            eventRecordService.insertBatch(list);
        } catch (Exception e) {
            profileLogEntity.setStatus(12);
            profileLogService.updateById(profileLogEntity);
            e.printStackTrace();
        }
    }
}
