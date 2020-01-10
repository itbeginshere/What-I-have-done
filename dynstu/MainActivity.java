package com.example.dynstu;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private MySqlLiteOpenHelper myDbHelper;
    private SQLiteDatabase myDatabase;

    private String userAccountsTable = "userAccountDetails_tbl";
    private String employeeAccountsTable = "employeeAccountDetails_tbl";

    private EditText edtEmail;
    private EditText edtPassword;

    private Cursor c = null;
    private Cursor d = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Dynamic Body Studio");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);

        myDbHelper = new MySqlLiteOpenHelper(getApplicationContext());
        myDatabase = myDbHelper.getReadableDatabase();

        try {
            myDbHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        try {
            myDbHelper.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }


       // myDbHelper.onCreate(myDatabase);

        displayTable("classSession_tbl");
        //displayTable(userAccountsTable);
    }

    private void displayTable(String tableName){
        c = myDatabase.query(tableName, null, null, null, null,null,null);

        if(c.moveToFirst()){
            do{
                Log.e("ERROR TABLE LOG", c.getString(0) + "---" + c.getString(1)+ "---" + c.getString(2)
                        + "---" + c.getString(3)+ "---" + c.getString(4)
                        + "---" + c.getString(5)+ "---" + c.getString(6)
                        + "---" + c.getString(7)+ "---" + c.getString(8)
                        + "---" + c.getString(9)+ "---" + c.getString(10)
                        + "---" + c.getString(12)+ "---" + c.getString(12));
            } while(c.moveToNext());
        }

        c.close();
    }

    public void onSignUp (View view){
        Intent myIntent = new Intent(getApplicationContext(), SignUp.class);
        startActivity(myIntent);
        finish();
    }

    public void onLogin(View view) {

        String emailText = getEmail();
        String passwordText = getPassword();

        if(emailExistsGuest(emailText) == true){
            if (fetchPasswordGuest().equals(passwordText) == true){
                Intent myIntent = new Intent(getApplicationContext(), Users.class);

                Bundle extra = new Bundle();

                extra.putString("TAG", "C");
                extra.putString("EMAIL", emailText);

                myIntent.putExtras(extra);

                startActivity(myIntent);

                finish();
            } else {
                Toast.makeText(getApplicationContext(),"INCORRECT EMAIL OR PASSWORD", Toast.LENGTH_LONG).show();
            }
        } else if(emailExistsEmployee(emailText) == true){
            if (fetchPasswordEmployee().equals(passwordText) == true){
                Intent myIntent = new Intent(getApplicationContext(), Users.class);

                Bundle extra = new Bundle();

                extra.putString("TAG", "E");
                extra.putString("EMAIL", emailText);

                myIntent.putExtras(extra);

                startActivity(myIntent);

                finish();
            } else {
                Toast.makeText(getApplicationContext(),"INCORRECT EMAIL OR PASSWORD", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(),"INCORRECT EMAIL OR PASSWORD", Toast.LENGTH_LONG).show();
        }
    }

    private String fetchPasswordGuest(){
        String stored_password;

        c = myDatabase.query(userAccountsTable,new String[] {"Password"},"Email = ? ",new String[] {getEmail()},null,null,null);
        c.moveToFirst();
        stored_password = c.getString(0);
        c.close();

        return stored_password;
    }

    private boolean emailExistsGuest(String email){
        boolean found = false;

        c = myDatabase.query(userAccountsTable, null, null, null, null, null, null);

        if(c.moveToFirst()){
            do{
                if(c.getString(4).equals(email)){
                    found = true;
                    return found;
                }
            } while (c.moveToNext());
        }

        return found;
    }

    private String fetchPasswordEmployee(){
        String stored_password = "";

        c = myDatabase.query(employeeAccountsTable, new String[] {"Password"},"Email = ?",new String[] {getEmail()},null,null,null);
        c.moveToFirst();
        stored_password = c.getString(0);
        c.close();

        return stored_password;
    }

    private boolean emailExistsEmployee(String email){
        boolean found = false;

        c = myDatabase.query(employeeAccountsTable, null, null, null, null, null, null);

        if(c.moveToFirst()){
            do{
                if(c.getString(3).equals(email)){
                    found = true;
                    return found;
                }
            } while (c.moveToNext());
        }

        return found;
    }

    public String getEmail(){
        return edtEmail.getText().toString();
    }

    public String getPassword(){
        return edtPassword.getText().toString();
    }


}
