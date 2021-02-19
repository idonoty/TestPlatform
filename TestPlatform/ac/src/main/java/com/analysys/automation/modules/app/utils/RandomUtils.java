package com.analysys.automation.modules.app.utils;

import java.util.Date;
import java.util.Random;

public class RandomUtils {

    public static String selectValueFromArray(String[] array) {
        Random r = new Random();
        int index = r.nextInt(array.length);
        return array[index];
    }

    public static String randomMac() {
        Random random = new Random();
        String[] mac = {
                String.format("%02x", 0x52),
                String.format("%02x", 0x54),
                String.format("%02x", 0x00),
                String.format("%02x", random.nextInt(0xff)),
                String.format("%02x", random.nextInt(0xff)),
                String.format("%02x", random.nextInt(0xff))
        };
        return String.join(":", mac);
    }

    public static Date randomDate(Date baseDate, boolean isAdded) {
        Random r = new Random();
        long milliSeconds = r.nextInt(3600 * 24 * 20 * 1000) - 3600 * 24 * 1000;
        if (isAdded == false)
            milliSeconds = milliSeconds * -1;
        Date d = new Date();
        d.setTime(baseDate.getTime() + milliSeconds);
        return d;
    }

    public static long randomLongValue() {
        Random r = new Random();
        long milliSeconds = 3600 * 8 * 1000 + r.nextInt(3600 * 2 * 1000);

        return milliSeconds;
    }

    public static String randomIP() {
        Random r = new Random();
        String result = r.nextInt(220) + ".";
        result = result + r.nextInt(220) + ".";
        result = result + r.nextInt(220) + ".";
        result = result + r.nextInt(220);
        return result;
    }

    //210.51.160.0 - 210.51.191.255     --北京
    private static String randomBjIP() {
        Random r = new Random();
        String result = "210.51.";
        result = result + (r.nextInt(191 - 160) + 160) + ".";
        result = result + r.nextInt(250);
        return result;
    }

    //114.80.0.0 - 114.81.255.255       --上海
    private static String randomShIP() {
        Random r = new Random();
        String result = "114.80.";
        result = result + r.nextInt(250) + ".";
        result = result + r.nextInt(250);
        return result;
    }

    //218.76.0.0 - 218.76.7.255         --长沙
    private static String randomCsIP() {
        Random r = new Random();
        String result = "218.76.";
        result = result + r.nextInt(7) + ".";
        result = result + r.nextInt(250);
        return result;
    }

    //218.89.185.168 - 218.89.188.255   --成都
    private static String randomCdIP() {
        Random r = new Random();
        String result = "218.89.";
        result = result + (r.nextInt(3) + 185) + ".";
        result = result + r.nextInt(250);
        return result;
    }

    //211.134.128.0 - 211.134.191.255   --东京
    private static String randomDjIP() {
        Random r = new Random();
        String result = "211.134.";
        result = result + (r.nextInt(191 - 128) + 128) + ".";
        result = result + r.nextInt(250);
        return result;
    }

    //69.230.0.0 - 69.230.31.255        --洛杉矶
    private static String randomLsjIP() {
        Random r = new Random();
        String result = "69.230.";
        result = result + r.nextInt(31) + ".";
        result = result + r.nextInt(250);
        return result;
    }

    //219.137.128.0 - 219.137.159.255  广州
    private static String randomGzIP() {
        Random r = new Random();
        String result = "219.137.";
        result = result + (r.nextInt(159 - 128) + 128) + ".";
        result = result + r.nextInt(250);
        return result;
    }

    //116.24.160.0 - 116.24.191.255  深圳
    private static String randomSzIP() {
        Random r = new Random();
        String result = "116.24.";
        result = result + (r.nextInt(191 - 160) + 160) + ".";
        result = result + r.nextInt(250);
        return result;
    }

    public static String randomIPByCity(String city) {
        switch (city) {
            case "北京":
                return randomBjIP();
            case "上海":
                return randomShIP();
            case "广州":
                return randomGzIP();
            case "深圳":
                return randomSzIP();
            case "成都":
                return randomCdIP();
            case "长沙":
                return randomCsIP();
            case "东京":
                return randomDjIP();
            case "洛杉矶":
                return randomLsjIP();
            default:
                return randomBjIP();
        }
    }

    public static void main(String[] args) {
        System.out.println(randomIP());
    }
}
