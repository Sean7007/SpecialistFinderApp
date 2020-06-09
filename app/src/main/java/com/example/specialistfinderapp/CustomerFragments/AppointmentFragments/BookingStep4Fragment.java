package com.example.specialistfinderapp.CustomerFragments.AppointmentFragments;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
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
import com.example.specialistfinderapp.Model.MyNotification;
import com.example.specialistfinderapp.R;
import com.example.specialistfinderapp.User;
import com.example.specialistfinderapp.util.Common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;

import static com.example.specialistfinderapp.util.Common.bookingDate;
import static com.example.specialistfinderapp.util.Common.currentDoctor;
import static com.example.specialistfinderapp.util.Common.currentHospital;
import static com.example.specialistfinderapp.util.Common.currentTimeSlot;

public class BookingStep4Fragment extends Fragment {
    //Variable declaration
    SimpleDateFormat simpleDateFormat;
    LocalBroadcastManager localBroadcastManager;
    Unbinder unbinder;

    AlertDialog dialog;

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
        dialog.show();

        //Process Timestamp
        //We will use Timestamp to filter all booking with date is greater today
        //For only display all future booking
        String startTime = Common.convertTimeSlotToString(currentTimeSlot);
        String[] convertTime = startTime.split("-"); //Split ex : 9:00 - 10:00
        //Get start time : get 9:00
        String[] startTimeConvert = convertTime[0].split(":");
        int startHourInt = Integer.parseInt(startTimeConvert[0].trim()); //From this you get 9
        int startMinInt = Integer.parseInt(startTimeConvert[1].trim());  //From this you get 00

        Calendar bookingDateWithourHouse = Calendar.getInstance();
        bookingDateWithourHouse.setTimeInMillis(bookingDate.getTimeInMillis());
        bookingDateWithourHouse.set(Calendar.HOUR_OF_DAY, startHourInt);
        bookingDateWithourHouse.set(Calendar.MINUTE, startMinInt);

        //Create timestamp object and apply to BookingInformation
        Timestamp timestamp = new Timestamp(bookingDateWithourHouse.getTime());

        //Create booking information
        final BookingInformation bookingInformation = new BookingInformation();

        bookingInformation.setCityBook(Common.city);
        bookingInformation.setTimestamp(timestamp);
        bookingInformation.setDone(false); //Always FALSE, use this field to filter
        bookingInformation.setDoctorId(Common.currentDoctor.getDoctorId());
        bookingInformation.setDoctorName(Common.currentDoctor.getName());
       // bookingInformation.setCustomerName(Common.currentUser.getName());
       // bookingInformation.setCustomerPhone(Common.currentUser.getPhoneNumber());
        bookingInformation.setHospitalId(Common.currentHospital.getHospitalId());
        bookingInformation.setHospitalName(Common.currentHospital.getName());
        bookingInformation.setTime(new StringBuilder(Common.convertTimeSlotToString(Common.currentTimeSlot))
        .append("at")
        .append(simpleDateFormat.format(bookingDateWithourHouse.getTime())).toString());
        bookingInformation.setSlot(Long.valueOf(currentTimeSlot));

        //Submit to Doctor document
        DocumentReference bookingDate = FirebaseFirestore.getInstance()
                .collection("AllHospitals")
                .document(Common.city)
                .collection("Branch")
                .document(Common.currentHospital.getHospitalId())
                .collection("Doctors")
                .document(Common.currentDoctor.getDoctorId())
                .collection(Common.simpleFormatDate.format(Common.bookingDate.getTime()))
                .document(String.valueOf(currentTimeSlot));

