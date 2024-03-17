package com.test.recipe.controller;

import com.test.recipe.common.ResponseResult;
import com.test.recipe.dto.RecipeRequest;
import com.test.recipe.mapper.InvoiceMapper;
import com.test.recipe.model.Invoice;
import com.test.recipe.service.Invoice2;
import com.test.recipe.service.OfdInvoiceExtractor;
import com.test.recipe.service.PdfInvoiceExtractor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/invoice")
public class InvoiceController {


    @PostMapping("/upload")
    public String upload(@RequestParam(value = "files", required = false) List<MultipartFile> files,
                         @RequestParam Long id) {

        StringBuffer stringBuffer = new StringBuffer();

        String result = stringBuffer.toString();
        if (StringUtils.isNotEmpty(result)) {
            return result;
        }
        if (files != null && !files.isEmpty()) {
            files.forEach(file -> {
                ResponseResult<Invoice2> result2 = handleOneInvoice(file, id);
                if (result2.getCode() == 100) {
                    stringBuffer.append(result2.getMsg());
                }
            });
        }
        return "suc";

    }

    @Value("${file.address}")
    private String address;

    @Resource
    InvoiceMapper invoiceMapper;

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


    public ResponseResult<Invoice2> handleOneInvoice(MultipartFile file, Long recipeId) {
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
        Invoice byNum = invoiceMapper.findByNum(result.getCode());
        ResponseResult responseResult = new ResponseResult();
        if (byNum != null) {
            responseResult.setCode(100);
            responseResult.setMsg("发票已存在，请勿重复上传. 发票号:" + result.getCode());
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

}
