package com.analysys.automation.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.compress.utils.Lists;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FastJsonUtils {

    @SuppressWarnings("unchecked")
    public static void compareJson(JSONObject json1, JSONObject json2, String key, List<String> result, List<String> excludedKeys) {
        Iterator<String> i = json1.keySet().iterator();
        while (i.hasNext()) {
            key = i.next();
            if(excludedKeys.contains(key)) {
                result.add("跳过: key" + key);
                continue;
            }
            if(!json2.containsKey(key)) {
                result.add("不一致: 预期结果包含结点" + key + ", 但是实际结果不包含结点" + key);
                continue;
            }
            compareJson(json1.get(key), json2.get(key), key, result, excludedKeys);
        }
    }

    public static void compareJson(Object json1, Object json2, String key, List<String> result, List<String> excludedKeys) {
        if (json1 instanceof JSONObject) {
            JSONObject newJson1 = new JSONObject(true);
            JSONObject newJson2 = new JSONObject(true);
            newJson1 = (JSONObject) json1;
            newJson2 = (JSONObject) json2;
            compareJson(newJson1, newJson2, key, result, excludedKeys);
        } else if (json1 instanceof JSONArray) {
            compareJson((JSONArray) json1, (JSONArray) json2, key, result);
        } else if (json1 instanceof String) {
            try {
                String json1ToStr = json1.toString();
                String json2ToStr = json2.toString();
                compareJson(json1ToStr, json2ToStr, key, result);
            } catch (Exception e) {
                System.out.println("转换发生异常 key:" + key);
                e.printStackTrace();
            }

        } else {
            if(json1 == null && json2 == null) {
                compareJson("null", "null", key, result);
            } else if((json1 == null && json2 != null) ) {
                compareJson("null", json2.toString(), key, result);
            } else if (json1 != null && json2 == null) {
                compareJson(json1.toString(), "null", key, result);
            } else {
                compareJson(json1.toString(), json2.toString(), key, result);
            }
        }
    }

    /**
     * @param str1
     * @param str2
     * @param key
     * @return
     */
    public static void compareJson(String str1, String str2, String key, List<String> result) {
        str1 = str1.replaceAll("\r\n", "").replaceAll("\r", "").replaceAll("\n", "");
        str2 = str2.replaceAll("\r\n", "").replaceAll("\r", "").replaceAll("\n", "");
        if (!str1.equals(str2)) {
            result.add("不一致：key:" + key + ",json1:" + str1 + ",json2:" + str2);
        } else {
            result.add("一致：key:" + key + ",json1:" + str1 + ",json2:" + str2);
        }
    }

    /**
     * @param json1
     * @param json2
     * @param key
     */
    public static void compareJson(JSONArray json1, JSONArray json2, String key, List<String> result) {
        if (json1 != null && json2 != null) {
            Iterator i1 = json1.iterator();
            Iterator i2 = json2.iterator();

            List<Object> jsonlist1 = Lists.newArrayList(i1);
            List<Object> jsonlist2 = Lists.newArrayList(i2);

            if (jsonlist1.size() != jsonlist2.size()) {
                result.add("不一致：两个" + key + "数组长度不相等");
                return;
            }

            jsonlist1.forEach(o -> System.out.println(o.toString()));

            //直接比较字符串就好了，不再重复拆分了
            compareJson(json1.toJSONString(), json2.toJSONString(), key, result);

        } else {
            if (json1 == null && json2 == null) {
                result.add("不一致：key:" + key + "  在json1和json2中均不存在");
            } else if (json1 == null) {
                result.add("不一致：key:" + key + "  在json1中不存在");
            } else if (json2 == null) {
                result.add("不一致：key:" + key + "  在json2中不存在");
            } else {
                result.add("不一致：key:" + key + "  未知原因");
            }
        }
    }


    public static void main(String[] args) {
        String st1 = "{\"username\":\"tom\",\"age\":18,\"address\":[{\"province\":\"上海市\"},{\"city\":\"上海市\"},{\"disrtict\":\"静安区\"}],\"value\": [1092, 769, 728, 4847, 2675, 5468, 0]}";
        String st2 = "{\"username\":\"tom\",\"age\":18,\"address\":[{\"province\":\"上海市\"},{\"disrtict\":\"静安区\"},{\"city\":\"上海市1\"}],\"value\": [1092, 769, 728, 4847, 2675, 5468, 0]}";
        System.out.println(st1);
        System.out.println(st2);
        JSONObject jsonObject1 = new JSONObject(true);
        JSONObject jsonObject2 = new JSONObject(true);
        jsonObject1 = JSONObject.parseObject(st1);
        jsonObject2 = JSONObject.parseObject(st2);

        System.out.println("按key值排序后:");
        System.out.println(JSON.toJSONString(jsonObject1));
        System.out.println(JSON.toJSONString(jsonObject2));
        List<String> result1 = new ArrayList<>();

        List<String> excludedKeys = new ArrayList<>();
        excludedKeys.add("requestId");
        excludedKeys.add("uniqueId");
        excludedKeys.add("times");
        excludedKeys.add("queryIds");

        compareJson(jsonObject1, jsonObject2, null, result1, excludedKeys);
        result1.forEach(str -> System.out.println(str));

        System.out.println("##################################################################");
        String st3 = "{\"code\":0,\"msg\":\"操作成功\",\"times\":\"1539754907810\",\"datas\":{\"result\":{\"seriesList\":[\"2018/06/01 00:00:00\",\"2018/06/02 00:00:00\",\"2018/06/03 00:00:00\",\"2018/06/04 00:00:00\",\"2018/06/05 00:00:00\",\"2018/06/06 00:00:00\",\"2018/06/07 00:00:00\",\"2018/06/08 00:00:00\",\"2018/06/09 00:00:00\",\"2018/06/10 00:00:00\",\"2018/06/11 00:00:00\",\"2018/06/12 00:00:00\",\"2018/06/13 00:00:00\",\"2018/06/14 00:00:00\",\"2018/06/15 00:00:00\",\"2018/06/16 00:00:00\",\"2018/06/17 00:00:00\",\"2018/06/18 00:00:00\",\"2018/06/19 00:00:00\",\"2018/06/20 00:00:00\",\"2018/06/21 00:00:00\",\"2018/06/22 00:00:00\",\"2018/06/23 00:00:00\",\"2018/06/24 00:00:00\",\"2018/06/25 00:00:00\",\"2018/06/26 00:00:00\",\"2018/06/27 00:00:00\",\"2018/06/28 00:00:00\",\"2018/06/29 00:00:00\",\"2018/06/30 00:00:00\",\"2018/07/01 00:00:00\",\"2018/07/02 00:00:00\",\"2018/07/03 00:00:00\",\"2018/07/04 00:00:00\",\"2018/07/05 00:00:00\",\"2018/07/06 00:00:00\",\"2018/07/07 00:00:00\",\"2018/07/08 00:00:00\",\"2018/07/09 00:00:00\",\"2018/07/10 00:00:00\",\"2018/07/11 00:00:00\",\"2018/07/12 00:00:00\",\"2018/07/13 00:00:00\",\"2018/07/14 00:00:00\",\"2018/07/15 00:00:00\",\"2018/07/16 00:00:00\",\"2018/07/17 00:00:00\",\"2018/07/18 00:00:00\",\"2018/07/19 00:00:00\"],\"$ALL\":{\"$Anything.unique-0\":[{\"value\":[2,2,2,3,1,1320,1530,1310,775,861,1510,1507,1636,1655,1470,630,605,634,1400,1499,1451,1321,691,625,1496,1333,1374,1033,969,439,350,1095,953,10,0,566,314,312,1087,1440,239,10,3,0,0,17,10,19,10],\"total\":27676,\"legged\":[],\"leggedFormat\":[]}]}},\"requestId\":\"201810170260df459a50c0db8dc185def8f7ca59\"},\"uniqueId\":\"502114025866264576\"}";
        String st4 = "{\"code\":1,\"msg\":\"操作成功\",\"times\":\"1539754907810\",\"datas\":{\"result\":{\"seriesList\":[\"2018/06/01 00:00:00\",\"2018/06/02 00:00:00\",\"2018/06/03 00:00:00\",\"2018/06/04 00:00:00\",\"2018/06/05 00:00:00\",\"2018/06/06 00:00:00\",\"2018/06/07 00:00:00\",\"2018/06/08 00:00:00\",\"2018/06/09 00:00:00\",\"2018/06/10 00:00:00\",\"2018/06/11 00:00:00\",\"2018/06/12 00:00:00\",\"2018/06/13 00:00:00\",\"2018/06/14 00:00:00\",\"2018/06/15 00:00:00\",\"2018/06/16 00:00:00\",\"2018/06/17 00:00:00\",\"2018/06/18 00:00:00\",\"2018/06/19 00:00:00\",\"2018/06/20 00:00:00\",\"2018/06/21 00:00:00\",\"2018/06/22 00:00:00\",\"2018/06/23 00:00:00\",\"2018/06/24 00:00:00\",\"2018/06/25 00:00:00\",\"2018/06/26 00:00:00\",\"2018/06/27 00:00:00\",\"2018/06/28 00:00:00\",\"2018/06/29 00:00:00\",\"2018/06/30 00:00:00\",\"2018/07/01 00:00:00\",\"2018/07/02 00:00:00\",\"2018/07/03 00:00:00\",\"2018/07/04 00:00:00\",\"2018/07/05 00:00:00\",\"2018/07/06 00:00:00\",\"2018/07/07 00:00:00\",\"2018/07/08 00:00:00\",\"2018/07/09 00:00:00\",\"2018/07/10 00:00:00\",\"2018/07/11 00:00:00\",\"2018/07/12 00:00:00\",\"2018/07/13 00:00:00\",\"2018/07/14 00:00:00\",\"2018/07/15 00:00:00\",\"2018/07/16 00:00:00\",\"2018/07/17 00:00:00\",\"2018/07/18 00:00:00\",\"2018/07/19 00:00:00\"],\"$ALL\":{\"$Anything.unique-0\":[{\"value\":[2,2,2,3,1,1320,1530,1310,775,861,1510,1507,1636,1655,1470,630,605,634,1400,1499,1451,1321,691,625,1496,1333,1374,1033,969,439,350,1095,953,10,0,566,314,312,1087,1440,239,10,3,0,0,17,10,19,11],\"total\":27676,\"legged\":[],\"leggedFormat\":[]}]}},\"requestId\":\"201810170260df459a50c0db8dc185def8f7ca59\"},\"uniqueId\":\"502114025866264576\"}";

        JSONObject jsonObject3 = new JSONObject(true);
        JSONObject jsonObject4 = new JSONObject(true);
        jsonObject3 = JSONObject.parseObject(st3);
        jsonObject4 = JSONObject.parseObject(st4);

        System.out.println("按key值排序后:");
        System.out.println(JSON.toJSONString(jsonObject3));
        System.out.println(JSON.toJSONString(jsonObject4));
        List<String> result = new ArrayList<>();
        compareJson(jsonObject3, jsonObject4, null, result, excludedKeys);
        result.forEach(str -> System.out.println(str));


//        st3 = "{\"code\":0,\"msg\":\"操作成功\",\"times\":null,\"datas\":{\"count\":1000,\"list\":[[\"唯一ID\",\"用户ID\",\"邮箱\",\"最后一次使用平台\",\"注册时间\",\"首次访问时间\",\"最近一次访问时间\",\"累计访问天数\",\"$os\",\"$original_id\",\"numberjia\",\"unset\",\"numberjian\",\"number\",\"boo\",\"array\",\"date1\"],[\"-1029061094582023690\",\"142ccb77b7402a87ec0df38ccce38dfdc12990\",\"217846932@analysys.demo.com.cn\",\"Andriod\",\"2018-05-11 22:47:58\",\"2018-05-15 05:58:59\",\"2018-06-20 21:34:32\",97.000,\"Android\",null,null,null,null,null,\"未知\",null,null],[\"-1031700985558523590\",\"15442c6457fd5c1ababd1bab4e4d4ec5212198\",\"78085140@analysys.demo.com.cn\",\"Andriod\",\"2018-05-07 03:28:51\",\"2018-05-07 03:28:51\",\"2018-06-20 23:58:35\",56.000,\"Android\",null,null,null,null,null,\"未知\",null,null],[\"-1047737094351778890\",\"18c8677f8b69f80c4292bb02164bef21212d33\",\"257676235@analysys.demo.com.cn\",\"Andriod\",\"2018-05-19 10:06:37\",\"2018-05-19 10:06:37\",\"2018-06-13 08:59:22\",10.000,\"Android\",null,null,null,null,null,\"未知\",null,null],[\"-1064242457595456190\",\"1de44b1be5bb1021f0e2054cd9a69965112953\",\"204861782@analysys.demo.com.cn\",\"Andriod\",\"2018-05-17 17:27:18\",\"2018-05-17 17:27:18\",\"2018-06-11 19:39:27\",7.000,\"Android\",null,null,null,null,null,\"未知\",null,null],[\"-1065984130325900990\",\"1afd5276f20bb2ab0805869a8b9e91ce1123b6\",\"751077512@analysys.demo.com.cn\",\"Andriod\",\"2018-05-08 09:45:54\",\"2018-05-08 09:45:54\",\"2018-06-20 16:57:05\",59.000,\"Android\",null,null,null,null,null,\"未知\",null,null],[\"-1100897902168576690\",\"1b6d53ecb9d46b4ad1a2176a149e6f0db12fd9\",\"515252221@analysys.demo.com.cn\",\"Andriod\",\"2018-05-10 10:45:38\",\"2018-05-10 10:45:38\",\"2018-06-20 11:49:58\",54.000,\"Android\",null,null,null,null,null,\"未知\",null,null],[\"-1111939703577793890\",\"19d98c8d11df4e4303d823f5a4fbc206a1237d\",\"282841878@analysys.demo.com.cn\",\"Andriod\",\"2018-05-26 06:13:33\",\"2018-05-29 21:21:41\",\"2018-06-20 21:57:42\",96.000,\"Android\",null,null,null,null,null,\"未知\",null,null],[\"-1113853939142647990\",\"1ba8cf472de756d2c117077b2d3ddde49128ca\",\"741252033@analysys.demo.com.cn\",\"Andriod\",\"2018-05-20 20:34:58\",\"2018-05-20 20:34:58\",\"2018-06-20 09:48:56\",0.000,\"Android\",null,null,null,null,null,\"未知\",null,null],[\"-1149713280669914490\",\"116109b0880d189bf79503b1bf296fad412ff2\",\"674662057@analysys.demo.com.cn\",\"Andriod\",\"2018-05-14 04:44:46\",\"2018-05-08 14:20:40\",\"2018-06-20 06:27:24\",75.000,\"Android\",null,null,null,null,null,\"未知\",null,null],[\"-1190190789095995790\",\"185759d255e801d613011c91c02f8b3b8129cc\",\"101372461@analysys.demo.com.cn\",\"Andriod\",\"2018-05-07 04:51:56\",\"2018-05-07 04:51:56\",\"2018-06-20 18:17:43\",54.000,\"Android\",null,null,null,null,null,\"未知\",null,null],[\"-1207438798562021190\",\"1d80aa586304ab6c52a6b00151605815f125e6\",\"814193649@analysys.demo.com.cn\",\"Andriod\",\"2018-06-11 13:21:30\",\"2018-05-25 11:38:26\",\"2018-06-19 08:43:34\",92.000,\"Android\",null,null,null,null,null,\"未知\",null,null],[\"-1212669169592562990\",\"1f513dbb5a878044c945a99979794366012cfe\",\"59243199@analysys.demo.com.cn\",\"Andriod\",\"2018-05-09 01:31:26\",\"2018-05-21 03:46:37\",\"2018-06-02 22:45:51\",13.000,\"Android\",null,null,null,null,null,\"未知\",null,null],[\"-121795061777007090\",\"1c40c9d750a81b008cacb556663c02f7812493\",\"394823169@analysys.demo.com.cn\",\"Andriod\",\"2018-05-30 06:17:34\",\"2018-05-24 12:32:56\",\"2018-06-18 11:20:37\",6.000,\"Android\",null,null,null,null,null,\"未知\",null,null],[\"-1243570042719202590\",\"13a11a7a90a6672b84ad7f4593a4c4dd61232b\",\"679661776@analysys.demo.com.cn\",\"Andriod\",\"2018-05-20 05:01:12\",\"2018-05-15 16:42:39\",\"2018-06-20 16:30:43\",7.000,\"Android\",null,null,null,null,null,\"未知\",null,null],[\"-1258964548088801790\",\"19a4c4116d856fe5e59525039b42327c112413\",\"905932658@analysys.demo.com.cn\",\"Andriod\",\"2018-05-10 20:52:31\",\"2018-05-10 20:52:31\",\"2018-06-20 22:26:12\",76.000,\"Android\",null,null,null,null,null,\"未知\",null,null],[\"-1260421029068436990\",\"1576542ea0e6729e204ecfeec5360833a14bd8\",\"409802091@analysys.demo.com.cn\",\"Andriod\",\"2018-05-07 02:52:13\",\"2018-05-07 02:52:13\",\"2018-06-14 09:51:31\",4.000,\"Android\",null,null,null,null,null,\"未知\",null,null],[\"-1265321129166160390\",\"1058c01fadc314722aee64fc6636742a312d08\",\"553292192@analysys.demo.com.cn\",\"Andriod\",\"2018-05-07 13:16:58\",\"2018-05-07 13:16:58\",\"2018-06-07 00:44:42\",3.000,\"Android\",null,null,null,null,null,\"未知\",null,null],[\"-1284254708468207490\",\"1a538ac645096e456c159048d26470b1d121fd\",\"196216215@analysys.demo.com.cn\",\"Andriod\",\"2018-05-10 12:27:09\",\"2018-05-15 00:55:43\",\"2018-06-20 11:04:37\",63.000,\"Android\",null,null,null,null,null,\"未知\",null,null],[\"-1284988448585206890\",\"1950b01d01d01edef640efcad1c8dad1f1216f\",\"829623662@analysys.demo.com.cn\",\"Andriod\",\"2018-05-07 13:13:14\",\"2018-05-07 13:13:14\",\"2018-06-20 19:46:26\",52.000,\"Android\",null,null,null,null,null,\"未知\",null,null],[\"-1298114976519723790\",\"146512bbb12b3c8f8c30105383841fb42120d3\",\"62838533@analysys.demo.com.cn\",\"Andriod\",\"2018-05-19 02:55:09\",\"2018-05-19 02:55:09\",\"2018-06-13 00:01:14\",26.000,\"Android\",null,null,null,null,null,\"未知\",null,null]]},\"uniqueId\":\"514756011848892416\"}";
//        st4 = "{\"code\":0,\"msg\":\"操作成功\",\"times\":null,\"datas\":null,\"uniqueId\":\"528618147054354432\"}";
//        jsonObject3 = new JSONObject(true);
//        jsonObject4 = new JSONObject(true);
//        jsonObject3 = JSONObject.parseObject(st3);
//        jsonObject4 = JSONObject.parseObject(st4);
//
//        System.out.println("按key值排序后:");
//        System.out.println(JSON.toJSONString(jsonObject3));
//        System.out.println(JSON.toJSONString(jsonObject4));
//        result = new ArrayList<>();
//        compareJson(jsonObject3, jsonObject4, null, result, excludedKeys);
//        result.forEach(str -> System.out.println(str));

        byte[]  buff = st3.getBytes();
        int i = buff.length;
        System.out.println(i);
    }

}
