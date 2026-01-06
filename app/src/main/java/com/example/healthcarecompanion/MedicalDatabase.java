package com.example.healthcarecompanion;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class MedicalDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "medical_storage.db";
    private static final int DATABASE_VERSION = 1;

    // Tables
    private static final String TABLE_FOLDERS = "folders";
    private static final String TABLE_DOCUMENTS = "documents";

    // Folders columns
    private static final String FIELD_FOLDER_ID = "_id";
    private static final String FIELD_FOLDER_NAME = "name";

    // Documents columns
    private static final String FIELD_DOCUMENT_ID = "_id";
    private static final String FIELD_DOCUMENT_NAME = "name";
    private static final String FIELD_DOCUMENT_URI = "uri";
    private static final String FIELD_DOCUMENT_FOLDER_ID = "folder_id";

    public MedicalDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create folders table
        db.execSQL("CREATE TABLE " + TABLE_FOLDERS + " (" +
                FIELD_FOLDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FIELD_FOLDER_NAME + " TEXT UNIQUE NOT NULL);");

        // Create documents table
        db.execSQL("CREATE TABLE " + TABLE_DOCUMENTS + " (" +
                FIELD_DOCUMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FIELD_DOCUMENT_NAME + " TEXT NOT NULL, " +
                FIELD_DOCUMENT_URI + " TEXT NOT NULL, " +
                FIELD_DOCUMENT_FOLDER_ID + " INTEGER NOT NULL, " +
                "FOREIGN KEY(" + FIELD_DOCUMENT_FOLDER_ID + ") REFERENCES " + TABLE_FOLDERS + "(" + FIELD_FOLDER_ID + "));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop tables and recreate on upgrade for simplicity
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOCUMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOLDERS);
        onCreate(db);
    }

    // Folders CRUD
    public long addFolder(String name) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FIELD_FOLDER_NAME, name);
        return db.insertWithOnConflict(TABLE_FOLDERS, null, values, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public List<String> getAllFolders() {
        List<String> folders = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_FOLDERS, new String[]{FIELD_FOLDER_NAME}, null, null, null, null, FIELD_FOLDER_NAME + " ASC");
        if (cursor.moveToFirst()) {
            do {
                folders.add(cursor.getString(cursor.getColumnIndexOrThrow(FIELD_FOLDER_NAME)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return folders;
    }

    public long getFolderId(String folderName) {
        long id = -1;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_FOLDERS, new String[]{FIELD_FOLDER_ID},
                FIELD_FOLDER_NAME + "=?", new String[]{folderName}, null, null, null);
        if (cursor.moveToFirst()) {
            id = cursor.getLong(cursor.getColumnIndexOrThrow(FIELD_FOLDER_ID));
        }
        cursor.close();
        return id;
    }

    // Documents CRUD
    public long addDocument(String name, String uri, String folderName) {
        long folderId = getFolderId(folderName);
        if (folderId == -1) {
            folderId = addFolder(folderName); // create folder if not exists
        }
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FIELD_DOCUMENT_NAME, name);
        values.put(FIELD_DOCUMENT_URI, uri);
        values.put(FIELD_DOCUMENT_FOLDER_ID, folderId);
        return db.insert(TABLE_DOCUMENTS, null, values);
    }

    // Get documents by folder name
    public Cursor getDocumentsByFolder(String folderName) {
        long folderId = getFolderId(folderName);
        if (folderId == -1) return null;

        SQLiteDatabase db = getReadableDatabase();
        return db.query(TABLE_DOCUMENTS,
                new String[]{FIELD_DOCUMENT_ID, FIELD_DOCUMENT_NAME, FIELD_DOCUMENT_URI},
                FIELD_DOCUMENT_FOLDER_ID + "=?",
                new String[]{String.valueOf(folderId)},
                null, null, FIELD_DOCUMENT_NAME + " ASC");
    }

    // Delete document by id
    public int deleteDocument(long documentId) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_DOCUMENTS, FIELD_DOCUMENT_ID + "=?", new String[]{String.valueOf(documentId)});
    }
}
