package com.example.chattingapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

public class Fragment1 extends Fragment {
    private View view;
    private ImageButton btn_changeNickName;
    private TextView profile_nickName;
    private EditText edit_changeNickName;
    private Button btn_saveNickName;
    private ImageView profile_image;
    private String changedNickName;
    private String originalName;
    private String myNickName;
    private boolean isChanged;
    private String shared = "file";
    private String photoURL;


    public Fragment1(String originalName, String myNickName, String photoURL) {
        this.originalName = originalName;
        this.myNickName = myNickName;
        this.photoURL = photoURL;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_1, container, false);

        btn_changeNickName = view.findViewById(R.id.btn_changeNickName);
        profile_nickName = view.findViewById(R.id.profile_nickName);
        edit_changeNickName = view.findViewById(R.id.edit_changeNickName);
        btn_saveNickName = view.findViewById(R.id.btn_saveNickName);
        profile_image = view.findViewById(R.id.profile_image);

        Glide.with(this).load(photoURL).into(profile_image);    // set same image of Google account that user uses as profile image

        // if the user is not using the displayed name of Google account (changed the nickname)
        if (originalName.equals(myNickName)){
            profile_nickName.setText(originalName);
        }
        else{
            profile_nickName.setText(myNickName);
        }

        // changing the nickname
        btn_changeNickName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_changeNickName.setVisibility(View.VISIBLE);
                btn_saveNickName.setVisibility(View.VISIBLE);
                btn_saveNickName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // cannot set nickname as blank
                        if (edit_changeNickName.getText().toString().equals("") || edit_changeNickName.getText().toString().replaceAll(" ", "").equals("")){
                            Toast.makeText(getContext(), "You cannot set your nickname as blank or space!", Toast.LENGTH_SHORT).show();
                            edit_changeNickName.setText("");
                            edit_changeNickName.setVisibility(View.INVISIBLE);
                            btn_saveNickName.setVisibility(View.INVISIBLE);
                        }

                        // changed and for update process user needs to login again, automatically moves to MainActivity for the login process
                        else{
                            Toast.makeText(getContext(), "Successfully changed, please login again!", Toast.LENGTH_SHORT).show();
                            changedNickName = edit_changeNickName.getText().toString();
                            profile_nickName.setText(changedNickName);
                            isChanged = true;
                            edit_changeNickName.setText("");
                            edit_changeNickName.setVisibility(View.INVISIBLE);
                            btn_saveNickName.setVisibility(View.INVISIBLE);

                            // saved the changed nickname as sharedPreference which is saved in the smartphone
                            // so when user restarts the application, still the changed nickname is maintained
                            SharedPreferences sharedPreferences = getContext().getSharedPreferences(shared, 0);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("changedNickName", changedNickName);
                            editor.putBoolean("isChanged", isChanged);
                            editor.commit();

                            Intent intent = new Intent(getContext(), MainActivity.class);
                            startActivity(intent);
                        }
                    }
                });
            }
        });

        return view;
    }
}
