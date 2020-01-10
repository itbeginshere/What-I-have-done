package com.example.dynstu;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePassword extends AppCompatActivity {

    private String userCode;
    private String userFirstName;
    private String userLastName;

    private String alteringUserCode;

    private EditText edtPassword;
    private EditText edtConfirm;

    private Button btnChangePassword;

    private MySqlLiteOpenHelper myDbHelper;
    private SQLiteDatabase myDatabase;

    private String userAccountsTable = "userAccountDetails_tbl";
    private String employeeDetailsTable = "employeeAccountDetails_tbl";

    private Cursor d = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        myDbHelper = new MySqlLiteOpenHelper(getApplicationContext());
        myDatabase = myDbHelper.getWritableDatabase();

        edtPassword = findViewById(R.id.cng_pass_password);
        edtConfirm = findViewById(R.id.cng_pass_confirm);

        btnChangePassword = findViewById(R.id.cng_btn_change);

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redoPassword();
            }
        });

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        userCode = extras.getString("CODE");
        userFirstName = extras.getString("FIRSTNAME");
        userLastName = extras.getString("LASTNAME");

        alteringUserCode = extras.getString("GUESTCODE");

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Change Password");
        mToolbar.setNavigationIcon(R.drawable.ic_arrow);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(getUserTag(getUserCode()).equals("G") == true){
                    Intent i = new Intent(getApplicationContext(), Events.class);

                    Bundle extra = new Bundle();

                    extra.putString("CODE", getUserCode());
                    extra.putString("FIRSTNAME", getUserFirstName());
                    extra.putString("LASTNAME", getUserLastName());

                    i.putExtras(extra);
                    startActivity(i);
                    finish();
                } else {
                    Intent i = new Intent(getApplicationContext(), EventsAdmin.class);

                    Bundle extra = new Bundle();

                    extra.putString("CODE", getUserCode());
                    extra.putString("FIRSTNAME", getUserFirstName());
                    extra.putString("LASTNAME", getUserLastName());

                    i.putExtras(extra);
                    startActivity(i);
                    finish();
                }
            }
        });

    }

    public void redoPassword() {

        if (checkForBlanks() == true){
            if (checkFields() == true) {

                if(getUserTag(getAlteringUserCode()).equals("G") == true){

                    SQLOperations oper = new SQLOperations(getApplicationContext(), "W");

                    ContentValues values_new = new ContentValues();
                    values_new.put("Password", getPassword());

                    oper.updateData(userAccountsTable, values_new, "Guest_ID = ?", new String[] {getAlteringUserCode()});

                    Toast.makeText(getApplicationContext(), "Password Successfully Changed!", Toast.LENGTH_LONG).show();

                    Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(myIntent);
                    finish();
                } else {
                    SQLOperations oper = new SQLOperations(getApplicationContext(), "W");

                    ContentValues values_new = new ContentValues();
                    values_new.put("Password", getPassword());

                    oper.updateData(employeeDetailsTable, values_new, "Employee_ID = ?", new String[] {getAlteringUserCode()});

                    Toast.makeText(getApplicationContext(), "Password Successfully Changed!", Toast.LENGTH_LONG).show();

                    Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(myIntent);
                    finish();
                }


            }
        }
    }

    private boolean checkForBlanks(){
        if(getPassword().equals("")==true){
            edtPassword.setError("The Password cannot be blank!");
            return false;
        } else if (getConfrim().equals("")==true){
            edtConfirm.setError("The Confirm Password cannot be blank!");
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
            edtPassword.setError("Your Pasword must be longer then 8 characters!");
            return false;
        }
        return true;
    }

    private boolean passMatch(){
        if(getPassword().equals(getConfrim())==false){
            edtConfirm.setError("The Passwords do not match!");
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

    private String getAlteringUserCode(){
        return alteringUserCode;
    }

    private String getUserTag(String userCode){
        String userTag = "";

        userTag = userCode.substring(0,1);

        return userTag;
    }

    private String getUserCode(){
        return userCode;
    }

    private String getUserFirstName(){
        return userFirstName;
    }

    private String getUserLastName(){
        return userLastName;
    }
}
