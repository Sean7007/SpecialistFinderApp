package com.example.specialistfinderapp.CustomerFragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.specialistfinderapp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerAppointmentFragment extends Fragment {
    //Variable declaration
    private static final String ARG_PARAM1 = "param1" ;
    private static final String ARG_PARAM2 = "param2";
    private static CustomerAppointmentFragment INSTANCE =null;
    View view;
    CardView card_view_booking;


    /*@OnClick(R.id.card_view_booking)
            void booking(){
        startActivity(new Intent(getActivity(), BookingActivity.class)); }*/

    //Constructor
    public CustomerAppointmentFragment() {
        // Required empty public constructor
    }

    public static CustomerAppointmentFragment newInstance() {
        CustomerAppointmentFragment fragment = new CustomerAppointmentFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static CustomerAppointmentFragment getINSTANCE(){
        if(INSTANCE == null)
            INSTANCE = new CustomerAppointmentFragment();
        return INSTANCE;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_appointment, container, false);

        card_view_booking = view.findViewById(R.id.card_view_booking);
        card_view_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BookingActivity.class);
                getActivity().startActivity(intent);
                return;
            }
        });


        return view;
    }


}
