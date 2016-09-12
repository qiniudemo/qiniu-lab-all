package com.qiniulab.cdn;

public class CdnPrefetchResult {
	// 回复状态码
	private int code;
	// 回复信息
	private String error;
	// 请求ID
	private String requestId;
	// 提交失败的链接
	private String[] invalidUrls;
	// 每天的预取配额次数
	private int quotaDay;
	// 每天的预取剩余次数
	private int surplusDay;

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

	public int getQuotaDay() {
		return quotaDay;
	}

	public void setQuotaDay(int quotaDay) {
		this.quotaDay = quotaDay;
	}

	public int getSurplusDay() {
		return surplusDay;
	}

	public void setSurplusDay(int surplusDay) {
		this.surplusDay = surplusDay;
	}

}
