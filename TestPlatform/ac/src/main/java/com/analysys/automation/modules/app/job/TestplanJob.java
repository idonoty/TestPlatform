package com.analysys.automation.modules.app.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.analysys.automation.common.filter.ExcludeApiKeyValueFilter;
import com.analysys.automation.common.filter.ExcludeKeyValueFilter;
import com.analysys.automation.common.utils.Constant;
import com.analysys.automation.modules.app.entity.TestcaseEntity;
import com.analysys.automation.modules.app.entity.TestcaseResultEntity;
import com.analysys.automation.modules.app.entity.TestplanEntity;
import com.analysys.automation.modules.app.service.TestcaseResultService;
import com.analysys.automation.modules.app.service.TestcaseService;
import com.analysys.automation.modules.app.service.TestplanService;
import com.analysys.automation.modules.app.utils.Urlcode;
import com.analysys.automation.test.JDBCUtils;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.skyscreamer.jsonassert.JSONCompare;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.JSONCompareResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.analysys.automation.common.utils.HttpClientUtil.getUBACookie;

@Component
public class TestplanJob {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TestplanService testplanService;

    @Autowired
    private TestcaseService testcaseService;

    @Autowired
    private TestcaseResultService testcaseResultService;

    //当走离线openapi时，接口需要增加后缀
    private String uri;

    @Scheduled(cron = "0/5 * * * * *")
    public synchronized void runTestplans() {
        List<TestplanEntity> runningList = testplanService.selectList(new EntityWrapper<TestplanEntity>().eq("status", Constant.Status.RUNNING.getValue()));
        //如果已经有running状态的job，不再运行新的任务。
        if (runningList.size() > 0) {
            return;
        }
        List<TestplanEntity> list = testplanService.selectList(new EntityWrapper<TestplanEntity>().eq("status", Constant.Status.NORMAL.getValue()));
        if (list.size() == 0) {
//            logger.warn("###############没有需要执行的Test Plan#################");
            return;
        }
        logger.warn("###############准备查询可执行的Test Plan#################");
        //一次只跑一个任务计划，只修改一个任务计划为RUNNING状态
        TestplanEntity testplanEntity = list.get(0);
        testplanEntity.setStatus(Constant.Status.RUNNING.getValue());

        testplanService.updateById(testplanEntity);
        runTestCasesUnderTestplan(testplanEntity);
    }

