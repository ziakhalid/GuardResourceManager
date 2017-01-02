package com.guardresourcemanager.genesis.guardresourcemanager.model;

import com.google.gson.annotations.SerializedName;

public class Panic {

	@SerializedName("ID")
	private String alertID;
	@SerializedName("Priority")
	private String priority;
	@SerializedName("ALert_Name")
	private String alertName;

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getAlertName() {
		return alertName;
	}

	public void setAlertName(String alertName) {
		this.alertName = alertName;
	}

	public String getAlertID() {
		return alertID;
	}

	public void setAlertID(String alertID) {
		this.alertID = alertID;
	}
}
