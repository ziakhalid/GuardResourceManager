package com.guardresourcemanager.genesis.guardresourcemanager.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.guardresourcemanager.genesis.guardresourcemanager.R;
import com.guardresourcemanager.genesis.guardresourcemanager.model.Guard;
import com.guardresourcemanager.genesis.guardresourcemanager.model.GuardDataEntryResponse;
import com.guardresourcemanager.genesis.guardresourcemanager.model.Shift;
import com.guardresourcemanager.genesis.guardresourcemanager.model.Util;
import com.guardresourcemanager.genesis.guardresourcemanager.rest.ApiClient;
import com.guardresourcemanager.genesis.guardresourcemanager.rest.ApiInterface;
import com.jakewharton.rxbinding.view.RxView;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class GuardListAdapter extends RecyclerView.Adapter<GuardListAdapter.MyViewHolder>{

	private List<Guard> guardList = new ArrayList<Guard>();
	String imeiNo="";
	private String action;
	Subscription getSelectedShiftSubscription;

	public GuardListAdapter(Context context, Observable<Void> submitButtonObservable) {

		imeiNo = Util.getIMEI();

		submitButtonObservable.subscribe(aVoid -> {

			if (Util.getSelectedShiftObservable() != null) {
				getSelectedShiftSubscription = Util.getSelectedShiftObservable()
					.subscribe(shift -> sendUpdatedGuardAttendanceToServer(shift));
			}
			else {
				Util.showToastLong(context, "Please Select Shift");
			}
		});

	}

	private void sendUpdatedGuardAttendanceToServer(Shift shift) {

		Timber.d("Inside ListAdapter : submit button is clicked Size of list " + guardList.size());
		for (int i = 0; i < guardList.size(); i++) {
			final Guard guard = guardList.get(i);
			final int pos = i;
			if (guard.isSelected()) {
				Timber.d(guard + " isSelected Imei NO : " + imeiNo);
				guard.setProgressStatus(Util.UPLOADING_DATA);
				ApiInterface apiService =
					ApiClient.getClient().create(ApiInterface.class);

				Observable<List<GuardDataEntryResponse>> call = apiService
					.sendGuardAttendence(guard.getGuardID(), guard.getName(), guard.getDateTime(), imeiNo,
						guard.isReplacable(), guard.isGuardCheckedin(), shift.getShiftNumber());
				call.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
					new Subscriber<List<GuardDataEntryResponse>>() {
						@Override
						public void onCompleted() {

						}

						@Override
						public void onError(Throwable e) {
							Timber.d("call failed: " + e.getMessage());
							guard.setProgressStatus(Util.ERROR_UPLOADING);
							notifyItemChanged(pos);
						}

						@Override
						public void onNext(List<GuardDataEntryResponse> guardDataEntryResponses) {
							guard.setProgressStatus(Util.SUCCESSFULLY_UPLOADED);
							notifyItemChanged(pos);
						}
					});
			}
		}

	}


	class MyViewHolder extends RecyclerView.ViewHolder{
		@BindView(R.id.guard_name)
		TextView guardName;
		@BindView(R.id.guard_token_id)
		TextView guardTokenNo;
		@BindView(R.id.cbGuardAttendance)
		CheckBox cbGuardAttendance;
		@BindView(R.id.btn_replace)
		Button btnReplaceGuard;
		@BindView(R.id.edit_guard_name)
		EditText editGuardName;
		@BindView(R.id.edit_guard_id)
		EditText editGuardTokenId;
		@BindView(R.id.collapsed_layout)
		LinearLayout collapseLayout;
		@BindView(R.id.guard_data_send_progress)
		ProgressBar guardDataSendProgress;
		@BindView(R.id.progress_status)
		ImageView imageProgressStatus;

		public MyViewHolder(View view) {
			super(view);
			ButterKnife.bind(this, view);
//			editGuardTokenId.setEnabled(false);
		}
	}

	@Override
	public GuardListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.guard_list_row, parent, false);
		return new MyViewHolder(view);
	}

	@Override
	public void onBindViewHolder(final GuardListAdapter.MyViewHolder holder, final int position) {
		final Guard guard = guardList.get(position);
		holder.guardName.setText(guard.getName().toUpperCase());
		holder.guardTokenNo.setText(guard.getTokenNumber().toUpperCase());

		if(action != null && action.equals(Util.TIME_OUT)){
			holder.btnReplaceGuard.setVisibility(View.GONE);
		}

		holder.cbGuardAttendance.setOnCheckedChangeListener(null);
		holder.cbGuardAttendance.setChecked(guard.isSelected());

		holder.cbGuardAttendance.setOnCheckedChangeListener(((compoundButton, isSelected) -> {
				guard.setSelected(isSelected);
				if (isSelected) guard.setDateTime(Util.getCurrentDateTime());
		}));


		if (guard.isGuardCheckedin()) guard.setProgressStatus(Util.SUCCESSFULLY_UPLOADED);

		if(guard.getProgressStatus() != null){
			switch (guardList.get(position).getProgressStatus()){

			case Util.UPLOADING_DATA:
				holder.btnReplaceGuard.setVisibility(View.GONE);
				holder.guardDataSendProgress.setVisibility(View.VISIBLE);
				holder.imageProgressStatus.setVisibility(View.GONE);
				break;
			case Util.SUCCESSFULLY_UPLOADED:
				holder.btnReplaceGuard.setVisibility(View.GONE);
				holder.guardDataSendProgress.setVisibility(View.GONE);
				holder.imageProgressStatus.setImageResource(R.drawable.action_accept);
				holder.imageProgressStatus.setVisibility(View.VISIBLE);
				guard.setSelected(false);
				holder.cbGuardAttendance.setVisibility(View.GONE);
				break;
			case Util.ERROR_UPLOADING:
				holder.btnReplaceGuard.setVisibility(View.GONE);
				holder.guardDataSendProgress.setVisibility(View.GONE);
				holder.imageProgressStatus.setImageResource(R.drawable.action_reject);
				holder.imageProgressStatus.setVisibility(View.VISIBLE);
				break;
			case Util.DEFAULT_VIEW:
				holder.btnReplaceGuard.setVisibility(View.VISIBLE);
				holder.guardDataSendProgress.setVisibility(View.GONE);
				holder.imageProgressStatus.setVisibility(View.GONE);
				break;
			}
		}

		RxView.clicks(holder.btnReplaceGuard).subscribe(aVoid -> {
			if (holder.collapseLayout.getVisibility() == View.VISIBLE) {
				holder.collapseLayout.setVisibility(View.GONE);
				holder.btnReplaceGuard.setText("REPLACE");
				holder.btnReplaceGuard.setBackgroundResource(R.drawable.replace_button_selector);
				if (holder.editGuardName.getText().toString().length() > 0) {
					updateGuardReplacementName(position, holder.editGuardName.getText().toString());
					holder.editGuardName.setText("");
				}
			}
			else {
				holder.collapseLayout.setVisibility(View.VISIBLE);
				holder.btnReplaceGuard.setText("SAVE");
				holder.btnReplaceGuard.setBackgroundResource(R.drawable.save_button_selector);
			}
		});
	}

	@Override
	public int getItemCount() {
		return guardList.size();
	}

	public void updateGuardList(List<Guard> guards, String action){
		this.guardList = guards;
		this.action = action;
		notifyDataSetChanged();
	}

	public void updateGuardReplacementName (int position, String newGuardName) {
		if (guardList != null && guardList.size() > 0) {
			guardList.get(position).setName(newGuardName);
			guardList.get(position).setReplacable(true);
			notifyItemChanged(position);
		}
	}

}
