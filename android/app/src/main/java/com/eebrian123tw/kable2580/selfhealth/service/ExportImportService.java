package com.eebrian123tw.kable2580.selfhealth.service;

import android.content.Context;

import com.eebrian123tw.kable2580.selfhealth.dao.HealthDataDao;
import com.eebrian123tw.kable2580.selfhealth.service.entity.DailyDataModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.threeten.bp.LocalDate;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ExportImportService {
  private ObjectMapper objectMapper;
  private HealthDataDao healthDataDao;

  public ExportImportService(Context context) {
    objectMapper = new ObjectMapper();
    objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
    healthDataDao = new HealthDataDao(context);
  }

  public String getHealthDataString(LocalDate startDate, LocalDate endDate) throws IOException {
    List<DailyDataModel> dailyDataModelList = healthDataDao.getDailyData(startDate, endDate);
    return objectMapper.writeValueAsString(dailyDataModelList);
  }

  public void saveHealthDataJson(File data) throws IOException {

    List<DailyDataModel> dailyDataModelList =
        objectMapper.readValue(
            data,
            objectMapper
                .getTypeFactory()
                .constructCollectionType(List.class, DailyDataModel.class));

    healthDataDao.saveDailyData(dailyDataModelList);
  }

  public void saveHealthDataJson(String data) throws IOException {
    List<DailyDataModel> dailyDataModelList =
        objectMapper.readValue(
            data,
            objectMapper
                .getTypeFactory()
                .constructCollectionType(List.class, DailyDataModel.class));

    healthDataDao.saveDailyData(dailyDataModelList);
  }
}
