package com.example.healthcarecompanion;
import android.annotation.SuppressLint;
import android.content.ContentValues; import android.content.Context; import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase; import android.database.sqlite.SQLiteOpenHelper;

import com.example.healthcarecompanion.models.MedicineEntry;

import java.util.ArrayList; import java.util.List;
public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "healthapp.db";
    private static final int DATABASE_VERSION = 3; // bump version

    // Documents table
    public static final String TABLE_DOC    = "documents";
    public static final String COL_ID       = "id";
    public static final String COL_FOLDER   = "folder_name";
    public static final String COL_FILENAME = "file_name";
    public static final String COL_PATH     = "file_path";

    // Reminders table
    public static final String TABLE_REMINDERS   = "reminders";
    public static final String COL_REM_ID        = "id";
    public static final String COL_MED_NAME      = "medicine_name";
    public static final String COL_DOSAGE        = "dosage";
    public static final String COL_REM_TIME      = "schedule_time";
    public static final String COL_REM_DATE      = "rem_date";
    public static final String COL_REM_STATUS    = "status";

    public static final String TABLE_MED      = "medicine_records";
    public static final String COL_MED_ID     = "id";
    public static final String COL_MED_DOSAGE = "dosage";
    public static final String COL_MED_TIME   = "time";
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create documents table
        String sqlDoc = "CREATE TABLE " + TABLE_DOC + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_FOLDER + " TEXT, "
                + COL_FILENAME + " TEXT, "
                + COL_PATH + " TEXT"
                + ")";
        db.execSQL(sqlDoc);

        // create reminders table
        String sqlRem = "CREATE TABLE " + TABLE_REMINDERS + " ("
                + COL_REM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_MED_NAME + " TEXT, "
                + COL_DOSAGE + " TEXT, "
                + COL_REM_TIME + " TEXT, "
                + COL_REM_DATE + " TEXT, "
                + COL_REM_STATUS + " TEXT"
                + ")";
        db.execSQL(sqlRem);

        String sqlMed = "CREATE TABLE " + TABLE_MED + " ("
                + COL_MED_ID     + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_MED_NAME   + " TEXT, "
                + COL_MED_DOSAGE + " TEXT, "
                + COL_MED_TIME   + " TEXT"
                + ")";
        db.execSQL(sqlMed);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        if (oldV < 2) {
            String sqlRem = "CREATE TABLE " + TABLE_REMINDERS + " ("
                    + COL_REM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COL_MED_NAME + " TEXT, "
                    + COL_DOSAGE + " TEXT, "
                    + COL_REM_TIME + " TEXT, "
                    + COL_REM_DATE + " TEXT, "
                    + COL_REM_STATUS + " TEXT"
                    + ")";
            db.execSQL(sqlRem);
        }
        if (oldV < 3) {
            // create medicine table on upgrade
            String sqlMed = "CREATE TABLE " + TABLE_MED + " ("
                    + COL_MED_ID     + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COL_MED_NAME   + " TEXT, "
                    + COL_MED_DOSAGE + " TEXT, "
                    + COL_MED_TIME   + " TEXT"
                    + ")";
            db.execSQL(sqlMed);
        }

    }

    // -- DOCUMENTS methods --
    public long insertDocument(String folderName, String fileName, String filePath) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_FOLDER,   folderName);
        cv.put(COL_FILENAME, fileName);
        cv.put(COL_PATH,     filePath);
        return db.insert(TABLE_DOC, null, cv);
    }
    @SuppressLint("Range")
    public List<String> getAllFolders() {
        List<String> folders = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT DISTINCT " + COL_FOLDER + " FROM " + TABLE_DOC,
                null
        );
        if (c.moveToFirst()) {
            do {
                folders.add(c.getString(c.getColumnIndex(COL_FOLDER)));
            } while (c.moveToNext());
        }
        c.close();
        return folders;
    }

    @SuppressLint("Range")
    public List<MedicineRecord> getDocumentsByFolder(String folderName) {
        List<MedicineRecord> docs = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT * FROM " + TABLE_DOC + " WHERE " + COL_FOLDER + "=?",
                new String[]{folderName}
        );
        if (c.moveToFirst()) {
            do {
                docs.add(new MedicineRecord(
                        c.getInt(c.getColumnIndex(COL_ID)),
                        c.getString(c.getColumnIndex(COL_FOLDER)),
                        c.getString(c.getColumnIndex(COL_FILENAME)),
                        c.getString(c.getColumnIndex(COL_PATH))
                ));
            } while (c.moveToNext());
        }
        c.close();
        return docs;
    }

    // -- REMINDERS methods --
    public long insertReminder(String medName, String dosage,
                               String time, String date, String status) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_MED_NAME,   medName);
        cv.put(COL_DOSAGE,     dosage);
        cv.put(COL_REM_TIME,   time);
        cv.put(COL_REM_DATE,   date);
        cv.put(COL_REM_STATUS, status);
        return db.insert(TABLE_REMINDERS, null, cv);
    }

    @SuppressLint("Range")
    public List<ReminderRecord> getRemindersByDate(String date) {
        List<ReminderRecord> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(
                TABLE_REMINDERS, null,
                COL_REM_DATE + "=?",
                new String[]{date},
                null, null,
                COL_REM_TIME + " ASC"
        );
        if (c.moveToFirst()) {
            do {
                list.add(new ReminderRecord(
                        c.getInt(c.getColumnIndex(COL_REM_ID)),
                        c.getString(c.getColumnIndex(COL_MED_NAME)),
                        c.getString(c.getColumnIndex(COL_DOSAGE)),
                        c.getString(c.getColumnIndex(COL_REM_TIME)),
                        c.getString(c.getColumnIndex(COL_REM_DATE)),
                        c.getString(c.getColumnIndex(COL_REM_STATUS))
                ));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }
    public int updateReminderStatus(int id, String status) {
        ContentValues cv = new ContentValues();
        cv.put(COL_REM_STATUS, status);
        return getWritableDatabase()
                .update(TABLE_REMINDERS, cv, COL_REM_ID+"=?", new String[]{String.valueOf(id)});
    }

    /** Insert a medicine record */
    public long insertMedicine(String name, String dosage, String time) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_MED_NAME,   name);
        cv.put(COL_MED_DOSAGE, dosage);
        cv.put(COL_MED_TIME,   time);
        return db.insert(TABLE_MED, null, cv);
    }

    /** Fetch all saved medicines (newest first) */
    @SuppressLint("Range")
    public List<MedicineEntry> getAllMedicines() {
        List<MedicineEntry> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(
                TABLE_MED,
                null, null, null, null, null,
                COL_MED_ID + " DESC"
        );
        while (c.moveToNext()) {
            list.add(new MedicineEntry(
                    c.getString(c.getColumnIndex(COL_MED_NAME)),
                    c.getString(c.getColumnIndex(COL_MED_DOSAGE)),
                    c.getString(c.getColumnIndex(COL_MED_TIME))
            ));
        }
        c.close();
        return list;
    }
}

