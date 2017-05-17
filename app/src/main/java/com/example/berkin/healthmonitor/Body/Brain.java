package com.example.berkin.healthmonitor.Body;

/**
 * Created by berkin on 09.05.2017.
 */

public class Brain extends Organ {
    private int Iq;

    public Brain(int Iq){
        this.Iq = Iq;
    }

    public int getIq(){
        return Iq;
    }
}
