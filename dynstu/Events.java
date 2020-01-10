package com.example.dynstu;


import android.content.Context;
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

import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;


public class Events extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private String userCode;
    private String userFirstName;
    private String userLastName;

    private String checkForPromo;

    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private MyPager myPager;
    private int count=0;

    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;

    private TextView txtNavName;

    private TextView txtPound;
    private TextView txtZumba;
    private TextView txtBalletRip;
    private TextView txtRyhthmPilates;
    private TextView txtBarre;


    private Button btnPromoCards;
    private Button btnPrivClass;
    
    private Button btnPound;
    private Button btnZumba;
    private Button btnBalletRip;
    private Button btnBarre;
    private Button btnRhyPiates;

    private MySqlLiteOpenHelper myDbHelper;
    private SQLiteDatabase myDatabase;

    private String notifMessageTable = "notifMessage_tbl";
    private String promoCardsTable = "promoCards_tbl";

    private Cursor c = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Intent myIntent = getIntent();

        Bundle extras = myIntent.getExtras();

        userCode = extras.getString("CODE");
        userFirstName = extras.getString("FIRSTNAME");
        userLastName = extras.getString("LASTNAME");

            NavigationView navigationView = findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.nav_user_name);
        navUsername.setText(getUserFirstName() + " " + getUserLastName() + " : " + getUserCode());

        txtPound = (TextView) findViewById(R.id.evt_txt_cls_pound);
        txtBalletRip = (TextView) findViewById(R.id.evt_txt_cls_balletrip);
        txtZumba = (TextView) findViewById(R.id.evt_txt_cls_zumba);
        txtRyhthmPilates = (TextView) findViewById(R.id.evt_txt_cls_rhythm_pilates);
        txtBarre = (TextView) findViewById(R.id.evt_txt_cls_barre);

        txtPound.setText("Pound fitness is a combination cardio and wight training exercise approach that includes some of the rythmix techniques used in Pilates.");
        txtBalletRip.setText("A dynamic 60 minute workout of fun, effective movement sequences aimed at transforming mind & body - defined and differentiated by the concept of barre-less ballet.");
        txtZumba.setText("Zumba an aerobic fitness programme featuring movements inspired by various styles of Latin American dance and performed primarily to Latin American dance music.");
        txtRyhthmPilates.setText("Ryhthm Pilates is an exciting fusion of study and disciplines, incorporating Pilates, Yoga & Dance.");
        txtBarre.setText("Barre Time fuses Barre, Ballet, Pilates and Cardio into an energetic and fun filled workout that is sure to leave you wanting more!");
        //Accessing another xml layout component
        btnPromoCards = findViewById(R.id.evt_btn_promo_cards);
        btnPrivClass = findViewById(R.id.evt_btn_prv_class);

        btnPound = findViewById(R.id.evt_btn_cls_pound);
        btnZumba = findViewById(R.id.evt_btn_cls_zumba);
        btnBalletRip = findViewById(R.id.evt_btn_cls_balletrip);
        btnBarre = findViewById(R.id.evt_btn_cls_barre);
        btnRhyPiates = findViewById(R.id.evt_btn_cls_rhypilates);

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

            btnPound.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onPound();
                }
            });

            btnZumba.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onZumba();
                }
            });

            btnBalletRip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBalletRip();
                }
            });

            btnBarre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBarre();
                }
            });

            btnRhyPiates.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRhyPilates();
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

        myDbHelper = new MySqlLiteOpenHelper(getApplicationContext());
        myDatabase = myDbHelper.getReadableDatabase();

        checkForNotifications(notifMessageTable, getUserCode());
        checkForPromo = checkPromoExists(promoCardsTable, getUserCode());
        if(checkForPromo.equals("none") == false){
            if(getPromoBalance(promoCardsTable, checkForPromo) <= 0){
                deletePromoCard(promoCardsTable, checkForPromo);
            }
        }

    }

    private String checkPromoExists(String tableName, String userCode){

       String promoCode = "";
       c = myDatabase.query(tableName, null, "Guest_ID = ?", new String[] {userCode}, null, null, null);

       if(c.moveToFirst()){
           promoCode = c.getString(0);
       } else {
           promoCode = "none";
       }
       c.close();
       return promoCode;
    }

    private int getPromoBalance(String tableName, String promoCode){
        int balance = 0;

        SQLOperations oper = new SQLOperations(getApplicationContext(), "R");

        c = myDatabase.query(tableName, null, "Promo_ID = ?", new String[] {promoCode}, null, null, null);

        c.moveToFirst();

        balance = Integer.parseInt(c.getString(4));

        c.close();

        return balance;
    }


    private void deletePromoCard(String tableName, String promoCard){
        SQLOperations oper = new SQLOperations(getApplicationContext(), "W");
        oper.deleteData(tableName, "Promo_ID = ?", new String[] {promoCard});
    }


    private void checkForNotifications(String tableName, String userCode){
        c = myDatabase.query(tableName, null,"Guest_ID = ? AND Read = ?",new String[] {userCode, "0"},null,null,null);

        if(c.moveToFirst()){
            Toast.makeText(getApplicationContext(), "You have unread notifications. Please take some time to read them by clicking on the 'Notificaitons' tab.", Toast.LENGTH_LONG).show();
        }

        c.close();
    }

    private void onPromoCards(){
            Intent myIntent = new Intent(getApplicationContext(), PromoCard.class);

            Bundle extra = new Bundle();

            extra.putString("CODE", getUserCode());
            extra.putString("FIRSTNAME", getUserFirstName());
            extra.putString("LASTNAME", getUserLastName());

            myIntent.putExtras(extra);

            startActivity(myIntent);

            finish();

    }

    private void onPrivClass(){
            Intent myIntent = new Intent(getApplicationContext(), AdminSendClear.class);

            Bundle extra = new Bundle();

            extra.putString("CODE", getUserCode());
            extra.putString("FIRSTNAME", getUserFirstName());
            extra.putString("LASTNAME", getUserLastName());

            myIntent.putExtras(extra);

            startActivity(myIntent);

            finish();
    }

    private void onPound(){
        Intent myIntent = new Intent(getApplicationContext(), ClassSearchCust.class);

        Bundle extra = new Bundle();

        extra.putString("CODE", getUserCode());
        extra.putString("FIRSTNAME", getUserFirstName());
        extra.putString("LASTNAME", getUserLastName());
        extra.putString("TYPE", "Pound");

        myIntent.putExtras(extra);

        startActivity(myIntent);

        finish();
    }

    private void onZumba() {

            Intent myIntent = new Intent(getApplicationContext(), ClassSearchCust.class);

            Bundle extra = new Bundle();

            extra.putString("CODE", getUserCode());
            extra.putString("FIRSTNAME", getUserFirstName());
            extra.putString("LASTNAME", getUserLastName());
            extra.putString("TYPE", "Zumba");

            myIntent.putExtras(extra);

            startActivity(myIntent);

            finish();

    }

    private void onBalletRip(){

            Intent myIntent = new Intent(getApplicationContext(), ClassSearchCust.class);

            Bundle extra = new Bundle();

            extra.putString("CODE", getUserCode());
            extra.putString("FIRSTNAME", getUserFirstName());
            extra.putString("LASTNAME", getUserLastName());
            extra.putString("TYPE", "Ballet Rip");

            myIntent.putExtras(extra);

            startActivity(myIntent);

            finish();
    }

    private void onBarre(){
        Intent myIntent = new Intent(getApplicationContext(), ClassSearchCust.class);

        Bundle extra = new Bundle();

        extra.putString("CODE", getUserCode());
        extra.putString("FIRSTNAME", getUserFirstName());
        extra.putString("LASTNAME", getUserLastName());
        extra.putString("TYPE", "Barre Time");

        myIntent.putExtras(extra);

        startActivity(myIntent);

        finish();
    }

    private void onRhyPilates(){
        Intent myIntent = new Intent(getApplicationContext(), ClassSearchCust.class);

        Bundle extra = new Bundle();

        extra.putString("CODE", getUserCode());
        extra.putString("FIRSTNAME", getUserFirstName());
        extra.putString("LASTNAME", getUserLastName());
        extra.putString("TYPE", "Rhythm Pilates");

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
                case R.id.nav_cust_search: {
                    Intent i = new Intent(getApplicationContext(), ClassSearchCust.class);

                    Bundle extra = new Bundle();

                    extra.putString("CODE", getUserCode());
                    extra.putString("FIRSTNAME", getUserFirstName());
                    extra.putString("LASTNAME", getUserLastName());
                    extra.putString("TYPE", "type-all");

                    i.putExtras(extra);

                    startActivity(i);

                    finish();
                    break;
                }
                case R.id.nav_notifications: {
                    Intent i = new Intent(getApplicationContext(), Notifications.class);

                    Bundle extra = new Bundle();

                    extra.putString("CODE", getUserCode());
                    extra.putString("FIRSTNAME", getUserFirstName());
                    extra.putString("LASTNAME", getUserLastName());

                    i.putExtras(extra);

                    startActivity(i);

                    finish();
                    break;
                }
                case R.id.nav_cust_classes: {
                    Intent i = new Intent(getApplicationContext(), Classes_List.class);

                    Bundle extra = new Bundle();

                    extra.putString("CODE", getUserCode());
                    extra.putString("FIRSTNAME", getUserFirstName());
                    extra.putString("LASTNAME", getUserLastName());

                    i.putExtras(extra);

                    startActivity(i);

                    finish();
                    break;
                }
                case R.id.nav_cust_private: {
                    Intent i = new Intent(getApplicationContext(), AdminSendClear.class);

                    Bundle extra = new Bundle();

                    extra.putString("CODE", getUserCode());
                    extra.putString("FIRSTNAME", getUserFirstName());
                    extra.putString("LASTNAME", getUserLastName());

                    i.putExtras(extra);

                    startActivity(i);

                    finish();
                    break;
                }
                case R.id.nav_cust_payment: {
                    Intent i = new Intent(getApplicationContext(), CreditCard.class);

                    Bundle extra = new Bundle();

                    extra.putString("CODE", getUserCode());
                    extra.putString("FIRSTNAME", getUserFirstName());
                    extra.putString("LASTNAME", getUserLastName());

                    i.putExtras(extra);

                    startActivity(i);

                    finish();
                    break;
                }
                case R.id.nav_cust_promo: {
                    Intent i = new Intent(getApplicationContext(), PromoCard.class);

                    Bundle extra = new Bundle();

                    extra.putString("CODE", getUserCode());
                    extra.putString("FIRSTNAME", getUserFirstName());
                    extra.putString("LASTNAME", getUserLastName());

                    i.putExtras(extra);

                    startActivity(i);

                    finish();
                    break;
                }

                case R.id.nav_cust_setiings: {
                    Intent i = new Intent(getApplicationContext(), Setting_Cust.class);

                    Bundle extra = new Bundle();

                    extra.putString("CODE", getUserCode());
                    extra.putString("FIRSTNAME", getUserFirstName());
                    extra.putString("LASTNAME", getUserLastName());

                    i.putExtras(extra);

                    startActivity(i);

                    finish();
                    break;
                }

                case R.id.nav_cust_log_out: {
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
        txtNavName.setText(firstName + " " + lastName);
        txtNavName.refreshDrawableState();
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
