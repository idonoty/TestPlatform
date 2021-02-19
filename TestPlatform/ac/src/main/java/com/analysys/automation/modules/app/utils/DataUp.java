package com.analysys.automation.modules.app.utils;

import cn.com.analysys.javasdk.AnalysysException;
import cn.com.analysys.javasdk.DEBUG;
import cn.com.analysys.javasdk.SyncCollecter;
import com.analysys.automation.modules.app.entity.TestdataUpEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class DataUp {

    public static String[] xwhats = {"$startup", "$end", "$pageview", "$pageview", "$pageview", "$pageview", "$user_click", "argo_download", "board_add",
            "click_banner", "login", "enter_demo", "watch_video", "assistant_select"};

    public static String[] libs = {"Android", "iOS", "JS", "WeChat", "java", "python","Android", "iOS", "JS"};

    public static String[] brand = {"apple", "huawei", "xiaomi", "samsung", "meizu", "oppo","vivo","apple","xiaomi","xiaomi"};

    public static String[] emails = {"@qq.com", "@163.com", "@outlook.com", "@ali.com"};

    public static String[] lib_versions = {"4.1.1", "3.3.1", "4.2.1", "4.3.2", "3.5.2", "3.6.2"};

    public static int[][] screen = {{1920, 1080}, {1280, 720}, {2560, 1600}, {1920, 1200}, {
            1600, 1200}, {800, 600}, {1024, 768}};

    public static int[] sum = {-199, -10, -1, 0, 1, 10, 99, 100, 1000};

    public static Boolean[] is_trues = {true, false};

    public static String[][] browsers = {{"Chrome", "Chrome 88.0.3212.93"},{"Chrome", "Chrome 87.0.1247.135"},{"Chrome",
            "Chrome 84.0.4147.135"}, {"Safari", "Safari 13.1.2"}, {"Edge", "Edge 84.0.522.63"}, {"Firefox", "Firefox 79.0"},
            {"WeChat", "7.0.15"}, {"QQ Browser", "QQBrower 3.7"}, {"Opera", "Opera 59.1.2926.54067"}};


    public static String[][] citys = {{"中国", "内蒙古", "呼和浩特"}, {"中国", "新疆", "乌鲁木齐"}, {"中国", "江苏", "南京市"}, {"中国", "四川", "成都市"}, {
            "中国", "重庆", "重庆市"}, {"中国", "陕西", "西安市"}, {"中国", "上海", "上海市"}, {"中国", "北京", "北京市"}, {"中国", "浙江", "杭州市"}, {"中国", "广东", "深圳市"},
            {"中国", "广东", "广州市"}, {"中国", "湖南", "长沙市"},{"中国", "湖南", "常德市"},{"中国", "湖南", "株洲市"}, {"中国", "湖南", "娄底市"},{"中国", "山东", "青岛市"},
            {"中国", "湖北", "武汉市"}, {"中国", "广东", "佛山市"},{"中国", "广东", "东莞市"},{"中国", "贵州", "遵义市"},{"中国", "江西", "南昌市"},
            {"美国", "华盛顿", "华盛顿"}, {"美国", "纽约", "纽约"}, {"美国", "纽约", "纽约"}, {"巴西", "圣保罗", "圣保罗"}, {"巴西", "里约热内卢",
            "里约热内卢"}, {"日本", "青森县", "八户市"}, {"日本", "北海道", "夕张市"}, {"英国", "爱丁堡", "爱丁堡"}, {"英国", "伦敦", "伦敦"}, {"俄罗斯", "莫斯科", "莫斯科"},
            {"俄罗斯", "圣彼得堡", "圣彼得堡"}, {"蒙古", "乌兰巴托", "乌兰巴托"}};

    //$utm_campaign_id,$utm_campaign,$utm_medium,$utm_source,$utm_content,$utm_term,$search_keyword
    public static String[][] utm = {{"campaign_id001", "产品运营大会", "cpc", "今日头条", "使用说明", "数据查询"}, {"campaign_id002", "同花顺推广", "同花顺", "同花顺", "召回老用户", "老客唤醒"}, {"campaign_id003", "官网", "官网", "官网", "活动说明", "转化"}, {"campaign_id004", "双11", "双11", "百度", "促销规则", "促活"}, {"campaign_id005", "拉新H5", "拉新", "微信", "获取新用户", "获客"}, {"campaign_id006", "知乎合作", "知乎", "知乎", "忠实用户留存", "留存"}};
    //$url,$title,$referrer,$referrer_domain
    public static String[][] url = {{"https://www.analysys.cn/event", "事件分析", "https://www.analysys.cn/my", "www.analysys" +
            ".cn", "事件搜索词"},
            {"https://www.baidu.com/search", "百度搜索", "https://www.baidu.com", "www.baidu.com", "百度搜索词"},
            {"https://uat.analysys.cn/session/book", "书签页", "https://uat.analysys.cn/session/user", "uat.analysys.cn", "uat" +
                    "环境"},
            {"https://sports.sohu.com/?spm=smpc.home.top-nav.7.1599551855620eiMBirD", "搜狐体育", "https://www.sogou" +
                    ".com/goingsouhu", "www.sogou.com", "搜狗搜狐"},
            {"https://192.168.8.111:4005/project-management/integra", "项目管理", "https://192.168.8.111:4005/login", "192.168.8" +
                    ".111:4005", "8.111"}, {"https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&rsv_idx=1&tn=baidu&wd=易观", "百度搜索"
            , "https://www.baidu.com", "www.baidu.com", "百度搜易观"}};


    public static ArrayList<String> numlist = new ArrayList<String>(Arrays.asList("1", "2", "3")), stringlist =
            new ArrayList<String>(Arrays.asList("衣服", "裤子", "鞋子", "帽子")),datelist =
            new ArrayList<String>(Arrays.asList("2020-01-10 12:33:01", "2015-10-10 03:21:31", "2012-08-15 23:33:01")), shuiguolist =
            new ArrayList<String>(Arrays.asList("苹果", "香蕉", "梨", "橘子", "西瓜", "菠萝"));
    public static ArrayList[] arraydatas = {numlist, stringlist, shuiguolist,datelist};

    public static String getTime(int time, int num) {  //生成时间用作关联
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(time, +num);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(calendar.getTime());
    }


    public static HashMap<String, Object> dataMap() {
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("sumid", sum[(int) (Math.random() * sum.length)]);
        dataMap.put("$lib", libs[(int) (Math.random() * libs.length)]);
        dataMap.put("$lib_version", lib_versions[(int) (Math.random() * lib_versions.length)]);
        int citynum = (int) (Math.random() * citys.length);
        dataMap.put("$country", citys[citynum][0]);
        dataMap.put("$province", citys[citynum][1]);
        dataMap.put("$city", citys[citynum][2]);
        dataMap.put("arraydata", arraydatas[(int) (Math.random() * arraydatas.length)]);
        dataMap.put("$importFlag", 1);
        return dataMap;
    }


    public static HashMap<String, Object> profileMap() {
        HashMap<String, Object> profilemap = dataMap();
        //时间取值为当前时间-63200到64640-63200
        //profilemap.put("$signup_time", getTime(Calendar.MINUTE, (int) (Math.random() * 64640) - 63200));
        profilemap.put("$signup_time", getTime(Calendar.MINUTE, (int) (Math.random() * 64640) - 63200));
        profilemap.put("birthday", getTime(Calendar.MINUTE, (int) (Math.random() * 64640) - 63200));
        profilemap.put("$first_visit_time", getTime(Calendar.MINUTE, (int) (Math.random() * 64640) - 63200));
        profilemap.put("$email",
                ((long) (Math.random() * 80000000000L) + 10000000000L) + emails[(int) (Math.random() * emails.length)]);
        profilemap.put("is_true_profile", is_trues[(int) (Math.random() * is_trues.length)]);
        return profilemap;
    }

    public static Map<String, Object> eventMap() {
        HashMap<String, Object> eventmap = dataMap();
        int bronum = (int) (Math.random() * browsers.length);
        int scrnum = (int) (Math.random() * screen.length);
        eventmap.put("is_true_event", is_trues[(int) (Math.random() * is_trues.length)]);
        eventmap.put("$browser", browsers[bronum][0]);
        eventmap.put("$brand", brand[(int) (Math.random() * brand.length)]);
        eventmap.put("$browser_version", browsers[bronum][1]);
        eventmap.put("$request_time", getTime(Calendar.MINUTE, (int) (Math.random() * 64640) - 63200));
        eventmap.put("$ip",
                ((int) (Math.random() * 100) + 101) + "." + ((int) (Math.random() * 100) + 101) + "." + ((int) (Math.random() * 100) + 101) + "." + ((int) (Math.random() * 90) + 10));
        eventmap.put("$screen_width", screen[scrnum][0]);
        eventmap.put("$screen_height", screen[scrnum][1]);
        return eventmap;
    }

    public static long timeToLong(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //日期转时间戳（毫秒）
        assert date != null;
        return date.getTime();
    }

    public static void dataupAlias(TestdataUpEntity testdataUpEntity) {
        int userNum = testdataUpEntity.getUserNum();
        int eventMin = testdataUpEntity.getEventMin();
        int eventMax = testdataUpEntity.getEventMax();
        long timeMin = timeToLong(testdataUpEntity.getTimeMin());
        long timeMax = timeToLong(testdataUpEntity.getTimeMax());
        String app_key = testdataUpEntity.getAppkey();
        String analysys_url = testdataUpEntity.getUrl();
        AnalysysSdk analysys = new AnalysysSdk(new SyncCollecter(analysys_url), app_key);
        int eventSize = 0;
        int profileKafka = 0;
        int eventKafka = 0;
        int profileSize;
        try {
            analysys.setDebugMode(DEBUG.OPENANDSAVE);
            //用户数作为循环次数
            for (profileSize = 1; profileSize <= userNum; profileSize++) {
                int upsize = (int) ((Math.random() * (eventMax - eventMin)) + eventMin);
                long id = (long) ((Math.random() * 800000000000L) + 100000000000L);
                String session = "session" + id;
                String xwho = "匿名" + id;
                String login_xwho = "登录" + id;
                //从1000取随机数，一半的为匿名用户，四分之一为直接登录，四分之一为匿名用户登录
                long ran = (int) (Math.random() * 1000) + 1;
                if (ran < 500) {
                    analysys.profileSet(xwho, false, profileMap(), libs[(int) (Math.random() * 4)],((long) (Math.random() * (timeMax - timeMin)) + timeMin) + "");
                    profileKafka = profileKafka + 1;
                } else if (ran < 750) {
                    analysys.profileSet(login_xwho, true, profileMap(), libs[(int) (Math.random() * 4)],((long) (Math.random() * (timeMax - timeMin)) + timeMin) + "");
                    profileKafka = profileKafka + 1;
                } else {
                    analysys.profileSet(xwho, false, profileMap(), libs[(int) (Math.random() * 4)],((long) (Math.random() * (timeMax - timeMin)) + timeMin) + "");
                    analysys.alias(login_xwho, xwho, libs[(int) (Math.random() * 4)],((long) (Math.random() * (timeMax - timeMin)) + timeMin) + "");
                    analysys.profileSet(login_xwho, true, profileMap(), libs[(int) (Math.random() * 4)],((long) (Math.random() * (timeMax - timeMin)) + timeMin) + "");
                    profileKafka = profileKafka + 3;
                    eventKafka = eventKafka + 1;
                }
                for (int j = 0; j < upsize; j++) {
                    Map<String, Object> eventmap = eventMap();
                    eventmap.put("$session_id", session);
                    String xwhat = xwhats[(int) (Math.random() * xwhats.length)];
                    if ((xwhat.equals("$startup") || xwhat.equals("$end") || xwhat.equals("$pageview")) && ran >= 500) {
                        int urlnum = (int) (Math.random() * url.length);
                        eventmap.put("$url", url[urlnum][0]);
                        eventmap.put("$title", url[urlnum][1]);
                        eventmap.put("$referrer", url[urlnum][2]);
                        eventmap.put("$referrer_domain", url[urlnum][3]);
                        eventmap.put("$search_keyword", url[urlnum][4]);
                        if (xwhat.equals("$pageview") && ran > 750) {
                            int utmnum = (int) (Math.random() * utm.length);
                            eventmap.put("$utm_campaign_id", utm[utmnum][0]);
                            eventmap.put("$utm_campaign", utm[utmnum][1]);
                            eventmap.put("$utm_medium", utm[utmnum][2]);
                            eventmap.put("$utm_source", utm[utmnum][3]);
                            eventmap.put("$utm_content", utm[utmnum][4]);
                            eventmap.put("$utm_term", utm[utmnum][5]);
                        }
                        ;
                    }
                    if (ran < 500) {
                        analysys.track(xwho, false, xwhat, eventmap,
                                libs[(int) (Math.random() * 4)], ((long) (Math.random() * (timeMax - timeMin)) + timeMin) + "");
                        eventSize = eventSize + 1;
                        eventKafka = eventKafka + 1;
                    } else if (ran < 750) {
                        analysys.track(login_xwho, true, xwhat, eventmap, libs[(int) (Math.random() * 4)],
                                ((long) (Math.random() * (timeMax - timeMin)) + timeMin) + "");
                        eventSize = eventSize + 1;
                        eventKafka = eventKafka + 1;
                    } else {
                        analysys.track(xwho, false, xwhat, eventmap, libs[(int) (Math.random() * 4)],
                                ((long) (Math.random() * (timeMax - timeMin)) + timeMin) + "");
                        analysys.track(login_xwho, true, xwhat, eventmap, libs[(int) (Math.random() * 4)],
                                ((long) (Math.random() * (timeMax - timeMin)) + timeMin) + "");
                        eventSize = eventSize + 2;
                        eventKafka = eventKafka + 2;
                    }
                }
                System.out.println("当前用户：" + profileSize);
            }
            System.out.println("上报的事件数为：" + eventSize + ",用户数为：" + (profileSize - 1) + "，kafka—event条数：" + eventKafka + "，kafka" +
                    "—profile条数：" + profileKafka);
            //或者也可以使用自定义的时间戳
//            String myxWhen = "1569859200000";
//            System.out.println("第1个profile");
//            analysys.profileSet(login_xwho, true, profileMap(), lib, myxWhen);
        } catch (AnalysysException e) {
            e.printStackTrace();
            analysys.flush();
        }

    }


    public static void dataup(TestdataUpEntity testdataUpEntity) {
        int userNum = testdataUpEntity.getUserNum();
        int eventMin = testdataUpEntity.getEventMin();
        int eventMax = testdataUpEntity.getEventMax();
        long timeMin = timeToLong(testdataUpEntity.getTimeMin());
        long timeMax = timeToLong(testdataUpEntity.getTimeMax());
        String app_key = testdataUpEntity.getAppkey();
        String analysys_url = testdataUpEntity.getUrl();
        AnalysysSdk analysys = new AnalysysSdk(new SyncCollecter(analysys_url), app_key);
        int eventSize = 0;
        int profileKafka = 0;
        int eventKafka = 0;
        int profileSize;
        try {
            analysys.setDebugMode(DEBUG.CLOSE);
            //用户数作为循环次数
            for (profileSize = 1; profileSize <= userNum; profileSize++) {
                int upsize = (int) ((Math.random() * (eventMax - eventMin)) + eventMin);
                long id = (long) ((Math.random() * 800000000000L) + 100000000000L);
                String session = "session" + id;
                String xwho = "匿名" + id;
                String login_xwho = "登录" + id;
                //从1000取随机数，6成为匿名用户，4成登录用户，不走alias
                long ran = (int) (Math.random() * 1000) + 1;
                if (ran < 600) {
                    analysys.profileSet(xwho, false, profileMap(), libs[(int) (Math.random() * 4)]);
                    profileKafka = profileKafka + 1;
                } else {
                    analysys.profileSet(login_xwho, true, profileMap(), libs[(int) (Math.random() * 4)]);
                    profileKafka = profileKafka + 1;
                }
                for (int j = 0; j < upsize; j++) {
                    Map<String, Object> eventmap = eventMap();
                    eventmap.put("$session_id", session);
                    String xwhat = xwhats[(int) (Math.random() * xwhats.length)];
                    long ranxwhat = (int) (Math.random() * 1000) + 1;
                    if ((xwhat.equals("$startup") || xwhat.equals("$end") || xwhat.equals("$pageview")) && ranxwhat >= 500) {
                        int urlnum = (int) (Math.random() * url.length);
                        eventmap.put("$url", url[urlnum][0]);
                        eventmap.put("$title", url[urlnum][1]);
                        eventmap.put("$referrer", url[urlnum][2]);
                        eventmap.put("$referrer_domain", url[urlnum][3]);
                        eventmap.put("$search_keyword", url[urlnum][4]);
                        if (xwhat.equals("$pageview") && ran > 750) {
                            int utmnum = (int) (Math.random() * utm.length);
                            eventmap.put("$utm_campaign_id", utm[utmnum][0]);
                            eventmap.put("$utm_campaign", utm[utmnum][1]);
                            eventmap.put("$utm_medium", utm[utmnum][2]);
                            eventmap.put("$utm_source", utm[utmnum][3]);
                            eventmap.put("$utm_content", utm[utmnum][4]);
                            eventmap.put("$utm_term", utm[utmnum][5]);
                        }
                    }
                    if (ran < 600) {
                        analysys.track(xwho, false, xwhat, eventmap,
                                libs[(int) (Math.random() * 4)], ((long) (Math.random() * (timeMax - timeMin)) + timeMin) + "");
                        eventSize = eventSize + 1;
                        eventKafka = eventKafka + 1;
                    } else {
                        analysys.track(login_xwho, true, xwhat, eventmap, libs[(int) (Math.random() * 4)],
                                ((long) (Math.random() * (timeMax - timeMin)) + timeMin) + "");
                        eventSize = eventSize + 1;
                        eventKafka = eventKafka + 1;
                    }
                }
                System.out.println("当前用户：" + profileSize);
            }
            System.out.println("上报的事件数为：" + eventSize + ",用户数为：" + (profileSize - 1) + "，kafka—event条数：" + eventKafka + "，kafka" +
                    "—profile条数：" + profileKafka);
        } catch (AnalysysException e) {
            e.printStackTrace();
            analysys.flush();
        }

    }
}
