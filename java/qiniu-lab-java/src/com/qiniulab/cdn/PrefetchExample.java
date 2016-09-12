package com.qiniulab.cdn;

import com.qiniu.common.QiniuException;

public class PrefetchExample {
	public static void main() {
		//ak,sk从 https://portal.qiniu.com/user/key 获取
		String ak = "";
		String sk = "";
		FusionCdn cdn = new FusionCdn(ak, sk);

		// 预取例子
		String[] urlsToPrefetch = new String[] { "http://if-pbl.qiniudn.com/golang.png",
				"http://if-pbl.qiniudn.com/test/golang.png" };
		CdnPrefetchResult prefetchResult;
		try {
			prefetchResult = cdn.prefetch(urlsToPrefetch);
			System.out.println(prefetchResult.getCode() + "," + prefetchResult.getError());
		} catch (QiniuException e) {
			if (e.response != null) {
				System.err.println(e.response.toString());
			}
		}
	}
}
