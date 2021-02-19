package com.analysys.automation.modules.app.job;

import com.analysys.automation.common.utils.Constant;
import com.analysys.automation.common.utils.DateUtils;
import com.analysys.automation.modules.app.entity.TestdataEntity;
import com.analysys.automation.modules.app.service.EventRecordService;
import com.analysys.automation.modules.app.service.ProfileRecordService;
import com.analysys.automation.modules.app.service.TestdataService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;

@Component
public class TestDataJob {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TestdataService testdataService;

    @Autowired
    private ProfileRecordService profileRecordService;

    @Autowired
    private EventRecordService eventRecordService;

    @Autowired
    private RestTemplate restTemplate;

    private final int THREAD_NUMBER = 30;


    @Scheduled(cron = "0/30 * * * * *")
    public synchronized void sendDataRequests() throws InterruptedException {
        List<TestdataEntity> testdataList = testdataService.selectList(new EntityWrapper<TestdataEntity>().eq("status", Constant.Status.NORMAL.getValue()));
        for (TestdataEntity testdataEntity : testdataList) {
            String groupName = testdataEntity.getGroupName();
            String appKey = testdataEntity.getAppkey();
            String url = testdataEntity.getUrl();

            List<Date> dateList = testdataService.queryDateList(groupName);
            int dateOffset = DateUtils.daysBetweenTwoDates(dateList.get(0), DateUtils.addDateDays(new Date(), -1));
            testdataEntity.setStatus(Constant.Status.RUNNING.getValue());
            testdataService.updateById(testdataEntity);
            logger.info("data upload task id = " + testdataEntity.getTestdataId() + ", profile begin : " + DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            profileRecordService.sendRequests(groupName, url, appKey, dateOffset, THREAD_NUMBER);
            logger.info("data upload task id = " + testdataEntity.getTestdataId() + ", profile end : " + DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            for (int i = 0; i < 5; i++) {
                logger.info("wait for one minute. ");
                Thread.sleep(1000);
            }
            logger.info("data upload task id = " + testdataEntity.getTestdataId() + ", event begin : " + DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            eventRecordService.sendRequests(groupName, url, appKey, dateOffset, THREAD_NUMBER);
            logger.info("data upload task id = " + testdataEntity.getTestdataId() + ", event end : " + DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            testdataEntity.setStatus(Constant.Status.FINISHED.getValue());
            testdataService.updateById(testdataEntity);
        }
    }
}
