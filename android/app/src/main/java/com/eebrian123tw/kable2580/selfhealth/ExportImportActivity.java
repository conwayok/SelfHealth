package com.eebrian123tw.kable2580.selfhealth;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ExportImportActivity extends AppCompatActivity implements View.OnClickListener {

    private Button exportButton;
    private Button importButton;
    private TextView fileNameTextView;

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
                break;
            case R.id.import_button:
                break;
        }

    }
}
