package com.example.dynstu;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Employee extends AppCompatActivity {

    private String employeeCode;
    private String employeeFirstName;
    private String employeeLastName;
    private Context appContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        Intent myIntent = getIntent();

        Bundle extra = myIntent.getExtras();

        employeeCode = extra.getString("CODE");
        employeeFirstName = extra.getString("FIRSTNAME");
        employeeLastName = extra.getString("LASTNAME");
        appContext = getApplicationContext();

        goToHomeActivity();

    }

    public void goToHomeActivity(){
        Intent myIntent = new Intent(getApplicationContext(), EventsAdmin.class);

        Bundle extra = new Bundle();
        extra.putString("CODE", getEmployeeCode());
        extra.putString("FIRSTNAME", getEmployeeFirstName());
        extra.putString("LASTNAME", getEmployeeLastName());
        myIntent.putExtras(extra);

        startActivity(myIntent);

        finish();

    }

    public String getEmployeeCode(){
        return employeeCode;
    }

    public String getEmployeeFirstName(){
        return employeeFirstName;
    }

    public String getEmployeeLastName(){
        return employeeLastName;
    }

    private Context getAppContext(){
        return appContext;
    }

}
