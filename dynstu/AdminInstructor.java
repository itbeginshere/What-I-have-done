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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdminInstructor extends AppCompatActivity{

    private String userCode;
    private String userFirstName;
    private String userLastName;

    private EditText edtEmpID;
    private EditText edtEmpFirstName;
    private EditText edtEmpLastName;
    private EditText edtEmpEmail;
    private EditText edtEmpPass;
    private EditText edtEmpConfirm;

    private Button btnEmpGet;
    private Button btnEmpAdd;
    private Button btnEmpUpdate;
    private Button btnEmpDelete;

    private MySqlLiteOpenHelper myDbHelper;
    private SQLiteDatabase myDatabase;

    private String employeeDetailsTable = "employeeAccountDetails_tbl";
    private Cursor c = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        edtEmpID = findViewById(R.id.admin_instruct_id);
        edtEmpFirstName = findViewById(R.id.admin_instruct_f_name);
        edtEmpLastName = findViewById(R.id.admin_instruct_l_name);
        edtEmpEmail = findViewById(R.id.admin_instruct_email);
        edtEmpPass = findViewById(R.id.admin_instruct_pass);
        edtEmpConfirm = findViewById(R.id.admin_instruct_pass_confirm);

        btnEmpGet = (Button) findViewById(R.id.admin_instruct_btn_get);
        btnEmpAdd = (Button) findViewById(R.id.admin_instruct_btn_add);
        btnEmpUpdate = (Button) findViewById(R.id.admin_instruct_btn_update);
        btnEmpDelete = (Button) findViewById(R.id.admin_instruct_btn_delete);

        btnEmpGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGet();
            }
        });

        btnEmpAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAdd();
            }
        });

        btnEmpUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUpdate();
            }
        });

        btnEmpDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDelete();
            }
        });

        Intent myIntent = getIntent();

        Bundle extras = myIntent.getExtras();

        userCode = extras.getString("CODE");
        userFirstName = extras.getString("FIRSTNAME");
        userLastName = extras.getString("LASTNAME");

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Modify Employees");
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


    public void onGet(){

        myDbHelper = new MySqlLiteOpenHelper(getApplicationContext());
        myDatabase = myDbHelper.getReadableDatabase();

        c = myDatabase.query(employeeDetailsTable, null, "Employee_ID = ?", new String[] {getEmployeeID()}, null, null, null);

        if(c.moveToFirst()){
            setEmployeeID(c.getString(0));
            setFirstName(c.getString(1));
            setLastName(c.getString(2));
            setEmail(c.getString(3));
            setPassword(c.getString(4));
            setConfrim(c.getString(4));
        } else {
           setFieldsToBlank();
           edtEmpID.setError("That Employee ID does not exist");
        }

        c.close();
    }

    public void onAdd(){

        SQLOperations oper = new SQLOperations(getApplicationContext(), "W");
        newCode gen = new newCode(getApplicationContext());
        String empNewCode =  gen.generateID("E", employeeDetailsTable);

        myDbHelper = new MySqlLiteOpenHelper(getApplicationContext());
        myDatabase = myDbHelper.getWritableDatabase();

        if(checkForBlanksAdd() == true){
            if(checkFieldsAdd() == true){
                if(emailExists(getEmail())== false){
                    ContentValues values_new = new ContentValues();
                    values_new.put("Employee_ID", empNewCode);
                    values_new.put("First_Name", getFirstName());
                    values_new.put("Last_Name", getLastName());
                    values_new.put("Email", getEmail());
                    values_new.put("Password", getPassword());
                    values_new.put("Active", 0);
                    oper.insertData(employeeDetailsTable, values_new);
                    Toast.makeText(getApplicationContext(), "The employee is now ready to get to work.", Toast.LENGTH_LONG).show();

                } else {
                    edtEmpEmail.setError("That email is already in use");
                }
            }
        } else {
            Toast.makeText(getApplicationContext(), "You have left field(s) blank", Toast.LENGTH_SHORT).show();
        }
    }

    public void onUpdate(){


        SQLOperations oper = new SQLOperations(getApplicationContext(), "W");

        myDbHelper = new MySqlLiteOpenHelper(getApplicationContext());
        myDatabase = myDbHelper.getWritableDatabase();

        if(checkForBlanksUpdate() == true){
            if(checkFieldsUpdate() == true){

                    ContentValues values_new = new ContentValues();
                    values_new.put("First_Name", getFirstName());
                    values_new.put("Last_Name", getLastName());
                    values_new.put("Email", getEmail());
                    values_new.put("Password", getPassword());
                    oper.updateData(employeeDetailsTable, values_new, "Employee_ID = ?" ,new String[] {getEmployeeID()});
                    Toast.makeText(getApplicationContext(), "The Employee details have been changed!", Toast.LENGTH_LONG).show();

            }
        } else {
        }

    }

    public void onDelete(){


        myDbHelper = new MySqlLiteOpenHelper(getApplicationContext());
        myDatabase = myDbHelper.getWritableDatabase();

        final AlertDialog.Builder confirm = new AlertDialog.Builder(this);
        confirm.setMessage("Are you sure that you want to delete the employee ID of " + getEmployeeID());
        confirm.setCancelable(false);

        confirm.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SQLOperations oper = new SQLOperations(getApplicationContext(), "W");

                ContentValues values_new = new ContentValues();

                values_new.put("Active", 1);

                oper.updateData(employeeDetailsTable, values_new, "Employee_ID = ?" ,new String[] {getEmployeeID()});
                Toast.makeText(getApplicationContext(), "The employee with the ID: " + getEmployeeID() + " is no longer allowed to be used.", Toast.LENGTH_LONG).show();
                setFieldsToBlank();
            }
        });

        confirm.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        confirm.create().show();
    }

    private void setFieldsToBlank(){
        setEmployeeID("");
        setFirstName("");
        setLastName("");
        setEmail("");
        setPassword("");
        setConfrim("");
    }


    private boolean checkForBlanksAdd(){
        if(getFirstName().equals("")==true){
            edtEmpFirstName.setError("The Name cannot be blank!");
            return false;
        } else if (getLastName().equals("")==true){
            edtEmpLastName.setError("The Surname cannot be blank!");
            return false;
        } else if (getEmail().equals("")==true){
            edtEmpEmail.setError("The Email cannot be blank!");
            return false;
        } else if (getPassword().equals("")==true){
            edtEmpPass.setError("The Password cannot be blank!");
            return false;
        } else if (getConfrim().equals("")==true){
            edtEmpConfirm.setError("The Confirm Password cannot be blank!");
            return false;
        }

        return true;
    }

    private boolean checkForBlanksUpdate(){
        if(getFirstName().equals("")==true){
            edtEmpFirstName.setError("The Name cannot be blank!");
            return false;
        } else if (getLastName().equals("")==true){
            edtEmpLastName.setError("The Surname cannot be blank!");
            return false;
        } else if (getEmail().equals("")==true){
            edtEmpEmail.setError("The Email cannot be blank!");
            return false;
        } else if (getPassword().equals("")==true){
            edtEmpPass.setError("The Password cannot be blank!");
            return false;
        } else if (getConfrim().equals("")==true){
            edtEmpConfirm.setError("The Confirm Password cannot be blank!");
            return false;
        } else if (getEmployeeID().equals("")==true){
            edtEmpID.setError("The Employee ID cannot be blank!");
            return false;
        }

        return true;
    }

    private boolean checkFieldsAdd(){
        if(validFirstName(getFirstName())==false){
            return false;
        } else if (validLastName(getLastName())==false) {
            return false;
        } else if (validEmail(getEmail())==false) {
            return false;
        } else if (validPass(getPassword())==false) {
            return false;
        } else if (passMatch(getPassword(), getConfrim())==false) {
            return false;
        }
        return true;
    }

    private boolean checkFieldsUpdate(){
        if(validFirstName(getFirstName())==false){
            return false;
        } else if (validLastName(getLastName())==false) {
            return false;
        } else if (validEmail(getEmail())==false) {
            return false;
        } else if (validPass(getPassword())==false) {
            return false;
        } else if (validEmpID(getEmployeeID())==false) {
            return false;
        }else if (passMatch(getPassword(), getConfrim())==false) {
            return false;
        }
        return true;
    }

    private boolean validEmpID(String empID){

        c = myDatabase.query(employeeDetailsTable, null, null, null, null, null, null);

        if(c.moveToFirst()){
            do{
                if(c.getString(0).equals(empID)){
                    return true;
                }
            } while (c.moveToNext());
        } else {
            edtEmpID.setError("That Employee ID does not exist");
            return false;
        }

        c.close();

        return false;
    }

    private boolean validEmail(String email){

        Pattern pattern = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

        Matcher matcher = pattern.matcher(email);

        if(matcher.matches() == false){
            edtEmpEmail.setError("Incorrect email format\nExample: JoeSmith@gmail.com");
            return false;
        } else {
            return true;
        }
    }

    private boolean validFirstName(String name){

        Pattern special = Pattern.compile ("[!@#$%&*()_+=|<>?{}\\[\\]~-]");
        Pattern digit = Pattern.compile("[0-9]");

        Matcher hasSpecial = special.matcher(name);
        Matcher hasDigit = digit.matcher(name);

        if(hasDigit.matches() == true){
            edtEmpFirstName.setError("Name cannot have numbers!");
            return false;
        } else if (hasSpecial.matches() == true){
            edtEmpFirstName.setError("Name cannot have special characters!");
            return false;
        } else {
            return true;
        }
    }

    private boolean validLastName(String surname){

        Pattern special = Pattern.compile ("[!@#$%&*()_+=|<>?{}\\[\\]~-]");
        Pattern digit = Pattern.compile("[0-9]");

        Matcher hasSpecial = special.matcher(surname);
        Matcher hasDigit = digit.matcher(surname);

        if(hasDigit.matches() == true){
            edtEmpLastName.setError("Surname cannot have numbers!");
            return false;
        } else if (hasSpecial.matches() == true){
            edtEmpLastName.setError("Surname cannot have special characters!");
            return false;
        } else {
            return true;
        }
    }

    private boolean validPass(String pass){
        if(pass.length()<8){
            edtEmpPass.setError("Your pasword must be longer then 8 characters");
            return false;
        }
        return true;
    }

    private boolean passMatch(String pass, String confirm){
        if(pass.equals(confirm)==false){
            edtEmpConfirm.setError("The passwords do not match");
            return false;
        }
        return true;
    }

    private boolean emailExists(String email){
        boolean found = false;

        myDbHelper = new MySqlLiteOpenHelper(getApplicationContext());
        myDatabase = myDbHelper.getReadableDatabase();

        c = myDatabase.query(employeeDetailsTable, null, null, null, null, null, null);

        if(c.moveToFirst()){
            do{
                if(c.getString(3).equals(email)){
                    found = true;
                    return found;
                }
            } while (c.moveToNext());
        }

        return found;
    }

    private String getEmployeeID() {
        return edtEmpID.getText().toString();
    }

    private String getFirstName(){
        return edtEmpFirstName.getText().toString();
    }

    private String getLastName(){
        return edtEmpLastName.getText().toString();
    }

    private String getEmail(){
        return edtEmpEmail.getText().toString();
    }

    private String getPassword(){
        return edtEmpPass.getText().toString();
    }

    private String getConfrim(){
        return edtEmpConfirm.getText().toString();
    }

    private void setEmployeeID(String value) {
        edtEmpID.setText(value);
    }

    private void setFirstName(String value){
        edtEmpFirstName.setText(value);
    }

    private void setLastName(String value){
        edtEmpLastName.setText(value);
    }

    private void setEmail(String value){
        edtEmpEmail.setText(value);
    }

    private void setPassword(String value){
        edtEmpPass.setText(value);
    }

    private void setConfrim(String value){
        edtEmpConfirm.setText(value);
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

