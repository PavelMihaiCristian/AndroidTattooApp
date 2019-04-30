package com.example.tattooshopapp.persistance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.tattooshopapp.model.ForecastItem;
import com.example.tattooshopapp.persistance.ForecastContract.ForecastEntry;

import java.util.ArrayList;
import java.util.List;

public class ForecastsDAO {
    private SQLiteDatabase writeDatabase;
    private SQLiteDatabase readDatabase;
    private ForcastDbHelper forcastDbHelper;

    public ForecastsDAO(Context context) {
        forcastDbHelper = new ForcastDbHelper(context);
        writeDatabase = forcastDbHelper.getWritableDatabase();
        readDatabase = forcastDbHelper.getReadableDatabase();
    }

    public void insertForecast(ForecastItem forecastItem) {
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ForecastEntry.COLUMN_DESCRIPTION, forecastItem.getDescription());
        values.put(ForecastEntry.COLUMN_LOCATION, forecastItem.getLocation());
        values.put(ForecastEntry.COLUMN_TEMPERATURE, forecastItem.getTemperature());
        values.put(ForecastEntry.COLUMN_PRESSURE, forecastItem.getPressure());
        values.put(ForecastEntry.COLUMN_HUMIDITY, forecastItem.getHumidity());

        // Insert the new row, returning the primary key value of the new row
        long newRowId = writeDatabase.insert(ForecastEntry.TABLE_NAME, null, values);
    }

    public ArrayList<ForecastItem> readForecastRecords() {
        String[] projection = {
                ForecastEntry.COLUMN_LOCATION,
                ForecastEntry.COLUMN_TEMPERATURE,
                ForecastEntry.COLUMN_DESCRIPTION
        };

        // Filter results WHERE "title" = 'My Title'
        String selection = ForecastEntry.COLUMN_LOCATION + " = ?";
        String[] selectionArgs = {"Horsens"};

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                ForecastEntry.COLUMN_DATE + " DESC";

        Cursor cursor = readDatabase.query(
                ForecastEntry.TABLE_NAME,   // The table to query
                null,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        ArrayList<ForecastItem> forecastItems = new ArrayList<>();
        while (cursor.moveToNext()) {
            String description = cursor.getString(cursor.getColumnIndex(ForecastEntry.COLUMN_DESCRIPTION));
            String location = cursor.getString(cursor.getColumnIndex(ForecastEntry.COLUMN_LOCATION));
            double temp = cursor.getDouble(cursor.getColumnIndex(ForecastEntry.COLUMN_TEMPERATURE));

            ForecastItem forecastItem = new ForecastItem(description, location, temp);
            forecastItems.add(forecastItem);
        }
        cursor.close();
        return forecastItems;
    }

    public int deleteForecats() {
        // Define 'where' part of query.
        String selection = ForecastEntry.COLUMN_LOCATION + " NOT LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = {"Horsens"};
        // Issue SQL statement.
        int deletedRows = writeDatabase.delete(ForecastEntry.TABLE_NAME, selection, selectionArgs);
        return deletedRows;
    }
}
