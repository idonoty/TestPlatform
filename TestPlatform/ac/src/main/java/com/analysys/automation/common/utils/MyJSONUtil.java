package com.analysys.automation.common.utils;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Slf4j
public class MyJSONUtil {
    private static <T> T getValueOfTypeTFromJSONObject(Class<T> clazz, String key, JSONObject jo) throws Exception {
        if (clazz.equals(Integer.class)) {
            return (T) jo.getInteger(key);
        } else if (clazz.equals(Float.class)) {
            return (T) jo.getFloat(key);
        } else if (clazz.equals(Double.class)) {
            return (T) jo.getDouble(key);
        } else if (clazz.equals(String.class)) {
            return (T) jo.getString(key);
        } else if (clazz.equals(BigDecimal.class)) {
            return (T) jo.getBigDecimal(key);
        } else if (clazz.equals(Boolean.class)) {
            return (T) jo.getBoolean(key);
        } else if (clazz.equals(Date.class)) {
            return (T) jo.getDate(key);
        } else if (clazz.equals(Long.class)) {
            return (T) jo.getLong(key);
        } else if (clazz.equals(JSONArray.class)) {
            return (T) jo.getJSONArray(key);
        } else if (clazz.equals(JSONObject.class)) {
            return (T) jo.getJSONObject(key);
        } else if (clazz.equals(Timestamp.class)) {
            return (T) jo.getTimestamp(key);
        } else {
            throw new Exception("cannot get any object from fastjson.JSONObject of Type:" + clazz.getSimpleName());
        }
    }

    /**
     * @param json1
     * @param json2
     * @return
     * @function 比较两个JSONObject的key-value对的个数,内容是否一致---忽略顺序
     */
    public static boolean compareTwoJSONObject(JSONObject json1, JSONObject json2, String parentKeys) {
        if (json1 == null && json2 == null) {
            return true;
        } else if (json1 == null && json2 != null) {
            return false;
        } else if (json1 != null && json2 == null) {
            return false;
        } else if (json1.size() != json2.size()) {
            return false;
        }
        for (String key : json1.keySet()) {
            System.out.println("开始比较的键值为: " + parentKeys +  "." + key);
            if (!json2.containsKey(key)) {
                System.err.println("错误键值为: " + parentKeys +  "." + key);
                return false;
            } else if (json1.get(key) == null && json2.get(key) != null) {
                System.err.println("错误键值为: " + parentKeys +  "." + key);
                return false;
            } else if (json2.get(key) == null && json1.get(key) != null) {
                System.err.println("错误键值为: " + key);
                return false;
            }
            try {
                JSONObject sonJSON1 = json1.getJSONObject(key);
                JSONObject sonJSON2 = json2.getJSONObject(key);
                if (!compareTwoJSONObject(sonJSON1, sonJSON2,parentKeys +  "." + key)) {
                    return false;
                }
            } catch (Exception e) {
                try {
                    JSONArray sonArray1 = json1.getJSONArray(key);
                    JSONArray sonArray2 = json2.getJSONArray(key);
                    if (!compareTwoJSONArray(sonArray1, sonArray2, parentKeys +  "." + key)) {
                        return false;
                    }
                } catch (Exception e1) {
                    try {
                        Object o1 = json1.get(key);
                        Object o2 = json2.get(key);
                        if (!o1.equals(o2)) {
                            log.warn("键值为 {} 的预期结果为 {}, 实际结果为 {} ", key, o1.toString(), o2.toString());
                            return false;
                        }
                    } catch (Exception e2) {
                        log.warn("键值为 {} 的内容解析异常", key);
                        return false;
                    }
                }
            }
        }

        return true;
    }

