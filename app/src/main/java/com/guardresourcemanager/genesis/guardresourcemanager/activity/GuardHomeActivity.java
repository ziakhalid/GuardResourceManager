package com.guardresourcemanager.genesis.guardresourcemanager.activity;

import java.util.List;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.guardresourcemanager.genesis.guardresourcemanager.R;
import com.guardresourcemanager.genesis.guardresourcemanager.adapter.CustomSpinnerAdapter;
import com.guardresourcemanager.genesis.guardresourcemanager.fragments.GuardHomeFragment;
import com.guardresourcemanager.genesis.guardresourcemanager.fragments.GuardListFragment;
import com.guardresourcemanager.genesis.guardresourcemanager.fragments.MyDialogFragment;
import com.guardresourcemanager.genesis.guardresourcemanager.model.Center;
import com.guardresourcemanager.genesis.guardresourcemanager.model.GrmResponse;
import com.guardresourcemanager.genesis.guardresourcemanager.model.Guard;
import com.guardresourcemanager.genesis.guardresourcemanager.model.Panic;
import com.guardresourcemanager.genesis.guardresourcemanager.model.Shift;
import com.guardresourcemanager.genesis.guardresourcemanager.model.Util;
import com.guardresourcemanager.genesis.guardresourcemanager.rest.ApiClient;
import com.guardresourcemanager.genesis.guardresourcemanager.rest.ApiInterface;
import com.guardresourcemanager.genesis.guardresourcemanager.service.LocationService;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class GuardHomeActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

	private static final int REQUEST_READ_PHONE_STATE = 1;
	FragmentManager fragmentManager ;
	FragmentTransaction transaction;
	GuardHomeFragment guardHomeFragment;
	GuardListFragment guardListFragment;
	TelephonyManager telephonyManager;
	Toolbar toolbar;
	Spinner spinnerShiftType;
	private int mStackLevel = 0;
	private List<Panic> panicList = null;
	Subscription centerInfoSubscription;
	Subscription shiftInfoSubscription;
	Subscription guardListSubscription;
	Subscription panicListSubscription;
	Subscription sendPanicSubscription;
	Observable<Shift> selectedShiftObservable;
	ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
	Observer<Void> timeInButtonObserver = new Observer<Void>() {
		@Override
		public void onCompleted() {

		}

		@Override
		public void onError(Throwable e) {

		}

		@Override
		public void onNext(Void aVoid) {
			Timber.d("TimeIn button is clicked");
			showGuardListFragment();
			getGuardList(Util.TIME_IN);
		}
	};
	Observer<Void> timeOutButtonObserver = new Observer<Void>() {
		@Override
		public void onCompleted() {

		}

		@Override
		public void onError(Throwable e) {

		}

		@Override
		public void onNext(Void aVoid) {
			Timber.d("TimeOut button is clicked");
			showGuardListFragment();
			getGuardList(Util.TIME_OUT);
		}
	};
	Observer<Panic> panicButtonObserver = new Observer<Panic>() {
		@Override
		public void onCompleted() {

		}

		@Override
		public void onError(Throwable e) {

		}

		@Override
		public void onNext(Panic panic) {
			Timber.d("Panic Alert : " + panic.getAlertName());
			sendPanicAlert(panic);
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent i;
		switch (item.getItemId()) {
		case R.id.menu_alarm:
			if (panicList != null && panicList.size()>0){
				showDialog();
			}else {
				Util.showToastLong(this, "No Panic List Available");
			}

			break;

		case R.id.menu_settings:
			i = new Intent(this, GRMPreferenceActivity.class);
			startActivity(i);
			break;
		default:
		}
		return true;
	}

	@Override
	protected void onStart() {
		super.onStart();
		setNormalToolbar();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toast.makeText(GuardHomeActivity.this,"Beware !!! Device Tracking Started",Toast.LENGTH_LONG).show();
		telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

		int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
		if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat
				.requestPermissions(this, new String[] { Manifest.permission.READ_PHONE_STATE },
					REQUEST_READ_PHONE_STATE);
		}
		else {
			Util.setIMEI(telephonyManager.getDeviceId().toString());
		}

		initToolbar();
		initSpinner();
		setNormalToolbar();
		getPanicList();
		getCenterInfo();

		Intent i = new Intent(this, LocationService.class);
		startService(i);
Log.e("anu","service intent fired");

		fragmentManager = getSupportFragmentManager();
		guardHomeFragment = GuardHomeFragment
			.newInstance(GuardHomeActivity.this, timeInButtonObserver, timeOutButtonObserver);
		guardListFragment = GuardListFragment.newInstance(GuardHomeActivity.this);
		showGuardHomeFragment();
	}

	private void initToolbar() {
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		toolbar.showOverflowMenu();
	}

	private void initSpinner(){
		spinnerShiftType = (Spinner) findViewById(R.id.spinnerShiftType);
		spinnerShiftType.setVisibility(View.GONE);
	}

	public void setSpinnerToolbar() {
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		spinnerShiftType.setVisibility(View.VISIBLE);
		refreshActionBarMenu();
	}

	public void setNormalToolbar() {
		getSupportActionBar().setDisplayShowTitleEnabled(true);
		spinnerShiftType.setVisibility(View.GONE);
		toolbar.setTitle("GRM");
		refreshActionBarMenu();
	}

	private void addItemToSpinner(List<Shift> shiftInfoList) {

		if (shiftInfoList != null && shiftInfoList.size() > 0) {

			CustomSpinnerAdapter spinAdapter = new CustomSpinnerAdapter(
				getApplicationContext(), shiftInfoList);

			spinnerShiftType.setAdapter(spinAdapter);
			spinnerShiftType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> adapter, View v,
					int position, long id) {
					// On selecting a spinner item
					Shift shift = (Shift) adapter.getAdapter().getItem(position);
					Util.showToastLong(GuardHomeActivity.this, "Selected : "+shift.getShiftName().toUpperCase());
					selectedShiftObservable = Observable.create(new Observable.OnSubscribe<Shift>() {
						@Override
						public void call(Subscriber<? super Shift> subscriber) {
							subscriber.onNext(shift);
						}
					});

					Util.setSelectedShiftObservable(selectedShiftObservable);
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub

				}
			});
		}
	}

	private void updateGuardList(List<Guard> guardList, String action) {
		if (guardList != null && guardListFragment.guardListAdapter != null) {
			guardListFragment.guardListAdapter.updateGuardList(guardList, action);
		}
	}

	private void showGuardHomeFragment() {
		transaction = fragmentManager.beginTransaction();
		transaction.replace(R.id.fragmentContainer, guardHomeFragment);
//		transaction.addToBackStack(null);
		transaction.commitAllowingStateLoss();
	}

	private void showGuardListFragment() {
		toolbar.setVisibility(View.VISIBLE);
		transaction = fragmentManager.beginTransaction();
		transaction.replace(R.id.fragmentContainer, guardListFragment);
		transaction.addToBackStack(null);
		transaction.commitAllowingStateLoss();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
		switch (requestCode) {
		case REQUEST_READ_PHONE_STATE:
			if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
				Util.setIMEI(telephonyManager.getDeviceId().toString());
			}
			break;

		default:
			break;
		}
	}

	public void showDialog() {
		mStackLevel++;

		// DialogFragment.show() will take care of adding the fragment
		// in a transaction.  We also want to remove any currently showing
		// dialog, so make our own transaction and take care of that here.
		FragmentTransaction ft = fragmentManager.beginTransaction();
		Fragment prev = fragmentManager.findFragmentByTag("dialog");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		// Create and show the dialog.
		DialogFragment newFragment = MyDialogFragment.newInstance(mStackLevel, panicList, panicButtonObserver);
		newFragment.show(ft, "dialog");
	}

	private void updatePanicList(List<Panic> panicList) {
		this.panicList = panicList;
	}

	public void refreshActionBarMenu (){
		invalidateOptionsMenu();
	}

	@Override
	protected void onStop() {
		super.onStop();
		unsubscribeSubscription(centerInfoSubscription);
		unsubscribeSubscription(shiftInfoSubscription);
		unsubscribeSubscription(guardListSubscription);
		unsubscribeSubscription(panicListSubscription);
		unsubscribeSubscription(sendPanicSubscription);
	}

	private void unsubscribeSubscription(Subscription subscrption) {
		if (subscrption != null && !subscrption.isUnsubscribed()){
			subscrption.unsubscribe();
		}
	}

	private void getCenterInfo() {

		Observable<List<Center>> call = apiService.getCenterInfo(Util.getIMEI());
		centerInfoSubscription = call.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
			.filter(centers -> !centers.isEmpty()).map(centers -> centers.get(0)).subscribe(new Subscriber<Center>() {
				@Override
				public void onCompleted() {

				}

				@Override
				public void onError(Throwable e) {

				}

				@Override
				public void onNext(Center center) {
					Util.setCenterName(center.getCenterName());
					Util.setCenterId(center.getCenterId());
					getShiftDetail(center);
				}
			});
	}

	private void getShiftDetail(Center center) {

		Observable<List<Shift>> call = apiService.getShiftInfo(center.getCenterId());
		shiftInfoSubscription = call.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(new Subscriber<List<Shift>>() {
				@Override
				public void onCompleted() {

				}

				@Override
				public void onError(Throwable e) {

				}

				@Override
				public void onNext(List<Shift> shiftList) {
					addItemToSpinner(shiftList);
				}
			});
	}

	private void getGuardList(final String action) {

		Observable<List<Guard>> call =
			(action.equals(Util.TIME_IN)) ? apiService.getGuardCheckinList(Util.getIMEI(), Util.getCurrentDate()) :
				apiService.getGuardCheckOutList(Util.getCurrentDate(), Util.getIMEI());
		guardListSubscription = call.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
			.filter(guards -> !guards.isEmpty())
			.subscribe(new Subscriber<List<Guard>>() {
				@Override
				public void onCompleted() {

				}

				@Override
				public void onError(Throwable e) {

				}

				@Override
				public void onNext(List<Guard> guards) {
					updateGuardList(guards, action);
				}
			});
	}

	public void getPanicList() {

		Observable<List<Panic>> call = apiService.getPanicInfo();
		call.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
			.subscribe(new Subscriber<List<Panic>>() {
				@Override
				public void onCompleted() {

				}

				@Override
				public void onError(Throwable e) {

				}

				@Override
				public void onNext(List<Panic> panicList) {
					updatePanicList(panicList);
				}
			});
	}

	private void sendPanicAlert(Panic panic) {

		Observable<List<GrmResponse>> call = apiService
			.sendPanicInfo(panic.getAlertName(), Util.getIMEI(),Util.getLatitude(),Util.getLongitude(), Util.getCurrentDateTime(),
				Util.getCenterName(), Util.getCenterId(),panic.getAlertID(),"");
		sendPanicSubscription = call.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
			.filter(grmResponses -> !grmResponses.isEmpty()).map(grmResponses -> grmResponses.get(0)).subscribe(
				new Subscriber<GrmResponse>() {
					@Override
					public void onCompleted() {

					}

					@Override
					public void onError(Throwable e) {

					}

					@Override
					public void onNext(GrmResponse grmResponse) {
						if (grmResponse.getResponse().equals(Util.RESPONSE_SUCCESS)) {
							Util.showToastLong(GuardHomeActivity.this, "Panic Alert Send Successfully");
						}
						else {
							Util.showToastLong(GuardHomeActivity.this, "Error sending Alert");
						}
					}
				});
		//test comment
		//second test commit
	}
}
