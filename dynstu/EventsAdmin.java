package com.example.dynstu;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class EventsAdmin extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private String userCode;
    private String userFirstName;
    private String userLastName;

    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private MyPager myPager;
    private int count=0;

    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;

    private TextView txtNavName;

    private Button btnPromoCards;
    private Button btnPrivClass;

    private Button btnSchedule;
    private Button btnCreateClass;

    private MySqlLiteOpenHelper myDbHelper;
    private SQLiteDatabase myDatabase;

    private String notifMessageTable = "notifMessage_tbl";

    private Cursor c = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_admin);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setTitle("Dynamic Body Studio");

        Intent myIntent = getIntent();

        Bundle extras = myIntent.getExtras();

        userCode = extras.getString("CODE");
        userFirstName = extras.getString("FIRSTNAME");
        userLastName = extras.getString("LASTNAME");

        NavigationView navigationViewAdmin = findViewById(R.id.nav_view_admin);
        navigationViewAdmin.setNavigationItemSelectedListener(this);

        View headerView = navigationViewAdmin.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.nav_user_name);
        navUsername.setText(getUserFirstName() + " " + getUserLastName() + " : " + getUserCode());


        btnPromoCards = findViewById(R.id.evt_admin_btn_promo_activate);
        btnPrivClass = findViewById(R.id.evt_admin_btn_prv_requests);

        btnSchedule = findViewById(R.id.evt_admin_btn_schedule);
        btnCreateClass = findViewById(R.id.evt_admin_btn_cre_cls);

        btnPromoCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPromoCards();
            }
        });

        btnPrivClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPrivClass();
            }
        });

        btnSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSchedule();
            }
        });

        btnCreateClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCreateClass();
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Dynamic Body Studio");

        drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        myPager = new MyPager(this);
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(myPager);
        circleIndicator = findViewById(R.id.circle);
        circleIndicator.setViewPager(viewPager);


        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(count<=5){
                            viewPager.setCurrentItem(count);
                            count++;
                        }else{
                            count = 1;
                            viewPager.setCurrentItem(count);
                        }
                    }
                });
            }
        }, 500, 3000);


    }

    private void onPromoCards(){

            Intent myIntent = new Intent(getApplicationContext(), PromoActivate.class);

            Bundle extra = new Bundle();

            extra.putString("CODE", getUserCode());
            extra.putString("FIRSTNAME", getUserFirstName());
            extra.putString("LASTNAME", getUserLastName());

            myIntent.putExtras(extra);

            startActivity(myIntent);

            finish();

    }

    private void onPrivClass(){

            Intent myIntent = new Intent(getApplicationContext(), AdminClass.class);

            Bundle extra = new Bundle();

            extra.putString("CODE", getUserCode());
            extra.putString("FIRSTNAME", getUserFirstName());
            extra.putString("LASTNAME", getUserLastName());

            myIntent.putExtras(extra);

            startActivity(myIntent);

            finish();
    }


    private void onSchedule(){
        Intent myIntent = new Intent(getApplicationContext(), AdminSchedule.class);

        Bundle extra = new Bundle();

        extra.putString("CODE", getUserCode());
        extra.putString("FIRSTNAME", getUserFirstName());
        extra.putString("LASTNAME", getUserLastName());

        myIntent.putExtras(extra);

        startActivity(myIntent);

        finish();
    }

    private void onCreateClass(){
        Intent myIntent = new Intent(getApplicationContext(), ClassDetails.class);

        Bundle extra = new Bundle();

        extra.putString("CODE", getUserCode());
        extra.putString("FIRSTNAME", getUserFirstName());
        extra.putString("LASTNAME", getUserLastName());
        extra.putString("CLASSID", "class-add");

        myIntent.putExtras(extra);

        startActivity(myIntent);

        finish();
    }

    @Override
    public void onBackPressed(){
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
            switch (item.getItemId()) {

                case R.id.nav_notifications: {
                       if(getUserTag(getUserCode()).equals("A") == true){
                            Intent i = new Intent(getApplicationContext(), MassMessage.class);

                            Bundle extra = new Bundle();

                            extra.putString("CODE", getUserCode());
                            extra.putString("FIRSTNAME", getUserFirstName());
                            extra.putString("LASTNAME", getUserLastName());

                            i.putExtras(extra);

                            startActivity(i);

                            finish();
                       } else {
                           Toast.makeText(getApplicationContext(), "Only the Admin can send mass messages.", Toast.LENGTH_SHORT).show();
                       }

                    break;
                }
                case R.id.nav_instruct_schedule: {
                        Intent i = new Intent(getApplicationContext(), AdminSchedule.class);

                        Bundle extra = new Bundle();

                        extra.putString("CODE", getUserCode());
                        extra.putString("FIRSTNAME", getUserFirstName());
                        extra.putString("LASTNAME", getUserLastName());

                        i.putExtras(extra);

                        startActivity(i);

                        finish();
                    break;
                }
                case R.id.nav_instruct_ctrls_classes: {
                        Intent i = new Intent(getApplicationContext(), ClassDetails.class);

                        Bundle extra = new Bundle();

                        extra.putString("CODE", getUserCode());
                        extra.putString("FIRSTNAME", getUserFirstName());
                        extra.putString("LASTNAME", getUserLastName());
                        extra.putString("CLASSID", "class-add");

                        i.putExtras(extra);

                        startActivity(i);

                        finish();
                    break;
                }
                case R.id.nav_admin_ctrls_employee: {

                    if(getUserTag(getUserCode()).equals("A")){
                        Intent i = new Intent(getApplicationContext(), AdminInstructor.class);

                        Bundle extra = new Bundle();

                        extra.putString("CODE", getUserCode());
                        extra.putString("FIRSTNAME", getUserFirstName());
                        extra.putString("LASTNAME", getUserLastName());

                        i.putExtras(extra);

                        startActivity(i);

                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Only the Admin can create a new employee account.", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                case R.id.nav_instruct_private: {
                        Intent i = new Intent(getApplicationContext(), AdminClass.class);

                        Bundle extra = new Bundle();

                        extra.putString("CODE", getUserCode());
                        extra.putString("FIRSTNAME", getUserFirstName());
                        extra.putString("LASTNAME", getUserLastName());

                        i.putExtras(extra);

                        startActivity(i);

                        finish();
                    break;
                }
                case R.id.nav_instruct_promo: {
                        Intent i = new Intent(getApplicationContext(), PromoActivate.class);

                        Bundle extra = new Bundle();

                        extra.putString("CODE", getUserCode());
                        extra.putString("FIRSTNAME", getUserFirstName());
                        extra.putString("LASTNAME", getUserLastName());

                        i.putExtras(extra);

                        startActivity(i);

                        finish();
                    break;
                }

                case R.id.nav_forget_pass: {
                    if(getUserTag(getUserCode()).equals("A")){
                        Intent i = new Intent(getApplicationContext(), FogettonPasswords.class);

                        Bundle extra = new Bundle();

                        extra.putString("CODE", getUserCode());
                        extra.putString("FIRSTNAME", getUserFirstName());
                        extra.putString("LASTNAME", getUserLastName());

                        i.putExtras(extra);

                        startActivity(i);

                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Only the Admin can change the password of a user account.", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }

                case R.id.nav_setiings_admin: {
                        Intent i = new Intent(getApplicationContext(), Setting_Admin.class);

                        Bundle extra = new Bundle();

                        extra.putString("CODE", getUserCode());
                        extra.putString("FIRSTNAME", getUserFirstName());
                        extra.putString("LASTNAME", getUserLastName());

                        i.putExtras(extra);

                        startActivity(i);

                        finish();
                    break;
                }
                case R.id.nav_log_out: {
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);

                        finish();
                    break;
                }
            }

        //close navigation drawer
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setNavUserName(String firstName, String lastName){
        txtNavName.setText("");
        txtNavName.setText(firstName + " " + lastName);
    }

    private String getUserTag(String userCode){
        String userTag = "";

        userTag = userCode.substring(0,1);

        return userTag;
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
