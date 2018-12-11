package com.eebrian123tw.kable2580.selfhealth.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class BackgroundService extends Service {

  @Override
  public void onCreate() {
    super.onCreate();
    // REGISTER RECEIVER THAT HANDLES SCREEN ON AND SCREEN OFF LOGIC
    IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
    filter.addAction(Intent.ACTION_SCREEN_OFF);
    BroadcastReceiver mReceiver = new ScreenReceiver();
    registerReceiver(mReceiver, filter);
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    boolean screenOn = intent.getBooleanExtra("screen_state", false);
    if (!screenOn) {
      System.out.println("SCREEN OFF");
    } else {
      System.out.println("SCREEN ON");
    }
    return START_NOT_STICKY;
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    // TODO Auto-generated method stub
    return null;
  }
}
