package com.sist.domain;

public class RequestBoardDTO {
	private long RB_num;
	private String title;
	private String content;
	private String reason;
	private int secret;
	private String reg_date;
	private String email;
	private String answer_content;
	private String answer_reg_date;
	private String nickname;
	public long getRB_num() {
		return RB_num;
	}
	public void setRB_num(long rB_num) {
		RB_num = rB_num;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public int getSecret() {
		return secret;
	}
	public void setSecret(int secret) {
		this.secret = secret;
	}
	public String getReg_date() {
		return reg_date;
	}
	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAnswer_content() {
		return answer_content;
	}
	public void setAnswer_content(String answer_content) {
		this.answer_content = answer_content;
	}
	public String getAnswer_reg_date() {
		return answer_reg_date;
	}
	public void setAnswer_reg_date(String answer_reg_date) {
		this.answer_reg_date = answer_reg_date;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	
}
