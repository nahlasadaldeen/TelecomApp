package com.tele.ramitelecom.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tele.ramitelecom.ui.users.UnitPriceModel;

import java.util.ArrayList;

public class UnitPriceTableOperations {
    DBHelper helper;

    public UnitPriceTableOperations(Context context) {
        helper = new DBHelper(context);
    }

    public boolean insertCenterData(UnitPriceModel unitData) {
        // this func will return a boolean value true if insert done or false if not
        SQLiteDatabase dbb = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(UnitPriceTable.UNIT_CLASS, unitData.unit_class);
        contentValues.put(UnitPriceTable.UNIT_PRICE, unitData.unit_price);
        contentValues.put(UnitPriceTable.UNIT_PROFIT, unitData.unit_profit);

        long insert = dbb.replace(UnitPriceTable.TABLE_NAME, null, contentValues);
        //here will return to know if it's done or not to use it as a dialog
        return insert != -1;
    }

    public ArrayList<UnitPriceModel> getAllUnitsData() {
        SQLiteDatabase db = helper.getReadableDatabase();
        ArrayList<UnitPriceModel> resultList = new ArrayList<>();
        String queryString = "SELECT * FROM " + UnitPriceTable.TABLE_NAME;

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                int unit_class = cursor.getInt(1);
                int unit_price = cursor.getInt(2);
                int unit_profit = cursor.getInt(3);

                UnitPriceModel newUnit = new UnitPriceModel(unit_class, unit_price, unit_profit);
                resultList.add(newUnit);
            }
            while (cursor.moveToNext());
        } else {
            //something went wrong
        }
        cursor.close();
        db.close();
        return resultList;
    }

    public boolean deleteUnitData(int id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] whereArgs = {id + ""};

        int res = db.delete(UnitPriceTable.TABLE_NAME, UnitPriceTable.UNIT_ID + " = ?", whereArgs);
        return res > 0;
    }

    public boolean deleteAllUnitData() {
        SQLiteDatabase db = helper.getWritableDatabase();

        int res = db.delete(UnitPriceTable.TABLE_NAME, UnitPriceTable.UNIT_ID + " = ?", null);
        return res > 0;
    }
}