package com.example.dynstu;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class FogettonPasswords extends AppCompatActivity {

    private String userCode;
    private String userFirstName;
    private String userLastName;

    private TextView txtCode;
    private TextView txtFullName;
    private TextView txtMessage;

    private ListView listEmails;

    private ArrayList<objPasswords> userEmails = new ArrayList<>();
    private ArrayList codesForUserEmails = new ArrayList();
    private AdapterPasswords adapterPasswords;

    private MySqlLiteOpenHelper myDbHelper;
    private SQLiteDatabase myDatabase;

    private String userAccountsTable = "userAccountDetails_tbl";

    private Cursor d = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fogetton_passwords);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        txtCode = findViewById(R.id.for_pass_code);
        txtFullName = findViewById(R.id.for_pass_name);
        txtMessage = findViewById(R.id.for_pass_message);

        Intent myIntent = getIntent();

        Bundle extras = myIntent.getExtras();

        userCode = extras.getString("CODE");
        userFirstName = extras.getString("FIRSTNAME");
        userLastName = extras.getString("LASTNAME");

        myDbHelper = new MySqlLiteOpenHelper(getApplicationContext());
        myDatabase = myDbHelper.getReadableDatabase();

        setExtras(getUserCode(), getUserFirstName(), getUserLastName());

        listEmails = findViewById(R.id.for_pass_list);
        userAccountEmails();
        adapterPasswords = new AdapterPasswords(this,userEmails);
        listEmails.setAdapter(adapterPasswords);
        listEmails.deferNotifyDataSetChanged();
        listEmails.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //send the code to the Chaneg Password
                // takes the usual plus GUESTCODE
                Intent myIntent = new Intent(getApplicationContext(), ChangePassword.class);

                Bundle extra = new Bundle();

                extra.putString("CODE", getUserCode());
                extra.putString("FIRSTNAME", getUserFirstName());
                extra.putString("LASTNAME", getUserLastName());
                extra.putString("GUESTCODE", codesForUserEmails.get(position).toString());

                myIntent.putExtras(extra);
                startActivity(myIntent);
                finish();

            }
        });


        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Forgotten Password");
        mToolbar.setNavigationIcon(R.drawable.ic_arrow);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(getUserTag(getUserCode()).equals("G") == true){
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

    private void userAccountEmails() {
        codesForUserEmails.clear();
        userEmails.clear();

        d = myDatabase.query(userAccountsTable, null, null, null, null,null,null);

        if(d.moveToFirst()){
            txtMessage.setText("");
            do{
               // userEmails.add(d.getString(4) + ":" + d.getString(1) + "|" + d.getString(2));

                String firstName = d.getString(1);
                String lastName = d.getString(2);
                String email = d.getString(4);
                objPasswords objpasswords = new objPasswords(email, firstName, lastName);

                userEmails.add(objpasswords);

                codesForUserEmails.add(d.getString(0));
            } while (d.moveToNext());

            updateListClasses();
        }  else {
            updateListClasses();
            noEmails();
        }

        d.close();
    }


    private void updateListClasses(){
        ArrayAdapter adapt_classes = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, userEmails);

        listEmails.setAdapter(adapt_classes);
    }

    private void setExtras(String code, String firstName, String lastName){
        txtCode.setText(code);
        txtFullName.setText(firstName + " " + lastName);
    }

    private String getUserTag(String userCode){
        String userTag = "";

        userTag = userCode.substring(0,1);

        return userTag;
    }

    private void noEmails(){
        txtMessage.setText("There are no registered customers yet");
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
