package com.example.dynstu;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.strictmode.SqliteObjectLeakedViolation;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
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

public class ClassDetails extends AppCompatActivity{

    private MySqlLiteOpenHelper myDbHelper;
    private SQLiteDatabase myDatabase;

    private String userCode;
    private String userFirstName;
    private String userLastName;

    private String classCode;

    private TextView edtUserCode;
    private TextView edtUserName;
    
    private EditText edtCapacity;
    private EditText edtDate;
    private EditText edtVenue;
    private EditText edtPrice;

    private Spinner spinType;
    private Spinner spinEndTime;
    private Spinner spinStartTime;
    private Spinner spinEquip;

    private Button btnAdd;
    private Button btnUpdate;
    private Button btnDelete;
    private Button btnGet;

    private String classSessionTable = "classSession_tbl";
    private String classSessionMembersTable = "classSessionMembers_tbl";
    private String promoCardsTable = "promoCards_tbl";
    private String notifMessageTable = "notifMessage_tbl";

    private Cursor c = null;
    private Cursor d = null;
    private Cursor e = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_details);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        edtUserCode = findViewById(R.id.cls_dets_emp_code);
        edtUserName = findViewById(R.id.cls_dets_emp_name);
        
        edtCapacity = findViewById(R.id.cls_dets_capacity);
        edtDate = findViewById(R.id.cls_dets_date);
        edtVenue = findViewById(R.id.cls_dets_venue);
        edtPrice = findViewById(R.id.cls_dets_price);

        spinType = findViewById(R.id.cls_dets_type);
        spinStartTime = findViewById(R.id.cls_dets_start_time);
        spinEndTime = findViewById(R.id.cls_dets_end_time);
        spinEquip = findViewById(R.id.cls_dets_equip);

        ArrayAdapter<CharSequence> adapt_type = ArrayAdapter.createFromResource(getApplicationContext(), R.array.class_types, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapt_time_s = ArrayAdapter.createFromResource(getApplicationContext(), R.array.time_hours, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapt_time_e = ArrayAdapter.createFromResource(getApplicationContext(), R.array.time_hours, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapt_equip = ArrayAdapter.createFromResource(getApplicationContext(), R.array.equip_select, android.R.layout.simple_spinner_item);

        adapt_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapt_time_e.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapt_time_s.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapt_equip.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinType.setAdapter(adapt_type);
        spinStartTime.setAdapter(adapt_time_s);
        spinEndTime.setAdapter(adapt_time_e);
        spinEquip.setAdapter(adapt_equip);

        btnAdd = findViewById(R.id.cls_dets_btn_add);
        btnUpdate = findViewById(R.id.cls_dets_btn_update);
        btnDelete = findViewById(R.id.cls_dets_btn_delete);
        btnGet = findViewById(R.id.cls_dets_btn_get);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClassAdd();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onClassUpdate();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClassDelete();
            }
        });

        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClassGet();
            }
        });

        Intent myIntent = getIntent();

        Bundle extras = myIntent.getExtras();

        userCode = extras.getString("CODE");
        userFirstName = extras.getString("FIRSTNAME");
        userLastName = extras.getString("LASTNAME");

        classCode = extras.getString("CLASSID"); // Default = class-add
        setExtras(getUserCode(), getUserFirstName(), getUserLastName());
        setFields(getClassCode());

        if(getClassCode().equals("class-add") == true) {
            setTitle("Adding a class");
        } else {
            setTitle("Class Controls");
        }

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Class Sessions");
        mToolbar.setNavigationIcon(R.drawable.ic_arrow);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

               leave();
            }
        });


        onClassGet();

    }

    private void leave(){

        Intent i = new Intent(getApplicationContext(), AdminSchedule.class);

        Bundle extra = new Bundle();

        extra.putString("CODE", getUserCode());
        extra.putString("FIRSTNAME", getUserFirstName());
        extra.putString("LASTNAME", getUserLastName());

        i.putExtras(extra);

        startActivity(i);
        finish();
    }


    public void onClassAdd(){
        boolean privateSelect = false;

        myDbHelper = new MySqlLiteOpenHelper(getApplicationContext());
        myDatabase = myDbHelper.getReadableDatabase();
        newCode gen = new newCode(getApplicationContext());
        SQLOperations oper = new SQLOperations(getApplicationContext(), "W");

        if(checkClassCode(getClassCode()) == false){
            if(checkForBlank()==true){
                if(checkFields()==true){

                    d = myDatabase.query(classSessionTable, null, null,null,null,null,null);
                    if(d.moveToFirst()){
                        if (checkForClash()== true) {

                            if (getPrivate().equals("Yes")) {
                                privateSelect = true;
                            } else {
                                privateSelect = false;
                            }

                            ContentValues values_new = new ContentValues();
                            values_new.put("Class_ID", gen.generateID("C", classSessionTable));
                            values_new.put("Employee_ID", getUserCode());
                            values_new.put("Capacity", Integer.parseInt(getCapacity()));
                            values_new.put("Type", getType());
                            values_new.put("Date", getDate());
                            values_new.put("Time_Start", getTimeStart());
                            values_new.put("Time_End", getTimeEnd());
                            values_new.put("Venue", getVenue());
                            values_new.put("Private", privateSelect);
                            values_new.put("Price", Double.parseDouble(getPrice()));
                            values_new.put("Equipment", getEquipment());
                            values_new.put("Complete", 0);
                            values_new.put("Cancelled", 0);
                            oper.insertData(classSessionTable, values_new);
                            Toast.makeText(getApplicationContext(), "Your class is now set up for customers to see.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (getPrivate().equals("Yes")) {
                            privateSelect = true;
                        } else {
                            privateSelect = false;
                        }

                        ContentValues values_new = new ContentValues();
                        values_new.put("Class_ID", gen.generateID("C", classSessionTable));
                        values_new.put("Employee_ID", getUserCode());
                        values_new.put("Capacity", Integer.parseInt(getCapacity()));
                        values_new.put("Type", getType());
                        values_new.put("Date", getDate());
                        values_new.put("Time_Start", getTimeStart());
                        values_new.put("Time_End", getTimeEnd());
                        values_new.put("Venue", getVenue());
                        values_new.put("Private", privateSelect);
                        values_new.put("Price", Double.parseDouble(getPrice()));
                        values_new.put("Equipment", getEquipment());
                        values_new.put("Complete", 0);
                        values_new.put("Cancelled", 0);
                        oper.insertData(classSessionTable, values_new);
                        Toast.makeText(getApplicationContext(), "Your class is now set up for customers to see.", Toast.LENGTH_SHORT).show();
                    }
                    d.close();
                }
            } else {
            }
        } else {
           // Toast.makeText(getApplicationContext(), "THIS CLASS ALREADY EXISTS", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClassUpdate(){

        boolean privateSelect = false;
        myDbHelper = new MySqlLiteOpenHelper(getApplicationContext());
        myDatabase = myDbHelper.getWritableDatabase();

        SQLOperations oper = new SQLOperations(getApplicationContext(), "W");
        if(checkClassCode(getClassCode()) == true){
            if(checkForBlank()==true){
                if(checkFields()==true){
                    if (checkForClash()== true) {
                        if(getPrivate().equals("Yes")){
                            privateSelect = true;
                        } else {
                            privateSelect = false;
                        }

                        ContentValues values_new = new ContentValues();
                        values_new.put("Class_ID", getClassCode());
                        values_new.put("Employee_ID", getUserCode());
                        values_new.put("Capacity", Integer.parseInt(getCapacity()));
                        values_new.put("Type",getType());
                        values_new.put("Date",getDate());
                        values_new.put("Time_Start",getTimeStart());
                        values_new.put("Time_End",getTimeEnd());
                        values_new.put("Venue",getVenue());
                        values_new.put("Private", privateSelect);
                        values_new.put("Price", Double.parseDouble(getPrice()));
                        values_new.put("Equipment", getEquipment());
                        oper.updateData(classSessionTable, values_new, "Class_ID = ? ", new String[] {getClassCode()});
                        changedBroadcast(classSessionMembersTable, getClassCode());
                        Toast.makeText(getApplicationContext(), "Your class has been changed", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
             }
        } else {
            Toast.makeText(getApplicationContext(), "YOU CAN ONLY CREATE CLASSES AT THIS SCREEN.", Toast.LENGTH_SHORT).show();
        }

    }

    public void onClassDelete(){
        myDbHelper = new MySqlLiteOpenHelper(getApplicationContext());
        myDatabase = myDbHelper.getWritableDatabase();
        if(checkClassCode(getClassCode()) == true) {

            final SQLOperations oper = new SQLOperations(getApplicationContext(), "W");

            final AlertDialog.Builder confirm = new AlertDialog.Builder(this);
            confirm.setMessage("Are you sure you want to cancel the class session of " + getClassCode() + "?");
            confirm.setCancelable(false);

            confirm.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ContentValues values_new = new ContentValues();
                    values_new.put("Cancelled", 1);
                    cancelBroadcast(classSessionMembersTable, getClassCode());
                    oper.updateData(classSessionTable, values_new, "Class_ID = ?", new String[]{getClassCode()});
                    Toast.makeText(getApplicationContext(), "Your class has been cancelled", Toast.LENGTH_SHORT).show();
                    leave();
                }
            });

            confirm.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            confirm.create().show();
        } else {
            Toast.makeText(getApplicationContext(), "YOU CAN ONLY CREATE CLASSES AT THIS SCREEN", Toast.LENGTH_SHORT).show();
        }

    }


    public void onClassGet(){
        myDbHelper = new MySqlLiteOpenHelper(getApplicationContext());
        myDatabase = myDbHelper.getWritableDatabase();

        c = myDatabase.query(classSessionTable, null, "Class_ID = ?",new String[] {getClassCode()},null,null,null);
        if(getClassCode().equals("class-add")){
            Toast.makeText(getApplicationContext(), "YOU CAN ONLY CREATE CLASSES AT THIS SCREEN", Toast.LENGTH_SHORT).show();
        } else {
            if(c.moveToFirst()){

                setCapacity(c.getString(2));
                setType(c.getString(3));
                setDate(c.getString(4));
                setTimeStart(c.getString(5));
                setTimeEnd(c.getString(6));
                setVenue(c.getString(7));
                setPrice(c.getString(9));
                setEquipment(c.getString(10));

            }
        }


        c.close();
    }

    private String getUserTag(String userCode){
        String userTag = "";

        userTag = userCode.substring(0,1);

        return userTag;
    }

    private boolean checkForBlank(){
        if(getCapacity().equals("")){
            edtCapacity.setError("The Capacity cannot be left blank!");
            return false;
        } else if(getType().equals("")){
            TextView errorText = (TextView)spinType.getSelectedView();
            errorText.setError("Type cannot be blank!");
            errorText.setTextColor(Color.RED);
            return false;
        } else if(getDate().equals("")){
            edtDate.setError("The Date cannot be left blank!");
            return false;
        } else if(getTimeStart().equals("")){
            TextView errorText = (TextView)spinStartTime.getSelectedView();
            errorText.setError("Starting Time cannot be blank!");
            errorText.setTextColor(Color.RED);
            return false;
        } else if (getTimeEnd().equals("")){
            TextView errorText = (TextView)spinEndTime.getSelectedView();
            errorText.setError("Ending time cannot be blank!");
            errorText.setTextColor(Color.RED);
            return false;
        } else if (getVenue().equals("")){
            edtVenue.setError("The Venue cannot be left blank!");
            return false;
        } else if (getPrice().equals("")){
            edtPrice.setError("The Price cannot be left blank!");
            return false;
        } else if (getEquipment().equals("")){
            TextView errorText = (TextView)spinEquip.getSelectedView();
            errorText.setError("Equipment cannot be blank!");
            errorText.setTextColor(Color.RED);
            return false;
        }

        return true;
    }

    private boolean checkFields(){

        if(validCapacity() == false){
            return false;
        } else if (validDate() == false){
            return false;
        } else if (validTime() == false){
            return false;
        } else if (validPrice() == false){
            return false;
        }
        return true;
    }

    private boolean checkClassCode(String code){
        if (code.equals("class-add") == true){
            return false;
        } else {
            return true;
        }
    }

    private boolean checkForClash(){

        c = myDatabase.query(classSessionTable, null, null,null,null,null,null);

        c.moveToFirst();

        double row_start_time = convertTimeToDecimal(c.getString(5));
        double row_end_time = convertTimeToDecimal(c.getString(6));

        double class_start_time = convertTimeToDecimal(getTimeStart());
        double class_end_time = convertTimeToDecimal(getTimeEnd());

        do{
            row_start_time = convertTimeToDecimal(c.getString(5));
            row_end_time = convertTimeToDecimal(c.getString(6));

            if(getClassCode().equals(c.getString(0)) == false){
                if(getDate().equals(c.getString(4))){
                    if(getVenue().equals(c.getString(7))){

                        if(isInBetween(class_start_time, row_start_time, row_end_time, "Start") == false || isInBetween(class_end_time, row_start_time, row_end_time, "End") == false){
                            Toast.makeText(getApplicationContext(), "The times you set falls between another class session with the time of \nStart:" + c.getString(5) + "\nEnd :" + c.getString(6), Toast.LENGTH_LONG).show();

                            TextView errorText = (TextView)spinStartTime.getSelectedView();
                            errorText.setError("Type cannot be blank!");
                            errorText.setTextColor(Color.YELLOW);

                            TextView errorText2 = (TextView)spinEndTime.getSelectedView();
                            errorText2.setError("Type cannot be blank!");
                            errorText2.setTextColor(Color.YELLOW);

                            return false;
                        }
                    }
                }
            }
        }while (c.moveToNext());

        c.close();
        return true;
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

    private boolean isNumber(String potentailNumber){
        try{
            double  value = Double.parseDouble(potentailNumber);
        } catch(NumberFormatException er) {

            return false;
        }
        return true;
    }

    private void cancelBroadcast(String tableName, String classCode){
        //notify all customers within the classMemberSessionTable
        //tableName is classMemberTable
        String currentGuestID = "";
        String notifMsg = "";

        e = myDatabase.query(tableName, null, "Class_ID = ?", new String[] {classCode}, null,null,null);

        if(e.moveToFirst()){
            do{

                currentGuestID = e.getString(1);

                //DO NOT FORGET TO REMOVE EACH CUSTOMER
                if(checkIfPrivateClass(classSessionTable, classCode) == true){
                    // it is a private session.
                    notifMsg = "The class with " + getUserFirstName() + " " + getUserLastName() + " on the " + getDate() + " at " + getTimeStart() + "-" + getTimeEnd() + " for " + getType() + " is cancelled";
                    insertNotifData(notifMessageTable, notifMsg, currentGuestID);

                } else {
                    //it is not a private session
                    if(checkForPromoCode(promoCardsTable, currentGuestID) == true){
                        //if they have a promo card
                        //messgae that the class has been cancelled
                        notifMsg = "The class with " + getUserFirstName() + " " + getUserLastName() + " on the " + getDate() + " at " + getTimeStart() + "-" + getTimeEnd() + " for " + getType() + " is cancelled";
                        insertNotifData(notifMessageTable, notifMsg, currentGuestID);

                        //message that the customer has been credited
                        updatePromoCard(promoCardsTable, currentGuestID);
                        notifMsg = "You have been credited with another session on your Promotional Card";
                        insertNotifData(notifMessageTable, notifMsg, currentGuestID);
                    } else {
                        //if they do not have a promo card
                        //messgae that the class has been cancelled
                        notifMsg = "The class with " + getUserFirstName() + " " + getUserLastName() + " on the " + getDate() + " at " + getTimeStart() + "-" + getTimeEnd() + " for " + getType() + " is cancelled";
                        insertNotifData(notifMessageTable, notifMsg, currentGuestID);
                    }

                }

                removeUserFromBookedClass(classSessionMembersTable, currentGuestID);

            } while(e.moveToNext());
        }
        e.close();
    }

    private void removeUserFromBookedClass(String tableName, String guestID){
        SQLOperations oper = new SQLOperations(getApplicationContext(), "W");

        oper.deleteData(tableName, "Guest_ID = ?", new String[] {guestID});

    }

    private boolean checkIfPrivateClass(String tableName, String classCode) {

        c = myDatabase.query(tableName, null, "Class_ID = ?", new String[]{classCode}, null, null, null);

        c.moveToFirst();
        if (c.getString(8).equals("1")) {
            c.close();
            return true;
        } else {
            c.close();
            return false;
        }
    }

    private boolean checkForPromoCode(String tableName, String userCode) {

        c = myDatabase.query(tableName, null, "Guest_ID = ?", new String[]{userCode}, null, null, null);

        if(c.moveToFirst()){
            return true;
        } else {
            return false;
        }

    }

    private void changedBroadcast(String tableName, String classCode){
        //notify all customers within the classMemberSessionTable

        String currentGuestID = "";
        String notifMsg = "";

        c = myDatabase.query(tableName,null,"Class_ID = ?",new String[] {classCode},null,null,null);

        if(c.moveToFirst()){
            do{
                currentGuestID = c.getString(1);
                //messgae that the class has been cancelled
                notifMsg = "The class with " + getUserFirstName() + " " + getUserLastName() + "  has been changed. Please check your booked classes to view these changes.";
                insertNotifData(notifMessageTable, notifMsg, currentGuestID);

            } while(c.moveToNext());
        } else {
            Toast.makeText(getApplicationContext(), "There is no such class that belongs to the following code: " + classCode, Toast.LENGTH_LONG).show();
        }

        c.close();
    }


    private void insertNotifData(String tableName, String message, String guestID) {

        SQLOperations oper = new SQLOperations(getApplicationContext(), "W");
        newCode gen = new newCode(getApplicationContext());

        ContentValues values_new = new ContentValues();
        values_new.put("Notif_ID", gen.generateID("N", tableName));
        values_new.put("Guest_ID", guestID);
        values_new.put("Message", message);
        values_new.put("READ", 0);

        oper.insertData(tableName, values_new);

    }

    private void updatePromoCard(String tableName, String userName){
        int oldBalance = 0;
        int newBalance = 0;

        SQLOperations oper = new SQLOperations(getApplicationContext(), "W");

        c = myDatabase.query(tableName, null, "Guest_ID = ?", new String[] {userName}, null, null, null);

        c.moveToFirst();

        oldBalance = Integer.parseInt(c.getString(4));
        newBalance = oldBalance + 1;

        c.close();

        ContentValues values_new = new ContentValues();

        values_new.put("Session_Balance", newBalance);

        oper.updateData(tableName, values_new, "Guest_ID = ?", new String[] {userName});

    }


    private boolean validCapacity(){
        if(isNumber(getCapacity())==false){
            edtCapacity.setError("The Capacity must be a number!");
            return false;
        }

        if(Integer.parseInt(getCapacity()) < 1){
            edtCapacity.setError("The Capacity value cannot be smaller than 1!");
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
                Toast.makeText(getApplicationContext(), "Invalid Date Format. Example DD/MM/YYYY D-Day M-Month Y-Year" + tempStringValue, Toast.LENGTH_SHORT).show();
                return false;
            } else {
                if(Integer.parseInt(tempStringValue) < 1){
                    edtDate.setError("Invalid Date!");
                    Toast.makeText(getApplicationContext(), "Invalid Date Format. Values cannot be negative or zero", Toast.LENGTH_SHORT).show();
                    return false;
                } else {
                    if((Integer.parseInt(tempStringValue) > 31) && (count == 1)){
                        edtDate.setError("Invalid Date!");
                        Toast.makeText(getApplicationContext(), "Invalid Date Format. Day value cannot be greater than 31", Toast.LENGTH_SHORT).show();
                        return false;
                    } else if((Integer.parseInt(tempStringValue) > 12) && (count == 2)){
                        edtDate.setError("Invalid Date!");
                        Toast.makeText(getApplicationContext(), "Invalid Date Format. Month value cannot be greater than 12", Toast.LENGTH_SHORT).show();
                        return false;
                    }  else if((Integer.parseInt(tempStringValue) < 2019) && (count == 3)){
                        edtDate.setError("Invalid Date!");
                        Toast.makeText(getApplicationContext(), "Invalid Date Format. Year value cannot be less than the current year, 2019", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
            }

            length = date.length();
            date = date.substring(pos+1, length);

            if(count > 3){
                edtDate.setError("Invalid Date!");
                Toast.makeText(getApplicationContext(), "Invalid Date Format. Example DD/MM/YYYY D-Day M-Month Y-Year", Toast.LENGTH_SHORT).show();
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


    private boolean validTime(){

        if(getTimeStartIndex() > getTimeEndIndex()){
            TextView errorText = (TextView)spinStartTime.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            Toast.makeText(getApplicationContext(), "Start Time cannot commence after End Time", Toast.LENGTH_SHORT).show();
            return false;
        } else if(getTimeStartIndex() == getTimeEndIndex()){
            TextView errorText = (TextView)spinStartTime.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            Toast.makeText(getApplicationContext(), "Start Time cannot commence as the same time as End Time", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean validPrice(){
        if(isNumber(getPrice()) == false){
            edtPrice.setError("The Price must be a number!");
            return false;
        } else {
            if(Double.parseDouble(getPrice())<=0){
                edtPrice.setError("The Class cannot be for free!");
                return false;
            }
        }
        return true;
    }

    public String getCapacity(){
        return edtCapacity.getText().toString();
    }

    public String getDate(){
        return edtDate.getText().toString();
    }

    public String getVenue(){
        return edtVenue.getText().toString();
    }

    public String getPrice(){
        return edtPrice.getText().toString();
    }

    public String getType(){
        return spinType.getSelectedItem().toString();
    }

    public String getTimeStart(){
        return spinStartTime.getSelectedItem().toString();
    }

    public String getTimeEnd(){
        return spinEndTime.getSelectedItem().toString();
    }

    public int getTimeStartIndex(){
        return spinStartTime.getSelectedItemPosition();
    }

    public int getTimeEndIndex(){
        return spinEndTime.getSelectedItemPosition();
    }

    public String getPrivate(){
        return "No";
    }

    public String getEquipment(){
        return spinEquip.getSelectedItem().toString();
    }

    public void setCapacity(String value){
        edtCapacity.setText(value);
    }

    public void setDate(String value){
        edtDate.setText(value);
    }

    public void setVenue(String value){
        edtVenue.setText(value);
    }

    public void setPrice(String value){
        edtPrice.setText(value);
    }

    public void setType(String value){
        for (int k = 0; k < spinType.getCount(); k++){
            if(spinType.getItemAtPosition(k).equals(value)){
                spinType.setSelection(k);
            }
        }
    }

    public void setTimeStart(String value){
        for (int k = 0; k < spinStartTime.getCount(); k++){
            if(spinStartTime.getItemAtPosition(k).equals(value)){
                spinStartTime.setSelection(k);
            }
        }
    }

    public void setTimeEnd(String value){
        for (int k = 0; k < spinEndTime.getCount(); k++){
            if(spinEndTime.getItemAtPosition(k).equals(value)){
                spinEndTime.setSelection(k);
            }
        }
    }

    public void setEquipment(String value){
        for (int k = 0; k < spinEquip.getCount(); k++){
            if(spinEquip.getItemAtPosition(k).equals(value)){
                spinEquip.setSelection(k);
            }
        }
    }


    private void setExtras(String code, String firstName, String lastName){
        edtUserCode.setText(code);
        edtUserName.setText(firstName + " " + lastName);
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

    private void setFields(String code){
        if(checkClassCode(code) == false){
            fillFields(code);
        } else {
        }

    }

    private void fillFields(String code){
        myDbHelper = new MySqlLiteOpenHelper(getApplicationContext());
        myDatabase = myDbHelper.getReadableDatabase();

        c = myDatabase.query(classSessionTable, new String[] {"Class_ID"},"Class_ID = ?",new String[] {getClassCode()},null,null,null);

        if(c.moveToFirst()){

            setCapacity(c.getString(2));
            setType(c.getString(3));
            setDate(c.getString(4));
            setTimeStart(c.getString(5));
            setTimeEnd(c.getString(6));
            setVenue(c.getString(7));
            setPrice(c.getString(9));
            setEquipment(c.getString(10));

        }

        c.close();
    }

    private String getClassCode(){
        return classCode;
    }


}
