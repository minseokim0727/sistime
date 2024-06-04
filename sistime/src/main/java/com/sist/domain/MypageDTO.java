package com.sist.domain;

public class MypageDTO {
	// 내가 쓴 글
	private String board_name;
	private String title;
	private String content;
	
	// 내가 쓴 댓글
	private String reply_content;
	private long board_num;
	private String reply_board_name;
	
	public String getReply_board_name() {
		return reply_board_name;
	}
	public void setReply_board_name(String reply_board_name) {
		this.reply_board_name = reply_board_name;
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
