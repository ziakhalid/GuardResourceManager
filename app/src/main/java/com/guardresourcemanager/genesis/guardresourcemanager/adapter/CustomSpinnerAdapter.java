package com.guardresourcemanager.genesis.guardresourcemanager.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.guardresourcemanager.genesis.guardresourcemanager.R;
import com.guardresourcemanager.genesis.guardresourcemanager.model.Shift;

public class CustomSpinnerAdapter extends ArrayAdapter<Shift> {

	public Resources res;
	LayoutInflater inflater;
	private Context context1;
	private List<Shift> data;

	public CustomSpinnerAdapter(Context context, List<Shift> shiftList) {
		super(context, R.layout.spinner_row, shiftList);

		context1 = context;
		data = shiftList;

		inflater = (LayoutInflater) context1
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return getCustomView(position, convertView, parent);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getCustomView(position, convertView, parent);
	}

	@Override
	public Shift getItem(int position) {
		return data.get(position);
	}

	// This funtion called for each row ( Called data.size() times )
	public View getCustomView(int position, View convertView, ViewGroup parent) {

		View row = inflater.inflate(R.layout.spinner_row, parent, false);
		TextView tvCategory = (TextView) row.findViewById(R.id.tvCategory);
		tvCategory.setText(data.get(position).getShiftName().toUpperCase());

		return row;
	}
}
