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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.Random;


public class PromoCard extends AppCompatActivity{

    private String userCode;
    private String userFirstName;
    private String userLastName;

    private TextView txtPromoInfo;

    private Spinner spinPromoPayType;

    private Button btnPur10;
    private Button btnPur20;
    private Button btnPur1;

    private MySqlLiteOpenHelper myDbHelper;
    private SQLiteDatabase myDatabase;

    private String promoCardsTable = "promoCards_tbl";
    private String paymentsDetailsTable = "paymentDetails_tbl";
    private Cursor c = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo_card);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        txtPromoInfo = findViewById(R.id.pro_card_txt_info);
        spinPromoPayType = findViewById(R.id.pro_card_meth_type);

        ArrayAdapter<CharSequence> adapt_pay_type = ArrayAdapter.createFromResource(getApplicationContext(), R.array.cash_card, android.R.layout.simple_spinner_item);

        adapt_pay_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinPromoPayType.setAdapter(adapt_pay_type);

        btnPur10 = findViewById(R.id.pro_card_pur_10);
        btnPur20 = findViewById(R.id.pro_card_pur_20);
        btnPur1 = findViewById(R.id.pro_card_pur_1);

        btnPur10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPurchase10();
            }
        });

        btnPur20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPurchase20();
            }
        });

        btnPur1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPurchase1();
            }
        });

        Intent myIntent = getIntent();

        Bundle extras = myIntent.getExtras();

        userCode = extras.getString("CODE");
        userFirstName = extras.getString("FIRSTNAME");
        userLastName = extras.getString("LASTNAME");

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Promotional Cards");
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


        myDbHelper = new MySqlLiteOpenHelper(getApplicationContext());
        myDatabase = myDbHelper.getReadableDatabase();

        String checkForPromo = retrievePromoCode(promoCardsTable, getUserCode());

        if(checkForPromo.equals("-") == false){
            if(getPromoBalance(promoCardsTable, checkForPromo) <= 0){
                deletePromoCard(promoCardsTable, checkForPromo);
            } else {
                setPromoMsg(checkForPromo);
            }
        } else {
            setPromoMsg(checkForPromo);
        }

    }

    private void onPurchase1(){

        if(retrievePromoCode(promoCardsTable, getUserCode()).equals("-")){
            //prucahse
            AlertDialog.Builder confirm = new AlertDialog.Builder(this);
            confirm.setMessage("Purchase a Promo Card of a single session for R50?");
            confirm.setCancelable(false);

            confirm.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    SQLOperations oper = new SQLOperations(getApplicationContext(),"W");

                    if (getPayMethod().equals("Card") == true){
                        if(checkForExistingDetails(paymentsDetailsTable, getUserCode()) == true){
                            ContentValues values_new = new ContentValues();

                            values_new.put("Promo_ID", generatePromoID("01-"));
                            values_new.put("Guest_ID", getUserCode());
                            values_new.put("Type", "Promo-01");
                            values_new.put("Price", 50);
                            values_new.put("Session_Balance", 1);
                            values_new.put("Activated", 1);

                            oper.insertData(promoCardsTable, values_new);
                            setPromoMsg(retrievePromoCode(promoCardsTable, getUserCode()));
                        } else {
                            //MEssage you do not have pay details
                            Toast.makeText(getApplicationContext(), "You do not have any payment method set up yet. Please add a card to your account", Toast.LENGTH_LONG).show();
                        }

                    } else {
                        ContentValues values_new = new ContentValues();

                        values_new.put("Promo_ID", generatePromoID("01-"));
                        values_new.put("Guest_ID", getUserCode());
                        values_new.put("Type", "Promo-01");
                        values_new.put("Price", 50);
                        values_new.put("Session_Balance", 1);
                        values_new.put("Activated", 0);

                        oper.insertData(promoCardsTable, values_new);
                        setPromoMsg(retrievePromoCode(promoCardsTable, getUserCode()));
                    }
                }
            });

            confirm.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            confirm.create().show();
        } else {
            //You already have a card purchases
            Toast.makeText(getApplicationContext(), "You already own a promotional card.\n" + retrievePromoCode(promoCardsTable, getUserCode()), Toast.LENGTH_LONG).show();
        }
    }


    private void onPurchase10(){

            if(retrievePromoCode(promoCardsTable, getUserCode()).equals("-")){
                //prucahse
                AlertDialog.Builder confirm = new AlertDialog.Builder(this);
                confirm.setMessage("Purchase a Promo Card of 10 sessions for R400?");
                confirm.setCancelable(false);

                confirm.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        SQLOperations oper = new SQLOperations(getApplicationContext(),"W");

                        if (getPayMethod().equals("Card") == true){
                            if(checkForExistingDetails(paymentsDetailsTable, getUserCode()) == true){
                                ContentValues values_new = new ContentValues();

                                values_new.put("Promo_ID", generatePromoID("10-"));
                                values_new.put("Guest_ID", getUserCode());
                                values_new.put("Type", "Promo-10");
                                values_new.put("Price", 400);
                                values_new.put("Session_Balance", 10);
                                values_new.put("Activated", 1);

                                oper.insertData(promoCardsTable, values_new);
                                setPromoMsg(retrievePromoCode(promoCardsTable, getUserCode()));
                            } else {
                                //MEssage you do not have pay details
                                Toast.makeText(getApplicationContext(), "You do not have any payment method set up yet. Please add a card to your account", Toast.LENGTH_LONG).show();
                            }

                        } else {
                            ContentValues values_new = new ContentValues();

                            values_new.put("Promo_ID", generatePromoID("10-"));
                            values_new.put("Guest_ID", getUserCode());
                            values_new.put("Type", "Promo-10");
                            values_new.put("Price", 400);
                            values_new.put("Session_Balance", 10);
                            values_new.put("Activated", 0);

                            oper.insertData(promoCardsTable, values_new);
                            setPromoMsg(retrievePromoCode(promoCardsTable, getUserCode()));
                        }
                    }
                });

                confirm.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                confirm.create().show();
            } else {
                //You already have a card purchases
                Toast.makeText(getApplicationContext(), "You already own a promotional card.\n" + retrievePromoCode(promoCardsTable, getUserCode()), Toast.LENGTH_LONG).show();
            }
    }

    private void onPurchase20(){

            if(retrievePromoCode(promoCardsTable, getUserCode()).equals("-")){
                //prucahse

                AlertDialog.Builder confirm = new AlertDialog.Builder(this);
                confirm.setMessage("Purchase a Promo Card of 20 sessions for R650?");
                confirm.setCancelable(false);

                confirm.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        SQLOperations oper = new SQLOperations(getApplicationContext(),"W");

                        if (getPayMethod().equals("Card") == true){
                            if(checkForExistingDetails(paymentsDetailsTable, getUserCode()) == true){
                                ContentValues values_new = new ContentValues();

                                values_new.put("Promo_ID", generatePromoID("20-"));
                                values_new.put("Guest_ID", getUserCode());
                                values_new.put("Type", "Promo-20");
                                values_new.put("Price", 650);
                                values_new.put("Session_Balance", 20);
                                values_new.put("Activated", 1);

                                oper.insertData(promoCardsTable, values_new);
                                setPromoMsg(retrievePromoCode(promoCardsTable, getUserCode()));
                            } else {
                                //MEssage you do not have pay details
                                Toast.makeText(getApplicationContext(), "You do not have any payment method set up yet. Please add a card to your account", Toast.LENGTH_LONG).show();
                            }

                        } else {
                            ContentValues values_new = new ContentValues();

                            values_new.put("Promo_ID", generatePromoID("20-"));
                            values_new.put("Guest_ID", getUserCode());
                            values_new.put("Type", "Promo-20");
                            values_new.put("Price", 650);
                            values_new.put("Session_Balance", 20);
                            values_new.put("Activated", 0);

                            oper.insertData(promoCardsTable, values_new);
                            setPromoMsg(retrievePromoCode(promoCardsTable, getUserCode()));
                        }
                    }
                });

                confirm.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                confirm.create().show();
            } else {
                //You already have a card purchases
                Toast.makeText(getApplicationContext(), "You already own a promotional card.\n" + retrievePromoCode(promoCardsTable, getUserCode()), Toast.LENGTH_LONG).show();
            }

    }


    private String retrievePromoCode(String tableName, String userName){
        String promoCode = "-";
        c = myDatabase.query(tableName, null, "Guest_ID = ?", new String[] {userName}, null, null, null);

        if(c.moveToFirst()){
            promoCode = c.getString(0);
        }
        c.close();
        return promoCode;
    }


    private void setPromoMsg(String promoCode){
        if(promoCode.equals("-") == true){
            txtPromoInfo.setText("Loving the experience???\n\nPurchase a promotional card to get MORE classes at a LOWER price!");
        } else {

            if(checkIfActive(promoCardsTable, promoCode) == true){
                txtPromoInfo.setText("Your code: " + promoCode + "\nBalance: " + getPromoBalance(promoCardsTable, promoCode));
            } else {
                txtPromoInfo.setText("Your code: " + promoCode + " is not activated.\nPlease contact the studio owner to activate your promotional card.");
            }
        }
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

    private boolean checkIfActive(String tableName, String promoCode){
        c = myDatabase.query(tableName, null, "Promo_ID = ?", new String[] {promoCode}, null, null, null);

        if(c.moveToFirst()){
            if(c.getString(5).equals("1")){
                return true;
            }
        }

        c.close();
        return false;
    }

    private boolean checkForExistingCode(String tableName, String newPromoCode){
        c = myDatabase.query(tableName, null, "Promo_ID = ?", new String[] {newPromoCode}, null, null, null);

        if(c.moveToFirst()){
            return true;
        }

        c.close();
        return false;
    }

    public boolean checkForExistingDetails(String tableName, String code){

        c = myDatabase.query(tableName, null, null,null, null,null,null);

        if(c.moveToFirst()){
            do{
                if(c.getString(0).equals(code) == true){

                    return true;
                }
            } while (c.moveToNext());
        }

        c.close();

        return false;
    }

    private String generatePromoID(String prefix){
        String finalCode = prefix;
        String[] alphabet = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        Random rand = new Random();
        // Obtain a number between [0 - 25].
        int index;


        do {
            for(int k = 0; k < 5; k++){
                index = rand.nextInt(26);
                finalCode = finalCode + alphabet[index];
            }


        } while (checkForExistingCode(promoCardsTable, finalCode) == true);

        return finalCode;
    }

    private void deletePromoCard(String tableName, String promoCard){
        SQLOperations oper = new SQLOperations(getApplicationContext(), "W");
        oper.deleteData(tableName, "Promo_ID = ?", new String[] {promoCard});
    }

    public String getPayMethod(){
        return spinPromoPayType.getSelectedItem().toString();
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

