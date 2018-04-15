package com.wamufi.particulates.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.wamufi.particulates.models.DbModel;

import java.util.ArrayList;

public class DbManager {

    private Context mContext;

    private DbOpenHelper mDbOpenHelper;
    private SQLiteDatabase mSQLiteDatabase;

    public DbManager(Context context) {
        mContext = context;
        mDbOpenHelper = new DbOpenHelper(context);
    }

    public void Open() throws SQLException {
        mSQLiteDatabase = mDbOpenHelper.getWritableDatabase();

    }

    public void Close() {
        mDbOpenHelper.close();
    }

    /**
     * Queries
     */
    public void insertStation(String searchName, String stationName) {
        mSQLiteDatabase = mDbOpenHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DbOpenHelper.SEARCH_NAME, searchName);
        contentValues.put(DbOpenHelper.STATION_NAME, stationName);
        long newRowId = mSQLiteDatabase.insert(DbOpenHelper.TABLE_NAME, null, contentValues);

        Toast.makeText(mContext, "The new Row Id is " + newRowId, Toast.LENGTH_LONG).show();
//        Snackbar.make()
        Log.v("DbManager", "insertStation: newRowId - " + newRowId);
    }

    public void selectStation(int id) {
        mSQLiteDatabase = mDbOpenHelper.getReadableDatabase();

        Cursor cursor = mSQLiteDatabase.query(DbOpenHelper.TABLE_NAME, new String[]{"_id", DbOpenHelper.SEARCH_NAME, DbOpenHelper.STATION_NAME}, "_id=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null) cursor.moveToFirst();
        Log.v("DbManager", "selectSelection: cursor.getString(1) - " + String.valueOf(cursor.getString(1)));
        cursor.close();
    }

    public ArrayList<DbModel> selectAllStations() {
        mSQLiteDatabase = mDbOpenHelper.getReadableDatabase();
        ArrayList<DbModel> dbModels = new ArrayList<DbModel>();
        DbModel dbModel = null;

        Cursor cursor = mSQLiteDatabase.query(DbOpenHelper.TABLE_NAME, new String[]{"_id", DbOpenHelper.SEARCH_NAME, DbOpenHelper.STATION_NAME}, null, null, null, null, null);
        while (cursor.moveToNext()) {
//            long itemId = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
            dbModel = new DbModel();
            dbModel.setSearchName(String.valueOf(cursor.getString(1)));
            dbModel.setStationName(String.valueOf(cursor.getString(2)));
            dbModels.add(dbModel);
        }
        Log.v("DbManager", "selectAllStations(): dbModels.size() - " + dbModels.size());

        cursor.close();

        return dbModels;
    }
}
