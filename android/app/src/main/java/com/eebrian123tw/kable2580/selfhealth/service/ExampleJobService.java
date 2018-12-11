package com.eebrian123tw.kable2580.selfhealth.service;

import android.app.job.JobParameters;
import android.app.job.JobService;

public class ExampleJobService extends JobService {
  @Override
  public boolean onStartJob(JobParameters params) {
    return false;
  }

  @Override
  public boolean onStopJob(JobParameters params) {
    return false;
  }
}
