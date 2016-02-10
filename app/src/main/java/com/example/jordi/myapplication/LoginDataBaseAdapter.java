package com.example.jordi.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class LoginDataBaseAdapter {
    static final String DATABASE_NAME = "login.db";
    static final int DATABASE_VERSION = 1;
    public static final int NAME_COLUMN = 1;
    // SQL Statement to create a new database.
    static final String DATABASE_CREATE = "create table "+"LOGIN"+
            "( " +"ID"+" integer primary key autoincrement,"+ "USERNAME  text,PASSWORD text,RANK integer, URI text); ";
    // Variable to hold the database instance
    public  SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private DataBaseHelper dbHelper;

    public  LoginDataBaseAdapter(Context _context) {
        context = _context;
        dbHelper = new DataBaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public  LoginDataBaseAdapter open() throws SQLException {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close()
    {
        db.close();
    }

    public  SQLiteDatabase getDatabaseInstance()
    {
        return db;
    }

    public void insertEntry(String userName, String password, int rank, String uri) {
        ContentValues newValues = new ContentValues();
        // Assign values for each row.
        newValues.put("USERNAME", userName);
        newValues.put("PASSWORD", password);
        newValues.put("RANK", rank);
        newValues.put("URI", uri);

        // Insert the row into the table
        db.insert("LOGIN", null, newValues);
    }

    public int deleteEntry(String Username) {
        String where = "USERNAME=?";
        int numberOfEntriesDeleted = db.delete("LOGIN", where, new String[]{Username}) ;
        return numberOfEntriesDeleted;
    }

    public String getPassword(String username) {
        Cursor cursor = db.query("LOGIN", null, " USERNAME=?", new String[]{username}, null, null, null);
        if (cursor.getCount() < 1 ) {
            cursor.close();
            return null;
        }
        cursor.moveToFirst();
        String password = cursor.getString(cursor.getColumnIndex("PASSWORD"));
        cursor.close();
        return password;
    }

    public int getRank(String username) {
        Cursor cursor = db.query("LOGIN", null, " USERNAME=?", new String[]{username}, null, null, null);
        if (cursor.getCount() < 1) {
            cursor.close();
            return -1;
        }
        cursor.moveToFirst();
        int rank= cursor.getInt(cursor.getColumnIndex("RANK"));
        cursor.close();
        return rank;
    }

    public String getUri(String username) {
        Cursor cursor = db.query("LOGIN", null, " USERNAME=?", new String[]{username}, null, null, null);
        if (cursor.getCount() < 1)
        {
            cursor.close();
            return null;
        }
        cursor.moveToFirst();
        String uri = cursor.getString(cursor.getColumnIndex("URI"));
        cursor.close();
        return uri;
    }


    public String[] getAllUsers() {
        Cursor cursor = db.query("LOGIN", null, null, null, null, null, "RANK" + " DESC");
        cursor.moveToFirst();
        int n = cursor.getCount();
        String[] users = new String[n];
        for (int i = 0; i < n; ++i) {
            if (i != 0) cursor.moveToNext();
            String username= cursor.getString(cursor.getColumnIndex("USERNAME"));
            users[i] = username;

        }
        cursor.close();
        return users;
    }

    public int[] returnDescOrderedRanks() {
        Cursor cursor = db.query("LOGIN", null, null, null, null, null, "RANK" + " DESC");
        cursor.moveToFirst();
        int n = cursor.getCount();
        int[] ranks = new int[n];
        for (int i = 0; i < n; ++i) {
            if (i != 0) cursor.moveToNext();
            int rank = cursor.getInt(cursor.getColumnIndex("RANK"));
            ranks[i] = rank;

        }
        cursor.close();
        return ranks;
    }

    public int getNumberOfUsers() {
        Cursor cursor = db.query("LOGIN", null, null, null, null, null, "RANK" + " DESC");
        cursor.moveToFirst();
        int n = cursor.getCount() + 1; // ¯\_(ツ)_/¯
        cursor.close();
        return n;
    }

    public Boolean checkIfUserAlreadyExists(String username) {
        Cursor cursor = db.query("LOGIN", null, " USERNAME=?", new String[]{username}, null, null, null);
        if (cursor.getCount() < 1) {
            cursor.close();
            return false;
        }
        else {
            cursor.close();
            return true;
        }
    }

    public void updateRank(String username, int rank) {
        // Define the updated row content
        ContentValues updatedValues = new ContentValues();
        // Assign values for each row
        updatedValues.put("USERNAME", username);
        updatedValues.put("RANK",rank);

        String where="USERNAME = ?";
        db.update("LOGIN",updatedValues, where, new String[]{username});
    }

    public void  updateUri(String username, String uri) {
        // Define the updated row content
        ContentValues updatedValues = new ContentValues();
        // Assign values for each row
        updatedValues.put("USERNAME", username);
        updatedValues.put("URI",uri);

        String where="USERNAME = ?";
        db.update("LOGIN",updatedValues, where, new String[]{username});
    }

    /*public void  updateEntry(String userName, String password, int rank, String uri) {
        // Define the updated row content
        ContentValues updatedValues = new ContentValues();
        // Assign values for each row
        updatedValues.put("USERNAME", userName);
        updatedValues.put("PASSWORD",password);
        updatedValues.put("RANK",rank);
        updatedValues.put("URI", uri);

        String where="USERNAME = ?";
        db.update("LOGIN",updatedValues, where, new String[]{userName});
    }*/
}