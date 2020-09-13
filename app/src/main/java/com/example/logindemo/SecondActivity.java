package com.example.logindemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class SecondActivity extends AppCompatActivity {

    private Button logout; //button declaration
    private FirebaseAuth firebaseAuth;  //firebase declaration

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        firebaseAuth=FirebaseAuth.getInstance();   //creating firebase instance
        logout=(Button)findViewById(R.id.logout1);  //creating button instance

        logout.setOnClickListener(new View.OnClickListener() {   //when button is clicked
            @Override
            public void onClick(View view) {
                Logout();   //invoke logout function

            }
        });
    }
        private void Logout(){
            firebaseAuth.signOut();    //signout from firebase database
            finish();                  //finish the task
            startActivity(new Intent(SecondActivity.this,MainActivity.class));  //go to mainactivity
        }

    //press cntrl+o


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {   //implement the menu in secondactivity

        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }




    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {   //handle click events on the menu
        switch (item.getItemId()){
            case R.id.LogoutMenu: {
                Logout();
                break;
            }
            case R.id.Profile:{
                startActivity(new Intent(SecondActivity.this,ProfileActivity.class));
            }


        }
        return super.onOptionsItemSelected(item);
    }
}