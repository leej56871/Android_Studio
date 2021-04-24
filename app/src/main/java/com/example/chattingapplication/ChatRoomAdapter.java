package com.example.chattingapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ViewHolder> {
    private List<String> roomList;
    private String myNickName;
    private String myEmail;
    private Context context;
    private RecyclerViewClickInterface recyclerViewClickInterface;

    class ViewHolder extends RecyclerView.ViewHolder{
        private Button btn_getin;

        public ViewHolder(View view) {
            super(view);
            btn_getin = view.findViewById(R.id.btn_getin);
            btn_getin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewClickInterface.onItemClick(getAdapterPosition());

                }
            });
        }

    }

    public ChatRoomAdapter(List<String> roomList, String myEmail, String myNickName, Context context, RecyclerViewClickInterface recyclerViewClickInterface) {
        this.recyclerViewClickInterface = recyclerViewClickInterface;
        this.context = context;
        this.myEmail = myEmail;
        this.myNickName = myNickName;
        this.roomList = roomList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_chatroom, viewGroup, false);

        return new ViewHolder(linearLayout);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        String room = this.roomList.get(position);
        viewHolder.btn_getin.setText(room);
    }

    @Override
    public int getItemCount() {
        if (roomList == null){
            return 0;
        }
        else{
            return roomList.size();
        }
    }

    public String getChat(int position){
        if (roomList == null){
            return null;
        }
        else{
            return roomList.get(position);
        }
    }

    public void addRoom(String room){
        this.roomList.add(room);
        notifyItemInserted(roomList.size() - 1);
    }
}
