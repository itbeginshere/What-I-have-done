package com.example.dynstu;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AdminClassMember extends AppCompatActivity{

    private String userCode;
    private String userFirstName;
    private String userLastName;

    private String ClassID;

    private TextView txtClassID;
    private TextView txtType;
    private TextView txtTime;

    private ListView listPresnt;
    private ListView listAbsent;

    private Button btnClear;
    private Button btnComplete;

    private ArrayList presentNames = new ArrayList();
    private ArrayList absentNames = new ArrayList();
    private ArrayList presentCodes = new ArrayList();
    private ArrayList absentCodes = new ArrayList();

    private MySqlLiteOpenHelper myDbHelper;
    private SQLiteDatabase myDatabase;

    private String classTable = "classSession_tbl";
    private String classMemberTable = "classSessionMembers_tbl";
    private String userTable = "userAccountDetails_tbl";
    private String attendanceTable = "attendance_tbl";

    private Cursor c = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_class_members);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        myDbHelper = new MySqlLiteOpenHelper(getApplicationContext());
        myDatabase = myDbHelper.getReadableDatabase();

        txtClassID = findViewById(R.id.attend_txt_class_id);
        txtType = findViewById(R.id.attend_txt_class_type);
        txtTime = findViewById(R.id.attend_txt_start_and_end);

        btnClear = findViewById(R.id.attend_btn_clear);
        btnComplete = findViewById(R.id.attend_btn_complete);

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClear();
            }
        });

        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onComplete();
            }
        });

        listAbsent = findViewById(R.id.attend_list_absent);
        listPresnt = findViewById(R.id.attend_list_present);

        Intent myIntent = getIntent();

        Bundle extras = myIntent.getExtras();

        userCode = extras.getString("CODE");
        userFirstName = extras.getString("FIRSTNAME");
        userLastName = extras.getString("LASTNAME");

        ClassID = extras.getString("CLASSID");

        retrieveCustomers();

        listPresnt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedName = presentNames.get(position).toString();
                String assocCode = presentCodes.get(position).toString();

                presentNames.remove(position);
                presentCodes.remove(position);

                absentNames.add(selectedName);
                absentCodes.add(assocCode);

                updateLists();
            }
        });

        listAbsent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedName = absentNames.get(position).toString();
                String assocCode = absentCodes.get(position).toString();

                absentNames.remove(position);
                absentCodes.remove(position);

                presentNames.add(selectedName);
                presentCodes.add(assocCode);

                updateLists();
            }
        });

        retrieveClassData(classTable, getSetClassID());

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Attendance Register");
        mToolbar.setNavigationIcon(R.drawable.ic_arrow);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                leave();
            }
        });

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

    public void onClear(){
        presentNames.clear();
        presentCodes.clear();
        absentNames.clear();
        absentCodes.clear();
        retrieveCustomers();
    }

    public void onComplete(){

        final AlertDialog.Builder confirm = new AlertDialog.Builder(this);
        confirm.setMessage("Are you sure you want to COMPLETE the class.\nThis will register the class as finished on the system.");
        confirm.setCancelable(false);

        confirm.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                SQLOperations oper = new SQLOperations(getApplicationContext(), "W");

                for (int k = 0; k < presentCodes.size(); k++) {
                    ContentValues values_present = new ContentValues();
                    values_present.put("Class_ID", getSetClassID());
                    values_present.put("Guest_ID", presentCodes.get(k).toString());
                    values_present.put("Present", 1);
                    oper.insertData(attendanceTable, values_present);
                    oper.deleteData(classMemberTable, "Guest_ID = ?", new String[] {presentCodes.get(k).toString()});
                }

                for (int k = 0; k < absentCodes.size(); k++) {
                    ContentValues values_present = new ContentValues();
                    values_present.put("Class_ID", getSetClassID());
                    values_present.put("Guest_ID", absentCodes.get(k).toString());
                    values_present.put("Present", 0);
                    oper.insertData(attendanceTable, values_present);
                    oper.deleteData(classMemberTable, "Guest_ID = ?", new String[] {presentCodes.get(k).toString()});
                }
                Toast.makeText(getApplicationContext(), "The class has now been finalised", Toast.LENGTH_SHORT).show();

                onCompleteClass(classTable, getSetClassID());
                leave();

            }
        });

        confirm.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        confirm.create().show();

    }

    private void retrieveCustomers(){
        retrieveCustomerCodes(classMemberTable, getSetClassID());
        retrieveCustomerNames(userTable);
        updateLists();

    }

    private void retrieveCustomerCodes(String tableName, String classCode){

        c = myDatabase.query(tableName, new String[] {"Guest_ID"}, "Class_ID = ?", new String[] {classCode}, null, null, null);

        if(c.moveToFirst()){
            do{
                absentCodes.add(c.getString(0));
            } while (c.moveToNext());
        }

        c.close();
    }

    private void retrieveCustomerNames(String tableName){

        c = myDatabase.query(tableName, new String[] {"Guest_ID","First_Name","Last_Name"},null,null,null,null,null);

        if (c.moveToFirst()){
            do{
                for (int k = 0; k < absentCodes.size(); k++){
                    if(absentCodes.get(k).toString().equals(c.getString(0))){
                        absentNames.add(c.getString(1) + " " + c.getString(2));
                    }
                }
            } while (c.moveToNext());
        }

        c.close();
    }

    private void retrieveClassData(String tableName, String classCode){

        String valueType = "";
        String valueTime = "";

        c = myDatabase.query(tableName, new String[] {"Type", "Time_Start", "Time_End"}, "Class_ID = ? ", new String[] {classCode}, null, null, null);

        if(c.moveToFirst()) {
            valueType = c.getString(0);
            valueTime = c.getString(1) + " - " + c.getString(2);
        }

        c.close();

        setClassID(classCode);
        setType(valueType);
        setTime(valueTime);

    }

    public void onCompleteClass(String tableName, String classCode) {

        SQLOperations oper = new SQLOperations(getApplicationContext(), "W");
        ContentValues values_new = new ContentValues();
        values_new.put("Complete", 1);
        oper.updateData(tableName, values_new, "Class_ID = ?", new String[] {classCode});

    }

    public String getSetClassID(){
        return ClassID;
    }

    public String getClassID(){
        return txtClassID.getText().toString();
    }

    public void setClassID(String value){
        txtClassID.setText(value);
    }

    public void setType(String value){
        txtType.setText(value);
    }

    public void setTime(String value){
        txtTime.setText(value);
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

    private void updateLists(){
        ArrayAdapter adapt_absent = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, absentNames);
        ArrayAdapter adapt_present = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, presentNames);

        listAbsent.setAdapter(adapt_absent);
        listPresnt.setAdapter(adapt_present);
    }

}
