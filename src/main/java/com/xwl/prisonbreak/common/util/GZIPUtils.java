package com.xwl.prisonbreak.common.util;

import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;


/**
 * @Auther: xuwulin
 * @Date: 2019/5/15 17:02
 * @Description: 字符串压缩/解压工具类
 */
@Slf4j
public class GZIPUtils {

	public static void main(String[] args) {
		String str = fileRead("E:\\chrome下载\\response_1561295260516.json");
		System.out.println("原长度：" + str.length());
		byte[] compressBytes = GZIPUtils.compress(str);
		String compress64Str = new BASE64Encoder().encode(compressBytes);
		System.out.println("压缩后字符串：" + compress64Str);
		System.out.println("压缩后长度：" + compress64Str.length());
		System.out.println("解压缩后字符串：" + GZIPUtils.uncompressToString(GZIPUtils.compress(str)));
	}

	public static final String GZIP_ENCODE_UTF_8 = "UTF-8";

	public static final String GZIP_ENCODE_ISO_8859_1 = "ISO-8859-1";

	/**
	 * 字符串压缩为GZIP字节数组
	 *
	 * @param str
	 * @return
	 */
	public static byte[] compress(String str) {
		return compress(str, GZIP_ENCODE_UTF_8);
	}

	/**
	 * 字符串压缩为GZIP字节数组
	 *
	 * @param str
	 * @param encoding
	 * @return
	 */
	public static byte[] compress(String str, String encoding) {
		if (str == null || str.length() == 0) {
			return null;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip;
		try {
			gzip = new GZIPOutputStream(out);
			gzip.write(str.getBytes(encoding));
			gzip.close();
			out.close();
		} catch (IOException e) {
			log.error("压缩字符串失败", e);
		}
		return out.toByteArray();
	}

	/**
	 * GZIP解压缩
	 *
	 * @param bytes
	 * @return
	 */
	public static byte[] uncompress(byte[] bytes) {
		if (bytes == null || bytes.length == 0) {
			return null;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		try {
			GZIPInputStream ungzip = new GZIPInputStream(in);
			byte[] buffer = new byte[256];
			int n;
			while ((n = ungzip.read(buffer)) >= 0) {
				out.write(buffer, 0, n);
			}
			ungzip.close();
			in.close();
			out.close();
		} catch (IOException e) {
			log.error("解压字符串失败", e);
		}
		return out.toByteArray();
	}

	/**
	 * @param bytes
	 * @return
	 */
	public static String uncompressToString(byte[] bytes) {
		return uncompressToString(bytes, GZIP_ENCODE_UTF_8);
	}

	/**
	 * 解压字符串，可指定字符
	 *
	 * @param bytes
	 * @param encoding
	 * @return
	 */
	public static String uncompressToString(byte[] bytes, String encoding) {
		if (bytes == null || bytes.length == 0) {
			return null;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		try {
			GZIPInputStream ungzip = new GZIPInputStream(in);
			byte[] buffer = new byte[256];
			int n;
			while ((n = ungzip.read(buffer)) >= 0) {
				out.write(buffer, 0, n);
			}
			return out.toString(encoding);
		} catch (IOException e) {
			System.out.println(e);
		}
		return null;
	}

	/**
	 * 文件读取
	 *
	 * @param filePath
	 * @return
	 */
	public static String fileRead(String filePath) {
		// 定义一个字符串缓存，将字符串存放缓存中
		StringBuilder sb = new StringBuilder();
		try {
			// 定义一个file对象，用来初始化FileReader
			File file = new File(filePath);
			// 定义一个fileReader对象，用来初始化BufferedReader
			FileReader reader = new FileReader(file);
			// new一个BufferedReader对象，将文件内容读取到缓存
			BufferedReader bReader = new BufferedReader(reader);
			String s = "";
			// 逐行读取文件内容，不读取换行符和末尾的空格
			while ((s = bReader.readLine()) != null) {
				// 将读取的字符串添加换行符后累加存放在缓存中
				sb.append(s);
			}
			bReader.close();
		} catch (Exception e) {
			log.error("读取文件失败", e);
		}
		return sb.toString();
	}
}

