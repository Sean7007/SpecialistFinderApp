package com.example.specialistfinderapp.CustomerFragments.AppointmentFragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.specialistfinderapp.Model.BookingInformation;
import com.example.specialistfinderapp.R;
import com.example.specialistfinderapp.util.Common;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.example.specialistfinderapp.util.Common.bookingDate;
import static com.example.specialistfinderapp.util.Common.currentTimeSlot;

public class BookingStep4Fragment extends Fragment {
    //Variable declaration
    SimpleDateFormat simpleDateFormat;
    LocalBroadcastManager localBroadcastManager;
    Unbinder unbinder;

    @BindView(R.id.txt_booking_doctor_text)
    TextView txt_booking_doctor_text;
    @BindView(R.id.txt_booking_time_text)
    TextView txt_booking_time_text;
    @BindView(R.id.txt_hospital_address)
    TextView txt_hospital_address;
    @BindView(R.id.txt_hospital_name)
    TextView txt_hospital_name;
    @BindView(R.id.txt_hospital_open_hours)
    TextView txt_hospital_open_hours;
    @BindView(R.id.txt_hospital_phone)
    TextView txt_hospital_phone;
    @BindView(R.id.txt_hospital_website)
    TextView txt_hospital_website;
    @OnClick(R.id.btn_confirm)
            void confirmBooking(){
        BookingInformation bookingInformation = new BookingInformation();
        bookingInformation.setDoctorId(Common.currentDoctor.getDoctorId());
        bookingInformation.setDoctorName(Common.currentDoctor.getName());
        bookingInformation.setCustomerName(Common.currentUser.getcFname());
        //bookingInformation.setCustomerPhone(Common.currentUser.getPhoneNumber());
        bookingInformation.setHospitalId(Common.currentHospital.getHospitalId());
        bookingInformation.setHospitalName(Common.currentHospital.getName());
        bookingInformation.setTime(new StringBuilder(Common.convertTimeSlotToString(Common.currentTimeSlot))
        .append("at")
        .append(simpleDateFormat.format(bookingDate.getTime())).toString());
        bookingInformation.setSlot(Long.valueOf(currentTimeSlot));

        //Submit to Doctor document
        DocumentReference bookingDate = FirebaseFirestore.getInstance()
                .collection("AllHospitals")
                .document(Common.city)
                .collection("Branch")
                .document(Common.currentHospital.getHospitalId())
                .collection("Doctor")
                .document(Common.currentDoctor.getDoctorId())
                .collection(Common.simpleFormatDate.format(Common.bookingDate.getTime()))
                .document(String.valueOf(currentTimeSlot));

        //Write data
        bookingDate.set(bookingInformation)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        getActivity().finish(); //Close Activity
                        Toast.makeText(getContext(), "Success!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), ""+ e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    BroadcastReceiver confirmBookinReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setData();
        }
    };

    private void setData() {
        txt_booking_doctor_text.setText(Common.currentDoctor.getName());
        txt_booking_time_text.setText(new StringBuilder(Common.convertTimeSlotToString(Common.currentTimeSlot))
        .append("at")
        .append(simpleDateFormat.format(bookingDate.getTime())));

        txt_hospital_address.setText(Common.currentHospital.getAddress());
        txt_hospital_website.setText(Common.currentHospital.getWebsites());
        txt_hospital_name.setText(Common.currentHospital.getName());
        txt_hospital_open_hours.setText(Common.currentHospital.getOpenHours());


    }

    static BookingStep4Fragment instance;

    public static BookingStep4Fragment getInstance(){
        if(instance == null)
            instance = new BookingStep4Fragment();
        return instance;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Apply format for date display
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(confirmBookinReceiver, new IntentFilter(Common.KEY_CONFIRM_BOOKING));

    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(confirmBookinReceiver);
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_booking_step_four, container, false);
        unbinder = ButterKnife.bind(this, itemView);
        return itemView;
    }
}

