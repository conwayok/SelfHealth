package com.eebrian123tw.kable2580.selfhealth;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.obsez.android.lib.filechooser.ChooserDialog;

import java.io.File;
import java.util.regex.Pattern;

public class ExportImportActivity extends AppCompatActivity implements View.OnClickListener,View.OnTouchListener {

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.export_button:
                new ChooserDialog().with(this)
                        .withFilter(true, false)
                        .withStartFile(Environment.getExternalStorageDirectory().getPath() + "/")
                        .withChosenListener(new ChooserDialog.Result() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onChoosePath(String path, File pathFile) {
                                Toast.makeText(ExportImportActivity.this, "FOLDER: " + path, Toast.LENGTH_SHORT).show();
                                fileNameTextView.setText(getString(R.string.file_name_string)+": " + path+"/selfHealth.json");
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
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onChoosePath(String s, File file) {
                                Toast.makeText(ExportImportActivity.this, "FILE: " + file, Toast.LENGTH_SHORT).show();
                                fileNameTextView.setText(getString(R.string.file_name_string)+": " + file);
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
        switch (v.getId()){
            case R.id.file_name_textview:
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    fileNameTextView.setLines(0);
                    fileNameTextView.setEllipsize(null);
                }else if(event.getAction()==MotionEvent.ACTION_UP) {
                    fileNameTextView.setLines(1);
                    fileNameTextView.setEllipsize(TextUtils.TruncateAt.END);
                }
                break;
        }
        return true;
    }
}
