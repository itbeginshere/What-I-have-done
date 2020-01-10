package com.example.dynstu;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;

public class newCode extends AppCompatActivity {

    private MySqlLiteOpenHelper myDbHelper;
    private SQLiteDatabase myDatabase;

    private Cursor c = null;

    public newCode(Context context){
        myDbHelper = new MySqlLiteOpenHelper(context);
        myDatabase = myDbHelper.getReadableDatabase();
    }

    public String generateID(String prefix, String tableName){

        int ID_CreatingCounter = 0; //number at the end of ID number
        String ID_Creating = ""; // new created ID number
        String ID_Existing; //current ID within the query
        String ID_prefix = prefix; // prefix of the ID number
        boolean newIncrement = true;

        c = myDatabase.query(tableName,null,null,null,null,null, null);  //select statment

        if(c.moveToFirst()){
            do{
                ID_Existing = c.getString(0);
                if(getUserTag(ID_Existing).equals("A") == false){
                    ID_CreatingCounter++;
                    ID_Creating = fillInZerosID(ID_CreatingCounter, ID_prefix);

                    if(iDExists(ID_Creating, ID_Existing) == false){
                        newIncrement = false;
                        break;
                    }
                }

            } while (c.moveToNext());

            if(newIncrement == true){
                ID_CreatingCounter++;
                ID_Creating = fillInZerosID(ID_CreatingCounter, ID_prefix);
            }

        } else {
            ID_Creating = prefix + "0000001";
        }

        return  ID_Creating;
    }


    private String fillInZerosID(int count, String prefix){
        int length;

        String finalID = "";
        length = String.valueOf(count).length();

        for(int k = 0; k < 7 - length; k++){
            finalID = finalID + "0";

        }

        finalID = prefix + finalID + String.valueOf(count) ;

        return finalID;
    }


    private boolean iDExists(String newID, String existingID){

        if (newID.equals(existingID)){
            return true;
        } else {
            return false;
        }
    }


    private String getUserTag(String userCode){
        String userTag = "";

        userTag = userCode.substring(0,1);

        return userTag;
    }

}
