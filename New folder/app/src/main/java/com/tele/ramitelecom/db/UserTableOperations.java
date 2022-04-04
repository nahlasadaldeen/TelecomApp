package com.tele.ramitelecom.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tele.ramitelecom.ui.users.UserModel;

import java.util.ArrayList;
import java.util.List;

public class UserTableOperations {
    DBHelper helper;

    public UserTableOperations(Context context) {
        helper = new DBHelper(context);
    }

    public boolean insertUserData(UserModel userData) {
        // this func will return a boolean value true if insert done or false if not
        SQLiteDatabase dbb = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(UserTable.NAME, userData.name);
        contentValues.put(UserTable.MOBILE_NUM, userData.mobileNum);
        contentValues.put(UserTable.EMAIL, userData.email);
        contentValues.put(UserTable.ENABLE_CODE, userData.enableCode);
        contentValues.put(UserTable.ATTEMPTS_NUM, userData.attemptsNum);
        contentValues.put(UserTable.MONEY_AMOUNT, userData.money_amount);
        contentValues.put(UserTable.SUBSCRIBE_TYPE, userData.subscribeType);
        contentValues.put(UserTable.IS_CENTER, userData.is_center);
        contentValues.put(UserTable.CENTER_ID, userData.center_id);

        long insert = dbb.replace(UserTable.TABLE_NAME, null, contentValues);
        //here will return to know if it's done or not to use it as a dialog
        return insert != -1;
    }

    public boolean checkIfMobileExists(String userMobile) {

        SQLiteDatabase db = helper.getReadableDatabase();

        String query = "select " + UserTable.MOBILE_NUM + " from " + UserTable.TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        String existMobile;

        if (cursor.moveToFirst()) {
            do {
                existMobile = cursor.getString(0);
                if (existMobile.equals(userMobile)) {
                    return true;
                }
            } while (cursor.moveToNext());
        }
        return false;
    }

    public List<UserModel> getUserData() {
        SQLiteDatabase db = helper.getReadableDatabase();
        List<UserModel> resultList = new ArrayList<>();
        String queryString = "SELECT * FROM " + UserTable.TABLE_NAME;

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String mobileNum = cursor.getString(2);
                String email = cursor.getString(3);
                int money_amount = cursor.getInt(4);
                String enableCode = cursor.getString(5);
                int attemptsNum = cursor.getInt(6);
                int subscribeType = cursor.getInt(7);
                int is_center = cursor.getInt(8);
                int center_id = cursor.getInt(9);

                UserModel newUser = new UserModel(id, name, mobileNum, email, enableCode, attemptsNum, money_amount, subscribeType, is_center, center_id);
                resultList.add(newUser);
            }
            while (cursor.moveToNext());
        } else {
            //something went wrong
        }
        cursor.close();
        db.close();
        return resultList;
    }

    public List<UserModel> getLoanUserData() {
        SQLiteDatabase db = helper.getReadableDatabase();
        List<UserModel> resultList = new ArrayList<>();
        String queryString = "SELECT * FROM " + UserTable.TABLE_NAME + " WHERE " + UserTable.MONEY_AMOUNT + " > 0  ORDER BY " + UserTable.NAME;

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String mobileNum = cursor.getString(2);
                String email = cursor.getString(3);
                int money_amount = cursor.getInt(4);
                String enableCode = cursor.getString(5);
                int attemptsNum = cursor.getInt(6);
                int subscribeType = cursor.getInt(7);
                int is_center = cursor.getInt(8);
                int center_id = cursor.getInt(9);
                UserModel newUser = new UserModel(id, name, mobileNum, email, enableCode, attemptsNum, money_amount, subscribeType, is_center, center_id);
                resultList.add(newUser);
            }
            while (cursor.moveToNext());
        } else {
            //something went wrong
        }
        cursor.close();
        db.close();
        return resultList;
    }

    public boolean deleteUserData(int id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] whereArgs = {id + ""};

        int res = db.delete(UserTable.TABLE_NAME, UserTable.USER_ID + " = ?", whereArgs);
        return res > 0;
    }

    public int updateUserData(UserModel userData) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(UserTable.NAME, userData.name);
        contentValues.put(UserTable.MOBILE_NUM, userData.mobileNum);
        contentValues.put(UserTable.EMAIL, userData.email);
        contentValues.put(UserTable.ENABLE_CODE, userData.enableCode);
        contentValues.put(UserTable.ATTEMPTS_NUM, userData.attemptsNum);
        contentValues.put(UserTable.MONEY_AMOUNT, userData.money_amount);
        contentValues.put(UserTable.SUBSCRIBE_TYPE, userData.subscribeType);
        contentValues.put(UserTable.IS_CENTER, userData.is_center);
        contentValues.put(UserTable.CENTER_ID, userData.center_id);
        String[] whereArgs = {userData.id + ""};
        return db.update(UserTable.TABLE_NAME, contentValues, UserTable.USER_ID + " = ?", whereArgs);
    }


    public int getLoanOfUserByMobileNum(String mobileNum) {
        SQLiteDatabase db = helper.getReadableDatabase();
        String queryString = "SELECT " + UserTable.MONEY_AMOUNT + " FROM " + UserTable.TABLE_NAME + " WHERE " + UserTable.MOBILE_NUM + " = '" + mobileNum + "'";
        int money_amount = 0;
        Cursor cursor = db.rawQuery(queryString, null);

        cursor.moveToFirst();
        money_amount = cursor.getInt(0);


        cursor.close();
        db.close();
        return money_amount;
    }

    public int updateUserLoanMoney(int money_amount, String mobileNum) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(UserTable.MONEY_AMOUNT, money_amount);
        String[] whereArgs = {mobileNum};
        return db.update(UserTable.TABLE_NAME, contentValues, UserTable.MOBILE_NUM + " = ?", whereArgs);
    }

}