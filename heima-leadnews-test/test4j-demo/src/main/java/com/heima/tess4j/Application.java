package com.heima.tess4j;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;

public class Application {
    public static void main(String[] args) throws TesseractException {
        ITesseract tesseract = new Tesseract();
        tesseract.setDatapath("E:\\test\\tessdata");
        //设置语言 --》简体中文
        tesseract.setLanguage("chi_sim");
        File file =  new File("E:\\test\\test.png");
        String result = tesseract.doOCR(file);
        System.out.println("识别的结果为： " + result);
    }
}
