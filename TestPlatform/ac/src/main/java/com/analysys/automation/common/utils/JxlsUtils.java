package com.analysys.automation.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jxls.reader.*;
import org.springframework.core.io.ClassPathResource;
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
public class JxlsUtils {
    public static List<Object> importFile(String xmlFile, String importFile) throws IOException, SAXException, InvalidFormatException {
        ClassPathResource classPathResource = new ClassPathResource(xmlFile);
        InputStream configInputStream = classPathResource.getInputStream();
        XLSReader xlsReader = ReaderBuilder.buildFromXML(configInputStream);
        InputStream xlsInputStream = new BufferedInputStream(new FileInputStream(importFile));
        ReaderConfig.getInstance().setSkipErrors(true);

        List<Object> list = new ArrayList<>();
        Map<String, Object> beans = new HashMap<>();
        beans.put("list", list);
        XLSReadStatus xlsReadStatus = xlsReader.read(xlsInputStream, beans);

        log.info("----------- = " + xlsReadStatus.isStatusOK());
        List<XLSReadMessage> result = xlsReadStatus.getReadMessages();
        result.forEach(e -> {
            log.error("------------>" + e.getMessage());
        });

        return list;
    }
}
