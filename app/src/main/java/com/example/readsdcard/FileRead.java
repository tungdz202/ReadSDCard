package com.example.readsdcard;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class FileRead extends AppCompatActivity {
    TextView content, filename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_file);

        filename = findViewById(R.id.filename);
        content = findViewById(R.id.content);

        filename.setText(getIntent().getStringExtra("filename"));
        content.setText(getIntent().getStringExtra("content"));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
