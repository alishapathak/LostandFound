package com.sourcey.materiallogindemo.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

    public Database(Context context) {
        super(context, "lostandfound.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table x (id int)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP table if exists x");

    }

    public boolean insertData(int id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", "x");

        long result = sqLiteDatabase.insert("x", null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }
}
