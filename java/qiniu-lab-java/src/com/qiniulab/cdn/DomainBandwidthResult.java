package com.qiniulab.cdn;

class DomainBandwidth {
	// 时间戳
	private long time;
	// 带宽，单位bit
	private long bandwidth;

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public long getBandwidth() {
		return bandwidth;
	}

	public void setBandwidth(long bandwidth) {
		this.bandwidth = bandwidth;
	}

}

public class DomainBandwidthResult {
	// 回复状态码
	private int code;
	// 回复信息
	private String error;
	// 回复的带宽数据列表
	private DomainBandwidth[] data;

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

	public DomainBandwidth[] getData() {
		return data;
	}

	public void setData(DomainBandwidth[] data) {
		this.data = data;
	}

}
