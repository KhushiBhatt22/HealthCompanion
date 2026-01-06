package com.example.healthcarecompanion.models;

public class MedicineEntry {
    private final String name;
    private final String dosage;
    private final String timeOfDay;

    public MedicineEntry(String name, String dosage, String timeOfDay) {
        this.name      = name;
        this.dosage    = dosage;
        this.timeOfDay = timeOfDay;
    }

    @Override
    public String toString() {
        return name + " — " + dosage + " at " + timeOfDay;
    }
}
