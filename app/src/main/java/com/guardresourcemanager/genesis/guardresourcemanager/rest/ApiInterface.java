package com.guardresourcemanager.genesis.guardresourcemanager.rest;

import java.util.List;

import com.guardresourcemanager.genesis.guardresourcemanager.model.Center;
import com.guardresourcemanager.genesis.guardresourcemanager.model.Guard;
import com.guardresourcemanager.genesis.guardresourcemanager.model.GuardDataEntryResponse;
import com.guardresourcemanager.genesis.guardresourcemanager.model.Panic;
import com.guardresourcemanager.genesis.guardresourcemanager.model.GrmResponse;
import com.guardresourcemanager.genesis.guardresourcemanager.model.Shift;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface ApiInterface {

	@GET("/sgm_android/WebService.asmx/getguardSify")
	Observable<List<Guard>> getGuardCheckinList(@Query("imei") String imei, @Query("date") String date);

	@GET("/sgm_android/WebService.asmx/getguardrepdata1")
	Observable<List<Guard>> getGuardCheckOutList(@Query("date") String date,
		@Query("imei") String imei);

	@GET("/sgm_android/WebService.asmx/Ins_guard_Shift_Updt")
	Observable<List<GuardDataEntryResponse>> sendGuardAttendence(@Query("grdid") String guardId,
		@Query("gaurd_name") String guardName, @Query("dttm") String dateTime, @Query("imei") String imei,
		@Query("rep_flag") boolean repFlag, @Query("intm_flag") boolean intmFlag,@Query("shift_id") String shiftId);

	@GET("/sgm_android/WebService.asmx/getcenter")
	Observable<List<Center>> getCenterInfo(@Query("imei") String imei);

	@GET("/sgm_android/WebService.asmx/getalertpriority")
	Observable<List<Panic>> getPanicInfo();

	@GET("/sgm_android/WebService.asmx/Ins_Panic_alert_Updated")
	Observable<List<GrmResponse>> sendPanicInfo(@Query("alert_name") String panicName, @Query("imei") String imei,
		@Query("lat") String lat, @Query("lon") String lon, @Query("DTTM") String dttm,
		@Query("center_name") String centerName, @Query("center_id") String centerID,
		@Query("panictype_id") String panicTypeID,
		@Query("remark") String remark);

	@GET("/sgm_android/WebService.asmx/getshiftSify")
	Observable<List<Shift>> getShiftInfo(@Query("center_id") String centerId);

	//test comment

}
