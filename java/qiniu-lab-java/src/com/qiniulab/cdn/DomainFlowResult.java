package com.qiniulab.cdn;

class DomainFlow {
	// 时间戳
	private long time;
	// 流量，单位bit
	private long flow;

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public long getFlow() {
		return flow;
	}

	public void setFlow(long flow) {
		this.flow = flow;
	}

}

public class DomainFlowResult {
	// 回复状态码
	private int code;
	// 回复信息
	private String error;
	// 回复的流量数据列表
	private DomainFlow[] data;

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

	public DomainFlow[] getData() {
		return data;
	}

	public void setData(DomainFlow[] data) {
		this.data = data;
	}

}
