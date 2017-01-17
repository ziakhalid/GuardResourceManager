package com.guardresourcemanager.genesis.guardresourcemanager.model;


import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import rx.Observable;

public class Util {

	public static final String TIME_IN = "TimeIn";
	public static final String TIME_OUT = "TimeOUT";
	public static final String UPLOADING_DATA = "UploadingData";
	public static final String SUCCESSFULLY_UPLOADED = "SuccessfullyUploaded";
	public static final String ERROR_UPLOADING = "ErrorUploading";
	public static final String DEFAULT_VIEW = "DefaultView";
	public static final String RESPONSE_SUCCESS = "success";
	private static String IMEI ="";
	private static String CENTER_ID ;
	private static double Latitude ;
	private static double Longitude ;

	public static double getLatitude() {
		return Latitude;
	}

	public static void setLatitude(double latitude) {
		Latitude = latitude;
	}

	public static double getLongitude() {
		return Longitude;
	}

	public static void setLongitude(double longitude) {
		Longitude = longitude;
	}

	private static Observable<Shift> selectedShiftObservable = null;

	public static Observable<Shift> getSelectedShiftObservable() {
		return selectedShiftObservable;
	}

	public static void setSelectedShiftObservable(
		Observable<Shift> selectedShiftObservable) {
		Util.selectedShiftObservable = selectedShiftObservable;
	}

	public static String getCenterName() {
		return CENTER_NAME;
	}

	public static void setCenterName(String centerName) {
		CENTER_NAME = centerName;
	}

	public static String getCenterId() {
		return CENTER_ID;
	}

	public static void setCenterId(String centerId) {
		CENTER_ID = centerId;
	}

	private static String CENTER_NAME = "GRM";

	public static String getCurrentDateTime(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentDateTime = sdf.format(new Date());
		return currentDateTime;
	}

	public static String getCurrentDate(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String currentDateTime = sdf.format(new Date());
		return currentDateTime;
	}

	public static String getIMEI() {
		return IMEI;
	}

	public static void setIMEI(String IMEI) {
		Util.IMEI = IMEI;
	}

	public static void showToastLong (Context context, String message){
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}

	public static void showToastShort (Context context, String message){
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}
}
