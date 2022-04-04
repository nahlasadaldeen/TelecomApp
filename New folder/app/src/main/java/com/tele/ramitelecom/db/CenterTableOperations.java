package com.tele.ramitelecom.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tele.ramitelecom.ui.users.CenterModel;

import java.util.ArrayList;
import java.util.List;

public class CenterTableOperations {
    DBHelper helper;

    public CenterTableOperations(Context context) {
        helper = new DBHelper(context);
    }

    public boolean insertCenterData(CenterModel centerData) {
        // this func will return a boolean value true if insert done or false if not
        SQLiteDatabase dbb = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CenterTable.CENTER_ID, centerData.id);
        contentValues.put(CenterTable.CENTER_NAME, centerData.center_name);
        contentValues.put(CenterTable.CENTER_CITY, centerData.center_city);
        contentValues.put(CenterTable.CENTER_ADDRESS, centerData.center_address);
        contentValues.put(CenterTable.USERS_COUNT, centerData.users_count);
        contentValues.put(CenterTable.CENTER_USER_ID, centerData.user_id);

        long insert = dbb.replace(CenterTable.TABLE_NAME, null, contentValues);
        //here will return to know if it's done or not to use it as a dialog
        return insert != -1;
    }

    public List<CenterModel> getCenterData() {
        SQLiteDatabase db = helper.getReadableDatabase();
        List<CenterModel> resultList = new ArrayList<>();
        String queryString = "SELECT * FROM " + CenterTable.TABLE_NAME;

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String center_name = cursor.getString(1);
                String center_city = cursor.getString(2);
                String center_address = cursor.getString(3);
                int users_count = cursor.getInt(4);
                int user_id = cursor.getInt(5);

                CenterModel newCenter = new CenterModel(id, center_name, center_city, center_address, users_count, user_id);
                resultList.add(newCenter);
            }
            while (cursor.moveToNext());
        } else {
            //something went wrong
        }
        cursor.close();
        db.close();
        return resultList;
    }

    public boolean deleteCenterData(int id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] whereArgs = {id + ""};

        int res = db.delete(CenterTable.TABLE_NAME, CenterTable.CENTER_ID + " = ?", whereArgs);
        return res > 0;
    }

    public int updateCenterData(CenterModel centerData) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CenterTable.CENTER_NAME, centerData.center_name);
        contentValues.put(CenterTable.CENTER_CITY, centerData.center_city);
        contentValues.put(CenterTable.CENTER_ADDRESS, centerData.center_address);
        contentValues.put(CenterTable.USERS_COUNT, centerData.users_count);
        contentValues.put(CenterTable.CENTER_USER_ID, centerData.user_id);
        String[] whereArgs = {centerData.id + ""};
        return db.update(CenterTable.TABLE_NAME, contentValues, CenterTable.CENTER_ID + " = ?", whereArgs);
    }
}