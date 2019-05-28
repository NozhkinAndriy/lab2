package com.example.nalen.weathertestproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseProcessor extends SQLiteOpenHelper {

    public static final String TAG = "DatabaseHelper";
    private static final String TABLE_NAME = "bookmarked_locs";
    public static final String COL_1 = "ID";
    private static final String COL_2 = "location_name";
    private static final String COL_3 = "location_coords";

    public DatabaseProcessor(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = " CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_2 + " TEXT, " + COL_3 + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String itemName, String itemLocation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, itemName);
        contentValues.put(COL_3, itemLocation);

        long result = db.insert(TABLE_NAME, null, contentValues);
        Log.d("TEST", Double.toString(result));
        return result != -1;
    }

    public List<BookmarkedCityItemInfo> getData() {
        List<BookmarkedCityItemInfo> locationlist = new ArrayList<BookmarkedCityItemInfo>() {
        };
        String query = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                //   locationlist.put(cursor.getString(0), cursor.getString(1));
                locationlist.add(new BookmarkedCityItemInfo(cursor.getString(0), cursor.getString(1), cursor.getString(2)));
            } while (cursor.moveToNext());
        }
        return locationlist;
    }

    public boolean removeItem(int itemIndex) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "ID = " + itemIndex, null);
        return true;
    }
}


  /*  public ArrayList<String> getData()
    {
        ArrayList<String> resultList =  null;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor result = db.getAll(query, null);
        for(int i = 0; i < result.getCount(); i++)
        {
            resultList.add(result.getString(i));
            result.moveToNext();
        }
        return resultList;
    }*/