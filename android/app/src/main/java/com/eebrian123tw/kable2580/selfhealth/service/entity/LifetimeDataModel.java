package com.eebrian123tw.kable2580.selfhealth.service.entity;

import lombok.Data;

@Data
public class LifetimeDataModel {
  private String userId;
  private long steps;
  private double hoursOfSleep;
  private long water;
  private double hoursPhoneUse;

  // kg
  private double weight;

  // cm
  private double height;
}
