//package com.clverpanda.nfshare.Util.Database;
//
//import android.content.Context;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//
///**
// * Created by clverpanda on 2017/4/6 0006.
// * It's the file for NFShare.
// */
//
//public class DeviceDbHelper extends SQLiteOpenHelper
//{
//    public DeviceDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
//                          int version)
//    {
//        super(context, name, factory, version);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase db)
//    {
//        String sql = "CREATE TABLE Devices ( Id INTEGER primary key autoincrement, " + BOOK_NAME + " text, "+ BOOK_AUTHOR +" text);";
//        db.execSQL(sql);
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
//}
