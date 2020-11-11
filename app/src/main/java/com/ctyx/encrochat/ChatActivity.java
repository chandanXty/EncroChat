 package com.ctyx.encrochat;

 import android.os.Bundle;
 import android.text.TextUtils;
 import android.util.Log;
 import android.view.View;
 import android.widget.ImageButton;

 import androidx.annotation.NonNull;
 import androidx.annotation.Nullable;
 import androidx.appcompat.app.AppCompatActivity;
 import androidx.appcompat.widget.Toolbar;
 import androidx.recyclerview.widget.LinearLayoutManager;
 import androidx.recyclerview.widget.RecyclerView;

 import com.google.android.material.textfield.TextInputEditText;
 import com.google.firebase.auth.FirebaseAuth;
 import com.google.firebase.database.ChildEventListener;
 import com.google.firebase.database.DataSnapshot;
 import com.google.firebase.database.DatabaseError;
 import com.google.firebase.database.DatabaseReference;
 import com.google.firebase.database.FirebaseDatabase;
 import com.google.firebase.database.ServerValue;
 import com.google.firebase.database.ValueEventListener;

 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.List;
 import java.util.Map;

 public class ChatActivity extends AppCompatActivity {

    private String mChatuser;


    private Toolbar mchatToolbar;
    private DatabaseReference mRootref;
    private FirebaseAuth mAuth;
    private String mCurrentUserId;

    private ImageButton mSendButton;
    private TextInputEditText mChatMessage;
    private RecyclerView mMessagesList;


    private List<messages> messagesList=new ArrayList<>();
    private LinearLayoutManager mLinearLayout;
    private MessageAdapter mAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mchatToolbar= (Toolbar) findViewById(R.id.chat_toolbar);
        setSupportActionBar(mchatToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRootref= FirebaseDatabase.getInstance().getReference();     //  DATABASE REFERENCE
        mAuth= FirebaseAuth.getInstance();
        mCurrentUserId=mAuth.getCurrentUser().getUid();





        mSendButton= (ImageButton) findViewById(R.id.chat_send_button);
        mChatMessage= (TextInputEditText) findViewById(R.id.chat_message_box);

        mAdaptor= new MessageAdapter(messagesList);

        mMessagesList= (RecyclerView) findViewById(R.id.messages_list);
        mLinearLayout=new LinearLayoutManager(this);
        mMessagesList.setHasFixedSize(true);
        mMessagesList.setLayoutManager(mLinearLayout);

        mMessagesList.setAdapter(mAdaptor);

        //Loading Messages



        mChatuser= getIntent().getStringExtra("user_id");     // Getting Info From All users Activity


        loadMessages();

        mRootref.child("users").child(mChatuser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String chat_user_name=snapshot.child("name").getValue().toString();
                getSupportActionBar().setTitle(chat_user_name);                  //setting the title to user name

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

         mRootref.child("chat").child(mCurrentUserId).addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {

                 if (!snapshot.hasChild(mChatuser)){
                     Map chatAddmap= new HashMap();
                     chatAddmap.put("seen",false);
                     chatAddmap.put("timestamp", ServerValue.TIMESTAMP);

                     Map chatUserMap= new HashMap();
                     chatUserMap.put("Chat/"+ mCurrentUserId+ "/"+mChatuser,chatAddmap);
                     chatUserMap.put("Chat/"+mChatuser+"/"+mCurrentUserId,chatAddmap);

                     mRootref.updateChildren(chatUserMap, new DatabaseReference.CompletionListener() {
                         @Override
                         public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                            if (error != null){
                                Log.d("CHAT_LOG",error.getMessage().toString());
                            }
                         }
                     });


                 }

             }




             @Override
             public void onCancelled(@NonNull DatabaseError error) {

             }
         });
         mSendButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 sendMessage();
             }
         });


    }

     /*****************    Load Messages         ***************************************/


     private void loadMessages(){

       mRootref.child("messages").child(mCurrentUserId).child(mChatuser).addChildEventListener(new ChildEventListener() {
           @Override
           public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

               messages message =snapshot.getValue(messages.class);
               messagesList.add(message);
               mAdaptor.notifyDataSetChanged();
           }

           @Override
           public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

           }

           @Override
           public void onChildRemoved(@NonNull DataSnapshot snapshot) {

           }

           @Override
           public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });

     }


     /*****************      Sending Of Messages       **********************************/


     private void sendMessage() {

         String message=mChatMessage.getText().toString();

         if(!TextUtils.isEmpty(message)) {

             String curr_user_ref = "messages/" + mCurrentUserId + "/" + mChatuser;
             String chat_user_ref = "messages/" + mChatuser + "/" + mCurrentUserId;

             DatabaseReference user_message_push = mRootref.child("messages").child(mCurrentUserId).child(mChatuser).push();
             String push_id = user_message_push.getKey();


             Map messageMap = new HashMap();
             messageMap.put("message", message);
             messageMap.put("seen", false);
             messageMap.put("type", "text");
             messageMap.put("time", ServerValue.TIMESTAMP);
             messageMap.put("from",mCurrentUserId);

             Map MessageUserMap = new HashMap();
             MessageUserMap.put(curr_user_ref + "/" + push_id, messageMap);
             MessageUserMap.put(chat_user_ref + '/' + push_id, messageMap);


             mChatMessage.setText("");
             //// Putting data on Server


             mRootref.updateChildren(MessageUserMap, new DatabaseReference.CompletionListener() {
                 @Override
                 public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                       if (error!=null)
                       {
                            Log.d("CHAT_LOG",error.getMessage().toString());
                       }
                 }
             });

         }



     }


}
