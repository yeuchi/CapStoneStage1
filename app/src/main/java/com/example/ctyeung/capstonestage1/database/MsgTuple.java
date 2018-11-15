package com.example.ctyeung.capstonestage1.database;

import android.content.ContentValues;

import java.util.List;

public class MsgTuple implements ITuple
{
    public int id;
    public String timeStamp="blank";
    public String subject="blank";
    public String type="blank";
    public String header="blank";
    public String footer="blank";
    public String path="blank";

    public MsgTuple()
    {

    }

    public MsgTuple(int id,
                    String timeStamp,   // timeStamp @ moment of send
                    String subject,
                    String type,
                    String header,
                    String footer,
                    String path)
    {
        this.id = id;
        this.timeStamp = timeStamp;
        this.subject = subject;
        this.type = type;
        this.header = header;
        this.footer = footer;
        this.path = path;
    }

    public boolean parse(List<String> list)
    {
        try {
            id = Integer.parseInt(list.get(0));
            timeStamp = list.get(1);
            subject = list.get(2);
            type = list.get(3);
            header = list.get(4);
            footer = list.get(5);
            path = list.get(6);

            return true;
        }
        catch (Exception ex)
        {
            return false;
        }
    }

    public ContentValues getContentValues()
    {
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MsgContract.Columns.COL_TIME_STAMP, timeStamp);
            contentValues.put(MsgContract.Columns.COL_MSG_SUBJECT, subject);
            contentValues.put(MsgContract.Columns.COL_IMAGE_TYPE, type);
            contentValues.put(MsgContract.Columns.COL_MSG_HEADER, header);
            contentValues.put(MsgContract.Columns.COL_MSG_FOOTER, footer);
            contentValues.put(MsgContract.Columns.COL_IMAGE_PATH, path);
            return contentValues;
        }
        catch (Exception ex)
        {
            return null;
        }
    }
}
