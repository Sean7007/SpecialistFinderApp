package com.example.specialistfinderapp.CustomerFragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.specialistfinderapp.Interface.IBookingInfoLoadListener;
import com.example.specialistfinderapp.Interface.IBookingInformationChangeListener;
import com.example.specialistfinderapp.Model.BookingInformation;
import com.example.specialistfinderapp.R;
import com.example.specialistfinderapp.Service.PicassoImageLoadingService;
import com.example.specialistfinderapp.util.Common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;
import ss.com.bannerslider.Slider;


/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerAppointmentFragment extends Fragment implements IBookingInfoLoadListener, IBookingInformationChangeListener {
    //Variable declaration
    private static final String ARG_PARAM1 = "param1" ;
    private static final String ARG_PARAM2 = "param2";
    private static CustomerAppointmentFragment INSTANCE =null;
    View view;
    CardView card_view_booking, card_booking_info;
    TextView txt_hospital_address, txt_hospital_doctor, txt_time, txt_time_remain;
    IBookingInfoLoadListener iBookingInfoLoadListener;
    IBookingInformationChangeListener iBookingInformationChangeListener;

    Button btn_change_booking,btn_delete_booking;
    AlertDialog dialog;


   /* @BindView(R.id.card_booking_info)
    CardView card_booking_info;
    @BindView(R.id.txt_hospital_address)
    TextView txt_hospital_address;
    @BindView(R.id.txt_hospital_doctor)
    TextView txt_hospital_doctor;
    @BindView(R.id.txt_time)
    TextView txt_time;
    @BindView(R.id.txt_time_remain)
    TextView txt_time_remain;

    @OnClick(R.id.card_view_booking);*/

    @OnClick(R.id.btn_delete_booking)
    void deleteBooking(){
        deleteBookingFromDoctor(false);
    }
    @OnClick(R.id.btn_change_booking)
    void changeBooking(){
        changingBookingFromUser();
    }

    private void changingBookingFromUser() {
        //Show Dialog
        androidx.appcompat.app.AlertDialog.Builder confirmDialog = new androidx.appcompat.app.AlertDialog.Builder(getActivity())
                .setCancelable(false)
                .setTitle("Hey!")
                .setMessage("Do you really want to change booking information?\nBecause We will delete your old booking information\nJust Confirm")
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteBookingFromDoctor(true);
                    }
                });

        confirmDialog.show();
    }

    private void deleteBookingFromDoctor(boolean isChange) {
        //
        if (Common.currentBooking != null) {
            dialog.show();
            //First, We need to get Information from user Object
                DocumentReference doctorBookingInfo = FirebaseFirestore.getInstance()
                        .collection("AllHospitals")
                        .document(Common.currentBooking.getCityBook())
                        .collection("Branch")
                        .document(Common.currentBooking.getDoctorId())
                        .collection(Common.convertTimeStampToStringKey(Common.currentBooking.getTimestamp()))
                        .document(Common.currentBooking.getSlot().toString());

                //
            doctorBookingInfo.delete().addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    deleteBookingFromUser(isChange);
                }
            });

            } else {
                Toast.makeText(getContext(), "Current Booking must not be null", Toast.LENGTH_SHORT).show();
            }
        }

    private void deleteBookingFromUser(boolean isChange) {
        //First, We need to get Information from user Object
        if(!TextUtils.isEmpty(Common.currentBookingId)) {
            DocumentReference userBookingInfo = FirebaseFirestore.getInstance()
                     //.collection("User")
                     //.document(Common.currentUser.getPhoneNumber())
                    .collection("Booking")
                    .document(Common.currentBookingId);

            //Delete
            userBookingInfo.delete()
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    //After, Delete from User, Delete it from Calendar
                    Paper.init(getActivity());
                    if(Paper.book().read(Common.EVENT_URI_CACHE) != null)
                    {
                        String eventString = Paper.book().read(Common.EVENT_URI_CACHE).toString();
                        Uri eventUri = Uri.parse(Paper.book().read(Common.EVENT_URI_CACHE).toString());
                        getActivity().getContentResolver().delete(eventUri,null, null);
                        Toast.makeText(getActivity(),"Success Delete Booking!", Toast.LENGTH_SHORT).show();
                        eventUri = null;
                        if(eventString!=null && !TextUtils.isEmpty(eventString))
                            eventUri = Uri.parse(eventString);
                        if(eventUri!=null)
                            getActivity().getContentResolver().delete(eventUri, null, null);
                    }

                    Toast.makeText(getActivity(), "Success delete booking !", Toast.LENGTH_SHORT).show();
                    //Refresh
                    loadUserBooking();

                    //Check if Change -> Call from change Button, we will find interface
                    if(isChange)
                        iBookingInformationChangeListener.onBookingInformationChange();
                    dialog.dismiss();
                }
            });
        } else {
                dialog.dismiss();
            Toast.makeText(getContext(), "Booking Information ID must not be empty", Toast.LENGTH_SHORT).show();
        }
    }


    void booking(){
        startActivity(new Intent(getActivity(), BookingActivity.class));
    }

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
        loadUserBooking();
        dialog = new SpotsDialog.Builder().setContext(getContext()).setCancelable(false).build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_appointment, container, false);

        card_view_booking = view.findViewById(R.id.card_view_booking);
        card_booking_info = view.findViewById(R.id.card_booking_info);
        txt_hospital_address = view.findViewById(R.id.txt_hospital_address);
        txt_hospital_doctor = view.findViewById(R.id.txt_hospital_doctor);
        txt_time = view.findViewById(R.id.txt_time);
        txt_time_remain = view.findViewById(R.id.txt_time_remain);
        btn_delete_booking = view.findViewById(R.id.btn_delete_booking);
        btn_change_booking = view.findViewById(R.id.btn_change_booking);

        //Init
        Slider.init(new PicassoImageLoadingService());
        iBookingInfoLoadListener = this;
        iBookingInformationChangeListener = this;

        //Check is Logged
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            loadUserBooking();
        }

        card_view_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BookingActivity.class);
                getActivity().startActivity(intent);
                return;
            }
        });
        btn_delete_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBookingFromDoctor(false);
            }
        });

        btn_change_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changingBookingFromUser();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserBooking();
    }

    private void loadUserBooking() {
        CollectionReference userBooking = FirebaseFirestore.getInstance()
               // .collection("Users")
               // .document(Common.currentUser.getAdress())
                .collection("Booking");

        //Get current date
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);

        Timestamp toDayTimeStamp = new Timestamp(calendar.getTime());
        //Select booking Information from Firebase with done=false and timestamp greater today
        userBooking
                .whereGreaterThanOrEqualTo("timestamp", toDayTimeStamp)
                .whereEqualTo("done", false)
                .limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            if (!task.getResult().isEmpty()){
                                for (QueryDocumentSnapshot queryDocumentSnapshot: task.getResult()){
                                    BookingInformation bookingInformation = queryDocumentSnapshot.toObject(BookingInformation.class);
                                    iBookingInfoLoadListener.onBookingInfoLoadSuccess(bookingInformation, queryDocumentSnapshot.getId());
                                    break; //Exit loop as soon as
                                }

                            }
                            else
                                iBookingInfoLoadListener.onBookingInfoLoadEmpty();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
              iBookingInfoLoadListener.onBookingInfoLoadFailed(e.getMessage());
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBookingInfoLoadEmpty() {
        card_booking_info.setVisibility(View.GONE);
    }

    @Override
    public void onBookingInfoLoadSuccess(BookingInformation bookingInformation, String bookingId) {
        Common.currentBooking = bookingInformation;
        Common.currentBookingId = bookingId;

     txt_hospital_address.setText(bookingInformation.getHospitalAddress());
     txt_hospital_doctor.setText(bookingInformation.getDoctorName());
     txt_time.setText(bookingInformation.getTime());
     String dateRemain = DateUtils.getRelativeTimeSpanString(
             Long.valueOf(bookingInformation.getTimestamp().toDate().getTime()),
             Calendar.getInstance().getInstance().getTimeInMillis(),0).toString();
     txt_time_remain.setText(dateRemain);

     card_booking_info.setVisibility(View.VISIBLE);

         dialog.dismiss();
    }

    @Override
    public void onBookingInfoLoadFailed(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBookingInformationChange() {
        startActivity(new Intent(getActivity(), BookingActivity.class));
    }
}
