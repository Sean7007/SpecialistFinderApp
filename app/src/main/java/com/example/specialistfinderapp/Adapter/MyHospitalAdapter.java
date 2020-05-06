package com.example.specialistfinderapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.specialistfinderapp.Model.Hospital;
import com.example.specialistfinderapp.R;

import java.util.List;

public class MyHospitalAdapter extends RecyclerView.Adapter<MyHospitalAdapter.MyViewHolder> {

    Context context;
    List<Hospital> hospitalList;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_hospital,parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
       holder.txt_hospital_name.setText(hospitalList.get(position).getName());
       holder.txt_hospital_address.setText(hospitalList.get(position).getAddress());
    }

    @Override
    public int getItemCount() {

        return hospitalList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txt_hospital_name, txt_hospital_address;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_hospital_address = (TextView) itemView.findViewById(R.id.txt_hospital_address);
            txt_hospital_name = (TextView) itemView.findViewById(R.id.txt_hospital_name);

        }
    }
}
