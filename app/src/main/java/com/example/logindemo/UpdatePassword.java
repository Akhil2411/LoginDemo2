package com.example.logindemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UpdatePassword extends AppCompatActivity {

    private EditText NewPassword;
    private Button BtnNewPassword;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);


        NewPassword=findViewById(R.id.newPassword);
        BtnNewPassword=findViewById(R.id.btnUpdatePassword);

        progressDialog=new ProgressDialog(this);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();


        BtnNewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String newPass=NewPassword.getText().toString();

                progressDialog.setMessage("Password Updating");
                progressDialog.show();

                firebaseUser.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            progressDialog.dismiss();
                            Toast.makeText(UpdatePassword.this,"PasswordChanged",Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            progressDialog.dismiss();
                            Toast.makeText(UpdatePassword.this,"Password cannot be changed",Toast.LENGTH_SHORT).show();
                        }
                    }
                });


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