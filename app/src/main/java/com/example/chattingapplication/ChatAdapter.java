package com.example.chattingapplication;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private List<ChatData> chatDataSet;
    private String myNickName;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView chat_nickname;
        private TextView chat_msg;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            chat_nickname = (TextView) view.findViewById(R.id.chat_nickname);
            chat_msg = (TextView) view.findViewById(R.id.chat_msg);
        }
    }

    public ChatAdapter(List<ChatData> chatData, Context context, String myNickName) {
        chatDataSet = chatData;
        this.myNickName = myNickName;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_chat, viewGroup, false);

        return new ViewHolder(linearLayout);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        ChatData chat = chatDataSet.get(position);
        viewHolder.chat_nickname.setText(chat.getNickname());
        viewHolder.chat_msg.setText(chat.getMsg());
        int msg_length = chat.getMsg().length();
        int margin = 0;
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

        if (chat.getNickname().equals(this.myNickName)){
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) viewHolder.chat_msg.getLayoutParams();
            params.setMargins(margin, 0, 0, 0);
            viewHolder.chat_msg.setLayoutParams(params);
            viewHolder.chat_nickname.setTextColor(Color.parseColor("#00BCD4"));
            viewHolder.chat_nickname.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
            viewHolder.chat_msg.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
            viewHolder.chat_nickname.setGravity(Gravity.RIGHT);
            viewHolder.chat_msg.setGravity(Gravity.RIGHT);
        }
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
        chatDataSet.add(chatData);
        notifyItemInserted(chatDataSet.size() - 1);
    }
}
