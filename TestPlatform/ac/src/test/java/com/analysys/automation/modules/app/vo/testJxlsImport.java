package com.analysys.automation.modules.app.vo;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jxls.reader.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.xml.sax.SAXException;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
public class testJxlsImport {

    @Test
    public void testImport() throws IOException, SAXException, InvalidFormatException {
        ClassPathResource classPathResource = new ClassPathResource("config/import/testcase.xml");
        InputStream configIs = classPathResource.getInputStream();
        XLSReader xlsReader = ReaderBuilder.buildFromXML(configIs);

        InputStream xlsIs = new BufferedInputStream(new FileInputStream("E:\\download\\测试用例.xlsx"));
        ReaderConfig.getInstance().setSkipErrors(true);

        List<TestcaseVO> testcaseVOS = new ArrayList<>();
        Map<String, Object> beans = new HashMap<>();

        beans.put("list", testcaseVOS);
        XLSReadStatus readStatus = new XLSReadStatus();

            readStatus = xlsReader.read(xlsIs, beans);

            log.info(" ============ " + readStatus.isStatusOK());
            readStatus.getReadMessages().forEach(e -> {
                log.error(">>>>>>>>>> " + ((XLSReadMessage) e).getMessage());
            });


//        List<Object> testcaseVOS = JxlsUtils.importFile("config/import/testcase.xml", "E:\\download\\测试用例.xlsx");

        System.out.println("***************************************");
        System.out.println("*************************************** " + testcaseVOS.size());
        testcaseVOS.forEach(e -> System.out.println(((TestcaseVO) e).toString()));
        System.out.println("***************************************");
    }
}
