package com.tele.ramitelecom.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tele.ramitelecom.ui.users.TransferModel;

import java.util.ArrayList;
import java.util.List;

public class TransferTableOperations {
    DBHelper helper;

    public TransferTableOperations(Context context) {
        helper = new DBHelper(context);
    }

    public long insertTransferData(TransferModel transferModel) {
        // this func will return a boolean value true if insert done or false if not
        SQLiteDatabase dbb = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TransferTable.MOBILE_NUM, transferModel.mobileNum);
        contentValues.put(TransferTable.BALANCE_OR_BILL, transferModel.balanceOrBill);
        contentValues.put(TransferTable.MONEY_AMOUNT, transferModel.money_amount);
        contentValues.put(TransferTable.COMPANY, transferModel.company);
        contentValues.put(TransferTable.TRANSFER_DATE, transferModel.transfer_date);
        contentValues.put(TransferTable.ONE_OR_ALL, transferModel.one_or_all);
        contentValues.put(TransferTable.TRANSFER_CODE, transferModel.transfer_code);
        contentValues.put(TransferTable.IS_SYNC, transferModel.is_sync);

        long insert = dbb.replace(TransferTable.TABLE_NAME, null, contentValues);
//        return insert != -1;
        return insert;

    }

    public int updateIsSync(long id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TransferTable.IS_SYNC, 0);
        String[] whereArgs = {id + ""};
        return db.update(TransferTable.TABLE_NAME, contentValues, TransferTable.ID + " = ?", whereArgs);
    }

