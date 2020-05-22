package com.example.specialistfinderapp.SpecialistFragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.specialistfinderapp.Adapter.MyHospitalAdapter;
import com.example.specialistfinderapp.Adapter.MyHospitalAdapter2;
import com.example.specialistfinderapp.Interface.IBranchLoadListener;
import com.example.specialistfinderapp.Interface.IGetDoctorListener;
import com.example.specialistfinderapp.Interface.IOnLoadCountHospital;
import com.example.specialistfinderapp.Interface.IUserLoginRememberListener;
import com.example.specialistfinderapp.Model.Doctor;
import com.example.specialistfinderapp.Model.Hospital;
import com.example.specialistfinderapp.R;
import com.example.specialistfinderapp.util.Common;
import com.example.specialistfinderapp.util.SpacesItemDecoration;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;

public class HospitalListActivity extends AppCompatActivity implements IOnLoadCountHospital, IBranchLoadListener, IGetDoctorListener, IUserLoginRememberListener {

    @BindView(R.id.txt_hospital_count)
    TextView txt_hospital_count;
    @BindView(R.id.recycler_hospital)
    RecyclerView recycler_hospital;

    IOnLoadCountHospital iOnLoadCountHospital;
    IBranchLoadListener iBranchLoadListener;

    AlertDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_list);

        ButterKnife.bind(this);
        initView();

        init();
        loadHospitalBaseOnCity(Common.state_name);
    }

    private void loadHospitalBaseOnCity(String name) {
       dialog.show();
      //AllHospitals/Francistown/Branch/ANkTqWDsYXs8FRJMhRcd/Doctors
        FirebaseFirestore.getInstance().collection("AllHospitals")
                .document(name)
                .collection("Branch")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                      if(task.isSuccessful()){
                          List<Hospital> hospitals = new ArrayList<>();
                          iOnLoadCountHospital.onLoadCountHospitalSuccess(task.getResult().size());
                          for (DocumentSnapshot hospitalSnapShot: task.getResult()){
                              Hospital hospital = hospitalSnapShot.toObject(Hospital.class);
                              hospital.setHospitalId(hospitalSnapShot.getId());
                              hospitals.add(hospital);
                          }
                          iBranchLoadListener.onBranchLoadSuccess(hospitals);
                      }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iBranchLoadListener.onBranchLoadFailed(e.getMessage());
            }
        });
    }

    private void init() {
        dialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();
        iOnLoadCountHospital = this;
        iBranchLoadListener = this;
    }

    private void initView() {
        recycler_hospital.setHasFixedSize(true);
        recycler_hospital.setLayoutManager(new GridLayoutManager(this,2));
        recycler_hospital.addItemDecoration(new SpacesItemDecoration(8));
    }

    @Override
    public void onLoadCountHospitalSuccess(int count) {
     txt_hospital_count.setText(new StringBuilder("All Hospitals (")
     .append(count)
     .append(")"));
    }

    @Override
    public void onBranchLoadSuccess(List<Hospital> hospitalListList) {
        MyHospitalAdapter2 hospitalAdapter = new MyHospitalAdapter2(this, hospitalListList, this, this);
        recycler_hospital.setAdapter(hospitalAdapter);

        dialog.dismiss();
    }

    @Override
    public void onBranchLoadFailed(String message) {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }

    @Override
    public void onGetDoctorSuccess(Doctor doctor) {
     Common.currentDoctor = doctor;
     Paper.book().write(Common.DOCTOR_KEY, new Gson().toJson(doctor));
    }

    @Override
    public void onUserLoginSuccess(String user) {
        //Save user
        Paper.init(this);
        Paper.book().write(Common.LOGGED_KEY,user);
        Paper.book().write(Common.STATE_KEY, Common.state_name);
        Paper.book().write(Common.HOSPITAL_KEY, new Gson().toJson(Common.selectedHospital));

    }
}