    private void runTestCasesUnderTestplan(TestplanEntity testplanEntity) {
        List<TestcaseEntity> list = null;
        int caseProgress=0;
        int caseAllNum=0;
        if (testplanEntity.getProjectName() != null && testplanEntity.getProjectName() != null) {
            Wrapper<TestcaseEntity> wrapper = new EntityWrapper<TestcaseEntity>().eq("project_name",
                    testplanEntity.getProjectName()).eq("status", Constant.Status.NORMAL.getValue());
            String moduleNmae = testplanEntity.getModuleName();
            String version = testplanEntity.getVersion();
            if (moduleNmae != null && !moduleNmae.equals("")) {
                wrapper = wrapper.in("module_name", moduleNmae);
            }
            if (version != null && !version.equals("")) {
                wrapper = wrapper.eq("version", version);
            }
            //logger.info("查询sql执行了");
            list = testcaseService.selectList(wrapper);
            logger.info("执行的用例数：" + list.size());
            caseAllNum=list.size();
        } else if (testplanEntity.getTestcaseId() != null) {
            QueryRunner runner = new QueryRunner(JDBCUtils.getDataSource());
            //自定义用例执行sql,先去掉数组的括号
            String testcaseIdlist = testplanEntity.getTestcaseId();
            if (!"".equals(testcaseIdlist) && testcaseIdlist != null && testcaseIdlist.startsWith("[") && testcaseIdlist.endsWith("]")) {
                testcaseIdlist = testcaseIdlist.substring(1, testcaseIdlist.length() - 1);
            }
            String sql = String.format("select testcase_id as testcaseId , testcase_code as testcaseCode,project_name as " +
                            "projectName,module_name as moduleName,`version`,`uri`,`method`,`data`,`status`, expected_result as  " +
                            "expectedResult,description,creator_id as creatorId,create_time as createTime,updator_id as updatorId " +
                            ",update_time as updateTime from t_testcase where `status`=10 and testcase_id in (%s)",
                    testcaseIdlist);
            try {
                list = runner.query(sql, new BeanListHandler<TestcaseEntity>(TestcaseEntity.class));
                logger.info("执行的用例数：" + list.size());
                caseAllNum=list.size();
            } catch (SQLException e) {
                testplanEntity.setStatus(Constant.Status.OVERWITHERROR.getValue());
                testplanService.updateById(testplanEntity);
                e.printStackTrace();
            }
//            list = testcaseService.selectList(new EntityWrapper<TestcaseEntity>().eq("status",
//                    Constant.Status.NORMAL.getValue()).eq("testcase_id",testplanEntity.getTestcaseId()));
        } else {
            testplanEntity.setStatus(Constant.Status.OVERWITHERROR.getValue());
            testplanService.updateById(testplanEntity);
            return;
        }
        logger.warn("----------------开始执行【" + testplanEntity.getTestplanName() + "】计划中的所有测试用例-------------------");
        if (list == null || list.size() == 0) {
            testplanEntity.setStatus(Constant.Status.FINISHED.getValue());
            testplanService.updateById(testplanEntity);
            return;
        }
        //取登录接口获取cookie
        String cookie = getUBACookie(testplanEntity.getEndpoint(), testplanEntity.getCookie());
        logger.info("登录的cookie是" + cookie);
//        logger.info("*************************" + list.size());
//        CountDownLatch latch = new CountDownLatch(list.size() - 1);
//        list.forEach(testcaseEntity -> {
//            try {
//                if (testcaseEntity.getProjectName().equals("方舟")) {
//                    runArkTestCase(testcaseEntity, testplanEntity,cookie);
//                }else if (testcaseEntity.getProjectName().equals("OPEN-API")){
//                    runApiTestCase(testcaseEntity, testplanEntity);
//                }
//
//                Thread.sleep(1000);
//            } catch (Exception e) {
//                logger.info(e.getMessage());
//                TestcaseResultEntity testcaseResultEntity = new TestcaseResultEntity(testcaseEntity);
//                testcaseResultEntity.setResult("失败");
//                testcaseResultEntity.setUri(uri);
//                testcaseResultEntity.setActualResult(e.getMessage());
//                testcaseResultEntity.setTestcaseCode(testcaseEntity.getTestcaseCode());
//                testcaseResultEntity.setTestplanName(testplanEntity.getTestplanName());
//                testcaseResultEntity.setCreateTime(new Date());
//                testcaseResultEntity.setDescription(testcaseEntity.getDescription()+" 错误：执行异常!");
//                testcaseResultService.insert(testcaseResultEntity);
//            }
//            latch.countDown();
//            logger.info("*************************" + Long.toString(latch.getCount()));
//        });
//        try {
//            latch.await();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        for (TestcaseEntity forentity : list) {
            caseProgress++;
            logger.info(caseProgress+"/"+caseAllNum);
            try {
                testplanEntity.setProgress(caseProgress+"/"+caseAllNum);
                testplanService.updateById(testplanEntity);
                if (forentity.getProjectName().equals("方舟")) {
                    runArkTestCase(forentity, testplanEntity, cookie);
                } else if (forentity.getProjectName().equals("OPEN-API")) {
                    runApiTestCase(forentity, testplanEntity);
                }
                Thread.sleep(1000);
            } catch (Exception e) {
                logger.info(e.getMessage());
                TestcaseResultEntity testcaseResultEntity = new TestcaseResultEntity(forentity);
                testcaseResultEntity.setResult("失败");
                testcaseResultEntity.setUri(uri);
                testcaseResultEntity.setActualResult(e.getMessage());
                testcaseResultEntity.setTestcaseCode(forentity.getTestcaseCode());
                testcaseResultEntity.setTestplanName(testplanEntity.getTestplanName());
                testcaseResultEntity.setCreateTime(new Date());
                testcaseResultEntity.setDescription(forentity.getDescription() + " 错误：执行异常!");
                testcaseResultService.insert(testcaseResultEntity);
            }
        }

