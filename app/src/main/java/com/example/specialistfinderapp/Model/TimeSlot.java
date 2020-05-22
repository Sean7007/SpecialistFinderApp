package com.example.specialistfinderapp.Model;

public class TimeSlot {
    private Long shot;

    //Constructor
    public TimeSlot(){}

    public TimeSlot(Long shot) {
        this.shot = shot;
    }

    public Long getShot() {
        return shot;
    }

    public void setShot(Long shot) {
        this.shot = shot;
    }
}
