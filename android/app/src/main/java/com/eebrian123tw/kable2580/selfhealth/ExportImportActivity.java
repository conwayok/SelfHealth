package com.eebrian123tw.kable2580.selfhealth;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.obsez.android.lib.filechooser.ChooserDialog;

import java.io.File;
import java.util.regex.Pattern;

public class ExportImportActivity extends AppCompatActivity implements View.OnClickListener {

    private Button exportButton;
    private Button importButton;
    private TextView
            fileNameTextView; // https://stackoverflow.com/questions/7856959/android-file-chooser
    private static final int FILE_SELECT_CODE = 1000;
    private static final String TAG = "ExportImportActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_import);

        exportButton = findViewById(R.id.export_button);
        importButton = findViewById(R.id.import_button);

        exportButton.setOnClickListener(this);
        importButton.setOnClickListener(this);

        fileNameTextView = findViewById(R.id.file_name_textview);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.export_button:
                new ChooserDialog().with(this)
                        .withFilter(true, false)
                        .withStartFile(Environment.getExternalStorageDirectory().getPath() + "/")
                        .withChosenListener(new ChooserDialog.Result() {
                            @Override
                            public void onChoosePath(String path, File pathFile) {
                                Toast.makeText(ExportImportActivity.this, "FOLDER: " + path, Toast.LENGTH_SHORT).show();
                                fileNameTextView.setText(getString(R.string.file_name_string) + path+"/selfHealth.json");
                            }
                        })
                        .build()
                        .show();

                break;
            case R.id.import_button:
                new ChooserDialog(this)
                        .withFilter(false, false, "json")
                        .withStartFile(Environment.getExternalStorageDirectory().getPath() + "/")
                        .withChosenListener(new ChooserDialog.Result() {
                            @Override
                            public void onChoosePath(String s, File file) {
                                Toast.makeText(ExportImportActivity.this, "FILE: " + file, Toast.LENGTH_SHORT).show();
                                fileNameTextView.setText(getString(R.string.file_name_string) + file);
                            }
                        })
                        .build()
                        .show();
                break;
        }
    }



}
