package com.guardresourcemanager.genesis.guardresourcemanager.activity;

import java.util.concurrent.TimeUnit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.crashlytics.android.Crashlytics;
import com.guardresourcemanager.genesis.guardresourcemanager.R;

import io.fabric.sdk.android.Fabric;
import rx.Observable;
import timber.log.Timber;

public class LauncherActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launcher);
		Fabric.with(this, new Crashlytics());
		Long timeBeforeActivityStarts = 2l;
		Observable<Long> timerObservable = Observable.timer(timeBeforeActivityStarts, TimeUnit.SECONDS);
		timerObservable.subscribe(aLong -> {
			Timber.d("Launching New Activity");
			Intent intent = new Intent(LauncherActivity.this, GuardHomeActivity.class);
			startActivity(intent);
			finish();
		});
	}
}
