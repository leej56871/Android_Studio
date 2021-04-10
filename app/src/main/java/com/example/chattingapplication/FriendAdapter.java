package com.example.chattingapplication;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {
    private List<String> friendList;
    private String UID;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView friend_nickName;

        public ViewHolder(View view) {
            super(view);
            friend_nickName = (TextView) view.findViewById(R.id.friend_nickname);
        }
    }

    public FriendAdapter(List<String> friendList, String UID) {
        this.UID = UID;
        this.friendList = friendList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_friend, viewGroup, false);
        return new ViewHolder(linearLayout);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        System.out.println(position);
        String friend = friendList.get(position);
        viewHolder.friend_nickName.setText(friend);
    }

    @Override
    public int getItemCount() {
        if (friendList == null){
            return 0;
        }
        else{
            return friendList.size();
        }
    }

    public List<String> getFriendList(){
        if (friendList == null){
            return null;
        }
        else{
            return friendList;
        }
    }

    public void addFriend(String friend){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(UID + "_friends");
        myRef.push().setValue(friend);
        friendList.add(friend);
        notifyItemInserted(friendList.size() - 1);
    }
}
