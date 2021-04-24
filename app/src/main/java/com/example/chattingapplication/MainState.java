package com.example.chattingapplication;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainState extends AppCompatActivity {
    private static final String TAG = "MainState";
    private TextView view_nickname;
    private ImageView view_icon_profile;
    private Button btn_start;
    private DatabaseReference myRef;
    private DatabaseReference friendRef;
    private FirebaseAuth mAuth;
    private List<String> userList = new ArrayList<>();
    private List<String> temp_list;
    private List<String> list;
    private List<String> friendList = new ArrayList<>();
    private List<String> list2;
    private String shared = "file";
    private String myNickName;
    private String originalName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_state);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // access to firebase
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("user_list"); // get the list of all users using this application
        // get the list of all users using this application only once when this state is created
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.add(snapshot.getValue().toString());   // add into userList
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        friendRef = database.getReference(mAuth.getUid() + "_friends"); // friend list of user

        // if user is first-time using application ever, we cannot access to empty database
        // so add message "Trigger" to prevent the error
        friendRef.push().setValue("Trigger");
        friendRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                friendList.add(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        view_nickname = (TextView)findViewById(R.id.nickname);
        Intent intent = getIntent();
        // get information flew from MainActivity
        String email = intent.getStringExtra("email");
        myNickName = intent.getStringExtra("nickname");
        String icon_profile = intent.getStringExtra("photoURL");

        // if the user's nickname has changed in Fragment1, change to the nickname that user has set
        SharedPreferences sharedPreferences = getSharedPreferences(shared, 0);
        boolean isChanged = sharedPreferences.getBoolean("isChanged",false);
        String changedNickName = sharedPreferences.getString("changedNickName", "");
        originalName = myNickName;

        // change only if user ever changed
        if (isChanged == true){
            myNickName = changedNickName;
        }

        view_nickname.append(" " + myNickName);
        view_icon_profile = (ImageView)findViewById(R.id.icon_profile);

        // set image as profile photo of user's Google account
        Glide.with(this).load(icon_profile).into(view_icon_profile);

        // pressing start button checks if  user is first-time using app
        // if yes, register the user to the database
        btn_start = (Button) findViewById(R.id.btn_start);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userList.add(snapshot.getValue().toString());

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                // if user has pressed the start button even before program read the user list in database
                // catch the out of bound exception if there is no one ever used the app (so user list is null)
                try{
                    temp_list = new ArrayList<String>(Arrays.asList(userList.toString().substring(2, userList.toString().length() - 2).split(", |=")));
                    list = new ArrayList<String>();
                    for (int i = 0; i < temp_list.size(); i++){
                        if (i % 2 != 0 ){
                            list.add(temp_list.get(i));
                        }
                    }
                }
                catch (StringIndexOutOfBoundsException e){
                    Toast.makeText(MainState.this, "Error has occurred! Try again!", Toast.LENGTH_LONG);
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
                list.remove("Trigger"); // There is Trigger at the top of the user list in database to prevent error happening by accessing empty database so exclude

                // get the friend list from the database and add friends into list2
                temp_list = new ArrayList<String>(Arrays.asList(friendList.toString().substring(2, friendList.toString().length() - 2).split(", |=")));
                list2 = new ArrayList<String>();
                for (int i = 0; i < temp_list.size(); i++){
                    if (i % 2 != 0 ){
                        list2.add(temp_list.get(i));
                    }
                }

                // user's information in user list of database is in form of 'UID/email' so to get user's email, split by '/'
                if (list != null && list.contains(mAuth.getUid() + "/" + email)){   // if user is already in the database
                    Toast.makeText(MainState.this, "Welcome back!", Toast.LENGTH_SHORT);
                }
                // if not, register the user to database
                else {
                    String id = mAuth.getUid().toString() + "/" +  email;
                    myRef.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            userList.add(snapshot.getValue().toString());
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
                    myRef.push().setValue(id);
                }

                // give information to BottomMenu
                Intent intent_start = new Intent(MainState.this, BottomMenu.class);
                intent_start.putExtra("originalName", originalName);
                intent_start.putExtra("myNickName", myNickName);
                intent_start.putExtra("email", email);
                intent_start.putStringArrayListExtra("list", (ArrayList<String>) list);
                intent_start.putExtra("UID", mAuth.getUid().toString());
                intent_start.putStringArrayListExtra("friendList", (ArrayList<String>) list2);
                intent_start.putExtra("photoURL", icon_profile);
                startActivity(intent_start);    // start BottomMenu
            }
        });
    }

}