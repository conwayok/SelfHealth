package com.eebrian123tw.kable2580.selfhealth.workerManager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.eebrian123tw.kable2580.selfhealth.service.IdleLogic;

import org.threeten.bp.LocalDateTime;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class UserPresentWorker extends Worker {
  private static final String TAG = "UserPresentWorker";

  public UserPresentWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
    super(context, workerParams);
  }

  @NonNull
  @Override
  public Result doWork() {
    Log.d(TAG, "UserPresentWorker doWork");

    LocalDateTime current = LocalDateTime.now().withNano(0);
    IdleLogic idleLogic = new IdleLogic(getApplicationContext());
    idleLogic.handlePhoneState("active", current);
    return Worker.Result.success();
  }
}
