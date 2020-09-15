package com.example.logindemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

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
    private FirebaseStorage firebaseStorage;
    private static int PICK_IMAGE=123;
    Uri imagePath;
    private StorageReference storageReference;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {   //check if excecuted successfully
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data.getData()!=null){
            imagePath=data.getData();
            try {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),imagePath);
                ProfilePic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        setUpVariables(); //calling a function to instantiate variables


        firebaseAuth=FirebaseAuth.getInstance(); //getting a firebase instance
        progressDialog=new ProgressDialog(this); //instance of progressdialog

        firebaseStorage=FirebaseStorage.getInstance();


        storageReference=firebaseStorage.getReference();    //specifies wher the data is stored...(root)


        ProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Image"),PICK_IMAGE);
            }
        });




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



    private void setUpVariables(){ //function to setup value of variables


        UserName=(EditText)findViewById(R.id.usName);
        Email=(EditText)findViewById(R.id.email);
        Pass=(EditText)findViewById(R.id.psw);
        SignUp=(Button) findViewById(R.id.btn2);
        login=(TextView) findViewById(R.id.txt2);
        Age= (EditText) findViewById(R.id.etAge);
        ProfilePic=(ImageView)findViewById(R.id.img);



    }
    private boolean validate(){       //function to check if all the fields are filled
        Boolean result=false;

        name=UserName.getText().toString();
        age=Age.getText().toString();
        password=Pass.getText().toString();
        email=Email.getText().toString();

        if(name.isEmpty() || password.isEmpty() || email.isEmpty() || age.isEmpty() || imagePath==null){
            Toast.makeText(this,"Please enter all the details",Toast.LENGTH_SHORT).show();
        }
        else{
            result=true;
        }
        return result;
    }




    private void sendEmailVerification(){   //function to send verification email

        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();   //get the user trying to register

        if(firebaseUser!=null){


            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {  //send a verification email
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){

                        sendUserData(); //function to send the data to firebase database


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


        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();   //get an instance of firebase database


        DatabaseReference myRef= firebaseDatabase.getReference(firebaseAuth.getUid());    //get a reference by refering the users id which is unique

        StorageReference imageReference = storageReference.child(firebaseAuth.getUid()).child("Images").child("Profile Pic"); //user Id/images/Profilepic.jpg
        UploadTask uploadTask=imageReference.putFile(imagePath);


        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegistrationActivity.this,"Upload Failed",Toast.LENGTH_LONG).show();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(RegistrationActivity.this,"Upload Successfull",Toast.LENGTH_LONG).show();

            }
        });


        UserProfile userProfile=new UserProfile(name,age,email);    //creating an object of userprofile class and passing the parameters

        myRef.setValue(userProfile);   //the values are set by refering to the userid

    }

}



















