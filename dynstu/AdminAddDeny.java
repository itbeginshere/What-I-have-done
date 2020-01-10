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
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AdminAddDeny extends AppCompatActivity{

    private String userCode;
    private String userFirstName;
    private String userLastName;
    
    private String requestCode;

    private EditText edtVenue;
    private EditText edtPrice;
    
    private Spinner spinEquip;
    
    private Button btnAdd;
    private Button btnDeny;

    private MySqlLiteOpenHelper myDbHelper;
    private SQLiteDatabase myDatabase;

    private String classSessionsTable = "classSession_tbl";
    private String classRequestTable = "privateClassRequest_tbl";
    private String notifMessageTable = "notifMessage_tbl";
    private String classSessionsMembersTable = "classSessionMembers_tbl";
    
    private Cursor c = null;

    private String reqGuestID;
    private String reqDate;
    private String reqTimeStart;
    private String reqTimeEnd;
    private String reqCapacity;
    private String reqEmployeeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_deny);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        myDbHelper = new MySqlLiteOpenHelper(getApplicationContext());
        myDatabase = myDbHelper.getReadableDatabase();

        edtVenue = findViewById(R.id.add_deny_venue);
        edtPrice = findViewById(R.id.add_deny_price);
        
        spinEquip = findViewById(R.id.add_deny_equip);
        
        ArrayAdapter<CharSequence> adapt_type = ArrayAdapter.createFromResource(getApplicationContext(), R.array.equip_select, android.R.layout.simple_spinner_item);
        
        adapt_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
        spinEquip.setAdapter(adapt_type);
                
        btnAdd = findViewById(R.id.add_deny_btn_add);
        btnDeny = findViewById(R.id.add_deny_btn_deny);
        
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAdd(getRequestCode());
            }
        });
        
        btnDeny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeny(getRequestCode());
            }
        });

        Intent myIntent = getIntent();

        Bundle extras = myIntent.getExtras();

        userCode = extras.getString("CODE");
        userFirstName = extras.getString("FIRSTNAME");
        userLastName = extras.getString("LASTNAME");
        
        requestCode = extras.getString("REQUESTCODE");

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Add or Deny Request");
        mToolbar.setNavigationIcon(R.drawable.ic_arrow);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

               leave();
            }
        });

        setRequestedValues(classRequestTable, getRequestCode());

    }

    private void leave(){
        Intent i = new Intent(getApplicationContext(), AdminClass.class);

        Bundle extra = new Bundle();

        extra.putString("CODE", getUserCode());
        extra.putString("FIRSTNAME", getUserFirstName());
        extra.putString("LASTNAME", getUserLastName());

        i.putExtras(extra);

        startActivity(i);

        finish();
    }

    public void onAdd(String code){
        SQLOperations oper = new SQLOperations(getApplicationContext(), "W");
        newCode gen = new newCode(getApplicationContext());

        String ClassID = gen.generateID("C", classSessionsTable);
        String notifMsg;
        myDbHelper = new MySqlLiteOpenHelper(getApplicationContext());
        myDatabase = myDbHelper.getWritableDatabase();

        if (checkForBlankFinal() == true){
            if(checkFieldsFinal() == true){

                c = myDatabase.query(classSessionsTable, null, null,null,null,null,null);
                if(c.moveToFirst()){
                    if (checkForClash() == true) {
                        ContentValues values_new = new ContentValues();
                        ContentValues values_new_reserve = new ContentValues();

                        values_new.put("Class_ID", ClassID);
                        values_new.put("Employee_ID", getEmployeeID());
                        values_new.put("Capacity", Integer.parseInt(getCapacity()));
                        values_new.put("Type",getType());
                        values_new.put("Date",getDate());
                        values_new.put("Time_Start",getTimeStart());
                        values_new.put("Time_End",getTimeEnd());
                        values_new.put("Venue",getVenue());
                        values_new.put("Private", getPrivate());
                        values_new.put("Price", Double.parseDouble(getPrice()));
                        values_new.put("Equipment", getEquipment());
                        values_new.put("Complete", 0);
                        values_new.put("Cancelled", 0);
                        oper.insertData(classSessionsTable, values_new);

                        values_new_reserve.put("Class_ID", ClassID);
                        values_new_reserve.put("Guest_ID", getGuestID());
                        oper.insertData(classSessionsMembersTable, values_new_reserve);

                        oper.deleteData(classRequestTable, "Request_ID = ?", new String[] {code});

                        notifMsg = "Your request has been accepted for " + getType() + " at " + getTimeStart() + " - " + getTimeEnd() + " on the " + getDate() + " at " + getVenue() + ". Bank charge: " + getPrice();
                        insertNotifData(notifMessageTable, notifMsg);
                        Toast.makeText(getApplicationContext(), "You have accepted the request.", Toast.LENGTH_SHORT).show();
                        leave();
                    } else {

                        oper.deleteData(classRequestTable, "Request_ID = ?", new String[] {code});

                        notifMsg = "Your request clashes with another class. You request has been denied";
                        insertNotifData(notifMessageTable, notifMsg);
                        Toast.makeText(getApplicationContext(), "The request was rejected due to a time clash.", Toast.LENGTH_SHORT).show();
                        leave();
                    }
                } else {
                    ContentValues values_new = new ContentValues();
                    ContentValues values_new_reserve = new ContentValues();

                    values_new.put("Class_ID", ClassID);
                    values_new.put("Employee_ID", getEmployeeID());
                    values_new.put("Capacity", Integer.parseInt(getCapacity()));
                    values_new.put("Type",getType());
                    values_new.put("Date",getDate());
                    values_new.put("Time_Start",getTimeStart());
                    values_new.put("Time_End",getTimeEnd());
                    values_new.put("Venue",getVenue());
                    values_new.put("Private", getPrivate());
                    values_new.put("Price", Double.parseDouble(getPrice()));
                    values_new.put("Equipment", getEquipment());
                    values_new.put("Complete", 0);
                    values_new.put("Cancelled", 0);
                    oper.insertData(classSessionsTable, values_new);

                    values_new_reserve.put("Class_ID", ClassID);
                    values_new_reserve.put("Guest_ID", getGuestID());
                    oper.insertData(classSessionsMembersTable, values_new_reserve);

                    oper.deleteData(classRequestTable, "Request_ID = ?", new String[] {code});

                    notifMsg = "Your request has been accepted for: " + getType() + " at " + getTimeStart() + " - " + getTimeEnd() + " on the " + getDate() + " at " + getVenue() + ". Bank charge: " + getPrice();
                    insertNotifData(notifMessageTable, notifMsg);
                    Toast.makeText(getApplicationContext(), "You have accepted the request", Toast.LENGTH_SHORT).show();
                    leave();
                }
            }
        } else {

        }
    }

    public void onDeny(String code){

        SQLOperations oper = new SQLOperations(getApplicationContext(), "W");

        myDbHelper = new MySqlLiteOpenHelper(getApplicationContext());
        myDatabase = myDbHelper.getWritableDatabase();

        String notifMsg = "Your request for a private session was rejected. Kindly request again if you are still wanting a private class";

        insertNotifData(notifMessageTable, notifMsg);
        oper.deleteData(classRequestTable, "Request_ID = ?", new String[] {code});
        Toast.makeText(getApplicationContext(), "You have denied the request.", Toast.LENGTH_SHORT).show();
        leave();
    }


    private boolean checkForBlankFinal(){

        if (getVenue().equals("")){
            edtVenue.setError("Venue cannot be blank!");
            return false;
        } else if (getPrice().equals("")){
            edtPrice.setError("Price cannot be blank!");
            return false;
        } else if (getEquipment().equals("")){
            TextView errorText = (TextView)spinEquip.getSelectedView();
            errorText.setError("Equipment cannot be blank!");
            errorText.setTextColor(Color.RED);
            return false;
        }
        return true;
    }

    private boolean checkFieldsFinal(){
        if (validPrice() == false){
            return false;
        }
        return true;
    }


    private double convertTimeToDecimal(String timeValue){

        String tempStringValue = "";
        double decimalValue = 0;

        int pos = timeValue.indexOf(':');

        tempStringValue = timeValue.substring(0, pos);
        decimalValue = decimalValue + Double.parseDouble(tempStringValue);

        tempStringValue = timeValue.substring(pos+1);
        decimalValue = (decimalValue + (Double.parseDouble(tempStringValue) / 100));


        return decimalValue;
    }

    private boolean isInBetween(double checkValue, double beginValue, double endValue, String checkType){

        boolean valid = false;
        if(checkType.equals("Start")){
            if(checkValue >= endValue){
                valid = true;
            } else if(checkValue < beginValue){
                valid = true;
            } else if (checkValue >= beginValue && checkValue < endValue) {
                valid = false;
            } else{
                valid = false;
            }
        } else {
            if(checkValue < beginValue){
                valid = true;
            } else if(checkValue > endValue){
                valid = true;
            } else if (checkValue >= beginValue && checkValue < endValue) {
                valid = false;
            } else{
                valid = false;
            }
        }
        return valid;
    }


    private boolean checkForClash(){

        c = myDatabase.query(classSessionsTable, null, null,null,null,null,null);

        c.moveToFirst();

        double row_start_time = convertTimeToDecimal(c.getString(5));
        double row_end_time = convertTimeToDecimal(c.getString(6));

        double class_start_time = convertTimeToDecimal(getTimeStart());
        double class_end_time = convertTimeToDecimal(getTimeEnd());

        do{
            row_start_time = convertTimeToDecimal(c.getString(5));
            row_end_time = convertTimeToDecimal(c.getString(6));

                if(getDate().equals(c.getString(4))){
                    if(getVenue().equals(c.getString(7))){

                        if(isInBetween(class_start_time, row_start_time, row_end_time, "Start") == false || isInBetween(class_end_time, row_start_time, row_end_time, "End") == false){
                            Toast.makeText(getApplicationContext(), "The times you set falls between another class session with the time of\nStart:" + c.getString(5) + "\nEnd :" + c.getString(6), Toast.LENGTH_LONG).show();
                            insertNotifData(notifMessageTable, "Your private class request clashes with another already set class. Kindly request the class session for a different date or time");
                            return false;

                        }
                    }
                }

        }while (c.moveToNext());

        return true;
    }



    private boolean validPrice(){
        if(isNumber(getPrice()) == false){
            edtPrice.setError("The price value is not a number!");
            return false;
        } else {
            if(Double.parseDouble(getPrice())<=0){
                edtPrice.setError("The lesson cannot be for free!");
                return false;
            }
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

    private void insertNotifData(String tableName, String message) {

        SQLOperations oper = new SQLOperations(getApplicationContext(), "W");
        newCode gen = new newCode(getApplicationContext());

        ContentValues values_new = new ContentValues();
        values_new.put("Notif_ID", gen.generateID("N", tableName));
        values_new.put("Guest_ID", getGuestID());
        values_new.put("Message", message);
        values_new.put("READ", 0);

        oper.insertData(tableName, values_new);

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

    private String getRequestCode(){
        return requestCode;
    }

 

    public String getVenue(){
        return edtVenue.getText().toString();
    }

    public String getPrice(){
        return edtPrice.getText().toString();
    }
    
    public String getEquipment(){
        return spinEquip.getSelectedItem().toString();
    }

    public String getType(){
        return "Private Session";
    }

    public String getPrivate(){
        return "1";
    }

    private void setRequestedValues(String tableName, String code){
        c = myDatabase.query(tableName, null,"Request_ID = ?",new String[] {code},null,null,null);

        if(c.moveToFirst()){
            setEmployeeID(c.getString(1));
            setCapacity(c.getString(2));
            setTimeStart(c.getString(3));
            setTimeEnd(c.getString(4));
            setDate(c.getString(5));
            setGuestID(c.getString(6));
        }

        c.close();
    }

    private void setEmployeeID(String value){
        reqEmployeeID = value;
    }

    private void setCapacity(String value){
        reqCapacity = value;
    }

    private void setTimeStart(String value){
        reqTimeStart = value;
    }

    private void setTimeEnd(String value){
        reqTimeEnd = value;
    }

    private void setDate(String value){
        reqDate = value;
    }

    private void setGuestID(String value){
        reqGuestID = value;
    }

    private String getEmployeeID(){
        return reqEmployeeID;
    }

    private String getCapacity(){
        return reqCapacity;
    }

    private String getTimeStart(){
        return reqTimeStart;
    }

    private String getTimeEnd(){
        return reqTimeEnd;
    }

    private String getDate() {
        return reqDate;
    }

    private String getGuestID(){
        return reqGuestID;
    }

}
