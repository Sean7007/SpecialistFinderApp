package com.example.specialistfinderapp.CustomerFragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.specialistfinderapp.Adapter.MyViewPagerAdapter;
import com.example.specialistfinderapp.Model.Doctor;
import com.example.specialistfinderapp.R;
import com.example.specialistfinderapp.util.Common;
import com.example.specialistfinderapp.util.NonSwipeViewPager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.shuhart.stepview.StepView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;

public class BookingActivity extends AppCompatActivity {

    LocalBroadcastManager localBroadcastManager;
    AlertDialog dialog;
    CollectionReference doctorRef;

    //Variable
    @BindView(R.id.step_view)
    StepView stepView;
    @BindView(R.id.booking_view_pager)
    NonSwipeViewPager viewPager;
    @BindView(R.id.btn_previous_step)
    Button btn_previous_step;
    @BindView(R.id.btn_next_step)
    Button btn_next_step;

    //Event
    @OnClick(R.id.btn_previous_step)
    void previousStep(){
        if(Common.step == 3 || Common.step > 0){
            Common.step--;
            viewPager.setCurrentItem(Common.step);

            if(Common.step < 3) // Always enable NEXT when Step < 3
            {
                btn_next_step.setEnabled(true);
                setColorButton();
            }
        }
    }

    @OnClick(R.id.btn_next_step)
    void nextClick(){
       if(Common.step < 3 || Common.step == 0)
       {
           Common.step++; //increase
           if(Common.step == 1) //After choosing hospital
           {
               if (Common.currentHospital != null)
                   loadDoctorByHospital(Common.currentHospital.getHospitalId());
           }
           else if(Common.step == 2)//Pick time slot
           {
               if(Common.currentDoctor != null)
                   loadTimeSlotOfDoctor(Common.currentDoctor.getDoctorId());
           }
           else if(Common.step == 3)//Pick time slot
           {
               if(Common.currentTimeSlot != -1)
                   confirmBooking();
           }
           viewPager.setCurrentItem(Common.step);
       }
    }

    private void confirmBooking() {
        //Send broadcast to fragment step four
        Intent intent = new Intent(Common.KEY_CONFIRM_BOOKING);
        localBroadcastManager.sendBroadcast(intent);
    }

    private void loadTimeSlotOfDoctor(String doctorId) {
        //Send Local Broadcast to Fragment step 3
        Intent intent = new Intent(Common.KEY_DISPLAY_TIME_SLOT);
        localBroadcastManager.sendBroadcast(intent);
    }

    private void loadDoctorByHospital(String hospitalId) {
     dialog.show();

     //Now, select all doctor of Hospital
        if(!TextUtils.isEmpty(Common.city)) {
            doctorRef = FirebaseFirestore.getInstance()
                    .collection("AllHospitals")
                    .document(Common.city)
                    .collection("Branch")
                    .document(hospitalId)
                    .collection("Doctors");
            doctorRef.get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                          ArrayList<Doctor> doctors = new ArrayList<>();
                          for(QueryDocumentSnapshot doctorSnapShot: task.getResult()){
                              Doctor doctor = doctorSnapShot.toObject(Doctor.class);
                              doctor.setPassword(""); //Remove password
                              doctor.setDoctorId(doctorSnapShot.getId()); //Get Id of Doctor

                              doctors.add(doctor);
                          }

                          //Send Broadcast to BookingStep2Fragment to load recycler
                            Intent intent = new Intent(Common.KEY_DOCTOR_LOAD_DONE);
                            intent.putParcelableArrayListExtra(Common.KEY_DOCTOR_LOAD_DONE, doctors);
                            localBroadcastManager.sendBroadcast(intent);

                            dialog.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                   dialog.dismiss();
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        localBroadcastManager.unregisterReceiver(buttonNextReceiver);
        super.onDestroy();
    }

    //Broadcast Receiver
    private BroadcastReceiver buttonNextReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int step = intent.getIntExtra(Common.KEY_STEP, 0);
            if(step == 1)
            Common.currentHospital = intent.getParcelableExtra(Common.KEY_HOSPITAL_STORE);
            else if (step == 2)
                Common.currentDoctor = intent.getParcelableExtra(Common.KEY_DOCTOR_SELECTED);
            else if (step == 3)
                Common.currentTimeSlot = intent.getIntExtra(Common.KEY_TIME_SLOT, -1);

            btn_next_step.setEnabled(true);
            setColorButton();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        ButterKnife.bind(BookingActivity.this);

        btn_previous_step = findViewById(R.id.btn_previous_step);
        btn_next_step = findViewById(R.id.btn_next_step);
        viewPager = findViewById(R.id.booking_view_pager);
        stepView = findViewById(R.id.step_view);

        dialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(buttonNextReceiver, new IntentFilter(Common.KEY_ENABLE_BUTTON_NEXT));

        btn_previous_step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Common.step == 3 || Common.step > 0){
                    Common.step--;
                    viewPager.setCurrentItem(Common.step);
                }
            }
        });

        btn_next_step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Common.step < 3 || Common.step == 0)
                {
                    Common.step++; //increase
                    if(Common.step == 1) //After choosing hospital
                    {
                        if (Common.currentHospital != null)
                            loadDoctorByHospital(Common.currentHospital.getHospitalId());
                    }
                    else if(Common.step == 2)//Pick time slot
                    {
                        if(Common.currentDoctor != null)
                            loadTimeSlotOfDoctor(Common.currentDoctor.getDoctorId());
                    }
                    else if(Common.step == 3)//Pick time slot
                    {
                        if(Common.currentTimeSlot != -1)
                            confirmBooking();
                    }
                    viewPager.setCurrentItem(Common.step);
                }

            }
        });

        setupStepView();
        setColorButton();

        //View
        viewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(4); //4 fragment so we need to keep the state of this 4 screen page
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //Show Step
                stepView.go(position, true);

                if(position == 0)
                    btn_previous_step.setEnabled(false);
                else
                    btn_previous_step.setEnabled(true);

                //Set disable button
                btn_next_step.setEnabled(false);
                setColorButton();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setColorButton() {
        if(btn_next_step.isEnabled()){
            btn_next_step.setBackgroundResource(R.color.colorPrimaryDark);
        }
        else{
            btn_next_step.setBackgroundResource(android.R.color.background_dark);
        }
        if(btn_previous_step.isEnabled()){
            btn_previous_step.setBackgroundResource(R.color.colorPrimaryDark);
        }
        else{
            btn_previous_step.setBackgroundResource(android.R.color.background_dark);
        }
    }

    private void setupStepView() {
        List<String> stepList = new ArrayList<>();
        stepList.add("Hospital");
        stepList.add("Specialist");
        stepList.add("Time");
        stepList.add("Confirm");
        stepView.setSteps(stepList);
    }
}
