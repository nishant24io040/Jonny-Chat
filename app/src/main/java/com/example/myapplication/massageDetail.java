package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.adapter.chatAdapter;
import com.example.myapplication.model.MassageModal;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class massageDetail extends AppCompatActivity {

    FirebaseDatabase database;
    FirebaseAuth mAuth;
    ImageView previousact;
    ImageView Profile,send;
    TextView UserName;
    EditText masgText;
    RecyclerView chatRecycler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_massage_detail);

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        previousact = findViewById(R.id.preactbutton);
        UserName = findViewById(R.id.users_name);
        Profile = findViewById(R.id.profileviewer);
        send = findViewById(R.id.sendButton);
        chatRecycler = findViewById(R.id.Chatrecyclerview);
        masgText = findViewById(R.id.masgtext);

        final String senderId = mAuth.getUid();
        String receverId = getIntent().getStringExtra("userid");
        String Propic = getIntent().getStringExtra("dp");
        String NameId = getIntent().getStringExtra("name");

        Glide.with(massageDetail.this).load(Propic).circleCrop().into(Profile);
        UserName.setText(NameId);

        previousact.setOnClickListener(view -> {
            Intent intent = new Intent(massageDetail.this,recyclerActivity.class);
            startActivity(intent);
        });

        final ArrayList<MassageModal> massageModals1 = new ArrayList<>();

        final chatAdapter chattadapter = new chatAdapter(this, massageModals1);
        chatRecycler.setAdapter(chattadapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        chatRecycler.setLayoutManager(layoutManager);

        final String senderRoom = senderId+receverId;
        final String receverRoom = receverId+senderId;

        database.getReference().child("Chats").child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        massageModals1.clear();
                        for(DataSnapshot snapshot1 : snapshot.getChildren()){

                            MassageModal modal = snapshot1.getValue(MassageModal.class);
                            massageModals1.add(modal);
                        }
                        chattadapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        send.setOnClickListener(view ->{
            String massage = masgText.getText().toString();
            MassageModal massagemodal = new MassageModal(senderId,massage);
            massagemodal.setTimeStamp(new Date().getTime());
            masgText.setText("");
            database.getReference().child("Chats").child(senderRoom)
                    .push()
                    .setValue(massagemodal).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    database.getReference().child("Chats").child(receverRoom)
                            .push()
                            .setValue(massagemodal).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    });
                }
            });
        });



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menuforchat,menu);
        return super.onCreateOptionsMenu(menu);
    }

}