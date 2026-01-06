package com.example.healthcarecompanion;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthcarecompanion.MedicineRecord;

import java.util.List;

public class DocumentListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_document_list);
        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main), (v, insets) -> {
                    Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
                    return insets;
                }
        );

        String folder = getIntent().getStringExtra("folderName");
        List<MedicineRecord> docs = new DBHelper(this).getDocumentsByFolder(folder);

        RecyclerView rv = findViewById(R.id.recyclerViewDocs);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new DocumentAdapter(this, docs));
    }
}
