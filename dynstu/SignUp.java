package com.example.dynstu;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {

    private MySqlLiteOpenHelper myDbHelper;
    private SQLiteDatabase myDatabase;

    private EditText edtName;
    private EditText edtSurname;
    private EditText edtEmail;
    private EditText edtAge;

    private String userAccountsTable = "userAccountDetails_tbl";
    private String employeeDetailsTable = "employeeAccountDetails_tbl";

    private Cursor c = null;
    private Cursor d = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setTitle("Sign Up Screen (1/2)");

        edtName = (EditText) findViewById(R.id.edtName);
        edtSurname = (EditText) findViewById(R.id.edtSurname2);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtAge = (EditText) findViewById(R.id.edtAge);
    }

    public void onBack(View view) {
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(myIntent);
        finish();
    }

    public void onNext(View view) {

        if (checkForBlanks() == true){
            if (checkFields() == true) {
                if (emailExists(getEmail()) == false) {
                    //Proceed
                    Intent myIntent = new Intent(getApplicationContext(), SignUpPass.class);
                    Bundle extra = new Bundle();
                    extra.putString("NAME", getName());
                    extra.putString("SURNAME", getSurname());
                    extra.putString("EMAIL", getEmail());
                    extra.putString("AGE", getAge());
                    myIntent.putExtras(extra);
                    startActivity(myIntent);
                    finish();
                } else {
                    edtEmail.setError("Email already in use!");
                    //Toast.makeText(getApplicationContext(), "Email already in use!", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    private boolean checkFields(){
        if(validFirstName(getName())==false){
            //edtName.setBackgroundColor(Color.RED);
            return false;
        } else if(validLastName(getSurname())==false){
           // edtSurname.setBackgroundColor(Color.RED);
            return false;
        } else if (validEmail(getEmail()) == false){
            //edtEmail.setBackgroundColor(Color.RED);
            return false;
        } else if (validAge(getAge())==false){
           // edtAge.setBackgroundColor(Color.RED);
            return false;
        }
        return true;
    }

    private boolean checkForBlanks(){
        if(getName().equals("")==true){
            edtName.setError("Name cannot be blank!");
            //Toast.makeText(getApplicationContext(),"You left a field blank!",Toast.LENGTH_SHORT).show();
            return false;
        } else if(getSurname().equals("")==true){
            edtSurname.setError("Surname cannot be blank!");
            //Toast.makeText(getApplicationContext(),"You left a field blank!",Toast.LENGTH_SHORT).show();
            return false;
        } else if (getEmail().equals("")==true){
            edtEmail.setError("Email cannot be blank!");
            //Toast.makeText(getApplicationContext(),"You left a field blank!",Toast.LENGTH_SHORT).show();
            return false;
        } else if (getAge().equals("")==true){
            edtAge.setError("Age cannot be blank");
            //Toast.makeText(getApplicationContext(),"You left a field blank!",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean validEmail(String email){

        Pattern pattern = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

        Matcher matcher = pattern.matcher(email);

        if(matcher.matches() == false){
            edtEmail.setError("Incorrect email format\nExample: JoeSmith@gmail.com");
            //Toast.makeText(getApplicationContext(), "Incorrect email format\nExample: JoeSmith@gmail.com", Toast.LENGTH_LONG).show();
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
            edtName.setError("Name cannot have numbers!");
            //Toast.makeText(getApplicationContext(), "Name can not have numbers!", Toast.LENGTH_LONG).show();
            return false;
        } else if (hasSpecial.matches() == true){
            edtName.setError("Name cannot have special characters!");
            //Toast.makeText(getApplicationContext(), "Name can not have special characters!", Toast.LENGTH_LONG).show();
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
            edtSurname.setError("Surname cannot have numbers!");
            //Toast.makeText(getApplicationContext(), "Surname can not have numbers!", Toast.LENGTH_LONG).show();
            return false;
        } else if (hasSpecial.matches() == true){
            edtSurname.setError("Surname cannot have special characters!");
            //Toast.makeText(getApplicationContext(), "Surname can not have special characters!", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean validAge(String age){

        Pattern letter = Pattern.compile("[a-zA-z]");
        Pattern special = Pattern.compile ("[!@#$%&*()_+=|<>?{}\\[\\]~-]");

        Matcher hasLetter = letter.matcher(age);
        Matcher hasSpecial = special.matcher(age);

        if(hasLetter.matches() == true){
            edtAge.setError("Age cannot have letters!");
            //Toast.makeText(getApplicationContext(), "Age can not have letters!", Toast.LENGTH_LONG).show();
            return false;
        } else if (hasSpecial.matches() == true){
            edtAge.setError("Age cannot have special characters!");
            //Toast.makeText(getApplicationContext(), "Age cannot have special characters!", Toast.LENGTH_LONG).show();
            return false;
        } else if (Integer.parseInt(age) < 0){
            edtAge.setError("Age cannot be negative!");
            //Toast.makeText(getApplicationContext(), "Age can not be negative!", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }

    }

    private boolean emailExists(String email){
        boolean found = false;

        myDbHelper = new MySqlLiteOpenHelper(getApplicationContext());
        myDatabase = myDbHelper.getReadableDatabase();

        c = myDatabase.query(userAccountsTable, null, null, null, null, null, null);

        if(c.moveToFirst()){
            do{
                if(c.getString(4).equals(email)){
                    found = true;
                    return found;
                }
            } while (c.moveToNext());
        }

        c.close();

        d = myDatabase.query(employeeDetailsTable, null, null, null, null, null, null);

        if(d.moveToFirst()){
            do{
                if(d.getString(3).equals(email)){
                    found = true;
                    return found;
                }
            } while (d.moveToNext());
        }

        d.close();


        return found;
    }


    public String getName(){
        return edtName.getText().toString();
    }

    public String getSurname(){
        return edtSurname.getText().toString();
    }

    public String getEmail(){
        return edtEmail.getText().toString();
    }

    public String getAge(){
        return edtAge.getText().toString();
    }


}
