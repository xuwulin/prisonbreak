package com.xwl.prisonbreak.common.util;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;

/**
 * DES加密算法
 * 这个加密算法是对称的加密算法
 * 这个加密算法，在本系统中的应用，主要是因为该加密算法加密后
 * 密文都是字符串和数字的，没有其他字符。这样的密文可以应用在url地址上，
 * 不用担心被URL 地址上传递的数字被转义
 *
 * @Description:
 * @Author: xwl
 * @Create: 2019/8/23
 */
@Slf4j
public class RDesUtils {

    /**
     * 加密算法,可用 DES,DESede,Blowfish.
     */
    private final static String ALGORITHM = "DES";

    private final static String CRYPTKEY = "TYYW2LCBA2019";


    /**
     * DES解密算法
     *
     * @param data
     * @param cryptKey 密钥 要是偶数
     * @return
     * @throws Exception
     */
    public static String decrypt(String data, String cryptKey) throws Exception {
        return new String(decrypt(hex2byte(data.getBytes()),
                cryptKey.getBytes()));
    }

    /**
     * DES加密算法
     *
     * @param data
     * @param cryptKey
     * @return
     * @throws Exception
     */
    public final static String encrypt(String data, String cryptKey)
            throws Exception {
        return byte2hex(encrypt(data.getBytes(), cryptKey.getBytes()));
    }

    private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        // DES算法要求有一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 从原始密匙数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
        // 创建一个密匙工厂，然后用它把DESKeySpec转换成
        // 一个SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        SecretKey securekey = keyFactory.generateSecret(dks);
        // Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        // 用密匙初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
        // 现在，获取数据并加密
        // 正式执行加密操作
        return cipher.doFinal(data);
    }

    private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
        // DES算法要求有一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 从原始密匙数据创建一个DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
        // 创建一个密匙工厂，然后用它把DESKeySpec对象转换成
        // 一个SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        SecretKey securekey = keyFactory.generateSecret(dks);
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        // 用密匙初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
        // 现在，获取数据并解密
        // 正式执行解密操作
        return cipher.doFinal(data);
    }

    private static byte[] hex2byte(byte[] b) {
        if ((b.length % 2) != 0)
            throw new IllegalArgumentException("长度不是偶数");
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }

    private static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1)
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;
        }
        return hs.toUpperCase();
    }

    /**
     * 使用默认加密盐加密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static String encrypt(String data) {
        if (StringUtils.isBlank(data)) {
            return null;
        }
        try {
            String result = RDesUtils.encrypt(data, CRYPTKEY);
            return result;
        } catch (Exception e) {
            log.error("加密失败", e);
        }
        return null;
    }

    /**
     * 使用默认加密盐解密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static String decrypt(String data) {
        if (StringUtils.isBlank(data)) {
            return null;
        }
        try {
            return RDesUtils.decrypt(data, CRYPTKEY);
        } catch (Exception e) {
            log.error("解密失败！", e);
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        /*  数据传输采用  url?data=加密数据  */
        String data = "{\"dwbm\":\"770000\",\"rybm\":\"7700000020\",\"xtzj\":\"MT7700002019001161\"}";
        String t1 = RDesUtils.encrypt(data); // 加密
        String t2 = RDesUtils.decrypt(t1); // 解密
        System.out.println(t1);
        System.out.println(t2);
    }
}

