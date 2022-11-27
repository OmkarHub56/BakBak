package com.myapps.bakbak.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.myapps.bakbak.Models.MessageModel;
import com.myapps.bakbak.R;

import java.util.List;

public class ChatsAdapter extends RecyclerView.Adapter{
    Context context;
    List<MessageModel> list;

    int SENDER_VIEW_TYPE=1,RECEIVER_VIEW_TYPE=2;
    public ChatsAdapter(Context context,List<MessageModel> list){
        this.context=context;
        this.list=list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==SENDER_VIEW_TYPE){
            View view=LayoutInflater.from(context).inflate(R.layout.sample_sender,parent,false);
            return new SenderChatViewHolder(view);
        }
        else{
            View view=LayoutInflater.from(context).inflate(R.layout.sample_receiver,parent,false);
            return new ReceiverChatViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageModel mgl=list.get(position);
        if(holder.getClass()==SenderChatViewHolder.class){
            SenderChatViewHolder vh=(SenderChatViewHolder) holder;
            vh.txtmessage.setText(mgl.getMessage());
        }
        else{
            ReceiverChatViewHolder vh=(ReceiverChatViewHolder) holder;
            vh.txtmessage.setText(mgl.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(list.get(position).getuId().equals(FirebaseAuth.getInstance().getUid())){
            return SENDER_VIEW_TYPE;
        }
        else{
            return RECEIVER_VIEW_TYPE;
        }
    }
}

class ReceiverChatViewHolder extends RecyclerView.ViewHolder{

    TextView txtmessage;
    public ReceiverChatViewHolder(@NonNull View itemView) {
        super(itemView);
        txtmessage= itemView.findViewById(R.id.rec_m);
    }
}

class SenderChatViewHolder extends RecyclerView.ViewHolder{

    TextView txtmessage;
    public SenderChatViewHolder(@NonNull View itemView) {
        super(itemView);
        txtmessage= itemView.findViewById(R.id.sen_m);
    }
}

