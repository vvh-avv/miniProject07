package com.model2.mvc.service.domain;

import java.sql.Date;


public class QuitUser {
	
	///Field
	private String userId;
	private String reason;
	private Date quitDate;
	
	///Constructor
	public QuitUser(){
	}
	
	///Method 
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Date getQuitDate() {
		return quitDate;
	}

	public void setQuitDate(Date quitDate) {
		this.quitDate = quitDate;
	}
	
	@Override
	public String toString() {
		return "QuitUserVO : [userId] "+userId+" [reason] "+reason+" [quitDate] "+quitDate;
	}

}