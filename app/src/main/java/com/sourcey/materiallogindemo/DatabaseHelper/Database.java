package com.sourcey.materiallogindemo.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.sourcey.materiallogindemo.domain.LostAndFound;

import java.io.ByteArrayOutputStream;

public class Database extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "LostAndFound.db";
    private static final String TABLE_NAME = "lost";
    private static final String TABLE_NAME1 = "found";
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (\n" +
            " category text,\n" +
            " item_name text ,\n" +
            " location text ,\n" +
            " lost_date text  ,\n" +
            " description text  ,\n" +
            " image blob  ,\n" +
            " user_name text  ,\n" +
            " user_email text  \n," +
            " user_phone text  \n" +
            ")";

    private static final String SQL_CREATE_ENTRIES1 = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME1 + " (\n" +
            " category text,\n" +
            " item_name text ,\n" +
            " location text ,\n" +
            " lost_date text  ,\n" +
            " description text  ,\n" +
            " image blob  ,\n" +
            " user_name text  ,\n" +
            " user_email text  \n," +
            " user_phone text  \n" +
            ")";
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME;
    private static final String SQL_DELETE_ENTRIES1 = "DROP TABLE IF EXISTS " + TABLE_NAME1;

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
        db.execSQL(SQL_CREATE_ENTRIES1);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL(SQL_DELETE_ENTRIES1);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


    public boolean insertData(LostAndFound lostAndFound) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        Bitmap b = lostAndFound.getImage();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 50, bos);
        byte[] img = bos.toByteArray();

        System.out.println("Item Name =" + lostAndFound.getItem_name());
        System.out.println("Item Description =" + lostAndFound.getDescription());
        ContentValues contentValues = new ContentValues();
        contentValues.put("category", lostAndFound.getCategory());
        contentValues.put("item_name", lostAndFound.getItem_name());
        contentValues.put("location", lostAndFound.getLocation());
        contentValues.put("lost_date", lostAndFound.getLost_date());
        contentValues.put("description", lostAndFound.getDescription());
        contentValues.put("image", img);
        contentValues.put("user_name", lostAndFound.getUser_name());
        contentValues.put("user_email", lostAndFound.getUser_email());
        contentValues.put("user_phone", lostAndFound.getUser_phone());

        long result = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        sqLiteDatabase.close();
        return result != -1;
    }

    public LostAndFound checkData(String item_name, String Category) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        LostAndFound lostAndFound = new LostAndFound();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + TABLE_NAME + " where category = ? and item_name =?",
                new String[]{Category, item_name}, null);
        if (cursor != null && cursor.moveToFirst()) {
            lostAndFound.setCategory(cursor.getString(cursor.getColumnIndex("category")));
            lostAndFound.setItem_name(cursor.getString(cursor.getColumnIndex("item_name")));
            lostAndFound.setLocation(cursor.getString(cursor.getColumnIndex("location")));
            lostAndFound.setLost_date(cursor.getString(cursor.getColumnIndex("lost_date")));
            lostAndFound.setDescription(cursor.getString(cursor.getColumnIndex("description")));

            byte[] img = cursor.getBlob(5);
            Bitmap image = BitmapFactory.decodeByteArray(img, 0, img.length);

            lostAndFound.setImage(image);
            lostAndFound.setUser_name(cursor.getString(cursor.getColumnIndex("user_name")));
            lostAndFound.setUser_email(cursor.getString(cursor.getColumnIndex("user_email")));
            lostAndFound.setUser_phone(cursor.getString(cursor.getColumnIndex("user_phone")));
            cursor.close();
            return lostAndFound;
        } else return null;

    }

    public boolean insertFoundData(LostAndFound lostAndFound) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        Bitmap b = lostAndFound.getImage();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 50, bos);
        byte[] img = bos.toByteArray();

        System.out.println("Item Name =" + lostAndFound.getItem_name());
        System.out.println("Item Description =" + lostAndFound.getDescription());
        ContentValues contentValues = new ContentValues();
        contentValues.put("category", lostAndFound.getCategory());
        contentValues.put("item_name", lostAndFound.getItem_name());
        contentValues.put("location", lostAndFound.getLocation());
        contentValues.put("lost_date", lostAndFound.getLost_date());
        contentValues.put("description", lostAndFound.getDescription());
        contentValues.put("image", img);
        contentValues.put("user_name", lostAndFound.getUser_name());
        contentValues.put("user_email", lostAndFound.getUser_email());
        contentValues.put("user_phone", lostAndFound.getUser_phone());

        long result = sqLiteDatabase.insert(TABLE_NAME1, null, contentValues);
        sqLiteDatabase.close();
        return result != -1;
    }

    public LostAndFound searchData(String item_name, String Category) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        LostAndFound lostAndFound = new LostAndFound();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + TABLE_NAME1 + " where category = ? and item_name =?",
                new String[]{Category, item_name}, null);
        if (cursor != null && cursor.moveToFirst()) {
            lostAndFound.setCategory(cursor.getString(cursor.getColumnIndex("category")));
            lostAndFound.setItem_name(cursor.getString(cursor.getColumnIndex("item_name")));
            lostAndFound.setLocation(cursor.getString(cursor.getColumnIndex("location")));
            lostAndFound.setLost_date(cursor.getString(cursor.getColumnIndex("lost_date")));
            lostAndFound.setDescription(cursor.getString(cursor.getColumnIndex("description")));

            byte[] img = cursor.getBlob(5);
            Bitmap image = BitmapFactory.decodeByteArray(img, 0, img.length);

            lostAndFound.setImage(image);

            lostAndFound.setUser_name(cursor.getString(cursor.getColumnIndex("user_name")));
            lostAndFound.setUser_email(cursor.getString(cursor.getColumnIndex("user_email")));
            lostAndFound.setUser_phone(cursor.getString(cursor.getColumnIndex("user_phone")));
            cursor.close();
            return lostAndFound;
        } else return null;

    }

}
