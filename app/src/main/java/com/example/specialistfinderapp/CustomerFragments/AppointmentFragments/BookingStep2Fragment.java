package com.example.specialistfinderapp.CustomerFragments.AppointmentFragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.specialistfinderapp.Adapter.MyDoctorAdapter;
import com.example.specialistfinderapp.Model.Doctor;
import com.example.specialistfinderapp.R;
import com.example.specialistfinderapp.util.Common;
import com.example.specialistfinderapp.util.SpacesItemDecoration;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.common.io.LineReader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BookingStep2Fragment extends Fragment {
    //Variables
    Unbinder unbinder;
    LocalBroadcastManager localBroadcastManager;
    private MyDoctorAdapter adapter;
    List<Doctor> doctorList; //Not sure

    @BindView(R.id.recycler_doctor)
    RecyclerView recycler_doctor;

    private BroadcastReceiver doctorDoneReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<Doctor> doctorArrayList = intent.getParcelableArrayListExtra(Common.KEY_DOCTOR_LOAD_DONE);

            //Create adapter late
            MyDoctorAdapter adapter = new MyDoctorAdapter(getContext(), doctorArrayList);
            recycler_doctor.setAdapter(adapter);
        }
    };


    static BookingStep2Fragment instance;

    public static BookingStep2Fragment getInstance(){
        if(instance == null)
            instance = new BookingStep2Fragment();
        return instance;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(doctorDoneReceiver, new IntentFilter(Common.KEY_DOCTOR_LOAD_DONE));
    }

    @Override
      public void onDestroy() {
        localBroadcastManager.unregisterReceiver(doctorDoneReceiver);
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_booking_step_two, container, false);
        unbinder = ButterKnife.bind(this,itemView);

        doctorList = new ArrayList<>();

        recycler_doctor = (RecyclerView) itemView.findViewById(R.id.recycler_doctor);

        initView();
        return itemView;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initView() {

        recycler_doctor.setHasFixedSize(true);
        recycler_doctor.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recycler_doctor.addItemDecoration(new SpacesItemDecoration(4));
        recycler_doctor.setItemAnimator(new DefaultItemAnimator());

        //Your adapter initialization here
        adapter = new MyDoctorAdapter(getActivity(), doctorList);
        recycler_doctor.setAdapter(adapter);
    }

}
