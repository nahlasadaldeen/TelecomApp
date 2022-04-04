package com.tele.ramitelecom.db;

public class UserTable {

    public static final String TABLE_NAME = "userTable";
    public static final String USER_ID = "_id";     // Column I (Primary Key)
    public static final String NAME = "name";
    public static final String MOBILE_NUM = "mobile_num";
    public static final String EMAIL = "email";
    public static final String MONEY_AMOUNT = "money_amount";
    public static final String ENABLE_CODE = "enable_code";
    public static final String ATTEMPTS_NUM = "attempts_num";
    public static final String SUBSCRIBE_TYPE = "subscribe_type"; // نوع الاشتراك (1 كود , 2 دائم)
    public static final String IS_CENTER = "is_center"; // 0 it is not center, 1 it is center
    public static final String CENTER_ID = "center_id"; //// 1 the account is for a deliver,
    // else the account is a user follow to center with this id

    public static final String IS_SYNC = "is_sync"; // is sync=0 (ok), =1 (need to upload diff to server)


    public static final String CREATE_STATEMENT = "CREATE TABLE " + TABLE_NAME +
            " (" + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME +
            " VARCHAR(255) ," + MOBILE_NUM + " VARCHAR(255) UNIQUE, " + EMAIL + " VARCHAR(255) , " +
            MONEY_AMOUNT + " INTEGER , " + ENABLE_CODE + " VARCHAR(255) ," +
            ATTEMPTS_NUM + " INTEGER , " + IS_SYNC + " INTEGER , " + SUBSCRIBE_TYPE + " INTEGER, " +
            IS_CENTER + " INTEGER, " + CENTER_ID + " INTEGER " +
            " );";
    public static final String DROP_STATEMENT = "DROP TABLE IF EXISTS " + TABLE_NAME;
}