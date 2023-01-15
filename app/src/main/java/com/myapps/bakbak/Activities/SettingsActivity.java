package com.myapps.bakbak.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.myapps.bakbak.ConstantsClass;
import com.myapps.bakbak.R;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth auth;
    String sender_id;
    FirebaseDatabase database;

    ImageView backBtn;

    Switch clearMessageSwitch,timeFormatSwitch,blockInvitesSwitch;

    Button changePasswordBtn;

    Button deleteAccountBtn;


    SharedPreferences myAppSettings;
    SharedPreferences.Editor myEdit;
    // keys :
    String MESSAGE_CLEAR_KEY= ConstantsClass.SHARED_PREF_CLEAR_MESSAGE_KEY,TIME_FORMAT_KEY=ConstantsClass.SHARED_PREF_TIME_FORMAT_KEY;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        auth=FirebaseAuth.getInstance();
        sender_id=auth.getUid();
        database=FirebaseDatabase.getInstance();

        backBtn=findViewById(R.id.back_btn);
        backBtn.setOnClickListener(view -> onBackPressed());

        clearMessageSwitch=findViewById(R.id.clearMes);
        timeFormatSwitch=findViewById(R.id.switch_12_24);
        blockInvitesSwitch=findViewById(R.id.switch_block_invites);


        myAppSettings=getSharedPreferences(ConstantsClass.SHARED_PREFERENCES_NAME,MODE_PRIVATE);
        myEdit=myAppSettings.edit();

        // message clear shared pref
        if(myAppSettings.contains(MESSAGE_CLEAR_KEY)){
            clearMessageSwitch.setChecked(myAppSettings.getBoolean(MESSAGE_CLEAR_KEY,false));
        }
        else{
            myEdit.putBoolean(MESSAGE_CLEAR_KEY,false);
            myEdit.commit();
        }
        clearMessageSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            myEdit.putBoolean(MESSAGE_CLEAR_KEY,b);
            myEdit.commit();
        });

        // time format shared pref
        if(myAppSettings.contains(TIME_FORMAT_KEY)){
            timeFormatSwitch.setChecked(myAppSettings.getBoolean(TIME_FORMAT_KEY,false));
        }
        else{
            myEdit.putBoolean(TIME_FORMAT_KEY,false);
            myEdit.commit();
        }
        timeFormatSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            myEdit.putBoolean(TIME_FORMAT_KEY,b);
            myEdit.commit();
        });

        // for blocking invites setting
        database.getReference().child(ConstantsClass.USERS).child(auth.getUid()).child(ConstantsClass.BLOCK_INVITES).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && Boolean.TRUE.equals(snapshot.getValue(Boolean.class))){
                    blockInvitesSwitch.setChecked(true);
                }
                else{
                    blockInvitesSwitch.setChecked(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        blockInvitesSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            database.getReference().child(ConstantsClass.USERS).child(auth.getUid()).child(ConstantsClass.BLOCK_INVITES).setValue(b);
        });

        changePasswordBtn=findViewById(R.id.change_pass_setting);
        changePasswordBtn.setOnClickListener(view -> {
            Intent intent=new Intent(SettingsActivity.this,ChangePasswordActivity.class);
            startActivity(intent);
        });

        deleteAccountBtn=findViewById(R.id.delete_account_btn);
        deleteAccountBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view==deleteAccountBtn){


            // todo the dialog operations

//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            AlertDialog dialog=new AlertDialog(this);
//            dialog.setMessage("Are you sure you want to delete account ?");
//            builder.setView();
//            builder.setMessage("Are you sure you want to delete account ?").setPositiveButton("Yes", dialogClickListener)
//                    .setNegativeButton("No", dialogClickListener).show();
//
//            AlertDialog.Builder alert = new AlertDialog.Builder(this);
//            EditText input = new EditText(this);
//            input.setId(65);
//            alert.setView(input);
//            String out;
//            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                //@Override
//                public void onClick(DialogInterface dialog, int which) {
//                    EditText input = (EditText) alert.findViewById(65);
//
//                    Editable value = input.getText();
//                    out = value.toString();
//
//                }
//            });



        }
    }

    public void deleteAccount(){



        // deleting all friends
        DatabaseReference friendsList=database.getReference().child(ConstantsClass.USERS).child(sender_id).child(ConstantsClass.FRIENDS);
        ValueEventListener friendsListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    database.getReference().child(ConstantsClass.USERS).child(dataSnapshot.getKey()).child(ConstantsClass.FRIENDS).child(sender_id).removeValue();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        friendsList.addValueEventListener(friendsListener);
        new Handler().postDelayed(() -> friendsList.removeEventListener(friendsListener),1500);

        // deleting all groups
        DatabaseReference groupsList=database.getReference().child(ConstantsClass.USERS).child(sender_id).child(ConstantsClass.GROUPS_ADDED_TO);
        ValueEventListener groupsListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    database.getReference().child(ConstantsClass.GROUP_CHATS).child(dataSnapshot.getKey()).child(ConstantsClass.GROUP_MEMBERS_INFO).child(sender_id).removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        groupsList.addValueEventListener(groupsListener);
        new Handler().postDelayed(() -> groupsList.removeEventListener(groupsListener),1500);

        auth.getCurrentUser().delete();
        database.getReference().child(ConstantsClass.USERS).child(sender_id).removeValue().addOnCompleteListener(task -> {
            finish();
            Intent intent=new Intent(SettingsActivity.this,LoginOrSignUpActivity.class);
            startActivity(intent);
        });
    }
}