package com.clverpanda.nfshare.util.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.clverpanda.nfshare.model.DownloadThreadInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by clverpanda on 2017/4/12 0012.
 * It's the file for NFShare.
 */

public class ThreadDbHelper extends SQLiteOpenHelper
{
    private static final String TAG = "ThreadDbHelper";
    private static final String DB_NAME = "download.db";
    private static final String KEY_TASK_ID = "task_id";
    private static final String KEY_URL = "url";
    private static final String KEY_START = "start";
    private static final String KEY_END = "end";
    private static final String KEY_FINISHED = "finished";

    private static final String SQL_CREATE = "CREATE TABLE thread_info(task_id integer primary key," +
                                                                    "url text," +
                                                                    "start long," +
                                                                    "end long," +
                                                                    "finished long)";
    private static final String SQL_DROP = "drop table if exists thread_info";
    private static final int VERSION = 1;


    public ThreadDbHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(SQL_DROP);
        db.execSQL(SQL_CREATE);
    }

    public synchronized void insertThread(DownloadThreadInfo threadInfo)
    {
        Log.e("insertThread: ", "insertThread");
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("insert into thread_info(task_id,url,start,end,finished) values(?,?,?,?,?)",
                new Object[]{threadInfo.getId(), threadInfo.getUrl(),
                        threadInfo.getStart(), threadInfo.getEnd(), threadInfo.getFinish()});
        db.close();
    }

    public synchronized void deleteThread(int taskId)
    {
        Log.e("deleteThread: ", "deleteThread");
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from  thread_info where task_id = ?",
                new Object[]{taskId});
        db.close();
    }

    public synchronized void updateThread(int taskId, long finished)
    {
        Log.e("updateThread: ", "updateThread");
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("update thread_info set finished = ?  where task_id=?",
                new Object[]{finished, taskId});
        db.close();
    }

    public DownloadThreadInfo getThread(int taskId)
    {
        Log.e("getThread: ", "getThread");
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from thread_info where task_id=?", new String[]{Integer.toString(taskId)});
        DownloadThreadInfo thread;
        if (cursor.moveToFirst())
        {
            thread = new DownloadThreadInfo();
            thread.setId(cursor.getInt(cursor.getColumnIndex("task_id")));
            thread.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            thread.setStart(cursor.getLong(cursor.getColumnIndex("start")));
            thread.setEnd(cursor.getLong(cursor.getColumnIndex("end")));
            thread.setFinish(cursor.getLong(cursor.getColumnIndex("finished")));
        }
        else
            thread = null;
        cursor.close();
        db.close();
        return thread;
    }

    public boolean isExists(int taskId)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from thread_info where task_id = ?",
                new String[]{Integer.toString(taskId)});
        boolean isExist = cursor.moveToNext();
        cursor.close();
        db.close();
        Log.e(TAG, "isExists: " + isExist);
        return isExist;
    }
}
