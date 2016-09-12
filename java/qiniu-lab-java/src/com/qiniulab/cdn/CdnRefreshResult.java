package com.qiniulab.cdn;

public class CdnRefreshResult {
	// 回复状态码
	private int code;
	// 回复信息
	private String error;
	// 请求ID
	private String requestId;
	// 提交失败的链接
	private String[] invalidUrls;
	// 提交失败的目录
	private String[] invalidDirs;
	// 链接刷新配额次数
	private int urlQuotaDay;
	// 链接刷新剩余次数
	private int urlSurplusDay;
	// 目录刷新配额次数
	private int dirQuotaDay;
	// 目录刷新剩余次数
	private int dirSurplusDay;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String[] getInvalidUrls() {
		return invalidUrls;
	}

	public void setInvalidUrls(String[] invalidUrls) {
		this.invalidUrls = invalidUrls;
	}

	public String[] getInvalidDirs() {
		return invalidDirs;
	}

	public void setInvalidDirs(String[] invalidDirs) {
		this.invalidDirs = invalidDirs;
	}

	public int getUrlQuotaDay() {
		return urlQuotaDay;
	}

	public void setUrlQuotaDay(int urlQuotaDay) {
		this.urlQuotaDay = urlQuotaDay;
	}

	public int getUrlSurplusDay() {
		return urlSurplusDay;
	}

	public void setUrlSurplusDay(int urlSurplusDay) {
		this.urlSurplusDay = urlSurplusDay;
	}

	public int getDirQuotaDay() {
		return dirQuotaDay;
	}

	public void setDirQuotaDay(int dirQuotaDay) {
		this.dirQuotaDay = dirQuotaDay;
	}

	public int getDirSurplusDay() {
		return dirSurplusDay;
	}

	public void setDirSurplusDay(int dirSurplusDay) {
		this.dirSurplusDay = dirSurplusDay;
	}

}
