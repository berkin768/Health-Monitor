package com.example.berkin.healthmonitor;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.berkin.healthmonitor.Body.Arm;
import com.example.berkin.healthmonitor.Body.Brain;
import com.example.berkin.healthmonitor.Body.Eye;
import com.example.berkin.healthmonitor.Body.Heart;
import com.example.berkin.healthmonitor.Body.Leg;
import com.example.berkin.healthmonitor.Body.Organ;
import com.example.berkin.healthmonitor.Model.OrganModel;
import com.example.berkin.healthmonitor.Model.PersonModel;

import org.w3c.dom.Text;


public class PersonActivity extends AppCompatActivity {

    public Spinner activitySpinner;
    public Object selectedActivity;
    private Intent intent;
    private Thread thread;
    private boolean isNew = true;
    private int loopCount = 0;
    private TextView armLabel;
    private TextView brainLabel;
    private TextView eyeLabel;
    private TextView heartLabel;
    private TextView legLabel;
    private SeekBar seekBar;
    private TextView seekNumber;
    private TextView time;
    private ImageView brainImg;
    private ImageView armImg;
    private ImageView legImg;
    private ImageView heartImg;
    private ImageView eyeImg;
    private int timeCount = 0;
    private int startButtonCountCheck = 0;
    private int leftSeekNumber = 0;
    private Organ eye;
    private Organ arm;
    private Organ brain;
    private Organ leg;
    private Organ heart;
    private Button startButton;

    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activitySpinner = (Spinner) findViewById(R.id.activitySpinner); //init
        armLabel = (TextView) findViewById(R.id.arm);
        brainLabel = (TextView) findViewById(R.id.brain);
        eyeLabel = (TextView) findViewById(R.id.eye);
        heartLabel = (TextView) findViewById(R.id.heart);
        legLabel = (TextView) findViewById(R.id.leg);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekNumber = (TextView) findViewById(R.id.second);
        startButton = (Button) findViewById(R.id.startButton);
        time = (TextView) findViewById(R.id.time);
        brainImg = (ImageView) findViewById(R.id.brainImg);
        armImg = (ImageView) findViewById(R.id.armImg);
        legImg = (ImageView) findViewById(R.id.legImg);
        eyeImg = (ImageView) findViewById(R.id.eyeImg);
        heartImg = (ImageView) findViewById(R.id.heartImg);
        db = initDB();

        intent = getIntent();
        PersonModel person = (PersonModel) intent.getSerializableExtra("person");
        OrganModel organ = (OrganModel) intent.getSerializableExtra("organ");

        eye = new Eye(person.getEyeColor());
        arm = new Arm();
        brain = new Brain(150);
        leg = new Leg();
        heart = new Heart(120);

        if (organ != null) {
            eye.setEnergyPercentage(organ.getEyeCondition());
            arm.setEnergyPercentage(organ.getArmCondition());
            brain.setEnergyPercentage(organ.getBrainCondition());
            leg.setEnergyPercentage(organ.getLegCondition());
            heart.setEnergyPercentage(organ.getHeartCondition());
            isNew = false;
        }

        armLabel.setText(arm.getEneryPercentage() + "");
        legLabel.setText(leg.getEneryPercentage() + "");
        heartLabel.setText(heart.getEneryPercentage() + "");
        brainLabel.setText(brain.getEneryPercentage() + "");
        eyeLabel.setText(eye.getEneryPercentage() + "");
        setColors();
        setSeekbarOperations();
        operations(person);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.load_profile:
                Intent intent = new Intent(PersonActivity.this,LoadProfileActivity.class);
                startActivity(intent);
                return true;
            case R.id.exit:
                finishAffinity();
                return true;
            case R.id.about:
                Intent goAbout = new Intent(PersonActivity.this,AboutActivity.class);
                startActivity(goAbout);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private DatabaseHandler initDB() {
        return new DatabaseHandler(this);
    }

