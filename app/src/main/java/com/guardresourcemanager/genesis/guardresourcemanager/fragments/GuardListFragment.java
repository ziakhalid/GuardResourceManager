package com.guardresourcemanager.genesis.guardresourcemanager.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.guardresourcemanager.genesis.guardresourcemanager.R;
import com.guardresourcemanager.genesis.guardresourcemanager.activity.GuardHomeActivity;
import com.guardresourcemanager.genesis.guardresourcemanager.adapter.GuardListAdapter;
import com.jakewharton.rxbinding.view.RxView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observable;

public class GuardListFragment extends Fragment {

	@BindView(R.id.recycler_view)
	RecyclerView recyclerView;
	@BindView(R.id.btn_submit)
	Button btnSubmit;

	Context context;
	private Unbinder unbinder;
	public GuardListAdapter guardListAdapter;
	Observable<Void> submitButtonObservable;
	GuardHomeActivity guardHomeActivity;

	public static GuardListFragment newInstance(Context context ) {
		GuardListFragment guardListFragment = new GuardListFragment();
		guardListFragment.context = context;
		guardListFragment.guardHomeActivity = (GuardHomeActivity) context;
		return guardListFragment;
	}

	@Override
	public void onStart() {
		super.onStart();
		guardHomeActivity.setSpinnerToolbar();
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
		@Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.guard_list_fragment, container, false);
		unbinder = ButterKnife.bind(this, view);

		submitButtonObservable = RxView.clicks(btnSubmit);

		guardListAdapter = new GuardListAdapter(context, submitButtonObservable);
		RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
		recyclerView.setLayoutManager(mLayoutManager);
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		recyclerView.setAdapter(guardListAdapter);

		return view;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (guardHomeActivity != null){
			guardHomeActivity.setNormalToolbar();
		}
	}
}
