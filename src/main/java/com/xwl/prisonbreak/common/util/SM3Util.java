package com.xwl.prisonbreak.common.util;

import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

import java.io.UnsupportedEncodingException;
import java.security.Security;
import java.util.Arrays;


/**
 * @author xwl
 * @date 2019-11-12 11:18
 * @description SM3加密工具，需要引入 bcprov-jdk15on-1.59
 */
public class SM3Util {
    private static final String ENCODING = "UTF-8";

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * sm3算法加密
     * @param paramStr 待加密字符串
     * @return 返回加密后，固定长度的16进制字符串
     */
    public static String encrypt(String paramStr) {
        // 将返回的hash值转为16进制字符串
        String resultHexString = "";
        // 将字符串转为16进制数组
        try {
            byte[] srcData = paramStr.getBytes(ENCODING);
            // 调用hash()
            byte[] resultHash = hash(srcData);
            // 将返回的hash值转为16进制字符串
            resultHexString = ByteUtils.toHexString(resultHash);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return resultHexString;
    }

    /**
     * 返回byte数组，生产对应的hash值
     * @param srcData
     * @return
     */
    public static byte[] hash(byte[] srcData) {
        SM3Digest digest = null;
        try {
            digest = new SM3Digest();
        } catch (Exception e) {
            e.printStackTrace();
        }
        digest.update(srcData, 0, srcData.length);
        byte[] hash = new byte[digest.getDigestSize()];
        digest.doFinal(hash, 0);
        return hash;
    }

    /**
     * 通过秘钥进行加密
     * @param key 秘钥
     * @param srcData 被加密的byte数组
     * @return
     */
    public static byte[] hmac(byte[] key, byte[] srcData) {
        KeyParameter keyParameter = new KeyParameter(key);
        SM3Digest digest = new SM3Digest();
        HMac mac = new HMac(digest);
        mac.init(keyParameter);
        mac.update(srcData, 0, srcData.length);
        byte[] result = new byte[mac.getMacSize()];
        mac.doFinal(result, 0);
        return result;
    }

    /**
     * 通过验证原数组和生成的hash数组是否为同一数组，验证2者是否为同一数据
     * @param srcStr 原字符串
     * @param sm3HexString 16进制字符串
     * @return 检验结果
     */
    public static boolean verify(String srcStr, String sm3HexString) {
        boolean flag = false;
        try {
            byte[] srcData = srcStr.getBytes(ENCODING);
            byte[] sm3Hash = ByteUtils.fromHexString(sm3HexString);
            byte[] newHash = hash(srcData);
            if (Arrays.equals(newHash, sm3Hash)) {
                flag = true;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static void main(String[] args) {
        String str = "bf4c05715714708e8d77fd797ee439a1";
        String hex = SM3Util.encrypt(str);
        System.out.println(hex);
    }
}
