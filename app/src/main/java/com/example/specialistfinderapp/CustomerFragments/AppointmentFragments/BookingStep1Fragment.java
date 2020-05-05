package com.example.specialistfinderapp.CustomerFragments.AppointmentFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.specialistfinderapp.R;

public class BookingStep1Fragment extends Fragment {
    //Variables

    static BookingStep1Fragment instance;

    public static BookingStep1Fragment getInstance(){
        if(instance == null)
            instance = new BookingStep1Fragment();
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
        /*view =*/ return inflater.inflate(R.layout.fragment_booking_step_one, container, false);
        /*return view;*/

    }
}
