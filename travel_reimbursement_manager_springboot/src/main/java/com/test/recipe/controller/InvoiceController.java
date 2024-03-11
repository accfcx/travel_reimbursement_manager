package com.test.recipe.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/invoice")
public class InvoiceController {

    @Value("${file.address}")
    private String address;

    @RequestMapping("/uploadFile")
    public String uploadImg(@RequestParam("file") MultipartFile upload) {
        ApplicationHome home = new ApplicationHome(getClass());
        File fileJar = home.getSource();
        // 要上传到哪里
        String savePath = fileJar.getParent() + address;

        File file = new File(savePath);
        if (!file.exists() || !file.isDirectory()) {
            file.mkdir();
        }
        // 获取原文件名
        String oldName = upload.getOriginalFilename();
        // 对原文件名进行处理，添加前缀
        String newName = UUID.randomUUID() + "_" + oldName;
        // 根据保存路径和新名字生成一个文件对象
        File saveFile = new File(savePath, newName);
        try {
            upload.transferTo(saveFile);

            // TODO 识别发票数据，发票单号，日期，金额

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 返回文件保存路径及文件名
        return address + newName;
    }

}
