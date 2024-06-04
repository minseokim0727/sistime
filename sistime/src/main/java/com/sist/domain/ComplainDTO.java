package com.sist.domain;

public class ComplainDTO {
	private long comp_num;
	private String board_name;
	private long num;
	private String comp_reason;
	private String email;
	
	public long getComp_num() {
		return comp_num;
	}
	public void setComp_num(long comp_num) {
		this.comp_num = comp_num;
	}
	public String getBoard_name() {
		return board_name;
	}
	public void setBoard_name(String board_name) {
		this.board_name = board_name;
	}
	public long getNum() {
		return num;
	}
	public void setNum(long num) {
		this.num = num;
	}
	public String getComp_reason() {
		return comp_reason;
	}
	public void setComp_reason(String comp_reason) {
		this.comp_reason = comp_reason;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	

	

}
