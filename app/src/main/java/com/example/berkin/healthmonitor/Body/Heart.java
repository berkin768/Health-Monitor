package com.example.berkin.healthmonitor.Body;

/**
 * Created by berkin on 09.05.2017.
 */

public class Heart extends Organ{
    private int heartBeat;

    public Heart(int heartBeat){
        this.heartBeat  = heartBeat;
    }

    public int getHeartBeat(){
        return heartBeat;
    }

    public void setHeartBeat(int heartBeat){
        this.heartBeat = heartBeat;
    }
}
