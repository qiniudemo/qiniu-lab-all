package com.qiniulab.upload;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.qiniu.util.UrlSafeBase64;
import com.qiniulab.config.LabConfig;

public class VideoUploadWithFop {
	public static void videoUploadWithFop() {
		// ak,sk，请配置正确
		String accessKey = LabConfig.ACCESS_KEY;
		String secretKey = LabConfig.SECRET_KEY;
		Auth auth = Auth.create(accessKey, secretKey);
		StringMap policy = new StringMap();
		// 文件保存在七牛空间的名字
		String key = "qiniu.mp4";
		// 文件上传的空间
		String bucket = "if-pbl";
		// 水印图片
		String watermarkImage = "http://if-pbl.qiniudn.com/qiniu_wm.png";
		// 转码队列（转码队列名称！！！）
		String persistentPipeline = "jemy";
		// 截图指令
		String vframe1SaveKey = "qiniu_screenshot_1280x720.jpg";
		String vframeFop1 = "vframe/jpg/offset/0/w/1280/h/720|saveas/"
				+ UrlSafeBase64.encodeToString(bucket + ":" + vframe1SaveKey);

		String vframe2SaveKey = "qiniu_screenshot_480x320.jpg";
		String vframeFop2 = "vframe/jpg/offset/0/w/480/h/320|saveas/"
				+ UrlSafeBase64.encodeToString(bucket + ":" + vframe2SaveKey);

		// 视频转码
		String avthumb1SaveKey = "qiniu_1280x720.mp4";
		String avthumbFop1 = "avthumb/mp4/s/1280x720/wmImage/"
				+ UrlSafeBase64.encodeToString(watermarkImage) + "|saveas/"
				+ UrlSafeBase64.encodeToString(bucket + ":" + avthumb1SaveKey);

		String avthumb2SaveKey = "qiniu_480x320.mp4";
		String avthumbFop2 = "avthumb/mp4/s/480x320/wmImage/"
				+ UrlSafeBase64.encodeToString(watermarkImage) + "|saveas/"
				+ UrlSafeBase64.encodeToString(bucket + ":" + avthumb2SaveKey);

		// 组装fops
		StringBuilder fops = new StringBuilder();
		fops.append(vframeFop1).append(";").append(vframeFop2).append(";")
				.append(avthumbFop1).append(";").append(avthumbFop2);

		policy.put("persistentOps", fops.toString());
		policy.put("persistentPipeline", persistentPipeline);

		// 生成上传凭证
		int expires = 3600;// 默认1小时
		String uptoken = auth.uploadToken(bucket, key, expires, policy);
		// 上传文件逻辑，有可能在客户端
		String localFilePath = "/Users/jemy/Documents/qiniu.mp4";
		UploadManager uploadManager = new UploadManager();
		try {
			Response respInfo = uploadManager.put(localFilePath, key, uptoken);
			StringMap respMap = respInfo.jsonToMap();
			// 输出返回结果
			System.out.println(respMap.get("hash"));
			System.out.println(respMap.get("key"));
			System.out.println(respMap.get("persistentId"));
		} catch (QiniuException ex) {
			System.out.println(ex.code());
			System.out.println(ex.response.error);
		}
	}

	public static void main(String[] args) {
		videoUploadWithFop();
	}

}
