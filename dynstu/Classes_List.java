package com.example.dynstu;

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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Classes_List extends AppCompatActivity{

    private String userCode;
    private String userFirstName;
    private String userLastName;

    private TextView txtCode;
    private TextView txtFullName;
    private TextView txtMessage;

    private ListView listClasses;

    private ArrayList<objSchedule> userClassDetails = new ArrayList<>();
    private ArrayList codesForUserClasses = new ArrayList();
    private AdapterSchedule AdapterSchedule;

    private MySqlLiteOpenHelper myDbHelper;
    private SQLiteDatabase myDatabase;

    private String classSessionTable = "classSession_tbl";
    private String classSessionMembersTable = "classSessionMembers_tbl";

    private Cursor c = null;
    private Cursor d = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_list);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        myDbHelper = new MySqlLiteOpenHelper(getApplicationContext());
        myDatabase = myDbHelper.getReadableDatabase();

        txtCode = findViewById(R.id.cls_dets_cust_code);
        txtFullName = findViewById(R.id.cls_dets_cust_name);
        txtMessage = findViewById(R.id.cls_dets_cust_message);

        Intent myIntent = getIntent();

        Bundle extras = myIntent.getExtras();

        userCode = extras.getString("CODE");
        userFirstName = extras.getString("FIRSTNAME");
        userLastName = extras.getString("LASTNAME");

        setExtras(getUserCode(), getUserFirstName(), getUserLastName());

        listClasses = findViewById(R.id.cls_dets_cust_classes);
        userClasses();

        AdapterSchedule = new AdapterSchedule(this,userClassDetails);
        listClasses.setAdapter(AdapterSchedule);
        listClasses.deferNotifyDataSetChanged();
        listClasses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cancelClass(getUserCode(), codesForUserClasses.get(position).toString());
            }
        });

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Cancel Personal Classes");
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

    private void userClasses(){

        //Takes a userID and scans which classes they currently have going on. That they belong to, which are not cancelled or completeed
        codesForUserClasses.clear();
        userClassDetails.clear();

        c = myDatabase.query(classSessionMembersTable, new String[] {"Class_ID"}, "Guest_ID = ? ", new String[] {getUserCode()}, null, null, null);
        d = myDatabase.query(classSessionTable, null, "Complete = ? OR Cancelled = ? ", new String[] {"0","0"}, null,null,null);

        if(c.moveToFirst()){
            txtMessage.setText("");
            do {
                codesForUserClasses.add(c.getString(0));
            } while(c.moveToNext());

            if (d.moveToFirst()){
                do{
                    for(int k = 0; k < codesForUserClasses.size(); k++) {
                        if (d.getString(0).equals(codesForUserClasses.get(k).toString())) {
                            //userClassDetails.add(d.getString(3) + "|" + d.getString(4) + "|" + d.getString(5) + "|" + d.getString(6) + "|"+d.getString(7));

                            String date = d.getString(4);
                            String type = d.getString(3);
                            String venue = d.getString(7);
                            String start = d.getString(5);
                            String end = d.getString(6);
                            objSchedule objschedule = new objSchedule(date,type,venue,start,end,"","","","");

                            userClassDetails.add(objschedule);
                        }
                    }
                } while (d.moveToNext());
            }

            updateListClasses();

        } else {
            updateListClasses();
            noJoinedClass();
        }

        d.close();
        c.close();

    }

    private void cancelClass(final String userID, final String classID){

        final AlertDialog.Builder confirm = new AlertDialog.Builder(this);
        confirm.setMessage("Remove booking for Class: " + classID + "?\nYou will not be refunded...");
        confirm.setCancelable(false);
        confirm.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SQLOperations oper = new SQLOperations(getApplicationContext(), "W");
                oper.deleteData(classSessionMembersTable, "Guest_ID = ? AND Class_ID = ?", new String[] {userID, classID});
                userClasses();
            }
        });

        confirm.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        confirm.create().show();

    }

    private void noJoinedClass(){
        txtMessage.setText("This is where your booked classes would be displayed if you had any.\n\nJoin one of our exciting classes today!");
    }

    private void updateListClasses(){
        //ArrayAdapter adapt_classes = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, userClassDetails);

        listClasses.setAdapter(AdapterSchedule);
    }

    private void setExtras(String code, String firstName, String lastName){
        txtCode.setText(code);
        txtFullName.setText(firstName + " " + lastName);
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
