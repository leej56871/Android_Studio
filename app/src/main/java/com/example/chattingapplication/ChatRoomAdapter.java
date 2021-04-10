//package com.example.chattingapplication;
//
//import android.content.Context;
//import android.graphics.Color;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.List;
//
//public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ViewHolder> {
//    private List<ChatData> chatRoomSet;
//    private String roomName;
//
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        private Button btn_getin;
//
//        public ViewHolder(View view) {
//            super(view);
//            btn_getin = view.findViewById(R.id.btn_getin);
//
//        }
//    }
//
//    public ChatRoomAdapter(List<ChatData> chatData, Context context, String myNickName) {
//        chatDataSet = chatData;
//        this.myNickName = myNickName;
//    }
//
//    // Create new views (invoked by the layout manager)
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
//        // Create a new view, which defines the UI of the list item
//        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(viewGroup.getContext())
//                .inflate(R.layout.row_chat, viewGroup, false);
//
//        return new ViewHolder(linearLayout);
//    }
//
//    // Replace the contents of a view (invoked by the layout manager)
//    @Override
//    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
//        ChatData chat = chatDataSet.get(position);
//        viewHolder.chat_nickname.setText(chat.getNickname());
//        viewHolder.chat_msg.setText(chat.getMsg());
//
//    }
//
//    @Override
//    public int getItemCount() {
//        if (chatDataSet == null){
//            return 0;
//        }
//        else{
//            return chatDataSet.size();
//        }
//    }
//
//    public ChatData getChat(int position){
//        if (chatDataSet == null){
//            return null;
//        }
//        else{
//            return chatDataSet.get(position);
//        }
//    }
//
//    public void addChat(ChatData chatData){
//        chatDataSet.add(chatData);
//        notifyItemInserted(chatDataSet.size() - 1);
//    }
//}
