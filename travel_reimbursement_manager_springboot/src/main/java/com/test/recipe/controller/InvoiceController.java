package com.test.recipe.controller;

import com.test.recipe.service.Invoice2;
import com.test.recipe.service.OfdInvoiceExtractor;
import com.test.recipe.service.PdfInvoiceExtractor;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import net.sourceforge.tess4j.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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

    @RequestMapping(value = "/extrat")
    public Invoice2 extrat(@RequestParam(value = "file", required = false) MultipartFile file) {
//        String fileName = "/Users/accfcx/Desktop/发票.pdf";
        String fileName = getDateFormat(FILE_NAME_FORMAT_STRING).format(new Date());

        ApplicationHome home = new ApplicationHome(getClass());
        File fileJar = home.getSource();
        String backupPath = fileJar.getParent() + address;

        File dest = null;
        boolean ofd = false;
        if (null != file && !file.isEmpty()) {
            if (file.getOriginalFilename().toLowerCase().endsWith(".ofd")) {
                ofd = true;
                dest = new File(backupPath, fileName + ".ofd");
            } else {
                dest = new File(backupPath, fileName + ".pdf");
            }
            dest.getParentFile().mkdirs();
            try {
                FileUtils.copyInputStreamToFile(file.getInputStream(), dest);
            } catch (IOException e) {
            }
        }
        Invoice2 result = null;
        try {
            if (null != dest) {
                if (ofd) {
                    result = OfdInvoiceExtractor.extract(dest);
                } else {
                    result = PdfInvoiceExtractor.extract(dest);
                }
                if (null != result.getAmount()) {
                    dest.delete();
                }
            } else {
                result = new Invoice2();
                result.setTitle("error");
            }
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            result = new Invoice2();
            result.setTitle("error");
        }
        return result;
    }

    private static ThreadLocal<Map<String, DateFormat>> threadLocal = new ThreadLocal<>();
    private static final String FILE_NAME_FORMAT_STRING = "yyyy/MM-dd/HH-mm-ssSSSS";

    public static DateFormat getDateFormat(String pattern) {
        Map<String, DateFormat> map = threadLocal.get();
        DateFormat format = null;
        if (null == map) {
            map = new HashMap<>();
            format = new SimpleDateFormat(pattern);
            map.put(pattern, format);
            threadLocal.set(map);
        } else {
            format = map.computeIfAbsent(pattern, k -> new SimpleDateFormat(k));
        }
        return format;
    }

}
