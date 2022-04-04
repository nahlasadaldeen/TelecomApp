package com.tele.ramitelecom.db;

public class UnitPriceTable {

    public static final String TABLE_NAME = "unitPriceTable";
    public static final String UNIT_ID = "_id";     // Column I (Primary Key)
    public static final String UNIT_CLASS = "unit_class";
    public static final String UNIT_PRICE = "unit_price";
    public static final String UNIT_PROFIT = "unit_profit";

    public static final String CREATE_STATEMENT = "CREATE TABLE " + TABLE_NAME +
            " (" + UNIT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + UNIT_CLASS +
            " INTEGER ," + UNIT_PRICE + " INTEGER , " + UNIT_PROFIT + " INTEGER );";
    public static final String DROP_STATEMENT = "DROP TABLE IF EXISTS " + TABLE_NAME;
}