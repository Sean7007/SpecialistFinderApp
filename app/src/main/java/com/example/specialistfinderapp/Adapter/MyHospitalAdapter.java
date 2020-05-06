package com.example.specialistfinderapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.specialistfinderapp.Interface.IRecyclerItemSelectedListener;
import com.example.specialistfinderapp.Model.Hospital;
import com.example.specialistfinderapp.R;
import com.example.specialistfinderapp.util.Common;

import java.util.ArrayList;
import java.util.List;

public class MyHospitalAdapter extends RecyclerView.Adapter<MyHospitalAdapter.MyViewHolder> {

    Context context;
    List<Hospital> hospitalList;
    List<CardView> cardViewList;
    LocalBroadcastManager localBroadcastManager;

    public MyHospitalAdapter(Context context, List<Hospital> hospitalList) {
        this.context = context;
        this.hospitalList = hospitalList;
        cardViewList = new ArrayList<>();
        localBroadcastManager = LocalBroadcastManager.getInstance(context);

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_hospital,parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
       holder.txt_hospital_name.setText(hospitalList.get(position).getName());
       holder.txt_hospital_address.setText(hospitalList.get(position).getAddress());

       if(!cardViewList.contains(holder.card_hospital))
           cardViewList.add(holder.card_hospital);

       holder.setiRecyclerItemSelectedListener
               (new IRecyclerItemSelectedListener() {
                   @Override
                   public void onItemSelectedListener(View view, int pos) {
                       for(CardView cardView: cardViewList)
                           //Set background for all card not selected to White
                           cardView.setCardBackgroundColor(context.getResources().getColor(android.R.color.white));

                       //Set selected BG for only selected item
                       holder.card_hospital.setCardBackgroundColor(context.getResources()
                       .getColor(android.R.color.holo_orange_dark));

                       //Send Broadcast to tell Booking Activity enable Button next
                       Intent intent = new Intent(Common.KEY_ENALBE_BUTTON_NEXT);
                       intent.putExtra(Common.KEY_HOSPITAL_STORE,hospitalList.get(pos));
                       localBroadcastManager.sendBroadcast(intent);
                   }
               }
       );
    }

    @Override
    public int getItemCount() {
        return hospitalList.size();
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
