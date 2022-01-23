package com.example.myapplication;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.myapplication.model.UserModal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.Calendar;
import java.util.Objects;


public class registerActivity2 extends AppCompatActivity {
    TextView email,password,fname,phone;
    Button signup,Browse;
    ProgressBar progress;
    CheckBox Spassword;
    ImageView profile;
    Uri filepath;


    private FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        profile= findViewById(R.id.imageView41);
        Browse = findViewById(R.id.Browse2);
        email = findViewById(R.id.editTextTextEmailAddress3);
        password = findViewById(R.id.editTextTextPassword3);
        fname = findViewById(R.id.editTextTextPersonName2);
        phone = findViewById(R.id.editTextPhone2);
        signup =  findViewById(R.id.button2);
        progress = findViewById(R.id.progressBar);
        Spassword = findViewById(R.id.checkBox2);


        TextView regis= findViewById(R.id.login5);
        regis.setOnClickListener(view -> {
            Intent intent1 = new Intent( getApplicationContext() , MainActivity.class);
            startActivity(intent1);
        });

        Spassword.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else {
                password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });


        Browse.setOnClickListener(view -> {
            Dexter.withActivity(registerActivity2.this)
                    .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                            Intent intent=new Intent(Intent.ACTION_PICK);
                            intent.setType("image/*");
                            startActivityForResult(Intent.createChooser(intent,"Select Image File"),1);
                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                            permissionToken.continuePermissionRequest();
                        }
                    }).check();
        });

        signup.setOnClickListener(view -> {
            progress.setVisibility(View.VISIBLE);
            String Email = email.getText().toString();
            String Password = password.getText().toString();
            String fullname = fname.getText().toString();
            String Phone = phone.getText().toString();


            if(TextUtils.isEmpty(Email)){
                email.setError("Email is Required");
                progress.setVisibility(View.INVISIBLE);
                return;
            }
            if(Password.length()<6){
                password.setError("Password is Required more then 6 character");
                progress.setVisibility(View.INVISIBLE);
                return;
            }


            mAuth = FirebaseAuth.getInstance();
            mAuth.createUserWithEmailAndPassword(Email,Password)
                    .addOnCompleteListener(registerActivity2.this, task -> {
                        if (task.isSuccessful()) {
                            progress.setVisibility(View.INVISIBLE);
                            ProgressDialog progressDialog = new ProgressDialog(this);
                            progressDialog.setTitle("File Uploading");
                            progressDialog.show();
                            final StorageReference uploader = storage.getReference("image1"+ str);
                            uploader.putFile(filepath).addOnSuccessListener(taskSnapshot -> {
                                progressDialog.dismiss();
                                uploader.getDownloadUrl().addOnSuccessListener(uri -> {
                                    String url = uri.toString();
                                    String Uid = Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getUser()).getUid();
                                    UserModal account = new UserModal(fullname,Phone,Password,Email,url);
                                    database.getReference().child("user").child(Uid).setValue(account);
                                });
                            })
                                    .addOnProgressListener(snapshot -> {
                                        long precent = (100*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                                        progressDialog.setMessage("Uploaded"+(int)precent+"%");
                                    });

                            email.setText("");
                            password.setText("");
                            phone.setText("");
                            fname.setText("");
                            Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_SHORT).show();


                        }
                        else {
                            progress.setVisibility(View.INVISIBLE);
                            email.setText("");
                            password.setText("");
                            Toast.makeText(getApplicationContext(), ""+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    });

        });

    }
    Calendar c = Calendar.getInstance();
    String str = c.getTime().toString();



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==1 && resultCode==RESULT_OK){
            filepath=data.getData();
            try {
                Glide.with(this).load(filepath).circleCrop().into(profile);

            }
            catch (Exception ex) {
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}