package com.example.ctyeung.capstonestage1.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

public class CapStoneContentProvider extends ContentProvider {
    // CDeclare a static variable for the Uri matcher that you construct
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    // Define a static buildUriMatcher method that associates URI's with their int match

    /**
     * Initialize a new matcher object without any matches,
     * then use .addURI(String authority, String path, int match) to add matches
     */
    public static UriMatcher buildUriMatcher() {

        // Initialize a UriMatcher with no matches by passing in NO_MATCH to the constructor
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        /*
          All paths added to the UriMatcher have a corresponding int.
          For each kind of uri you may want to access, add the corresponding match with addURI.
          The two calls below add matches for the task directory and a single item by ID.
         */
        uriMatcher.addURI(MsgContract.AUTHORITY, MsgContract.PATH, MsgContract.ITEM_ALL);
        uriMatcher.addURI(MsgContract.AUTHORITY, MsgContract.PATH + "/*", MsgContract.ITEM_SINGLE);

        return uriMatcher;
    }

    private DBHelper mDBHelper;

    @Override
    public boolean onCreate() {
        // Complete onCreate() and initialize a TaskDbhelper on startup
        // [Hint] Declare the DbHelper as a global variable

        Context context = getContext();
        mDBHelper = new DBHelper(context);
        return true;
    }

    // Implement insert to handle requests to insert a single new row of data
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {

        // Write URI matching code to identify the match for the tasks directory
        int match = sUriMatcher.match(uri);
        Uri returnUri = null; // URI to be returned

        String tableName = getTableName(match);
        Uri contentUri = getContentUri(match);

        try {
            if (null != tableName && null != contentUri) {
                // Get access to the task database (to write new data to)
                final SQLiteDatabase db = mDBHelper.getWritableDatabase();

                // Insert new values into the database
                // Inserting values into tasks table
                long id = db.insert(tableName, null, values);
                if (id <= 0)
                    throw new android.database.SQLException("Failed to insert row into " + uri);

                returnUri = ContentUris.withAppendedId(contentUri, id);

                // Notify the resolver if the uri has been changed, and return the newly inserted URI
                getContext().getContentResolver().notifyChange(uri, null);
            }
            return returnUri;
        } catch (Exception ex) {
            return null;
        }
    }

    // Implement query to handle requests for data by URI
    @Override
    public Cursor query(@NonNull Uri uri,       // table name
                        String[] columns,       // ex. ["title"]
                        String selection,       // ex. "title=?"
                        String[] selectionArgs,   // ex. "Thor"
                        String sortOrder) {     // ex. "title DESC"

        int match = sUriMatcher.match(uri);
        String tableName = getTableName(match);

        try {
            if (null == tableName)
                throw new Exception("Invalid table");

            // handle join or single table query
            return defaultQuery(uri, columns, selection, selectionArgs, sortOrder, tableName);
        } catch (Exception ex) {
            return null;
        }
    }

    protected Cursor defaultQuery(@NonNull Uri uri,       // table name
                                  String[] columns,       // ex. ["title"]
                                  String selection,       // ex. "title=?"
                                  String[] selectionArgs,   // ex. "Thor"
                                  String sortOrder,
                                  String tableName) {
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        Cursor cursor = db.query(tableName,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int delete(@NonNull Uri uri,
                      String selection,
                      String[] selectionArgs) {
        /*
         * For now, only support truncate of table
         */
        int match = sUriMatcher.match(uri);
        String tableName = getTableName(match);

        try {
            final SQLiteDatabase db = mDBHelper.getWritableDatabase();
            Cursor cursor = null;

            if (null != tableName) {
                if (null == selection && null == selectionArgs)
                    db.execSQL("delete from " + tableName);
                return 0;
            }
        } catch (Exception ex) {
        }
        return -1;
    }

    @Override
    public int update(@NonNull Uri uri,
                      ContentValues values,
                      String selection,
                      String[] selectionArgs) {

        // Write URI matching code to identify the match for the tasks directory
        int match = sUriMatcher.match(uri);
        int numRowsUpdated = 0;

        String tableName = getTableName(match);

        try {
            if (null != tableName) {
                // Get access to the task database (to write new data to)
                final SQLiteDatabase db = mDBHelper.getWritableDatabase();

                // Insert new values into the database
                // Inserting values into tasks table
                numRowsUpdated = db.update(tableName, values, selection, selectionArgs);

                // Notify the resolver if the uri has been changed, and return the newly inserted URI
                getContext().getContentResolver().notifyChange(uri, null);
            }
            return numRowsUpdated;

        } catch (Exception ex) {
            return -1;
        }
    }

    @Override
    public String getType(@NonNull Uri uri) {

        throw new UnsupportedOperationException("Not yet implemented");
    }

    protected Uri getContentUri(int matchUri) {
        switch (matchUri) {
            default:
                return null;

            case MsgContract.ITEM_ALL:
                return MsgContract.CONTENT_URI;
        }
    }

    protected String getTableName(int matchUri) {
        switch (matchUri) {
            default:
                return null;

            case MsgContract.ITEM_ALL:
                return MsgContract.TABLE_NAME;

        }
    }
}
