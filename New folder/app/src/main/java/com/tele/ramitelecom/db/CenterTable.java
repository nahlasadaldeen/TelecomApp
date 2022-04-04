package com.tele.ramitelecom.db;

public class CenterTable {

    public static final String TABLE_NAME = "centerTable";
    public static final String CENTER_ID = "_id";     // Column I (Primary Key)
    public static final String CENTER_NAME = "center_name";
    public static final String CENTER_CITY = "center_city";
    public static final String CENTER_ADDRESS = "center_address";
    public static final String USERS_COUNT = "users_count";
    public static final String CENTER_USER_ID = "user_id";

    public static final String CREATE_STATEMENT = "CREATE TABLE " + TABLE_NAME +
            " (" + CENTER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + CENTER_NAME +
            " VARCHAR(255) ," + CENTER_CITY + " VARCHAR(255) , " + CENTER_ADDRESS + " VARCHAR(255) , " +
            USERS_COUNT + " INTEGER , " + CENTER_USER_ID + " INTEGER " + " );";
    public static final String DROP_STATEMENT = "DROP TABLE IF EXISTS " + TABLE_NAME;
}