package com.xwb.filesystem.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

@RestController
@RequestMapping("/downloadUtil")
public class DownloadUtil {

    @Value("${file.path}")
    private String file_path;

    @GetMapping("/download")
    public void download(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        String path = request.getParameter("path");
        if(path != null) {
            File file = new File(file_path + path);
            if (file.exists()) {
                response.setContentType("application/force-download");
                response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(path, "UTF-8"));
                byte[] buffer = new byte[1024];
                FileInputStream in = null;
                OutputStream out = null;
                try {
                    in = new FileInputStream(file);
                    out = response.getOutputStream();
                    int i = 0;
                    while ((i = in.read(buffer)) != -1) {
                        out.write(buffer, 0, i);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (in != null) {
                            in.close();
                        }
                        if(out != null){
                            out.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
