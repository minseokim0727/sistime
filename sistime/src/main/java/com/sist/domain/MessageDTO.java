package com.sist.domain;

public class MessageDTO {
	private int msg_num;
	private String content;
	private String send_email;
	private String read_email;
	private String send_date;
	private String read_date;
	
	public int getMsg_num() {
		return msg_num;
	}
	public void setMsg_num(int msg_num) {
		this.msg_num = msg_num;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getSend_email() {
		return send_email;
	}
	public void setSend_email(String send_email) {
		this.send_email = send_email;
	}
	public String getRead_email() {
		return read_email;
	}
	public void setRead_email(String read_email) {
		this.read_email = read_email;
	}
	public String getSend_date() {
		return send_date;
	}
	public void setSend_date(String send_date) {
		this.send_date = send_date;
	}
	public String getRead_date() {
		return read_date;
	}
	public void setRead_date(String read_date) {
		this.read_date = read_date;
	}
	

}
