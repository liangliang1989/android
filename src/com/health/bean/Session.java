package com.health.bean;

/**
 * session����
 * 
 * @author jiqunpeng
 * 
 *         ����ʱ�䣺2014-3-18 ����11:37:46
 */
public class Session {
	private String id;
	private long time;

	public Session(String id, long time) {
		super();
		this.id = id;
		this.time = time;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
