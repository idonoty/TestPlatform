package com.analysys.automation.common.utils;

import java.util.ArrayList;
import java.util.List;

public class ListUtils {
    public static List<List<? extends Object>> splitList(List<? extends Object> originalList, int listNumber) {
        List<List<? extends Object>> results = new ArrayList<>();
        if (originalList.size() <= listNumber) {
            for (int i = 0; i < originalList.size(); i++) {
                List subList = new ArrayList<>();
                subList.add(originalList.get(i));
                results.add(subList);
            }
        } else {
            int length = originalList.size() / listNumber;
            for (int i = 0; i < listNumber; i++) {
                List subList = new ArrayList<>();
                if (i == originalList.size() - 1) {
                    subList = originalList.subList(i * length, originalList.size());
                } else {
                    subList = originalList.subList(i * length, (i + 1) * length);
                }
                results.add(subList);
            }
        }
        return results;
    }
}
