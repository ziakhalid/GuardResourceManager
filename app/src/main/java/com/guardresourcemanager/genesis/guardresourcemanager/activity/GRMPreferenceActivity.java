package com.guardresourcemanager.genesis.guardresourcemanager.activity;


import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.guardresourcemanager.genesis.guardresourcemanager.R;
import com.guardresourcemanager.genesis.guardresourcemanager.fragments.GRMPreferenceFragment;

public class GRMPreferenceActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preference);

		ActionBar ab = getSupportActionBar();
		if (ab != null) {
			ab.setDisplayHomeAsUpEnabled(true);
		}

		getFragmentManager()
			.beginTransaction()
			.replace(R.id.fragment_container, new GRMPreferenceFragment())
			.commit();
	}


	@Override
	public void setTitle(CharSequence title) {
		ActionBar ab = getSupportActionBar();
		if (ab != null) {
			ab.setTitle(title);
		}
		else {
			super.setTitle(title);
		}
	}


	@Override
	public void onBackPressed() {
//		Log.e("onBackPressed. back stack = " + getFragmentManager().getBackStackEntryCount());
		FragmentManager fragmentManager = getFragmentManager();
		if (fragmentManager.getBackStackEntryCount() > 1) {
			fragmentManager.popBackStack();
		}
		else {
			super.onBackPressed();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
