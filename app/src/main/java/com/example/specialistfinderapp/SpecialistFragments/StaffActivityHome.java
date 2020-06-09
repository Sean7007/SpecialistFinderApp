package com.example.specialistfinderapp.SpecialistFragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.specialistfinderapp.Adapter.MyTimeSlotAdapter2;
import com.example.specialistfinderapp.Genysis.SpecialistHome2;
import com.example.specialistfinderapp.Genysis.SpecialistLogin;
import com.example.specialistfinderapp.Interface.INotificationCountListener;
import com.example.specialistfinderapp.Interface.ITimeSlotLoadListener2;
import com.example.specialistfinderapp.Model.TimeSlot;
import com.example.specialistfinderapp.R;

import com.example.specialistfinderapp.util.Common;
import com.example.specialistfinderapp.util.SpacesItemDecoration;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;
import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;

import static com.example.specialistfinderapp.util.Common.simpleDateFormat;

public class StaffActivityHome extends AppCompatActivity implements ITimeSlotLoadListener2, INotificationCountListener, NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.activity_main)
    DrawerLayout drawerLayout;
    @BindView(R.id.navigation_view)
    NavigationView navigationView;

    ActionBarDrawerToggle actiionBarDrawerToggle;
    ITimeSlotLoadListener2 iTimeSlotLoadListener2;
    AlertDialog alertDialog;

    @BindView(R.id.recycler_time_slot)
    RecyclerView recycler_time_slot;
    @BindView(R.id.calendarView)
    HorizontalCalendarView calendarView;
    DocumentReference doctorDoc;

    //Notification variables
    TextView txt_notification_badge;
    CollectionReference notificationCollection;
    CollectionReference currentBookDateCollection;

    EventListener<QuerySnapshot> notificationEvent;
    EventListener<QuerySnapshot> bookingEvent;

    ListenerRegistration notificationListener;
    ListenerRegistration bookingRealtimelistener;

    INotificationCountListener iNotificationCountListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_home);

        drawerLayout = findViewById(R.id.activity_main);
        navigationView = findViewById(R.id.navigation_view);

        Button map = findViewById(R.id.map);
        Button chat = findViewById(R.id.chat);

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StaffActivityHome.this, SpecialistLogin.class);
                startActivity(intent);
                return;
            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StaffActivityHome.this, SpecChatFragment.class);
                startActivity(intent);
                return;
            }
        });


        ButterKnife.bind(this);
        init();
        initView();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return true;
    }

    private void initView() {

        actiionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(actiionBarDrawerToggle);
        actiionBarDrawerToggle.syncState();

       if(drawerLayout != null){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId() == R.id.exit)
                         logOut();
                          return true;
            }
        });
       }

        //Copy from FragmentStep3
        alertDialog = new SpotsDialog.Builder().setCancelable(false).setContext(this).build();

        Calendar date = Calendar.getInstance();
        date.add(Calendar.DATE, 0);
        loadAvailableTimeSlotOfDoctor(Common.currentDoctor.getDoctorId(),Common.simpleFormatDate.format(date.getTime()));

        //
        recycler_time_slot.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recycler_time_slot.setLayoutManager(layoutManager);
        recycler_time_slot.addItemDecoration(new SpacesItemDecoration(8));

        //Calendar
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.DATE, 0);
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.DATE, 2);

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(1)
                .mode(HorizontalCalendar.Mode.DAYS)
                .defaultSelectedDate(startDate)
                .configure()
                .end()
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

    private void logOut() {
        //Delete all remembering keys and start SpecialistHome2
        Paper.init(this);
        Paper.book().delete(Common.HOSPITAL_KEY);
        Paper.book().delete(Common.DOCTOR_KEY);
        Paper.book().delete(Common.STATE_KEY);
        Paper.book().delete(Common.LOGGED_KEY);

        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to Logout?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent mainActivity = new Intent(StaffActivityHome.this, SpecialistHome2.class);
                        mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        mainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(mainActivity);
                        finish();
                    }
                }).setNegativeButton("BACK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        }).show();
    }

    private void loadAvailableTimeSlotOfDoctor(String doctorId, String bookDate) {
        //
        alertDialog.show();


        //Get Information of this doctor
        doctorDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) //if doc is available
                    {
                        //Get information of booking
                        //If not created, return empty
                        CollectionReference date = FirebaseFirestore.getInstance()
                                .collection("AllHospitals")
                                .document(Common.state_name)
                                .collection("Branch")
                                .document(Common.selectedHospital.getHospitalId())
                                .collection("Doctors")
                                .document(doctorId)
                                .collection(bookDate);

                        date.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    QuerySnapshot querySnapshot = task.getResult();
                                    if (querySnapshot.isEmpty()) //If there is no appointment
                                        iTimeSlotLoadListener2.onTimeSlotLoadEmpty();
                                    else {
                                        //If have an appointment
                                        List<TimeSlot> timeSlots = new ArrayList<>();
                                        for (QueryDocumentSnapshot document : task.getResult())
                                            timeSlots.add(document.toObject(TimeSlot.class));
                                        iTimeSlotLoadListener2.onTimeSlotLoadSuccess(timeSlots);
                                    }
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                iTimeSlotLoadListener2.onTimeSlotLoadFailed(e.getMessage());

                            }
                        });
                    }
                }
            }
        });

    }

    private void init() {
        initNotificationRealtimeUpdate();
        iNotificationCountListener = this;
        iTimeSlotLoadListener2 = this;
        initBookingRealtimeUpdate();
    }

    private void initBookingRealtimeUpdate() {
        doctorDoc = FirebaseFirestore.getInstance()
                .collection("AllHospitals")
                .document(Common.state_name)
                .collection("Branch")
                .document(Common.selectedHospital.getHospitalId())
                .collection("Doctors")
                .document(Common.currentDoctor.getDoctorId());

        //Get Current date
        final Calendar date = Calendar.getInstance();
        date.add(Calendar.DATE,0);
        bookingEvent = new EventListener<QuerySnapshot>(){
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                //If have any new booking, update adapter
                loadAvailableTimeSlotOfDoctor(Common.currentDoctor.getDoctorId(),
                        Common.simpleDateFormat.format(date.getTime()));
            }
        };
        currentBookDateCollection = doctorDoc.collection(Common.simpleDateFormat.format(date.getTime()));
        bookingRealtimelistener = currentBookDateCollection.addSnapshotListener(bookingEvent);
    }

    private void initNotificationRealtimeUpdate() {
        notificationCollection = FirebaseFirestore.getInstance()
                .collection("AllHospitals")
                .document(Common.state_name)
                .collection("Branch")
                .document(Common.selectedHospital.getHospitalId())
                .collection("Doctors")
                .document(Common.currentDoctor.getDoctorId())
                .collection("Notifications");

        notificationEvent = new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(queryDocumentSnapshots.size() > 0)
                    loadNotification();
            }
        };
        notificationListener = notificationCollection.whereEqualTo("read", false) //Only listen and count all notifications
        .addSnapshotListener(notificationEvent);

    }

    @Override
    public void onBackPressed(){
        //To ensure the application does not close when navigation is opened
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }

        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(StaffActivityHome.this, "Exit" , Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("BACK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        }).show();
    }

    @Override
    public void onTimeSlotLoadSuccess(List<TimeSlot> timeSlot) {
        MyTimeSlotAdapter2 adapter = new MyTimeSlotAdapter2(this, timeSlot);
        recycler_time_slot.setAdapter(adapter);

        alertDialog.dismiss();
    }

    @Override
    public void onTimeSlotLoadFailed(String message) {
      Toast.makeText(this,"" + message, Toast.LENGTH_SHORT).show();
      alertDialog.dismiss();
    }

    @Override
    public void onTimeSlotLoadEmpty() {
     MyTimeSlotAdapter2 adapter = new MyTimeSlotAdapter2(this);
     recycler_time_slot.setAdapter(adapter);

     alertDialog.dismiss();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.staff_home_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_new_notification);

        txt_notification_badge = (TextView)menuItem.getActionView().findViewById(R.id.notification_badge);
        loadNotification();
        menuItem.getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void loadNotification() {
     notificationCollection.whereEqualTo("read", false)
             .get()
             .addOnFailureListener(new OnFailureListener() {
                 @Override
                 public void onFailure(@NonNull Exception e) {
                     Toast.makeText(StaffActivityHome.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                 }
             }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
         @Override
         public void onComplete(@NonNull Task<QuerySnapshot> task) {
             if(task.isSuccessful()){
                 iNotificationCountListener.onNotificationCountSuccess(task.getResult().size());
             }
         }
     });
    }

    @Override
    public void onNotificationCountSuccess(int count) {
      if(count == 0)
          txt_notification_badge.setVisibility(View.INVISIBLE);
      else
      {
          txt_notification_badge.setVisibility(View.VISIBLE);
          if (count <= 9)
              txt_notification_badge.setText(String.valueOf(count));
          else
              txt_notification_badge.setText("9+");
      }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initBookingRealtimeUpdate();
        initNotificationRealtimeUpdate();
    }

    @Override
    protected void onStop() {
        if(notificationListener != null)
            notificationListener.remove();
        if (bookingRealtimelistener != null)
            bookingRealtimelistener.remove();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if(notificationListener != null)
            notificationListener.remove();
        if (bookingRealtimelistener != null)
            bookingRealtimelistener.remove();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){

            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(StaffActivityHome.this, SpecialistLogin.class));
                finish();
                return true;
        }
        return false;
    }


}
