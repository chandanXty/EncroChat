package com.ctyx.encrochat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class StartAct extends AppCompatActivity {



    private Button myregButton;
    private Button mLoginButton;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


        myregButton= (Button)  findViewById (R.id.regbutt);


        myregButton.setOnClickListener(new View.OnClickListener() {
          @Override

            public void onClick(View view) {
              Intent reg_intent= new Intent(StartAct.this,RegisterActivity.class);
              startActivity(reg_intent);
          }

    });

        mLoginButton= (Button)  findViewById(R.id.login_button);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Login_intent= new Intent (StartAct.this,Login_activity.class);
                startActivity(Login_intent);
            }



        } );
    }
}
