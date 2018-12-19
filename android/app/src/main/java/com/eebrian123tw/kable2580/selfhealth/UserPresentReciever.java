package com.eebrian123tw.kable2580.selfhealth;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.eebrian123tw.kable2580.selfhealth.workerManager.UserPresentWorker;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class UserPresentReciever extends BroadcastReceiver {

  private static final String SCREEN_TOGGLE_TAG = "SCREEN_TOGGLE_TAG";

  @Override
  public void onReceive(Context context, Intent intent) {
    // if user wakes up device
    if (Intent.ACTION_USER_PRESENT.equals(intent.getAction())) {
      Log.d(SCREEN_TOGGLE_TAG, "User present");
      WorkManager.getInstance()
          .enqueue(new OneTimeWorkRequest.Builder(UserPresentWorker.class).build());
    } else {
      Log.d(SCREEN_TOGGLE_TAG, "else");
    }
  }
}
