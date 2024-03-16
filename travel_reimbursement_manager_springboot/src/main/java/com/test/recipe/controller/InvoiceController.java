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



}
