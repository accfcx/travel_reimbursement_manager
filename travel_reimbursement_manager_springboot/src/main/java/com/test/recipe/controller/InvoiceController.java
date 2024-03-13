package com.test.recipe.controller;

import com.test.recipe.common.ResponseResult;
import com.test.recipe.mapper.InvoiceMapper;
import com.test.recipe.model.Invoice;
import com.test.recipe.service.Invoice2;
import com.test.recipe.service.OfdInvoiceExtractor;
import com.test.recipe.service.PdfInvoiceExtractor;
import org.apache.commons.io.FileUtils;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/invoice")
public class InvoiceController {

    @Value("${file.address}")
    private String address;

    @Resource
    InvoiceMapper invoiceMapper;

    @RequestMapping("/uploadFile")
    public ResponseResult<Invoice2> extrat(@RequestParam(value = "file", required = false) MultipartFile file, @RequestParam Long recipeId) {
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
            }
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }

        // 识别发票数据，发票单号，日期，金额
        System.out.println(result);

        Invoice byNum = invoiceMapper.findByNum(result.getCode());

        ResponseResult responseResult = new ResponseResult();

        if (byNum != null) {
            responseResult.setCode(100);
            responseResult.setMsg("发票已存在，请勿重复上传");
            return responseResult;
        }

        Invoice invoice = new Invoice();
        invoice.setRecipeId(recipeId);
        invoice.setInvoiceNumber(result.getCode());
        invoice.setInvoiceDate(result.getDate());
        invoice.setInvoiceType(result.getType());
        invoice.setAmount(result.getTotalAmount());

        invoiceMapper.insert(invoice);

        responseResult.setData(result);

        return responseResult;
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
