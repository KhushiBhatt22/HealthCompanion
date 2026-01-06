package com.example.healthcarecompanion;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PersonalDetailActivity extends AppCompatActivity {
    EditText editName, editAge;
    AutoCompleteTextView editCondition;
    Button btnSubmit;
    TextView tvSuggestions;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_personal_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.scroll_view), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Linking views
        editName = findViewById(R.id.edit_name);
        editAge = findViewById(R.id.edit_age);
        editCondition = findViewById(R.id.edit_condition);
        btnSubmit = findViewById(R.id.btn_submit);
        tvSuggestions = findViewById(R.id.tv_suggestions);

        // List of common medical conditions
        String[] conditions = new String[]{
                "Diabetes", "Hypertension", "Asthma", "Back Pain", "Anxiety", "Obesity"
        };

        // Set suggestions to AutoCompleteTextView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, conditions);
        editCondition.setAdapter(adapter);

        btnSubmit.setOnClickListener(view -> {
            String name = editName.getText().toString().trim();
            String age = editAge.getText().toString().trim();
            String condition = editCondition.getText().toString().trim();

            if (name.isEmpty() || age.isEmpty() || condition.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }
            String suggestion = getSuggestionForCondition(condition);
            tvSuggestions.setText("Recommended Activities:\n" + suggestion);
            tvSuggestions.setVisibility(View.VISIBLE);

        });
    }

    // Suggest exercises and yoga poses based on medical condition
    private String getSuggestionForCondition(String condition) {
        switch (condition.toLowerCase()) {
            case "diabetes":
                return "• Morning walk\n• Gentle Yoga (e.g., Surya Namaskar)\n• Deep breathing";
            case "hypertension":
                return "• Meditation\n• Pranayama (breath control)\n• Light cardio (e.g., cycling)";
            case "asthma":
                return "• Pursed-lip breathing\n• Seated forward bend\n• Low-impact walking";
            case "back pain":
                return "• Cat-Cow stretch\n• Bridge pose\n• Child's pose (Balasana)";
            case "anxiety":
                return "• Guided meditation\n• Restorative Yoga\n• Journaling or Mindful Breathing";
            case "obesity":
                return "• Brisk walking\n• Swimming\n• Power Yoga or Chair Yoga";
            default:
                return "• General stretching\n• Walking\n• Breathing exercises";
        }
    }
}