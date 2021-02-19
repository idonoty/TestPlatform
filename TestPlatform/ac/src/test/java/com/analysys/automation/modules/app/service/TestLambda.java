package com.analysys.automation.modules.app.service;

import com.analysys.automation.modules.app.entity.EventRecordEntity;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestLambda {
    @Autowired
    private EventRecordService eventRecordService;

    @Test
    public void test01() {
        List<String> list = eventRecordService.selectList(new EntityWrapper<EventRecordEntity>().between("xwhen", 1546790409095l, 1546790987327l))
                .parallelStream().filter(e -> e.getXwho().equals("1002832"))
                .sorted(Comparator.comparing(EventRecordEntity::getXwhen).reversed())
                .map(EventRecordEntity::getXwhat).distinct().collect(Collectors.toList());

        list.forEach(e -> System.out.println(e));

    }

    @Test
    public void test02() {
        IntStream.of(new int[]{1, 2, 3}).forEach(System.out::println);
        IntStream.range(1, 3).forEach(System.out::println);
        IntStream.rangeClosed(1, 3).forEach(System.out::println);
    }

    @Test
    public void getUUIDFiles() throws IOException {
        for (int k = 1; k <= 1; k++) {
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("D:\\uuid" + k + ".csv"), "UTF-8"));
            out.write("uuid");
            out.newLine();
            for (int i = 0; i < 10000; i++) {
                String uuid = UUID.randomUUID().toString().replaceAll("-", "");
                out.write(uuid);
                out.newLine();
            }
            out.flush();
            out.close();
        }
    }

    @Test
    public void getOrderIdByUUId() {
        for (int i = 0; i < 100000; i++) {
            int machineId = 1;//最大支持1-9个集群机器部署
            int hashCodeV = UUID.randomUUID().toString().hashCode();
            if (hashCodeV < 0) {//有可能是负数
                hashCodeV = -hashCodeV;
            }
//         0 代表前面补充0     
//         4 代表长度为4     
//         d 代表参数为正数型
            System.out.println("################## " + machineId + String.format("%011d", hashCodeV));
        }
    }
}
