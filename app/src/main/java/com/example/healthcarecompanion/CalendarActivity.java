package com.example.healthcarecompanion;

import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
public class CalendarActivity extends AppCompatActivity {
    private CalendarView calendarView;
    private TextView txtSelectedDate;
    private RecyclerView rvTasks;
    private DBHelper dbHelper;
    private TaskAdapter
        adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calendar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v,
                                                                            insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });
        dbHelper = new DBHelper(this);
        calendarView = findViewById(R.id.calendarView);
        txtSelectedDate = findViewById(R.id.txtSelectedDate);
        rvTasks = findViewById(R.id.rvTasks);
        rvTasks.setLayoutManager(new LinearLayoutManager(this));
        // initial load for today
        loadTasksForDate(new Date(calendarView.getDate()));

        calendarView.setOnDateChangeListener((widget, y, m, d) -> {
            Calendar cal = Calendar.getInstance();
            cal.set(y, m, d,0,0,0);
            loadTasksForDate(cal.getTime());
        });

    }
    private void loadTasksForDate(Date date) {
        String fmtDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(date);
        txtSelectedDate.setText(
                new SimpleDateFormat("EEEE, MMM dd", Locale.getDefault())
                        .format(date)
        );
        List<ReminderRecord> tasks = dbHelper.getRemindersByDate(fmtDate);
        adapter = new TaskAdapter(tasks);
        rvTasks.setAdapter(adapter);
    }
    @Override
    protected void onResume() {
        super.onResume();
        long millis = calendarView.getDate();       // epoch millis at midnight
        Date selected = new Date(millis);
        loadTasksForDate(selected);
    }



}