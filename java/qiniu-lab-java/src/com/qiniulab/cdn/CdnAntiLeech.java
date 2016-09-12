package com.qiniulab.cdn;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;

public class CdnAntiLeech {
	/**
	 * 生成资源基于CDN时间戳防盗链的访问外链
	 * 
	 * @param 资源原始外链
	 * @param 结果资源的有效期，单位秒
	 * @throws MalformedURLException
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 */

	public static String getAntiLeechAccessUrlBasedOnTimestamp(String url, String encryptKey, int durationInSeconds)
			throws MalformedURLException, UnsupportedEncodingException, NoSuchAlgorithmException {
		URL urlObj = new URL(url);
		String path = urlObj.getPath();

		long timestampNow = System.currentTimeMillis() / 1000 + durationInSeconds;
		String expireHex = Long.toHexString(timestampNow);

		String toSignStr = String.format("%s%s%s", encryptKey, path, expireHex);
		String signedStr = md5ToLower(toSignStr);

		String signedUrl = null;
		if (urlObj.getQuery() != null) {
			signedUrl = String.format("%s&sign=%s&t=%s", url, signedStr, expireHex);
		} else {
			signedUrl = String.format("%s?sign=%s&t=%s", url, signedStr, expireHex);
		}

		return signedUrl;
	}

	private static String md5ToLower(String src) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("MD5");
		digest.update(src.getBytes("utf-8"));
		byte[] md5Bytes = digest.digest();
		return Hex.encodeHexString(md5Bytes);
	}
}
