package com.demo.upload.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@RestController
public class FileController implements WebMvcConfigurer {

    SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd/");

    @PostMapping("/upload")
    public String upload(MultipartFile file, HttpServletRequest req) {
        String format=sdf.format(new Date());
        File folder=new File("/usr/local/docker/java/pic/"+format);
        if(!folder.isDirectory()){
            if(!folder.mkdirs()){
                return "文件夹创建失败";
            }
        }
        System.out.println(folder.getPath());
        String oldName=file.getOriginalFilename();
        String newName= UUID.randomUUID().toString()+oldName.substring(oldName.lastIndexOf("."),oldName.length());
        try {
            file.transferTo(new File(folder,newName));
            String filePath=req.getScheme()+"://"+req.getServerName()+":"+req.getServerPort()+"/files/"+format+newName;
            return filePath;
        }catch (IOException e){
            e.printStackTrace();
        }
        return "上传失败！";
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/files/**").addResourceLocations("file:/usr/local/docker/java/pic/");
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }
}