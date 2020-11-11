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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

//import android.widget.Toolbar;

public class RegisterActivity extends AppCompatActivity {



    private TextInputEditText mdisplayName;
    private TextInputEditText mEmail;
    private TextInputEditText mPassword;
    private Button mCreateButton;

    private FirebaseAuth mAuth;
    private DatabaseReference mDataBase;

    private Toolbar mToolbar;



    //progress bar


    private ProgressDialog mRegProcess;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


//
        mdisplayName= (TextInputEditText) findViewById(R.id.reg_name);
        mEmail= (TextInputEditText) findViewById(R.id.reg_mail);
        mPassword= (TextInputEditText) findViewById(R.id.reg_pass);
        mCreateButton=  (Button) findViewById(R.id.reg_button);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();


        mRegProcess= new ProgressDialog(this);



        mToolbar=(Toolbar) findViewById(R.id.register_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String display_name=mdisplayName.getText().toString();
                String email=mEmail.getText().toString();
                String password=mPassword.getText().toString();

                if (!TextUtils.isEmpty(display_name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password))

                {

                    mRegProcess.setTitle("Registering User");
                    mRegProcess.setMessage("Please Wait while we Create Your Account!");
                    mRegProcess.show();
                    mRegProcess.setCanceledOnTouchOutside(false);
                    register_user(display_name,email,password);
                }









            }
        });

    }



    private void register_user(final String display_name, String email, String password){

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {

                    FirebaseUser current_user=FirebaseAuth.getInstance().getCurrentUser();
                    String uid = current_user.getUid();



                    mDataBase= FirebaseDatabase.getInstance().getReference().child("users").child(uid);
                    HashMap<String,String> userMap= new HashMap<>();
                    userMap.put("name",display_name);
                    userMap.put("status","Hi there! I am using Encrochat!");
                    userMap.put("image","default");
                    userMap.put("thumb_image","default");

                    mDataBase.setValue(userMap);


                    mRegProcess.dismiss();
                    Intent mainIntent=new Intent(RegisterActivity.this,MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mainIntent);
                    finish();
                }
                else {
                    mRegProcess.hide();
                    Toast.makeText(RegisterActivity.this,"Some error Occured!",Toast.LENGTH_LONG).show();
                }

            }
        });

    }



}
