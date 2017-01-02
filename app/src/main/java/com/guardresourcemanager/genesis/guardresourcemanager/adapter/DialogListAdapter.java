package com.guardresourcemanager.genesis.guardresourcemanager.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.guardresourcemanager.genesis.guardresourcemanager.R;
import com.guardresourcemanager.genesis.guardresourcemanager.model.Panic;

public class DialogListAdapter extends BaseAdapter {

	Context context;
	List<Panic> panicList = null;

	public DialogListAdapter(Context context, List<Panic> panicList) {
		this.context = context;
		this.panicList = panicList;
	}

	@Override
	public int getCount() {
		return panicList.size();
	}

	@Override
	public Object getItem(int i) {
		return panicList.get(i);
	}

	@Override
	public long getItemId(int i) {
		return 0;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {

		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.dialog_list_row, viewGroup, false);
		TextView textView = (TextView) v.findViewById(R.id.textRow);
		if (panicList != null) {
			textView.setText(panicList.get(i).getAlertName().toUpperCase());
		}
		return v;
	}

}
