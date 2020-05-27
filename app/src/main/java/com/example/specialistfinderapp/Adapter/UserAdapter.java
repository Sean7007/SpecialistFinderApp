package com.example.specialistfinderapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.specialistfinderapp.MessageActivity;
import com.example.specialistfinderapp.R;
import com.example.specialistfinderapp.User;
import com.example.specialistfinderapp.Users;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context mContext;
    private List<Users> mUsers;

    public UserAdapter(Context mContext, List<Users> mUsers){
        this.mUsers = mUsers;
        this.mContext = mContext;

    }
    public class ViewHolder extends RecyclerView.ViewHolder{
         //Variables
        public TextView username;

        //Constructor
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.username);
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Users user = mUsers.get(position);
        holder.username.setText(user.getcFName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            /*    Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("userid", user.getId());
                mContext.startActivity(intent);*/
            }
        });

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }
}
