package com.ctyx.encrochat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UsersActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView mUsersList;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);


        mToolbar= (Toolbar) findViewById(R.id.users_appbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("All Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDatabase= FirebaseDatabase.getInstance().getReference().child("users");
        mUsersList= (RecyclerView) findViewById(R.id.users_list);

        mUsersList.setHasFixedSize(true);
        mUsersList.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<users,UsersViewholder> firebaserecycleradapter=new FirebaseRecyclerAdapter<users, UsersViewholder>(

                users.class,
                R.layout.users_single_layout,
                UsersViewholder.class,
                mDatabase
        ) {


            @Override
            protected void populateViewHolder(UsersViewholder usersViewholder, users users, int i) {
                  usersViewholder.setName(users.getName());
                  usersViewholder.setStatus(users.getStatus());
                  final String mUserid= getRef(i).getKey();
                  usersViewholder.mView.setOnClickListener(new View.OnClickListener(){

                      @Override
                      public void onClick(View v) {


                          Intent chatintent= new Intent(UsersActivity.this,ChatActivity.class);
                          chatintent.putExtra("user_id",mUserid);
                          startActivity(chatintent);
                      }
                  });


            }
        };

        mUsersList.setAdapter(firebaserecycleradapter);
    }
    public static class UsersViewholder extends RecyclerView.ViewHolder{

        View mView;

        public UsersViewholder(@NonNull View itemView) {
            super(itemView);

            mView=itemView;

        }
        public void setName(String name){
            TextView userNamView= mView.findViewById(R.id.users_single_name);
            userNamView.setText(name);
        }
        public void setStatus(String status){
            TextView userstatusview= mView.findViewById(R.id.user_single_status);
            userstatusview.setText(status);
        }




    }


}
