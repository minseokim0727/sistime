package com.sist.domain;

public class EventReplyDTO {
	private long reply_num;
	private String content;
	private String reg_date;
	private long up_reply;
	private long EVENTPAGE_NUM;
	private String email;
	private long num;
	public long getNum() {
		return num;
	}
	public void setNum(long num) {
		this.num = num;
	}
	private long answer;
	
	public long getAnswer() {
		return answer;
	}
	public void setAnswer(long answer) {
		this.answer = answer;
	}
	private String nickname;
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public int getAnswerCount() {
		return answerCount;
	}
	public void setAnswerCount(int answerCount) {
		this.answerCount = answerCount;
	}
	public int getLikeCount() {
		return likeCount;
	}
	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}
	public int getDisLikeCount() {
		return disLikeCount;
	}
	public void setDisLikeCount(int disLikeCount) {
		this.disLikeCount = disLikeCount;
	}
	private int answerCount;
	private int likeCount;
	private int disLikeCount;
	
	public long getReply_num() {
		return reply_num;
	}
	public void setReply_num(long reply_num) {
		this.reply_num = reply_num;
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
	public long getUp_reply() {
		return up_reply;
	}
	public void setUp_reply(long up_reply) {
		this.up_reply = up_reply;
	}
	public long getEVENTPAGE_NUM() {
		return EVENTPAGE_NUM;
	}
	public void setEVENTPAGE_NUM(long eVENTPAGE_NUM) {
		EVENTPAGE_NUM = eVENTPAGE_NUM;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}
