package com.wamufi.particulates.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbOpenHelper extends SQLiteOpenHelper {

    private Context mContext;

    public static final String DATABASE_NAME = "ParticulatesDb.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "search";
    public static final String SEARCH_NAME = "search_name"; // 검색한 읍/면/동 이름
    public static final String STATION_NAME = "station_name"; // 측정소 읍/면/동 이름
    public static final String STATION_ADDR = "station_addr";

    public DbOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.v("DbOpenHelper", "DbOpenHelper");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + " ( _id INTEGER PRIMARY KEY, " + SEARCH_NAME + " TEXT ," + STATION_NAME + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.w(DbOpenHelper.class.getName(), "Upgrading database from version " + i + " to " + i1 + ", which will destroy all old data.");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
