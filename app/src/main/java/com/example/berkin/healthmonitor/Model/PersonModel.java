package com.example.berkin.healthmonitor.Model;

import java.io.Serializable;

/**
 * Created by berkin on 10.05.2017.
 */

public class PersonModel implements Serializable {
    private String name;
    private String surname;
    private int age;
    private String eyeColor ;

    public PersonModel(String name, String surname, int age, String eyeColor) {
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.eyeColor = eyeColor;
    }

    public String getName(){
        return this.name;
    }

    public String getSurname(){
        return this.surname;
    }

    public int getAge(){
        return this.age;
    }

    public String getEyeColor(){
        return this.eyeColor;
    }
}