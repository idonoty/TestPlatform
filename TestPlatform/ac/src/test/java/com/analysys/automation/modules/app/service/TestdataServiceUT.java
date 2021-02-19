package com.analysys.automation.modules.app.service;

import com.analysys.automation.common.utils.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestdataServiceUT {
    @Autowired
    private TestdataService testdataService;

    @Test
    public void testDateSorting() {
        List<Date> dataList = testdataService.queryDateList("a");
        dataList.forEach(d -> System.out.println(DateUtils.format(d)));

        System.out.println( " ---------------------- ");
        dataList.stream().sorted(new Comparator<Date>() {
            @Override
            public int compare(Date o1, Date o2) {
                return Long.valueOf(o1.getTime() - o2.getTime()).intValue();
            }
        }).forEach(d -> System.out.println(DateUtils.format(d)));
    }
}
