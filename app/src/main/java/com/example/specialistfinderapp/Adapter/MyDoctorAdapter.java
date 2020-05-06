package com.example.specialistfinderapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.specialistfinderapp.Model.Doctor;
import com.example.specialistfinderapp.R;

import java.util.List;

public class MyDoctorAdapter extends RecyclerView.Adapter<MyDoctorAdapter.MyViewHolder> {
    Context context;
    List<Doctor> doctorList;

    public MyDoctorAdapter(Context context, List<Doctor> doctorList) {
        this.context = context;
        this.doctorList = doctorList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_doctor, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
       holder.txt_doctor_name.setText(doctorList.get(position).getName());
       holder.ratingBar.setRating((float)doctorList.get(position).getRating());
    }

    @Override
    public int getItemCount() {
        return doctorList.size();
    }

    //Constructor
    public class MyViewHolder extends RecyclerView.ViewHolder {
        //Variable
        TextView txt_doctor_name;
        RatingBar ratingBar;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_doctor_name = (TextView) itemView.findViewById(R.id.txt_doctor_name);
            ratingBar = (RatingBar) itemView.findViewById(R.id.rtb_doctor);
        }
    }
}
