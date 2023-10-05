package com.example.venato.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {

    public final static String DATABASE_NAME = "Login.db";
    public final static String TABLE_NAME = "login";
    public final static String col1 = "USERNAME";
    public final static String col2 = "PASSWORD";
    public final static String col3 = "LOGIN";
    public DataBaseHelper(@Nullable Context context) {
        super(context,  DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE "+ TABLE_NAME +" (USERNAME TEXT, PASSWORD TEXT, LOGIN TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean insertData(String username, String password, String login) {
        SQLiteDatabase sqLiteDatabase =this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(col1, username);
        contentValues.put(col2, password);
        contentValues.put(col3, login);
        long result = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        if(result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isEmpty(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        long r = DatabaseUtils.queryNumEntries(sqLiteDatabase, TABLE_NAME);
        if(r==0) {
            return true;
        } else {
            return false;
        }
    }

    public void deleteData() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM "+ TABLE_NAME);
    }


    public String getUsername(Context context){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM "+ TABLE_NAME, null);

        String username = "employee1";

        if(c!=null && c.getCount()>0){
            c.moveToFirst();
            username = c.getString(0);
        }
        else {
            Toast.makeText(context, "Failure", Toast.LENGTH_SHORT).show();
        }

        return username;
    }

    public String getPassword(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM "+ TABLE_NAME, null);
        String password = c.getString(1);
        return password;
    }


    public String getLogin(Context context){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM "+ TABLE_NAME, null);
        String login = "";
        if(c!=null && c.getCount()>0){
            c.moveToFirst();
            login = c.getString(2);
        }
        else {
            Toast.makeText(context, "Failure", Toast.LENGTH_SHORT).show();
        }
        return login;
    }
}
