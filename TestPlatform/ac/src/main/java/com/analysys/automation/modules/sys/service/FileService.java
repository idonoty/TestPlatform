package com.analysys.automation.modules.sys.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    String upload(MultipartFile file);

    int validate(MultipartFile file, String suffixName);
}
