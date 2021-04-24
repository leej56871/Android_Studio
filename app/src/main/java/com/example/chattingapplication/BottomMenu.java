package com.example.chattingapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;

public class BottomMenu extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private List<String> list = new ArrayList<String>(); // user list
    private List<String> list2 = new ArrayList<String>(); // friend list
    private FragmentManager fm;
    private FragmentTransaction ft;
    private Fragment1 fragment1;
    private Fragment2 fragment2;
    private Fragment3 fragment3;
    private String UID;
    private String myEmail;
    private String myNickName;
    private String originalName;
    private String photoURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_menu);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_menu);

        // There are 3 clickable buttons that transits the screen Fragment1, Fragment2 and Fragment3
        // Each of Fragments are for profile, friend list (adding friend), chat list (chatting with friends)
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_profile:
                        setFragment(0);
                        break;
                    case R.id.action_friends:
                        setFragment(1);
                        break;
                    case R.id.action_chats:
                        setFragment(2);
                        break;
                }
                return true;
            }
        });

        // get information from MainState
        Intent intent = getIntent();
        list = intent.getStringArrayListExtra("list");
        UID = intent.getStringExtra("UID");
        list2 = intent.getStringArrayListExtra("friendList");
        myEmail = intent.getStringExtra("email");
        myNickName = intent.getStringExtra("myNickName");
        originalName = intent.getStringExtra("originalName");
        photoURL = intent.getStringExtra("photoURL");

        // define each fragments and set needed information of each fragments as their parameters
        fragment1 = new Fragment1(originalName, myNickName, photoURL);
        fragment2 = new Fragment2(list, list2, UID, myEmail, myNickName);
        fragment3 = new Fragment3(fragment2.getFriendList(), myEmail, myNickName);

        // initial fragment facing after clicking start button of MainState is the first fragment, Fragment1
        setFragment(0);



    }

    private void setFragment(int n){
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        switch (n){
            case 0:
                ft.replace(R.id.main_frame, fragment1);
                ft.commit();
                break;
            case 1:
                ft.replace(R.id.main_frame, fragment2);
                ft.commit();
                break;
            case 2:
                ft.replace(R.id.main_frame, fragment3);
                ft.commit();
                break;
        }
    }
}