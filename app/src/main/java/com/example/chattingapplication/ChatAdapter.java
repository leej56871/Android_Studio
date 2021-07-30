package com.example.chattingapplication;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private List<ChatData> chatDataSet;
    private String myNickName;
    private String myEmail;
    private String url = " ";

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView chat_nickname;
        private TextView chat_msg;
        private ImageView image_msg_left;
        private ImageView image_msg_right;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            chat_nickname = (TextView) view.findViewById(R.id.chat_nickname);
            chat_msg = (TextView) view.findViewById(R.id.chat_msg);
            image_msg_left = (ImageView) view.findViewById(R.id.image_msg_left);
            image_msg_right = (ImageView) view.findViewById(R.id.image_msg_right);

        }
    }

    public ChatAdapter(List<ChatData> chatData, String myNickName, String myEmail) {
        // get the required information from the parameters
        chatDataSet = chatData;
        this.myNickName = myNickName;
        this.myEmail = myEmail;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, this represents the UIs shown in the screen
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_chat, viewGroup, false);

        return new ViewHolder(linearLayout);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        ChatData chat = chatDataSet.get(position);
        viewHolder.chat_nickname.setText(chat.getNickname());
        if (chat.getFormat().equals("text")){
            // get the chat data
            viewHolder.image_msg_left.setVisibility(View.GONE);
            viewHolder.image_msg_right.setVisibility(View.GONE);
            viewHolder.chat_msg.setText(chat.getMsg());
            int msg_length = chat.getMsg().length();
            int margin = 0;

            // according to the length of the message, give different margins to the chat box
            if (msg_length > 300){
                margin = 200;
            }
            else if (msg_length > 200 && msg_length <= 300){
                margin = 350;
            }
            else if (msg_length > 100 && msg_length <= 200){
                margin = 300;
            }
            else if (msg_length > 50 && msg_length <= 100 ){
                margin = 450;
            }
            else if (msg_length > 25 && msg_length <= 50){
                margin = 550;
            }
            else if (msg_length > 10 && msg_length <= 25){
                margin = 600;
            }
            else {
                margin = 720;
            }

            // if the chat is written by the user (recognizing by email)
            // then show the chat box at right
            // user different colors for chats written by user and others for better readability
            if (chat.getEmail().equals(this.myEmail)){
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) viewHolder.chat_msg.getLayoutParams();
                params.setMargins(margin, 0, 0, 0);
                viewHolder.chat_msg.setLayoutParams(params);
                viewHolder.chat_nickname.setTextColor(Color.parseColor("#00BCD4"));
                viewHolder.chat_nickname.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
                viewHolder.chat_msg.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
                viewHolder.chat_nickname.setGravity(Gravity.RIGHT);
                viewHolder.chat_msg.setGravity(Gravity.RIGHT);
            }

            // else written by the friend, show chat box at left
            else{
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) viewHolder.chat_msg.getLayoutParams();
                params.setMargins(0, 0, margin, 0);
                viewHolder.chat_msg.setLayoutParams(params);
                viewHolder.chat_nickname.setTextColor(Color.parseColor("#06CC0E"));
                viewHolder.chat_nickname.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                viewHolder.chat_msg.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                viewHolder.chat_nickname.setGravity(Gravity.LEFT);
                viewHolder.chat_msg.setGravity(Gravity.LEFT);
            }
        }

        else if (!chat.getFormat().equals("text")){
            viewHolder.chat_msg.setText(" ");
            viewHolder.chat_msg.setVisibility(View.GONE);

            if (!chat.getEmail().equals(this.myEmail)){
                viewHolder.image_msg_left.setVisibility(View.VISIBLE);
                viewHolder.chat_nickname.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                viewHolder.chat_nickname.setTextColor(Color.parseColor("#06CC0E"));
                viewHolder.chat_nickname.setGravity(Gravity.RIGHT);
                viewHolder.image_msg_right.setVisibility(View.GONE);
                if (chat.getFormat().equals("image"))
                {
                    Picasso.get().load(chat.getUrl()).resize(800, 1000).into(viewHolder.image_msg_left);
                }
                else{
                    viewHolder.image_msg_left.setBackgroundResource(R.drawable.icon_pdf);
                    viewHolder.image_msg_left.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(chat.getUrl()));
                            //viewHolder.itemView.getContext().startActivity(intent);
//                            File downloadPDF = null;
//                            try {
//                                downloadPDF = File.createTempFile(chat.getMsg(), ".pdf", viewHolder.chat_msg.getContext().getCacheDir());
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                                Toast.makeText(viewHolder.chat_msg.getContext(), "ERROR: No file found!", Toast.LENGTH_SHORT).show();
//                            }
//                            if (downloadPDF != null){
//                                Intent intent = new Intent(viewHolder.chat_msg.getContext(), View_PDF.class);
//                                intent.putExtra("pdf", downloadPDF.getPath());
//                                viewHolder.chat_msg.getContext().startActivity(intent);
//                            }
//                            else{
//                                Toast.makeText(viewHolder.chat_msg.getContext(), "There was an error on downloading PDF file from firebase, please retry.", Toast.LENGTH_SHORT).show();
//                            }

                            Intent intent = new Intent(viewHolder.chat_msg.getContext(), View_PDF.class);
                            intent.putExtra("pdf", chat.getUrl());
                            intent.putExtra("file_name", chat.getMsg());
                            viewHolder.chat_msg.getContext().startActivity(intent);

                        }
                    });
                }
            }
            else if (chat.getEmail().equals(this.myEmail)){
                viewHolder.image_msg_left.setVisibility(View.GONE);
                viewHolder.chat_nickname.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
                viewHolder.chat_nickname.setTextColor(Color.parseColor("#00BCD4"));
                viewHolder.chat_nickname.setGravity(Gravity.LEFT);
                viewHolder.image_msg_right.setVisibility(View.VISIBLE);
                if (chat.getFormat().equals("image"))
                {
                    Picasso.get().load(chat.getUrl()).resize(800, 1000).into(viewHolder.image_msg_right);
                }
                else{

                    viewHolder.image_msg_right.setBackgroundResource(R.drawable.icon_pdf);
                    viewHolder.image_msg_right.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(chat.getUrl()));
                            //viewHolder.itemView.getContext().startActivity(intent);

                            Intent intent = new Intent(viewHolder.chat_msg.getContext(), View_PDF.class);
                            intent.putExtra("pdf", chat.getUrl());
                            intent.putExtra("file_name", chat.getMsg());
                            viewHolder.chat_msg.getContext().startActivity(intent);

                        }
                    });
                }
            }
        }

    }

    @Override
    public int getItemCount() {
        if (chatDataSet == null){
            return 0;
        }
        else{
            return chatDataSet.size();
        }
    }

    public ChatData getChat(int position){
        if (chatDataSet == null){
            return null;
        }
        else{
            return chatDataSet.get(position);
        }
    }

    public void addChat(ChatData chatData){
        // add chat that is called by ChatActivity
        chatDataSet.add(chatData);
        notifyItemInserted(chatDataSet.size() - 1);
    }
}
