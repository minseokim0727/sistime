package com.sist.domain;

public class Board_ReplyDTO {
	
	private long replynum;
	private String replycontent;
	private String B_replydate;
	private long board_num;
	private String email;
	private String userName;
	private String title;
	
	
	
	public long getReplynum() {
		return replynum;
	}
	public void setReplynum(long replynum) {
		this.replynum = replynum;
	}
	public String getReplycontent() {
		return replycontent;
	}
	public void setReplycontent(String replycontent) {
		this.replycontent = replycontent;
	}
	public String getB_replydate() {
		return B_replydate;
	}
	public void setB_replydate(String b_replydate) {
		B_replydate = b_replydate;
	}
	public long getBoard_num() {
		return board_num;
	}
	public void setBoard_num(long board_num) {
		this.board_num = board_num;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

}
