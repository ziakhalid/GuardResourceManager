package com.guardresourcemanager.genesis.guardresourcemanager.fragments;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.guardresourcemanager.genesis.guardresourcemanager.R;
import com.guardresourcemanager.genesis.guardresourcemanager.adapter.DialogListAdapter;
import com.guardresourcemanager.genesis.guardresourcemanager.model.Panic;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;

public class MyDialogFragment extends DialogFragment {
	int mNum;
	List<Panic> panicList = null;
	Observer<Panic> panicObserver;
	Observable<Panic> panicObservable;
	Subscription panicSubcription;

	public static MyDialogFragment newInstance(int num, List<Panic> panicList, Observer<Panic> panicObserver) {
		MyDialogFragment f = new MyDialogFragment();
		f.panicList = panicList;
		f.panicObserver = panicObserver;

		// Supply num input as an argument.
		Bundle args = new Bundle();
		args.putInt("num", num);
		f.setArguments(args);

		return f;
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mNum = getArguments().getInt("num");

		// Pick a style based on the num.
		int style = DialogFragment.STYLE_NORMAL, theme = 0;
		switch ((mNum-1)%6) {
		case 1: style = DialogFragment.STYLE_NO_TITLE; break;
		case 2: style = DialogFragment.STYLE_NO_FRAME; break;
		case 3: style = DialogFragment.STYLE_NO_INPUT; break;
		case 4: style = DialogFragment.STYLE_NORMAL; break;
		case 5: style = DialogFragment.STYLE_NORMAL; break;
		case 6: style = DialogFragment.STYLE_NO_TITLE; break;
		case 7: style = DialogFragment.STYLE_NO_FRAME; break;
		case 8: style = DialogFragment.STYLE_NORMAL; break;
		}
		switch ((mNum-1)%6) {
		case 4: theme = android.R.style.Theme_Holo; break;
		case 5: theme = android.R.style.Theme_Holo_Light_Dialog; break;
		case 6: theme = android.R.style.Theme_Holo_Light; break;
		case 7: theme = android.R.style.Theme_Holo_Light_Panel; break;
		case 8: theme = android.R.style.Theme_Holo_Light; break;
		}
		setStyle(style, theme);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.list_fragment_dialog, container, false);
		ListView listDialogFragment = (ListView) view.findViewById(R.id.listDialogFragment);
		DialogListAdapter dialogListAdapter = new DialogListAdapter(getActivity(), this.panicList);
		listDialogFragment.setAdapter(dialogListAdapter);
		listDialogFragment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				Panic  panic = (Panic) adapterView.getAdapter().getItem(i);
				panicObservable = Observable.create(subscriber -> subscriber.onNext(panic));
				panicSubcription = panicObservable.subscribe(panicObserver);
				MyDialogFragment.this.dismiss();
			}
		});

		return view;
	}

	@Override
	public void onStop() {
		super.onStop();

		if (panicSubcription != null && !panicSubcription.isUnsubscribed()){
			panicSubcription.unsubscribe();
		}
	}
}
