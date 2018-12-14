package com.eebrian123tw.kable2580.selfhealth;

import android.support.annotation.NonNull;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;
import com.evernote.android.job.util.support.PersistableBundleCompat;

public class DemoSyncJob extends Job {

  public static final String TAG = "job_demo_tag";

  @Override
  @NonNull
  protected Result onRunJob(Params params) {
    // run your job here
    return Result.SUCCESS;
  }

  //  public static void scheduleJob() {
  //    new JobRequest.Builder(DemoSyncJob.TAG).setExecutionWindow(30_000L,
  // 40_000L).build().schedule();
  //  }
  private void scheduleAdvancedJob() {
    PersistableBundleCompat extras = new PersistableBundleCompat();
    extras.putString("key", "Hello world");

    int jobId =
        new JobRequest.Builder(DemoSyncJob.TAG)
            .setExecutionWindow(30_000L, 40_000L)
            .setBackoffCriteria(5_000L, JobRequest.BackoffPolicy.EXPONENTIAL)
//            .setRequiresCharging(true)
            .setRequiresDeviceIdle(true)
//            .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
            .setExtras(extras)
            .setRequirementsEnforced(true)
            .setUpdateCurrent(true)
            .build()
            .schedule();
  }
}
