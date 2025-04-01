package com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.mainActivity.HistorySettings;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class HistorySqliteDataBase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "HistoryDatabase.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "History";
    private static final String COLUMN_ID = "id";
    private static final String ImagePath = "ImagePath";
    private static final String ImagePhase = "ImagePhase";

    public HistorySqliteDataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ImagePath + " TEXT, " +
                ImagePhase + " TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Insert Data
    public boolean insertData(String str1, String str2) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ImagePath, str1);
        values.put(ImagePhase, str2);

        long result = db.insert(TABLE_NAME, null, values);
        db.close();
        return result != -1; // Returns true if insertion is successful
    }

    // Retrieve Data
    public Cursor getAllData() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    // Delete Data
    public boolean deleteData(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_NAME, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return result > 0; // Returns true if deletion is successful
    }
    public boolean deleteAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_NAME, null, null); // Deletes all rows
        db.close();
        return result > 0; // Returns true if deletion is successful
    }
    // **Retrieve Data into ArrayList**
//    public ArrayList<Item> getAllDataAtOnce() {
//        ArrayList<Item> dataList = new ArrayList<>();
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
//
//        if (cursor.moveToFirst()) {
//            do {
//                String string1 = cursor.getString(cursor.getColumnIndexOrThrow(ImagePath));
//                String string2 = cursor.getString(cursor.getColumnIndexOrThrow(ImagePhase));
//                dataList.add(new Item(string1, string2));
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//        db.close();
//        return dataList;
//    }


}
