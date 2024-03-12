package com.test.recipe;

import net.sourceforge.tess4j.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class test {
    public static void main(String[] args) {
        File pdfFile = new File("/Users/accfcxsilly/Desktop/发票.pdf");
        try (PDDocument document = PDDocument.load(pdfFile)) {
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            for (int page = 0; page < document.getNumberOfPages(); ++page) {
                BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);
                String result = new Tesseract().doOCR(bim);
                System.out.println(result);
            }
        } catch (IOException | TesseractException e) {
            System.err.println("Exception while trying to read pdf document - " + e);
        }
    }
}
