package com.ldzhn.baseframework.utils;

import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @description: 压缩解压缩类,编码UTF-8
 * @author: xlwang
 * @create: 2018/11/27 10:12
 * @version: V1.0
 */
public class MessageGZIP {

	private static String encode = Charsets.UTF_8.name();
	private static Base64 base64 = new Base64(76, new byte[]{124, 124}, true);// apache base64


	/**
	 * @author: xlwang
	 * @description: 解压缩, 编码UTF-8
	 * @create: 11:04 2018/11/27
	 * @param content
	 * @throws IOException
	 **/
	public static byte[] compressToByte(String content) throws IOException {
		if (content == null || content.length() == 0) {
			throw new IllegalArgumentException("参数无效,不能为空");
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip = new GZIPOutputStream(out);
		gzip.write(content.getBytes(encode));
		gzip.close();

		byte[] str = out.toByteArray();
//	    log.info("压缩前大小="+content.getBytes().length/1024+"K,压缩后大小="+out.toByteArray().length/1024+"K,压缩后base64后大小="+str.length/1024+" K");
		return str;
	}

	/**
	 * @return
	 * @author: xlwang
	 * @description: 解压,编码UTF-8
	 * @create: 10:20 2018/11/27
	 * @Param content 解压内容
	 **/
	public static String uncompressByte(byte[] content) throws IOException {
		if (content == null || content.length == 0) {
			throw new IllegalArgumentException("参数无效,不能为空");
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = new ByteArrayInputStream(content);
		GZIPInputStream gunzip = new GZIPInputStream(in);
		byte[] buffer = new byte[256];
		int n;
		while ((n = gunzip.read(buffer)) >= 0) {
			out.write(buffer, 0, n);
		}
		return out.toString(encode);
	}

	/**
	 * @author: xlwang
	 * @description: 先压缩，再转换base64编码,编码UTF-8
	 * @create: 10:22 2018/11/27
	 * @param content
	 * @throws IOException
	 **/
	public static String compressToBase64(String content) throws IOException {
		if (content == null || content.length() == 0) {
			return content;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip = new GZIPOutputStream(out);
		gzip.write(content.getBytes(encode));
		gzip.close();

		String str = base64.encodeToString(out.toByteArray());
//			log.info("压缩前大小="+content.getBytes().length/1024+"K,压缩后大小="+out.toByteArray().length/1024+"K,压缩后base64后大小="+str.getBytes().length/1024+" K");
		return str;
	}



	/**
	 * @author: xlwang
	 * @description: 解压，编码UTF-8
	 * @create: 11:15 2018/11/27
	 * @param content
	 * @return
	 * @throws IOException
	 **/
	public static String uncompress(String content) throws IOException {
		if (content == null || content.length() == 0) {
			return content;
		}
		byte[] byteBase64 = base64.decode(content);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = new ByteArrayInputStream(byteBase64);
		GZIPInputStream gunzip = new GZIPInputStream(in);
		byte[] buffer = new byte[256];
		int n;
		while ((n = gunzip.read(buffer)) >= 0) {
			out.write(buffer, 0, n);
		}
		return out.toString(encode);
	}

}
