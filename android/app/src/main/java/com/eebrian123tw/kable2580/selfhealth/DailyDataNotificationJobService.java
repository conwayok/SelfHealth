package com.eebrian123tw.kable2580.selfhealth;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.eebrian123tw.kable2580.selfhealth.service.entity.DailyDataModel;

import org.threeten.bp.LocalDate;

import java.util.Random;

public class DailyDataNotificationJobService extends JobService {
  @Override
  public boolean onStartJob(JobParameters params) {
    Random random = new Random();
    DailyDataModel dailyDataModel = new DailyDataModel();
    dailyDataModel.setDataDate(LocalDate.now());
    dailyDataModel.setUserId("1234567");
    dailyDataModel.setSteps(random.nextInt(1000) + 1000);
    dailyDataModel.setHoursOfSleep(2.6);
    dailyDataModel.setHoursPhoneUse(3.5);
    dailyDataModel.setWaterCC(random.nextInt(1000) + 1500);

    notificationDailyData(dailyDataModel);
    jobFinished(params, true);
    return true;
  }

  @Override
  public boolean onStopJob(JobParameters params) {
    return false;
  }

  private void notificationDailyData(DailyDataModel dailyDataModel) {

    // notification
    NotificationManager notificationManager =
        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_daily_data);
    remoteViews.setTextViewText(
        R.id.notification_step_textview,
        getString(R.string.today_step_string)
            + ": "
            + dailyDataModel.getSteps()
            + getString(R.string.step_string));
    remoteViews.setTextViewText(
        R.id.notification_sleep_textview,
        getString(R.string.yesterday_sleep_string)
            + ": "
            + dailyDataModel.getHoursOfSleep()
            + getString(R.string.hour_string));
    remoteViews.setTextViewText(
        R.id.notification_drink_textview,
        getString(R.string.today_drink_string)
            + ": "
            + dailyDataModel.getWaterCC()
            + getString(R.string.cc_string));
    remoteViews.setTextViewText(
        R.id.notification_use_phone_textview,
        getString(R.string.today_use_phone_string)
            + ": "
            + dailyDataModel.getHoursPhoneUse()
            + getString(R.string.hour_string));

    int notificationId = 11011;
    String channelId = "daily_data_channel_id";

    Intent notificationIntent = new Intent(this, MainActivity.class);
    TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

    stackBuilder.addParentStack(MainActivity.class);
    stackBuilder.addNextIntent(notificationIntent);
    PendingIntent resultPendingIntent =
        stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
    NotificationCompat.Builder notificationBuilder =
        new NotificationCompat.Builder(this, channelId);
    notificationBuilder
        .setAutoCancel(true)
        .setCustomContentView(remoteViews)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentIntent(resultPendingIntent)
        .setCustomBigContentView(remoteViews)
        .setOngoing(true);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      int important = NotificationManager.IMPORTANCE_HIGH;
      NotificationChannel notificationChannel =
          new NotificationChannel(channelId, "daily_data_channel_name", important);
      notificationBuilder.setChannelId(channelId);
      if (notificationManager != null) {
        notificationManager.createNotificationChannel(notificationChannel);
      }
    }

    if (notificationManager != null) {
      notificationManager.notify(notificationId, notificationBuilder.build());
    }
  }
}
