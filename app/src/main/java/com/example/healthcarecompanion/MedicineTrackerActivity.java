package com.example.healthcarecompanion;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.healthcarecompanion.models.MedicineEntry;

import java.util.LinkedList;
import java.util.List;

public class MedicineTrackerActivity extends AppCompatActivity {

    private EditText editMedicineName, editDosage, editTimeOfDay;
    private Button   btnAdd, btnDisplay;
    private TextView tvDisplay;
    private DBHelper dbHelper;
    // In-memory storage for last 3 entries
   // private final LinkedList<MedicineEntry> medicineList = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_tracker);
        dbHelper = new DBHelper(this);

        editMedicineName = findViewById(R.id.editMedicineName);
        editDosage       = findViewById(R.id.editDosage);
        editTimeOfDay    = findViewById(R.id.editTimeOfDay);
        btnAdd           = findViewById(R.id.btnAdd);
        btnDisplay       = findViewById(R.id.btnDisplay);
        tvDisplay        = findViewById(R.id.tvDisplay);

        btnAdd.setOnClickListener(v -> addMedicine());
        btnDisplay.setOnClickListener(v -> displayMedicines());
    }

    private void addMedicine() {
        String name   = editMedicineName.getText().toString().trim();
        String dosage = editDosage.getText().toString().trim();
        String time   = editTimeOfDay.getText().toString().trim();

        if (name.isEmpty() || dosage.isEmpty() || time.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        long id = dbHelper.insertMedicine(name, dosage, time);
        if (id != -1) {
            Toast.makeText(this, "Medicine saved", Toast.LENGTH_SHORT).show();
            // Clear inputs
            editMedicineName.setText("");
            editDosage.setText("");
            editTimeOfDay.setText("");
        } else {
            Toast.makeText(this, "Error saving medicine", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayMedicines() {
        List<MedicineEntry> meds = dbHelper.getAllMedicines();
        if (meds.isEmpty()) {
            tvDisplay.setText("No medicine records found.");
        } else {
            StringBuilder sb = new StringBuilder();
            for (MedicineEntry m : meds) {
                sb.append(m.toString()).append("\n");
            }
            tvDisplay.setText(sb.toString());
        }
    }
}
