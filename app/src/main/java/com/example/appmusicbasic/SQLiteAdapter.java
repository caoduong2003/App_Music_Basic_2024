package com.example.appmusicbasic;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class SQLiteAdapter {
    private SQLiteHelper sqLiteHelper;
    private SQLiteDatabase Database;


    public SQLiteAdapter(Context context) {
        sqLiteHelper = new SQLiteHelper(context);
    }

    public void open() {
        Database = sqLiteHelper.getWritableDatabase();
    }

    public void close() {
        sqLiteHelper.close();
    }

    public Cursor getAllUsers() {
        String sql = "SELECT * FROM users";
        return Database.rawQuery(sql, null);

    }

    public Cursor getUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        return Database.rawQuery(sql, new String[]{username, password});
    }

    public void insertUser(String username, String password, String role) {
        String sql = "INSERT INTO users(username, password, role) VALUES(?, ?, ?)";
        Database.execSQL(sql, new String[]{username, password, role});
    }



}
