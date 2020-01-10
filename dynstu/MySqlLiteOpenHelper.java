package com.example.dynstu;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MySqlLiteOpenHelper extends SQLiteOpenHelper{

    private String DB_PATH = null;
    private static String DB_NAME = "dynamic_studio";
    private SQLiteDatabase myDataBase;
    private final Context myContext;

    public MySqlLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, 10);
        this.myContext = context;
        this.DB_PATH = "/data/data/" + context.getPackageName() + "/" + "databases/";
    }


    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if (dbExist) {
        } else {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error(e.getMessage() + "Error copying database");
            }
        }
    }

    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }

    private void copyDataBase() throws IOException {
        InputStream myInput = myContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[10];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException {
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

    @Override
    public synchronized void close() {
        if (myDataBase != null)
            myDataBase.close();
        super.close();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {


        try{
            String dropTableOne = "DROP TABLE userAccountDetails_tbl";
            String dropTableTwo = "DROP TABLE employeeAccountDetails_tbl";
            String dropTableThree = "DROP TABLE classSession_tbl";
            String dropTableFour = "DROP TABLE classSessionMembers_tbl";
            String dropTableFive = "DROP TABLE paymentDetails_tbl";
            String dropTableSix = "DROP TABLE promoCards_tbl";
            String dropTableSeven = "DROP TABLE privateClassRequest_tbl";
            String dropTableEight = "DROP TABLE notifMessage_tbl";
            String dropTableNine = "DROP TABLE attendance_tbl";

            db.execSQL(dropTableOne);
            db.execSQL(dropTableTwo);
            db.execSQL(dropTableThree);
            db.execSQL(dropTableFour);
            db.execSQL(dropTableFive);
            db.execSQL(dropTableSix);
            db.execSQL(dropTableSeven);
            db.execSQL(dropTableEight);
            db.execSQL(dropTableNine);

        } catch (Exception ex){
            Log.e("ERROR ERRO2R ERROR ",ex.getMessage());
        }



        try {

            String userAccountDetails_tbl = "CREATE TABLE userAccountDetails_tbl(Guest_ID TEXT NOT NULL, First_Name TEXT NOT NULL, Last_Name TEXT NOT NULL, Age INTEGER NOT NULL, Email TEXT NOT NULL, Password TEXT NOT NULL, Active INTEGER(1) NOT NULL, PRIMARY KEY(Guest_ID));";
            String employeeAccountDetails_tbl = "CREATE TABLE employeeAccountDetails_tbl(Employee_ID TEXT NOT NULL, First_Name TEXT NOT NULL, Last_Name TEXT NOT NULL, Email TEXT NOT NULL, Password TEXT NOT NULL, Active INTEGER(1) NOT NULL, PRIMARY KEY(Employee_ID))";
            String classSession_tbl = "CREATE TABLE classSession_tbl(Class_ID TEXT NOT NULL, Employee_ID TEXT NOT NULL, Capacity INT NOT NULL, Type TEXT NOT NULL, Date TEXT NOT NULL, Time_Start TEXT NOT NULL, Time_End TEXT NOT NULL, Venue TEXT NOT NULL, Private BOOLEAN NOT NULL, Price DECIMAL(8,2) NOT NULL, Equipment TEXT NOT NULL, Complete INTEGER(1) NOT NULL, Cancelled INTEGER(1) NOT NULL, PRIMARY KEY(Class_ID));";
            String classSessionMembers_tbl = "CREATE TABLE classSessionMembers_tbl(Class_ID TEXT NOT NULL, Guest_ID TEXT NOT NULL, PRIMARY KEY(Class_ID, Guest_ID));";
            String paymentDetails_tbl = "CREATE TABLE paymentDetails_tbl(Guest_ID TEXT NOT NULL, Card_Name TEXT NOT NULL, Card_Number TEXT NOT NULL, Card_Code TEXT NOT NULL, Exp_Year TEXT NOT NULL, Exp_Month TEXT NOT NULL, PRIMARY KEY (Guest_ID))";
            String promoCards_tbl = "CREATE TABLE promoCards_tbl(Promo_ID TEXT NOT NULL, Guest_ID TEXT NOT NULL, Type TEXT NOT NULL, Price DECIMAL(8,2) NOT NULL, Session_Balance INTEGER NOT NULL, Activated INTEGER NOT NULL, PRIMARY KEY(Promo_ID))";
            String privateClassRequest_tbl = "CREATE TABLE privateClassRequest_tbl(Request_ID TEXT NOT NULL, Employee_ID TEXT NOT NULL, Capacity INTEGER NOT NULL, Start_Time TEXT NOT NULL, End_Time TEXT NOT NULL, Date TEXT NOT NULL, Guest_ID TEXT NOT NULL, PRIMARY KEY (Request_ID));";
            String notifMessage_tbl = "CREATE TABLE notifMessage_tbl(Notif_ID TEXT NOT NULL, Guest_ID TEXT NOT NULL, Message NOT NULL, Read INTEGER(1) NOT NULL, PRIMARY KEY(Notif_ID));";
            String attendance_tbl = "CREATE TABLE attendance_tbl(Class_ID TEXT NOT NULL, Guest_ID TEXT NOT NULL, Present INTEGER(1), PRIMARY KEY (Class_ID, Guest_ID));";

            db.execSQL(userAccountDetails_tbl);
            db.execSQL(employeeAccountDetails_tbl);
            db.execSQL(classSession_tbl);
            db.execSQL(classSessionMembers_tbl);
            db.execSQL(paymentDetails_tbl);
            db.execSQL(promoCards_tbl);
            db.execSQL(privateClassRequest_tbl);
            db.execSQL(notifMessage_tbl);
            db.execSQL(attendance_tbl);

        } catch (Exception ex){
            Log.e("ERROR ER3ROR ERROR ",ex.getMessage());
        }

        //Accounts
        String addEmp = "INSERT INTO employeeAccountDetails_tbl VALUES('E0000001', 'Tyrone', 'Magnus', 'tyrone@gmail.com', '12345678', 0)";
        String addAdmin = "INSERT INTO employeeAccountDetails_tbl VALUES('A0000001', 'Vicky', 'Luff', 'Admin@gmail.com', '12345678', 0)";
        String addUserOne = "INSERT INTO userAccountDetails_tbl VALUES('G0000001', 'Tom', 'Jones', 17, 'testone@gmail.com', '12345678', 1)";
        String addUserTwo = "INSERT INTO userAccountDetails_tbl VALUES('G0000002', 'Lavell', 'Carwford', 18, 'testtwo@gmail.com', '12345678', 1)";
        //Classes Normal
        String addClassOne = "INSERT INTO classSession_tbl VALUES('C0000001', 'E0000001', 30, 'Pound', '06/11/2019', '10:00', '11:00', 'Dynamic Body Studio', 0, 50, 'Equipment', 0, 0)";
        String addClassTwo = "INSERT INTO classSession_tbl VALUES('C0000002', 'E0000001', 1, 'Rhythm Pilates', '06/11/2019', '12:00', '14:00', 'Dynamic Body Studio', 0, 50, 'Equipment', 0, 0)";
        String addClassThree = "INSERT INTO classSession_tbl VALUES('C0000003', 'E0000001', 20, 'Barre Time', '06/11/2019', '18:00', '20:00', 'Dynamic Body Studio', 0, 50, 'None', 0, 0)";
        String addClassFour = "INSERT INTO classSession_tbl VALUES('C0000004', 'E0000001', 30, 'Pound', '07/11/2019', '10:00', '11:00', 'Dynamic Body Studio', 0, 50, 'Equipment', 0, 0)";
        String addClassFive = "INSERT INTO classSession_tbl VALUES('C0000005', 'A0000001', 20, 'Barre Time', '07/11/2019', '18:00', '20:00', 'Dynamic Body Studio', 0, 50, 'None', 0, 0)";
        String addClassSix = "INSERT INTO classSession_tbl VALUES('C0000006', 'A0000001', 20, 'Zumba', '07/11/2019', '12:00', '14:00', 'Dynamic Body Studio', 0, 50, 'None', 0, 0)";
        String addClassSeven = "INSERT INTO classSession_tbl VALUES('C0000007', 'A0000001', 10, 'Ballet Rip', '08/11/2019', '10:00', '11:00', 'Dynamic Body Studio', 0, 50, 'Mat', 0, 0)";
        String addClassEight = "INSERT INTO classSession_tbl VALUES('C0000008', 'A0000001', 10, 'Ballet Rip', '08/11/2019', '13:00', '15:00', 'Dynamic Body Studio', 0, 50, 'Mat', 0, 0)";
        //Classes Private
        String addReqOne = "INSERT INTO privateClassRequest_tbl VALUES('R0000001','E0000001', 2,'08:00','10:00','06/11/2019','G0000001')";
        String addReqTwo = "INSERT INTO privateClassRequest_tbl VALUES('R0000002','A0000001', 1,'12:00','13:00','06/11/2019','G0000002')";
        //Payment Details
        String addPayment = "INSERT INTO paymentDetails_tbl VALUES('G0000002','CrawFord Card','0123 1456 7895 4125','014','2025','01')";
        //Promo Card
        String addPromoCard = "INSERT INTO promoCards_tbl VALUES('20-ECIGY','G0000002','Promo-20', 650, 19, 1)";
        //Joined in Class
        String addClassMember = "INSERT INTO classSessionMembers_tbl VALUES('C0000002', 'G0000002')";
        try {
            db.execSQL(addEmp);
            db.execSQL(addAdmin);
            db.execSQL(addUserOne);
            db.execSQL(addUserTwo);

            db.execSQL(addClassOne);
            db.execSQL(addClassTwo);
            db.execSQL(addClassThree);
            db.execSQL(addClassFour);
            db.execSQL(addClassFive);
            db.execSQL(addClassSix);
            db.execSQL(addClassSeven);
            db.execSQL(addClassEight);

            db.execSQL(addReqOne);
            db.execSQL(addReqTwo);

            db.execSQL(addPayment);

            db.execSQL(addPromoCard);

            db.execSQL(addClassMember);
        } catch (Exception ex){
            Log.e("ERROR ERR46OR ERROR ",ex.getMessage());
        }




    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion)
            try {
                copyDataBase();
            } catch (IOException e) {
                e.printStackTrace();

            }
    }

    public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        return myDataBase.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
    }


}
