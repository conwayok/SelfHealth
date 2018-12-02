package com.eebrian123tw.kable2580.selfhealth;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

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

    List<DetailDataUnit> detailData = new ArrayList<>();
    detailData.add(new DetailDataUnit("步數", 1564, "週三"));
    detailData.add(new DetailDataUnit("步數", 165564, "週三"));
    detailData.add(new DetailDataUnit("步數", 155664, "週三"));
    detailData.add(new DetailDataUnit("步數", 1567864, "週三"));
    detailData.add(new DetailDataUnit("步數", 155464, "週三"));
    detailData.add(new DetailDataUnit("步數", 15634, "週三"));
    detailData.add(new DetailDataUnit("步數", 1578664, "週三"));
    detailData.add(new DetailDataUnit("步數", 156344, "週三"));
    detailData.add(new DetailDataUnit("步數", 15644, "週三"));
    detailData.add(new DetailDataUnit("步數", 15624, "週三"));
    detailData.add(new DetailDataUnit("步數", 15624, "週三"));
    detailData.add(new DetailDataUnit("步數", 15624, "週三"));
    detailData.add(new DetailDataUnit("步數", 15624, "週三"));
    detailData.add(new DetailDataUnit("步數", 15624, "週三"));
    detailData.add(new DetailDataUnit("步數", 15624, "週三"));
    detailData.add(new DetailDataUnit("步數", 15624, "週三"));
    detailData.add(new DetailDataUnit("步數", 15624, "週三"));
    detailData.add(new DetailDataUnit("步數", 15624, "週三"));
    detailData.add(new DetailDataUnit("步數", 15624, "週三"));
    detailData.add(new DetailDataUnit("步數", 15624, "週三"));
    detailData.add(new DetailDataUnit("步數", 15624, "週三"));
    detailData.add(new DetailDataUnit("步數", 15624, "週三"));
    detailData.add(new DetailDataUnit("步數", 15624, "週三"));
    detailData.add(new DetailDataUnit("步數", 15624, "週三"));
    detailData.add(new DetailDataUnit("步數", 15624, "週三"));
    detailData.add(new DetailDataUnit("步數", 15624, "週三"));
    detailData.add(new DetailDataUnit("步數", 15624, "週三"));
    detailData.add(new DetailDataUnit("步數", 15624, "週三"));
    detailData.add(new DetailDataUnit("步數", 15624, "週三"));
    detailData.add(new DetailDataUnit("步數", 15624, "週三"));
    detailData.add(new DetailDataUnit("步數", 15624, "週三"));
    detailData.add(new DetailDataUnit("步數", 15624, "週三"));
    detailData.add(new DetailDataUnit("步數", 15624, "週三"));
    detailData.add(new DetailDataUnit("步數", 15624, "週三"));
    detailData.add(new DetailDataUnit("步數", 15624, "週三"));
    detailData.add(new DetailDataUnit("步數", 15624, "週三"));
    detailData.add(new DetailDataUnit("步數", 15624, "週三"));
    detailData.add(new DetailDataUnit("步數", 15624, "週三"));
    detailData.add(new DetailDataUnit("步數", 15624, "週三"));
    detailData.add(new DetailDataUnit("步數", 15624, "週三"));
    detailData.add(new DetailDataUnit("步數", 15624, "週三"));
    detailData.add(new DetailDataUnit("步數", 15624, "週三"));
    detailData.add(new DetailDataUnit("步數", 15624, "週三"));
    detailData.add(new DetailDataUnit("步數", 15624, "週三"));
    detailData.add(new DetailDataUnit("步數", 15624, "週三"));
    detailData.add(new DetailDataUnit("步數", 15624, "週三"));
    detailData.add(new DetailDataUnit("步數", 15624, "週三"));
    detailData.add(new DetailDataUnit("步數", 15624, "週三"));
    detailData.add(new DetailDataUnit("步數", 15624, "週三"));
    detailData.add(new DetailDataUnit("步數", 15624, "週三"));
    detailData.add(new DetailDataUnit("步數", 15624, "週三"));
    detailData.add(new DetailDataUnit("步數", 15624, "週三"));
    detailData.add(new DetailDataUnit("步數", 15624, "週三"));
    detailData.add(new DetailDataUnit("步數", 15624, "週三"));
    detailData.add(new DetailDataUnit("步數", 15624, "週三"));
    detailData.add(new DetailDataUnit("步數", 15624, "週三"));
    detailData.add(new DetailDataUnit("步數", 15624, "週三"));
    detailData.add(new DetailDataUnit("步數", 15624, "週三"));
    detailData.add(new DetailDataUnit("步數", 15624, "週三"));
    detailData.add(new DetailDataUnit("步數", 15624, "週三"));
    detailData.add(new DetailDataUnit("步數", 15624, "週三"));
    detailData.add(new DetailDataUnit("步數", 15624, "週三"));
    detailData.add(new DetailDataUnit("步數", 15624, "週三"));
    detailData.add(new DetailDataUnit("步數", 15624, "週三"));
    detailData.add(new DetailDataUnit("步數", 15624, "週三"));
    detailData.add(new DetailDataUnit("步數", 15624, "週三"));
    detailData.add(new DetailDataUnit("步數", 15624, "週三"));
    detailData.add(new DetailDataUnit("步數", 15624, "週三"));
    detailData.add(new DetailDataUnit("步數", 15624, "週三"));
    detailData.add(new DetailDataUnit("步數", 15624, "週三"));
    detailData.add(new DetailDataUnit("步數", 15624, "週三"));

    detailDataRecyclerView.setAdapter(new DetailDataAdapter(this, detailData));
  }


  @Override
  public void onClick(View v) {
    switch (v.getId()){
      case R.id.add_button:

        break;
    }
  }
}
