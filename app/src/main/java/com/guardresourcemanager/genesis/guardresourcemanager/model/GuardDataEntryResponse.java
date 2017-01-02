package com.guardresourcemanager.genesis.guardresourcemanager.model;

import com.google.gson.annotations.SerializedName;

public class GuardDataEntryResponse {

	@SerializedName("response")
	private String response;

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}
}
