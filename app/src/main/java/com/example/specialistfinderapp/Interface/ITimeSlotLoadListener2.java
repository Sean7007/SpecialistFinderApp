package com.example.specialistfinderapp.Interface;

import com.example.specialistfinderapp.Model.TimeSlot;

import java.util.List;

public interface ITimeSlotLoadListener2 {
    void onTimeSlotLoadSuccess(List<TimeSlot> timeSlotList);
    void onTimeSlotLoadFailed(String message);
    void onTimeSlotLoadEmpty();
}
