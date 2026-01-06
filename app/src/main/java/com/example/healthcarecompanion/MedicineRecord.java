package com.example.healthcarecompanion;

public class MedicineRecord {

    private int id;
    private String folderName;
    private String fileName;
    private String filePath;

    public MedicineRecord(int id, String folderName, String fileName, String filePath) {
        this.id = id;
        this.folderName = folderName;
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public int getId() {
        return id;
    }

    public String getFolderName() {
        return folderName;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }
}
