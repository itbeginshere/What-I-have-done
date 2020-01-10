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
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreditCard extends AppCompatActivity{

        private String userCode;
        private String userFirstName;
        private String userLastName;

        private EditText edtCardName;
        private EditText edtCardNumber;
        private EditText edtCardCode;

        private Spinner spinCardExpYear;
        private Spinner spinCardExpMonth;

        private Button btnCardAdd;
        private Button btnCardGet;
        private Button btnCardUpdate;
        private Button btnCardDelete;

        private MySqlLiteOpenHelper myDbHelper;
        private SQLiteDatabase myDatabase;

        private String paymentsDetailsTable = "paymentDetails_tbl";

        private Cursor c = null;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_creditcard);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                edtCardName = findViewById(R.id.pay_meth_name);
                edtCardNumber = findViewById(R.id.pay_meth_number);
                edtCardCode = findViewById(R.id.pay_meth_code);

                spinCardExpMonth = findViewById(R.id.pay_meth_exp_month);
                spinCardExpYear = findViewById(R.id.pay_meth_exp_year);

                ArrayAdapter<CharSequence> adapt_exp_month = ArrayAdapter.createFromResource(getApplicationContext(), R.array.expiry_month, android.R.layout.simple_spinner_item);
                ArrayAdapter<CharSequence> adapt_exp_year = ArrayAdapter.createFromResource(getApplicationContext(), R.array.expiry_year, android.R.layout.simple_spinner_item);

                adapt_exp_month.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                adapt_exp_year.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinCardExpMonth.setAdapter(adapt_exp_month);
                spinCardExpYear.setAdapter(adapt_exp_year);

                btnCardGet = findViewById(R.id.pay_meth_btn_get);
                btnCardAdd = findViewById(R.id.pay_meth_btn_add);
                btnCardUpdate = findViewById(R.id.pay_meth_btn_update);
                btnCardDelete = findViewById(R.id.pay_meth_btn_delete);

                btnCardGet.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                onGet();
                        }
                });

                btnCardAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                onAdd();
                        }
                });

                btnCardUpdate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                onUpdate();
                        }
                });

                btnCardDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                onRemove();
                        }
                });


                Intent myIntent = getIntent();

                Bundle extras = myIntent.getExtras();

                userCode = extras.getString("CODE");
                userFirstName = extras.getString("FIRSTNAME");
                userLastName = extras.getString("LASTNAME");

                Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
                mToolbar.setTitle("Payment Method");
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


                onGet();
        }

        public void onGet(){

                myDbHelper = new MySqlLiteOpenHelper(getApplicationContext());
                myDatabase = myDbHelper.getReadableDatabase();

                setData(paymentsDetailsTable, getUserCode());
        }

        public void onAdd(){

                SQLOperations oper = new SQLOperations(getApplicationContext(), "W");

                myDbHelper = new MySqlLiteOpenHelper(getApplicationContext());
                myDatabase = myDbHelper.getWritableDatabase();

                if(checkForBlanks() == true){
                        if(checkFields() == true){
                                if(checkForExistingDetails(paymentsDetailsTable, getUserCode()) == true){

                                        ContentValues values_new = new ContentValues();
                                        values_new.put("Guest_ID", getUserCode());
                                        values_new.put("Card_Name", getCardName());
                                        values_new.put("Card_Number", getCardNumber());
                                        values_new.put("Card_code", getCardCode());
                                        values_new.put("Exp_Year", getCardExpYear());
                                        values_new.put("Exp_Month", getCardExpMonth());

                                        oper.insertData(paymentsDetailsTable, values_new);
                                        Toast.makeText(getApplicationContext(), "You have succesfully added a payment method.", Toast.LENGTH_LONG).show();
                                } else {
                                        Toast.makeText(getApplicationContext(), "You already have a payment method!", Toast.LENGTH_LONG).show();
                                }
                        }
                } else {
                }
        }

        public void onUpdate(){

                SQLOperations oper = new SQLOperations(getApplicationContext(), "W");

                myDbHelper = new MySqlLiteOpenHelper(getApplicationContext());
                myDatabase = myDbHelper.getWritableDatabase();

                if(checkForBlanks() == true){
                        if(checkFields() == true){
                                if(checkForExistingDetails(paymentsDetailsTable, getUserCode()) == false){

                                        ContentValues values_new = new ContentValues();
                                        values_new.put("Guest_ID", getUserCode());
                                        values_new.put("Card_Name", getCardName());
                                        values_new.put("Card_Number", getCardNumber());
                                        values_new.put("Card_code", getCardCode());
                                        values_new.put("Exp_Year", getCardExpYear());
                                        values_new.put("Exp_Month", getCardExpMonth());

                                        oper.updateData(paymentsDetailsTable, values_new,"Guest_ID = ?" ,new String[] {getUserCode()});

                                        Toast.makeText(getApplicationContext(), "Your payment method has been changed successfully", Toast.LENGTH_LONG).show();
                                } else {
                                        Toast.makeText(getApplicationContext(), "You do not have a payment method!", Toast.LENGTH_LONG).show();
                                }
                        }
                } else {
                }
        }

        public void onRemove(){
                myDbHelper = new MySqlLiteOpenHelper(getApplicationContext());
                myDatabase = myDbHelper.getWritableDatabase();

                final AlertDialog.Builder confirm = new AlertDialog.Builder(this);
                confirm.setMessage("Are you sure you want to DELETE  your current payment method?");
                confirm.setCancelable(false);

                confirm.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                                SQLOperations oper = new SQLOperations(getApplicationContext(), "W");
                                oper.deleteData(paymentsDetailsTable,"Guest_ID = ?" ,new String[] {getUserCode()});
                                Toast.makeText(getApplicationContext(), "You have deleted your payment method.", Toast.LENGTH_LONG).show();
                                setData(paymentsDetailsTable, getUserCode());
                        }
                });

                confirm.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                });
                confirm.create().show();
        }

        private void setData(String tableName, String code){
                c = myDatabase.query(tableName, null, "Guest_ID = ?", new String[] {code} ,null, null, null);

                if(c.moveToFirst()){
                        setCardName(c.getString(1));
                        setCardNumber(c.getString(2));
                        setCardCode(c.getString(3));
                        setExpYear(c.getString(4));
                        setExpMonth(c.getString(5));

                } else {
                        setCardName("");
                        setCardNumber("");
                        setCardCode("");
                        setExpMonthDefaul();
                        setExpYearDefault();
                }

                c.close();
        }

        private boolean isNumber(String potentailNumber){

                try{
                        double  value = Double.parseDouble(potentailNumber);
                } catch(NumberFormatException er) {
                        return false;
                }
                return true;
        }


        private boolean checkForBlanks(){
                if(getCardName().equals("") == true){
                        edtCardName.setError("The Card Name cannot be left blank!");
                        return false;
                } else if(getCardNumber().equals("") == true){
                        edtCardNumber.setError("The Card Number cannot be left blank!");
                        return false;
                } else if (getCardCode().equals("") == true){
                        edtCardCode.setError("The Card Security Code cannot be left blank!");
                        return false;
                } else if(getCardExpYear().equals("") == true){
                        TextView errorText = (TextView)spinCardExpYear.getSelectedView();
                        errorText.setError("Expiry Year cannot be blank!");
                        errorText.setTextColor(Color.RED);
                        return false;
                } else if (getCardExpMonth().equals("") == true){
                        TextView errorText = (TextView)spinCardExpMonth.getSelectedView();
                        errorText.setError("Expiry Month cannot be blank!");
                        errorText.setTextColor(Color.RED);
                        return false;
                }
                return true;
        }

        private boolean checkFields(){

                if(validCardName(getCardName()) == false){
                        return false;
                } else if (validCardNum(getCardNumber()) == false){
                        return false;
                } else if (validCardCode(getCardCode()) == false){
                        return false;
                } else if (validExpYear(getCardExpYear()) == false){
                        return false;
                } else if (validExpMonth(getCardExpMonth()) == false){
                        return false;
                }

                return true;
        }

        private boolean checkForExistingDetails(String tableName, String code){

                c = myDatabase.query(tableName, null, "Guest_ID = ?",new String[] {code}, null,null,null);

                if(c.moveToFirst()){
                        do{
                                if(c.getString(0).equals(getUserCode()) == true){

                                        return false;
                                }
                        } while (c.moveToNext());
                }

                c.close();

                return true;
        }

        private boolean validCardName(String cardName){

                Pattern pattern = Pattern.compile("[0-9a-zA-Z]+");

                Matcher matcher = pattern.matcher(cardName);

                if(matcher.matches() == false){
                        edtCardName.setError("A Card Name cannot have special characters!");
                        return false;
                }

                return true;
        }

        private boolean validCardNum(String cardNumber){

                String tempBuilder = cardNumber;
                String tempSection = "";
                int pos = tempBuilder.indexOf(" ");
                int length = tempBuilder.length();
                int validCount = 0;

                if(length != 19) {
                        edtCardNumber.setError("Invalid Card Number");
                        Toast.makeText(getApplicationContext(), "Invalid card number. Example XXXX XXXX XXXX XXXX", Toast.LENGTH_LONG).show();
                        return false;
                }

                while (pos != -1){

                        pos = tempBuilder.indexOf(" ");
                        validCount++;
                        if(pos == -1){
                                tempSection = tempBuilder.substring(0, 4);
                        } else {
                                tempSection = tempBuilder.substring(0, pos);
                        }

                        if(isNumber(tempSection) == false){
                                edtCardNumber.setError("Invalid Card Number");
                                Toast.makeText(getApplicationContext(), "Invalid card number. Example XXXX XXXX XXXX XXXX", Toast.LENGTH_LONG).show();
                                return false;
                        }
                        length = tempBuilder.length();
                        tempBuilder = tempBuilder.substring(pos+1, length);


                }

                if(validCount != 4){
                        edtCardNumber.setError("Invalid Card Number");
                        Toast.makeText(getApplicationContext(), "Invalid card number. Example XXXX XXXX XXXX XXXX", Toast.LENGTH_LONG).show();
                        return false;
                }

                return true;
        }

        public boolean validCardCode(String cardCode){

                if(isNumber(cardCode) == false){
                        edtCardCode.setError("A Card Code must only have numbers!");
                        return false;
                } else if (cardCode.length() != 3){
                        edtCardCode.setError("A Card Code must be three numbers in length!");
                        return false;
                }
                return true;
        }

        public boolean validExpYear(String expYear){

                if(isNumber(expYear) == false){
                        TextView errorText = (TextView)spinCardExpYear.getSelectedView();
                        errorText.setError("");
                        errorText.setTextColor(Color.RED);
                        Toast.makeText(getApplicationContext(), "The Expiry Year of the card must only have numbers!", Toast.LENGTH_LONG).show();
                        return false;
                } else {
                        return true;
                }
        }

        public boolean validExpMonth(String expMonth){

                if(isNumber(expMonth) == false){
                        TextView errorText = (TextView)spinCardExpMonth.getSelectedView();
                        errorText.setError("");
                        errorText.setTextColor(Color.RED);
                        Toast.makeText(getApplicationContext(), "The Expiry Month of the card must only have numbers!", Toast.LENGTH_LONG).show();
                        return false;
                } else {
                        return true;
                }
        }

        public String getCardName(){
                return edtCardName.getText().toString();
        }

        public String getCardNumber(){
                return edtCardNumber.getText().toString();
        }

        public String getCardCode(){
                return edtCardCode.getText().toString();
        }

        public String getCardExpYear(){
                return spinCardExpYear.getSelectedItem().toString();
        }

        public String getCardExpMonth(){
                return spinCardExpMonth.getSelectedItem().toString();
        }

        public void setCardName(String value){
                edtCardName.setText(value);
        }

        public void setCardNumber(String value){
                edtCardNumber.setText(value);
        }

        public void setCardCode(String value){
                edtCardCode.setText(value);
        }

        public void setExpYear(String value){

                for(int k = 0; k < spinCardExpYear.getCount(); k++) {
                        if (spinCardExpYear.getItemAtPosition(k).equals(value)) {
                                spinCardExpYear.setSelection(k);
                                return;
                        }
                }
        }

        public void setExpMonth(String value){

                for(int k = 0; k < spinCardExpMonth.getCount(); k++) {
                        if (spinCardExpMonth.getItemAtPosition(k).equals(value)) {
                                spinCardExpMonth.setSelection(k);
                                return;
                        }
                }
        }

        public void setExpYearDefault(){
                spinCardExpYear.setSelection(0);
        }

        public void setExpMonthDefaul(){
                spinCardExpMonth.setSelection(0);
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
