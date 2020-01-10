package com.example.dynstu;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Setting_Cust  extends AppCompatActivity{

    private String userCode;
    private String userFirstName;
    private String userLastName;

    private MySqlLiteOpenHelper myDbHelper;
    private SQLiteDatabase myDatabase;

    private String userAccountsTable = "userAccountDetails_tbl";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_cust);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        myDbHelper = new MySqlLiteOpenHelper(getApplicationContext());
        myDatabase = myDbHelper.getReadableDatabase();

        Intent myIntent = getIntent();

        Bundle extras = myIntent.getExtras();

        userCode = extras.getString("CODE");
        userFirstName = extras.getString("FIRSTNAME");
        userLastName = extras.getString("LASTNAME");

        final ToggleButton tb1 = (ToggleButton)findViewById(R.id.cust_toggle);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Settings");
        mToolbar.setNavigationIcon(R.drawable.ic_arrow);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), Events.class);

                Bundle extra = new Bundle();

                extra.putString("CODE", getUserCode());
                extra.putString("FIRSTNAME", getUserFirstName());
                extra.putString("LASTNAME", getUserLastName());

                i.putExtras(extra);

                startActivity(i);
                finish();
            }
        });


    }

    public void onToggle(View view){
        Toast.makeText(getApplicationContext(), "Your notification settings have been changed.", Toast.LENGTH_LONG).show();
    }

    public void onChange(View view){
        Intent i = new Intent(getApplicationContext(), ChangePassword.class);

        Bundle extra = new Bundle();

        extra.putString("CODE", getUserCode());
        extra.putString("FIRSTNAME", getUserFirstName());
        extra.putString("LASTNAME", getUserLastName());
        extra.putString("GUESTCODE", getUserCode());

        i.putExtras(extra);

        startActivity(i);
        finish();
    }

    public void onDelete(View view){
        AlertDialog.Builder confirm = new AlertDialog.Builder(this);
        confirm.setMessage("Are your sure you want to delete your account?");
        confirm.setCancelable(false);

        confirm.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SQLOperations oper = new SQLOperations(getApplicationContext(), "W");
                ContentValues update_values = new ContentValues();
                update_values.put("Active", 1);
                oper.updateData(userAccountsTable, update_values, "Guest_ID = ?", new String[]{getUserCode()});
                Toast.makeText(getApplicationContext(), "Your account has been deleted.", Toast.LENGTH_LONG).show();

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        confirm.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        confirm.create().show();
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
