package com.example.specialistfinderapp.CustomerFragments.AppointmentFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.specialistfinderapp.R;

public class BookingStep3Fragment extends Fragment {

    static BookingStep3Fragment instance;

    public static BookingStep3Fragment getInstance(){
        if(instance == null)
            instance = new BookingStep3Fragment();
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
        /*view =*/ return inflater.inflate(R.layout.fragment_booking_step_three, container, false);
        /*return view;*/
    }
}

