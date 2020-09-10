package com.example.logindemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {
    private EditText UserName;
    private EditText Email;
    private EditText Pass;
    private Button SignUp;
    private TextView login;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        setUpVariables(); //calling a function to instantiate variables


        firebaseAuth=FirebaseAuth.getInstance(); //getting a firebase instance


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActivity.this,MainActivity.class));
            }
        });

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(validate()){
                   String user_email=Email.getText().toString().trim();
                   String user_password=Pass.getText().toString().trim();

                   firebaseAuth.createUserWithEmailAndPassword(user_email,user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                           if (task.isSuccessful()) {
                               Log.d("tag","done");
                               Toast.makeText(RegistrationActivity.this, "Registration Successfull", Toast.LENGTH_SHORT).show();
                               startActivity(new Intent(RegistrationActivity.this,MainActivity.class));
                           }
                           else {
                               Log.d("tag","undone");
                               Toast.makeText(RegistrationActivity.this, "Registration not Successfull", Toast.LENGTH_SHORT).show();
                           }
                       }
                   });
               }
            }
        });
    }
    private void setUpVariables(){
        UserName=(EditText)findViewById(R.id.usName);
        Email=(EditText)findViewById(R.id.email);
        Pass=(EditText)findViewById(R.id.psw);
        SignUp=(Button) findViewById(R.id.btn2);
        login=(TextView) findViewById(R.id.txt2);
    }
    private boolean validate(){
        Boolean result=false;

        String name=UserName.getText().toString();
        String password=Pass.getText().toString();
        String email=Email.getText().toString();

        if(name.isEmpty() || password.isEmpty() || email.isEmpty()){
            Toast.makeText(this,"Please enter all the details",Toast.LENGTH_SHORT).show();
        }
        else{
            result=true;
        }
        return result;
    }

}