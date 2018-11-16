package com.example.ctyeung.capstonestage1.database;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class MsgData
{
    private Context mContext;
    private Uri mUri;
    public String[] mColumns = null;

    public MsgData(Context context)
    {
        this.mContext = context;
        this.mUri = MsgContract.CONTENT_URI;
    }

    public boolean update(int id,
                          String columnName,
                          String value)
    {
        try
        {
            ContentValues contentValues = new ContentValues();
            contentValues.put(columnName, value);

            String[] args = {String.valueOf(id)};
            int success = mContext.getContentResolver().update(this.mUri,
                        contentValues,
                        MsgContract.Columns.COL_ID +"=?",
                        args);

            return (success>0)?true:false;
        }
        catch (Exception ex)
        {
            return false;
        }
    }

    public List<MsgTuple> query(String columnName,
                          String value)
    {
        try
        {
            String[] args = {value};
            // query from db
            Cursor cursor = mContext.getContentResolver().query(this.mUri,
                    mColumns,
                    columnName + "=?",
                    args,
                    null);

            return parseResult(cursor);
        }
        catch (Exception ex)
        {
            return null;
        }
    }

    /*
     * retrieve all send items
     */
    public List<MsgTuple> query()
    {
        try
        {
            Cursor cursor = mContext.getContentResolver().query(this.mUri,
                    mColumns,
                    MsgContract.Columns.COL_TIME_STAMP + " != 'blank'",
                    null,
                    null);

            return parseResult(cursor);
        }
        catch (Exception ex)
        {
            return null;
        }
    }

    /*
     * widget or list selection
     */
    public List<MsgTuple> query(int id)
    {
        try
        {
            String[] args = {String.valueOf(id)};
            Cursor cursor = mContext.getContentResolver().query(this.mUri,
                    mColumns,
                    MsgContract.Columns.COL_ID + "=?",
                    args,
                    null);

            return parseResult(cursor);
        }
        catch (Exception ex)
        {
            return null;
        }
    }

    protected List<MsgTuple> parseResult(Cursor cursor)
    {
        if(null==cursor || 0==cursor.getCount())
            return null;

        List<MsgTuple> list = new ArrayList<MsgTuple>();
        cursor.moveToFirst();

        for(int i=0; i<cursor.getCount(); i++) {
            MsgTuple tuple = new MsgTuple(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6));

            list.add(tuple);
            cursor.moveToNext();
        }
        return list;
    }

    public boolean insert(MsgTuple tuple)
    {
        try
        {
            ContentValues contentValues = tuple.getContentValues();
            mContext.getContentResolver().insert(this.mUri, contentValues);
            return true;
        }
        catch (Exception ex)
        {
            return false;
        }
    }
}
