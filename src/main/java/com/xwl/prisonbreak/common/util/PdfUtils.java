package com.xwl.prisonbreak.common.util;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;

/**
 * @author xwl
 * @date 2019-08-19 19:45
 * @description
 */
public class PdfUtils {

    /**
     * @param filePath   生成pdf文件夹路径
     * @param fileName   生成pdf文件名
     * @param imagesPath 需要转换的图片文件夹路径
     */
    public static void imagesToPdf(String filePath, String fileName, String imagesPath) {
        try {
            fileName = filePath + fileName + ".pdf";
            File file = new File(fileName);
            // 第一步：创建一个document对象。
            Document document = new Document();
            document.setMargins(0, 0, 0, 0);
            // 第二步：
            // 创建一个PdfWriter实例，
            PdfWriter.getInstance(document, new FileOutputStream(file));
            // 第三步：打开文档。
            document.open();
            // 第四步：在文档中增加图片。
            File files = new File(imagesPath);
            String[] images = files.list();
            int len = images.length;

            for (int i = 0; i < len; i++) {
                if (images[i].toLowerCase().endsWith(".bmp")
                        || images[i].toLowerCase().endsWith(".jpg")
                        || images[i].toLowerCase().endsWith(".jpeg")
                        || images[i].toLowerCase().endsWith(".gif")
                        || images[i].toLowerCase().endsWith(".png")) {
                    String temp = imagesPath + "\\" + images[i];
                    Image img = Image.getInstance(temp);
                    img.setAlignment(Image.ALIGN_CENTER);
                    // 根据图片大小设置页面，一定要先设置页面，再newPage（），否则无效
                    document.setPageSize(new Rectangle(img.getWidth(), img.getHeight()));
                    document.newPage();
                    document.add(img);
                }
            }
            // 第五步：关闭文档。
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String filePath = "D:\\Users\\XUNING\\Desktop\\pdf\\";
        String fileName = "imageToPdf";
        String imagesPath = "D:\\Users\\XUNING\\Desktop\\image";
        imagesToPdf(filePath, fileName, imagesPath);
    }
}
