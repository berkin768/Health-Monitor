package com.example.berkin.healthmonitor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import com.example.berkin.healthmonitor.Model.OrganModel;
import com.example.berkin.healthmonitor.Model.PersonModel;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by berkin on 09.05.2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "healthMonitor.db";

    // Contacts table name
    private static final String TABLE_NAME = "Person";
    private static final String TABLE_NAME2 = "Body";
    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String BODY_KEY_ID = "bId";
    private static final String FOREIGN_KEY_ID = "personId";
    private static final String KEY_NAME = "name";
    private static final String KEY_ARM = "arm";
    private static final String KEY_BRAIN = "brain";
    private static final String KEY_EYE = "eye";
    private static final String KEY_HEART = "heart";
    private static final String KEY_LEG = "leg";
    private static final String KEY_SURNAME = "surname";
    private static final String KEY_AGE = "age";
    private static final String KEY_EYECOLOR = "eyeColor";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    private String createFirstTable(){
        String query = "CREATE TABLE " + TABLE_NAME + " (" +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_NAME + " TEXT, " +
                KEY_SURNAME + " TEXT, "+
                KEY_AGE + " INTEGER, " +
                KEY_EYECOLOR + " TEXT);";

        return query;
    }

    private String createSecondTable(){
        String query = "CREATE TABLE " + TABLE_NAME2 + " (" +
                BODY_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_ARM + " INTEGER, " +
                KEY_BRAIN + " INTEGER, "+
                KEY_EYE + " INTEGER, " +
                KEY_HEART + " INTEGER, "+
                KEY_LEG +" INTEGER, " +
                FOREIGN_KEY_ID + " INTEGER REFERENCES "+TABLE_NAME+ "("+KEY_ID+"));";
                //"FOREIGN KEY ("+ FOREIGN_KEY_ID + ") REFERENCES "+TABLE_NAME+ "("+KEY_ID+"));";

        return query;
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createFirstTable());
        db.execSQL(createSecondTable());
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + "," + TABLE_NAME2);
        // Create tables again
        onCreate(db);
    }

    boolean addContact(PersonModel person) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, person.getName()); // Person Name
        values.put(KEY_SURNAME, person.getSurname()); // Person Surname
        values.put(KEY_AGE, person.getAge()); // Person Age
        values.put(KEY_EYECOLOR, person.getEyeColor()); // Person Surname

        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        //db.close(); // Closing database connection
        return true;
    }

    boolean addBody(OrganModel organ, int foreignKey) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ARM, organ.getArmCondition()); // Person Name
        values.put(KEY_BRAIN, organ.getBrainCondition()); // Person Surname
        values.put(KEY_EYE, organ.getEyeCondition()); // Person Age
        values.put(KEY_HEART, organ.getHeartCondition()); // Person Surname
        values.put(KEY_LEG, organ.getLegCondition()); // Person Age
        values.put(FOREIGN_KEY_ID, foreignKey); // Person FK

        // Inserting Row
        db.insert(TABLE_NAME2, null, values);
        //db.close(); // Closing database connection
        return true;
    }

    boolean updateBody(OrganModel organ, int foreignKey) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("update " + TABLE_NAME2 + " set "+
                KEY_ARM +"="+ organ.getArmCondition() +", " +
                KEY_BRAIN +"="+ organ.getBrainCondition() + ", " +
                KEY_EYE +"="+ organ.getEyeCondition() + ", " +
                KEY_HEART +"="+ organ.getHeartCondition() + ", " +
                KEY_LEG +"="+ organ.getLegCondition() +
                " where " + FOREIGN_KEY_ID + "=" + foreignKey + ";", null);

        if (cursor != null)
            cursor.moveToFirst();
        return true;
    }

    public String getContactPkWithFullName(String name, String surname){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("select " + KEY_ID + " from " + TABLE_NAME + " where " + KEY_NAME + " = '" + name + "' and " +KEY_SURNAME + " = '" + surname + "';", null);
        if (cursor != null)
            cursor.moveToFirst();

        return cursor.getString(0);
    }


    public PersonModel getPersonWithPk(String pk){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where " + KEY_ID + "=" + pk + ";", null);
        PersonModel newPerson = null;
        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
            String surname = cursor.getString(cursor.getColumnIndex(KEY_SURNAME));
            String age = cursor.getString(cursor.getColumnIndex(KEY_AGE));
            String eyeColor = cursor.getString(cursor.getColumnIndex(KEY_EYECOLOR));


            newPerson = new PersonModel(name,surname,Integer.parseInt(age),eyeColor);
        }
        return newPerson;
    }

    public OrganModel getOrganWithPk(String pk){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME2 + " where " + FOREIGN_KEY_ID + "=" + pk + ";", null);
        OrganModel newOrgan = null;
        if (cursor.moveToFirst()) {
            String arm = cursor.getString(cursor.getColumnIndex(KEY_ARM));
            String brain = cursor.getString(cursor.getColumnIndex(KEY_BRAIN));
            String eye = cursor.getString(cursor.getColumnIndex(KEY_EYE));
            String heart = cursor.getString(cursor.getColumnIndex(KEY_HEART));
            String leg = cursor.getString(cursor.getColumnIndex(KEY_LEG));

            newOrgan = new OrganModel(Integer.parseInt(arm),Integer.parseInt(brain),Integer.parseInt(eye),Integer.parseInt(heart),Integer.parseInt(leg));
        }
        return newOrgan;
    }

    public List<PersonModel> getAllPerson() {

        List<PersonModel> allPerson = new LinkedList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
                String surname = cursor.getString(cursor.getColumnIndex(KEY_SURNAME));
                String age = cursor.getString(cursor.getColumnIndex(KEY_AGE));
                String eyeColor = cursor.getString(cursor.getColumnIndex(KEY_EYECOLOR));

                PersonModel newPerson = new PersonModel(name,surname,Integer.parseInt(age),eyeColor);
                allPerson.add(newPerson);
                cursor.moveToNext();
            }
        }

        return allPerson;
    }

    // Deleting single person
    public void deletePerson(String name,String surname) {
        SQLiteDatabase db = this.getWritableDatabase();
        String pk = getContactPkWithFullName(name,surname);
        Cursor deletePerson = db.rawQuery("delete from " + TABLE_NAME + " where " + KEY_NAME + " = '" + name + "' and " +KEY_SURNAME + " = '" + surname + "';", null);
        Cursor deleteOrgan = db.rawQuery("delete from " + TABLE_NAME2 + " where " + FOREIGN_KEY_ID + " = " + pk +";", null);

        deletePerson.moveToNext();
        deleteOrgan.moveToNext();
        db.close();
    }
}
