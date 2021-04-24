package com.example.chattingapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class Fragment3 extends Fragment implements RecyclerViewClickInterface {
    private View view;
    private List<String> roomList = new ArrayList<String>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private String myEmail;
    private String myNickName;
    private List<String> tempList = new ArrayList<String>();
    private List<String> emailList = new ArrayList<String>();

    public Fragment3(List<String> list, String myEmail, String myNickName){
        this.myEmail = myEmail;
        this.myNickName = myNickName;
        this.roomList = list;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_3, container, false);

        // we again use recycler view for showing the list of the chats
        recyclerView = (RecyclerView) view.findViewById(R.id.chatRoom_recycler);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ChatRoomAdapter(roomList, myEmail, myNickName, getContext(), this);
        adapter.setHasStableIds(true);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        return view;


    }

    @Override
    public void onItemClick(int position) {
        // if the user has clicked the chat in the recycler view to start the chat
        // send the required information to ChatActivity and start ChatActivity
        Intent intent = new Intent(getContext(), ChatActivity.class);
        intent.putExtra("myNickName", myNickName);
        intent.putExtra("myEmail", myEmail);
        intent.putExtra("friend", roomList.get(position));
        startActivity(intent);
    }
}
