package com.xwb.filesystem.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/fileViewUtil")
public class FileViewUtil {

    @Value("${file.path}")
    private String file_path;

    @GetMapping("/list")
    public List<String> list(@RequestParam(value = "page", defaultValue = "1") int page){
        List<String> result = new ArrayList<>();
        File folder = new File(file_path);
        if(!folder.exists()){
            return result;
        }
        File[] files = folder.listFiles();
        result = Arrays.asList(files).stream()
                .filter(file -> !file.isDirectory())
                .map(file -> file.getName()).collect(Collectors.toList());
        Collections.reverse(result);
        return result;
    }
}
