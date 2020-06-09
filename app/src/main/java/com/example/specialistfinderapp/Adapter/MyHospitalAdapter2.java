package com.example.specialistfinderapp.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.specialistfinderapp.Genysis.CustomerLogin;
import com.example.specialistfinderapp.Genysis.SpecialistCustomLoginDialog;
import com.example.specialistfinderapp.Interface.IDialogClickListener;
import com.example.specialistfinderapp.Interface.IGetDoctorListener;
import com.example.specialistfinderapp.Interface.IRecyclerItemSelectedListener;
import com.example.specialistfinderapp.Interface.IUserLoginRememberListener;
import com.example.specialistfinderapp.MainActivity;
import com.example.specialistfinderapp.Model.Doctor;
import com.example.specialistfinderapp.Model.Hospital;
import com.example.specialistfinderapp.R;
import com.example.specialistfinderapp.SpecialistFragments.HospitalListActivity;
import com.example.specialistfinderapp.SpecialistFragments.SpecialistFragmentHome1;
import com.example.specialistfinderapp.SpecialistFragments.StaffActivityHome;
import com.example.specialistfinderapp.util.Common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class MyHospitalAdapter2 extends RecyclerView.Adapter<MyHospitalAdapter2.MyViewHolder> implements IDialogClickListener {

    Context context;
    List<Hospital> hospitalList;
    List<CardView> cardViewList;
    Button back;

    IUserLoginRememberListener iUserLoginRememberListener;
    IGetDoctorListener iGetDoctorListener;

    public MyHospitalAdapter2(Context context, List<Hospital> hospitalList, IUserLoginRememberListener iUserLoginRememberListener, IGetDoctorListener iGetDoctorListener) {
        this.context = context;
        this.hospitalList = hospitalList;
        cardViewList = new ArrayList<>();
        this.iGetDoctorListener = iGetDoctorListener;
        this.iUserLoginRememberListener = iUserLoginRememberListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_hospital2,parent, false);
        back =itemView.findViewById(R.id.back);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
       holder.txt_hospital_name.setText(hospitalList.get(position).getName());
       holder.txt_hospital_address.setText(hospitalList.get(position).getAddress());

       if(!cardViewList.contains(holder.card_hospital))
           cardViewList.add(holder.card_hospital);

       holder.setiRecyclerItemSelectedListener(new IRecyclerItemSelectedListener() {
                   @Override
                   public void onItemSelectedListener(View view, int pos) {
                       Common.selectedHospital = hospitalList.get(pos);
                       showLoginDialog();
                   }
               });
    }

    private void showLoginDialog() {
        SpecialistCustomLoginDialog.getInstance()
                .showLoginDialog("SPECIALIST LOGIN",
                        "LOGIN",
                        "BACK",
                        context,
                        this);
    }

    @Override
    public int getItemCount() {
        return hospitalList.size();
    }

    @Override
    public void onClickPositiveButton(DialogInterface dialogInterface, String userName, String password) {
      //Show loading
        AlertDialog loading = new SpotsDialog.Builder().setCancelable(false).setContext(context).build();
        loading.show();
        FirebaseFirestore.getInstance().collection("AllHospitals")
                .document(Common.state_name)
                .collection("Branch")
                .document(Common.selectedHospital.getHospitalId())
                .collection("Doctors")
                .whereEqualTo("username", userName)
                .whereEqualTo("password", password)
                .limit(1)
                .get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().size() > 0) {
                        dialogInterface.dismiss();
                        loading.dismiss();

                        //Allow for auto-login
                        iUserLoginRememberListener.onUserLoginSuccess(userName);
                        //Create Doctor
                        Doctor doctor = new Doctor();
                        for (DocumentSnapshot doctorSnapShot : task.getResult()) {
                            doctor = doctorSnapShot.toObject(Doctor.class);
                            doctor.setDoctorId(doctorSnapShot.getId());
                        }
                        iGetDoctorListener.onGetDoctorSuccess(doctor);

                        //Navigate Staff home and clear previous activity
                        Intent staffHome = new Intent(context, StaffActivityHome.class);
                        staffHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        staffHome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(staffHome);
                    } else {
                        Toast.makeText(context, "Wrong Username/password or Wrong Hospital", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }

    @Override
    public void onClickNegativeButton(DialogInterface dialogInterface) {
      dialogInterface.dismiss();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_hospital_name, txt_hospital_address;
        CardView card_hospital;

        IRecyclerItemSelectedListener iRecyclerItemSelectedListener;

        public void setiRecyclerItemSelectedListener(IRecyclerItemSelectedListener iRecyclerItemSelectedListener) {
            this.iRecyclerItemSelectedListener = iRecyclerItemSelectedListener;
        }


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_hospital_address = (TextView) itemView.findViewById(R.id.txt_hospital_address);
            txt_hospital_name = (TextView) itemView.findViewById(R.id.txt_hospital_name);
            card_hospital = (CardView) itemView.findViewById(R.id.card_hospital);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            iRecyclerItemSelectedListener.onItemSelectedListener(v, getAdapterPosition());
        }
    }
}
