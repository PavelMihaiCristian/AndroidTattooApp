package com.example.tattooshopapp.persistance;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.tattooshopapp.persistance.ForecastContract.ForecastEntry;

public class ForcastDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "forecasts.db";
    private static final int DATABASE_VERSION = 1;

    public ForcastDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_FORECASTS_TABLE = "CREATE TABLE " + ForecastEntry.TABLE_NAME + " ("
                + ForecastEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ForecastEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, "
                + ForecastEntry.COLUMN_LOCATION + " TEXT NOT NULL, "
                + ForecastEntry.COLUMN_TEMPERATURE + " REAL, "
                + ForecastEntry.COLUMN_PRESSURE + " REAL NOT NULL, "
                + ForecastEntry.COLUMN_HUMIDITY + " REAL NOT NULL, "
                + ForecastEntry.COLUMN_DATE + " NUMERIC NOT NULL DEFAULT current_timestamp);";
        db.execSQL(SQL_CREATE_FORECASTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}

