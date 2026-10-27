package com.example.studentregistration;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "StudentDB";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "students";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + TABLE_NAME + " (" +
                        "student_id TEXT PRIMARY KEY, " +
                        "name TEXT NOT NULL, " +
                        "enrolled INTEGER NOT NULL)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,
                          int oldVersion,
                          int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // ADD DATA
    public boolean addStudent(String id, String name,
                              boolean enrolled) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("student_id", id);
        values.put("name", name);
        values.put("enrolled", enrolled ? 1 : 0);

        long result = db.insert(TABLE_NAME, null, values);

        return result != -1;
    }

    // EDIT DATA
    public boolean updateStudent(String id, String name,
                                 boolean enrolled) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("enrolled", enrolled ? 1 : 0);

        int result = db.update(
                TABLE_NAME,
                values,
                "student_id=?",
                new String[]{id}
        );

        return result > 0;
    }

    // DELETE DATA
    public boolean deleteStudent(String id) {

        SQLiteDatabase db = getWritableDatabase();

        int result = db.delete(
                TABLE_NAME,
                "student_id=?",
                new String[]{id}
        );

        return result > 0;
    }

    // VIEW ALL DATA
    public Cursor getAllStudents() {

        SQLiteDatabase db = getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM " + TABLE_NAME +
                        " ORDER BY name ASC",
                null
        );
    }
}
