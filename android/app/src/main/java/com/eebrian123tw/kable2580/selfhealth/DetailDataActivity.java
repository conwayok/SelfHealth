package com.eebrian123tw.kable2580.selfhealth;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.eebrian123tw.kable2580.selfhealth.service.HealthDataCalculator;
import com.eebrian123tw.kable2580.selfhealth.service.entity.DailyDataModel;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;

public class DetailDataActivity extends AppCompatActivity implements View.OnClickListener {

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

    LocalDate start = LocalDate.of(2018, 12, 3);
    LocalDate end = LocalDate.of(2018, 12, 8);


    HealthDataCalculator healthDataCalculator = new HealthDataCalculator(this, start, end);
//    healthDataCalculator.get

    List<DetailDataUnit> detailData = new ArrayList<>();
    detailData.add(new DetailDataUnit(DetailDataUnit.Type.STEPS,3462,LocalDate.now()));
    detailData.add(new DetailDataUnit(DetailDataUnit.Type.STEPS,3462,LocalDate.now()));
    detailData.add(new DetailDataUnit(DetailDataUnit.Type.STEPS,3462,LocalDate.now()));
    detailData.add(new DetailDataUnit(DetailDataUnit.Type.STEPS,3462,LocalDate.now()));
    detailData.add(new DetailDataUnit(DetailDataUnit.Type.STEPS,3462,LocalDate.now()));
    detailData.add(new DetailDataUnit(DetailDataUnit.Type.STEPS,3462,LocalDate.now()));
    detailData.add(new DetailDataUnit(DetailDataUnit.Type.STEPS,3462,LocalDate.now()));
    detailData.add(new DetailDataUnit(DetailDataUnit.Type.STEPS,3462,LocalDate.now()));
    detailData.add(new DetailDataUnit(DetailDataUnit.Type.STEPS,3462,LocalDate.now()));
    detailData.add(new DetailDataUnit(DetailDataUnit.Type.STEPS,3462,LocalDate.now()));

    detailDataRecyclerView.setAdapter(new DetailDataAdapter(this, detailData));
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
