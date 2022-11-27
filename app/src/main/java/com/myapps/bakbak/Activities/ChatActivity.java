package com.myapps.bakbak.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.myapps.bakbak.Adapters.ChatsAdapter;
import com.myapps.bakbak.Models.MessageModel;
import com.myapps.bakbak.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    // screen dimensions utility purposes
    int screenWidth,screenHeight;
    float screenDensity;

    LinearLayout enterMessage;
    EditText enteredMessage;

    FirebaseDatabase database;
    FirebaseAuth auth;
    CircleImageView receiver_profile_pic;

    String sender_id;
    String receiver_id;

    String senderRoom,receiverRoom;

    List<MessageModel> messageList=new ArrayList<>();
    ChatsAdapter adapter;
    RecyclerView chatsView;

    TextView receiverUsername;

    TextView receiver_status;
    CircleImageView status_symbol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        screenWidth = getResources().getDisplayMetrics().widthPixels;
        screenHeight=getResources().getDisplayMetrics().heightPixels;
        screenDensity=getResources().getDisplayMetrics().density;

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        sender_id=auth.getUid();
        receiver_id=getIntent().getStringExtra("userId");

        senderRoom=sender_id+receiver_id;
        receiverRoom=receiver_id+sender_id;

        adapter=new ChatsAdapter(this,messageList);
        chatsView=findViewById(R.id.chats_recyclerview);
        LinearLayoutManager lmg=new LinearLayoutManager(this);

        lmg.setStackFromEnd(true);
        chatsView.setLayoutManager(lmg);
        chatsView.setAdapter(adapter);

        receiverUsername=findViewById(R.id.receiver_username);
        receiverUsername.setText(getIntent().getStringExtra("username"));

        receiver_status=findViewById(R.id.online_offline_status);
        status_symbol=findViewById(R.id.status_symbol);

        enteredMessage=findViewById(R.id.message);
//        enteredMessage.setTextSize(screenHeight/100f);

        receiver_profile_pic=findViewById(R.id.receiver_profilepic);
//        receiver_profile_pic.setImageResource(getIntent().getStringExtra("profilePic"));
        String profilePicture=getIntent().getStringExtra("profilePic");
        if(profilePicture==null){
            Glide.with(this).load(R.drawable.user_icon).into(receiver_profile_pic);
        }
        else{
            Glide.with(this).load(profilePicture).into(receiver_profile_pic);
        }

        database.getReference().child("Chats").child(senderRoom).child("messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // temporary arrangement
                messageList.clear();
                
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    MessageModel msg=dataSnapshot.getValue(MessageModel.class);
                    messageList.add(msg);
                }

                adapter.notifyDataSetChanged();
                chatsView.scrollToPosition(messageList.size()-1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        database.getReference().child("Users").child(receiver_id).child("online").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean status=snapshot.getValue(Boolean.class);
                if(status){
                    receiver_status.setText("Online");
                    status_symbol.setBackgroundResource(R.color.green);
                }
                else{
                    receiver_status.setText("Offline");
                    status_symbol.setBackgroundResource(R.color.gray);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void sendMessage(View view){
        if(enteredMessage.getText().equals("")){
            return;
        }

        String mess=enteredMessage.getText().toString();
        Date date=new Date();
        MessageModel message=new MessageModel(sender_id,mess,date.getTime());

        database.getReference().child("Chats").child(senderRoom).child("messages").push().setValue(message).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                database.getReference().child("Chats").child(receiverRoom).child("messages").push().setValue(message).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
            }
        });
    }

}