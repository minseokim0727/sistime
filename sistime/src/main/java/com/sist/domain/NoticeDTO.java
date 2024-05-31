package com.sist.domain;

import java.util.List;

import com.sist.util.MyMultipartFile;

public class NoticeDTO {
    private long notice_num;
    private int notice;
    public int getNotice() {
		return notice;
	}
	public void setNotice(int notice) {
		this.notice = notice;
	}
	private String title;
    private String content;
    private String reg_date;
    private String saveFilename;
	private String originalFilename;
	private String email;
	private long noticefile_num;
	public long getNoticefile_num() {
		return noticefile_num;
	}
	public void setNoticefile_num(long noticefile_num) {
		this.noticefile_num = noticefile_num;
	}
	private long gap;
	public long getGap() {
		return gap;
	}
	public void setGap(long gap) {
		this.gap = gap;
	}
	private List<MyMultipartFile> listFile;
	
	public List<MyMultipartFile> getListFile() {
		return listFile;
	}
	public void setListFile(List<MyMultipartFile> listFile) {
		this.listFile = listFile;
	}
	
	public long getNotice_num() {
		return notice_num;
	}
	public void setNotice_num(long notice_num) {
		this.notice_num = notice_num;
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
