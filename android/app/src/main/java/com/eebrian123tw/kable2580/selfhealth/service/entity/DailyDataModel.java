package com.eebrian123tw.kable2580.selfhealth.service.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DailyDataModel {
  @JsonProperty("userId")
  private String userId;

  @JsonProperty("dataDate")
  private String dataDate;

  @JsonProperty("steps")
  private int steps;

  @JsonProperty("hoursOfSleep")
  private double hoursOfSleep;

  @JsonProperty("waterCC")
  private int waterCC;

  @JsonProperty("hoursPhoneUse")
  private double hoursPhoneUse;
}
