package com.example.chattingapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<ChatData> chatList;
    private String myNickName;
    private EditText edit_msg;
    private ImageButton btn_send;
    private DatabaseReference myRef;
    private String roomNumber;

    public ChatActivity(String roomNumber){
        this.roomNumber = roomNumber;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Intent intent_fromMainState = getIntent();
        this.myNickName = intent_fromMainState.getStringExtra("myNickName");

        btn_send = findViewById(R.id.btn_send);
        edit_msg = findViewById(R.id.edit_msg);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = edit_msg.getText().toString();
                if (msg != null) {
                    ChatData chatData = new ChatData();
                    chatData.setNickname(ChatActivity.this.myNickName);
                    chatData.setMsg(msg);
                    myRef.push().setValue(chatData);
                    edit_msg.setText("");
                }

            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        chatList = new ArrayList<>();
        adapter = new ChatAdapter(chatList, ChatActivity.this, this.myNickName);
        recyclerView.setAdapter(adapter);

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference(this.roomNumber);

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ChatData chat = snapshot.getValue(ChatData.class);
                ((ChatAdapter) adapter).addChat(chat);
                recyclerView.scrollToPosition(adapter.getItemCount() - 1);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}