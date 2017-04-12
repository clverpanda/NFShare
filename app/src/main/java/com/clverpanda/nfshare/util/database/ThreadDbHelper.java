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
    private static final String SQL_CREATE = "CREATE TABLE thread_info(_id integer primary key autoincrement," +
                                                                    "thread_id integer," +
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
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP);
        db.execSQL(SQL_CREATE);
    }

    public synchronized void insertThread(DownloadThreadInfo threadInfo) {
        Log.e("insertThread: ", "insertThread");
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("insert into thread_info(thread_id,url,start,end,finished) values(?,?,?,?,?)",
                new Object[]{threadInfo.getId(), threadInfo.getUrl(),
                        threadInfo.getStart(), threadInfo.getEnd(), threadInfo.getFinish()});
        db.close();
    }

    public synchronized void deleteThread(String url) {
        Log.e("deleteThread: ", "deleteThread");
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from  thread_info where url = ?",
                new Object[]{url});
        db.close();
    }

    public synchronized void updateThread(String url, int thread_id, long finished) {
        Log.e("updateThread: ", "updateThread");
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("update thread_info set finished = ?  where url = ? and thread_id=?",
                new Object[]{finished, url, thread_id});
        db.close();
    }

    public List<DownloadThreadInfo> getThread(String url) {
        Log.e("getThread: ", "getThread");
        List<DownloadThreadInfo> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from thread_info where url=?", new String[]{url});
        while (cursor.moveToNext()) {
            DownloadThreadInfo thread = new DownloadThreadInfo();
            thread.setId(cursor.getInt(cursor.getColumnIndex("thread_id")));
            thread.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            thread.setStart(cursor.getLong(cursor.getColumnIndex("start")));
            thread.setEnd(cursor.getLong(cursor.getColumnIndex("end")));
            thread.setFinish(cursor.getLong(cursor.getColumnIndex("finished")));
            list.add(thread);
        }
        cursor.close();
        db.close();
        return list;
    }

    public boolean isExists(String url, int thread_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from thread_info where url=? and thread_id = ?",
                new String[]{url, String.valueOf(thread_id)});
        boolean isExist = cursor.moveToNext();
        cursor.close();
        db.close();
        Log.e(TAG, "isExists: " + isExist);
        return isExist;
    }
}
