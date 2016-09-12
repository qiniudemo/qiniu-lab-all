package com.qiniulab.cdn;

import com.qiniu.common.QiniuException;

public class RefreshExample {
	public static void main(String[] args) {
		// ak,sk从 https://portal.qiniu.com/user/key 获取
		String ak = "";
		String sk = "";
		FusionCdn cdn = new FusionCdn(ak, sk);

		// 刷新例子
		String[] urlsToRefresh = new String[] { "http://if-pbl.qiniudn.com/golang.png",
				"http://if-pbl.qiniudn.com/test/golang.png" };
		CdnRefreshResult refreshResult;
		try {
			refreshResult = cdn.refresh(urlsToRefresh, null);
			System.out.println(refreshResult.getCode() + "," + refreshResult.getError());
		} catch (QiniuException e) {
			if (e.response != null) {
				System.err.println(e.response.toString());
			}
		}
	}
}
