package com.jxd.travel.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class FilePathConfig implements WebMvcConfigurer {

    @Value("${file.path}")
    private String path;//映射路径

    @Value("${file.address}")
    private String address;//真实路径中文件夹名

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //获取jar包所在目录
        ApplicationHome h = new ApplicationHome(getClass());
        File jarF = h.getSource();
        //在jar包所在目录下生成一个upload文件夹用来存储上传的图片
        String dirPath = jarF.getParentFile().toString()+ address;

        registry.addResourceHandler(path).addResourceLocations("file:" + dirPath);
    }
}
