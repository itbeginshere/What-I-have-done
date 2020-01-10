package com.example.dynstu;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Customer extends AppCompatActivity {


    private String customerCode;
    private String customerFirstName;
    private String customerLastName;
    private Context appContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        Intent myIntent = getIntent();

        Bundle extra = myIntent.getExtras();

        customerCode = extra.getString("CODE");
        customerFirstName = extra.getString("FIRSTNAME");
        customerLastName = extra.getString("LASTNAME");
        appContext = getApplicationContext();

        goToHomeActivity();

    }


    public void goToHomeActivity(){
        Intent myIntent = new Intent(getApplicationContext(), Events.class);

        Bundle extra = new Bundle();
        extra.putString("CODE", getCustomerCode());
        extra.putString("FIRSTNAME", getCustomerFirstName());
        extra.putString("LASTNAME", getCustomerLastName());
        myIntent.putExtras(extra);

        startActivity(myIntent);

        finish();
    }

    public String getCustomerCode(){
        return customerCode;
    }

    public String getCustomerFirstName(){
        return customerFirstName;
    }

    public String getCustomerLastName(){
        return customerLastName;
    }

    private Context getAppContext(){
        return appContext;
    }

}
