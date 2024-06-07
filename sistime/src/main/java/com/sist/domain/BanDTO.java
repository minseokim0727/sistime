package com.sist.domain;

public class BanDTO {

	private long ban_num;
	private int ban_state;
	private String ban_date;
	private String ban_reason;

	public long getBan_num() {
		return ban_num;
	}

	public void setBan_num(long ban_num) {
		this.ban_num = ban_num;
	}

	public int getBan_state() {
		return ban_state;
	}

	public void setBan_state(int ban_state) {
		this.ban_state = ban_state;
	}

	public String getBan_date() {
		return ban_date;
	}

	public void setBan_date(String ban_date) {
		this.ban_date = ban_date;
	}

	public String getBan_reason() {
		return ban_reason;
	}

	public void setBan_reason(String ban_reason) {
		this.ban_reason = ban_reason;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	private String email;
}
