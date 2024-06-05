package com.sist.domain;

public class MypageDTO {
	// 내가 쓴 글
	private String board_name;
	private String title;
	private String content;
	private String reg_date;
	
	// 내가 쓴 댓글
	private String reply_content;
	private long board_num;
	private String reply_reg_date;
	
	
	public String getReg_date() {
		return reg_date;
	}
	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}
	public String getReply_reg_date() {
		return reply_reg_date;
	}
	public void setReply_reg_date(String reply_reg_date) {
		this.reply_reg_date = reply_reg_date;
	}
	
	public String getReply_content() {
		return reply_content;
	}
	public void setReply_content(String reply_content) {
		this.reply_content = reply_content;
	}
	public long getBoard_num() {
		return board_num;
	}
	public void setBoard_num(long board_num) {
		this.board_num = board_num;
	}
	public String getBoard_name() {
		return board_name;
	}
	public void setBoard_name(String board_name) {
		this.board_name = board_name;
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
	
}
