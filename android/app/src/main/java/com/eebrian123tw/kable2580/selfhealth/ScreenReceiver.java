package com.eebrian123tw.kable2580.selfhealth;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.eebrian123tw.kable2580.selfhealth.workerManager.ScreenOffWorker;
import com.eebrian123tw.kable2580.selfhealth.workerManager.WakeWorker;

import java.util.Objects;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class ScreenReceiver extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {
    // if user wakes up device
    if (Objects.equals(intent.getAction(), Intent.ACTION_USER_PRESENT)) {
      WorkManager.getInstance().enqueue(new OneTimeWorkRequest.Builder(WakeWorker.class).build());
    }
    // if user turns of screen
    else if (Objects.equals(intent.getAction(), Intent.ACTION_SCREEN_OFF)) {
      WorkManager.getInstance()
          .enqueue(new OneTimeWorkRequest.Builder(ScreenOffWorker.class).build());
    }
  }
}
