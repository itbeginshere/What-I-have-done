package com.example.dynstu;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class AdminSendClear extends AppCompatActivity{

    private String userCode;
    private String userFirstName;
    private String userLastName;

    private String[] employeeNamesForSpin;
    private String[] employeeCodesForSpinAdapter;

    private EditText edtDate;
    private EditText edtCapacity;
    
    private Spinner spinEmployeeNames;
    private Spinner spinTimeStart;
    private Spinner spinTimeEnd;
    
    private Button btnSend;
    private Button btnClear;

    private MySqlLiteOpenHelper myDbHelper;
    private SQLiteDatabase myDatabase;

    private String employeeDetailsTable = "employeeAccountDetails_tbl";
    private String classRequestTable = "privateClassRequest_tbl";
    private String paymentsDetailsTable = "paymentDetails_tbl";

    private Cursor c = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendclear);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        edtDate = findViewById(R.id.send_clr_date);
        edtCapacity = findViewById(R.id.send_clr_capacity);
        
        spinEmployeeNames = findViewById(R.id.send_clr_employee_names);
        spinTimeStart = findViewById(R.id.send_clr_time_start);
        spinTimeEnd = findViewById(R.id.send_clr_time_end);

        myDbHelper = new MySqlLiteOpenHelper(getApplicationContext());
        myDatabase = myDbHelper.getReadableDatabase();

       importInstructorNames(employeeDetailsTable, getAmountEmployee(employeeDetailsTable));

        ArrayAdapter<CharSequence> adapt_s_time = ArrayAdapter.createFromResource(getApplicationContext(), R.array.time_hours, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapt_e_time = ArrayAdapter.createFromResource(getApplicationContext(), R.array.time_hours, android.R.layout.simple_spinner_item);
        ArrayAdapter<String> adapt_emp_names = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, employeeNamesForSpin);

        adapt_s_time.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapt_e_time.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapt_emp_names.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinTimeStart.setAdapter(adapt_s_time);
        spinTimeEnd.setAdapter(adapt_e_time);
        spinEmployeeNames.setAdapter(adapt_emp_names);
        
        btnSend = findViewById(R.id.send_clr_btn_send);
        btnClear = findViewById(R.id.send_clr_btn_clear);
        
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSend();
            }
        });
        
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClear();
            }
        });

        Intent myIntent = getIntent();

        Bundle extras = myIntent.getExtras();

        userCode = extras.getString("CODE");
        userFirstName = extras.getString("FIRSTNAME");
        userLastName = extras.getString("LASTNAME");


        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Request Private Session");
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

    public void onSend(){

        SQLOperations oper = new SQLOperations(getApplicationContext(), "W");
        newCode gen = new newCode(getApplicationContext());

        myDbHelper = new MySqlLiteOpenHelper(getApplicationContext());
        myDatabase = myDbHelper.getWritableDatabase();

        if(checkForBlankInitial() == true){
            if(checkFieldsInitail() == true){
                if(checkDate(getDate()) == true){
                    if (checkForExistingDetails(paymentsDetailsTable) == false) {
                       ContentValues values_new = new ContentValues();
                       values_new.put("Request_ID", gen.generateID("R", classRequestTable));
                       values_new.put("Employee_ID", getEmployeeCode());
                       values_new.put("Capacity", Integer.parseInt(getCapacity()));
                       values_new.put("Start_Time", getTimeStart());
                       values_new.put("End_Time", getTimeEnd());
                       values_new.put("Date", getDate());
                       values_new.put("Guest_ID", getUserCode());
                       oper.insertData(classRequestTable, values_new);
                       Toast.makeText(getApplicationContext(), "Your private session request has been sent to " + getEmployeeName(), Toast.LENGTH_SHORT).show();
                    } else {
                       Toast.makeText(getApplicationContext(), "You do not have any payment method set up yet. Add a card to your account", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    edtDate.setError("The date you entered has already passed");
                }
            }
        } else {
        }

    }
    
    
    public void onClear(){

        edtCapacity.setText("");
        edtDate.setText("");

        spinTimeStart.setSelection(0);
        spinTimeEnd.setSelection(0);
        spinEmployeeNames.setSelection(0);
    }

    private boolean checkForBlankInitial(){
        if(getCapacity().equals("")){
            edtCapacity.setError("The Capacity cannot be left blank!");
            return false;
        } else if(getDate().equals("")){
            edtDate.setError("The Date cannot be left blank!");
            return false;
        } else if(getTimeStart().equals("")){
            TextView errorText = (TextView)spinTimeStart.getSelectedView();
            errorText.setError("Starting Time cannot be blank!");
            errorText.setTextColor(Color.RED);
            return false;
        } else if (getTimeEnd().equals("")){
            TextView errorText = (TextView)spinTimeEnd.getSelectedView();
            errorText.setError("Ending Time cannot be blank!");
            errorText.setTextColor(Color.RED);
        } else if (getEmployeeName().equals("")){
            TextView errorText = (TextView)spinEmployeeNames.getSelectedView();
            errorText.setError("Employee Names should not be blank!");
            errorText.setTextColor(Color.RED);
            errorText.setText("Error...");
            return false;
        }
        return true;
    }

    private boolean checkFieldsInitail(){

        if(validCapacity() == false){
            return false;
        } else if (validDate() == false){
            return false;
        } else if (validTime() == false){
            return false;
        }
        return true;
    }

    private boolean checkForExistingDetails(String tableName){

        c = myDatabase.query(tableName, null, null,null, null,null,null);

        if(c.moveToFirst()){
            do{
                if(c.getString(0).equals(getUserCode()) == true){
                    return false;
                }
            } while (c.moveToNext());
        }

        c.close();

        return true;
    }

    private boolean checkDate(String date){

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date currentDate = new Date();
        String strDate = formatter.format(currentDate);

        try {
            Date finalDate = new SimpleDateFormat("dd/MM/yyyy").parse(strDate);
            Date enteredDate = new SimpleDateFormat("dd/MM/yyyy").parse(date);

            if(finalDate.compareTo(enteredDate) <= 0){
                return true;
            } else {
                edtDate.setBackgroundColor(Color.RED);
                return false;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }


        return true;
    }

    private boolean isNumber(String potentailNumber){

        try{
            double  value = Double.parseDouble(potentailNumber);
        } catch(NumberFormatException er) {

            return false;
        }
        return true;
    }

    private boolean validCapacity(){

        if(isNumber(getCapacity())==false){
            edtCapacity.setError("The capacity must be a number!");
            return false;
        }

        if(Integer.parseInt(getCapacity()) < 1){
            edtCapacity.setError("The capacity value cannot be smaller than 1");
            return false;
        }

        return true;
    }

    private boolean validTime(){

        if(getTimeStartIndex() > getTimeEndIndex()){
            TextView errorText = (TextView)spinTimeStart.getSelectedView();
            errorText.setError("Start Time cannot commence after End Time");
            errorText.setTextColor(Color.RED);

            Toast.makeText(getApplicationContext(), "Start Time cannot commence after End Time", Toast.LENGTH_SHORT).show();
            return false;
        } else if(getTimeStartIndex() == getTimeEndIndex()){
            TextView errorText = (TextView)spinTimeStart.getSelectedView();
            errorText.setError("Start Time cannot commence as the same time as End Time");
            errorText.setTextColor(Color.RED);

            Toast.makeText(getApplicationContext(), "Start Time cannot commence as the same time as End Time", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean validDate(){
        String date = getDate();
        String tempStringValue = "";
        int pos = date.indexOf('/');
        int length = 0;
        int count = 0;
        while(pos != -1){
            pos = date.indexOf('/');
            count++;

            if(pos == -1){
                tempStringValue = date.substring(0, 4);
            } else {
                tempStringValue = date.substring(0, pos);
            }

            if(isNumber(tempStringValue) == false) {
                edtDate.setError("Invalid Date!");
                Toast.makeText(getApplicationContext(), "Invalid Date Format. Example DD/MM/YYYY D-Day M-Month Y-Year" + tempStringValue, Toast.LENGTH_LONG).show();
                return false;
            } else {
                if(Integer.parseInt(tempStringValue) < 1){
                    edtDate.setError("Invalid Date!");
                    Toast.makeText(getApplicationContext(), "Invalid Date Format. Values cannot be negative or zero", Toast.LENGTH_LONG).show();
                    return false;
                } else {
                    if((Integer.parseInt(tempStringValue) > 31) && (count == 1)){
                        edtDate.setError("Invalid Date!");
                        Toast.makeText(getApplicationContext(), "Invalid Date Format. Day value cannot be greater than 31", Toast.LENGTH_LONG).show();
                        return false;
                    } else if((Integer.parseInt(tempStringValue) > 12) && (count == 2)){
                        edtDate.setError("Invalid Date!");
                        Toast.makeText(getApplicationContext(), "Invalid Date Format. Month value cannot be greater than 12", Toast.LENGTH_LONG).show();
                        return false;
                    }  else if((Integer.parseInt(tempStringValue) < 2019) && (count == 3)){
                        edtDate.setError("Invalid Date!");
                        Toast.makeText(getApplicationContext(), "Invalid Date Format. Year value cannot be less than the current year, 2019", Toast.LENGTH_LONG).show();
                        return false;
                    }
                }
            }

            length = date.length();
            date = date.substring(pos+1, length);

            if(count > 3){
                edtDate.setError("Invalid Date!");
                Toast.makeText(getApplicationContext(), "Invalid Date Format. Example DD/MM/YYYY D-Day M-Month Y-Year", Toast.LENGTH_LONG).show();
                return false;
            }

        }

        if(count != 3){
            edtDate.setError("Invalid Date!");
            Toast.makeText(getApplicationContext(), "Invalid Date Format. Example DD/MM/YYYY D-Day M-Month Y-Year COUNT", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }




    private void importInstructorNames(String tableName, int size){

        int counter = 0;

        employeeNamesForSpin = new String[size];
        employeeCodesForSpinAdapter = new String[size];

        c = myDatabase.query(tableName, new String[] {"First_Name", "Last_Name", "Employee_ID"}, null, null, null, null, null);

        if(c.moveToFirst()){
            do{
                employeeNamesForSpin[counter] = c.getString(0) + " " + c.getString(1);
                employeeCodesForSpinAdapter[counter] = c.getString(2);
                counter++;
            } while (c.moveToNext());
        }

        c.close();

    }

    public String getCapacity(){
        return edtCapacity.getText().toString();
    }

    public String getDate(){
        return edtDate.getText().toString();
    }

    public String getEmployeeName(){
        return spinEmployeeNames.getSelectedItem().toString();
    }

    public String getEmployeeCode() {
        return employeeCodesForSpinAdapter[getEmployeeNameIndex()];
    }

    public int getEmployeeNameIndex(){
        return spinEmployeeNames.getSelectedItemPosition();
    }
    
    public String getTimeStart(){
        return spinTimeStart.getSelectedItem().toString();
    }

    public int getTimeStartIndex(){
        return spinTimeStart.getSelectedItemPosition();
    }

    public String getTimeEnd(){
        return spinTimeEnd.getSelectedItem().toString();
    }

    public int getTimeEndIndex(){
        return spinTimeEnd.getSelectedItemPosition();
    }
    
    private int getAmountEmployee(String tableName){
        int amount = 0;

        c = myDatabase.query(tableName, null, null, null, null, null,null);

        if(c.moveToFirst()){
            do{
                amount++;
            } while(c.moveToNext());
        }

        c.close();

        return amount;
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
