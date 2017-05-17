package com.example.berkin.healthmonitor.Body;

/**
 * Created by berkin on 09.05.2017.
 */

public class Organ {
    private int energyPercentage;
    private String status;

    public Organ(){
        energyPercentage = 100;
        status = "Fresh";
    }

    public String getStatus(){
        return status;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public int getEneryPercentage(){
        return energyPercentage;
    }

    public void setEnergyPercentage(int energyPercentage){
        this.energyPercentage = energyPercentage;
        if(energyPercentage > 100)
            this.energyPercentage = 100;
        if(energyPercentage < 1)
            this.energyPercentage = 0;
    }
}
