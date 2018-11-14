package com.example.ctyeung.capstonestage1.database;

import android.net.Uri;
import android.provider.BaseColumns;

public class MsgContract
{
    public static final String PATH = "msgcontract";
    public static final int ITEM_ALL = 160;
    public static final int ITEM_SINGLE = 161;

    // The authority, which is how your code knows which Content Provider to access
    public static final String AUTHORITY = "com.example.ctyeung.capstonestage1";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // TaskEntry content URI = base content URI + path
    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH).build();

    // Task table and column names
    public static final String TABLE_NAME = "Message";

    public static final String CREATE_TABLE = "CREATE TABLE "  + TABLE_NAME + " (" +
            Columns.COL_ID + " INTEGER PRIMARY KEY, " +
            Columns.COL_TIME_STAMP + " TEXT NOT NULL, " +
            Columns.COL_MSG_SUBJECT + " TEXT NOT NULL, " +
            Columns.COL_IMAGE_TYPE  + " TEXT NOT NULL, " +
            Columns.COL_MSG_HEADER  + " TEXT NOT NULL, " +
            Columns.COL_MSG_FOOTER  + " TEXT NOT NULL, " +
            Columns.COL_IMAGE_PATH  + " TEXT NOT NULL);";

    public static final class Columns implements BaseColumns {

        // TaskEntry content URI = base content URI + path
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH).build();

        // Since TaskEntry implements the interface "BaseColumns", it has an automatically produced
        // "_ID" column in addition to the two below
        public static final String COL_ID = "id";
        public static final String COL_TIME_STAMP = "timeStamp";
        public static final String COL_MSG_SUBJECT = "subject";
        public static final String COL_IMAGE_TYPE = "imageType";
        public static final String COL_MSG_HEADER = "header";
        public static final String COL_MSG_FOOTER = "footer";
        public static final String COL_IMAGE_PATH = "imagePath";
    }

}
