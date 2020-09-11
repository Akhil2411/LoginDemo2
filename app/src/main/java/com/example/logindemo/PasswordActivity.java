package com.example.logindemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PasswordActivity extends AppCompatActivity {
    private EditText email;
    private Button resetbtn;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        email=(EditText)findViewById(R.id.frgtEmail);
        resetbtn=(Button)findViewById(R.id.passwordReset);
        firebaseAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);

        resetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail=email.getText().toString().trim();

                progressDialog.setMessage("Processing");
                progressDialog.show();

                if (userEmail.equals("")){
                    progressDialog.dismiss();
                    Toast.makeText(PasswordActivity.this,"Please Enter Your registered email id",Toast.LENGTH_SHORT).show();
                }else{
                    firebaseAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                progressDialog.dismiss();
                                Toast.makeText(PasswordActivity.this,"Password reset email send",Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(PasswordActivity.this,MainActivity.class));
                            }else{
                                progressDialog.dismiss();
                                Toast.makeText(PasswordActivity.this,"Account does not Exist",Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }


            }
        });
    }
}