package com.example.logindemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateProfile extends AppCompatActivity {

    private EditText UserName,UserAge,UserEmail;
    private Button SaveBtn;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        UserName=findViewById(R.id.etEditName);
        UserAge=findViewById(R.id.etEditAge);
        UserEmail=findViewById(R.id.etEditEmail);
        SaveBtn=findViewById(R.id.saveButton);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase= FirebaseDatabase.getInstance();


        final DatabaseReference databaseReference=firebaseDatabase.getReference(firebaseAuth.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserProfile userProfile=snapshot.getValue(UserProfile.class);

                UserName.setText(userProfile.getName());
                UserAge.setText(userProfile.getAge());
                UserEmail.setText(userProfile.getEmail());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateProfile.this,error.getCode(),Toast.LENGTH_SHORT).show();
            }
        });

        SaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String name=UserName.getText().toString();
                String age=UserAge.getText().toString();
                String email=UserEmail.getText().toString();


                UserProfile userProfile=new UserProfile(name,age,email);

                databaseReference.setValue(userProfile);

                Toast.makeText(UpdateProfile.this, "Updated Successfully", Toast.LENGTH_SHORT).show();

                finish();



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















