package com.example.healthcarecompanion;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private ImageView userInfoIcon;
    private TextView  userInfoLabel;
    private ImageView trackIcon;
    private ImageView alertIcon;
    private TextView  alertLabel;
    private ImageView storeDocIcon;
    private TextView  storeDocLabel;
    private ImageView calendarIcon;
    private TextView  calendarLabel;
    private ImageView motivationIcon;
    private TextView  motivationLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // apply edge-to-edge padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        // 1 Profile
        userInfoIcon  = findViewById(R.id.UserInfo);
        userInfoLabel = findViewById(R.id.label_userinfo);
        View.OnClickListener openProfile = v ->
                startActivity(new Intent(this, PersonalDetailActivity.class));
        userInfoIcon.setOnClickListener(openProfile);
        userInfoLabel.setOnClickListener(openProfile);

        // 2️Medicine Tracker
        trackIcon = findViewById(R.id.trackdata);
        trackIcon.setOnClickListener(v ->
                startActivity(new Intent(this, MedicineTrackerActivity.class)));

        // 3️Reminders
        alertIcon  = findViewById(R.id.setreminder);
        alertLabel = findViewById(R.id.label_setreminder);
        View.OnClickListener openAlerts = v ->
                startActivity(new Intent(this, MyAlertsActivity.class));
        alertIcon.setOnClickListener(openAlerts);
        alertLabel.setOnClickListener(openAlerts);

        // 4️ Store Documents (opens folder view)
        storeDocIcon  = findViewById(R.id.storedoc);
        storeDocLabel = findViewById(R.id.label_storedoc);
        View.OnClickListener openFolders = v ->
                startActivity(new Intent(this, FolderActivity.class));
        storeDocIcon.setOnClickListener(openFolders);
        storeDocLabel.setOnClickListener(openFolders);

        // 5️Calendar (once you implement CalendarActivity)
        calendarIcon  = findViewById(R.id.calender);
        calendarLabel = findViewById(R.id.label_calender);
        View.OnClickListener openCalendar = v ->
                startActivity(new Intent(this, CalendarActivity.class));
        calendarIcon.setOnClickListener(openCalendar);
        calendarLabel.setOnClickListener(openCalendar);

        // 6️Motivation (once you implement MotivationActivity)
        motivationIcon  = findViewById(R.id.motivation);
        motivationLabel = findViewById(R.id.label_motivation);
        View.OnClickListener openMotivation = v -> {
            Intent intent = new Intent(MainActivity.this, MotivationActivity.class);
            startActivity(intent);
        };
        motivationIcon.setOnClickListener(openMotivation);
        motivationLabel.setOnClickListener(openMotivation);

        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name        = "Reminder Channel";
            String description       = "Channel for reminders";
            int importance           = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel chan = new NotificationChannel(
                    "reminder_channel", name, importance
            );
            chan.setDescription(description);
            NotificationManager mgr = getSystemService(NotificationManager.class);
            mgr.createNotificationChannel(chan);
        }
    }


}