    /**
     * @param array1
     * @param array2
     * @function 比较两个JSONArray元素个数, 内容是否一致---忽略顺序
     * @return 一致返回true, 不一致返回false
     */
    public static boolean compareTwoJSONArray(JSONArray array1, JSONArray array2, String parentKeys) {
        if (array1 == null && array2 == null) {
            return true;
        } else if (array1 == null && array2 != null) {
            return false;
        } else if (array1 != null && array2 == null) {
            return false;
        } else if (array1.size() != array2.size()) {
//            System.err.println(parentKeys + ", 一个数组长度为 " + array1.size() + ", 另一个数组的长度为  " + array2.size());
//            System.err.println(JSON.toJSONString(array1));
//            System.err.println(JSON.toJSONString(array2));
            return false;
        }
        for (int index = 0; index < array1.size(); index++) {
            //array1的第index个元素还是JSONArray,则遍历array2的所有元素,递归比较...
            try {
                JSONArray sonArray1 = array1.getJSONArray(index);
                boolean flag = false;
                for (int index2 = 0; index2 < array2.size(); index2++) {
                    JSONArray sonArray2 = array2.getJSONArray(index2);
                    if (compareTwoJSONArray(sonArray1, sonArray2, parentKeys + "[" + index + "]")) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    return false;
                }
            } catch (Exception e) {
                //array1的第index个元素是JSONObject,则遍历array2的所有元素,递归比较...
                try {
                    JSONObject sonJSON1 = array1.getJSONObject(index);
                    boolean flag = false;
                    for (int index2 = 0; index2 < array2.size(); index2++) {
                        JSONObject sonJSON2 = array2.getJSONObject(index2);
                        if (compareTwoJSONObject(sonJSON1, sonJSON2, parentKeys + "[" + index + "]")) {
                            flag = true;
                            break;
                        }
                    }
                    if (!flag) {
                        return false;
                    }
                } catch (Exception e1) {
                    //array1的第index个元素非JSONArray&&非JSONObject,则遍历array2的所有元素,递归比较...
                    try {
                        Object o1 = array1.get(index);
                        boolean flag = false;
                        for (int index2 = 0; index2 < array2.size(); index2++) {
                            Object o2 = array2.get(index2);
                            if ((o1 == null && o2 == null) || o1.equals(o2)) {
                                flag = true;
                                array2.remove(index2);
                                break;
                            }
                        }
                        if (!flag) {
                            return false;
                        }
                    } catch (Exception e2) {
                        return array1.equals(array2);
//                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static JSONObject removeJSONObjectExcludeKeys(JSONObject jsonObject, List<String> excludeKeyList) {
        Iterator<String> iterator = jsonObject.keySet().iterator();
        JSONObject tempObject = (JSONObject) jsonObject.clone();
        while (iterator.hasNext()) {
            String key = iterator.next();
            if (excludeKeyList.contains(key)) {
                tempObject.remove(key);
            } else if (jsonObject.get(key) instanceof JSONObject) {
                tempObject.put(key, removeJSONObjectExcludeKeys((JSONObject) jsonObject.get(key), excludeKeyList));
            } else if (jsonObject.get(key) instanceof JSONArray) {
                JSONArray sonArray = (JSONArray) jsonObject.get(key);
                tempObject.put(key, removeJSONArrayExcludeKeys(sonArray, excludeKeyList));
            }
        }

        return tempObject;
    }

    public static JSONArray removeJSONArrayExcludeKeys(JSONArray jsonArray, List<String> excludeKeyList) {
        for (int i = 0; i < jsonArray.size(); i++) {
            Object object = jsonArray.get(i);
            if (object instanceof JSONObject) {
                jsonArray.set(i, removeJSONObjectExcludeKeys((JSONObject) object, excludeKeyList));
            } else if (object instanceof JSONArray) {
                jsonArray.set(i, removeJSONArrayExcludeKeys((JSONArray) object, excludeKeyList));
            }
        }

        return jsonArray;
    }


    public static void main(String[] args) {
        String st1 = "{\"code\":0,\"msg\":\"操作成功\",\"times\":null,\"datas\":{\"result\":{\"$SIGNUP_USER\":{\"$Anything$Session.position_cohorts_new.uniqAvg-0\":[{\"value\":[\"-\",\"-\",\"-\"],\"total\":\"-\",\"legged\":[\"2.000\",\"(无值)\",\"(无值)\",\"1366\"],\"leggedFormat\":[\"2\",\"(无值)\",\"(无值)\",\"1366\"]},{\"value\":[\"-\",\"-\",\"-\"],\"total\":\"-\",\"legged\":[\"2.000\",\"(无值)\",\"(无值)\",\"1280\"],\"leggedFormat\":[\"2\",\"(无值)\",\"(无值)\",\"1280\"]},{\"value\":[\"-\",\"-\",\"-\"],\"total\":\"-\",\"legged\":[\"2.000\",\"(无值)\",\"(无值)\",\"1536\"],\"leggedFormat\":[\"2\",\"(无值)\",\"(无值)\",\"1536\"]},{\"value\":[0.0,0.0,0.1176],\"total\":0.1111,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1920\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1920\"]},{\"value\":[\"-\",\"-\",\"-\"],\"total\":\"-\",\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1600\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1600\"]},{\"value\":[\"-\",\"-\",\"-\"],\"total\":\"-\",\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1422\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1422\"]},{\"value\":[\"-\",\"-\",\"-\"],\"total\":\"-\",\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"375\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"375\"]},{\"value\":[\"-\",\"-\",\"-\"],\"total\":\"-\",\"legged\":[\"(无值)\",\"(无值)\",\"方舟社区版下载\",\"768\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"方舟社区版下载\",\"768\"]},{\"value\":[\"-\",\"-\",\"-\"],\"total\":\"-\",\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1778\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1778\"]},{\"value\":[\"-\",\"-\",\"-\"],\"total\":\"-\",\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1536\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1536\"]},{\"value\":[\"-\",\"-\",\"-\"],\"total\":\"-\",\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1360\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1360\"]},{\"value\":[\"-\",\"-\",\"-\"],\"total\":\"-\",\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"2560\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"2560\"]},{\"value\":[\"-\",\"-\",\"-\"],\"total\":\"-\",\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1477\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1477\"]},{\"value\":[\"-\",\"-\",\"-\"],\"total\":\"-\",\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1280\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1280\"]},{\"value\":[\"-\",\"-\",\"-\"],\"total\":\"-\",\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1680\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1680\"]},{\"value\":[\"-\",\"-\",\"-\"],\"total\":\"-\",\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1440\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1440\"]},{\"value\":[\"-\",\"-\",\"-\"],\"total\":\"-\",\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"2048\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"2048\"]},{\"value\":[\"-\",\"-\",\"-\"],\"total\":\"-\",\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"393\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"393\"]},{\"value\":[\"-\",\"-\",\"-\"],\"total\":\"-\",\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"360\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"360\"]},{\"value\":[\"-\",\"-\",\"-\"],\"total\":\"-\",\"legged\":[\"(无值)\",\"(无值)\",\"方舟社区版下载\",\"1536\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"方舟社区版下载\",\"1536\"]},{\"value\":[\"-\",\"-\",\"-\"],\"total\":\"-\",\"legged\":[\"(无值)\",\"(无值)\",\"方舟社区版下载\",\"1920\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"方舟社区版下载\",\"1920\"]},{\"value\":[\"-\",\"-\",\"-\"],\"total\":\"-\",\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1239\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1239\"]},{\"value\":[\"-\",\"-\",\"-\"],\"total\":\"-\",\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1366\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1366\"]},{\"value\":[\"-\",\"-\",\"-\"],\"total\":\"-\",\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1549\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1549\"]},{\"value\":[\"-\",\"-\",\"-\"],\"total\":\"-\",\"legged\":[\"(无值)\",\"(无值)\",\"官网首页头部banner\",\"1536\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"官网首页头部banner\",\"1536\"]},{\"value\":[\"-\",\"-\",\"-\"],\"total\":\"-\",\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1301\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1301\"]},{\"value\":[\"-\",\"-\",\"-\"],\"total\":\"-\",\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1157\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1157\"]},{\"value\":[\"-\",\"-\",\"-\"],\"total\":\"-\",\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"424\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"424\"]}],\"$Anything.sessionGeneral-1\":[{\"value\":[0,0,1],\"total\":1,\"legged\":[\"2.000\",\"(无值)\",\"(无值)\",\"1366\"],\"leggedFormat\":[\"2\",\"(无值)\",\"(无值)\",\"1366\"]},{\"value\":[0,0,1],\"total\":1,\"legged\":[\"2.000\",\"(无值)\",\"(无值)\",\"1280\"],\"leggedFormat\":[\"2\",\"(无值)\",\"(无值)\",\"1280\"]},{\"value\":[0,0,1],\"total\":1,\"legged\":[\"2.000\",\"(无值)\",\"(无值)\",\"1536\"],\"leggedFormat\":[\"2\",\"(无值)\",\"(无值)\",\"1536\"]},{\"value\":[0,1,23],\"total\":24,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1920\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1920\"]},{\"value\":[0,0,3],\"total\":3,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1600\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1600\"]},{\"value\":[0,0,1],\"total\":1,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1422\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1422\"]},{\"value\":[0,0,2],\"total\":2,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"375\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"375\"]},{\"value\":[0,0,1],\"total\":1,\"legged\":[\"(无值)\",\"(无值)\",\"方舟社区版下载\",\"768\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"方舟社区版下载\",\"768\"]},{\"value\":[0,0,1],\"total\":1,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1778\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1778\"]},{\"value\":[0,0,21],\"total\":21,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1536\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1536\"]},{\"value\":[0,0,1],\"total\":1,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1360\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1360\"]},{\"value\":[0,0,1],\"total\":1,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"2560\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"2560\"]},{\"value\":[0,0,1],\"total\":1,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1477\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1477\"]},{\"value\":[0,0,16],\"total\":16,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1280\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1280\"]},{\"value\":[0,0,3],\"total\":3,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1680\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1680\"]},{\"value\":[0,0,7],\"total\":7,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1440\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1440\"]},{\"value\":[0,0,2],\"total\":2,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"2048\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"2048\"]},{\"value\":[0,0,1],\"total\":1,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"393\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"393\"]},{\"value\":[0,0,4],\"total\":4,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"360\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"360\"]},{\"value\":[0,0,1],\"total\":1,\"legged\":[\"(无值)\",\"(无值)\",\"方舟社区版下载\",\"1536\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"方舟社区版下载\",\"1536\"]},{\"value\":[0,0,1],\"total\":1,\"legged\":[\"(无值)\",\"(无值)\",\"方舟社区版下载\",\"1920\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"方舟社区版下载\",\"1920\"]},{\"value\":[0,0,2],\"total\":2,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1239\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1239\"]},{\"value\":[0,1,9],\"total\":10,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1366\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1366\"]},{\"value\":[0,0,1],\"total\":1,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1549\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1549\"]},{\"value\":[0,0,1],\"total\":1,\"legged\":[\"(无值)\",\"(无值)\",\"官网首页头部banner\",\"1536\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"官网首页头部banner\",\"1536\"]},{\"value\":[0,0,1],\"total\":1,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1301\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1301\"]},{\"value\":[0,0,1],\"total\":1,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1157\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1157\"]},{\"value\":[0,0,1],\"total\":1,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"424\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"424\"]}],\"$Anything.sessionGeneral-2\":[{\"value\":[0,0,1],\"total\":1,\"legged\":[\"2.000\",\"(无值)\",\"(无值)\",\"1366\"],\"leggedFormat\":[\"2\",\"(无值)\",\"(无值)\",\"1366\"]},{\"value\":[0,0,1],\"total\":1,\"legged\":[\"2.000\",\"(无值)\",\"(无值)\",\"1280\"],\"leggedFormat\":[\"2\",\"(无值)\",\"(无值)\",\"1280\"]},{\"value\":[0,0,1],\"total\":1,\"legged\":[\"2.000\",\"(无值)\",\"(无值)\",\"1536\"],\"leggedFormat\":[\"2\",\"(无值)\",\"(无值)\",\"1536\"]},{\"value\":[0,1,23],\"total\":24,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1920\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1920\"]},{\"value\":[0,0,3],\"total\":3,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1600\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1600\"]},{\"value\":[0,0,1],\"total\":1,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1422\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1422\"]},{\"value\":[0,0,2],\"total\":2,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"375\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"375\"]},{\"value\":[0,0,1],\"total\":1,\"legged\":[\"(无值)\",\"(无值)\",\"方舟社区版下载\",\"768\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"方舟社区版下载\",\"768\"]},{\"value\":[0,0,1],\"total\":1,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1778\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1778\"]},{\"value\":[0,0,21],\"total\":21,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1536\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1536\"]},{\"value\":[0,0,1],\"total\":1,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1360\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1360\"]},{\"value\":[0,0,1],\"total\":1,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"2560\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"2560\"]},{\"value\":[0,0,1],\"total\":1,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1477\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1477\"]},{\"value\":[0,0,16],\"total\":16,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1280\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1280\"]},{\"value\":[0,0,3],\"total\":3,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1680\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1680\"]},{\"value\":[0,0,7],\"total\":7,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1440\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1440\"]},{\"value\":[0,0,2],\"total\":2,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"2048\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"2048\"]},{\"value\":[0,0,1],\"total\":1,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"393\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"393\"]},{\"value\":[0,0,4],\"total\":4,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"360\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"360\"]},{\"value\":[0,0,1],\"total\":1,\"legged\":[\"(无值)\",\"(无值)\",\"方舟社区版下载\",\"1536\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"方舟社区版下载\",\"1536\"]},{\"value\":[0,0,1],\"total\":1,\"legged\":[\"(无值)\",\"(无值)\",\"方舟社区版下载\",\"1920\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"方舟社区版下载\",\"1920\"]},{\"value\":[0,0,2],\"total\":2,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1239\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1239\"]},{\"value\":[0,1,9],\"total\":10,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1366\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1366\"]},{\"value\":[0,0,1],\"total\":1,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1549\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1549\"]},{\"value\":[0,0,1],\"total\":1,\"legged\":[\"(无值)\",\"(无值)\",\"官网首页头部banner\",\"1536\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"官网首页头部banner\",\"1536\"]},{\"value\":[0,0,1],\"total\":1,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1301\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1301\"]},{\"value\":[0,0,1],\"total\":1,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1157\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1157\"]},{\"value\":[0,0,1],\"total\":1,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"424\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"424\"]}]},\"seriesList\":[\"2019/09/11 00:00:00\",\"2019/09/12 00:00:00\",\"2019/09/13 00:00:00\"],\"reportUpdateTime\":1575267481340,\"excludeDate\":[\"2019/09/12 00:00:00\",\"2019/09/13 00:00:00\"]},\"reportUpdateTime\":1575267481340,\"requestId\":\"2019120297a416cc951245b98a00e64a33154bbe\"},\"exception\":null,\"uniqueId\":\"651078286964686848\"}";
        String st2 = "{\"code\":0,\"msg\":\"操作成功\",\"times\":null,\"datas\":{\"result\":{\"$SIGNUP_USER\":{\"$Anything$Session.position_cohorts_new.uniqAvg-0\":[{\"value\":[\"-\",\"-\",\"-\"],\"total\":\"-\",\"legged\":[\"2.000\",\"(无值)\",\"(无值)\",\"1280\"],\"leggedFormat\":[\"2\",\"(无值)\",\"(无值)\",\"1280\"]},{\"value\":[\"-\",\"-\",\"-\"],\"total\":\"-\",\"legged\":[\"2.000\",\"(无值)\",\"(无值)\",\"1536\"],\"leggedFormat\":[\"2\",\"(无值)\",\"(无值)\",\"1536\"]},{\"value\":[\"-\",\"-\",\"-\"],\"total\":\"-\",\"legged\":[\"2.000\",\"(无值)\",\"(无值)\",\"1366\"],\"leggedFormat\":[\"2\",\"(无值)\",\"(无值)\",\"1366\"]},{\"value\":[0.0,0.0,0.1176],\"total\":0.1111,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1920\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1920\"]},{\"value\":[\"-\",\"-\",\"-\"],\"total\":\"-\",\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1549\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1549\"]},{\"value\":[\"-\",\"-\",\"-\"],\"total\":\"-\",\"legged\":[\"(无值)\",\"(无值)\",\"方舟社区版下载\",\"1920\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"方舟社区版下载\",\"1920\"]},{\"value\":[\"-\",\"-\",\"-\"],\"total\":\"-\",\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1280\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1280\"]},{\"value\":[\"-\",\"-\",\"-\"],\"total\":\"-\",\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"2560\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"2560\"]},{\"value\":[\"-\",\"-\",\"-\"],\"total\":\"-\",\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"393\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"393\"]},{\"value\":[\"-\",\"-\",\"-\"],\"total\":\"-\",\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1477\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1477\"]},{\"value\":[\"-\",\"-\",\"-\"],\"total\":\"-\",\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"375\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"375\"]},{\"value\":[\"-\",\"-\",\"-\"],\"total\":\"-\",\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1600\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1600\"]},{\"value\":[\"-\",\"-\",\"-\"],\"total\":\"-\",\"legged\":[\"(无值)\",\"(无值)\",\"方舟社区版下载\",\"1536\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"方舟社区版下载\",\"1536\"]},{\"value\":[\"-\",\"-\",\"-\"],\"total\":\"-\",\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1536\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1536\"]},{\"value\":[\"-\",\"-\",\"-\"],\"total\":\"-\",\"legged\":[\"(无值)\",\"(无值)\",\"方舟社区版下载\",\"768\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"方舟社区版下载\",\"768\"]},{\"value\":[\"-\",\"-\",\"-\"],\"total\":\"-\",\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1778\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1778\"]},{\"value\":[\"-\",\"-\",\"-\"],\"total\":\"-\",\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1239\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1239\"]},{\"value\":[\"-\",\"-\",\"-\"],\"total\":\"-\",\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1680\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1680\"]},{\"value\":[\"-\",\"-\",\"-\"],\"total\":\"-\",\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1422\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1422\"]},{\"value\":[\"-\",\"-\",\"-\"],\"total\":\"-\",\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1366\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1366\"]},{\"value\":[\"-\",\"-\",\"-\"],\"total\":\"-\",\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1440\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1440\"]},{\"value\":[\"-\",\"-\",\"-\"],\"total\":\"-\",\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"2048\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"2048\"]},{\"value\":[\"-\",\"-\",\"-\"],\"total\":\"-\",\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"360\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"360\"]},{\"value\":[\"-\",\"-\",\"-\"],\"total\":\"-\",\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1360\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1360\"]},{\"value\":[\"-\",\"-\",\"-\"],\"total\":\"-\",\"legged\":[\"(无值)\",\"(无值)\",\"官网首页头部banner\",\"1536\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"官网首页头部banner\",\"1536\"]},{\"value\":[\"-\",\"-\",\"-\"],\"total\":\"-\",\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"424\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"424\"]},{\"value\":[\"-\",\"-\",\"-\"],\"total\":\"-\",\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1301\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1301\"]},{\"value\":[\"-\",\"-\",\"-\"],\"total\":\"-\",\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1157\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1157\"]}],\"$Anything.sessionGeneral-1\":[{\"value\":[0,0,1],\"total\":1,\"legged\":[\"2.000\",\"(无值)\",\"(无值)\",\"1280\"],\"leggedFormat\":[\"2\",\"(无值)\",\"(无值)\",\"1280\"]},{\"value\":[0,0,1],\"total\":1,\"legged\":[\"2.000\",\"(无值)\",\"(无值)\",\"1536\"],\"leggedFormat\":[\"2\",\"(无值)\",\"(无值)\",\"1536\"]},{\"value\":[0,0,1],\"total\":1,\"legged\":[\"2.000\",\"(无值)\",\"(无值)\",\"1366\"],\"leggedFormat\":[\"2\",\"(无值)\",\"(无值)\",\"1366\"]},{\"value\":[0,1,23],\"total\":24,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1920\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1920\"]},{\"value\":[0,0,1],\"total\":1,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1549\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1549\"]},{\"value\":[0,0,1],\"total\":1,\"legged\":[\"(无值)\",\"(无值)\",\"方舟社区版下载\",\"1920\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"方舟社区版下载\",\"1920\"]},{\"value\":[0,0,16],\"total\":16,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1280\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1280\"]},{\"value\":[0,0,1],\"total\":1,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"2560\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"2560\"]},{\"value\":[0,0,1],\"total\":1,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"393\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"393\"]},{\"value\":[0,0,1],\"total\":1,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1477\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1477\"]},{\"value\":[0,0,2],\"total\":2,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"375\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"375\"]},{\"value\":[0,0,3],\"total\":3,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1600\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1600\"]},{\"value\":[0,0,1],\"total\":1,\"legged\":[\"(无值)\",\"(无值)\",\"方舟社区版下载\",\"1536\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"方舟社区版下载\",\"1536\"]},{\"value\":[0,0,21],\"total\":21,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1536\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1536\"]},{\"value\":[0,0,1],\"total\":1,\"legged\":[\"(无值)\",\"(无值)\",\"方舟社区版下载\",\"768\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"方舟社区版下载\",\"768\"]},{\"value\":[0,0,1],\"total\":1,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1778\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1778\"]},{\"value\":[0,0,2],\"total\":2,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1239\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1239\"]},{\"value\":[0,0,3],\"total\":3,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1680\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1680\"]},{\"value\":[0,0,1],\"total\":1,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1422\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1422\"]},{\"value\":[0,1,9],\"total\":10,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1366\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1366\"]},{\"value\":[0,0,7],\"total\":7,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1440\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1440\"]},{\"value\":[0,0,2],\"total\":2,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"2048\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"2048\"]},{\"value\":[0,0,4],\"total\":4,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"360\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"360\"]},{\"value\":[0,0,1],\"total\":1,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1360\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1360\"]},{\"value\":[0,0,1],\"total\":1,\"legged\":[\"(无值)\",\"(无值)\",\"官网首页头部banner\",\"1536\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"官网首页头部banner\",\"1536\"]},{\"value\":[0,0,1],\"total\":1,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"424\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"424\"]},{\"value\":[0,0,1],\"total\":1,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1301\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1301\"]},{\"value\":[0,0,1],\"total\":1,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1157\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1157\"]}],\"$Anything.sessionGeneral-2\":[{\"value\":[0,0,1],\"total\":1,\"legged\":[\"2.000\",\"(无值)\",\"(无值)\",\"1280\"],\"leggedFormat\":[\"2\",\"(无值)\",\"(无值)\",\"1280\"]},{\"value\":[0,0,1],\"total\":1,\"legged\":[\"2.000\",\"(无值)\",\"(无值)\",\"1536\"],\"leggedFormat\":[\"2\",\"(无值)\",\"(无值)\",\"1536\"]},{\"value\":[0,0,1],\"total\":1,\"legged\":[\"2.000\",\"(无值)\",\"(无值)\",\"1366\"],\"leggedFormat\":[\"2\",\"(无值)\",\"(无值)\",\"1366\"]},{\"value\":[0,1,23],\"total\":24,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1920\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1920\"]},{\"value\":[0,0,1],\"total\":1,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1549\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1549\"]},{\"value\":[0,0,1],\"total\":1,\"legged\":[\"(无值)\",\"(无值)\",\"方舟社区版下载\",\"1920\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"方舟社区版下载\",\"1920\"]},{\"value\":[0,0,16],\"total\":16,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1280\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1280\"]},{\"value\":[0,0,1],\"total\":1,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"2560\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"2560\"]},{\"value\":[0,0,1],\"total\":1,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"393\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"393\"]},{\"value\":[0,0,1],\"total\":1,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1477\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1477\"]},{\"value\":[0,0,2],\"total\":2,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"375\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"375\"]},{\"value\":[0,0,3],\"total\":3,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1600\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1600\"]},{\"value\":[0,0,1],\"total\":1,\"legged\":[\"(无值)\",\"(无值)\",\"方舟社区版下载\",\"1536\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"方舟社区版下载\",\"1536\"]},{\"value\":[0,0,21],\"total\":21,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1536\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1536\"]},{\"value\":[0,0,1],\"total\":1,\"legged\":[\"(无值)\",\"(无值)\",\"方舟社区版下载\",\"768\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"方舟社区版下载\",\"768\"]},{\"value\":[0,0,1],\"total\":1,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1778\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1778\"]},{\"value\":[0,0,2],\"total\":2,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1239\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1239\"]},{\"value\":[0,0,3],\"total\":3,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1680\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1680\"]},{\"value\":[0,0,1],\"total\":1,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1422\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1422\"]},{\"value\":[0,1,9],\"total\":10,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1366\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1366\"]},{\"value\":[0,0,7],\"total\":7,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1440\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1440\"]},{\"value\":[0,0,2],\"total\":2,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"2048\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"2048\"]},{\"value\":[0,0,4],\"total\":4,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"360\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"360\"]},{\"value\":[0,0,1],\"total\":1,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1360\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1360\"]},{\"value\":[0,0,1],\"total\":1,\"legged\":[\"(无值)\",\"(无值)\",\"官网首页头部banner\",\"1536\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"官网首页头部banner\",\"1536\"]},{\"value\":[0,0,1],\"total\":1,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"424\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"424\"]},{\"value\":[0,0,1],\"total\":1,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1301\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1301\"]},{\"value\":[0,0,1],\"total\":1,\"legged\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1157\"],\"leggedFormat\":[\"(无值)\",\"(无值)\",\"(无值)\",\"1157\"]}]},\"seriesList\":[\"2019/09/11 00:00:00\",\"2019/09/12 00:00:00\",\"2019/09/13 00:00:00\"],\"reportUpdateTime\":1577763775523,\"excludeDate\":[]},\"reportUpdateTime\":1577763775523,\"requestId\":\"201912313d13713eb862fdb46a74c49dd8061ff9\"},\"exception\":null,\"uniqueId\":\"661536082529091584\"}";
//        System.out.println(st1);
//        System.out.println(st2);
        JSONObject jsonObject1 = new JSONObject(true);
        JSONObject jsonObject2 = new JSONObject(true);
        jsonObject1 = JSONObject.parseObject(st1);
        jsonObject2 = JSONObject.parseObject(st2);

        List<String> excludedKey = new ArrayList<>();
        excludedKey.add("uniqueId");
        excludedKey.add("times");
        excludedKey.add("requestId");
        excludedKey.add("curReqId");
        excludedKey.add("reportUpdateTime");
        excludedKey.add("queryIds");
        excludedKey.add("queryId");
        excludedKey.add("exception");
        excludedKey.add("drillingKey");
        excludedKey.add("content");
        excludedKey.add("leave");
        excludedKey.add("excludeDate");

        System.out.println(removeJSONObjectExcludeKeys(jsonObject1, excludedKey));
        System.out.println(removeJSONObjectExcludeKeys(jsonObject2, excludedKey));
        System.out.println(compareTwoJSONObject(removeJSONObjectExcludeKeys(jsonObject1, excludedKey), removeJSONObjectExcludeKeys(jsonObject2, excludedKey), "$"));

//        System.out.println("##################################################################");
//        String st3 = "{\"code\":0,\"msg\":\"操作成功\",\"times\":\"1539754907810\",\"datas\":{\"result\":{\"seriesList\":[\"2018/06/01 00:00:00\",\"2018/06/02 00:00:00\",\"2018/06/03 00:00:00\",\"2018/06/04 00:00:00\",\"2018/06/05 00:00:00\",\"2018/06/06 00:00:00\",\"2018/06/07 00:00:00\",\"2018/06/08 00:00:00\",\"2018/06/09 00:00:00\",\"2018/06/10 00:00:00\",\"2018/06/11 00:00:00\",\"2018/06/12 00:00:00\",\"2018/06/13 00:00:00\",\"2018/06/14 00:00:00\",\"2018/06/15 00:00:00\",\"2018/06/16 00:00:00\",\"2018/06/17 00:00:00\",\"2018/06/18 00:00:00\",\"2018/06/19 00:00:00\",\"2018/06/20 00:00:00\",\"2018/06/21 00:00:00\",\"2018/06/22 00:00:00\",\"2018/06/23 00:00:00\",\"2018/06/24 00:00:00\",\"2018/06/25 00:00:00\",\"2018/06/26 00:00:00\",\"2018/06/27 00:00:00\",\"2018/06/28 00:00:00\",\"2018/06/29 00:00:00\",\"2018/06/30 00:00:00\",\"2018/07/01 00:00:00\",\"2018/07/02 00:00:00\",\"2018/07/03 00:00:00\",\"2018/07/04 00:00:00\",\"2018/07/05 00:00:00\",\"2018/07/06 00:00:00\",\"2018/07/07 00:00:00\",\"2018/07/08 00:00:00\",\"2018/07/09 00:00:00\",\"2018/07/10 00:00:00\",\"2018/07/11 00:00:00\",\"2018/07/12 00:00:00\",\"2018/07/13 00:00:00\",\"2018/07/14 00:00:00\",\"2018/07/15 00:00:00\",\"2018/07/16 00:00:00\",\"2018/07/17 00:00:00\",\"2018/07/18 00:00:00\",\"2018/07/19 00:00:00\"],\"$ALL\":{\"$Anything.unique-0\":[{\"value\":[2,2,10,19,10],\"total\":27676,\"legged\":[],\"leggedFormat\":[]}]}},\"requestId\":\"201810170260df459a50c0db8dc185def8f7ca59\"},\"uniqueId\":\"502114025866264576\"}";
//        String st4 = "{\"code\":0,\"msg\":\"操作成功\",\"times\":\"1539754907810\",\"datas\":{\"result\":{\"seriesList\":[\"2018/06/01 00:00:00\",\"2018/06/02 00:00:00\",\"2018/06/03 00:00:00\",\"2018/06/04 00:00:00\",\"2018/06/05 00:00:00\",\"2018/06/06 00:00:00\",\"2018/06/07 00:00:00\",\"2018/06/08 00:00:00\",\"2018/06/09 00:00:00\",\"2018/06/10 00:00:00\",\"2018/06/11 00:00:00\",\"2018/06/12 00:00:00\",\"2018/06/13 00:00:00\",\"2018/06/14 00:00:00\",\"2018/06/15 00:00:00\",\"2018/06/16 00:00:00\",\"2018/06/17 00:00:00\",\"2018/06/18 00:00:00\",\"2018/06/19 00:00:00\",\"2018/06/20 00:00:00\",\"2018/06/21 00:00:00\",\"2018/06/22 00:00:00\",\"2018/06/23 00:00:00\",\"2018/06/24 00:00:00\",\"2018/06/25 00:00:00\",\"2018/06/26 00:00:00\",\"2018/06/27 00:00:00\",\"2018/06/28 00:00:00\",\"2018/06/29 00:00:00\",\"2018/06/30 00:00:00\",\"2018/07/01 00:00:00\",\"2018/07/02 00:00:00\",\"2018/07/03 00:00:00\",\"2018/07/04 00:00:00\",\"2018/07/05 00:00:00\",\"2018/07/06 00:00:00\",\"2018/07/07 00:00:00\",\"2018/07/08 00:00:00\",\"2018/07/09 00:00:00\",\"2018/07/10 00:00:00\",\"2018/07/11 00:00:00\",\"2018/07/12 00:00:00\",\"2018/07/13 00:00:00\",\"2018/07/14 00:00:00\",\"2018/07/15 00:00:00\",\"2018/07/16 00:00:00\",\"2018/07/17 00:00:00\",\"2018/07/18 00:00:00\",\"2018/07/19 00:00:00\"],\"$ALL\":{\"$Anything.unique-0\":[{\"value\":[3,2,10,19,11],\"total\":27676,\"legged\":[],\"leggedFormat\":[]}]}},\"requestId\":\"201810170260df459a50c0db8dc185def8f7ca59\"},\"uniqueId\":\"502114025866264576\"}";
//        System.out.println(st3);
//        System.out.println(st4);
//        JSONObject jsonObject3 = new JSONObject(true);
//        JSONObject jsonObject4 = new JSONObject(true);
//        jsonObject3 = JSONObject.parseObject(st3);
//        jsonObject4 = JSONObject.parseObject(st4);
//
//        System.out.println(compareTwoJSONObject(jsonObject3, jsonObject4));
    }
}