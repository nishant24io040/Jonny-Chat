package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.example.myapplication.adapter.fAdapter;
import com.example.myapplication.model.UserModal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class wholeusers extends AppCompatActivity {

    fAdapter adapter;
    DatabaseReference database;
    ArrayList<UserModal> list;
    FirebaseAuth mAuth;
    RecyclerView allusersRc;
    String puid;
    ImageView bButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wholeusers);

        allusersRc=findViewById(R.id.alluserRc);
        database= FirebaseDatabase.getInstance().getReference().child("user");
        allusersRc.setNestedScrollingEnabled(true);
        allusersRc.setLayoutManager(new LinearLayoutManager(this));
        mAuth = FirebaseAuth.getInstance();
        puid = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
        list = new ArrayList<>();
        adapter = new fAdapter(this,list);
        allusersRc.setAdapter(adapter);
        bButton = findViewById(R.id.bButton);

        bButton.setOnClickListener(view -> {
            Intent intent = new Intent(wholeusers.this , MainActivity.class);
            startActivity(intent);
        });


        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    UserModal user2 = dataSnapshot.getValue(UserModal.class);
                    assert user2 != null;
                    user2.setUid(dataSnapshot.getKey());
                    list.add(user2);
                }
                for (int i=0; i<list.size(); i++){
                    if(list.get(i).getUid().equals(mAuth.getUid())) {
                        list.remove(i);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}