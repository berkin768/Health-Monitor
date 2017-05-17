package com.example.berkin.healthmonitor.Body;

/**
 * Created by berkin on 09.05.2017.
 */

public class Leg extends Organ{
    private int length;

    public Leg(){
        length = 40;
    }

    public int getLength(){
        return length;
    }

    public void setLength(int length){
        this.length = length;
    }
}
