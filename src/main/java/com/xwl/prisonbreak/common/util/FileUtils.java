package com.xwl.prisonbreak.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Auther: xuwulin
 * @Date: 2019/5/15 17:02
 * @Description: 文件工具
 */
@Slf4j
public class FileUtils {

    public static final String BASE_PATH = "";

    /**
     * 获取文件系统分隔符
     *
     * @return
     */
    public static String getFileSeparator() {
        return System.getProperty("file.separator");
    }

    /**
     * 获取文件保存的文件夹：格式：/日期/时间/
     *
     * @return
     */
    public static String getFolder() {
        String dateStr = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String timeStr = new SimpleDateFormat("HHmmss").format(new Date());
        return getFileSeparator() + dateStr + getFileSeparator() + timeStr + getFileSeparator();
    }

    /**
     * 获取随机文件名，文件名以FL_开头
     * @param prefix 文件名前缀
     * @param length 文件名长度（除去前缀后的长度）
     * @return
     */
    public static String getFileName(String prefix, Integer length) {
        if (StringUtils.isBlank(prefix)) {
            prefix = "FN";
        }
        if (length == null) {
            length = 16;
        }
        return UUIDUtils.getUid(prefix, length);
    }

    /**
     * 返回文件名后缀
     *
     * @param fileName
     * @return
     */
    public static String getFileNameSub(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."), fileName.length());
    }

    /**
     * 文件上传
     *
     * @param file
     * @param filePath
     * @param fileName
     * @throws Exception
     */
    // TODO 这样上传,如果文件过大,会导致内存溢出
    public static void uploadFile(byte[] file, String filePath, String fileName) throws Exception {
        File targetFile = new File(filePath);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        FileOutputStream out = new FileOutputStream(filePath + fileName);
        out.write(file);
        out.flush();
        out.close();
    }

    /**
     * 匹配文件的后缀
     *
     * @param fileName
     * @param exts
     * @return
     */
    public static boolean match(String fileName, String... exts) {
        if (StringUtils.isEmpty(fileName)) {
            return false;
        }
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        for (String ext1 : exts) {
            if (ext.equals(ext1)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 文件路径取md5
     *
     * @param path 文件路径
     * @return 返回32位的字符串
     */
    public static String md5File(String path) {
        String value = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(path);
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[4096];
            int length;
            while ((length = fis.read(buffer, 0, 4096)) != -1) {
                md.update(buffer, 0, length);
            }
            value = Hex.encodeHexString(md.digest());
        } catch (NoSuchAlgorithmException | IOException e) {
            log.error("calc md5 failed", e);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    log.error("close resource failed");
                }
            }
        }
        return value;
    }

    /**
     * 将文件转为字节数组
     *
     * @param pathStr 文件路径
     * @return
     */
    public static byte[] getBytesByFile(String pathStr) {
        File file = new File(pathStr);
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            byte[] data = bos.toByteArray();
            bos.close();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将字节数组写入文件
     * @param bytes 字节数组
     * @param filePath 要写入的文件路径
     * @param fileName 文件名
     */
    public static void byte2File(byte[] bytes, String filePath, String fileName) {
        try {
            Path path = Paths.get(filePath, fileName);
            File file = new File(path.toString());
            OutputStream output = new FileOutputStream(file);
            BufferedOutputStream bufferedOutput = new BufferedOutputStream(output);
            bufferedOutput.write(bytes);
            bufferedOutput.close();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String s = md5File("D:\\Users\\XUNING\\Desktop\\和为\\项目时间节点.png");
        System.out.println(s);
    }
}
