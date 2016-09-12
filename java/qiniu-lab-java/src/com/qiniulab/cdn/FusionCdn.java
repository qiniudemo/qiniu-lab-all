package com.qiniulab.cdn;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Client;
import com.qiniu.http.Response;
import com.qiniu.util.Auth;
import com.qiniu.util.Json;
import com.qiniu.util.StringMap;

/**
 * Fusion Cdn 提供的功能
 * 
 * <ol>
 * <li>刷新链接和目录</li>
 * <li>预取资源</li>
 * <li>基于时间戳的防盗链</li>
 * <li>获取流量数据</li>
 * <li>获取带宽数据</li>
 * </ol>
 */

public class FusionCdn {
	private Auth auth;

	public FusionCdn(String accessKey, String secretKey) {
		this.auth = Auth.create(accessKey, secretKey);
	}

	/**
	 * 刷新目录或者链接列表，注意目录必须以 / 结尾
	 * 
	 * @param urls
	 *            待刷新的链接列表
	 * @param dirs
	 *            待刷新的目录列表
	 * @throws QiniuException
	 */

	public CdnRefreshResult refresh(String[] urls, String[] dirs) throws QiniuException {
		CdnRefreshResult refreshResult = null;

		String refreshAPI = "http://fusion.qiniuapi.com/refresh";
		Client client = new Client();
		StringMap map = new StringMap();
		map.put("urls", urls);
		map.put("dirs", dirs);
		String postBody = Json.encode(map);
		String accessToken = String.format("QBox %s", this.auth.signRequest(refreshAPI, null, null));
		StringMap headers = new StringMap();
		headers.put("Authorization", accessToken);
		try {
			Response resp = client.post(refreshAPI, postBody.getBytes("UTF-8"), headers, Client.JsonMime);
			refreshResult = resp.jsonToObject(CdnRefreshResult.class);
		} catch (UnsupportedEncodingException e) {
		}

		return refreshResult;
	}

	/**
	 * 根据资源访问外链进行预取操作，不支持预取目录
	 * 
	 * @param urls
	 *            待预取的链接列表
	 * @throws QiniuException
	 */

	public CdnPrefetchResult prefetch(String[] urls) throws QiniuException {
		CdnPrefetchResult prefetchResult = null;

		String refreshAPI = "http://fusion.qiniuapi.com/prefetch";
		Client client = new Client();
		StringMap map = new StringMap();
		map.put("urls", urls);
		String postBody = Json.encode(map);
		String accessToken = String.format("QBox %s", this.auth.signRequest(refreshAPI, null, null));
		StringMap headers = new StringMap();
		headers.put("Authorization", accessToken);
		try {
			Response resp = client.post(refreshAPI, postBody.getBytes("UTF-8"), headers, Client.JsonMime);
			prefetchResult = resp.jsonToObject(CdnPrefetchResult.class);
		} catch (UnsupportedEncodingException e) {
		}

		return prefetchResult;
	}

	/**
	 * @param domain
	 *            需要获取带宽的域名
	 * @param startTime
	 *            起始时间
	 * @param endTime
	 *            结束时间
	 */
	public DomainBandwidthResult getDomainBandwidth(String domain, String startTime, String endTime)
			throws QiniuException {
		DomainBandwidthResult bandwidthResult;
		String reqUrl = null;
		try {
			reqUrl = String.format("http://fusion.qiniuapi.com/domain/bandwidth?domain=%s&startTime=%s&endTime=%s",
					URLEncoder.encode(domain, "utf-8"), URLEncoder.encode(startTime, "utf-8"),
					URLEncoder.encode(endTime, "utf-8"));
		} catch (UnsupportedEncodingException e1) {
		}

		Client client = new Client();
		StringMap map = new StringMap();
		map.put("Content-Type", Client.FormMime);
		String accessToken = String.format("QBox %s", this.auth.signRequest(reqUrl, null, null));
		StringMap headers = new StringMap();
		headers.put("Authorization", accessToken);
		Response resp = client.get(reqUrl, headers);
		bandwidthResult = resp.jsonToObject(DomainBandwidthResult.class);
		return bandwidthResult;
	}

	/**
	 * @param domain
	 *            需要获取流量的域名
	 * @param startTime
	 *            起始时间
	 * @param endTime
	 *            结束时间
	 */
	public DomainFlowResult getDomainFlow(String domain, String startTime, String endTime) throws QiniuException {
		DomainFlowResult flowResult;
		String reqUrl = null;
		try {
			reqUrl = String.format("http://fusion.qiniuapi.com/domain/traffic?domain=%s&startTime=%s&endTime=%s",
					URLEncoder.encode(domain, "utf-8"), URLEncoder.encode(startTime, "utf-8"),
					URLEncoder.encode(endTime, "utf-8"));
		} catch (UnsupportedEncodingException e1) {
		}

		Client client = new Client();
		StringMap map = new StringMap();
		map.put("Content-Type", Client.FormMime);
		String accessToken = String.format("QBox %s", this.auth.signRequest(reqUrl, null, null));
		StringMap headers = new StringMap();
		headers.put("Authorization", accessToken);
		Response resp = client.get(reqUrl, headers);
		flowResult = resp.jsonToObject(DomainFlowResult.class);
		return flowResult;
	}
}
