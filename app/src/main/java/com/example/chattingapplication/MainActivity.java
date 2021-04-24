package com.example.chattingapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private FirebaseAuth mAuth; // firebase authentication
    private SignInButton btn_login; // Google login button
    private GoogleApiClient googleApiClient; // google api client
    private static final int REQ_SIGN_GOOGLE = 100; // load result of google login
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // Google Sign with Firebase
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

        // firebase authentication
        mAuth = FirebaseAuth.getInstance();
        btn_login = (SignInButton) findViewById(R.id.btn_login);    // Google sign in button
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Rather than manually doing Google login process, create an intent,
                // give necessary information to intent
                // and just get result after Google login is successful
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, REQ_SIGN_GOOGLE);
            }
        });
    }

    // get returned result from google login (called from startActivityForResult)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_SIGN_GOOGLE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()){
                GoogleSignInAccount account = result.getSignInAccount();
                resultLogin(account);
            }
        }
    }

    // after Google login is successful, access to firebase with Google account
    private void resultLogin(GoogleSignInAccount account){
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // when successfully accessed to firebase with Google account
                        if (task.isSuccessful()){
                            Toast.makeText(MainActivity.this,"Login was successful!", Toast.LENGTH_SHORT).show();
                            // Send information to next intent
                            Intent intent = new Intent(getApplicationContext(), MainState.class);   // going to MainState
                            intent.putExtra("email", account.getEmail());   // send email address of user to MainState
                            intent.putExtra("nickname", account.getDisplayName());  // get the displayed name of Google account and send to MainState
                            intent.putExtra("photoURL", String.valueOf(account.getPhotoUrl())); // Change the profile image of google account into form of string and send to MainState
                            startActivity(intent);  // Start MainState
                        }
                        else{
                            // when login is failed
                            Toast.makeText(MainActivity.this,"Login failed!", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }
}