        //Write data
        bookingDate.set(bookingInformation)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Check if booking already exists
                        addToUserBooking(bookingInformation);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), ""+ e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

   private void addToUserBooking(BookingInformation bookingInformation) {

        //First, create new Collection
        final CollectionReference userBooking = FirebaseFirestore.getInstance()
               // .collection("Users")
               // .document(Common.currentUser.getPhoneNumber())
                .collection("Booking");

        //Check if document exists in this collection
       //Get Current date
       Calendar calendar = Calendar.getInstance();
       calendar.add(Calendar.DATE, 0);
       calendar.set(Calendar.HOUR_OF_DAY, 0);
       calendar.set(Calendar.MINUTE, 0);
       Timestamp toDayTimeStamp = new Timestamp(calendar.getTime());
        userBooking
                .whereGreaterThan("timestamp", toDayTimeStamp) //if have any document with field done = false
                .whereEqualTo("done", false)
                .limit(1) //Only take 1
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                       if (task.getResult().isEmpty()){
                           //Set data
                           userBooking.document()
                                   .set(bookingInformation)
                                   .addOnSuccessListener(new OnSuccessListener<Void>() {
                                       @Override
                                       public void onSuccess(Void aVoid) {
                                       //Create Notification
                                           MyNotification myNotification = new MyNotification();
                                           myNotification.setUid(UUID.randomUUID().toString());
                                           myNotification.setTitle("New Booking");
                                           myNotification.setContent("You have a new appointment!");
                                           myNotification.setRead(false); //Filter notification with 'raed' is false on Doctor

                                           //Submit Notification to 'Notifications' collection of Doctor
                                           FirebaseFirestore.getInstance()
                                                   .collection("AllHospitals")
                                                   .document(Common.city)
                                                   .collection("Branch")
                                                   .document(Common.currentHospital.getHospitalId())
                                                   .collection("Doctor")
                                                   .document(Common.currentDoctor.getDoctorId())
                                                   .collection("Notifications") //if its not available, it will be created automatically
                                                   .document(myNotification.getUid()) // Create unique key
                                                   .set(myNotification)
                                                   .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                       @Override
                                                       public void onSuccess(Void aVoid) {
                                                          // if (dialog.isShowing())
                                                               dialog.dismiss();

                                                           addToCalendar(Common.bookingDate,
                                                                   Common.convertTimeSlotToString(Common.currentTimeSlot));

                                                           resetStaticData();
                                                           getActivity().finish(); //Close Activity
                                                           Toast.makeText(getContext(), "Success!", Toast.LENGTH_SHORT).show();
                                                       }
                                                   });
                                       }
                                   }).addOnFailureListener(new OnFailureListener() {
                               @Override
                               public void onFailure(@NonNull Exception e) {
                                   if (dialog.isShowing())
                                       dialog.dismiss();
                                   Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                               }
                           });
                       }
                       else {
                           if (dialog.isShowing())
                               dialog.dismiss();
                           resetStaticData();
                           getActivity().finish(); //Close Activity
                           Toast.makeText(getContext(), "Success!", Toast.LENGTH_SHORT).show();
                       }
                    }
                });

    }

    private void addToCalendar(Calendar bookingDate, String startDate) {
        String startTime = Common.convertTimeSlotToString(currentTimeSlot);
        String[] convertTime = startTime.split("-"); //Split ex : 9:00 - 10:00
        //Get start time : get 9:00
        String[] startTimeConvert = convertTime[0].split(":");
        int startHourInt = Integer.parseInt(startTimeConvert[0].trim()); //From this you get 9
        int startMinInt = Integer.parseInt(startTimeConvert[1].trim());  //From this you get 00

        String[] endTimeConvert = convertTime[1].split(":");
        int endHourInt = Integer.parseInt(endTimeConvert[0].trim()); //From this you get 10
        int endMinInt = Integer.parseInt(endTimeConvert[1].trim());  //From this you get 00

        Calendar startEvent = Calendar.getInstance();
        startEvent.setTimeInMillis(bookingDate.getTimeInMillis());
        startEvent.set(Calendar.HOUR_OF_DAY, startHourInt); //Set event start hour
        startEvent.set(Calendar.MINUTE, startMinInt); //Set event start min

        Calendar endEvent = Calendar.getInstance();
        endEvent.setTimeInMillis(bookingDate.getTimeInMillis());
        endEvent.set(Calendar.HOUR_OF_DAY, endHourInt); //Set event end hour
        endEvent.set(Calendar.MINUTE, endMinInt); //Set event end min

        //After we have startEvent and endEvent, convert it to format string
        SimpleDateFormat calendarDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String startEventTime = calendarDateFormat.format(startEvent.getTime());
        String endEventTime = calendarDateFormat.format(endEvent.getTime());

        addToDeviceCalendar(startEventTime, endEventTime, "Doctor Appointment",
                new StringBuilder("Assisted by: ")
        .append(startTime)
        .append("with")
        .append(currentDoctor.getName())
        .append("at")
        .append(currentDoctor.getName()),
                new StringBuilder("Address: ").append(currentHospital.getAddress()).toString());
    }

    private void addToDeviceCalendar(String startEventTime, String endEventTime, String title, StringBuilder description, String location) {
        SimpleDateFormat calendarDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

        try{
            Date start = calendarDateFormat.parse(startEventTime);
            Date end = calendarDateFormat.parse(endEventTime);

            ContentValues event = new ContentValues();

            //Put
            event.put(CalendarContract.Events.CALENDAR_ID, getCalendar(getContext()));
            event.put(CalendarContract.Events.TITLE,title);
            event.put(CalendarContract.Events.DESCRIPTION, String.valueOf(description));
            event.put(CalendarContract.Events.EVENT_LOCATION, location);

            //Time
            event.put(CalendarContract.Events.DTSTART, start.getTime());
            event.put(CalendarContract.Events.DTEND, end.getTime());
            event.put(CalendarContract.Events.ALL_DAY, 0);
            event.put(CalendarContract.Events.HAS_ALARM, 1);

            String timeZone = TimeZone.getDefault().getID();
            event.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone);

            Uri calendars;
            if(Build.VERSION.SDK_INT >= 8)
                calendars = Uri.parse("content://com.android.calendar/events");
            else
                calendars = Uri.parse("content://calendar/events");

            Uri uri_save = getActivity().getContentResolver().insert(calendars, event);
            //Save to cache
            Paper.init(getActivity());
            Paper.book().write(Common.EVENT_URI_CACHE,uri_save.toString());

        } catch (ParseException e){
            e.printStackTrace();
        }
    }

    private String getCalendar(Context context) {
        //Get default calendarId of calendar gmail
        String gmailIdCalendar = "";
        String projection[] = {"_id","calendar_displayName"};
        Uri calendars = Uri.parse("content://com.android.calendar/calendars");

        ContentResolver contentResolver = context.getContentResolver();
        //Select all calendar
        Cursor managedCursor = contentResolver.query(calendars, projection, null, null, null);
        if (managedCursor.moveToFirst())
        {
            String calName;
            int nameCol = managedCursor.getColumnIndex(projection[1]);
            int idCol = managedCursor.getColumnIndex(projection[0]);
            do{
                calName = managedCursor.getString(nameCol);
                if(calName.contains("@gmail.com"))
                {
                    gmailIdCalendar = managedCursor.getString(idCol);
                    break; //Exit as soon as we have id
                }
            }while(managedCursor.moveToNext());
        }
        return gmailIdCalendar;
    }

    private void resetStaticData() {
        Common.step = 0;
        Common.currentTimeSlot = -1;
        Common.currentHospital = null;
        Common.currentDoctor = null;
        Common.bookingDate.add (Calendar.DATE, 0); //Current Date added
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

        dialog = new SpotsDialog.Builder().setContext(getContext()).setCancelable(false).build();

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

