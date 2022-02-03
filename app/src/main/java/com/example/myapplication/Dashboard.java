package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.model.UserModal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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

public class Dashboard extends AppCompatActivity {
    TextView name;
    FirebaseAuth mAuth;
    ImageView img;
    Button button,savechanges;
    Uri url;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();

    @SuppressLint({"SetTextI18n", "WrongConstant"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        mAuth = FirebaseAuth.getInstance();
        name = findViewById(R.id.fullname);
        img = findViewById(R.id.imageView25);
        button = findViewById(R.id.button3);
        savechanges = findViewById(R.id.button4);
        Glide.with(Dashboard.this).load(R.drawable.jumper).circleCrop().into(img);
        Toast.makeText(this, "Upload Your Image", Toast.LENGTH_SHORT).show();

//        if(img.getVisibility() == 0){
//
//            Toast.makeText(getApplicationContext(), "Mission Successful", Toast.LENGTH_SHORT).show();
//            FirebaseUser user = mAuth.getCurrentUser();
//
//            UserModal users = new UserModal();
//            users.setUid(user.getUid());
//            users.setName(user.getDisplayName());
//            users.setPropic(Objects.requireNonNull(user.getPhotoUrl()).toString());
//            users.setEmail(user.getEmail());
//            database.getReference().child("user").child(users.getUid()).setValue(users);
//        }

        name.setText(mAuth.getCurrentUser().getDisplayName());
        database.getReference().child("user").child(mAuth.getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            UserModal userModal = snapshot.getValue(UserModal.class);
                            if(userModal.getPropic()==null){
                                Glide.with(Dashboard.this).load(mAuth.getCurrentUser().getPhotoUrl()).circleCrop().into(img);
                            }
                            else{
                                Glide.with(Dashboard.this).load(userModal.getPropic()).circleCrop().into(img);

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        button.setOnClickListener(view -> {
            Dexter.withActivity(Dashboard.this)
                    .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                            Intent intent=new Intent(Intent.ACTION_PICK);
                            intent.setType("image/*");
                            startActivityForResult(Intent.createChooser(intent,"Upload DP"),2);
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
        savechanges.setOnClickListener(view -> {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("File Uploading");
            progressDialog.show();
            final StorageReference uploader = storage.getReference("image2"+ mAuth.getCurrentUser().getUid());
            uploader.putFile(url).addOnSuccessListener(taskSnapshot -> {
                progressDialog.dismiss();
                uploader.getDownloadUrl().addOnSuccessListener(uri -> {
                    String url = uri.toString();
                    String fullname = mAuth.getCurrentUser().getDisplayName();
                    String Email = mAuth.getCurrentUser().getEmail();
                    String Uid = Objects.requireNonNull(mAuth.getCurrentUser().getUid());
                    UserModal account = new UserModal(fullname,Email,url,Uid);
                    database.getReference().child("user").child(Uid).setValue(account);
                });
            })
                    .addOnProgressListener(snapshot -> {
                        long precent = (100*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                        progressDialog.setMessage("Uploaded"+(int)precent+"%");
                    });
            savechanges.setVisibility(View.INVISIBLE);
            Intent intent = new Intent(Dashboard.this,recyclerActivity.class);
            Toast.makeText(this, "First Add Friends to your Chat from Find Friends", Toast.LENGTH_LONG).show();
            startActivity(intent);
        });


    }
    Calendar c = Calendar.getInstance();
    String str = c.getTime().toString();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==2 && resultCode==RESULT_OK){
            assert data != null;
            savechanges.setVisibility(View.VISIBLE);
            url=data.getData();
            try {
                Glide.with(this).load(url).circleCrop().into(img);


            }
            catch (Exception ignored) {
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}