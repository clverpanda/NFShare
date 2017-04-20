package com.clverpanda.nfshare.util.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.clverpanda.nfshare.model.TaskInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by clverpanda on 2017/4/7 0007.
 * It's the file for NFShare.
 */

public class TasksDbHelper extends SQLiteOpenHelper
{
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "NFShare_DB";
    private static final String TABLE_NAME = "Tasks";
    private static final String KEY_ID = "Id";
    private static final String KEY_NAME = "Name";
    private static final String KEY_DESCRIPTION = "Description";
    private static final String KEY_TYPE = "Type";
    private static final String KEY_FROMDEVICE = "FromDevice";
    private static final String KEY_STATUS = "Status";
    private static final String KEY_RECEIVETIME = "ReceiveTime";

    public TasksDbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, VERSION);
    }


    public TasksDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                         int version)
    {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(DeviceDbHelper.createSql);
        db.execSQL(DeviceDbHelper.createSql2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {}

    public void addTaskInfo(TaskInfo taskInfo)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, taskInfo.getName());
        values.put(KEY_DESCRIPTION, taskInfo.getDescription());
        values.put(KEY_TYPE, taskInfo.getType());
        values.put(KEY_FROMDEVICE, taskInfo.getFrom());
        values.put(KEY_STATUS, taskInfo.getStatus());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public TaskInfo getTaskInfo(int Id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT Id, Name, Description, Type, FromDevice, Status, datetime(ReceiveTime, 'localtime') FROM " + TABLE_NAME +
                " WHERE Id=" + Id;
        Cursor cursor = db.rawQuery(selectQuery, null);
        TaskInfo tasksInfo = null;
        if (cursor.moveToFirst())
        {
            tasksInfo = new TaskInfo(cursor.getInt(0), cursor.getString(1),
                    cursor.getString(2), cursor.getInt(3), cursor.getString(4),
                    cursor.getInt(5), cursor.getString(6));
        }
        cursor.close();
        db.close();
        return tasksInfo;
    }

    public List<TaskInfo> queryRawSQL(String selectQuery)
    {
        List<TaskInfo> taskInfoList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst())
        {
            do {
                TaskInfo tasksInfo = new TaskInfo(cursor.getInt(0), cursor.getString(1),
                        cursor.getString(2), cursor.getInt(3), cursor.getString(4),
                        cursor.getInt(5), cursor.getString(6));
                taskInfoList.add(tasksInfo);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return taskInfoList;
    }

    public List<TaskInfo> getAllTaskInfo()
    {
        String selectQuery = "SELECT Id, Name, Description, Type, FromDevice, Status, datetime(ReceiveTime, 'localtime') FROM " + TABLE_NAME +
                " ORDER BY Id DESC";
        return queryRawSQL(selectQuery);
    }

    public List<TaskInfo> getAllDoneTaskInfo()
    {
        String selectQuery = "SELECT Id, Name, Description, Type, FromDevice, Status, datetime(ReceiveTime, 'localtime') FROM " + TABLE_NAME +
                " WHERE Status=1" +
                " ORDER BY Id DESC";
        return queryRawSQL(selectQuery);
    }

    public List<TaskInfo> getAllRunningTaskInfo()
    {
        String selectQuery = "SELECT Id, Name, Description, Type, FromDevice, Status, datetime(ReceiveTime, 'localtime') FROM " + TABLE_NAME +
                " WHERE Status=0 or Status=2 or Status=-1" +
                " ORDER BY Id DESC";
        return queryRawSQL(selectQuery);
    }


    public void setStatus(int Id, int Status)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_STATUS, Status);
        db.update(TABLE_NAME, values, KEY_ID + "=?", new String[]{Integer.toString(Id)});
        db.close();
    }

    public void deleteTaskInfo(TaskInfo taskInfo)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + "=?", new String[]{Integer.toString(taskInfo.getId())});
        db.close();
    }
}
