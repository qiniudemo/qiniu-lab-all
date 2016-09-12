package com.qiniulab.fop;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import com.qiniu.util.Auth;
import com.qiniu.util.UrlSafeBase64;
import com.qiniulab.config.LabConfig;

/**
 * 图片实时处理并持久化
 * */
public class ImageSave {
	private static String saveasSign(String resUrl, String fop,
			String saveBucket, String saveKey, String accessKey,
			String secretKey) throws UnsupportedEncodingException,
			InvalidKeyException, NoSuchAlgorithmException {
		URI resUri = URI.create(resUrl);

		String newUrl = resUri.getHost() + resUri.getPath();
		String saveEntry = UrlSafeBase64.encodeToString(saveBucket + ":"
				+ saveKey);
		String fopWithSaveas = fop + "|saveas/" + saveEntry;

		Auth auth = Auth.create(accessKey, secretKey);

		StringBuilder newResUrl = new StringBuilder();
		newResUrl.append(resUrl).append("?").append(fopWithSaveas)
				.append("/sign/")
				.append(auth.sign(newUrl + "?" + fopWithSaveas));
		return newResUrl.toString();
	}

	/**
	 * 公开空间图片实时处理并持久化
	 * */
	public static String publicImageSaveas()
			throws UnsupportedEncodingException, InvalidKeyException,
			NoSuchAlgorithmException {
		String resUrl = "http://if-pbl.qiniudn.com/qiniu.png";
		String fop = "imageView2/0/w/100";
		String saveBucket = "if-pbl";
		String saveKey = "qiniu_w_100.png";
		return saveasSign(resUrl, fop, saveBucket, saveKey,
				LabConfig.ACCESS_KEY, LabConfig.SECRET_KEY);
	}

	/**
	 * 私有空间图片实时处理并持久化
	 * */
	public static String privateImageSaveas()
			throws UnsupportedEncodingException, InvalidKeyException,
			NoSuchAlgorithmException {
		String resUrl = "http://if-pri.qiniudn.com/qiniu.png";
		String fop = "imageView2/0/w/100";
		String saveBucket = "if-pri";
		String saveKey = "qiniu_w_100.png";
		Auth auth = Auth.create(LabConfig.ACCESS_KEY, LabConfig.SECRET_KEY);
		return auth.privateDownloadUrl(saveasSign(resUrl, fop, saveBucket,
				saveKey, LabConfig.ACCESS_KEY, LabConfig.SECRET_KEY));
	}

	public static void main(String[] args) {
		try {
			System.out.println(publicImageSaveas());
			System.out.println(privateImageSaveas());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

}
