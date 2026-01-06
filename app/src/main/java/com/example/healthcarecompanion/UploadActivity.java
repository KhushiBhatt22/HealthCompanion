package com.example.healthcarecompanion;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts.OpenDocument;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class UploadActivity extends AppCompatActivity {
    private EditText edtFolderName;
    private TextView txtFileName;
    private Uri fileUri;
    private DBHelper dbHelper;

    // Launcher for the system file picker
    private ActivityResultLauncher<String[]> pickDocumentLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        edtFolderName = findViewById(R.id.edtFolderName);
        txtFileName   = findViewById(R.id.txtFileName);
        Button btnSelectFile = findViewById(R.id.btnSelectFile);
        Button btnUpload     = findViewById(R.id.btnUpload);

        dbHelper = new DBHelper(this);

        // Register the OpenDocument launcher
        pickDocumentLauncher = registerForActivityResult(
                new OpenDocument(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        if (uri != null) {
                            fileUri = uri;
                            txtFileName.setText(getFileName(uri));
                        }
                    }
                }
        );

        btnSelectFile.setOnClickListener(v -> pickDocumentLauncher.launch(new String[]{"*/*"}));
        btnUpload    .setOnClickListener(v -> uploadFile());
    }

    /** Safely extract DISPLAY_NAME or fallback to last path segment */
    private String getFileName(Uri uri) {
        String result = null;
        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
            try (Cursor cursor = getContentResolver()
                    .query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int idx = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (idx != -1) {
                        result = cursor.getString(idx);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }

    /** Validate inputs, copy file into app storage, and insert DB record */
    private void uploadFile() {
        String folderName = edtFolderName.getText().toString().trim();
        if (folderName.isEmpty()) {
            edtFolderName.setError("Enter folder name");
            return;
        }
        if (fileUri == null) {
            Toast.makeText(this, "Select a file first", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            File folder = new File(getFilesDir(), folderName);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            String fileName = getFileName(fileUri);
            File destFile = new File(folder, fileName);

            try (InputStream in = getContentResolver().openInputStream(fileUri);
                 OutputStream out = new FileOutputStream(destFile)) {
                byte[] buffer = new byte[1024];
                int    read;
                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
            }

            long id = dbHelper.insertDocument(folderName, fileName, destFile.getAbsolutePath());
            if (id != -1) {
                Toast.makeText(this, "File uploaded", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Upload failed", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