        testplanEntity.setStatus(Constant.Status.FINISHED.getValue());
        testplanService.updateById(testplanEntity);
        logger.warn("----------------执行计划【" + testplanEntity.getTestplanName() + "】中的所有测试用例结束， 共执行" + list.size() + "个接口测试用例-------------------");
    }

    public static ArrayList<Object> result(String id,
                                           TestcaseEntity testcaseEntity, TestplanEntity testplanEntity, String cookie) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        long X = (long) (Math.random() * 100000000000000L);
        HttpGet httpGet =
                new HttpGet(testplanEntity.getEndpoint() + "/ark/offlinecalc/all/task/" + id + "/result?_t=" + X);
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
        httpGet.setHeader("cookie", cookie);
        httpGet.setHeader("Accept", "application/json, text/plain, */*");
        httpGet.setHeader("Content-Type", "application/x-www-form-urlencoded");
        CloseableHttpResponse response = httpClient.execute(httpGet);
        String body = EntityUtils.toString(response.getEntity(), "UTF-8");
        int statusCode = response.getStatusLine().getStatusCode();
        ArrayList<Object> list = new ArrayList<>();
        list.add(statusCode);
        list.add(body);
        return list;
    }

    public static ArrayList<Object> resultApi(String id,
                                              TestcaseEntity testcaseEntity, TestplanEntity testplanEntity) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet =
                new HttpGet(testplanEntity.getEndpoint() + "/uba/api/async/tasks/" + id);
        String[] headers = testplanEntity.getCookie().split(";");
        httpGet.setHeader("appkey", headers[0]);
        httpGet.setHeader("token", headers[1]);
        httpGet.setHeader("Content-Type", "application/json");
        CloseableHttpResponse response = httpClient.execute(httpGet);
        String body = EntityUtils.toString(response.getEntity(), "UTF-8");
        int statusCode = response.getStatusLine().getStatusCode();
        ArrayList<Object> list = new ArrayList<>();
        list.add(statusCode);
        list.add(body);
        return list;
    }

    private void runArkTestCase(TestcaseEntity testcaseEntity, TestplanEntity testplanEntity, String cookie) throws IOException,
            org.json.JSONException {
        String content = testcaseEntity.getData();
        // 创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String params = "";
        if (content != null && !content.equals("")) {
            params = "?" + content.substring(0, content.indexOf("=")) + "=" + Urlcode.getURLEncoderString(content.substring(content.indexOf("=") + 1, content.length()));
        }
        uri = testcaseEntity.getUri().trim();
        // 创建http POST请求
        HttpPost httpPost = new HttpPost(testplanEntity.getEndpoint().trim() + uri + params.trim());
        logger.info(httpPost.getURI().toString());
        //由于可能条件为url时可能包含=和&，所以不用这种方式添加参数，改用urlcode
//        if (content!=null && content !=""){
//            // 参数处理，核心是 UrlEncodedFormEntity 需要List<NameValuePair>
//            List<NameValuePair> list = new ArrayList();
//            String params_list[]=content.split("&");
//            for(String param : params_list){
//                String values[] = param.split("=");
//                list.add(new BasicNameValuePair(values[0],values[1]));
//            }
//            if (list.size() > 0) {
//                httpPost.setEntity(new UrlEncodedFormEntity(list,"utf-8"));
//            }
//        }
        //设置header内容
        httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
        httpPost.setHeader("cookie", cookie);
        httpPost.setHeader("Accept", "application/json, text/plain, */*");
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        CloseableHttpResponse response = null;
        String body = "";
        int statusCode = 400;
        ArrayList<Object> list;
        try {
            // 执行请求
            response = httpclient.execute(httpPost);
            statusCode = response.getStatusLine().getStatusCode();
            body = EntityUtils.toString(response.getEntity(), "UTF-8");
            if (statusCode == 200 && JSON.parseObject(body).get("datas") != null && !JSON.parseObject(body).get("datas").equals("")) {
                if (JSONPath.read(body, "code").toString().equals("10") && JSONPath.read(body, "msg").toString().startsWith("该请求正在处理中") && JSON.parseObject(JSON.toJSONString(JSON.parseObject(body).get("datas"))).containsKey(
                        "id")) {
                    String id = JSONObject.parseObject(JSONObject.parseObject(body).get("datas").toString()).get(
                            "id").toString();
                    list = result(id, testcaseEntity, testplanEntity, cookie);
                    statusCode = (int) list.get(0);
                    body = (String) list.get(1);
                    String calStatus = JSONObject.parseObject(JSONObject.parseObject(body).get("datas").toString()).get(
                            "calcStatus").toString();
                    while (calStatus.equals("0") || calStatus.equals("1")) {
                        list = result(id, testcaseEntity, testplanEntity, cookie);
                        statusCode = (int) list.get(0);
                        body = (String) list.get(1);
                        calStatus = JSONObject.parseObject(JSONObject.parseObject(body).get("datas").toString()).get(
                                "calcStatus").toString();
                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException e) {
                            logger.error("线程休眠失败：执行result接口失败");
                            e.printStackTrace();
                        }
                    }
                }
            }
        } finally {
            if (response != null) {
                response.close();
            }
            httpclient.close();
        }

