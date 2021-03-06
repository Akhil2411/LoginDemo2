package com.example.logindemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {


    private ImageView ProfilePic;
    private TextView ProfileName,ProfileAge,ProfileEmail;
    private Button EditBtn;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private Button ChangePassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ProfilePic=findViewById(R.id.ProfileImage);
        ProfileName=findViewById(R.id.ProfileName);     //type casting not required
        ProfileAge=findViewById(R.id.ProfileAge);
        ProfileEmail=findViewById(R.id.ProfileEmail);
        EditBtn=findViewById(R.id.ProfileEditBtn);
        ChangePassword=findViewById(R.id.ChangePassword);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();


        DatabaseReference databaseReference=firebaseDatabase.getReference(firebaseAuth.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserProfile userProfile=snapshot.getValue(UserProfile.class);

                ProfileName.setText("Name:"+userProfile.getName());
                ProfileAge.setText("Age:"+userProfile.getAge());
                ProfileEmail.setText("Email:"+userProfile.getEmail());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this,error.getCode(),Toast.LENGTH_SHORT).show();
            }
        });

        EditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this,UpdateProfile.class));
            }
        });

        ChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this,UpdatePassword.class));
            }
        });


    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case android.R.id.home:
                onBackPressed();


        }

        return super.onOptionsItemSelected(item);
    }
}
















