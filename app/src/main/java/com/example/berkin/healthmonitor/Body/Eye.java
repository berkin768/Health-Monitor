package com.example.berkin.healthmonitor.Body;

/**
 * Created by berkin on 09.05.2017.
 */

public class Eye extends Organ {
    private String eyeColor;

    public Eye(String eyeColor){
        this.eyeColor=eyeColor;
    }

    public String getEyeColor(){
        return eyeColor;
    }
}
