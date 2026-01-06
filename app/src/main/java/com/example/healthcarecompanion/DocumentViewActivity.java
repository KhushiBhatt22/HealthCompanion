package com.example.healthcarecompanion;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class DocumentViewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_view);

        String path = getIntent().getStringExtra("filePath");
        ImageView iv = findViewById(R.id.fullImageView);
        if (path != null) {
            iv.setImageBitmap(BitmapFactory.decodeFile(path));
        }
    }
}
