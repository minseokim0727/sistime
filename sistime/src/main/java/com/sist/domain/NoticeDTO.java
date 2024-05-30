package com.sist.domain;

public class NoticeDTO {
    private long notice_num;
    private String subject;
    private String content;
    private String reg_date;
    private String saveFilename;
	private String originalFilename;
	private String email;
	private long file_num;
	public long getFile_num() {
		return file_num;
	}
	public void setFile_num(long file_num) {
		this.file_num = file_num;
	}
	public long getNotice_num() {
		return notice_num;
	}
	public void setNotice_num(long notice_num) {
		this.notice_num = notice_num;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
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
	public String getSaveFilename() {
		return saveFilename;
	}
	public void setSaveFilename(String saveFilename) {
		this.saveFilename = saveFilename;
	}
	public String getOriginalFilename() {
		return originalFilename;
	}
	public void setOriginalFilename(String originalFilename) {
		this.originalFilename = originalFilename;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
}