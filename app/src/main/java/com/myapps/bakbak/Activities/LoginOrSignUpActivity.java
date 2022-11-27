package com.myapps.bakbak.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.myapps.bakbak.Models.MyUsers;
import com.myapps.bakbak.R;

public class LoginOrSignUpActivity extends AppCompatActivity implements View.OnClickListener {

    Button createNewAccountButton,loginButton;
    EditText enterRegisterUsername,enterRegisterEmail,enterRegisterPassword;

    TextView t1,t2;

    // register or sign in
    String curr_status="SIGN";

    FirebaseAuth auth;
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginorsignup);
//        getSupportActionBar().hide();

        createNewAccountButton=findViewById(R.id.button100);
        createNewAccountButton.setOnClickListener(this);

        loginButton=findViewById(R.id.button99);
        loginButton.setOnClickListener(this);

        enterRegisterUsername=findViewById(R.id.editTextTextPersonName100);
        enterRegisterEmail=findViewById(R.id.editTextTextPersonName99);
        enterRegisterPassword=findViewById(R.id.editTextTextPersonName98);

        t1=findViewById(R.id.spc1);
        t2=findViewById(R.id.spc2);

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
//        database.setPersistenceEnabled(true);

        if(auth.getCurrentUser()!=null){
            Intent intent=new Intent(LoginOrSignUpActivity.this,FriendsListActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View view) {
        if(view==loginButton){
            String email=enterRegisterEmail.getText().toString();
            String password=enterRegisterPassword.getText().toString();
            if(curr_status.equals("CREATE")){
                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            String username=enterRegisterUsername.getText().toString();
                            database.getReference().child("Users").child(auth.getCurrentUser().getUid()).setValue(new MyUsers(username,email,password));
                            Toast.makeText(LoginOrSignUpActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(LoginOrSignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            else{
                auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent=new Intent(LoginOrSignUpActivity.this,FriendsListActivity.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(LoginOrSignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
        else if(view==createNewAccountButton){
            if(curr_status.equals("SIGN")){
                enterRegisterUsername.setVisibility(View.VISIBLE);
                enterRegisterEmail.setHint("Email");
                loginButton.setText("REGISTER");
                createNewAccountButton.setText("SIGN IN");

                t1.setText("Register");
                t2.setText("If you are new here");

                curr_status="CREATE";

            }
            else{
                enterRegisterUsername.setVisibility(View.GONE);
                enterRegisterEmail.setHint("Username or Email");
                loginButton.setText("LOG IN");
                createNewAccountButton.setText("CREATE NEW ACCOUNT");

                t1.setText("Sign in");
                t2.setText("If you already have an account");

                curr_status="SIGN";
            }
        }
    }
}