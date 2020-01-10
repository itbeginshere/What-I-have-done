package com.example.dynstu;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class SQLOperations extends AppCompatActivity {

    private MySqlLiteOpenHelper myDbHelper;
    private SQLiteDatabase myDatabase;

    private Context appContext;

    public SQLOperations(Context context, String tag){
        myDbHelper = new MySqlLiteOpenHelper(context);
        if(tag.equals("W") == true){
            setMyDatabaseWrite();
        } else if(tag.equals("R") == true){
            setMyDatabaseRead();
        } else {
            setMyDatabaseRead();
        }
        appContext = context;
    }

    public boolean checkIfActive(String active){
        if(active.equals("1")){
            return true;
        } else {
            return false;
        }
    }

    public void updateData(String tableName, ContentValues updateValues, String wehereClause, String[] whereArgs){
        int noOfRows = myDatabase.update(tableName, updateValues,wehereClause, whereArgs);

    }

    public void insertData(String tableName, ContentValues insertValues){
        long rowID = myDatabase.insert(tableName,null, insertValues);

    }


    public void deleteData(String tableName, String whereClause, String[] whereArgs){

        int rowID = myDatabase.delete(tableName,whereClause, whereArgs);

    }

    public Context getContext(){
        return appContext;
    }

    public MySqlLiteOpenHelper getMyDbHelper(){
        return myDbHelper;
    }

    public SQLiteDatabase getMyDatabase(){
        return myDatabase;
    }

    private void setMyDatabaseRead(){
        myDatabase = myDbHelper.getReadableDatabase();
    }

    private void setMyDatabaseWrite(){
        myDatabase = myDbHelper.getWritableDatabase();
    }

}
