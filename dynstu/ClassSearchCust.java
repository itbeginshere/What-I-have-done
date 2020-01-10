package com.example.dynstu;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class ClassSearchCust extends AppCompatActivity{

    private String userCode;
    private String userFirstName;
    private String userLastName;

    private String classType;

    private EditText edtMinPrice;
    private EditText edtMaxPrice;

    private Spinner spinType;

    private ListView listClasses;

    private Button btnSearch;

    private ArrayList codesForClassesInList = new ArrayList();
    private ArrayList<objSchedule> infoAboutClassesForList = new ArrayList<>();
    private AdapterClass AdapterClass;

    private MySqlLiteOpenHelper myDbHelper;
    private SQLiteDatabase myDatabase;

    private String classSessionTable = "classSession_tbl";
    private String classSessionMembersTable = "classSessionMembers_tbl";
    private String paymentDetailsTable = "paymentDetails_tbl";
    private String promoCardsTable = "promoCards_tbl";

    private Cursor c = null;
    private Cursor d = null;
    private Cursor e = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_search);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        edtMinPrice =  findViewById(R.id.cls_srch_min_price);
        edtMaxPrice = findViewById(R.id.cls_srch_max_price);

        spinType = findViewById(R.id.cls_srch_type);

        ArrayAdapter<CharSequence> adpat_type = ArrayAdapter.createFromResource(getApplicationContext(), R.array.class_types_with_any, android.R.layout.simple_spinner_item);

        adpat_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinType.setAdapter(adpat_type);

        Intent myIntent = getIntent();

        Bundle extras = myIntent.getExtras();

        userCode = extras.getString("CODE");
        userFirstName = extras.getString("FIRSTNAME");
        userLastName = extras.getString("LASTNAME");

        classType = extras.getString("TYPE"); // Default = type-all

        if(classType.equals("type-all")){
            spinType.setSelection(0);
        } else {
            setClassType(classType);
        }

        btnSearch = findViewById(R.id.cls_srch_btn_seacrh);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uponArrival(getMinPrice(), getMaxPrice());
            }
        });

        listClasses = findViewById(R.id.cls_srch_classes_list);

        //ArrayAdapter adapt_classes = new ArrayAdapter<objSchedule>(getApplicationContext(), android.R.layout.simple_spinner_item, infoAboutClassesForList);

        uponArrival("0", "999");
        AdapterClass = new AdapterClass(this,infoAboutClassesForList);
        listClasses.setAdapter(AdapterClass);
        listClasses.deferNotifyDataSetChanged();
        listClasses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onJoin(codesForClassesInList.get(position).toString());
            }
        });

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Available Classes");
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

    private void uponArrival(String minimum, String maximum){

        myDbHelper = new MySqlLiteOpenHelper(getApplicationContext());
        myDatabase = myDbHelper.getReadableDatabase();

        codesForClassesInList.clear();
        infoAboutClassesForList.clear();

        Map<String, String> sessionCapacity = new HashMap<String, String>();
        int capacityCounter = 0;
        String startClassID = "";
 /*       Calendar cal = GregorianCalendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 7);
        Date nextTime = cal.getTime();
        Date currentTime = Calendar.getInstance().getTime();

        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat nextDate = new SimpleDateFormat("dd/MM/yyyy");
        String currentWeek = currentDate.format(currentTime);
        String nextWeek = nextDate.format(nextTime);

*/
        String minPrice = minimum;
        String maxPrice = maximum;


        if (minPrice.equals("") ==  true){
            minPrice = "0";
        }

        if (maxPrice.equals("") == true){
            maxPrice = "9999";
        }
        if (checkFields(minPrice, maxPrice) == true) {

            /*if (getClassType().equals("Any") == true) {
                c = myDatabase.query(classSessionTable, null, "Private = ? AND Price BETWEEN ? AND ? AND Date BETWEEN ? AND ? AND Complete = ? AND Cancelled = ?", new String[]{"0", minPrice, maxPrice, currentWeek, nextWeek, "0", "0"}, null, null, "Class_ID");

            } else {
                c = myDatabase.query(classSessionTable, null, "Private = ? AND Price BETWEEN ? AND ? AND Type = ? AND Date BETWEEN ? AND ? AND Complete = ? AND Cancelled = ?", new String[]{"0", minPrice, maxPrice, getClassType(), currentWeek, nextWeek, "0", "0"}, null, null, "Class_ID");

            }*/

            if (getClassType().equals("Any") == true) {
                c = myDatabase.query(classSessionTable, null, "Private = ? AND Price BETWEEN ? AND ? AND Complete = ? AND Cancelled = ?", new String[]{"0", minPrice, maxPrice, "0", "0"}, null, null, "Class_ID");

            } else {
                c = myDatabase.query(classSessionTable, null, "Private = ? AND Price BETWEEN ? AND ? AND Type = ? AND Complete = ? AND Cancelled = ?", new String[]{"0", minPrice, maxPrice, getClassType(), "0", "0"}, null, null, "Class_ID");

            }

            d = myDatabase.query(classSessionMembersTable, null, null, null, null, null, "Class_ID");


            if (d.moveToFirst()) {
                startClassID = d.getString(0);
                do {
                    capacityCounter++;
                    if (startClassID.equals(d.getString(0))) {
                        //still the same class ID

                        if (sessionCapacity.get(startClassID) != null) {
                            sessionCapacity.remove(startClassID);
                            sessionCapacity.put(startClassID, String.valueOf(capacityCounter));
                        } else {
                            sessionCapacity.put(startClassID, String.valueOf(capacityCounter));
                        }
                    } else {
                        //The class ID has changed
                        capacityCounter = 1;
                        startClassID = d.getString(0);
                        sessionCapacity.put(startClassID, String.valueOf(capacityCounter));
                    }

                } while (d.moveToNext());


                if (c.moveToFirst()) {
                    do {
                        if (sessionCapacity.get(c.getString(0)) != null) {
                            if (Integer.parseInt(sessionCapacity.get(c.getString(0))) >= Integer.parseInt(c.getString(2))) {
                                //class is full
                                String date = c.getString(4);
                                String type = c.getString(3);
                                String venue = c.getString(7);
                                String start = c.getString(5);
                                String end = c.getString(6);
                                String full = "FULLY BOOKED";
                                String price = c.getString(9);
                                objSchedule objschedule = new objSchedule(date,type,venue,start,end,"","",price,full);
                                Log.e("ERROR TABLE LOG", c.getString(0) + "---" + c.getString(1)+ "---" + c.getString(2));
                                infoAboutClassesForList.add(objschedule);

                                //infoAboutClassesForList2.add("FULLY BOOKED!!!" + c.getString(3) + " | " + c.getString(4) + "|" + c.getString(5) + "-" + c.getString(6) + "|" + c.getString(7) + "|" + c.getString(9));
                                codesForClassesInList.add("FULL");
                            } else {
                                //class has space
                                String date = c.getString(4);
                                String type = c.getString(3);
                                String venue = c.getString(7);
                                String start = c.getString(5);
                                String end = c.getString(6);
                                String price = c.getString(9);
                                objSchedule objschedule =new objSchedule(date,type,venue,start,end,"","",price,"");
                                Log.e("ERROR TABLE LOG", c.getString(0) + "---" + c.getString(1)+ "---" + c.getString(2));
                                infoAboutClassesForList.add(objschedule);

                               // infoAboutClassesForList2.add(c.getString(3) + " | " + c.getString(4) + "|" + c.getString(5) + "-" + c.getString(6) + "|" + c.getString(7) + "|" + c.getString(9));
                                codesForClassesInList.add(c.getString(0));
                            }
                        } else {

                            String date = c.getString(4);
                            String type = c.getString(3);
                            String venue = c.getString(7);
                            String start = c.getString(5);
                            String end = c.getString(6);
                            String price = c.getString(9);
                            objSchedule objschedule =new objSchedule(date,type,venue,start,end,"","",price,"");
                            Log.e("ERROR TABLE LOG", c.getString(0) + "---" + c.getString(1)+ "---" + c.getString(2));
                            infoAboutClassesForList.add(objschedule);

                            //infoAboutClassesForList2.add(c.getString(3) + " | " + c.getString(4) + "|" + c.getString(5) + "-" + c.getString(6) + "|" + c.getString(7) + "|" + c.getString(9));
                            codesForClassesInList.add(c.getString(0));
                        }
                    } while (c.moveToNext());
                }

            } else {
                if (c.moveToFirst()) {
                    do {

                        String date = c.getString(4);
                        String type = c.getString(3);
                        String venue = c.getString(7);
                        String start = c.getString(5);
                        String end = c.getString(6);
                        String price = c.getString(9);
                        objSchedule objschedule =new objSchedule(date,type,venue,start,end,"","",price,"");
                        Log.e("ERROR TABLE LOG", c.getString(0) + "---" + c.getString(1)+ "---" + c.getString(2));
                        infoAboutClassesForList.add(objschedule);

                        //infoAboutClassesForList2.add(c.getString(3) + " | " + c.getString(4) + "|" + c.getString(5) + "-" + c.getString(6) + "|" + c.getString(7) + "|" + c.getString(9));
                        codesForClassesInList.add(c.getString(0));
                    } while (c.moveToNext());
                }
            }

            d.close();
            c.close();

            updateListClasses();
        } else {
            Toast.makeText(getApplicationContext(), "The prices can only be numerical values", Toast.LENGTH_LONG).show();
        }
    }

    private void onJoin(String classID){
        myDbHelper = new MySqlLiteOpenHelper(getApplicationContext());
        myDatabase = myDbHelper.getWritableDatabase();

        final String selectedClass = classID;
        final AlertDialog.Builder confirm = new AlertDialog.Builder(this);
        final AlertDialog.Builder confirmPayOption = new AlertDialog.Builder(this);

        if(selectedClass.equals("FULL") == true){
            Toast.makeText(getApplicationContext(), "Sorry, this class is fully booked.", Toast.LENGTH_LONG).show();
        } else {

            confirm.setMessage("Are you sure you want to JOIN this class session?");
            confirmPayOption.setMessage("You do not have a promo card. Purchase with card?");

            confirm.setCancelable(false);
            confirmPayOption.setCancelable(false);

            confirm.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if (alreadyJoined(classSessionMembersTable, selectedClass, getUserCode()) == true) {
                        if (retrievedPromoCode(promoCardsTable, getUserCode()) == true) {
                            // if there is a promoCard
                            if(checkPromoActive(promoCardsTable, getUserCode()) == true){
                                //is avtive
                                if (positiveBalance(promoCardsTable, getUserCode()) == true) {
                                    //substract one
                                    SQLOperations oper = new SQLOperations(getApplicationContext(), "W");
                                    updatePromoCard(promoCardsTable, getUserCode());
                                    ContentValues values_new = new ContentValues();
                                    values_new.put("Class_ID", selectedClass);
                                    values_new.put("Guest_ID", getUserCode());
                                    oper.insertData(classSessionMembersTable, values_new);
                                    Toast.makeText(getApplicationContext(), "You now have a seat in the class. Please check your personal classes.", Toast.LENGTH_SHORT).show();
                                } else {
                                    confirmPayOption.setMessage("Your promo card has been depleted. Purchase with card?");
                                    confirmPayOption.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            SQLOperations oper = new SQLOperations(getApplicationContext(), "W");
                                            ContentValues values_new = new ContentValues();
                                            values_new.put("Class_ID", selectedClass);
                                            values_new.put("Guest_ID", getUserCode());
                                            oper.insertData(classSessionMembersTable, values_new);
                                            Toast.makeText(getApplicationContext(), "You now have a seat in the class. Please check your personal classes.", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    confirmPayOption.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(getApplicationContext(), "Return to the Home Screen to buy a promocard", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    confirmPayOption.create().show();
                                }
                            } else {
                                //not active
                                confirmPayOption.setMessage("Your promo card is not active. Purchase with card?");
                                if (checkForPayDetails(paymentDetailsTable, getUserCode()) == true) {
                                    //No promoCard
                                    confirmPayOption.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            SQLOperations oper = new SQLOperations(getApplicationContext(), "W");
                                            ContentValues values_new = new ContentValues();
                                            values_new.put("Class_ID", selectedClass);
                                            values_new.put("Guest_ID", getUserCode());
                                            oper.insertData(classSessionMembersTable, values_new);
                                        }
                                    });

                                    confirmPayOption.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(getApplicationContext(), "Return to the Home Screen to buy a promocard.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    confirmPayOption.create().show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "You do not have any payment method set up yet. Please add a card to your account.", Toast.LENGTH_SHORT).show();
                                }
                            }

                        } else {
                            if (checkForPayDetails(paymentDetailsTable, getUserCode()) == true) {
                                //No promoCard
                                confirmPayOption.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        SQLOperations oper = new SQLOperations(getApplicationContext(), "W");
                                        ContentValues values_new = new ContentValues();
                                        values_new.put("Class_ID", selectedClass);
                                        values_new.put("Guest_ID", getUserCode());
                                        oper.insertData(classSessionMembersTable, values_new);
                                    }
                                });

                                confirmPayOption.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(getApplicationContext(), "Return to the Home Screen to buy a promocard.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                confirmPayOption.create().show();
                            } else {
                                Toast.makeText(getApplicationContext(), "You do not have any payment method set up yet. Please add a card to your account.", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                }
            });

            confirm.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            confirm.create().show();
        }
    }

    private boolean checkPromoActive(String tableName, String userCode){

        c = myDatabase.query(tableName, null,"Guest_ID = ?",new String[] {userCode},null,null,null);

        c.moveToFirst();

        if(c.getString(5).equals("0") == true){
            c.close();
            return false;
        } else {
            c.close();
            return true;
        }

    }


    private void updatePromoCard(String tableName, String userName){
        int oldBalance = 0;
        int newBalance = 0;

        SQLOperations oper = new SQLOperations(getApplicationContext(), "W");

        c = myDatabase.query(tableName, null, "Guest_ID = ?", new String[] {userName}, null, null, null);

        c.moveToFirst();

        oldBalance = Integer.parseInt(c.getString(4));
        newBalance = oldBalance - 1;

        c.close();

        ContentValues values_new = new ContentValues();

        values_new.put("Session_Balance", newBalance);

        oper.updateData(tableName, values_new, "Guest_ID = ?", new String[] {userName});

    }

    private boolean retrievedPromoCode(String tableName, String userName){

        c = myDatabase.query(tableName, null, "Guest_ID = ?", new String[] {userName}, null, null, null);

        if(c.moveToFirst()){
            if(c.getString(1).equals(userName)) {
                c.close();
                return true;
            }
        }
        c.close();
        return false;
    }

    private boolean positiveBalance(String tableName, String userName){
        c = myDatabase.query(tableName, null, "Guest_ID = ?", new String[] {userName}, null, null, null);

        if(c.moveToFirst()){
            if (Integer.parseInt(c.getString(4)) >= 1){
                c.close();
                return true;
            }
        }
        c.close();
        return false;
    }

    private boolean alreadyJoined(String tableName, String classSelect , String userName){

        d = myDatabase.query(tableName, null, null, null, null, null , "Class_ID");

        if(d.moveToFirst()){
            do{
                if(d.getString(0).equals(classSelect) == true && d.getString(1).equals(userName) == true){
                    Toast.makeText(getApplicationContext(), "You have already joined this class session!", Toast.LENGTH_LONG).show();
                    return false;
                }
            } while (d.moveToNext());
        } else {
            d.close();
            return true;
        }
        d.close();
        return true;
    }

    private boolean checkForPayDetails(String tableName, String userName){
        e = myDatabase.query(tableName, null, null, null, null, null , null);

        if(e.moveToFirst()){
            do{
                if(e.getString(0).equals(userName) == true){

                    return true;
                }
            } while (e.moveToNext());
        } else {
            e.close();
            Toast.makeText(getApplicationContext(), "You do not have any payment method set up yet. Please add a card to your account.", Toast.LENGTH_LONG).show();
            return false;
        }
        e.close();
        Toast.makeText(getApplicationContext(), "You do not have any payment method set up yet. Please add a card to your account.", Toast.LENGTH_LONG).show();
        return false;
    }

    private boolean isNumber(String potentailNumber){

        try{
            double  value = Double.parseDouble(potentailNumber);
        } catch(NumberFormatException er) {

            return false;
        }
        return true;
    }

    private boolean checkFields(String min, String max){
        if(isNumber(min)== false){
            edtMinPrice.setBackgroundColor(Color.RED);
            return false;
        } else if (isNumber(max)== false){
            edtMaxPrice.setBackgroundColor(Color.RED);
            return false;
        }
        //min cant be bigger than max and vice versa

        return true;
    }

    private void updateListClasses(){
        //ArrayAdapter adapt_classes = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, infoAboutClassesForList);

        listClasses.setAdapter(AdapterClass);
    }

    public void setClassType(String value){
        for (int k = 0; k < spinType.getCount(); k++){
            if(spinType.getItemAtPosition(k).equals(value)){
                spinType.setSelection(k);
            }
        }
    }

    public String getClassType(){
        return spinType.getSelectedItem().toString();
    }

    public String getMinPrice(){
        return edtMinPrice.getText().toString();
    }

    public String getMaxPrice(){
        return edtMaxPrice.getText().toString();
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
