package com.ctyx.encrochat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

//import android.widget.Toolbar;

public class Login_activity extends AppCompatActivity {



    private Toolbar mToolbar;

    private TextInputEditText mLoginEmail;
    private TextInputEditText mLoginPass;

    private Button mLoginButton;

    private ProgressDialog mLoginProgress;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);

        mToolbar= (Toolbar) findViewById(R.id.login_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mAuth=FirebaseAuth.getInstance();

        mLoginEmail= (TextInputEditText) findViewById(R.id.login_email);
        mLoginPass= (TextInputEditText) findViewById(R.id.login_pass);
        mLoginButton= (Button) findViewById(R.id.login_butt);

        mLoginProgress=new ProgressDialog(this);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=mLoginEmail.getEditableText().toString();
                String password=mLoginPass.getEditableText().toString();

                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password))
                {

                    mLoginProgress.setTitle("Logging in");
                    mLoginProgress.setMessage("We are Checking Your Credentials");
                    mLoginProgress.setCanceledOnTouchOutside(false);
                    loginuser(email,password);
                }

            }
        });







    }


        private void loginuser(String email,String password){

                 mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                     @Override
                     public void onComplete(@NonNull Task<AuthResult> task) {
                         if (task.isSuccessful())
                         {
                             mLoginProgress.dismiss();
                             Intent main_intent= new Intent(Login_activity.this,MainActivity.class);
                             main_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                             startActivity(main_intent);
                             finish();
                         }
                         else{
                             mLoginProgress.hide();
                             Toast.makeText(Login_activity.this, "Can't Sign Please verify your Details", Toast.LENGTH_SHORT).show();
                         }
                     }
                 });


        }

}
