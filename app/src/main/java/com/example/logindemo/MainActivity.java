package com.example.logindemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText UserName;
    private EditText Password;
    private Button login;
    private TextView Reg;
   // private TextView Info;
    //private int counter=5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        UserName=(EditText) findViewById(R.id.user);
        Password=(EditText) findViewById(R.id.password);
        login=(Button) findViewById(R.id.btn);
        Reg=(TextView)findViewById(R.id.userSign);
//      Info=(TextView)findViewById(R.id.txt);

       // Info.setText("Number of attempts remaining:5");

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Validate(UserName.getText().toString(),Password.getText().toString());
            }
        });
        Reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,RegistrationActivity.class));
            }
        });
    }
    private void Validate(String Username,String UserPass){
        if(Username.equals("Admin") && UserPass.equals("asdf")){
            Intent intent=new Intent(MainActivity.this,SecondActivity.class);
            startActivity(intent);
        }
       else{
//            counter--;

//            Info.setText("Number of attempts remaining:"+String.valueOf(counter));
            Toast.makeText(MainActivity.this,"Invalid Username or password",Toast.LENGTH_SHORT).show();

//            if(counter==0){
//                login.setEnabled(false);
            }

    }
}