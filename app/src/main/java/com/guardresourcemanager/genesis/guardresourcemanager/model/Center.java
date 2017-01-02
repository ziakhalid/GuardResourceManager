package com.guardresourcemanager.genesis.guardresourcemanager.model;

import com.google.gson.annotations.SerializedName;

public class Center {

	@SerializedName("name")
	private String centerName = "GRM";
	@SerializedName("center_id")
	private String centerId;

	public String getCenterName() {
		return centerName;
	}

	public void setCenterName(String centerName) {
		this.centerName = centerName;
	}

	public String getCenterId() {
		return centerId;
	}

	public void setCenterId(String centerId) {
		this.centerId = centerId;
	}
}
