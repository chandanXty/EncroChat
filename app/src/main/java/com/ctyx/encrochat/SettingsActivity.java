package com.ctyx.encrochat;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private DatabaseReference mUserDataBase;
    private FirebaseUser mCurrentUser;


    private CircleImageView mDpImage;
    private TextView mName;
    private TextView mStatus;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mCurrentUser= FirebaseAuth.getInstance().getCurrentUser();
        String Current_uid=mCurrentUser.getUid();


        mDpImage= (CircleImageView) findViewById(R.id.dp_picture_settings);
        mName= (TextView)  findViewById(R.id.name_settings);
        mStatus= (TextView) findViewById(R.id.status_in_settings);

        mUserDataBase= FirebaseDatabase.getInstance().getReference().child("users").child(Current_uid);


        mUserDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String name= snapshot.child("name").getValue().toString();
                String image= snapshot.child("image").getValue().toString();
                String status= snapshot.child("status").getValue().toString();
                String thumbimage= snapshot.child("thumb_image").getValue().toString();

                mName.setText(name);
                mStatus.setText(status);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


    }
}
