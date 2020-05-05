package com.example.specialistfinderapp.CustomerFragments.AppointmentFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.specialistfinderapp.R;

public class BookingStep4Fragment extends Fragment {

    static BookingStep4Fragment instance;

    public static BookingStep4Fragment getInstance(){
        if(instance == null)
            instance = new BookingStep4Fragment();
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
        /*view =*/ return inflater.inflate(R.layout.fragment_booking_step_four, container, false);
        /*return view;*/
    }
}

