package com.example.chattingapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<ChatData> chatList;
    private String myNickName;
    private EditText edit_msg;
    private ImageButton btn_send;
    private ImageButton btn_add;
    private DatabaseReference myRef;
    private String myEmail;
    private String friendEmail;
    private String roomNumber;
    private String fileChecker;
    private Uri fileUri;
    private StorageTask uploadTask;
    private String myUrl = " ";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK && data != null && data.getData() != null){
            fileUri = data.getData();
            if (fileChecker.equals("pdf")){
                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("PDF Files");
                StorageReference filePath = storageReference.child(this.roomNumber + adapter.getItemCount());
//                filePath.putFile(fileUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                        if (task.isSuccessful()){
//                            myUrl = task.getResult().getMetadata().getReference().getDownloadUrl().toString();
//
//                            ChatData chatData = new ChatData();
//                            chatData.setNickname(ChatActivity.this.myNickName);
//                            chatData.setEmail(ChatActivity.this.myEmail);
//                            chatData.setFormat("pdf");
//                            chatData.setUrl(myUrl);
//                            chatData.setMsg(roomNumber + adapter.getItemCount());
//
//                            //chatData.setMsg(filePath.toString());
//                            //chatData.setUrl(fileUri.toString());
//
//                            myRef.push().setValue(chatData);
//                        }
//                    }
//                });

                uploadTask = filePath.putFile(fileUri);
                uploadTask.continueWithTask(new Continuation() {
                    @Override
                    public Object then(@NonNull Task task) throws Exception {
                        if (!task.isSuccessful()){
                            throw task.getException();
                        }
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){
                            Uri downloadUrl = task.getResult();
                            myUrl = downloadUrl.toString();

                            ChatData chatData = new ChatData();
                            chatData.setNickname(ChatActivity.this.myNickName);
                            chatData.setEmail(ChatActivity.this.myEmail);
                            chatData.setFormat("pdf");
                            chatData.setUrl(myUrl);
                            chatData.setMsg(roomNumber + adapter.getItemCount());
                            myRef.push().setValue(chatData);
                        }
                    }
                });

            }
            else if (fileChecker.equals("image")){
                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Image Files");
                StorageReference filePath = storageReference.child(this.roomNumber + adapter.getItemCount());
                uploadTask = filePath.putFile(fileUri);
                uploadTask.continueWithTask(new Continuation() {
                    @Override
                    public Object then(@NonNull Task task) throws Exception {
                        if (!task.isSuccessful()){
                            throw task.getException();
                        }
                        return filePath.getDownloadUrl();

                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){
                            Uri downloadUrl = task.getResult();
                            myUrl = downloadUrl.toString();

                            ChatData chatData = new ChatData();
                            chatData.setNickname(ChatActivity.this.myNickName);
                            chatData.setEmail(ChatActivity.this.myEmail);
                            chatData.setFormat("image");
                            chatData.setUrl(myUrl);
                            myRef.push().setValue(chatData);    // send the message to database
                        }


                    }
                });
            }
            else{
                Toast.makeText(this, "Nothing Selected!", Toast.LENGTH_SHORT);
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // get information flew from Fragment3
        Intent intent_fromFragment3 = getIntent();
        this.myNickName = intent_fromFragment3.getStringExtra("myNickName");
        this.myEmail = intent_fromFragment3.getStringExtra("myEmail");
        this.friendEmail = intent_fromFragment3.getStringExtra("friend");

        // first to create the database that contains the chat between the user and the friend,
        // we need both users to have same key that let them access to the same database
        // that key will be made by putting both user's email in the list, sort them so
        // both user wil have same order, then take the string of email before the '@' since the
        // reference cannot contain special words like '@', ',' ...
        // we call that reference as roomNumber here
        List<String> list = new ArrayList<String>();
        list.add(myEmail.split("\\.|@")[0]);
        list.add(friendEmail.split("\\.|@")[0]);
        Collections.sort(list); // sort
        String[] temp = list.toString().substring(1, list.toString().length()-1).split(", ");
        roomNumber = temp[0] + temp[1]; // make roomNumber

        btn_send = findViewById(R.id.btn_send);
        edit_msg = findViewById(R.id.edit_msg);
        btn_add = findViewById(R.id.btn_add);

        // after writing the message, we press the send button to send the message
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = edit_msg.getText().toString();
                if (msg != null) {
                    // the message that user sends will be stored into the format of
                    // ChatData which is the Java file that I have made
                    // ChatData contains 3 strings, message (msg), nickname of the user (myNickName)
                    // and email of the user (myEmail)
                    // message and nickname would be displayed in the chat room but email is for
                    // recognizing who have sent the message (since both user and the friend can change their
                    // nickname same)

                    ChatData chatData = new ChatData();
                    chatData.setNickname(ChatActivity.this.myNickName);
                    chatData.setMsg(msg);
                    chatData.setEmail(ChatActivity.this.myEmail);
                    chatData.setFormat("text");
                    chatData.setUrl(" ");
                    myRef.push().setValue(chatData);    // send the message to database
                    edit_msg.setText("");   // after sending message, clear the typing box
                }

            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence options[] = new CharSequence[] {
                        "Images",
                        "PDF Files",
                        "MS Word File"

                };

                AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
                builder.setTitle("Select the File");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0){
                            fileChecker = "image";
                            Intent intent = new Intent();
                            intent.setAction(intent.ACTION_GET_CONTENT);
                            intent.setType("image/*");
                            startActivityForResult(Intent.createChooser(intent, "Select Image"), 0);
                        }
                        else if (which == 1){
                            fileChecker = "pdf";
                            Intent intent = new Intent();
                            intent.setAction(intent.ACTION_GET_CONTENT);
                            intent.setType("application/pdf");
                            startActivityForResult(Intent.createChooser(intent, "Select PDF file"), 0);
                        }
                        else if (which == 2){
                            fileChecker = "docx";
                            Intent intent = new Intent();
                            intent.setAction(intent.ACTION_GET_CONTENT);
                            intent.setType("application/msword*");
                            startActivityForResult(Intent.createChooser(intent, "Select MS Word file"), 0);
                        }
                    }
                });
                builder.show();
            }
        });

        // ChatActivity also uses the recycler viewer to show the chats in list
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        chatList = new ArrayList<>();
        adapter = new ChatAdapter(chatList, this.myNickName, this.myEmail);
        recyclerView.setAdapter(adapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference(this.roomNumber);

        // if database is changed (whether user has sent the message of the friend has sent the message)
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // get the value of newly sent message and use adapter to show in the recycler viewer
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