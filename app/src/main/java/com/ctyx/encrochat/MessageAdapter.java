package com.ctyx.encrochat;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {


    private List<messages> mMessageList;
    private FirebaseAuth mAuth;


    public MessageAdapter(List<messages> mMessageList){
        this.mMessageList=mMessageList;
    }


    @Override
    public MessageViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.message_single_layout,parent,false);

        return new MessageViewHolder(v);
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        public TextView messageText;


        public MessageViewHolder( View itemView) {
            super(itemView);

            messageText= (TextView) itemView.findViewById(R.id.message_text_layout);


        }


    }


    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MessageViewHolder holder, int position) {

        mAuth=FirebaseAuth.getInstance();
        String curr_user_id=mAuth.getCurrentUser().getUid();

        messages c= mMessageList.get(position);
        String from_user=c.getFrom();
        holder.messageText.setText(c.getMessage());

        if(from_user.equals(curr_user_id)){

            holder.messageText.setBackgroundResource(R.drawable.send_m_bg);
            holder.messageText.setTextColor(Color.BLACK);


        }
        else {

            holder.messageText.setBackgroundResource(R.drawable.message_text_background);
            holder.messageText.setTextColor(Color.WHITE);
        }

    }

    @Override
    public int getItemCount() {
       return mMessageList.size();
    }
}
