package com.sist.domain;



public class QnaDTO {
	private long qna_num;
	private String title;
	private String content;
	private String reg_date;
	private int secret;
	private String email;
	private String answer_content;
	private String answer_reg_date;
	public long getQna_num() {
		return qna_num;
	}
	public void setQna_num(long qna_num) {
		this.qna_num = qna_num;
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
	public String getReg_date() {
		return reg_date;
	}
	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}
	public int getSecret() {
		return secret;
	}
	public void setSecret(int secret) {
		this.secret = secret;
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
	
	
}
