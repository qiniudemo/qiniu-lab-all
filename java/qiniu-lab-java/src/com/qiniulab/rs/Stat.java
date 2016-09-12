package com.qiniulab.rs;

import com.qiniu.common.QiniuException;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import com.qiniulab.config.LabConfig;

/**
 * 获取文件的基本信息
 * */
public class Stat {

	public static void stat() {
		String bucket = "if-pbl";
		String key = "qiniu.png";
		Auth auth = Auth.create(LabConfig.ACCESS_KEY,
				LabConfig.SECRET_KEY);
		BucketManager bm = new BucketManager(auth);
		try {
			FileInfo fi = bm.stat(bucket, key);
			System.out.println(fi.hash);
			System.out.println(fi.fsize);
			System.out.println(fi.mimeType);
			System.out.println(fi.putTime);
			System.out.println(fi.endUser);
		} catch (QiniuException ex) {
			// 401 表示 AccessKey, SecretKey 设置不对
			// 612表示文件不存在
			// 631表示空间不存在
			System.err.println(ex.response.statusCode);
			System.err.println(ex.response.error);
		}
	}

	public static void main(String args[]) {
		stat();
	}
}
