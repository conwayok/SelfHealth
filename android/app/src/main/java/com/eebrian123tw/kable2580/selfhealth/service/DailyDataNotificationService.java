package com.eebrian123tw.kable2580.selfhealth.service;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.service.autofill.Dataset;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.eebrian123tw.kable2580.selfhealth.R;
import com.eebrian123tw.kable2580.selfhealth.UserPresentReceiver;
import com.eebrian123tw.kable2580.selfhealth.activity.MainActivity;
import com.eebrian123tw.kable2580.selfhealth.dao.HealthDataDao;
import com.eebrian123tw.kable2580.selfhealth.dao.SettingsDao;
import com.eebrian123tw.kable2580.selfhealth.googleFit.GoogleFitHistory;
import com.eebrian123tw.kable2580.selfhealth.googleFit.GoogleFitOauth;
import com.eebrian123tw.kable2580.selfhealth.googleFit.GoogleFitRecording;
import com.eebrian123tw.kable2580.selfhealth.service.entity.DailyDataModel;
import com.eebrian123tw.kable2580.selfhealth.service.entity.SettingsModel;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.tasks.OnSuccessListener;
import com.jakewharton.threetenabp.AndroidThreeTen;

import org.threeten.bp.LocalDate;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class DailyDataNotificationService extends Service {
    private static final String TAG = "DailyDataNotService";

    private SettingsDao settingsDao;
    private HealthDataDao healthDataDao;

    @Override
    public void onCreate() {
        // initialize time zone information
        AndroidThreeTen.init(this);
        settingsDao = new SettingsDao(this);
        healthDataDao = new HealthDataDao(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction(Intent.ACTION_USER_PRESENT);
        UserPresentReceiver userPresentReceiver = new UserPresentReceiver();
        registerReceiver(userPresentReceiver, intentFilter);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");
        Timer timer = new Timer();
        TimerTask timerTask =
                new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            HealthDataDao healthDataDao = new HealthDataDao(DailyDataNotificationService.this);
                            DailyDataModel dailyDataModel;

                            List<DailyDataModel> dailyDataModelList =
                                    healthDataDao.getDailyData(LocalDate.now(), LocalDate.now());
                            if (dailyDataModelList.size() == 0) {
                                dailyDataModel = new DailyDataModel();
                                dailyDataModel.setDataDate(LocalDate.now().toString());
                                healthDataDao.saveDailyData(dailyDataModel);
                            } else {
                                dailyDataModel = dailyDataModelList.get(0);
                            }
                            notificationDailyData(dailyDataModel);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };
        timer.schedule(timerTask, 0, 1000);


        TimerTask steps = new TimerTask() {
            @Override
            public void run() {
                try {
                    if (GoogleFitOauth.hasOauth(DailyDataNotificationService.this) && settingsDao.getSettings().isConnectedToGoogleFit()) {
                        GoogleFitRecording.fitRecordingSubscribe(DailyDataNotificationService.this, DataType.TYPE_STEP_COUNT_DELTA);
                        GoogleFitRecording.fitRecordingSubscribe(DailyDataNotificationService.this, DataType.TYPE_CALORIES_EXPENDED);
                        GoogleFitRecording.fitRecordingSubscribe(DailyDataNotificationService.this, DataType.AGGREGATE_DISTANCE_DELTA);
                        Calendar cal = Calendar.getInstance();
                        Date now = new Date();
                        cal.setTime(now);
                        long endTime = cal.getTimeInMillis();
                        cal.set(Calendar.HOUR_OF_DAY, 0);
                        cal.set(Calendar.MINUTE, 0);
                        cal.set(Calendar.SECOND, 0);
                        cal.set(Calendar.MILLISECOND, 0);
                        Log.d(TAG, "run: ");
                        long startTime = cal.getTimeInMillis();

                        final GoogleFitHistory history = new GoogleFitHistory(DailyDataNotificationService.this);
                        history.readHistoryData(startTime,endTime,1, TimeUnit.DAYS).addOnSuccessListener(new OnSuccessListener<DataReadResponse>() {
                            @Override
                            public void onSuccess(DataReadResponse dataReadResponse) {
                                List<DataSet> datasets=history.getDataSets(dataReadResponse);
                                for(DataSet dataSet:datasets){
                                    for (DataPoint dp : dataSet.getDataPoints()) {
                                        if(dp.getDataType().equals(DataType.AGGREGATE_STEP_COUNT_DELTA)){
                                            for (Field field : dp.getDataType().getFields()) {
                                                Log.i(TAG, "\tField: " + field.getName() + " Value: " + dp.getValue(field));
                                                try {
                                                    DailyDataModel dailyDataModel=healthDataDao.getDailyDataSingle(LocalDate.now());
                                                    dailyDataModel.setSteps(dp.getValue(field).asInt());
                                                    healthDataDao.saveDailyData(dailyDataModel);
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
//                                        Log.i(TAG, "Data point:");
//                                        Log.i(TAG, "\tType: " + dp.getDataType().getName());
//                                        Log.i(TAG, "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
//                                        Log.i(TAG, "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
//                                        for (Field field : dp.getDataType().getFields()) {
//                                            Log.i(TAG, "\tField: " + field.getName() + " Value: " + dp.getValue(field));
//                                        }
                                    }

                                }


                            }
                        });


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };

        timer.schedule(steps,0,3*60*1000);


        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            stopForeground(true);
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        System.out.println("service in onTaskRemoved");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

        } else {
            Intent restartService =
                    new Intent(getApplicationContext(), DailyDataNotificationService.class);
            PendingIntent restartServicePI =
                    PendingIntent.getService(getApplicationContext(), 0, restartService, 0);
            AlarmManager mgr =
                    (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
            if (mgr != null) {
                mgr.set(AlarmManager.RTC_WAKEUP, 3000, restartServicePI);
            }
        }
        super.onTaskRemoved(rootIntent);
        Log.i(TAG, "onTaskRemoved");
    }

    private void notificationDailyData(DailyDataModel dailyDataModel) {

        // notification
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_daily_data);

        try {
            boolean warn = false;
            SettingsModel settingsModel = settingsDao.getSettings();
            if (dailyDataModel.getSteps() < settingsModel.getDailyStepsGoal()) {
                remoteViews.setTextColor(R.id.notification_step_textview, Color.RED);
                warn = true;
            } else {
                remoteViews.setTextColor(R.id.notification_step_textview, Color.BLACK);
            }
            if (dailyDataModel.getWaterCC() < settingsModel.getDailyWaterGoal()) {
                remoteViews.setTextColor(R.id.notification_drink_textview, Color.RED);
                warn = true;
            } else {
                remoteViews.setTextColor(R.id.notification_drink_textview, Color.BLACK);
            }
            if (dailyDataModel.getHoursOfSleep() < settingsModel.getDailySleepHoursGoal()) {
                remoteViews.setTextColor(R.id.notification_sleep_textview, Color.RED);
                warn = true;
            } else {
                remoteViews.setTextColor(R.id.notification_sleep_textview, Color.BLACK);
            }
            if (dailyDataModel.getHoursPhoneUse() > settingsModel.getDailyPhoneUseHoursGoal()) {
                remoteViews.setTextColor(R.id.notification_use_phone_textview, Color.RED);
                warn = true;
            } else {
                remoteViews.setTextColor(R.id.notification_use_phone_textview, Color.BLACK);
            }
            if (warn) {
                remoteViews.setTextColor(R.id.notication_title, Color.rgb(255, 165, 0));
            } else {
                remoteViews.setTextColor(R.id.notication_title, Color.GREEN);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        DecimalFormat formatter = new DecimalFormat("#.#");
        formatter.applyPattern("0.0");


        try {

            DailyDataModel yesterday = healthDataDao.getDailyDataSingle(LocalDate.now().minusDays(1));
            double hoursOfSleep = 0;

            if (yesterday != null) {
                hoursOfSleep = yesterday.getHoursOfSleep();
            }
            if (hoursOfSleep < 1) {
                hoursOfSleep *= 60;
                remoteViews.setTextViewText(
                        R.id.notification_sleep_textview,
                        getString(R.string.yesterday_sleep_string)
                                + ": "
                                + formatter.format(hoursOfSleep)
                                + getString(R.string.minute_string));
            } else {
                remoteViews.setTextViewText(
                        R.id.notification_sleep_textview,
                        getString(R.string.yesterday_sleep_string)
                                + ": "
                                + formatter.format(hoursOfSleep)
                                + getString(R.string.hour_string));
            }


        } catch (IOException e) {
            e.printStackTrace();
            remoteViews.setTextViewText(
                    R.id.notification_sleep_textview,
                    getString(R.string.yesterday_sleep_string)
                            + ": "
                            + "0"
                            + getString(R.string.hour_string));
        }


        remoteViews.setTextViewText(
                R.id.notification_step_textview,
                getString(R.string.today_step_string)
                        + ": "
                        + dailyDataModel.getSteps()
                        + getString(R.string.step_string));

        remoteViews.setTextViewText(
                R.id.notification_drink_textview,
                getString(R.string.today_drink_string)
                        + ": "
                        + dailyDataModel.getWaterCC()
                        + getString(R.string.cc_string));


        double hoursPhoneUse = dailyDataModel.getHoursPhoneUse();
        if (hoursPhoneUse < 1) {
            hoursPhoneUse *= 60;
            remoteViews.setTextViewText(
                    R.id.notification_use_phone_textview,
                    getString(R.string.today_use_phone_string)
                            + ": "
                            + formatter.format(hoursPhoneUse)
                            + getString(R.string.minute_string));

        } else {
            remoteViews.setTextViewText(
                    R.id.notification_use_phone_textview,
                    getString(R.string.today_use_phone_string)
                            + ": "
                            + formatter.format(hoursPhoneUse)
                            + getString(R.string.hour_string));

        }


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
                .setAutoCancel(false)
                .setOnlyAlertOnce(true)
                .setCustomContentView(remoteViews)
                .setSmallIcon(R.drawable.ic_launcher_background)
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

        try {
            if (settingsDao.getSettings().isShowNotification()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForeground(notificationId, notificationBuilder.build());
                    //                notificationManager.notify(notificationId, notificationBuilder.build());
                } else if (notificationManager != null) {
                    notificationManager.notify(notificationId, notificationBuilder.build());
                }
            } else {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForeground(notificationId, notificationBuilder.build());
                    stopForeground(true);
                    //                notificationManager.notify(notificationId, notificationBuilder.build());
                } else if (notificationManager != null) {
                    notificationManager.cancel(notificationId);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
