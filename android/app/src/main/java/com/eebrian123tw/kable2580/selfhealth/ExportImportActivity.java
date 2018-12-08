package com.eebrian123tw.kable2580.selfhealth;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.eebrian123tw.kable2580.selfhealth.dao.HealthDataDao;
import com.eebrian123tw.kable2580.selfhealth.service.ExportImportService;
import com.eebrian123tw.kable2580.selfhealth.service.entity.DailyDataModel;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.obsez.android.lib.filechooser.ChooserDialog;

import org.threeten.bp.LocalDate;
import org.threeten.bp.Month;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ExportImportActivity extends AppCompatActivity
    implements View.OnClickListener, View.OnTouchListener {

  private Button exportButton;
  private Button importButton;
  private TextView
      fileNameTextView; // https://stackoverflow.com/questions/7856959/android-file-chooser
  private static final String TAG = "ExportImportActivity";

  @SuppressLint("ClickableViewAccessibility")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_export_import);

    exportButton = findViewById(R.id.export_button);
    importButton = findViewById(R.id.import_button);

    exportButton.setOnClickListener(this);
    importButton.setOnClickListener(this);

    fileNameTextView = findViewById(R.id.file_name_textview);

    fileNameTextView.setOnTouchListener(this);
  }

  public static class LocalDateDeserializer extends StdDeserializer<LocalDate> {
    public LocalDateDeserializer() {
      super(LocalDate.class);
    }

    @Override
    public LocalDate deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
      try {
        JsonNode node = jp.getCodec().readTree(jp);
        int year = Integer.parseInt(node.get("year").asText());
        Month month = Month.valueOf(node.get("month").asText());
        int dayOfMonth = Integer.parseInt(node.get("dayOfMonth").asText());
        return LocalDate.of(year, month, dayOfMonth);
      } catch (final Exception e) {
        throw new IOException(e);
      }
    }
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.export_button:
        new ChooserDialog()
            .with(this)
            .withFilter(true, false)
            .withStartFile(Environment.getExternalStorageDirectory().getPath() + "/")
            .withChosenListener(
                new ChooserDialog.Result() {
                  @SuppressLint("SetTextI18n")
                  @Override
                  public void onChoosePath(String path, File pathFile) {
                    Toast.makeText(ExportImportActivity.this, "FOLDER: " + path, Toast.LENGTH_SHORT)
                        .show();
                    fileNameTextView.setText(
                        getString(R.string.file_name_string) + ": " + path + "/selfHealth.json");
                  }
                })
            .build()
            .show();

        break;
      case R.id.import_button:
        new ChooserDialog(this)
            .withFilter(false, false, "json")
            .withStartFile(Environment.getExternalStorageDirectory().getPath() + "/")
            .withChosenListener(
                new ChooserDialog.Result() {
                  @SuppressLint("SetTextI18n")
                  @Override
                  public void onChoosePath(String s, File file) {
                    Toast.makeText(ExportImportActivity.this, "FILE: " + file, Toast.LENGTH_SHORT)
                        .show();
                    fileNameTextView.setText(getString(R.string.file_name_string) + ": " + file);
                    importData(file);
                  }
                })
            .build()
            .show();
        break;
    }
  }

  @SuppressLint("ClickableViewAccessibility")
  @Override
  public boolean onTouch(View v, MotionEvent event) {
    switch (v.getId()) {
      case R.id.file_name_textview:
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
          fileNameTextView.setLines(0);
          fileNameTextView.setEllipsize(null);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
          fileNameTextView.setLines(1);
          fileNameTextView.setEllipsize(TextUtils.TruncateAt.END);
        }
        break;
    }
    return true;
  }

  private void importData(File file) {
    try {

      ExportImportService exportImportService = new ExportImportService(ExportImportActivity.this);
      exportImportService.saveHealthDataJson(file);
      //      ObjectMapper mapper = new ObjectMapper();
      //      TypeFactory typeFactory = mapper.getTypeFactory();
      //      SimpleModule module = new SimpleModule();
      //      module.addDeserializer(LocalDate.class, new LocalDateDeserializer());
      //      mapper.registerModule(module);
      //      mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
      //      List<DailyDataModel> dailyDataModelList =
      //          mapper.readValue(
      //              file, typeFactory.constructCollectionType(List.class, DailyDataModel.class));
      //
      //
      //      Log.i(TAG, dailyDataModelList.size() + "");
      //            Toast.makeText(
      //                    ExportImportActivity.this,
      //                    "import " + dailyDataModelList.size() + " counts",
      //                    Toast.LENGTH_SHORT)
      //                .show();
      Toast.makeText(ExportImportActivity.this, "import ", Toast.LENGTH_SHORT).show();
      //      HealthDataDao healthDataDao = new HealthDataDao(ExportImportActivity.this);
      //      healthDataDao.saveDailyData(dailyDataModelList);
    } catch (IOException e) {
      e.printStackTrace();
      Toast.makeText(ExportImportActivity.this, "json parse error", Toast.LENGTH_SHORT).show();
    }
  }
}
