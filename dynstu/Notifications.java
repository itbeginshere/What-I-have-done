package com.example.dynstu;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Notifications extends AppCompatActivity {

    private String userCode;
    private String userFirstName;
    private String userLastName;

    private ArrayList codesForUserNotificaitons = new ArrayList();
    private ArrayList<objNotif> userNotificationsDetails = new ArrayList<>();
    private AdapterNotif adapterNotif;

    private ListView listNotifications;

    private TextView txtMessage;

    private Button btnRead;

    private int notifcationPosition = 0;

    private MySqlLiteOpenHelper myDbHelper;
    private SQLiteDatabase myDatabase;

    private String notifMessageTable = "notifMessage_tbl";

    private Cursor d = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        myDbHelper = new MySqlLiteOpenHelper(getApplicationContext());
        myDatabase = myDbHelper.getReadableDatabase();

        Intent myIntent = getIntent();

        Bundle extras = myIntent.getExtras();

        userCode = extras.getString("CODE");
        userFirstName = extras.getString("FIRSTNAME");
        userLastName = extras.getString("LASTNAME");

        txtMessage = findViewById(R.id.notif_msgs_txt);

        btnRead = findViewById(R.id.notif_msgs_read);

        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readNotification();
            }
        });

        listNotifications = findViewById(R.id.notif_msgs_list);
        userNotifications();
        adapterNotif = new AdapterNotif(this,userNotificationsDetails);
        listNotifications.setAdapter(adapterNotif);
        listNotifications.deferNotifyDataSetChanged();
        listNotifications.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Click on a code and display the message in the messageText filed

                displayFullMessage(notifMessageTable, position);
                setSelectedNotificationPosition(position);

            }
        });

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Your Notifications");
        mToolbar.setNavigationIcon(R.drawable.ic_arrow);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(getCodeTag(getUserCode()).equals("G") == true){
                    Intent i = new Intent(getApplicationContext(), Events.class);

                    Bundle extra = new Bundle();

                    extra.putString("CODE", getUserCode());
                    extra.putString("FIRSTNAME", getUserFirstName());
                    extra.putString("LASTNAME", getUserLastName());

                    i.putExtras(extra);

                    startActivity(i);
                    finish();
                } else {
                    Intent i = new Intent(getApplicationContext(), EventsAdmin.class);

                    Bundle extra = new Bundle();

                    extra.putString("CODE", getUserCode());
                    extra.putString("FIRSTNAME", getUserFirstName());
                    extra.putString("LASTNAME", getUserLastName());

                    i.putExtras(extra);

                    startActivity(i);
                    finish();
                }
            }
        });

    }

    private String getCodeTag(String userCode){
        String codeTag = "";

        codeTag = userCode.substring(0,1);

        return codeTag;
    }

    private void readNotification(){

        if(codesForUserNotificaitons.size() > 0){
            final String currentNotif = codesForUserNotificaitons.get(getCurrentNotification()).toString();

            final AlertDialog.Builder confirm = new AlertDialog.Builder(this);
            confirm.setMessage("Do you want to mark this message as read?\nYou will no longer be able to view this message...");
            confirm.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SQLOperations oper = new SQLOperations(getApplicationContext(), "W");

                    ContentValues update_values = new ContentValues();

                    update_values.put("Read", 1);

                    oper.updateData(notifMessageTable, update_values,"Notif_ID = ?", new String[] {currentNotif});
                    userNotifications();

                }
            });
            confirm.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            confirm.create().show();
        } else {
            Toast.makeText(getApplicationContext(), "You have no messages to read.", Toast.LENGTH_SHORT).show();
        }
    }

    private void userNotifications(){
        codesForUserNotificaitons.clear();
        userNotificationsDetails.clear();

        d = myDatabase.query(notifMessageTable, null, "Guest_ID = ? AND Read = ?", new String[] {getUserCode(),"0"}, null,null,null);

        if(d.moveToFirst()){
            txtMessage.setText("");
            do{
                String message = copyFirstThirtyChars(d.getString(2));
                String code = d.getString(0);
                objNotif objnotif = new objNotif(code, message);

                userNotificationsDetails.add(objnotif);

                codesForUserNotificaitons.add(d.getString(0));
            } while (d.moveToNext());

            updateListNotifications();
        } else {
            updateListNotifications();
            noNotifications();
        }

        d.close();
    }

    private String copyFirstThirtyChars(String value){

        String thirtyChars = "";

        if((thirtyChars.length() >= 20) == true){
            thirtyChars = value.substring(0, value.length() / 2);
        } else {
            thirtyChars = value;
        }

        return thirtyChars;
    }

    private void updateListNotifications(){
        adapterNotif = new AdapterNotif(this,userNotificationsDetails);
        listNotifications.setAdapter(adapterNotif);
    }

    private void noNotifications(){
        txtMessage.setText("You do not have any notifications...");
    }

    private void setSelectedNotificationPosition(int index){
        notifcationPosition = index;
    }

    private int getCurrentNotification(){
        return notifcationPosition;
    }

    private void displayFullMessage(String tableName, int selectedNotif){
        String currentNotifID = codesForUserNotificaitons.get(selectedNotif).toString();

        d = myDatabase.query(tableName, null,"Notif_ID = ?",new String[] {currentNotifID},null,null,null);

        d.moveToFirst();

        txtMessage.setText(d.getString(2));

        d.close();
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
