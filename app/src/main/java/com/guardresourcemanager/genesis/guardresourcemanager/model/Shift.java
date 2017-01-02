package com.guardresourcemanager.genesis.guardresourcemanager.model;

import com.google.gson.annotations.SerializedName;

public class Shift {

	@SerializedName("shiftname")
	private String shiftName;
	@SerializedName("shiftno")
	private String shiftNumber;

	public String getShiftName() {
		return shiftName;
	}

	public void setShiftName(String shiftName) {
		this.shiftName = shiftName;
	}

	public String getShiftNumber() {
		return shiftNumber;
	}

	public void setShiftNumber(String shiftNumber) {
		this.shiftNumber = shiftNumber;
	}
}
