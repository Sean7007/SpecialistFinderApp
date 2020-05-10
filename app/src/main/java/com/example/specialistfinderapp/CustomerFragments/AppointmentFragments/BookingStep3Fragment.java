package com.example.specialistfinderapp.CustomerFragments.AppointmentFragments;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.specialistfinderapp.Adapter.MyTimeSlotAdapter;
import com.example.specialistfinderapp.Interface.ITimeSlotLoadListener;
import com.example.specialistfinderapp.Model.TimeSlot;
import com.example.specialistfinderapp.R;
import com.example.specialistfinderapp.util.Common;
import com.example.specialistfinderapp.util.SpacesItemDecoration;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;
import dmax.dialog.SpotsDialog;

public class BookingStep3Fragment extends Fragment implements ITimeSlotLoadListener {
    //Variable
    DocumentReference doctorDoc;
    ITimeSlotLoadListener iTimeSlotLoadListener;
    AlertDialog dialog;

    Unbinder unbinder;
    LocalBroadcastManager localBroadcastManager;
    Calendar selected_date;

    @BindView(R.id.recycler_time_slot)
    RecyclerView recycler_time_slot;
    @BindView(R.id.calendarView)
    HorizontalCalendarView calendarView;
    SimpleDateFormat simpleDateFormat;

    BroadcastReceiver displayTimeSlot = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
           Calendar date = Calendar.getInstance();
           date.add(Calendar.DATE, 0); //Add current date
            loadAvailableTimeSlotOfDoctor(Common.currentDoctor.getDoctorId(),
                    simpleDateFormat.format(date.getTime()));
        }
    };

    private void loadAvailableTimeSlotOfDoctor(String doctorId, final String bookDate) {
        dialog.show();

        //
        doctorDoc = FirebaseFirestore.getInstance()
                .collection("AllHospitals")
                .document(Common.city)
                .collection("Branch")
                .document(Common.currentHospital.getHospitalId())
                .collection("Doctor")
                .document(Common.currentDoctor.getDoctorId());

        //Get Information of this doctor
        doctorDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
              if(task.isSuccessful()) {
                  DocumentSnapshot documentSnapshot = task.getResult();
                  if (documentSnapshot.exists()) //if barber available
                  {
                      //Get information of booking
                      //If not created, return empty
                      CollectionReference date = FirebaseFirestore.getInstance()
                              .collection("AllHospitals")
                              .document(Common.city)
                              .collection("Branch")
                              .document(Common.currentHospital.getHospitalId())
                              .collection("Doctor")
                              .document(Common.currentDoctor.getDoctorId())
                              .collection(bookDate);

                      date.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                          @Override
                          public void onComplete(@NonNull Task<QuerySnapshot> task) {
                              if (task.isSuccessful()) {
                                  QuerySnapshot querySnapshot = task.getResult();
                                  if (querySnapshot.isEmpty()) //If there is no appointment
                                      iTimeSlotLoadListener.onTimeSlotLoadEmpty();
                                  else {
                                      //If have an appointment
                                      List<TimeSlot> timeSlots = new ArrayList<>();
                                      for (QueryDocumentSnapshot documentSnapshot1 : task.getResult())
                                          timeSlots.add(documentSnapshot1.toObject(TimeSlot.class));
                                      iTimeSlotLoadListener.onTimeSlotLoadSuccess(timeSlots);
                                  }
                              }
                          }
                      }).addOnFailureListener(new OnFailureListener() {
                          @Override
                          public void onFailure(@NonNull Exception e) {
                              iTimeSlotLoadListener.onTimeSlotLoadFailed(e.getMessage());

                          }
                      });
                  }
              }
            }
        });
    }

    static BookingStep3Fragment instance;

    public static BookingStep3Fragment getInstance(){
        if(instance == null)
            instance = new BookingStep3Fragment();
        return instance;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        iTimeSlotLoadListener = this;
        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(displayTimeSlot, new IntentFilter(Common.KEY_DISPLAY_TIME_SLOT));

        simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy"); //05_07_2020 (this is key)
        dialog = new SpotsDialog.Builder().setContext(getContext()).setCancelable(false).build();

    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(displayTimeSlot);
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_booking_step_three, container, false);
        unbinder = ButterKnife.bind(this, itemView);
        init(itemView);
        return itemView;
    }

    private void init(View itemView) {
        recycler_time_slot.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        recycler_time_slot.setLayoutManager(gridLayoutManager);
        recycler_time_slot.addItemDecoration(new SpacesItemDecoration(8));

        //Calendar
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.DATE, 0);
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.DATE, 2);

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(itemView, R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(1)
                .mode(HorizontalCalendar.Mode.DAYS)
                .defaultSelectedDate(startDate)
                .build();
        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                if(Common.bookingDate.getTimeInMillis() != date.getTimeInMillis()){
                    Common.bookingDate = date; //This code will not load again if you selected new day same with day
                    loadAvailableTimeSlotOfDoctor(Common.currentDoctor.getDoctorId(),
                            simpleDateFormat.format(date.getTime()));
                }
            }
        });
    }

    @Override
    public void onTimeSlotLoadSuccess(List<TimeSlot> timeSlotList) {
        MyTimeSlotAdapter adapter = new MyTimeSlotAdapter(getContext(),timeSlotList);
        recycler_time_slot.setAdapter(adapter);

        dialog.dismiss();
    }

    @Override
    public void onTimeSlotLoadFailed(String message) {
       Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
       dialog.dismiss();
    }

    @Override
    public void onTimeSlotLoadEmpty() {
        MyTimeSlotAdapter adapter = new MyTimeSlotAdapter(getContext());
        recycler_time_slot.setAdapter(adapter);

        dialog.dismiss();
    }
}

