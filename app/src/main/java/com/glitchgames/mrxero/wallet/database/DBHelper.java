package com.glitchgames.mrxero.wallet.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DBHelper  extends SQLiteOpenHelper{
    private static final String DATABASE_NAME;
    private static final int DATABASE_VERSION;
    private static final String TABLE_NAME;
    private static final String ID;
    private static final String MAIN_BALANCE;
    private static final String WITHDRAW;
    private static final String DEPOSIT;
    private static final String FINAL_BALANCE;
    private static final String TIME;
    private static final String SQL_QUERY;
    private static final String UPDATE_QUERY;
    private static final String GET_DATA_FROM_BALANCE;
    private static final String GET_FINAL_BALANCE;
    private static final String GET_MAIN_BALANCE;
    private static final String GET_LAST_TRANSACTION;
    private static final String GET_TRANSACTION_LOG;
    private static final String UNDO_LAST_TRANSACTION;
    private static final String CLEAR_TRANSATION_LOG;
    private static final String IS_VISIBLE;
    private static final String IS_VISIBLE_TRUE;

    static {
        DATABASE_NAME = "Wallet.db";
        TABLE_NAME = "UserTransactionTable";
        ID = "ID";
        MAIN_BALANCE = "MainBalance";
        WITHDRAW = "Withdraw";
        DEPOSIT = "Deposit";
        TIME = "LogTime";
        FINAL_BALANCE = "FinalBalance";
        IS_VISIBLE= "IsVisible";
        DATABASE_VERSION = 1;
        SQL_QUERY = "CREATE TABLE "+TABLE_NAME+" (\n" +
                "\t"+ID+"\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "\t"+MAIN_BALANCE+"\tDOUBLE(255),\n" +
                "\t"+WITHDRAW+"\tDOUBLE(255),\n" +
                "\t"+DEPOSIT+"\tDOUBLE(255),\n" +
                "\t"+TIME+"\tTIMESTAMP current_time,\n" +
                "\t"+FINAL_BALANCE+"\tDOUBLE(255),\n" +
                "\t"+IS_VISIBLE+"\tINTEGER DEFAULT 1\n" +
                ");";
        UPDATE_QUERY ="DROP TABLE IF EXISTS "+ TABLE_NAME;
        GET_DATA_FROM_BALANCE = "SELECT * FROM "+ TABLE_NAME;
        GET_FINAL_BALANCE = "SELECT * FROM "+ TABLE_NAME +" ORDER BY "+ID+" DESC LIMIT 1";
        GET_MAIN_BALANCE = "SELECT * FROM "+ TABLE_NAME +" ORDER BY "+ID+" DESC LIMIT 1";
        GET_LAST_TRANSACTION = "SELECT * FROM "+ TABLE_NAME +" ORDER BY "+ID+" DESC LIMIT 1";
        GET_TRANSACTION_LOG = "SELECT * FROM "+ TABLE_NAME +" WHERE "+IS_VISIBLE+"=1 ORDER BY "+ID+" DESC";
        UNDO_LAST_TRANSACTION = "DELETE FROM "+ TABLE_NAME +" WHERE "+ID+"=(SELECT "+ID+" FROM "+TABLE_NAME+" ORDER BY ID DESC LIMIT 1)";
        CLEAR_TRANSATION_LOG = "UPDATE "+ TABLE_NAME +" SET "+IS_VISIBLE+" = 0";
        IS_VISIBLE_TRUE = "SELECT * FROM "+ TABLE_NAME +" ORDER BY "+ID+" DESC LIMIT 1";
    }

    Context context;
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            db.execSQL(SQL_QUERY);
            Toast.makeText(context, "Database Created Successfully", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            Toast.makeText(context, "Exception: "+e, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try{
            db.execSQL(UPDATE_QUERY);
            onCreate(db);
            Toast.makeText(context, "Database Updated Successfully", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            Toast.makeText(context, "Exception: "+e, Toast.LENGTH_LONG).show();
        }
    }

    public long insertData(Double MainBalance,Double Withdraw,Double Deposit, Double FinalBalance){
        SQLiteDatabase sqLiteDatabase = null;

        try{
            sqLiteDatabase=this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MAIN_BALANCE, MainBalance);
            contentValues.put(WITHDRAW,Withdraw);
            contentValues.put(DEPOSIT, Deposit);
            String currentTS = this.getCurrentTimeStampAsString();
            contentValues.put(TIME, currentTS);
            contentValues.put(FINAL_BALANCE, FinalBalance);

            long rowID = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
            return rowID;

        }
        catch (Exception e){
            Log.d("exc-insert",e.getLocalizedMessage());
        }

        finally {
            if(sqLiteDatabase!=null){
                sqLiteDatabase.close();
            }
        }
        return 0;
    }

    public void undoLastTransaction(){
        SQLiteDatabase writableDatabase=null;
        try{
            writableDatabase=this.getWritableDatabase();
            writableDatabase.execSQL(UNDO_LAST_TRANSACTION);
        }
        catch (Exception e){
            Log.d("undoLastTransaction", e.getLocalizedMessage());
        }
//        finally {
//            if(writableDatabase!=null){
//                writableDatabase.close();
//            }
//        }
    }

    public void clearTransactionLog(){
        SQLiteDatabase writableDatabase = null;
        try{
            writableDatabase=this.getWritableDatabase();
            writableDatabase.execSQL(CLEAR_TRANSATION_LOG);
        }
        catch (Exception e){
            Log.d("clearLastTransaction", e.getLocalizedMessage());
        }
//        finally {
//            if(writableDatabase!=null) {
//                writableDatabase.close();
//            }
//        }
    }

    public long updateMainBalance(Double FinalBalance){
        try {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MAIN_BALANCE, FinalBalance);

            long rowID = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
            return rowID;
        }
        catch (Exception e){
            Log.d("updateMainBalance", e.getLocalizedMessage());
        }
        return 0;
    }

    public Cursor getData(){
        SQLiteDatabase sqLiteDatabase = null;
        try {
            sqLiteDatabase = this.getWritableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery(GET_DATA_FROM_BALANCE, null);
            return cursor;
        }
        catch (Exception e){
            Log.d("getData()", e.getLocalizedMessage());
        }
//        finally {
//            if (sqLiteDatabase != null){
//                sqLiteDatabase.close();
//            }
//        }
        return null;
    }

    public Cursor getTransactionLog(){
        SQLiteDatabase sqLiteDatabase = null;
        try {
            sqLiteDatabase = this.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery(GET_TRANSACTION_LOG, null);
            return cursor;
        }
        catch (Exception e){
            Log.d("getTransactionLog()", e.getLocalizedMessage());
        }
//        finally {
//            if(sqLiteDatabase!=null){
//                sqLiteDatabase.close();
//            }
//        }
        return null;
    }

    public String getCurrentTimeStampAsString(){
        Timestamp ts=new Timestamp(System.currentTimeMillis());
        return new SimpleDateFormat("HH:mm a | dd-MMMM-yyyy", Locale.ENGLISH).format(ts);
    }

    public Cursor getFinalBalance(){
        SQLiteDatabase sqLiteDatabase = null;
        try {
            sqLiteDatabase = this.getWritableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery(GET_FINAL_BALANCE, null);
            return cursor;
        }
        catch (Exception e){
            Log.d("getFinalBalance()", e.getLocalizedMessage());
        }
//        finally {
//            if (sqLiteDatabase != null){
//                sqLiteDatabase.close();
//            }
//        }
        return null;
    }

    public Cursor getMainBalance(){
        SQLiteDatabase sqLiteDatabase = null;
        try {
            sqLiteDatabase = this.getWritableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery(GET_MAIN_BALANCE, null);
            return cursor;
        }
        catch (Exception e){
            Log.d("getMainBalance()", e.getLocalizedMessage());
        }
//        finally {
//            if (sqLiteDatabase != null){
//                sqLiteDatabase.close();
//            }
//        }
        return null;
    }

    public Cursor getLastTransaction(){
        SQLiteDatabase sqLiteDatabase = null;
        try {
            sqLiteDatabase = this.getWritableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery(GET_LAST_TRANSACTION, null);
            return cursor;
        }
        catch (Exception e){
            Log.d("getLastTransaction()", e.getLocalizedMessage());
        }
//        finally {
//            if (sqLiteDatabase != null){
//                sqLiteDatabase.close();
//            }
//        }
        return null;
    }

    public Cursor getIsVisibleTrue(){
        SQLiteDatabase sqLiteDatabase = null;
        try {
            sqLiteDatabase = this.getWritableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery(IS_VISIBLE_TRUE, null);
            return cursor;
        }
        catch (Exception e){
            Log.d("getIsVisibleTrue", e.getLocalizedMessage());
        }
        return null;
    }

}
