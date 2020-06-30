package com.xwb.filesystem.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/uploadUtil")
public class UploadUtil {

    @Value("${file.path}")
    private String file_path;

    @PostMapping("/upload")
    public void upload(MultipartFile file){
        File folder = new File(file_path);
        if(!folder.exists()){
            folder.mkdir();
        }

        String fileName = file.getOriginalFilename();
        String extend = fileName.substring(fileName.lastIndexOf('.'), fileName.length());

        //生成前缀：日期+序列号
        String prefix = "";
        String now = new SimpleDateFormat("yyyyMMdd").format(new Date());
        File[] files = folder.listFiles();
        List<File> fileList = Arrays.asList(files).stream()
                .filter(f -> !f.isDirectory() && f.getPath().contains(now)).collect(Collectors.toList());
        if(fileList.isEmpty()){
            prefix = now + "1";
        }else{
            prefix = now + (fileList.size() + 1);
        }

        if(".jpg".equals(extend.toLowerCase()) || ".png".equals(extend.toLowerCase())){
            //图片文件，保存文件名为：日期前缀+image
            fileName = prefix + "_img";
        }else{
            //非图片文件，保存文件名为：日期前缀+原文件名
            String originalName = fileName.substring(0, fileName.lastIndexOf('.'));
            fileName = prefix + "_" + originalName;
        }
        String filePath = file_path + fileName + extend;
        File dest = new File(filePath);
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}