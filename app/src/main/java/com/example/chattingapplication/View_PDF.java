package com.example.chattingapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class View_PDF extends AppCompatActivity {
    private String url = " ";
    private String file_name = " ";
    private PDFView pdfView;

    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pdf);

        pdfView = findViewById(R.id.pdf_view);

        Intent intent = getIntent();
        file_name = intent.getStringExtra("file_name");
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("PDF Files");
        StorageReference filePath = storageReference.child(file_name);

        try {
            File localFile = File.createTempFile(file_name, "pdf");
            filePath.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    System.out.println("Successful!");
                    pdfView.fromFile(localFile).load();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    System.out.println("Got error for opening the PDF file!");
                    Toast.makeText(View_PDF.this, "Got error for opening the PDF file!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            System.out.println("Got error for downloading the PDF file!");
            Toast.makeText(this, "Got error for downloading the PDF file!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}