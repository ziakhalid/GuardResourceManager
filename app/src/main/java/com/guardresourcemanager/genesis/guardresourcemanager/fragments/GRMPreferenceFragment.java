package com.guardresourcemanager.genesis.guardresourcemanager.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.guardresourcemanager.genesis.guardresourcemanager.R;

public class GRMPreferenceFragment extends BasePreferenceFragment {

	@Override
	public void onStart() {
		super.onStart();
		getActivity().setTitle(R.string.Settings);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences_dev);
	}
}
