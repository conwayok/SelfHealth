package com.eebrian123tw.kable2580.selfhealth.workerManager;

import android.content.Context;
import android.support.annotation.NonNull;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class WakeWorker extends Worker {
  public WakeWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
    super(context, workerParams);
  }

  @NonNull
  @Override
  public Result doWork() {
    return null;
  }
}
