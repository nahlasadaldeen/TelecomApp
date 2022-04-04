package com.tele.ramitelecom.db;

public class TransferTable {

    public static final String TABLE_NAME = "transferTable";
    public static final String ID = "_id";     // Column I (Primary Key)
    public static final String MOBILE_NUM = "mobile_num";
    public static final String BALANCE_OR_BILL = "balance_or_bill";
    public static final String MONEY_AMOUNT = "money_amount";
    public static final String COMPANY = "company";
    public static final String TRANSFER_DATE = "transfer_date";
    public static final String ONE_OR_ALL = "one_or_all"; // مفرق أو جملة ( 1 مفرق, 2 جملة)
    public static final String TRANSFER_CODE = "transfer_code";
    public static final String IS_SYNC = "is_sync"; // is sync=0 (ok), =1 (need to upload diff to server)

    public static final String CREATE_STATEMENT = "CREATE TABLE " + TABLE_NAME +
            " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + MOBILE_NUM + " VARCHAR(255) , " +
            BALANCE_OR_BILL + " INTEGER , " + MONEY_AMOUNT + " INTEGER , " + COMPANY + " INTEGER ,"
            + TRANSFER_DATE + " VARCHAR(255), " + ONE_OR_ALL + " INTEGER," + IS_SYNC + " INTEGER," + TRANSFER_CODE + " VARCHAR(255) );";
    public static final String DROP_STATEMENT = "DROP TABLE IF EXISTS " + TABLE_NAME;
}