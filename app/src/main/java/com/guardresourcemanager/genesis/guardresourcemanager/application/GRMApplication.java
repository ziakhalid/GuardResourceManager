package com.guardresourcemanager.genesis.guardresourcemanager.application;

import android.app.Application;

import com.guardresourcemanager.genesis.guardresourcemanager.BuildConfig;

import timber.log.Timber;

public class GRMApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		if (BuildConfig.DEBUG){
			Timber.plant(new Timber.DebugTree());
		}
	}
}
