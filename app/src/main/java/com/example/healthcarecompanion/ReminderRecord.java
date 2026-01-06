package com.example.healthcarecompanion;

public class ReminderRecord {
     private int id;
     private String medicineName, dosage, time, date, status;
    public ReminderRecord(int id, String medicineName, String dosage,
                          String time, String date, String status) {
        this.id           = id;
        this.medicineName = medicineName;
        this.dosage       = dosage;
        this.time         = time;
        this.date         = date;
        this.status       = status;
    }

    public String getMedicineName() { return medicineName; }
    public String getDosage()       { return dosage; }
    public String getTime()         { return time; }
    public String getDate()         { return date; }
    public String getStatus()       { return status; }
}
