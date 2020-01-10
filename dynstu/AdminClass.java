package com.example.dynstu;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdminClass extends AppCompatActivity{
    private String userCode;
    private String userFirstName;
    private String userLastName;

    private TextView txtCode;
    private TextView txtFullName;
    private TextView txtMessage;

    private ListView listRequests;

    private ArrayList<objSchedule> userRequestsDetails = new ArrayList<>();
    private AdapterPrivate AdapterPrivate;
    private ArrayList codesForUserRequests = new ArrayList();

    private MySqlLiteOpenHelper myDbHelper;
    private SQLiteDatabase myDatabase;

    private String classRequestTable = "privateClassRequest_tbl";

    private Cursor d = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_admin);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        myDbHelper = new MySqlLiteOpenHelper(getApplicationContext());
        myDatabase = myDbHelper.getReadableDatabase();

        txtCode = findViewById(R.id.admin_req_instruct_code);
        txtFullName = findViewById(R.id.admin_req_instruct_name);
        txtMessage = findViewById(R.id.admin_req_instruct_message);

        Intent myIntent = getIntent();

        Bundle extras = myIntent.getExtras();

        userCode = extras.getString("CODE");
        userFirstName = extras.getString("FIRSTNAME");
        userLastName = extras.getString("LASTNAME");

        setExtras(getUserCode(), getUserFirstName(), getUserLastName());

        listRequests = findViewById(R.id.admin_req_instruct_list);
        userRequests();
        AdapterPrivate = new AdapterPrivate(this,userRequestsDetails);
        listRequests.setAdapter(AdapterPrivate);
        listRequests.deferNotifyDataSetChanged();
        listRequests.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //send the code to the AdminAddDeny Class
                // takes the usual plus REQUESTCODE
                Intent myIntent = new Intent(getApplicationContext(), AdminAddDeny.class);

                Bundle extra = new Bundle();

                extra.putString("CODE", getUserCode());
                extra.putString("FIRSTNAME", getUserFirstName());
                extra.putString("LASTNAME", getUserLastName());
                extra.putString("REQUESTCODE", codesForUserRequests.get(position).toString());

                myIntent.putExtras(extra);
                startActivity(myIntent);
                finish();

            }
        });

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Private Session Requets");
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

    private void userRequests() {
        codesForUserRequests.clear();
        userRequestsDetails.clear();

        d = myDatabase.query(classRequestTable, null, "Employee_ID = ?", new String[] {getUserCode()}, null,null,null);

        if(d.moveToFirst()){
            txtMessage.setText("");
            do{
                String date = d.getString(5);
                String code = d.getString(6);
                String cap = d.getString(2);
                String start = d.getString(3);
                String end = d.getString(4);
                objSchedule objschedule =new objSchedule(date,"","",start,end,cap,code,"","");

                userRequestsDetails.add(objschedule);

               // userRequestsDetails.add(d.getString(2) + "|" + d.getString(3) + "|" + d.getString(4) + "|" + d.getString(5) + "|"+d.getString(6));
                codesForUserRequests.add(d.getString(0));
            } while (d.moveToNext());

            updateListClasses();
        } else {
            updateListClasses();
            noRequests();
        }

        d.close();
    }

    private void updateListClasses(){
        ArrayAdapter adapt_classes = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, userRequestsDetails);

        listRequests.setAdapter(adapt_classes);
    }

    private void noRequests(){
        txtMessage.setText("You do not have any private class requests as of now.\n\nCheck in a few minutes, maybe someone will have joined by then!");
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
