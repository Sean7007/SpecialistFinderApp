package com.example.specialistfinderapp.Genysis;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.specialistfinderapp.Adapter.MyStateAdapter;
import com.example.specialistfinderapp.Interface.IOnAllStateLoadListener;
import com.example.specialistfinderapp.Model.City;
import com.example.specialistfinderapp.Model.Doctor;
import com.example.specialistfinderapp.Model.Hospital;
import com.example.specialistfinderapp.R;
import com.example.specialistfinderapp.SpecialistFragments.StaffActivityHome;
import com.example.specialistfinderapp.util.Common;
import com.example.specialistfinderapp.util.SpacesItemDecoration;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;

public class SpecialistHome2 extends AppCompatActivity implements IOnAllStateLoadListener {

    @BindView(R.id.recycler_state)
    RecyclerView recycler_state;
    CollectionReference allHospitalCollection;

    IOnAllStateLoadListener iOnAllStateLoadListener;
    MyStateAdapter adapter;
    AlertDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //CHECK IF USER IS LOGGED IN
        Paper.init(this);
        String user = Paper.book().read(Common.LOGGED_KEY);
        if(TextUtils.isEmpty(user)){
            setContentView(R.layout.activity_specialist_login_state);
            ButterKnife.bind(this);

            init();
            initView();
            loadAllStateFromFireStore();

        }else{ //If user already login
            //Auto login
            Gson gson = new Gson();
            Common.state_name = Paper.book().read(Common.STATE_KEY);
            Common.selectedHospital = gson.fromJson(Paper.book().read(Common.HOSPITAL_KEY, ""),
                    new TypeToken<Hospital>(){}.getType());
            Common.currentDoctor = gson.fromJson(Paper.book().read(Common.DOCTOR_KEY, ""),
                    new TypeToken<Doctor>(){}.getType());

            //Intent
            Intent intent = new Intent(this, StaffActivityHome.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }

    private void loadAllStateFromFireStore() {
        dialog.show();

        allHospitalCollection
                .get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        iOnAllStateLoadListener.onAllStateLoadFailed(e.getMessage());
                    }
                }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
              if(task.isSuccessful()){
                  List<City> cities = new ArrayList<>();
                  for (DocumentSnapshot citySnapShot: task.getResult()){
                      City city = citySnapShot.toObject(City.class);
                      cities.add(city);
                  }
                  iOnAllStateLoadListener.onAllStateLoadSuccess(cities);
              }
            }
        });
    }

    private void init() {
        allHospitalCollection = FirebaseFirestore.getInstance().collection("AllHospitals");
        iOnAllStateLoadListener = this;

        dialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();
    }

    private void initView() {
        recycler_state.setHasFixedSize(true);
        recycler_state.setLayoutManager(new GridLayoutManager(this,2));
        recycler_state.addItemDecoration(new SpacesItemDecoration(8));
    }

    @Override
    public void onAllStateLoadSuccess(List<City> cityList) {
     adapter = new MyStateAdapter(this, cityList);
     recycler_state.setAdapter(adapter);

     dialog.dismiss();
    }

    @Override
    public void onAllStateLoadFailed(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        dialog.dismiss();

    }
}
