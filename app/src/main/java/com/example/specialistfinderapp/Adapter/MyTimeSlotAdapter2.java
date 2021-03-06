package com.example.specialistfinderapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.specialistfinderapp.Interface.IRecyclerItemSelectedListener;
import com.example.specialistfinderapp.Model.TimeSlot;
import com.example.specialistfinderapp.R;
import com.example.specialistfinderapp.util.Common;

import java.util.ArrayList;
import java.util.List;

public class MyTimeSlotAdapter2 extends RecyclerView.Adapter<MyTimeSlotAdapter2.MyViewHolder> {
        //Variables
        Context context;
        List<TimeSlot> timeSlotList;
            List<CardView> cardViewList;

    public MyTimeSlotAdapter2(Context context) {
            this.context = context;
            this.timeSlotList = new ArrayList<>();
            cardViewList = new ArrayList<>();

            }

    public MyTimeSlotAdapter2(Context context, List<TimeSlot> timeSlotList) {
            this.context = context;
            this.timeSlotList = new ArrayList<>(); //for now
            cardViewList = new ArrayList<>();
            }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.layout_time_slot, parent, false);
            return new MyViewHolder(itemView);
            }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
            holder.txt_time_slot.setText(new StringBuilder(Common.convertTimeSlotToString(position)).toString());

            if(timeSlotList.size() == 0) //If all position is available, just show list
            {            holder.card_time_slot.setEnabled(true);

            holder.txt_time_slot_description.setText("Available");
            holder.txt_time_slot_description.setTextColor(context.getResources()
            .getColor(android.R.color.black));
            holder.txt_time_slot.setTextColor(context.getResources().getColor(android.R.color.black));
            holder.card_time_slot.setCardBackgroundColor(context.getResources().getColor(android.R.color.white));
            }
            else{//If have position is full (booked)
            for(TimeSlot slotValue: timeSlotList)
            {
            //Loop all time slot from server and set different color
            int slot = Integer.parseInt(slotValue.getShot().toString());
            if(slot == position) //If slot == position
            {
            holder.card_time_slot.setEnabled(false);
            //We will set tag for all time slot is full
            //So base on tag, we can all remain card background without change full time slow
            holder.card_time_slot.setTag(Common.DISABLE_TAG);
            holder.card_time_slot.setCardBackgroundColor(context.getResources().getColor(android.R.color.darker_gray));
            holder.txt_time_slot_description.setText("Full");
            holder.txt_time_slot_description.setTextColor(context.getResources().getColor(android.R.color.black));
            holder.txt_time_slot.setTextColor(context.getResources().getColor(android.R.color.white));
            }
            }
            }
            //Add only available time_slot card to list which is 20 cards
            if(!cardViewList.contains(holder.card_time_slot))
            cardViewList.add(holder.card_time_slot);

            //Check if card time slot is available
            if(!timeSlotList.contains(position)) {

            holder.setiRecyclerItemSelectedListener(new IRecyclerItemSelectedListener() {
    @Override
    public void onItemSelectedListener(View view, int pos) {
            //Loop all card in card list
            for (CardView cardView : cardViewList) {
                if (cardView.getTag() == null) //Only available card time slot can be changed
                    cardView.setCardBackgroundColor(context.getResources()
                     .getColor(android.R.color.white));
            }
             //Our selected card will be change color
            holder.card_time_slot.setCardBackgroundColor(context.getResources()
                       .getColor(android.R.color.holo_orange_dark));

            //After that, send broadcast to enable button NEXT
     /*   Intent intent = new Intent(Common.KEY_ENABLE_BUTTON_NEXT);
        intent.putExtra(Common.KEY_TIME_SLOT, position); //Put index of time slot we have selected
        intent.putExtra(Common.KEY_STEP, 3); //Go to step 3 */
            }
            });
            }
    }

    @Override
    public int getItemCount() {
            return Common.TIME_SLOT_TOTAL;
            }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_time_slot, txt_time_slot_description;
        CardView card_time_slot;

        IRecyclerItemSelectedListener iRecyclerItemSelectedListener;

        public void setiRecyclerItemSelectedListener(IRecyclerItemSelectedListener iRecyclerItemSelectedListener) {
            this.iRecyclerItemSelectedListener = iRecyclerItemSelectedListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            card_time_slot = (CardView) itemView.findViewById(R.id.card_time_slot);
            txt_time_slot = (TextView) itemView.findViewById(R.id.txt_time_slot);
            txt_time_slot_description = (TextView) itemView.findViewById(R.id.txt_time_slot_description);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            this.iRecyclerItemSelectedListener.onItemSelectedListener(v, getAdapterPosition());
        }
    }
}