//        logger.info("Test Case 【 " + testcaseEntity.getTestcaseId() + " 】返回码为: " + statusCode);
//        logger.warn("Test Case 【 " + testcaseEntity.getTestcaseId() + " 】返回结果为: " + response.getBody());

        logger.info(body);
        TestcaseResultEntity testcaseResultEntity = new TestcaseResultEntity(testcaseEntity);
        if (statusCode >= 400) {
            testcaseResultEntity.setResult("失败");
            testcaseResultEntity.setActualResult(body);
            testcaseResultEntity.setTestcaseCode(testcaseEntity.getTestcaseCode());
            testcaseResultEntity.setTestplanName(testplanEntity.getTestplanName());
            testcaseResultEntity.setCreateTime(new Date());
            testcaseResultEntity.setDescription(testcaseEntity.getDescription() + " 错误：接口返回异常!");
            testcaseResultService.insert(testcaseResultEntity);
        } else {
            //StringUtils.isNotBlank(body)是判断某字符串是否不为空或长度不为0或不由空白符(whitespace)构成
            if (statusCode == 200 && StringUtils.isNotBlank(body)
                    && (!JSONObject.parseObject(body).get("code").toString().equals("0") || JSONObject.parseObject(body).get("msg").toString().endsWith("异常!") || JSONObject.parseObject(body).get("msg").toString().endsWith("异常"))) {
                testcaseResultEntity.setResult("失败");
                testcaseResultEntity.setDescription(testcaseEntity.getDescription() + " 错误：" + JSONObject.parseObject(body).get("msg").toString());
                testcaseResultEntity.setActualResult(body);
                testcaseResultEntity.setTestplanName(testplanEntity.getTestplanName());
                testcaseResultEntity.setTestcaseCode(testcaseEntity.getTestcaseCode());
                testcaseResultEntity.setCreateTime(new Date());
                testcaseResultService.insert(testcaseResultEntity);
                return;
            }

            JSON actualResultString;
            try {
                body.replaceAll("/r", "").replaceAll("/n", "").replaceAll("/r/n", "");
//                actualResultString = (JSON) JSON.parseObject(JSON.parseObject(body).get("datas").toString()).get("result");
//                if (actualResultString==null){
//                    throw new JSONException();
//                }
                actualResultString = JSON.parseObject(body);
            } catch (JSONException e) {
                testcaseResultEntity.setResult("失败");
                testcaseResultEntity.setDescription(testcaseEntity.getDescription() + " 错误：实际返回无数据,或解析实际结果JSON内容失败");
                testcaseResultEntity.setActualResult(body);
                testcaseResultEntity.setTestplanName(testplanEntity.getTestplanName());
                testcaseResultEntity.setTestcaseCode(testcaseEntity.getTestcaseCode());
                testcaseResultEntity.setCreateTime(new Date());
                testcaseResultService.insert(testcaseResultEntity);
                return;
            }

            JSON expectedResultString = null;
            String expectedRsultStr = "";
            if (testcaseEntity.getExpectedResult() != null && !testcaseEntity.getExpectedResult().equals("")) {
                expectedRsultStr = testcaseEntity.getExpectedResult().replaceAll("/r", "").replaceAll("/n", "").replaceAll("/r/n", "");
            }
            try {
//                expectedResultString = (JSON) JSON.parseObject(JSON.parseObject(expectedRsultStr).get("datas").toString()).get("result");
//                if (expectedResultString==null){
//                    throw new JSONException();
//                }
                if (!expectedRsultStr.equals("")) {
                    expectedResultString = JSON.parseObject(expectedRsultStr);
                }
            } catch (JSONException e) {
                testcaseResultEntity.setResult("失败");
                testcaseResultEntity.setDescription(testcaseEntity.getDescription() + " 错误：预期结果无数据，或解析预期结果JSON内容失败");
                testcaseResultEntity.setActualResult(body);
                testcaseResultEntity.setTestplanName(testplanEntity.getTestplanName());
                testcaseResultEntity.setTestcaseCode(testcaseEntity.getTestcaseCode());
                testcaseResultEntity.setCreateTime(new Date());
                testcaseResultService.insert(testcaseResultEntity);
                return;
            }


//            /**
//             * 比较结果前，把不需要进行比较的字段内容进行删除，得到一个新的json字符串
//             */
//            List<String> excludedKey = new ArrayList<>();
//            excludedKey.add("uniqueId");
//            excludedKey.add("times");
//            excludedKey.add("requestId");
//            excludedKey.add("curReqId");
//            excludedKey.add("reportUpdateTime");
//            excludedKey.add("queryIds");
//            excludedKey.add("queryId");
//            excludedKey.add("exception");
//            excludedKey.add("content");
//            excludedKey.add("leave");
//            excludedKey.add("excludeDate");
//
//            JSONObject newExpectedJson = new JSONObject();
//            newExpectedJson = JSONObject.parseObject(expectedRsultStr);
//            newExpectedJson = MyJSONUtil.removeJSONObjectExcludeKeys(newExpectedJson, excludedKey);
//
//            JSONObject newActualJson = new JSONObject();
//            newActualJson = JSONObject.parseObject(body);
//            newActualJson = MyJSONUtil.removeJSONObjectExcludeKeys(newActualJson, excludedKey);
//
//            if (!MyJSONUtil.compareTwoJSONObject(newExpectedJson, newActualJson, "$")) {
//                testcaseResultEntity.setResult("失败");
//            } else {
//                testcaseResultEntity.setResult("成功");
//            }

            if (expectedResultString != null && actualResultString != null) {
                String newExpectedResultString = JSON.toJSONString(expectedResultString, new ExcludeKeyValueFilter());
                String newActualResultString = JSON.toJSONString(actualResultString, new ExcludeKeyValueFilter());
                JSONCompareResult compareResult = JSONCompare.compareJSON(newExpectedResultString, newActualResultString, JSONCompareMode.NON_EXTENSIBLE);
                if (compareResult.passed()) {
                    testcaseResultEntity.setResult("成功");
                } else {
                    testcaseResultEntity.setResult("失败");
                    testcaseResultEntity.setDescription(testcaseEntity.getDescription() + " 错误：" + compareResult.getMessage());
                }

                testcaseResultEntity.setActualResult(body);
                testcaseResultEntity.setTestplanName(testplanEntity.getTestplanName());
                testcaseResultEntity.setTestcaseCode(testcaseEntity.getTestcaseCode());
                testcaseResultEntity.setCreateTime(new Date());
                testcaseResultService.insert(testcaseResultEntity);
            } else {
                testcaseResultEntity.setResult("失败");
                testcaseResultEntity.setDescription(testcaseEntity.getDescription() + " 错误：预期结果解析为空");
                testcaseResultEntity.setActualResult(body);
                testcaseResultEntity.setTestplanName(testplanEntity.getTestplanName());
                testcaseResultEntity.setTestcaseCode(testcaseEntity.getTestcaseCode());
                testcaseResultEntity.setCreateTime(new Date());
                testcaseResultService.insert(testcaseResultEntity);
            }
        }
    }


    private void runApiTestCase(TestcaseEntity testcaseEntity, TestplanEntity testplanEntity) throws IOException,
            org.json.JSONException {
        String content = testcaseEntity.getData();
        // 创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建http POST请求
        uri = testcaseEntity.getUri().trim();
        if (testplanEntity.getTestplanName().contains("离线")) {
            uri = testcaseEntity.getUri().trim() + "/async";
        }
        HttpPost httpPost = new HttpPost(testplanEntity.getEndpoint().trim() + uri);
        if (content != null && !content.equals("")) {
            httpPost.setEntity(new StringEntity(content, "utf-8"));
        }
        logger.info(httpPost.getURI().toString());
        String[] headers = (testplanEntity.getCookie().trim()).split(";");
        httpPost.setHeader("appkey", headers[0]);
        httpPost.setHeader("token", headers[1]);
        httpPost.setHeader("Content-Type", "application/json");
        CloseableHttpResponse response = null;
        String body = "";
        int statusCode = 400;
        ArrayList<Object> list;
        try {
            // 执行请求
            response = httpclient.execute(httpPost);
            statusCode = response.getStatusLine().getStatusCode();
            body = EntityUtils.toString(response.getEntity(), "UTF-8");
            if (statusCode == 200 && body != null && !body.equals("")) {
                if (JSON.parseObject(JSON.toJSONString(JSON.parseObject(body))).containsKey("status") && JSON.parseObject(JSON.toJSONString(JSON.parseObject(body))).containsKey("taskId")) {
                    if (JSONPath.read(body, "status").toString().equals("0") || JSONPath.read(body, "status").toString().equals("1")) {
                        String id = JSONObject.parseObject(JSONObject.parseObject(body).get("taskId").toString()).toString();
                        list = resultApi(id, testcaseEntity, testplanEntity);
                        statusCode = (int) list.get(0);
                        body = (String) list.get(1);
                        String calStatus = JSONObject.parseObject(JSONObject.parseObject(body).get("status").toString()).toString();
                        while (calStatus.equals("0") || calStatus.equals("1")) {
                            list = resultApi(id, testcaseEntity, testplanEntity);
                            statusCode = (int) list.get(0);
                            body = (String) list.get(1);
                            calStatus = JSONObject.parseObject(JSONObject.parseObject(body).get("status").toString()).toString();
                            try {
                                Thread.sleep(1500);
                            } catch (InterruptedException e) {
                                logger.error("线程休眠失败：执行result接口失败");
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        } finally {
            if (response != null) {
                response.close();
            }
            httpclient.close();
        }

//        logger.info("Test Case 【 " + testcaseEntity.getTestcaseId() + " 】返回码为: " + statusCode);
//        logger.warn("Test Case 【 " + testcaseEntity.getTestcaseId() + " 】返回结果为: " + response.getBody());

        logger.info(body);
        TestcaseResultEntity testcaseResultEntity = new TestcaseResultEntity(testcaseEntity);
        if (statusCode >= 400) {
            testcaseResultEntity.setResult("失败");
            testcaseResultEntity.setActualResult(body);
            testcaseResultEntity.setUri(uri);
            testcaseResultEntity.setTestcaseCode(testcaseEntity.getTestcaseCode());
            testcaseResultEntity.setTestplanName(testplanEntity.getTestplanName());
            testcaseResultEntity.setCreateTime(new Date());
            testcaseResultEntity.setDescription(testcaseEntity.getDescription() + " 错误：接口返回异常!");
            testcaseResultService.insert(testcaseResultEntity);
        } else {
            //StringUtils.isNotBlank(body)是判断某字符串是否不为空或长度不为0或不由空白符(whitespace)构成
            if (statusCode == 200 && StringUtils.isNotBlank(body) && JSONObject.parseObject(body).containsKey("code") && JSONObject.parseObject(body).containsKey("msg")) {
                if ((!JSONObject.parseObject(body).get("code").toString().equals("0") || JSONObject.parseObject(body).get("msg").toString().contains("异常!") || JSONObject.parseObject(body).get("msg").toString().endsWith("异常"))) {
                    testcaseResultEntity.setResult("失败");
                    testcaseResultEntity.setUri(uri);
                    testcaseResultEntity.setDescription(testcaseEntity.getDescription() + " 错误：" + JSONObject.parseObject(body).get("msg").toString());
                    testcaseResultEntity.setActualResult(body);
                    testcaseResultEntity.setTestplanName(testplanEntity.getTestplanName());
                    testcaseResultEntity.setTestcaseCode(testcaseEntity.getTestcaseCode());
                    testcaseResultEntity.setCreateTime(new Date());
                    testcaseResultService.insert(testcaseResultEntity);
                    return;
                }
            }

            JSON actualResultString;
            try {
                body.replaceAll("/r", "").replaceAll("/n", "").replaceAll("/r/n", "");
                if (JSON.parseObject(body).containsKey("result")) {
                    body = JSON.parseObject(body).getString("result");
                }
                actualResultString = JSON.parseObject(body);
            } catch (JSONException e) {
                testcaseResultEntity.setResult("失败");
                testcaseResultEntity.setUri(uri);
                testcaseResultEntity.setDescription(testcaseEntity.getDescription() + " 错误：实际返回无数据,或解析实际结果JSON内容失败");
                testcaseResultEntity.setActualResult(body);
                testcaseResultEntity.setTestplanName(testplanEntity.getTestplanName());
                testcaseResultEntity.setTestcaseCode(testcaseEntity.getTestcaseCode());
                testcaseResultEntity.setCreateTime(new Date());
                testcaseResultService.insert(testcaseResultEntity);
                return;
            }

            JSON expectedResultString = null;
            String expectedRsultStr = "";
            if (testcaseEntity.getExpectedResult() != null && !testcaseEntity.getExpectedResult().equals("")) {
                expectedRsultStr = testcaseEntity.getExpectedResult().replaceAll("/r", "").replaceAll("/n", "").replaceAll("/r/n", "");
            }
            try {
                if (!expectedRsultStr.equals("")) {
                    if (JSON.parseObject(expectedRsultStr).containsKey("result")) {
                        expectedRsultStr = JSON.parseObject(expectedRsultStr).getString("result");
                    }
                    expectedResultString = JSON.parseObject(expectedRsultStr);
                }
            } catch (JSONException e) {
                testcaseResultEntity.setResult("失败");
                testcaseResultEntity.setUri(uri);
                testcaseResultEntity.setDescription(testcaseEntity.getDescription() + " 错误：预期结果无数据，或解析预期结果JSON内容失败");
                testcaseResultEntity.setActualResult(body);
                testcaseResultEntity.setTestplanName(testplanEntity.getTestplanName());
                testcaseResultEntity.setTestcaseCode(testcaseEntity.getTestcaseCode());
                testcaseResultEntity.setCreateTime(new Date());
                testcaseResultService.insert(testcaseResultEntity);
                return;
            }


            if (expectedResultString != null && actualResultString != null) {
                String newExpectedResultString = JSON.toJSONString(expectedResultString, new ExcludeApiKeyValueFilter());
                String newActualResultString = JSON.toJSONString(actualResultString, new ExcludeApiKeyValueFilter());
                JSONCompareResult compareResult = JSONCompare.compareJSON(newExpectedResultString, newActualResultString, JSONCompareMode.NON_EXTENSIBLE);
                if (compareResult.passed()) {
                    testcaseResultEntity.setResult("成功");
                } else {
                    testcaseResultEntity.setResult("失败");
                    testcaseResultEntity.setDescription(testcaseEntity.getDescription() + " 错误：" + compareResult.getMessage());
                }

                testcaseResultEntity.setActualResult(body);
                testcaseResultEntity.setUri(uri);
                testcaseResultEntity.setTestplanName(testplanEntity.getTestplanName());
                testcaseResultEntity.setTestcaseCode(testcaseEntity.getTestcaseCode());
                testcaseResultEntity.setCreateTime(new Date());
                testcaseResultService.insert(testcaseResultEntity);
            } else {
                testcaseResultEntity.setResult("失败");
                testcaseResultEntity.setDescription(testcaseEntity.getDescription() + " 错误：预期结果解析为空");
                testcaseResultEntity.setActualResult(body);
                testcaseResultEntity.setUri(uri);
                testcaseResultEntity.setTestplanName(testplanEntity.getTestplanName());
                testcaseResultEntity.setTestcaseCode(testcaseEntity.getTestcaseCode());
                testcaseResultEntity.setCreateTime(new Date());
                testcaseResultService.insert(testcaseResultEntity);
            }
        }
    }

}
