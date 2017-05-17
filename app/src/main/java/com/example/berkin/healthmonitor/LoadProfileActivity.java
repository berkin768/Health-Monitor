package com.example.berkin.healthmonitor;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.berkin.healthmonitor.Body.Arm;
import com.example.berkin.healthmonitor.Body.Brain;
import com.example.berkin.healthmonitor.Body.Eye;
import com.example.berkin.healthmonitor.Body.Heart;
import com.example.berkin.healthmonitor.Body.Leg;
import com.example.berkin.healthmonitor.Model.OrganModel;
import com.example.berkin.healthmonitor.Model.PersonModel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by berkin on 15.05.2017.
 */

public class LoadProfileActivity extends AppCompatActivity{

    private DatabaseHandler db;
    private List<PersonModel> people = new LinkedList();
    private ListView user_list;
    private TextView meta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_profile_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        meta = (TextView) findViewById(R.id.meta);
        db = initDB();
        people = getProfiles();
        user_list = (ListView) findViewById(R.id.user_list);
        addToListView();
        getSelectedItem();
        deleteItem();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private DatabaseHandler initDB(){
        return new DatabaseHandler(this);
    }

    private List<PersonModel> getProfiles(){
        return db.getAllPerson();
    }

    private void addToListView(){
                List<String> personFullName = new ArrayList<String>();
                for(PersonModel person : people){
                    personFullName.add(person.getName() + " " + person.getSurname());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,personFullName);
                user_list.setAdapter(adapter);

                if(user_list.getCount() == 0){
                    meta.setText("THERE IS NO RECORD");
                }
    }

    public String findUser(String personName, String personSurname){
        return db.getContactPkWithFullName(personName,personSurname);
    }

    private void getSelectedItem(){
        user_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                String selectedFromList =(String) (user_list.getItemAtPosition(position));
                String name = selectedFromList.split(" ")[0];
                String surname = selectedFromList.split(" ")[1];
                boolean isNew = false;
                String pk = findUser(name,surname);

                PersonModel person = db.getPersonWithPk(pk);
                OrganModel organ = db.getOrganWithPk(pk);

                Intent intent = new Intent(LoadProfileActivity.this,PersonActivity.class);
                intent.putExtra("isNew",isNew);
                intent.putExtra("organ",organ);
                intent.putExtra("person",person);
                startActivity(intent);
            }});
    }

    private void deleteItem(){
        user_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                // TODO Auto-generated method stub
                String selectedFromList =(String) (user_list.getItemAtPosition(pos));
                String name = null;
                String surname = null;
                if(!selectedFromList.equals(" ")){
                     name = selectedFromList.split(" ")[0];
                     surname = selectedFromList.split(" ")[1];
                }
                else{
                     name = "";
                     surname = "";
                }

                createAlertDialog(name,surname);

                return true;
            }
        });
    }

    private void createAlertDialog(final String name,final String surname){
        AlertDialog.Builder builder = new AlertDialog.Builder(LoadProfileActivity.this);
        builder.setTitle("Delete User?");
        builder.setMessage("Do you want to delete this user?");
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id) {
                //İptal butonuna basılınca yapılacaklar.Sadece kapanması isteniyorsa boş bırakılacak
            }
        });


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                db.deletePerson(name,surname);
                Intent refresh = new Intent(LoadProfileActivity.this, LoadProfileActivity.class);
                startActivity(refresh);//Start the same Activity
                finish(); //finish Activity.
                //Tamam butonuna basılınca yapılacaklar
            }
        });
        builder.show();
    }
}
