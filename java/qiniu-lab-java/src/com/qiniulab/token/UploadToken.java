package com.qiniulab.token;

import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.qiniu.util.UrlSafeBase64;
import com.qiniulab.config.LabConfig;

/**
 * 该例子演示如何使用七牛最新的java sdk来生成各种上传凭证
 * 文档：http://developer.qiniu.com/article/developer/security/put-policy.html
 * 
 * 这里利用到不同参数生成相应上传凭证的例子中的参数根据需要是可以自由组合的。
 * 
 * 备注：公开空间和私有空间的区别在于下载的时候访问外链不同，对于上传部分，这两个类型的空间行为一致。
 */
public class UploadToken {
	/**
	 * 生成无key文件的上传凭证
	 */
	public static void createSimpleUploadWithoutKeyToken() {
		Auth auth = Auth.create(LabConfig.ACCESS_KEY, LabConfig.SECRET_KEY);
		String uptoken = auth.uploadToken(LabConfig.PUBLIC_BUCKET);
		System.out.println(uptoken);
	}

	/**
	 * 生成有key文件的上传凭证(其实和无key文件一样)
	 */
	public static void createSimpleUploadWithKeyToken() {
		Auth auth = Auth.create(LabConfig.ACCESS_KEY, LabConfig.SECRET_KEY);
		String uptoken = auth.uploadToken(LabConfig.PUBLIC_BUCKET);
		System.out.println(uptoken);
	}

	/**
	 * 生成覆盖空间已有文件的上传凭证
	 * 
	 * @param key
	 *            空间中已有的文件名称
	 */
	public static void createOverwriteUploadToken(String key) {
		Auth auth = Auth.create(LabConfig.ACCESS_KEY, LabConfig.SECRET_KEY);
		String uptoken = auth.uploadToken(LabConfig.PUBLIC_BUCKET, key);
		System.out.println(uptoken);
	}

	/**
	 * 业务服务器指定上传的文件名称，这种情况下客户端的代码就可以不指定上传的文件名（key）了
	 */
	public static void createSimpleUploadWithSaveKeyToken() {
		Auth auth = Auth.create(LabConfig.ACCESS_KEY, LabConfig.SECRET_KEY);
		StringMap policy = new StringMap();
		policy.put("saveKey", "camera_upload/2015/03/12/file100.mp4");
		long expires = 3600;// 3600秒＝1小时
		String uptoken = auth.uploadToken(LabConfig.PUBLIC_BUCKET, null, expires, policy);
		System.out.println(uptoken);
	}

	/**
	 * 带上传回调的凭证，这种情况下，文件上传到七牛之后，七牛会以POST的方式回调客户的业务服务器，
	 * 回调内容就是callbackBody的内容，这个callbackBody里面的相关系统变量会被默认填充正确的值，然后去回调。
	 */
	public static void createCallbackUploadWithJsonBody() {
		Auth auth = Auth.create(LabConfig.ACCESS_KEY, LabConfig.SECRET_KEY);
		long expires = 3600;// 3600秒＝1小时
		StringMap putPolicy = new StringMap();
		putPolicy.put("callbackUrl", "http://d2.qiniu.com/fake/callback");
		putPolicy.put("callbackBody",
				"{\"hash\":\"$(hash)\",\"key\":\"$(key)\",\"bucket\":\"$(bucket)\",\"hello\":\"$(x:hello)\"}");
		putPolicy.put("callabckBodyType", "application/json");
		String uptoken = auth.uploadToken(LabConfig.PUBLIC_BUCKET, null, expires, null);
		System.out.println(uptoken);
	}

	/**
	 * 带上传回调的凭证，这种情况下，文件上传到七牛之后，七牛会以POST的方式回调客户的业务服务器，
	 * 回调内容就是callbackBody的内容，这个callbackBody里面的相关系统变量会被默认填充正确的值，然后去回调。
	 */
	public static void createCallbackUploadWithUrlBody() {
		Auth auth = Auth.create(LabConfig.ACCESS_KEY, LabConfig.SECRET_KEY);
		long expires = 3600;// 3600秒＝1小时
		StringMap putPolicy = new StringMap();
		putPolicy.put("callbackUrl", "http://d2.qiniu.com/fake/callback");
		putPolicy.put("callbackBody", "hash=$(hash)&key=$(key)&bucket=$(bucket)&hello=$(x:hello)");
		String uptoken = auth.uploadToken(LabConfig.PUBLIC_BUCKET, null, expires, null);
		System.out.println(uptoken);
	}

	/**
	 * 带持久化处理的上传凭证，如果需要对上传的文件在到达七牛之后立即触发转码操作，那么可以使用这种方式 生成对应的上传凭证
	 */
	public static void createSimpleUploadWithPfop() {
		Auth auth = Auth.create(LabConfig.ACCESS_KEY, LabConfig.SECRET_KEY);
		StringMap policy = new StringMap();

		// 转码和截图结果，需要手动指定名称，否则生成的文件名是随机的，不方便管理
		String mp4SaveEntry = UrlSafeBase64
				.encodeToString(String.format("%s:%s", LabConfig.PUBLIC_BUCKET, "demo_mp4_result.mp4"));
		String pngSaveEntry = UrlSafeBase64
				.encodeToString(String.format("%s:%s", LabConfig.PUBLIC_BUCKET, "demo_snapshot_result.png"));

		policy.put("persistentOps",
				String.format("avthumb/mp4|saveas/%s;vframe/png/offset/0|saveas/%s", mp4SaveEntry, pngSaveEntry));
		policy.put("persistentNotifyUrl", "http://api.abc.com/qiniuFopNotify");

		// 私有队列名称，处理速度有保障，可以到后台创建 https://portal.qiniu.com/create/mps
		policy.put("persistentPipeline", "p1");

		long expires = 3600;// 3600秒＝1小时
		String uptoken = auth.uploadToken(LabConfig.PUBLIC_BUCKET, null, expires, policy);
		System.out.println(uptoken);
	}

	/**
	 * 限制文件的上传大小，不过这个是等文件到达七牛之后才去检查的，所以如果客户端可以限制，可以客户端先限制， 这里的限制是最后的一道保险。
	 */
	public static void createSimpleUploadWithFsizeLimit() {
		Auth auth = Auth.create(LabConfig.ACCESS_KEY, LabConfig.SECRET_KEY);
		StringMap policy = new StringMap();
		int fsizeLimit = 4 * 1024 * 1024;// 4MB，可以限定客户端上传的文件大小不超过4MB
		// 这里的fsizeLimit注意是Integer，不要指定为String
		policy.put("fsizeLimit", new Integer(fsizeLimit));

		long expires = 3600;// 3600秒＝1小时
		String uptoken = auth.uploadToken(LabConfig.PUBLIC_BUCKET, null, expires, policy);
		System.out.println(uptoken);
	}

	/**
	 * 限定文件的上传类型，和上面一样，也是文件到达七牛之后采取检查的，所以如果客户端可以限制，可以客户端先限制， 这里的限制是最后的一道保险。
	 */
	public static void createSimpleUploadWithMimeLimit() {
		Auth auth = Auth.create(LabConfig.ACCESS_KEY, LabConfig.SECRET_KEY);
		StringMap policy = new StringMap();
		policy.put("mimeLimit", "image/png;image/jpeg");

		long expires = 3600;// 3600秒＝1小时
		String uptoken = auth.uploadToken(LabConfig.PUBLIC_BUCKET, null, expires, policy);
		System.out.println(uptoken);
	}

	public static void main(String[] args) {

	}

}
