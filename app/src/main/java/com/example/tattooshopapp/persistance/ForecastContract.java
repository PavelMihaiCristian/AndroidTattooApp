package com.example.tattooshopapp.persistance;

import android.provider.BaseColumns;

public final class ForecastContract {
    private ForecastContract() {}
    public static final class ForecastEntry implements BaseColumns {
        public final static String TABLE_NAME = "forecasts";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_DESCRIPTION ="description";
        public final static String COLUMN_LOCATION ="location";
        public final static String COLUMN_TEMPERATURE = "temperature";
        public final static String COLUMN_PRESSURE = "pressure";
        public final static String COLUMN_HUMIDITY = "humidity";
        public final static String COLUMN_DATE = "date";
    }
}
