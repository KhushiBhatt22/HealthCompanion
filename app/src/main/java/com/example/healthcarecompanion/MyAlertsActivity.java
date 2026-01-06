package com.example.healthcarecompanion;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MyAlertsActivity extends AppCompatActivity {
    private Spinner    spinnerType;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private Button     btnSetReminder;
    private DBHelper   dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_alerts);

        dbHelper = new DBHelper(this);

        spinnerType    = findViewById(R.id.spinner_alert_type);
        datePicker     = findViewById(R.id.date_picker);
        timePicker     = findViewById(R.id.time_picker);
        btnSetReminder = findViewById(R.id.btn_set_reminder);

        spinnerType.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"Medicine Reminder", "Doctor Appointment"}
        ));
        timePicker.setIs24HourView(true);

        // Only show the confirm dialog here
        btnSetReminder.setOnClickListener(v -> showConfirmDialog());

        // request exact alarms on Android 12+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AlarmManager am = getSystemService(AlarmManager.class);
            if (am != null && !am.canScheduleExactAlarms()) {
                startActivity(new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM));
            }
        }
    }

    private void showConfirmDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Reminder")
                .setMessage("Do you want to set this reminder?")
                .setPositiveButton("Yes", (dlg, which) -> {
                    actuallySetReminder();               // ← single insertion point
                    Toast.makeText(this,
                            "Reminder created successfully",
                            Toast.LENGTH_SHORT
                    ).show();
                })
                .setNegativeButton("No", (dlg, which) ->
                        Toast.makeText(this, "Reminder not set", Toast.LENGTH_SHORT).show()
                )
                .show();
    }

    private void actuallySetReminder() {
        // Gather date & time
        int hour   = timePicker.getHour();
        int minute = timePicker.getMinute();
        Calendar cal = Calendar.getInstance();
        cal.set(
                datePicker.getYear(),
                datePicker.getMonth(),
                datePicker.getDayOfMonth(),
                hour, minute, 0
        );

        // Format date & time strings
        String dateStr = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(cal.getTime());
        String timeStr = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);

        String medName = spinnerType.getSelectedItem().toString();

        // 1) INSERT exactly once
        long remId = dbHelper.insertReminder(medName, "", timeStr, dateStr, "Pending");

        // 2) Schedule alarm referencing that single row (for precision you can pass remId)
        scheduleAlarm(cal.getTimeInMillis(), remId);

        // (No further DB writes here)
    }

    private void scheduleAlarm(long triggerAtMillis, long reminderId) {
        if (triggerAtMillis <= System.currentTimeMillis()) return;

        Intent intent = new Intent(this, ReminderReceiver.class)
                .putExtra("reminderId", reminderId);

        PendingIntent pi = PendingIntent.getBroadcast(
                this, (int)reminderId, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager am = getSystemService(AlarmManager.class);
        if (am != null) {
            am.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP, triggerAtMillis, pi
            );
        }
    }
}
