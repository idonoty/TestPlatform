package com.analysys.automation.common.utils;

import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReflectUtils {
    public static Map<String, Object> Object2Map(Object obj) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();
        Class clazz = obj.getClass();

        Field[] fileds = clazz.getDeclaredFields();
        for(Field field : fileds) {
            String fieldName = camel2Underline(field.getName()).toLowerCase();
            String key = fieldName;

            //分群名称不需要放入map中
            if(key.equals("group_name")) {
                continue;
            }

            if(!fieldName.equals("xwho") && !fieldName.equals("set")
                    && !fieldName.equals("goods") && !fieldName.equals("usertype")
                    && !fieldName.equals("interest"))
                key = "$" + fieldName;
            field.setAccessible(true);
            Object value = field.get(obj);
            if(value != null && StringUtils.isNotBlank(value.toString())) {
                if(value.getClass().getName().equals("java.util.Date")) {
                    value = DateUtils.format((Date) value, "yyyy-MM-dd HH:mm:ss");
                }
                map.put(key, value);
            }

        }
        return map;
    }

    private static String camel2Underline(String line) {
        if (line == null || "".equals(line)) {
            return "";
        }
        line = String.valueOf(line.charAt(0)).toUpperCase()
                .concat(line.substring(1));
        StringBuffer sb = new StringBuffer();
        Pattern pattern = Pattern.compile("[A-Z]([a-z\\d]+)?");
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            String word = matcher.group();
            sb.append(word.toUpperCase());
            sb.append(matcher.end() == line.length() ? "" : "_");
        }
        return sb.toString();
    }

    public static void main(String[] args) throws IllegalAccessException {
//        MockUserEntity mockUserEntity = new MockUserEntity();
//        mockUserEntity.setXwho("0004128a98bf4953b5fba79f554d78a8");
//        mockUserEntity.setLogin(false);
//        Map<String, Object> map = Object2Map(mockUserEntity);
//        map.forEach((k, v) -> System.out.println(k + " - " + v));
//
//        System.out.println("*********************************************");
//
//        MockEventPropertyEntity mockEventPropertyEntity = new MockEventPropertyEntity();
//        mockEventPropertyEntity.setXwho("021d4fa273e84641981dbd126b575ea4");
//        mockEventPropertyEntity.setGoods("computer");
//        mockEventPropertyEntity.setScreenHeight(900);
//        map = Object2Map(mockEventPropertyEntity);
//        map.forEach((k, v) -> System.out.println(k + " - " + v));
        System.out.println(UUID.randomUUID().toString().replace("-", "").replaceAll("[a-z|A-Z]", ""));
    }
}
