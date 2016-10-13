package com.qiniulab.cdn;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;

public class GetAntiLeechUrlExample {

	public static void main(String[] args) {
		// cdn 配置的基于时间戳防盗链的加密字符串，cdn 配置完成后会得到
		String encryptKey = "";
		// 待加密链接
		String fileKey = "xxxx.pdf";
		String encodedFileKey;
		try {
			// 考虑到文件名称会有中文，所以需要做urlencode
			encodedFileKey = URLEncoder.encode(fileKey, "utf-8");//.replace("%2F", "/");
			String urlToSign = String.format("http://img.abc.com/%s", encodedFileKey);
			// 有效期
			int duration = 3600;

			String signedUrl = CdnAntiLeech.getAntiLeechAccessUrlBasedOnTimestamp(urlToSign, encryptKey, duration);
			System.out.println(signedUrl);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

	}

}
