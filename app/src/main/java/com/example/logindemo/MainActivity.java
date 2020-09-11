package com.example.logindemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.session.MediaSession;
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
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {


    //variable declaration

    private EditText UserName;
    private EditText Password;
    private Button login;
    private TextView Reg;
    private FirebaseAuth firebaseAuth; //creating a variable for firebase database
    private ProgressDialog progressDialog; //creating a variable for displaying at loading time

    //variable declaration ends

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      //assigning id to the newly created variables/////////////

        UserName=(EditText) findViewById(R.id.user);
        Password=(EditText) findViewById(R.id.password);
        login=(Button) findViewById(R.id.btn);
        Reg=(TextView)findViewById(R.id.userSign);

       //id assign ends////////////////////////////////////////

        firebaseAuth=FirebaseAuth.getInstance();  //get an instance of the firebase and assign it to firebaseAuth


        progressDialog=new ProgressDialog(this); //get an instance of progress dialogue


/////////////////////////////user already logged in? then direct to second activity process starts///////////////////////

        FirebaseUser user=firebaseAuth.getCurrentUser(); //contains details of current logged user



        if(user!=null){   //if user is not empty,then user has already logged in

            finish();   //finish(destroys) the registration activity

            startActivity(new Intent(MainActivity.this,SecondActivity.class));//moves directly to second activity
        }

/////////////////////////////user already logged in? then direct to second activity process completed///////////////////////

        login.setOnClickListener(new View.OnClickListener() {    //login button clicked activity defined
            @Override
            public void onClick(View view) {

                if (ErrorFree()) {

                    Validate(UserName.getText().toString(), Password.getText().toString()); //passes login validation to the function validation
                }
            }

        });

        Reg.setOnClickListener(new View.OnClickListener() {  //activity to do when signUp is clicked
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this,RegistrationActivity.class)); //directs to registration activity
            }
        });
    }
    private void Validate(String UserName,String UserPass){  //user and password validation
        progressDialog.setMessage("Loading"); //displayed at the time of loading at time when button clicked
        progressDialog.show();  //to show the progress dialogue

        firebaseAuth.signInWithEmailAndPassword(UserName,UserPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) { //addoncompletelistener to check the task success
              if(task.isSuccessful()){     //if the task is successfull

                  progressDialog.dismiss(); //if task succesfull end the process dialogue

//                  Toast.makeText(MainActivity.this,"Login Successfull",Toast.LENGTH_SHORT).show();
//
//                  startActivity(new Intent(MainActivity.this,SecondActivity.class)); //move to next activity

                  checkEmailVerification();  //invoke the function

              }else{ //if task is not completed

                  progressDialog.dismiss();

                  Toast.makeText(MainActivity.this,"Invalid Login",Toast.LENGTH_SHORT).show();
              }

            }
        });

    }



    private boolean ErrorFree(){
        boolean result=false;

        String User=UserName.getText().toString();
        String password=Password.getText().toString();

        if(User.isEmpty() || password.isEmpty()){
            Toast.makeText(this,"Please Enter the Credentials",Toast.LENGTH_SHORT).show();

        }else{
            result=true;
        }
        return result;

    }





    private void checkEmailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();   //get instance of already created user
                                                                                   //user already created so we use getInstance

        Boolean emailFlag= firebaseUser.isEmailVerified(); //true if user has already verified his email else false

        if(emailFlag){
            finish();
            startActivity(new Intent(MainActivity.this,SecondActivity.class));
            Toast.makeText(this,"Login Succesfull",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"Please verify Your Email",Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut(); //signOut of the firebase
        }
    }
}