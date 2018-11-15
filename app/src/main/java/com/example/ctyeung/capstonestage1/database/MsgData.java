package com.example.ctyeung.capstonestage1.database;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class MsgData
{
    private Activity mActivity;
    private Uri uri;
    public String[] columns = null;

    public MsgData(Activity activity)
    {
        mActivity = activity;
        this.uri = MsgContract.CONTENT_URI;
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
            int success = mActivity.getContentResolver().update(this.uri,
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
        Cursor cursor = null;
        try
        {
            String[] args = {value};
            // query from db
            cursor = mActivity.getContentResolver().query(this.uri,
                    columns,
                    columnName + "=?",
                    args,
                    null);

            if(null==cursor || 0==cursor.getCount())
                throw new Exception("not found");

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
        catch (Exception ex)
        {
            return null;
        }
    }


    public MsgTuple query(int id)
    {
        Cursor cursor = null;
        try
        {
            String[] args = {String.valueOf(id)};
            // query from db
            cursor = mActivity.getContentResolver().query(this.uri,
                    columns,
                    MsgContract.Columns.COL_ID + "=?",
                    args,
                    null);

            if(null==cursor || 0==cursor.getCount())
                throw new Exception("not found");

            cursor.moveToFirst();
            MsgTuple tuple = new MsgTuple(cursor.getInt(0),
                                        cursor.getString(1),
                                        cursor.getString(2),
                                        cursor.getString(3),
                                        cursor.getString(4),
                                        cursor.getString(5),
                                        cursor.getString(6));
            return tuple;
        }
        catch (Exception ex)
        {
            return null;
        }
    }

    public boolean insert(MsgTuple tuple)
    {
        try
        {
            ContentValues contentValues = tuple.getContentValues();
            mActivity.getContentResolver().insert(this.uri, contentValues);
            return true;
        }
        catch (Exception ex)
        {
            return false;
        }
    }
}
