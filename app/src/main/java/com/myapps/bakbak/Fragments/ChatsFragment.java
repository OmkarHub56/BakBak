package com.myapps.bakbak.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.myapps.bakbak.Adapters.MyUserlistAdapter;
import com.myapps.bakbak.Models.MyUsers;
import com.myapps.bakbak.R;

import java.util.ArrayList;
import java.util.List;

public class ChatsFragment extends Fragment {
    RecyclerView list_of_friends;
    ConstraintLayout mainL;
    MyUserlistAdapter md;
    FirebaseDatabase database;
    FirebaseAuth auth;
    List<MyUsers> list=new ArrayList<>();
    public ChatsFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainL= (ConstraintLayout) inflater.inflate(R.layout.fragment_chats, container, false);
        list_of_friends=mainL.findViewById(R.id.recycler_view);

        list_of_friends.setLayoutManager(new LinearLayoutManager(getActivity()));
        md=new MyUserlistAdapter(list,getActivity());
        list_of_friends.setAdapter(md);


        database=FirebaseDatabase.getInstance();

        auth=FirebaseAuth.getInstance();

        database.getReference().child("Users").child(auth.getUid()).child("friends").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dtp : snapshot.getChildren()){
//                    Log.i("nm",dtp.getValue(String.class));
                    database.getReference().child("Users").child(dtp.getValue(String.class)).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            database.getReference().child("Users").child(dtp.getValue(String.class)).removeEventListener(this);
                            MyUsers user=snapshot.getValue(MyUsers.class);
                            user.setUserId(dtp.getValue(String.class));
                            Log.i("vb",user.getUserId());
                            list.add(user);

                            Log.i("nm",user.getUsername());
                            md.notifyDataSetChanged();
                            Log.i("nm",String.valueOf(list.size()));

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

//                    Log.i("um",user.username);
                }
                md.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return mainL;
    }
}