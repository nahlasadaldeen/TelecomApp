package com.tele.ramitelecom.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    private static final String TAG = "DB TAG";    // TAG
    private static final String DATABASE_NAME = "TelecomDatabase";    // Database Name
    private static final int DATABASE_Version = 1;    // Database Version

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_Version);
    }

    public void createTables(SQLiteDatabase db) {
        try {
            db.execSQL(UserTable.CREATE_STATEMENT);
            db.execSQL(TransferTable.CREATE_STATEMENT);
            db.execSQL(CenterTable.CREATE_STATEMENT);
            db.execSQL(UnitPriceTable.CREATE_STATEMENT);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

    }

    public void onCreate(SQLiteDatabase db) {
        createTables(db);
    }

    public void dropTables(SQLiteDatabase db) {
        try {
            Log.e(TAG, "Drop Tables");
            db.execSQL(UserTable.DROP_STATEMENT);
            db.execSQL(TransferTable.DROP_STATEMENT);
            db.execSQL(CenterTable.DROP_STATEMENT);
            db.execSQL(UnitPriceTable.DROP_STATEMENT);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            Log.e(TAG, "OnUpgrade");
            dropTables(db);
            onCreate(db);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }
}