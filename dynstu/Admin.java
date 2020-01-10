package com.example.dynstu;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Admin extends AppCompatActivity {

    private String adminCode;
    private String adminFirstName;
    private String adminLastName;
    private Context appContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Intent myIntent = getIntent();

        Bundle extra = myIntent.getExtras();

        adminCode = extra.getString("CODE");
        adminFirstName = extra.getString("FIRSTNAME");
        adminLastName = extra.getString("LASTNAME");
        appContext = getApplicationContext();

        goToHomeActivity();

    }

    public void goToHomeActivity(){

        Intent myIntent = new Intent(getApplicationContext(), EventsAdmin.class);

        Bundle extra = new Bundle();
        extra.putString("CODE", getAdminCode());
        extra.putString("FIRSTNAME", getAdminFirstName());
        extra.putString("LASTNAME", getAdminLastName());
        myIntent.putExtras(extra);

        startActivity(myIntent);

        finish();
    }

    public String getAdminCode(){
        return adminCode;
    }

    public String getAdminFirstName(){
        return adminFirstName;
    }

    public String getAdminLastName(){
        return adminLastName;
    }

    private Context getAppContext(){
        return appContext;
    }



}
