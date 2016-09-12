package com.qiniulab.upload;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.qiniu.util.UrlSafeBase64;
import com.qiniulab.config.LabConfig;

/**
 * 简单文件上传
 * */
public class SimpleUpload {
	/**
	 * 上传无key文件，这种上传方式将使得七牛使用文件的hash作为文件名
	 * */
	public void simpleUploadWithoutKey() throws QiniuException {
		Auth auth = Auth.create(LabConfig.ACCESS_KEY, LabConfig.SECRET_KEY);
		String token = auth.uploadToken(LabConfig.PUBLIC_BUCKET);
		UploadManager um = new UploadManager();
		try {
			Response resp = um.put("/Users/jemy/Documents/qiniu.mp4", null,
					token);
			System.out.println(resp.bodyString());
		} catch (QiniuException ex) {
			System.out.println(ex.getMessage());
		}
	}

	/**
	 * 上传有key文件，七牛将使用指定的key作为文件名
	 * */
	public void simpleUploadWithKey() throws QiniuException {
		Auth auth = Auth.create(LabConfig.ACCESS_KEY, LabConfig.SECRET_KEY);
		String token = auth.uploadToken(LabConfig.PUBLIC_BUCKET);
		UploadManager um = new UploadManager();
		try {
			Response resp = um.put("/Users/jemy/Documents/qiniu.mp4",
					"qiniu.mp4", token);
			System.out.println(resp.bodyString());
		} catch (QiniuException ex) {
			System.out.println(ex.getMessage());
		}
	}

	/**
	 * 带扩展参数上传，所谓扩展参数就是除了七牛规定的参数之外的请求参数， 必须符合一定的命名规则，而且值不能为空。
	 * */
	public void simpleUploadWithExtraParams() {
		Auth auth = Auth.create(LabConfig.ACCESS_KEY, LabConfig.SECRET_KEY);
		String token = auth.uploadToken(LabConfig.PUBLIC_BUCKET);

		StringMap extraParams = new StringMap();
		extraParams.put("x:hello", "hello");
		extraParams.put("x:qiniu", "qiniu");
		extraParams.put("apple", "");
		extraParams.put("x:apple", "");
		extraParams.put("", "");

		UploadManager um = new UploadManager();
		try {
			Response resp = um.put("/Users/jemy/Documents/qiniu.png",
					"qiniu_ex.png", token, extraParams, null, false);
			System.out.println(resp.bodyString());
		} catch (QiniuException ex) {
			System.out.println(ex.getMessage());
		}
	}

	/**
	 * 客户端上传指定文件的类型(mimeType)
	 * */
	public void simpleUploadWithMimeType() {
		Auth auth = Auth.create(LabConfig.ACCESS_KEY, LabConfig.SECRET_KEY);
		String token = auth.uploadToken(LabConfig.PUBLIC_BUCKET);
		UploadManager um = new UploadManager();
		String mimeType = "image/gif";
		try {
			Response resp = um.put("/Users/jemy/Documents/qiniu.png",
					"qiniu.png", token, null, mimeType, false);
			System.out.println(resp.bodyString());
		} catch (QiniuException ex) {
			System.out.println(ex.getMessage());
		}
	}

	/**
	 * 客户端上传设置文件的crc32校验，这将触发七牛对文件做crc32的计算，并和上传端指定的文件crc32做比较
	 * */
	public void simpleUploadWithCrc32Check() {
		Auth auth = Auth.create(LabConfig.ACCESS_KEY, LabConfig.SECRET_KEY);
		String token = auth.uploadToken(LabConfig.PUBLIC_BUCKET);
		UploadManager um = new UploadManager();
		try {
			Response resp = um.put("/Users/jemy/Documents/qiniu.png",
					"qiniu_crc32.png", token, null, null, true);
			System.out.println(resp.bodyString());
		} catch (QiniuException ex) {
			System.out.println(ex.getMessage());
		}
	}

