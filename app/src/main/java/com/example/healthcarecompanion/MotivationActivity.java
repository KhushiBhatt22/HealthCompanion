package com.example.healthcarecompanion;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class MotivationActivity extends AppCompatActivity {
    private DBHelper     dbHelper;
    private ImageView    imgMedal;
    private TextView     tvMedalName, tvPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motivation);

        // 1) Init DBHelper & views
        dbHelper     = new DBHelper(this);
        imgMedal     = findViewById(R.id.imgMedal);
        tvMedalName  = findViewById(R.id.tvMedalName);
        tvPoints     = findViewById(R.id.tvPoints);

        // 2) Load and display stats
        loadMotivationStats();
    }

    private void loadMotivationStats() {
        // Fetch count of "Taken" reminders
        int takenCount = getTakenCount();
        int points     = takenCount * 10;
        tvPoints.setText(String.format(Locale.getDefault(), "Points: %,d", points));

        // Decide medal
        if (points >= 500) {
            imgMedal.setImageResource(R.drawable.ic_medal_gold);
            tvMedalName.setText("Gold Medal");
        } else if (points >= 300) {
            imgMedal.setImageResource(R.drawable.ic_medal_silver);
            tvMedalName.setText("Silver Medal");
        } else {
            imgMedal.setImageResource(R.drawable.ic_medal_bronze);
            tvMedalName.setText("Bronze Medal");
        }
    }

    /**
     * Utility to count all reminders with status="Taken"
     */
    private int getTakenCount() {
        // use a ? placeholder & match it with your args array
        String sql = "SELECT COUNT(*) FROM " + DBHelper.TABLE_REMINDERS +
                " WHERE "    + DBHelper.COL_REM_STATUS + "=?";
        try (Cursor c = dbHelper.getReadableDatabase()
                .rawQuery(sql, new String[]{"Taken"})) {
            if (c.moveToFirst()) {
                return c.getInt(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

}
