package com.guardresourcemanager.genesis.guardresourcemanager.model;

import java.text.DateFormat;

import com.google.gson.annotations.SerializedName;

public class Guard {

	@SerializedName("name")
	private String name;
	@SerializedName("token_no")
	private String tokenNumber;
	@SerializedName("guard_id")
	private String guardID;
	@SerializedName("intm_flag")
	private boolean isGuardCheckedin = false;
	private boolean isSelected = false;
	private boolean isReplacable = false;
	private String dateTime;
	private String progressStatus = Util.DEFAULT_VIEW;

	public Guard(String name, String tokenNumber) {
		this.name = name;
		this.tokenNumber = tokenNumber;
	}

	public boolean isGuardCheckedin() {
		return isGuardCheckedin;
	}

	public void setGuardCheckedin(boolean guardCheckedin) {
		isGuardCheckedin = guardCheckedin;
	}

	public String getProgressStatus() {
		return progressStatus;
	}

	public void setProgressStatus(String progressStatus) {
		this.progressStatus = progressStatus;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public boolean isReplacable() {
		return isReplacable;
	}

	public void setReplacable(boolean replacable) {
		isReplacable = replacable;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean selected) {
		isSelected = selected;
	}

	public String getGuardID() {
		return guardID;
	}

	public void setGuardID(String guardID) {
		this.guardID = guardID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTokenNumber() {
		return tokenNumber;
	}

	public void setTokenNumber(String tokenNumber) {
		this.tokenNumber = tokenNumber;
	}
}
