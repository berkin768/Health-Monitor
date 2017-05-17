package com.example.berkin.healthmonitor.Body;

/**
 * Created by berkin on 09.05.2017.
 */

public class Arm extends Organ{
    private int length;

    public Arm(){
        length = 100;
    }

    public int getLength(){
        return length;
    }

    public void setLength(int length){
        this.length = length;
    }
}
