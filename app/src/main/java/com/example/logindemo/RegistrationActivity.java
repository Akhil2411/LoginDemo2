package com.example.logindemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {
    private EditText UserName;
    private EditText Email;
    private EditText Pass;
    private Button SignUp;
    private TextView login;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private EditText Age;
    private ImageView ProfilePic;
    String name,email,age,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        setUpVariables(); //calling a function to instantiate variables


        firebaseAuth=FirebaseAuth.getInstance(); //getting a firebase instance
        progressDialog=new ProgressDialog(this); //instance of progressdialog


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

                   progressDialog.setMessage("Signing Up");
                   progressDialog.show();

                   firebaseAuth.createUserWithEmailAndPassword(user_email,user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                           if (task.isSuccessful()) {

                               progressDialog.dismiss();

//                               Log.d("tag","done");
////
////                               Toast.makeText(RegistrationActivity.this, "Registration Successfull", Toast.LENGTH_SHORT).show();
////                               startActivity(new Intent(RegistrationActivity.this,MainActivity.class));


                               sendEmailVerification();


                           }
                           else {
                               progressDialog.dismiss();

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
        Age= (EditText) findViewById(R.id.etAge);
        ProfilePic=(ImageView)findViewById(R.id.img);



    }
    private boolean validate(){
        Boolean result=false;

        name=UserName.getText().toString();
        age=Age.getText().toString();
        password=Pass.getText().toString();
        email=Email.getText().toString();

        if(name.isEmpty() || password.isEmpty() || email.isEmpty() || age.isEmpty()){
            Toast.makeText(this,"Please enter all the details",Toast.LENGTH_SHORT).show();
        }
        else{
            result=true;
        }
        return result;
    }

    private void sendEmailVerification(){
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();   //get the user trying to register

        if(firebaseUser!=null){


            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {  //send a verification email
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        sendUserData();
                        Toast.makeText(RegistrationActivity.this,"Succesfully Registered,Verification Mail send",Toast.LENGTH_LONG).show();
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(RegistrationActivity.this,MainActivity.class));
                    }else{
                        Toast.makeText(RegistrationActivity.this,"Unable to send Verification Mail",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    private void sendUserData(){
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference myRef= firebaseDatabase.getReference(firebaseAuth.getUid());

        UserProfile userProfile=new UserProfile(name,age,email);

        myRef.setValue(userProfile);

    }

}



















