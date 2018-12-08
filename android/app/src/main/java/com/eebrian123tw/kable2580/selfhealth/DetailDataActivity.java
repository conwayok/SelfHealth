package com.eebrian123tw.kable2580.selfhealth;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.eebrian123tw.kable2580.selfhealth.dao.HealthDataDao;
import com.eebrian123tw.kable2580.selfhealth.service.HealthDataCalculator;
import com.eebrian123tw.kable2580.selfhealth.service.entity.DailyDataModel;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DetailDataActivity extends AppCompatActivity implements View.OnClickListener {

  private static final String TAG = "DetailDataActivity";
  private RecyclerView detailDataRecyclerView;
  private Button addButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail_data);

    addButton=findViewById(R.id.add_button);
    detailDataRecyclerView = findViewById(R.id.datail_data_recyclerview);
    detailDataRecyclerView.setLayoutManager(
        new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

    addButton.setOnClickListener(this);

    Intent intent=getIntent();
    DetailDataUnit.Type type=(DetailDataUnit.Type) intent.getSerializableExtra("type");

    LocalDate start = LocalDate.of(2018, 1, 1);
    LocalDate end = LocalDate.now();

//    HealthDataCalculator healthDataCalculator = new HealthDataCalculator(this, start, end);
    HealthDataDao healthDataDao=new HealthDataDao(this);
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    try {
      List<DailyDataModel>dailyDataModelList=healthDataDao.getDailyData(start,end);
      Log.i(TAG,dailyDataModelList.size()+"");
      List<DetailDataUnit> detailData = new ArrayList<>();
      Collections.reverse(dailyDataModelList);
      for(DailyDataModel dailyDataModel:dailyDataModelList){
        double value=0;
        switch (type){
          case STEPS:
            value=dailyDataModel.getSteps();
            break;
          case SLEEP:
            value=dailyDataModel.getHoursOfSleep();
            break;
          case DRINK:
            value=dailyDataModel.getWaterCC();
            break;
          case PHONE_USE:
            value=dailyDataModel.getHoursPhoneUse();
            break;
        }
        LocalDate localDate = LocalDate.parse(dailyDataModel.getDataDate(), dateTimeFormatter);
        detailData.add(new DetailDataUnit(type,value,localDate));
      }
      detailDataRecyclerView.setAdapter(new DetailDataAdapter(this, detailData));
    } catch (IOException e) {
      e.printStackTrace();
    }
//    healthDataCalculator.get




  }


  @Override
  public void onClick(View v) {
    switch (v.getId()){
      case R.id.add_button:
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(this);
        edittext.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        alert.setMessage("Enter Your Message");
        alert.setTitle("Enter Your Title");
        alert.setView(edittext);
        alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int whichButton) {
            Toast.makeText(DetailDataActivity.this,edittext.getText().toString(),Toast.LENGTH_SHORT).show();
          }
        });
        alert.show();
        break;
    }
  }
}