	/**
	 * 自定义文件上传后七牛的回复内容
	 * */
	public void simpleUploadWithReturnBody() {
		Auth auth = Auth.create(LabConfig.ACCESS_KEY, LabConfig.SECRET_KEY);
		StringMap extraParams = new StringMap();
		extraParams.put("x:hello", "hello");
		extraParams.put("x:qiniu", "qiniu");
		StringMap putPolicy = new StringMap();
		putPolicy
				.put("returnBody",
						"{\"hash\":\"$(hash)\",\"key\":\"$(key)\",\"bucket\":\"$(bucket)\",\"hello\":\"$(x:hello)\"}");
		String token = auth.uploadToken(LabConfig.PUBLIC_BUCKET, null, 3600,
				putPolicy);
		UploadManager um = new UploadManager();
		try {
			Response resp = um.put("/Users/jemy/Documents/qiniu.png",
					"qiniu_r1.png", token, extraParams, null, true);
			System.out.println(resp.bodyString());
		} catch (QiniuException ex) {
			System.out.println(ex.code());
			System.out.println(ex.response.error);
		}
	}

	public void simpleSimpleUploadWithPfop(String saveKey, String fopResultKey) {
		Auth auth = Auth.create(LabConfig.ACCESS_KEY, LabConfig.SECRET_KEY);
		StringMap policy = new StringMap();
		// 限制文件的上传大小
		int fsizeLimit = 4 * 1024 * 1024;// 4MB，可以限定客户端上传的文件大小不超过4MB
		policy.put("fsizeLimit", new Integer(fsizeLimit));
		// 业务服务器指定上传的文件名称
		policy.put("saveKey", saveKey);

		policy.put("callbackBodyType", "application/json");
		policy.put("callbackUrl", "http://www.abc.com/common/video/callback/0");
		policy.put(
				"callbackBody",
				"{\"fname\":\"$(fname)\",\"hash\":\"$(hash)\",\"key\":\"$(key)\",\"bucket\":\"$(bucket)\"}");

		policy.put(
				"persistentOps",
				"avthumb/mp4/vb/128k|saveas/"
						+ UrlSafeBase64.encodeToString(LabConfig.PUBLIC_BUCKET
								+ ":" + fopResultKey));
		policy.put("persistentNotifyUrl",
				"http://www.abc.com//common/video/persistent_notify/0");

		long expires = 3600;// 3600秒＝1小时
		String token = auth.uploadToken(LabConfig.PUBLIC_BUCKET, null, expires,
				policy);
		System.out.println(token);
		UploadManager um = new UploadManager();
		try {
			Response resp = um.put("/Users/jemy/Documents/qiniu.mp4", null,
					token);
			System.out.println(resp.bodyString());
		} catch (QiniuException ex) {
			System.out.println(ex.code());
			System.out.println(ex.response.error);
		}
	}

	//使用文件的hash值作为文件名并且设置扩展名
	public void simpleUploadWithSaveKey() {
		Auth auth = Auth.create(LabConfig.ACCESS_KEY, LabConfig.SECRET_KEY);

		StringMap putPolicy = new StringMap();
		putPolicy.put("saveKey", "$(etag).png");
		putPolicy
				.put("returnBody",
						"{\"hash\":\"$(hash)\",\"key\":\"$(key)\",\"bucket\":\"$(bucket)\"");
		String token = auth.uploadToken(LabConfig.PUBLIC_BUCKET, null, 3600,
				putPolicy);
		UploadManager um = new UploadManager();
		try {
			Response resp = um.put("/Users/jemy/Documents/qiniu.png", null,
					token, null, null, true);
			System.out.println(resp.bodyString());
		} catch (QiniuException ex) {
			System.out.println(ex.code());
			System.out.println(ex.response.error);
		}
	}

	public static void main(String[] args) throws QiniuException {
		SimpleUpload uploader = new SimpleUpload();
		uploader.simpleUploadWithSaveKey();
	}

}
