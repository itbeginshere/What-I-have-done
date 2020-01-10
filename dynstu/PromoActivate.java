package com.example.dynstu;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PromoActivate extends AppCompatActivity {

    private String userCode;
    private String userFirstName;
    private String userLastName;

    private EditText edtPromoCode;

    private Button btnPromoActivate;

    private MySqlLiteOpenHelper myDbHelper;
    private SQLiteDatabase myDatabase;

    private String promoCardsTable = "promoCards_tbl";

    private Cursor c = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo_activate);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        edtPromoCode = findViewById(R.id.pro_act_card_code);
        btnPromoActivate = findViewById(R.id.pro_act_btn_activate);

        btnPromoActivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onActivate();
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
        mToolbar.setTitle("Code Activation");
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

        setFieldsToBlank();
    }

    private void onActivate(){

        if(checkForBlanks() == true){
            if(checkIfCodeExists(promoCardsTable, getPromoCode()) == true){
                if(getPromoCardActive(promoCardsTable, getPromoCode()).equals("0")){
                    SQLOperations oper = new SQLOperations(getApplicationContext(), "W");

                    ContentValues values_new = new ContentValues();

                    values_new.put("Activated", 1);

                    oper.updateData(promoCardsTable, values_new,"Promo_ID = ?" ,new String[] {getPromoCode()});

                    Toast.makeText(getApplicationContext(), "The promotional card code has been activated.", Toast.LENGTH_LONG).show();

                } else {
                    edtPromoCode.setError("Invalid Code");
                    Toast.makeText(getApplicationContext(), "That promotional card code is already activated.", Toast.LENGTH_LONG).show();
                }
            } else {
                edtPromoCode.setError("Invalid Code");
               Toast.makeText(getApplicationContext(), "That promotional card code does not exist.", Toast.LENGTH_LONG).show();
            }
        } else {
            edtPromoCode.setError("Invalid Code");
            Toast.makeText(getApplicationContext(), "You forgot to enter a promotional code!", Toast.LENGTH_LONG).show();
        }

    }

    private boolean checkForBlanks(){
        if(getPromoCode().equals("") == true){
            return false;
        }
        return true;
    }

    private void setFieldsToBlank(){
        edtPromoCode.setText("");
    }

    private boolean checkIfCodeExists(String tableName, String code){

        c = myDatabase.query(tableName, null, null, null, null, null,null);

        if(c.moveToFirst()){
          do{
              if(c.getString(0).equals(code)){
                  return true;
              }

          } while (c.moveToNext());
        }

        c.close();

        return false;
    }

    private String getPromoCardActive(String tableName, String code){
       String activated;

       c = myDatabase.query(tableName, new String[] {"Activated"}, "Promo_ID = ?", new String[] {code}, null, null, null);

       if(c.moveToFirst()){
           activated = c.getString(0);
       } else {
           activated = "0";
       }

       c.close();

       return activated;
    }

    public String getPromoCode(){
        return edtPromoCode.getText().toString();
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