//     جلب كل عمليات التحويل الغير متزامنة
    public List<TransferModel> getAllUnSyncTransferData() {
        SQLiteDatabase db = helper.getReadableDatabase();
        List<TransferModel> resultList = new ArrayList<>();
        String queryString = "SELECT * FROM " + TransferTable.TABLE_NAME  + " WHERE " + TransferTable.IS_SYNC + " = 1 ";

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String mobileNum = cursor.getString(1);
                int balance_ot_bill = cursor.getInt(2);
                int money_amount = cursor.getInt(3);
                int company = cursor.getInt(4);
                String transfer_date = cursor.getString(5);
                int one_or_all = cursor.getInt(6);
                String transfer_code = cursor.getString(7);
                TransferModel transferModel = new TransferModel(id, mobileNum, balance_ot_bill, money_amount, company, transfer_date, one_or_all, transfer_code);
                resultList.add(transferModel);
            }
            while (cursor.moveToNext());
        } else {
            //something went wrong
        }
        cursor.close();
        db.close();
        return resultList;
    }

    // جلب كل عمليات التحويل المفرق والجملة
    public List<TransferModel> getOneAndAllTransferData() {
        SQLiteDatabase db = helper.getReadableDatabase();
        List<TransferModel> resultList = new ArrayList<>();
        String queryString = "SELECT * FROM " + TransferTable.TABLE_NAME + " ORDER BY " + TransferTable.ID + " DESC;";

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String mobileNum = cursor.getString(1);
                int balance_ot_bill = cursor.getInt(2);
                int money_amount = cursor.getInt(3);
                int company = cursor.getInt(4);
                String transfer_date = cursor.getString(5);
                int one_or_all = cursor.getInt(6);
                String transfer_code = cursor.getString(7);
                TransferModel transferModel = new TransferModel(id, mobileNum, balance_ot_bill, money_amount, company, transfer_date, one_or_all, transfer_code);
                resultList.add(transferModel);
            }
            while (cursor.moveToNext());
        } else {
            //something went wrong
        }
        cursor.close();
        db.close();
        return resultList;
    }

    // جلب كل عمليات التحويل المفرق
    public ArrayList<TransferModel> getOneTransferData() {
        SQLiteDatabase db = helper.getReadableDatabase();
        ArrayList<TransferModel> resultList = new ArrayList<>();
        String queryString = "SELECT * FROM " + TransferTable.TABLE_NAME + " WHERE " + TransferTable.ONE_OR_ALL + " = 1 " + " ORDER BY " + TransferTable.ID + " DESC;";

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String mobileNum = cursor.getString(1);
                int balance_ot_bill = cursor.getInt(2);
                int money_amount = cursor.getInt(3);
                int company = cursor.getInt(4);
                String transfer_date = cursor.getString(5);
                int one_or_all = cursor.getInt(6);
                String transfer_code = cursor.getString(7);
                TransferModel transferModel = new TransferModel(id, mobileNum, balance_ot_bill, money_amount, company, transfer_date, one_or_all, transfer_code);
                resultList.add(transferModel);
            }
            while (cursor.moveToNext());
        } else {
            //something went wrong
        }
        cursor.close();
        db.close();
        return resultList;
    }

    // جلب كل عمليات التحويل الجملة
    public List<TransferModel> getAllTransferData() {
        SQLiteDatabase db = helper.getReadableDatabase();
        List<TransferModel> resultList = new ArrayList<>();
        String queryString = "SELECT * FROM " + TransferTable.TABLE_NAME + " WHERE " + TransferTable.ONE_OR_ALL + " = 2 " + " ORDER BY " + TransferTable.ID + " DESC;";

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String mobileNum = cursor.getString(1);
                int balance_ot_bill = cursor.getInt(2);
                int money_amount = cursor.getInt(3);
                int company = cursor.getInt(4);
                String transfer_date = cursor.getString(5);
                int one_or_all = cursor.getInt(6);
                String transfer_code = cursor.getString(7);
                TransferModel transferModel = new TransferModel(id, mobileNum, balance_ot_bill, money_amount, company, transfer_date, one_or_all, transfer_code);
                resultList.add(transferModel);
            }
            while (cursor.moveToNext());
        } else {
            //something went wrong
        }
        cursor.close();
        db.close();
        return resultList;
    }

    // جلب كل عمليات التحويل المفرق والجملة في تاريخ محدد
    public List<TransferModel> getOneAndAllTransferDataByDate(String date) {
        SQLiteDatabase db = helper.getReadableDatabase();
        List<TransferModel> resultList = new ArrayList<>();
        String queryString = "SELECT * FROM " + TransferTable.TABLE_NAME + " WHERE " + TransferTable.TRANSFER_DATE + " =  '" + date + "' ORDER BY " + TransferTable.ID + " DESC;";

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String mobileNum = cursor.getString(1);
                int balance_ot_bill = cursor.getInt(2);
                int money_amount = cursor.getInt(3);
                int company = cursor.getInt(4);
                String transfer_date = cursor.getString(5);
                int one_or_all = cursor.getInt(6);
                String transfer_code = cursor.getString(7);

                TransferModel transferModel = new TransferModel(id, mobileNum, balance_ot_bill, money_amount, company, transfer_date, one_or_all, transfer_code);
                resultList.add(transferModel);
            }
            while (cursor.moveToNext());
        } else {
            //something went wrong
        }
        cursor.close();
        db.close();
        return resultList;
    }

    // جلب كل عمليات التحويل المفرق في تاريخ محدد
    public ArrayList<TransferModel> getOneTransferDataByDate(String date) {
        SQLiteDatabase db = helper.getReadableDatabase();
        ArrayList<TransferModel> resultList = new ArrayList<>();
        String queryString = "SELECT * FROM " + TransferTable.TABLE_NAME + " WHERE " + TransferTable.ONE_OR_ALL + " = 1 AND " + TransferTable.TRANSFER_DATE + " =  '" + date + "' ORDER BY " + TransferTable.ID + " DESC;";

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String mobileNum = cursor.getString(1);
                int balance_ot_bill = cursor.getInt(2);
                int money_amount = cursor.getInt(3);
                int company = cursor.getInt(4);
                String transfer_date = cursor.getString(5);
                int one_or_all = cursor.getInt(6);
                String transfer_code = cursor.getString(7);

                TransferModel transferModel = new TransferModel(id, mobileNum, balance_ot_bill, money_amount, company, transfer_date, one_or_all, transfer_code);
                resultList.add(transferModel);
            }
            while (cursor.moveToNext());
        } else {
            //something went wrong
        }
        cursor.close();
        db.close();
        return resultList;
    }

    // جلب كل عمليات التحويل الجملة في تاريخ محدد
    public List<TransferModel> getAllTransferDataByDate(String date) {
        SQLiteDatabase db = helper.getReadableDatabase();
        List<TransferModel> resultList = new ArrayList<>();
        String queryString = "SELECT * FROM " + TransferTable.TABLE_NAME + " WHERE " + TransferTable.ONE_OR_ALL + "= 2 AND " + TransferTable.TRANSFER_DATE + " =  '" + date + "' ORDER BY " + TransferTable.ID + " DESC;";

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String mobileNum = cursor.getString(1);
                int balance_ot_bill = cursor.getInt(2);
                int money_amount = cursor.getInt(3);
                int company = cursor.getInt(4);
                String transfer_date = cursor.getString(5);
                int one_or_all = cursor.getInt(6);
                String transfer_code = cursor.getString(7);

                TransferModel transferModel = new TransferModel(id, mobileNum, balance_ot_bill, money_amount, company, transfer_date, one_or_all, transfer_code);
                resultList.add(transferModel);
            }
            while (cursor.moveToNext());
        } else {
            //something went wrong
        }
        cursor.close();
        db.close();
        return resultList;
    }

    // جلب كل عمليات التحويل المفرق والجملة لمستخدم محدد
    public ArrayList<TransferModel> getTransferDataByUser(String mobile_Num) {
        SQLiteDatabase db = helper.getReadableDatabase();
        ArrayList<TransferModel> resultList = new ArrayList<>();
        String queryString = "SELECT * FROM " + TransferTable.TABLE_NAME + " WHERE " + TransferTable.MOBILE_NUM + " =  '" + mobile_Num + "' ORDER BY " + TransferTable.ID + " DESC;";

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String mobileNum = cursor.getString(1);
                int balance_ot_bill = cursor.getInt(2);
                int money_amount = cursor.getInt(3);
                int company = cursor.getInt(4);
                String transfer_date = cursor.getString(5);
                int one_or_all = cursor.getInt(6);

                String transfer_code = cursor.getString(7);

                TransferModel transferModel = new TransferModel(id, mobileNum, balance_ot_bill, money_amount, company, transfer_date, one_or_all, transfer_code);
                resultList.add(transferModel);
            }
            while (cursor.moveToNext());
        } else {
            //something went wrong
        }
        cursor.close();
        db.close();
        return resultList;
    }

    // جلب كل عمليات التحويل المفرق الفواتير
    public List<TransferModel> getOneBillTransferData() {
        SQLiteDatabase db = helper.getReadableDatabase();
        List<TransferModel> resultList = new ArrayList<>();
        String queryString = "SELECT * FROM " + TransferTable.TABLE_NAME + " WHERE " + TransferTable.BALANCE_OR_BILL + " = 2 AND " + TransferTable.ONE_OR_ALL + " = 1";

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String mobileNum = cursor.getString(1);
                int balance_ot_bill = cursor.getInt(2);
                int money_amount = cursor.getInt(3);
                int company = cursor.getInt(4);
                String transfer_date = cursor.getString(5);
                int one_or_all = cursor.getInt(6);
                String transfer_code = cursor.getString(7);

                TransferModel transferModel = new TransferModel(id, mobileNum, balance_ot_bill, money_amount, company, transfer_date, one_or_all, transfer_code);
                resultList.add(transferModel);
            }
            while (cursor.moveToNext());
        } else {
            //something went wrong
        }
        cursor.close();
        db.close();
        return resultList;
    }

    // جلب كل عمليات التحويل المفرق الرصيد
    public List<TransferModel> getOneBalanceTransferData() {
        SQLiteDatabase db = helper.getReadableDatabase();
        List<TransferModel> resultList = new ArrayList<>();
        String queryString = "SELECT * FROM " + TransferTable.TABLE_NAME + " WHERE " + TransferTable.BALANCE_OR_BILL + " = 1 AND " + TransferTable.ONE_OR_ALL + " = 1";

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String mobileNum = cursor.getString(1);
                int balance_ot_bill = cursor.getInt(2);
                int money_amount = cursor.getInt(3);
                int company = cursor.getInt(4);
                String transfer_date = cursor.getString(5);
                int one_or_all = cursor.getInt(6);
                String transfer_code = cursor.getString(7);

                TransferModel transferModel = new TransferModel(id, mobileNum, balance_ot_bill, money_amount, company, transfer_date, one_or_all, transfer_code);
                resultList.add(transferModel);
            }
            while (cursor.moveToNext());
        } else {
            //something went wrong
        }
        cursor.close();
        db.close();
        return resultList;
    }

    // جلب كل عمليات التحويل المفرق سيرتيل
    public List<TransferModel> getOneSyriatelTransferData() {
        SQLiteDatabase db = helper.getReadableDatabase();
        List<TransferModel> resultList = new ArrayList<>();
        String queryString = "SELECT * FROM " + TransferTable.TABLE_NAME + " WHERE " + TransferTable.COMPANY + " = 1 AND " + TransferTable.ONE_OR_ALL + " = 1";

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String mobileNum = cursor.getString(1);
                int balance_ot_bill = cursor.getInt(2);
                int money_amount = cursor.getInt(3);
                int company = cursor.getInt(4);

                String transfer_date = cursor.getString(5);
                int one_or_all = cursor.getInt(6);
                String transfer_code = cursor.getString(7);


                TransferModel transferModel = new TransferModel(id, mobileNum, balance_ot_bill, money_amount, company, transfer_date, one_or_all, transfer_code);
                resultList.add(transferModel);
            }
            while (cursor.moveToNext());
        } else {
            //something went wrong
        }
        cursor.close();
        db.close();
        return resultList;
    }

    // جلب كل عمليات التحويل المفرق ام تي ان
    public List<TransferModel> getOneMtnTransferData() {
        SQLiteDatabase db = helper.getReadableDatabase();
        List<TransferModel> resultList = new ArrayList<>();
        String queryString = "SELECT * FROM " + TransferTable.TABLE_NAME + " WHERE " + TransferTable.COMPANY + " = 2 AND " + TransferTable.ONE_OR_ALL + " = 1";

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String mobileNum = cursor.getString(1);
                int balance_ot_bill = cursor.getInt(2);
                int money_amount = cursor.getInt(3);
                int company = cursor.getInt(4);

                String transfer_date = cursor.getString(5);
                int one_or_all = cursor.getInt(6);
                String transfer_code = cursor.getString(7);

                TransferModel transferModel = new TransferModel(id, mobileNum, balance_ot_bill, money_amount, company, transfer_date, one_or_all, transfer_code);
                resultList.add(transferModel);
            }
            while (cursor.moveToNext());
        } else {
            //something went wrong
        }
        cursor.close();
        db.close();
        return resultList;
    }

    // جلب كل عمليات التحويل الجملة سيرتيل
    public List<TransferModel> getAllSyriatelTransferData() {
        SQLiteDatabase db = helper.getReadableDatabase();
        List<TransferModel> resultList = new ArrayList<>();
        String queryString = "SELECT * FROM " + TransferTable.TABLE_NAME + " WHERE " + TransferTable.COMPANY + " = 1 AND " + TransferTable.ONE_OR_ALL + " = 2";

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String mobileNum = cursor.getString(1);
                int balance_ot_bill = cursor.getInt(2);
                int money_amount = cursor.getInt(3);
                int company = cursor.getInt(4);

                String transfer_date = cursor.getString(5);
                int one_or_all = cursor.getInt(6);
                String transfer_code = cursor.getString(7);

                TransferModel transferModel = new TransferModel(id, mobileNum, balance_ot_bill, money_amount, company, transfer_date, one_or_all, transfer_code);
                resultList.add(transferModel);
            }
            while (cursor.moveToNext());
        } else {
            //something went wrong
        }
        cursor.close();
        db.close();
        return resultList;
    }

    // جلب كل عمليات التحويل الجملة ام تي ان
    public List<TransferModel> getAllMtnTransferData() {
        SQLiteDatabase db = helper.getReadableDatabase();
        List<TransferModel> resultList = new ArrayList<>();
        String queryString = "SELECT * FROM " + TransferTable.TABLE_NAME + " WHERE " + TransferTable.COMPANY + " = 2 AND " + TransferTable.ONE_OR_ALL + " = 2";

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String mobileNum = cursor.getString(1);
                int balance_ot_bill = cursor.getInt(2);
                int money_amount = cursor.getInt(3);
                int company = cursor.getInt(4);

                String transfer_date = cursor.getString(5);
                int one_or_all = cursor.getInt(6);
                String transfer_code = cursor.getString(7);

                TransferModel transferModel = new TransferModel(id, mobileNum, balance_ot_bill, money_amount, company, transfer_date, one_or_all, transfer_code);
                resultList.add(transferModel);
            }
            while (cursor.moveToNext());
        } else {
            //something went wrong
        }
        cursor.close();
        db.close();
        return resultList;
    }

}