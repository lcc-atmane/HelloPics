package com.phantomhive.exil.hellopics.Img_Editor.Views.AdjustImage.SavedAdjustment;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;


public class FilterSqlite extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "filters.db";
        private static final int DATABASE_VERSION = 1;

        // Table details
        private static final String TABLE_NAME = "filters";
        private static final String COLUMN_ID = "id";
        private static final String COLUMN_NAME = "name";
        private static final String COLUMN_FILTER_VALUES = "filter_values";

        public FilterSqlite(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String createTableQuery = "CREATE TABLE " + TABLE_NAME + " ("
                    + COLUMN_ID + " TEXT PRIMARY KEY, "
                    + COLUMN_NAME + " TEXT, "
                    + COLUMN_FILTER_VALUES + " TEXT)";
            db.execSQL(createTableQuery);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }

        // Insert a new filter
        public boolean insertData(String filterName, float[] filterValues) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            // Generate a unique ID
            String id = UUID.randomUUID().toString();

            values.put(COLUMN_ID, id);
            values.put(COLUMN_NAME, filterName);
            values.put(COLUMN_FILTER_VALUES, convertArrayToString(filterValues));

            long result = db.insert(TABLE_NAME, null, values);
            return result != -1;
        }

        // Get all filters
        public ArrayList<Filter> getAllFilters() {
            ArrayList<Filter> filterList = new ArrayList<>();
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = null;

            try {
                cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

                if (cursor.moveToFirst()) {
                    int idIndex = cursor.getColumnIndex(COLUMN_ID);
                    int nameIndex = cursor.getColumnIndex(COLUMN_NAME);
                    int valuesIndex = cursor.getColumnIndex(COLUMN_FILTER_VALUES);

                    // Ensure all columns exist before accessing them
                    if (idIndex != -1 && nameIndex != -1 && valuesIndex != -1) {
                        do {
                            String id = cursor.getString(idIndex);
                            String name = cursor.getString(nameIndex);
                            String valuesStr = cursor.getString(valuesIndex);

                            Filter filter = new Filter();
                            filter.setId(id);
                            filter.setName(name);
                            filter.setFilterValues(convertStringToArray(valuesStr));

                            filterList.add(filter);
                        } while (cursor.moveToNext());
                    } else {
                        Log.e("DB_ERROR", "One or more columns are missing in the cursor!");
                    }
                }
            } catch (Exception e) {
                Log.e("DB_ERROR", "Error retrieving filters: " + e.getMessage());
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }

            return filterList;
        }


    // Delete a filter by name
        public boolean deleteData(String filterName) {
            SQLiteDatabase db = this.getWritableDatabase();
            int result = db.delete(TABLE_NAME, COLUMN_NAME + " = ?", new String[]{filterName});
            return result > 0;
        }

        // Update a filter
        public boolean updateData(String filterName, float[] newFilterValues, String newFilterName) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(COLUMN_NAME, newFilterName);
            values.put(COLUMN_FILTER_VALUES, convertArrayToString(newFilterValues));

            int result = db.update(TABLE_NAME, values, COLUMN_NAME + " = ?", new String[]{filterName});
            return result > 0;
        }

        // Helper method to convert float array to string
        private String convertArrayToString(float[] array) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < array.length; i++) {
                sb.append(array[i]);
                if (i < array.length - 1) {
                    sb.append(",");
                }
            }
            return sb.toString();
        }

        // Helper method to convert string to float array
        private float[] convertStringToArray(String str) {
            if (str == null || str.isEmpty()) {
                return new float[0];
            }

            String[] items = str.split(",");
            float[] results = new float[items.length];

            for (int i = 0; i < items.length; i++) {
                try {
                    results[i] = Float.parseFloat(items[i]);
                } catch (NumberFormatException e) {
                    results[i] = 0.0f;
                }
            }

            return results;
        }
}