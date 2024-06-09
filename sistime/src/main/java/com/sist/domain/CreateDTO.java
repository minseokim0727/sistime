package com.sist.domain;

public class CreateDTO {
	private int BOARD_ID;
	private String BOARD_NAME;
	private String BOARD_DESCRIPTION;
	
	public int getBOARD_ID() {
		return BOARD_ID;
	}
	public void setBOARD_ID(int bOARD_ID) {
		BOARD_ID = bOARD_ID;
	}
	public String getBOARD_NAME() {
		return BOARD_NAME;
	}
	public void setBOARD_NAME(String bOARD_NAME) {
		BOARD_NAME = bOARD_NAME;
	}
	public String getBOARD_DESCRIPTION() {
		return BOARD_DESCRIPTION;
	}
	public void setBOARD_DESCRIPTION(String bOARD_DESCRIPTION) {
		BOARD_DESCRIPTION = bOARD_DESCRIPTION;
	}
}
