package com.example.ctyeung.capstonestage1.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper
{
    // The name of the database
    private static final String DATABASE_NAME = "capstoneDb.db";

    // If you change the database schema, you must increment the database version
    private static final int VERSION = 1;

    // Constructor
    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, VERSION);
    }

    /**
     * Called when the tasks database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        // password requires the following order of operation
            db.execSQL(MsgContract.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,
                          int oldVersion,
                          int newVersion) {
        // **** this need to change

        db.execSQL("DROP TABLE IF EXISTS " + MsgContract.TABLE_NAME);
        onCreate(db);
    }
}
