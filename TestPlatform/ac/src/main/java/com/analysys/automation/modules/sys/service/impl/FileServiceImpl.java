package com.analysys.automation.modules.sys.service.impl;

import com.analysys.automation.modules.sys.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service("fileService")
public class FileServiceImpl implements FileService {
    @Value("${file.filepath.upload}")
    private String uploadFilePath;


    @Override
    public String upload(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        String fileName = originalFileName.substring(0, originalFileName.indexOf(".")) + System.currentTimeMillis() + originalFileName.substring(originalFileName.indexOf("."));
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        String dateString = df.format(new Date());
        uploadFilePath = uploadFilePath + dateString + "\\";

        File dest = new File(uploadFilePath + fileName);
        // 检测是否存在目录
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
            return uploadFilePath + fileName;
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     *  0: 表示成功
     *  1: 文件内容为空
     *  2: 后缀名称不为指定内容
     *
     * @param file
     * @return
     */
    @Override
    public int validate(MultipartFile file, String suffixName) {
        if(file == null) {
            return 1;
        }
        if (file.isEmpty()) {
            return 1;
        }

        String originalFileName = file.getOriginalFilename();
        String originalSuffixName = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);

        if(!originalSuffixName.equalsIgnoreCase(suffixName)) {
            return 2;
        }
        return 0;
    }
}
