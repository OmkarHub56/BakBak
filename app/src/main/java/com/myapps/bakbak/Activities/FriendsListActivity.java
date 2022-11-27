package com.myapps.bakbak.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.myapps.bakbak.Adapters.FragmentAdapter;
import com.myapps.bakbak.Adapters.MyUserlistAdapter;
import com.myapps.bakbak.Models.MyUsers;
import com.myapps.bakbak.R;

import java.util.ArrayList;
import java.util.List;

public class FriendsListActivity extends AppCompatActivity {


    MyUserlistAdapter md;
    List<MyUsers> list;
    FirebaseDatabase database;
    FirebaseAuth auth;
    TabLayout tabLayout;
    FragmentAdapter adapter;
    ViewPager2 viewPager2;

    String CHATS_TAB="Chats",SENT_TAB="Sent",RECEIVED_TAB="Received";
    EditText searchBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

//        getSupportActionBar().hide();
        list=new ArrayList<>();
//        list_of_friends=findViewById(R.id.recycler_view);
        md=new MyUserlistAdapter(list,this);
        searchBar=findViewById(R.id.editTextTextPersonName);
//        list_of_friends.setAdapter(md);
//        list_of_friends.setLayoutManager(new LinearLayoutManager(this));

        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();

//        List<String> list=new ArrayList<>();
//        list.add("kmdkz");
//        list.add("knddd");
//        database.getReference().child("Users").child(auth.getUid()).child("friends").setValue(list);

        tabLayout=findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(CHATS_TAB));
        tabLayout.addTab(tabLayout.newTab().setText(SENT_TAB));
        tabLayout.addTab(tabLayout.newTab().setText(RECEIVED_TAB));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos=tab.getPosition();
                if(pos==0){
                    searchBar.setHint("Search for contacts");
                }
                else if(pos==1){
                    searchBar.setHint("Search for friends");
                }
                else if(pos==2){
                    searchBar.setHint("Search for contacts");
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2=findViewById(R.id.view_pager);
        adapter=new FragmentAdapter(getSupportFragmentManager(),getLifecycle(),this);
        viewPager2.setAdapter(adapter);

        new TabLayoutMediator(tabLayout,viewPager2, (tab, position) -> {
            if(position==0){
                tab.setText("Chats");
            }
            else if(position==1){
                tab.setText("Sent");
            }
            else if(position==2){
                tab.setText("Received");
            }
        }).attach();
    }

    public void openSettings(View view){
        Intent intent=new Intent(this,ProfileActivity.class);
        startActivity(intent);
    }
}