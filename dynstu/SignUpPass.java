package com.example.dynstu;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignUpPass extends AppCompatActivity {

    private MySqlLiteOpenHelper myDbHelper;
    private SQLiteDatabase myDatabase;

    private String userName;
    private String userSurname;
    private String userEmail;
    private String userAge;

    private EditText edtPassword;
    private EditText edtConfirm;

    private String userAccountsTable = "userAccountDetails_tbl";

    private Cursor c = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setTitle("Sign Up Screen (2/2)");

        edtPassword = (EditText) findViewById(R.id.edtPass);
        edtConfirm = (EditText) findViewById(R.id.edtPassConfirm);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        userName = extras.getString("NAME");
        userSurname = extras.getString("SURNAME");
        userEmail = extras.getString("EMAIL");
        userAge = extras.getString("AGE");
    }

    public void onBack(View v) {
        Intent myIntent = new Intent(getApplicationContext(), SignUp.class);
        startActivity(myIntent);
        finish();
    }

    public void onReg(View v) {

        newCode gen = new newCode(getApplicationContext());
        String newUserCode = gen.generateID("G",userAccountsTable);

        if (checkForBlanks() == true){
            if (checkFields() == true) {
                myDbHelper = new MySqlLiteOpenHelper(getApplicationContext());
                myDatabase = myDbHelper.getWritableDatabase();

                SQLOperations oper = new SQLOperations(getApplicationContext(), "W");

                ContentValues values_new = new ContentValues();
                values_new.put("Guest_ID", newUserCode);
                values_new.put("First_Name", userName);
                values_new.put("Last_Name", userSurname);
                values_new.put("Age", userAge);
                values_new.put("Email", userEmail);
                values_new.put("Password", getPassword());
                values_new.put("Active", 1);
                oper.insertData(userAccountsTable, values_new);

                Toast.makeText(getApplicationContext(), "ACCOUNT CREATED SUCCESSFULLY", Toast.LENGTH_LONG).show();

                Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(myIntent);
                finish();
            }
        }
    }


    private boolean checkForBlanks(){
        if(getPassword().equals("")==true){
            edtPassword.setError("Password cannot be blank!");
            return false;
        } else if (getConfrim().equals("")==true){
            edtConfirm.setError("Confirm Password cannot be blank!");
            return false;
        }
        return true;
    }

    private boolean checkFields(){
        if(validPass()==false){
            return false;
        } else if (passMatch()==false) {
            return false;
        }
        return true;
    }

    private boolean validPass(){
        if(getPassword().length()<8){
            edtPassword.setError("Your pasword must be longer then 8 characters!");
            //Toast.makeText(getApplicationContext(),"Your pasword must be longer then 8 characters",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean passMatch(){
        if(getPassword().equals(getConfrim())==false){
            edtConfirm.setError("The passwords do not match!");
            //Toast.makeText(getApplicationContext(),"The passwords do not match",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public String getPassword(){
        return edtPassword.getText().toString();
    }

    public String getConfrim(){
        return edtConfirm.getText().toString();
    }

}