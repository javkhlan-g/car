package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class message extends AppCompatActivity {

    TextView contentText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        contentText = findViewById(R.id.content);

        Intent intent = getIntent();
        String content = intent.getStringExtra("CONTENT");

        contentText.setText(content);

    }
}