    public void setSeekbarOperations() {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int seekValue = seekBar.getProgress();
                seekNumber.setText(seekValue + "");
                leftSeekNumber = seekValue;
                loopCount = seekValue;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int seekValue = seekBar.getProgress();
                seekNumber.setText(seekValue + "");
                loopCount = seekValue;
                leftSeekNumber = seekValue;
            }
        });
    }


    public void operations(final PersonModel person) {

        int initialPosition = activitySpinner.getSelectedItemPosition();  //To prevent  create toast message at the beginning

        activitySpinner.setSelection(initialPosition, false);
        selectedActivity = "Jogging";  //DEFAULT OPTION

        activitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedActivity = activitySpinner.getSelectedItem().toString();
                Toast.makeText(getApplicationContext(), selectedActivity.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startButtonCountCheck++;
                leftSeekNumber = loopCount;
                //--------------THREAD SECTION-------------------------//
                thread = new Thread(new Runnable() {
                    boolean stop = false;

                    @Override
                    public void run() {
                        for (int i = 0; i < loopCount; i++) {
                            if (startButtonCountCheck % 2 == 1)
                                setHumanAttributes(person, selectedActivity.toString());

                            //-------------- UI THREAD SECTION-------------------------//
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // This code will always run on the UI thread, therefore is safe to modify UI elements.
                                    if (startButtonCountCheck % 2 == 1) {
                                        printValues();
                                        startButton.setText("STOP");
                                    } else {
                                        startButton.setText("START");
                                        stop = true;
                                    }
                                    if (leftSeekNumber == 0) {
                                        //AFTER COUNT FINISHED, BUTTON SHOULD PRINT START
                                        startButton.setText("START");
                                        stop = true;
                                    }
                                }
                            });
                            //-------------- UI THREAD SECTION CLOSE-------------------------//
                            try {
                                if (stop) {

                                    startButtonCountCheck = 0;
                                    thread.interrupt();
                                    break;
                                } else {
                                    Thread.sleep(500);
                                }
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }
                });

                thread.start();
                //--------------THREAD ENDS SECTION-------------------------//
            }
        });


    }

    private void setColors(){
        if(arm.getEneryPercentage() >= 66)
            armImg.setImageResource(R.drawable.arm_green);
        else if(arm.getEneryPercentage() < 66 && arm.getEneryPercentage() >= 33)
            armImg.setImageResource(R.drawable.arm_yellow);
        else
            armImg.setImageResource(R.drawable.arm_red);

        if(brain.getEneryPercentage() >= 66)
            brainImg.setImageResource(R.drawable.brain_green);
        else if(brain.getEneryPercentage() < 66 && brain.getEneryPercentage() >= 33)
            brainImg.setImageResource(R.drawable.brain_yellow);
        else
            brainImg.setImageResource(R.drawable.brain_red);

        if(eye.getEneryPercentage() >= 66)
            eyeImg.setImageResource(R.drawable.eye_green);
        else if(eye.getEneryPercentage() < 66 && eye.getEneryPercentage() >= 33)
            eyeImg.setImageResource(R.drawable.eye_yellow);
        else
            eyeImg.setImageResource(R.drawable.eye_red);

        if(heart.getEneryPercentage() >= 66)
            heartImg.setImageResource(R.drawable.heart_green);
        else if(heart.getEneryPercentage() < 66 && heart.getEneryPercentage() >= 33)
            heartImg.setImageResource(R.drawable.heart_yellow);
        else
            heartImg.setImageResource(R.drawable.heart_red);

        if(leg.getEneryPercentage() >= 66)
            legImg.setImageResource(R.drawable.leg_green);
        else if(leg.getEneryPercentage() < 66 && leg.getEneryPercentage() >= 33)
            legImg.setImageResource(R.drawable.leg_yellow);
        else
            legImg.setImageResource(R.drawable.leg_red);
    }

    private void printValues() {
        setColors();
        armLabel.setText(arm.getEneryPercentage() + "");
        legLabel.setText(leg.getEneryPercentage() + "");
        heartLabel.setText(heart.getEneryPercentage() + "");
        brainLabel.setText(brain.getEneryPercentage() + "");
        eyeLabel.setText(eye.getEneryPercentage() + "");
        time.setText(timeCount + "");
        leftSeekNumber--;
    }

    public String findUser(String personName, String personSurname) {
        return db.getContactPkWithFullName(personName, personSurname);
    }

    public void setHumanAttributes(PersonModel person, String selectedActivity) {
        if (selectedActivity.equals("Jogging")) {
            int armCondition = arm.getEneryPercentage();
            arm.setEnergyPercentage(armCondition - 1);
            int legCondition = leg.getEneryPercentage();
            leg.setEnergyPercentage(legCondition - 2);
            int heartCondition = heart.getEneryPercentage();
            heart.setEnergyPercentage(heartCondition - 2);
        } else if (selectedActivity.equals("Football")) {
            int armCondition = arm.getEneryPercentage();
            arm.setEnergyPercentage(armCondition - 2);
            int legCondition = leg.getEneryPercentage();
            leg.setEnergyPercentage(legCondition - 3);
            int heartCondition = heart.getEneryPercentage();
            heart.setEnergyPercentage(heartCondition - 2);
        } else if (selectedActivity.equals("Cycling")) {
            int armCondition = arm.getEneryPercentage();
            arm.setEnergyPercentage(armCondition - 1);
            int legCondition = leg.getEneryPercentage();
            leg.setEnergyPercentage(legCondition - 3);
            int heartCondition = heart.getEneryPercentage();
            heart.setEnergyPercentage(heartCondition - 2);
        } else if (selectedActivity.equals("Course")) {
            int eyeCondition = eye.getEneryPercentage();
            eye.setEnergyPercentage(eyeCondition - 1);
            int brainCondition = brain.getEneryPercentage();
            brain.setEnergyPercentage(brainCondition - 3);
        } else if (selectedActivity.equals("Cinema")) {
            int eyeCondition = eye.getEneryPercentage();
            eye.setEnergyPercentage(eyeCondition - 3);
            int brainCondition = brain.getEneryPercentage();
            brain.setEnergyPercentage(brainCondition - 1);
        } else if (selectedActivity.equals("Music")) {
            int eyeCondition = eye.getEneryPercentage();
            eye.setEnergyPercentage(eyeCondition - 3);
            int brainCondition = brain.getEneryPercentage();
            brain.setEnergyPercentage(brainCondition - 1);
        } else if (selectedActivity.equals("Eat")) {
            int armCondition = arm.getEneryPercentage();
            arm.setEnergyPercentage(armCondition + 1);
            int legCondition = leg.getEneryPercentage();
            leg.setEnergyPercentage(legCondition + 3);
            int heartCondition = heart.getEneryPercentage();
            heart.setEnergyPercentage(heartCondition + 2);
        } else if (selectedActivity.equals("Sleep")) {
            int eyeCondition = eye.getEneryPercentage();
            eye.setEnergyPercentage(eyeCondition + 3);
            int brainCondition = brain.getEneryPercentage();
            brain.setEnergyPercentage(brainCondition + 2);
        }

        OrganModel organ = new OrganModel(arm.getEneryPercentage(), brain.getEneryPercentage(), eye.getEneryPercentage(), heart.getEneryPercentage(), leg.getEneryPercentage());
        String pk = findUser(person.getName(), person.getSurname());

        timeCount++;
        if (isNew == true) {
            db.addBody(organ, Integer.parseInt(pk));
            isNew = false;
        } else
            db.updateBody(organ, Integer.parseInt(pk));
    }
}