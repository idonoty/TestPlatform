package com.analysys.automation.modules.app.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class testdemo {

    public static String getTime(int time, int num) {  //生成时间用作关联
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(time, +num);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(calendar.getTime());
    }

    public static void main(String[] args) {
        System.out.println(Calendar.MINUTE);
        System.out.println(getTime(Calendar.MINUTE, (int) (Math.random() * 64640) - 63200));
        for (int i = 0; i <20 ; i++) {
            System.out.println("2020-07-2"+((int) (Math.random() * 8) +1)+" 01:59:"+((int) (Math.random() * 10) +10));
        }
    }
}
