package com.eebrian123tw.kable2580.selfhealth.workerManager;

import android.content.Context;
import android.support.annotation.NonNull;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class DemoWorker extends Worker {
  public DemoWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
    super(context, workerParams);
  }

  @NonNull
  @Override
  public Result doWork() {

    return null;
  }
}
