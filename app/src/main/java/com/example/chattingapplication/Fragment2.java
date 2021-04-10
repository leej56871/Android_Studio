package com.example.chattingapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Fragment2 extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private Button btn_addFriend;
    private EditText searchFriend;
    private Button btn_submitFriend;
    private TextView friend_nickname;
    private List<String> friendList = new ArrayList<String>();
    private List<String> userList = new ArrayList<String>();
    private List<String> emailList = new ArrayList<String>();
    private String UID;
    private String myEmail;



    public Fragment2(List<String> list, List<String> list2, String UID, String myEmail){
        this.UID = UID;
        this.myEmail = myEmail;
        List<String> tempList = new ArrayList<String>();
        list.remove("Trigger");
        if (list == null){
            Toast.makeText(getContext(), "Failed to load user list! Please go back and try again!", Toast.LENGTH_LONG);
        }
        String[] temp = list.toString().substring(1, list.toString().length() - 1).split(", |/");
        for (int i = 0; i < temp.length; i++){
            if (i%2 != 0){
                emailList.add(temp[i]);
            }
            else{
                userList.add(temp[i]);
            }
        }
        System.out.println(list2.toString());
        while (list2.contains("Trigger")){
            list2.remove("Trigger");
        }
        this.friendList = list2;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_2, container, false);

        btn_addFriend = view.findViewById(R.id.btn_addFriend);
        recyclerView = view.findViewById(R.id.friend_recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new FriendAdapter(friendList, UID);
        adapter.setHasStableIds(true);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        searchFriend = view.findViewById(R.id.searchFriend);
        btn_submitFriend = view.findViewById(R.id.btn_submitFriend);
        friend_nickname = view.findViewById(R.id.friend_nickname);

        btn_addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.INVISIBLE);
                btn_addFriend.setVisibility(View.INVISIBLE);
                searchFriend.setVisibility(View.VISIBLE);
                btn_submitFriend.setVisibility(View.VISIBLE);

                btn_submitFriend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (searchFriend.getText().toString().equals("") || searchFriend.getText().toString() == null){
                            Toast.makeText(getContext(),"You did not write anything!", Toast.LENGTH_LONG).show();
                        }
                        else if (friendList.contains(searchFriend.getText().toString())){
                            Toast.makeText(getContext(),"That friend is already added!", Toast.LENGTH_LONG).show();
                        }
                        else if (searchFriend.getText().toString().equals(myEmail)){
                            Toast.makeText(getContext(), "You cannot add yourself as friend!", Toast.LENGTH_SHORT).show();
                        }
                        else if (emailList.contains(searchFriend.getText().toString())){
                                ((FriendAdapter) adapter).addFriend(searchFriend.getText().toString());
                                Toast.makeText(getContext(),"Friend added!", Toast.LENGTH_SHORT).show();
                            }
                        else {
                            Toast.makeText(getContext(),"Could not find such user!", Toast.LENGTH_SHORT).show();
                        }
                        searchFriend.setText("");
                        recyclerView.setVisibility(View.VISIBLE);
                        btn_addFriend.setVisibility(View.VISIBLE);
                        searchFriend.setVisibility(View.INVISIBLE);
                        btn_submitFriend.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });
        return view;

    }

}
