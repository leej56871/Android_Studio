package com.example.chattingapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_menu);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_menu);
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

        Intent intent = getIntent();
        list = intent.getStringArrayListExtra("list");
        UID = intent.getStringExtra("UID");
        list2 = intent.getStringArrayListExtra("friendList");
        myEmail = intent.getStringExtra("email");


        fragment1 = new Fragment1();
        fragment2 = new Fragment2(list, list2, UID, myEmail);
        fragment3 = new Fragment3();
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