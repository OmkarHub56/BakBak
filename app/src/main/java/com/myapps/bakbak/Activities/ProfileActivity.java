package com.myapps.bakbak.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.myapps.bakbak.Models.MyUsers;
import com.myapps.bakbak.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    // 1 if the user opened his profile
    int type=1;
    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseStorage storage;
    MyUsers currentUser;

    CircleImageView profile_pic;

    int SELECT_IMAGE_CODE=1;

    TextView user_username,user_status;
//    CircleImageView user_profilePic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        user_username=findViewById(R.id.textView3);
        user_status=findViewById(R.id.textView4);
//        user_profilePic=findViewById(R.id.profil_pic);
        profile_pic=findViewById(R.id.profil_pic);

//        getSupportActionBar().hide();

        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        storage=FirebaseStorage.getInstance();
//        currentUser=database.getReference().child("Users").child(auth.getCurrentUser().getUid());
        database.getReference().child("Users").child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MyUsers mu=snapshot.getValue(MyUsers.class);
                user_username.setText(mu.getUsername());
//                snapshot.getKey()
//                user_status.setText(mu);
//                Glide.with(ProfileActivity.this).load(R.drawable.user_icon).into(profile_pic);
//                profile_pic.setImageResource(R.drawable.user_icon);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        Glide.with(this).load(storage.getReference().child("Users/").child(auth.getCurrentUser().getUid()+"/").child("profilePic.jpeg/")).into(profile_pic);
    }

//    public void updateProfilePic(View view){
//        Intent intent=new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent,"title"),SELECT_IMAGE_CODE);
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode==1){
//            Uri uri=data.getData();
//            profile_pic.setImageURI(uri);
//
//            storage.getReference().child("Users").child(auth.getCurrentUser().getUid()).child("profilePic").putFile(uri);
//        }
//    }
}