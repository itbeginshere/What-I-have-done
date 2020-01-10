package com.example.dynstu;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class AdminSchedule extends AppCompatActivity{

    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;

    private String userCode;
    private String userFirstName;
    private String userLastName;

    private TextView txtCode;
    private TextView txtFullName;
    private TextView txtMessage;

    private ListView listClasses;

    private Spinner spinControls;

    private ArrayList<objSchedule> userClassDetails = new ArrayList<>();
    private ArrayList codesForUserClasses = new ArrayList();
    private AdapterSchedule AdapterSchedule;

    private MySqlLiteOpenHelper myDbHelper;
    private SQLiteDatabase myDatabase;

    private String classSessionTable = "classSession_tbl";

    private Cursor d = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_admin);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        myDbHelper = new MySqlLiteOpenHelper(getApplicationContext());
        myDatabase = myDbHelper.getReadableDatabase();

        txtCode = findViewById(R.id.admin_clss_instruct_code);
        txtFullName = findViewById(R.id.admin_clss_instruct_name);
        txtMessage = findViewById(R.id.admin_clss_instruct_message);

        Intent myIntent = getIntent();

        Bundle extras = myIntent.getExtras();

        userCode = extras.getString("CODE");
        userFirstName = extras.getString("FIRSTNAME");
        userLastName = extras.getString("LASTNAME");

        setExtras(getUserCode(), getUserFirstName(), getUserLastName());

        spinControls = findViewById(R.id.admin_clss_instruct_control);

        ArrayAdapter<CharSequence> adapt_controls = ArrayAdapter.createFromResource(getApplicationContext(), R.array.schedule_controls, android.R.layout.simple_spinner_item);
        adapt_controls.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinControls.setAdapter(adapt_controls);

        listClasses = findViewById(R.id.admin_clss_instruct_list);
        userClasses();
        AdapterSchedule = new AdapterSchedule(this,userClassDetails);
        listClasses.setAdapter(AdapterSchedule);
        listClasses.deferNotifyDataSetChanged();
        listClasses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(getCtrlsPosition() == 0){
                    //edit
                    //send the selected class ID to the ClassDetails class
                    Intent myIntent = new Intent(getApplicationContext(), ClassDetails.class);

                    Bundle extra = new Bundle();

                    extra.putString("CODE", getUserCode());
                    extra.putString("FIRSTNAME", getUserFirstName());
                    extra.putString("LASTNAME", getUserLastName());
                    extra.putString("CLASSID", codesForUserClasses.get(position).toString());

                    myIntent.putExtras(extra);
                    startActivity(myIntent);

                    finish();
                } else {
                    //attendance or complete
                    //send the selected class ID to the AdminClassMember class
                    Intent myIntent = new Intent(getApplicationContext(), AdminClassMember.class);

                    Bundle extra = new Bundle();

                    extra.putString("CODE", getUserCode());
                    extra.putString("FIRSTNAME", getUserFirstName());
                    extra.putString("LASTNAME", getUserLastName());
                    extra.putString("CLASSID", codesForUserClasses.get(position).toString());

                    myIntent.putExtras(extra);
                    startActivity(myIntent);

                    finish();
                }
            }
        });



        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Your Class Schedule");
        mToolbar.setNavigationIcon(R.drawable.ic_arrow);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), EventsAdmin.class);

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

    private void userClasses(){

        //Takes a userID and scans which classes they currently have going on. That they belong to, which are not cancelled or completeed
        codesForUserClasses.clear();
        userClassDetails.clear();

        d = myDatabase.query(classSessionTable, null, "Employee_ID = ? AND Complete = ? AND Cancelled = ? ", new String[] {getUserCode(),"0","0"}, null,null,null);

       if(d.moveToFirst()){
           txtMessage.setText("");
           do{
               String date = d.getString(4);
               String type = d.getString(3);
               String venue = d.getString(7);
               String start = d.getString(5);
               String end = d.getString(6);
               objSchedule objschedule =new objSchedule(date,type,venue,start,end,"","","","");

               userClassDetails.add(objschedule);

               codesForUserClasses.add(d.getString(0));
           } while (d.moveToNext());

           updateListClasses();
       } else {
           updateListClasses();
           noClasses();
       }

        d.close();
    }

    private void updateListClasses(){
        ArrayAdapter adapt_classes = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, userClassDetails);

        listClasses.setAdapter(adapt_classes);
    }

    private void noClasses(){
        txtMessage.setText("You do not have any active classes as of now.\nCreate a class for people to join!");
    }

    private void setExtras(String code, String firstName, String lastName){
        txtCode.setText(code);
        txtFullName.setText(firstName + " " + lastName);
    }

    private int getCtrlsPosition(){
        return spinControls.getSelectedItemPosition();
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