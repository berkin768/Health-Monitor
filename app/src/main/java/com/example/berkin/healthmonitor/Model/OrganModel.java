package com.example.berkin.healthmonitor.Model;

import java.io.Serializable;

/**
 * Created by berkin on 10.05.2017.
 */

public class OrganModel implements Serializable {
    private int armCondition;
    private int brainCondition;
    private int eyeCondition;
    private int heartCondition ;
    private int legCondition;

    public OrganModel(int armCondition,int brainCondition,int eyeCondition,int heartCondition,int legCondition){
        this.armCondition = armCondition;
        this.brainCondition = brainCondition;
        this.heartCondition = heartCondition;
        this.legCondition = legCondition;
        this.eyeCondition = eyeCondition;
    }

    public int getArmCondition(){
        return this.armCondition;
    }

    public int getBrainCondition(){
        return this.brainCondition;
    }

    public int getEyeCondition(){
        return this.eyeCondition;
    }

    public int getHeartCondition(){
        return this.heartCondition;
    }
    public int getLegCondition(){
        return this.legCondition;
    }
}
