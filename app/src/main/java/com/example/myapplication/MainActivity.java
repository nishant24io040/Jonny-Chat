package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.model.UserModal;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    Button getLogin;
    private FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Objects.requireNonNull(getSupportActionBar()).
                hide();




        getLogin = findViewById(R.id.glogin);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        if (mAuth.getCurrentUser()!=null){
            Intent intent = new Intent(MainActivity.this , recyclerActivity.class);
            startActivity(intent);
        }

        getLogin.setOnClickListener(view -> signIn());
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

//        textView1.setOnClickListener(view -> {
//            Intent intent = new Intent( getApplicationContext() , registerActivity2.class);
//            startActivity(intent);
//        });

//        Spassword.setOnCheckedChangeListener((compoundButton, b) -> {
//            if (b){
//                password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
//            }
//            else {
//                password.setTransformationMethod(PasswordTransformationMethod.getInstance());
//            }
//        });

//        login.setOnClickListener(view -> {
//            progressBar.setVisibility(View.VISIBLE);
//            String Email=email.getText().toString();
//            String Password = password.getText().toString();
//            if(TextUtils.isEmpty(Email)){
//                email.setError("Email is Required");
//                progressBar.setVisibility(View.INVISIBLE);
//                return;
//            }
//            if(Password.length()<6){
//                password.setError("Password is Required");
//                progressBar.setVisibility(View.INVISIBLE);
//                return;
//            }
//            mAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(MainActivity.this, task -> {
//                if (task.isSuccessful()) {
//                    progressBar.setVisibility(View.INVISIBLE);
//                    email.setText("");
//                    password.setText("");
//                    Intent intent2 = new Intent(getApplicationContext() , recyclerActivity.class);
//                    startActivity(intent2);
//                }
//                else {
//                    progressBar.setVisibility(View.INVISIBLE);
//                    email.setText("");
//                    password.setText("");
//                    Toast.makeText(getApplicationContext(), ""+task.getException(), Toast.LENGTH_SHORT).show();
//                }
//            });

//        });
    }
    int RC_SIGN_IN = 65;
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
//                Log.d("TAG", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(), "Error in catch"+task.getException(), Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();

                        UserModal users = new UserModal();

//                        users.setUid(user.getUid());
//                        users.setEmail(user.getEmail());
//                        database.getReference().child("user").child(users.getUid()).setValue(user.);

                        Intent intent = new Intent(MainActivity.this , recyclerActivity.class);
                        startActivity(intent);

                    } else {
                        Toast.makeText(getApplicationContext(), "error in else", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
