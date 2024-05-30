package com.sist.domain;

public class SessionInfo {
	private String email;
	private String userName;
	private int userRoll;
	
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getUserRoll() {
		return userRoll;
	}
	public void setUserRoll(int userRoll) {
		this.userRoll = userRoll;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}
