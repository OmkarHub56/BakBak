package com.myapps.bakbak.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.myapps.bakbak.Activities.ChatActivity;
import com.myapps.bakbak.Models.MyUsers;
import com.myapps.bakbak.R;

import java.util.List;

public class MyUserlistAdapter extends RecyclerView.Adapter<MyUserlistViewholder> {

    List<MyUsers> list;
    Context context;

    public MyUserlistAdapter(List<MyUsers> list,Context context){
        this.context=context;
        this.list=list;
    }
    @NonNull
    @Override
    public MyUserlistViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.one_user_layout,parent,false);
        return new MyUserlistViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyUserlistViewholder holder, int position) {
        MyUsers user=list.get(position);
//        Glide.with(context).load(user.profilePic).into(holder.image);
        Glide.with(context).load(R.drawable.user_icon).into(holder.image);
        holder.userName.setText(user.getUsername());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, ChatActivity.class);
                intent.putExtra("userId",user.getUserId());
                intent.putExtra("profilePic",user.getProfilePic());
                intent.putExtra("username",user.getUsername());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class MyUserlistViewholder extends RecyclerView.ViewHolder{

    ImageView image;
    TextView userName;
    View itemView;
    public MyUserlistViewholder(@NonNull View itemView) {
        super(itemView);
        this.itemView=itemView;
        image=this.itemView.findViewById(R.id.profile_logo);
        userName=this.itemView.findViewById(R.id.username);
    }
}
