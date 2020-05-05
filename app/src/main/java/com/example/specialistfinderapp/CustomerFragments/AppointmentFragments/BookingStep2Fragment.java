package com.example.specialistfinderapp.CustomerFragments.AppointmentFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.specialistfinderapp.R;

public class BookingStep2Fragment extends Fragment {
    static BookingStep2Fragment instance;

    public static BookingStep2Fragment getInstance(){
        if(instance == null)
            instance = new BookingStep2Fragment();
        return instance;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        /*view =*/ return inflater.inflate(R.layout.fragment_booking_step_two, container, false);
        /*return view;*/
    }
}
