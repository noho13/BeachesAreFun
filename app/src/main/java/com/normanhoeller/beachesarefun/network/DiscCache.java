package com.normanhoeller.beachesarefun.network;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;

import com.normanhoeller.beachesarefun.Utils;

/**
 * Created by normanMedicuja on 27/04/17.
 */

public class DiscCache extends SQLiteOpenHelper {

    private static final String TAG = DiscCache.class.getSimpleName();

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "BeachCache.db";
    private static final String TABLE_NAME = "bitmaps";
    private static final String COL_KEY = "key";
    private static final String COL_BITMAP = "bitmap";
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + TABLE_NAME + " (" + COL_KEY + " TEXT,"
                                                                                            + COL_BITMAP + " BLOB)";
    private static final String SQL_DELETE_ENTRIES = "DELETE FROM " + TABLE_NAME + " WHERE 1";

    public DiscCache(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Bitmap cache created");
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // just delete content
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void put(String key, Bitmap bitmap) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_KEY, key);
        values.put(COL_BITMAP, Utils.getBytesFromBitmap(bitmap));
        db.insert(TABLE_NAME, null, values);
    }

    public Bitmap get(String key) {
        Bitmap bitmap = null;
        Cursor cursor = getKeyCursor(key);
        if (cursor.moveToFirst()) {
            byte[] tmp = cursor.getBlob(cursor.getColumnIndex(COL_BITMAP));
            bitmap = Utils.getImageFromBytes(tmp);
        }
        cursor.close();
        return bitmap;
    }

    public boolean isBitmapInCache(String key) {
        Cursor cursor = getKeyCursor(key);
        return (cursor.moveToFirst() && cursor.getCount() != 0);
    }

    private Cursor getKeyCursor(String key) {
        SQLiteDatabase db = this.getWritableDatabase();
        String table = "bitmaps";
        String[] projection = {"key", "bitmap"};
        String where = "key=?";
        String[] selectionArgs = new String[]{key};
        String sortOrder = "key DESC";
        return db.query(table, projection, where, selectionArgs, null, null, sortOrder);
    }

}
