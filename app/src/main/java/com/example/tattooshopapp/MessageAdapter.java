package com.example.tattooshopapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private ArrayList<Message> messages;

    public MessageAdapter(ArrayList<Message> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.item_message,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder viewHolder, int position) {
        viewHolder.userNameView.setText(messages.get(position).getName());
        Message message = messages.get(position);
        boolean isPhoto = message.getPhotoUrl() != null;
        if (isPhoto) {
            viewHolder.textView.setVisibility(View.GONE);
            viewHolder.imageView.setVisibility(View.VISIBLE);
            Glide.with(viewHolder.imageView.getContext())
                    .load(message.getPhotoUrl())
                    .into(viewHolder.imageView);
        } else {
            viewHolder.textView.setVisibility(View.VISIBLE);
            viewHolder.imageView.setVisibility(View.GONE);
            viewHolder.textView.setText(message.getText());
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;
        TextView userNameView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.messageTextView);
            userNameView = itemView.findViewById(R.id.nameTextView);
            imageView = itemView.findViewById(R.id.photoImageView);
        }
    }
}
