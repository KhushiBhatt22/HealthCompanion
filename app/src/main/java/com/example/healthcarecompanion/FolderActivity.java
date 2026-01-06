package com.example.healthcarecompanion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
public class FolderActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FolderAdapter folderAdapter;
    private DBHelper dbHelper;
    private View emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);

        FloatingActionButton fab = findViewById(R.id.fabAddFolder);
        fab.setOnClickListener(v -> {
            startActivity(new Intent(this, UploadActivity.class));
        });


        recyclerView = findViewById(R.id.recyclerViewFolders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        emptyView    = findViewById(R.id.emptyView);
        dbHelper = new DBHelper(this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadFolders();
    }

    private void loadFolders() {
        List<String> folders = dbHelper.getAllFolders();
        if (folders.isEmpty()) {
            // show empty-state, hide list
            emptyView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            // set adapter
            folderAdapter = new FolderAdapter(folders, this::onFolderClicked);
            recyclerView.setAdapter(folderAdapter);
        }
    }

    private void onFolderClicked(String folderName) {
        Intent intent = new Intent(this, DocumentListActivity.class);
        intent.putExtra("folderName", folderName);
        startActivity(intent);
    }
}
