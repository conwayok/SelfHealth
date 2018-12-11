package com.eebrian123tw.kable2580.selfhealth.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Objects;

public class ScreenReceiver extends BroadcastReceiver {

  private boolean screenOn;

  @Override
  public void onReceive(Context context, Intent intent) {
    if (Objects.requireNonNull(intent.getAction()).equals(Intent.ACTION_SCREEN_OFF)) {
      screenOn = false;
    } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
      screenOn = true;
    }
    Intent i = new Intent(context, BackgroundService.class);
    i.putExtra("screen_state", screenOn);
    context.startService(i);
  }
}
