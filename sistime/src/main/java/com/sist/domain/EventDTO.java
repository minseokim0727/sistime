package com.sist.domain;

public class EventDTO {

		private long eventpage_num;
		private String event_title;
		private String event_content;
		private String start_date;
		private String end_date;
		private int hitccount;
		private String saveFilename;
		private String originalFilename;
		private String reg_date;
		private String email;
		private long filesize;
		private int likeCount;
		
		public int getLikeCount() {
			return likeCount;
		}
		public void setLikeCount(int likeCount) {
			this.likeCount = likeCount;
		}
		public long getFilesize() {
			return filesize;
		}
		public void setFilesize(long filesize) {
			this.filesize = filesize;
		}
		private long gap;
		public long getGap() {
			return gap;
		}
		public void setGap(long gap) {
			this.gap = gap;
		}
		
		public long getEventpage_num() {
			return eventpage_num;
		}
		public void setEventpage_num(long eventpage_num) {
			this.eventpage_num = eventpage_num;
		}
		public String getEvent_title() {
			return event_title;
		}
		public void setEvent_title(String event_title) {
			this.event_title = event_title;
		}
		public String getEvent_content() {
			return event_content;
		}
		public void setEvent_content(String event_content) {
			this.event_content = event_content;
		}
		public String getStart_date() {
			return start_date;
		}
		public void setStart_date(String start_date) {
			this.start_date = start_date;
		}
		public String getEnd_date() {
			return end_date;
		}
		public void setEnd_date(String end_date) {
			this.end_date = end_date;
		}
		public int getHitccount() {
			return hitccount;
		}
		public void setHitccount(int hitccount) {
			this.hitccount = hitccount;
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
		public String getReg_date() {
			return reg_date;
		}
		public void setReg_date(String reg_date) {
			this.reg_date = reg_date;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		
		
	
}
