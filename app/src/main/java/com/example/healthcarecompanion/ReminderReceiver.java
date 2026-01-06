package com.example.healthcarecompanion;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class ReminderReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        long remId = intent.getLongExtra("reminderId", -1);
        if (remId != -1) {
            // mark as taken in the DB
            DBHelper db = new DBHelper(context);
            db.updateReminderStatus((int)remId, "Taken");
        }

        String reminderLabel = intent.getStringExtra("reminderLabel");
        if (reminderLabel == null) reminderLabel = "Reminder";

        NotificationManager nm =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "reminder_channel";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            nm.createNotificationChannel(new NotificationChannel(
                    channelId, "Reminders", NotificationManager.IMPORTANCE_HIGH
            ));
        }

        NotificationCompat.Builder bx = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Healthcare Companion")
                .setContentText(reminderLabel)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        nm.notify((int)System.currentTimeMillis(), bx.build());
    }
}

