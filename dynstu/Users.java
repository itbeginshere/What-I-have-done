package com.example.dynstu;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class Users extends AppCompatActivity {

    private String accountTag;
    private String accountEmail;
    private Context appContext;
    private String adminEmail = "Admin@gmail.com";

    private String userDetailsTable = "userAccountDetails_tbl";
    private String employeeDetailsTable = "employeeAccountDetails_tbl";

    private Cursor c = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        Intent myIntent = getIntent();

        Bundle extras = myIntent.getExtras();

        accountTag = extras.getString("TAG");
        accountEmail = extras.getString("EMAIL");
        appContext = getApplicationContext();

        deteremineUser();
    }

    public void deteremineUser(){
        if(getAccountTag().equals("E") || getAccountEmail().equals(adminEmail)){
            sendDetailsEmployee(employeeDetailsTable);
        } else {
            sendDetailsCustomer(userDetailsTable);
        }
    }

    private void sendDetailsEmployee(String tableName){

        String code;
        String firstName;
        String lastName;

        SQLOperations oper = new SQLOperations(getAppContext(),"R");

        SQLiteDatabase myDatabase = oper.getMyDatabase();

        c = myDatabase.query(tableName, null,"Email = ?",new String[] {getAccountEmail()},null,null,null);

        c.moveToFirst();

        if(oper.checkIfActive(c.getString(5)) == false){
            //proceed with pulling
            code = c.getString(0);
            firstName = c.getString(1);
            lastName = c.getString(2);

            if(getAccountEmail().equals(adminEmail)){
                Intent myIntent = new Intent(getAppContext(), Admin.class);

                Bundle extra = new Bundle();

                extra.putString("CODE", code);
                extra.putString("FIRSTNAME", firstName);
                extra.putString("LASTNAME", lastName);

                myIntent.putExtras(extra);

                startActivity(myIntent);

                finish();
            } else {
                Intent myIntent = new Intent(getAppContext(), Employee.class);

                Bundle extra = new Bundle();

                extra.putString("CODE", code);
                extra.putString("FIRSTNAME", firstName);
                extra.putString("LASTNAME", lastName);

                myIntent.putExtras(extra);

                startActivity(myIntent);

                finish();
            }
        } else {
            //This employee no longer is usable
            Toast.makeText(getAppContext(), "The Account you tried to sign in with is no longer allowed to be used.", Toast.LENGTH_SHORT).show();
            Intent myIntent = new Intent(getAppContext(), MainActivity.class);
            startActivity(myIntent);
            finish();
        }

        c.close();

    }

    private void sendDetailsCustomer(String tableName){
        String code;
        String firstName;
        String lastName;

        SQLOperations oper = new SQLOperations(getAppContext(), "R");

        SQLiteDatabase myDatabase = oper.getMyDatabase();

        c = myDatabase.query(tableName, null,"Email = ?",new String[] {getAccountEmail()},null,null,null);

        c.moveToFirst();

        if(oper.checkIfActive(c.getString(6)) == true){
            //proceed with pulling
            code = c.getString(0);
            firstName = c.getString(1);
            lastName = c.getString(2);

            Intent myIntent = new Intent(getAppContext(), Customer.class);

            Bundle extra = new Bundle();

            extra.putString("CODE", code);
            extra.putString("FIRSTNAME", firstName);
            extra.putString("LASTNAME", lastName);

            myIntent.putExtras(extra);

            startActivity(myIntent);

            finish();
        } else {
            //This customer no longer is usable

            Toast.makeText(getAppContext(), "The Account you tried to sign in with is no longer allowed to be used.", Toast.LENGTH_SHORT).show();
            Intent myIntent = new Intent(getAppContext(), MainActivity.class);
            startActivity(myIntent);
            finish();
        }

        c.close();
    }

    private String getAccountTag(){
        return accountTag;
    }

    private String getAccountEmail(){
        return accountEmail;
    }

    private Context getAppContext(){
        return appContext;
    }



}
