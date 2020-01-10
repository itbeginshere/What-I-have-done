package com.example.dynstu;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MassMessage extends AppCompatActivity {

    private String userCode;
    private String userFirstName;
    private String userLastName;

    private EditText edtMassMessage;

    private Button btnMessageSend;

    private MySqlLiteOpenHelper myDbHelper;
    private SQLiteDatabase myDatabase;

    private String notifMessageTable = "notifMessage_tbl";
    private String userAccountsTable = "userAccountDetails_tbl";

    private Cursor c = null;
    private Cursor d = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mass_message);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        edtMassMessage = findViewById(R.id.mass_txt_message);
        btnMessageSend = findViewById(R.id.mass_msg_btn_send);

        edtMassMessage.setText("");

        btnMessageSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        Intent myIntent = getIntent();

        Bundle extras = myIntent.getExtras();

        userCode = extras.getString("CODE");
        userFirstName = extras.getString("FIRSTNAME");
        userLastName = extras.getString("LASTNAME");

        myDbHelper = new MySqlLiteOpenHelper(getApplicationContext());
        myDatabase = myDbHelper.getReadableDatabase();

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Mass Message");
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

    private void sendMessage(){

        String currentGuest = "";
        String notifMsg = getMassMessage();
        edtMassMessage.setBackgroundColor(Color.LTGRAY);

        if(getMassMessage().equals("") == false){
            //message is there

            c = myDatabase.query(userAccountsTable, null, null,null,null,null,null);

            if(c.moveToFirst()){
                //send the notif to each customer
                do{
                    currentGuest = c.getString(0);
                    insertNotifData(notifMessageTable, notifMsg, currentGuest);
                } while(c.moveToNext());
                Toast.makeText(getApplicationContext(), "Your message has been sent to all customers.", Toast.LENGTH_SHORT).show();

            } else {
                //There are no customers
                Toast.makeText(getApplicationContext(), "There are no users registered yet.", Toast.LENGTH_SHORT).show();
            }

            c.close();

        } else {
            // no message there
            edtMassMessage.setError("Your Message cannot be blank!");
        }
        edtMassMessage.setText("");
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


    public String getMassMessage(){
        return edtMassMessage.getText().toString();
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
