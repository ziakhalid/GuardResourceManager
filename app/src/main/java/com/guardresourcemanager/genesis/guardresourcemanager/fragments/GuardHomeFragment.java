package com.guardresourcemanager.genesis.guardresourcemanager.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.guardresourcemanager.genesis.guardresourcemanager.R;
import com.guardresourcemanager.genesis.guardresourcemanager.activity.GuardHomeActivity;
import com.guardresourcemanager.genesis.guardresourcemanager.service.LocationService;
import com.jakewharton.rxbinding.view.RxView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observable;
import rx.Observer;
import rx.Subscription;

public class GuardHomeFragment extends Fragment{

	private Context context;
	private Unbinder unbinder;
	Observer<Void> btnTimeInObserver;
	Observer<Void> btnTimeOutObserver;

	@BindView(R.id.btn_timeIn)
	Button btnTimeIn;

	@BindView(R.id.btn_timeOut)
	Button btnTimeOut;

	public static GuardHomeFragment newInstance(Context context, Observer<Void> timeInObserver, Observer<Void> timeOutObserver){
		GuardHomeFragment guardHomeFragment = new GuardHomeFragment();
		guardHomeFragment.context = context;
		guardHomeFragment.btnTimeInObserver = timeInObserver;
		guardHomeFragment.btnTimeOutObserver = timeOutObserver;
		return guardHomeFragment;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
		@Nullable Bundle savedInstanceState) {

		Intent i;
		i = new Intent(context, LocationService.class);
		getActivity().startService(i);

		View view = inflater.inflate(R.layout.guard_home_fragment, container, false);
		unbinder = ButterKnife.bind(this, view);

		RxView.clicks(btnTimeIn).subscribe(btnTimeInObserver);
		RxView.clicks(btnTimeOut).subscribe(btnTimeOutObserver);

		return view;
	}

	@Override
	public void onPause() {
		super.onPause();
	}
}
