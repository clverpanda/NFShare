package com.clverpanda.nfshare.Util.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import com.clverpanda.nfshare.Model.DeviceInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by clverpanda on 2017/4/6 0006.
 * It's the file for NFShare.
 */

public class DeviceDbHelper extends SQLiteOpenHelper
{
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "NFShare_DB";
    private static final String TABLE_NAME = "Devices";
    private static final String KEY_ID = "Id";
    private static final String KEY_NAME = "Name";
    private static final String KEY_WIFIMAC = "WifiMac";
    private static final String KEY_PUBLICKEY = "PublicKey";
    static final String createSql = "CREATE TABLE Devices ( " +
                                            "Id INTEGER primary key autoincrement, " +
                                            "Name text, " +
                                            "WifiMac text, " +
                                            "PublicKey text);";
    static final String createSql2 = "CREATE TABLE Tasks ( " +
            "Id INTEGER primary key autoincrement, " +
            "Name text, " +
            "Description text, " +
            "Type INTEGER, " +
            "FromDevice text, " +
            "IsDone INTEGER, " +
            "ReceiveTime DATETIME DEFAULT CURRENT_TIMESTAMP);";
    public DeviceDbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, VERSION);
    }


    public DeviceDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                          int version)
    {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(createSql);
        db.execSQL(createSql2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {}

    public void addDeviceInfo(DeviceInfo deviceInfo)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, deviceInfo.getName());
        values.put(KEY_WIFIMAC, deviceInfo.getWifiMac());
        values.put(KEY_PUBLICKEY, deviceInfo.getPublicKey());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public DeviceInfo getDeviceInfo(int Id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{KEY_ID, KEY_NAME, KEY_WIFIMAC, KEY_PUBLICKEY},
                KEY_ID + "=?", new String[]{Integer.toString(Id)}, null, null, null, null);
        DeviceInfo deviceInfo = null;
        if (cursor.moveToFirst())
        {
            deviceInfo = new DeviceInfo(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
        }
        cursor.close();
        db.close();
        return deviceInfo;
    }


    public List<DeviceInfo> getAllDeviceInfo()
    {
        List<DeviceInfo> deviceInfoList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst())
        {
            do {
                DeviceInfo deviceInfo = new DeviceInfo(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
                deviceInfoList.add(deviceInfo);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return deviceInfoList;
    }

    public void updateDeviceInfo(DeviceInfo deviceInfo)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, deviceInfo.getName());
        values.put(KEY_WIFIMAC, deviceInfo.getWifiMac());
        values.put(KEY_PUBLICKEY, deviceInfo.getPublicKey());
        db.update(TABLE_NAME, values, KEY_ID + "=?", new String[]{Integer.toString(deviceInfo.getId())});
        db.close();
    }

    public void deleteDeviceInfo(DeviceInfo deviceInfo)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + "=?", new String[]{Integer.toString(deviceInfo.getId())});
        db.close();
    }

}
