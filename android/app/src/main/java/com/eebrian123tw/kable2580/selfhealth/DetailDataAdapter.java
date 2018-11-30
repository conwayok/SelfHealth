package com.eebrian123tw.kable2580.selfhealth;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class DetailDataAdapter extends RecyclerView.Adapter<DetailDataAdapter.ViewHolder> {
  private List<DetailDataUnit> detailData;
  private Context context;

  public DetailDataAdapter(Context context, List<DetailDataUnit> detailData) {
    this.context = context;
    this.detailData = detailData;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    View view =
        LayoutInflater.from(context).inflate(R.layout.detail_data_unit_cardview, viewGroup, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
    DetailDataUnit detailDataUnit = detailData.get(i);
    viewHolder.dataTimeTextView.setText(detailDataUnit.getDataTime());
    viewHolder.dataTypeTextView.setText(detailDataUnit.getType());
    viewHolder.valueTextView.setText(String.valueOf(detailDataUnit.getValue()));
  }

  @Override
  public int getItemCount() {
    return detailData.size();
  }

  class ViewHolder extends RecyclerView.ViewHolder {
    private TextView dataTimeTextView;
    private TextView dataTypeTextView;
    private TextView valueTextView;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      dataTimeTextView = itemView.findViewById(R.id.datatime_textview);
      dataTypeTextView = itemView.findViewById(R.id.data_type_textview);
      valueTextView = itemView.findViewById(R.id.value_textview);
    }
  }
}
