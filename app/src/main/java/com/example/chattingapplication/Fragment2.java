package com.example.chattingapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
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
    private String myNickName;

    public Fragment2(List<String> list, List<String> list2, String UID, String myEmail, String myNickName){
        // list1 is user list from the database and list2 is the friend list from the database
        this.UID = UID;
        this.myEmail = myEmail;
        this.myNickName = myNickName;

        list.remove("Trigger"); // exclude the "Trigger" locating at user list of the database
        if (list == null){
            Toast.makeText(getContext(), "Failed to load user list! Please go back and try again!", Toast.LENGTH_LONG);
        }

        // changed list into string so there will be ',' between every element
        // and since the user's information in user list in database is in format of 'UID/email'
        // split by ',' and '/' to get the email
        String[] temp = list.toString().substring(1, list.toString().length() - 1).split(", |/");
        for (int i = 0; i < temp.length; i++){
            if (i%2 != 0){
                emailList.add(temp[i]); // add email to emailList
            }
            else{
                userList.add(temp[i]);  // add UID to userList
            }
        }

        // for friend list in database, this app keep adds the "Trigger" to the database since unlike
        // user list of the database, it is created inside the app (user list should be manually created with "Trigger")
        // since otherwise the process of adding user and user's friend list in database would not be dynamic but static
        // simply, no matter how many users are here, we need one user list in database, but if user is newly registered,
        // app needs to directly interact with database and add user's friend list in realtime

        // so there are a lot of "Triggers" in every user's friend list, we exclude them
        while (list2.contains("Trigger")){
            list2.remove("Trigger");
        }
        this.friendList = list2;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_2, container, false);

        // we use the recycler viewer to show list of the friends
        // and it needs recycler viewer, layout manager, and adapter
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

        // if user clicked add friend button to add friend
        btn_addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.INVISIBLE);
                btn_addFriend.setVisibility(View.INVISIBLE);

                // place to write and submit email of friend that user wants to add appears
                searchFriend.setVisibility(View.VISIBLE);
                btn_submitFriend.setVisibility(View.VISIBLE);

                btn_submitFriend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // check if user wrote nothing or just blank
                        if (searchFriend.getText().toString().equals("") || searchFriend.getText().toString() == null){
                            Toast.makeText(getContext(),"You did not write anything!", Toast.LENGTH_LONG).show();
                        }
                        // check if user has already added that email as the friend
                        else if (friendList.contains(searchFriend.getText().toString())){
                            Toast.makeText(getContext(),"That friend is already added!", Toast.LENGTH_LONG).show();
                        }
                        // check if user wrote his or her own email
                        else if (searchFriend.getText().toString().equals(myEmail)){
                            Toast.makeText(getContext(), "You cannot add yourself as friend!", Toast.LENGTH_SHORT).show();
                        }
                        // check if the email of friend is existing in the user list of the app (check whether friend is current user of app)
                        else if (emailList.contains(searchFriend.getText().toString())){
                            // use the adapter to add the friend
                            ((FriendAdapter) adapter).addFriend(searchFriend.getText().toString());
                            Toast.makeText(getContext(),"Friend added!", Toast.LENGTH_SHORT).show();
                        }
                        // else then it means that the friend never used the app or not existing in the user list
                        else {
                            Toast.makeText(getContext(),"Could not find such user!", Toast.LENGTH_SHORT).show();
                        }
                        searchFriend.setText("");   // make the text writing place into blank after clicking submit button
                        recyclerView.setVisibility(View.VISIBLE);
                        btn_addFriend.setVisibility(View.VISIBLE);

                        // adding friend section disappears
                        searchFriend.setVisibility(View.INVISIBLE);
                        btn_submitFriend.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });
        return view;

    }

    public List<String> getFriendList(){
        return friendList;
    }
}
