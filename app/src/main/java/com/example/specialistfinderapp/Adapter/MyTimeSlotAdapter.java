package com.example.specialistfinderapp.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.specialistfinderapp.Model.TimeSlot;
import com.example.specialistfinderapp.R;
import com.example.specialistfinderapp.util.Common;

import java.util.ArrayList;
import java.util.List;

public class MyTimeSlotAdapter extends RecyclerView.Adapter<MyTimeSlotAdapter.MyViewHolder> {
    //Variables
    Context context;
    List<TimeSlot> timeSlotList;

    public MyTimeSlotAdapter(Context context) {
        this.context = context;
        this.timeSlotList = new ArrayList<>();
    }

    public MyTimeSlotAdapter(Context context, List<TimeSlot> timeSlotList) {
        
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txt_time_slot.setText(new StringBuffer(Common.convertTimeSlotToString(position)).toString());
        if(timeSlotList.size() == 0) //If all position is available, just show list
        {
            holder.txt_time_slot_description.setText("Available");
            holder.txt_time_slot_description.setTextColor(context.getResources()
            .getColor(android.R.color.white));
            holder.txt_time_slot.setTextColor(context.getResources().getColor(android.R.color.black));
            holder.card_time_slot.setCardBackgroundColor(context.getResources().getColor(android.R.color.white));
        }
        else{//If have position is full (booked)
            for(TimeSlot slotValue: timeSlotList)
            {
                //Loop all time slot from server and set different color
                int slot = Integer.parseInt(slotValue.getShot().toString());
                if(slot == position) //If slot == posistion
                {
                    holder.card_time_slot.setCardBackgroundColor(context.getResources().getColor(android.R.color.darker_gray));
                    holder.txt_time_slot_description.setText("Full");
                    holder.txt_time_slot_description.setTextColor(context.getResources().getColor(android.R.color.white));
                }
            }

        }
    }

    @Override
    public int getItemCount() {
        return Common.TIME_SLOT_TOTAL;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_time_slot, txt_time_slot_description;
        CardView card_time_slot;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            card_time_slot = (CardView) itemView.findViewById(R.id.card_time_slot);
            txt_time_slot = (TextView) itemView.findViewById(R.id.txt_time_slot);
            txt_time_slot_description = (TextView) itemView.findViewById(R.id.txt_time_slot_description);
        }
    }
}
