package com.example.specialistfinderapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.specialistfinderapp.Interface.IRecyclerItemSelectedListener;
import com.example.specialistfinderapp.Interface.IRecyclerItemSelectedListener2;
import com.example.specialistfinderapp.Model.City;
import com.example.specialistfinderapp.R;
import com.example.specialistfinderapp.SpecialistFragments.HospitalListActivity;
import com.example.specialistfinderapp.util.Common;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import devs.mulham.horizontalcalendar.HorizontalCalendar;

public class MyStateAdapter extends RecyclerView.Adapter<MyStateAdapter.MyViewHolder> {
    Context context;
    List<City> cityList;

    int lastPosition = -1;

    public MyStateAdapter(Context context, List<City> cityList) {
        this.context = context;
        this.cityList = cityList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_state, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
       holder.txt_state_name.setText(cityList.get(i).getName());

       setAnimation(holder.itemView, i);
       holder.setiRecyclerItemSelectedListener2(new IRecyclerItemSelectedListener2() {
           @Override
           public void onItemSelectedListener(View view, int pos) {
               Common.state_name = cityList.get(i).getName();
               context.startActivity(new Intent(context, HospitalListActivity.class));
           }
       });
    }

    private void setAnimation(View itemView, int position) {
        if(position > lastPosition){
            Animation animation = AnimationUtils.loadAnimation(context,android.R.anim.slide_in_left);
            itemView.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    public class MyViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.txt_state_name)
        TextView txt_state_name;
        IRecyclerItemSelectedListener2 iRecyclerItemSelectedListener2;

        public void setiRecyclerItemSelectedListener2(IRecyclerItemSelectedListener2 iRecyclerItemSelectedListener2) {
            this.iRecyclerItemSelectedListener2 = iRecyclerItemSelectedListener2;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            iRecyclerItemSelectedListener2.onItemSelectedListener(v, getAdapterPosition());
        }
    }
}
