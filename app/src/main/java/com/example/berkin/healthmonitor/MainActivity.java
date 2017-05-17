package com.example.berkin.healthmonitor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import com.example.berkin.healthmonitor.Model.PersonModel;

import at.markushi.ui.CircleButton;

/**
 * Created by berkin on 09.05.2017.
 */

public class MainActivity extends AppCompatActivity {
    private EditText nameField;
    private EditText surnameField;
    private EditText ageField;
    private EditText eyeColorField;
    private CircleButton continueButton;
    private DatabaseHandler db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        db = initDB();
        nameField = (EditText) findViewById(R.id.name);
        surnameField = (EditText) findViewById(R.id.surname);
        ageField = (EditText) findViewById(R.id.age);
        eyeColorField = (EditText) findViewById(R.id.eye);
        continueButton = (CircleButton) findViewById(R.id.continueButton);

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendInfo(db);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.load_profile) {
            Intent intent = new Intent(MainActivity.this,LoadProfileActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.exit) {
            finishAffinity();
        }
        else if(id == R.id.about){
            Intent intent = new Intent(MainActivity.this,AboutActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private DatabaseHandler initDB(){
        return new DatabaseHandler(this);
    }

    boolean tryParseInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void sendInfo(DatabaseHandler db){
        Intent intent = new Intent(MainActivity.this,PersonActivity.class);

        String name = nameField.getText().toString();
        String surname = surnameField.getText().toString();
        boolean isInt = tryParseInt(ageField.getText().toString());
        int age = (isInt) ? Integer.parseInt(ageField.getText().toString()):0;
        String eyeColor = eyeColorField.getText().toString();

        PersonModel person = new PersonModel(name,surname,age,eyeColor);
        db.addContact(person);
        intent.putExtra("person", person);
        startActivity(intent);
    }
}