package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapter.Adapter;
import com.example.myapplication.model.UserModal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;


public class ScrollingActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Adapter adapter;
    DatabaseReference database;
    ArrayList<UserModal> list;
    FirebaseAuth mAuth;
    String puid;
    Toolbar toolbar;



    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

//        toolbar=findViewById(R.id.appbar);
//        setSupportActionBar(toolbar);
//        ActionBar actionBar = getSupportActionBar();
//        assert actionBar != null;
//        actionBar.setDisplayHomeAsUpEnabled(false);

        recyclerView = findViewById(R.id.rcyview2);
        database=FirebaseDatabase.getInstance().getReference().child("user");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAuth = FirebaseAuth.getInstance();
        puid = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
        list = new ArrayList<>();
        adapter = new Adapter(this,list);
        recyclerView.setAdapter(adapter);

       database.addValueEventListener(new ValueEventListener() {
           @SuppressLint("NotifyDataSetChanged")
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                   UserModal user2 = dataSnapshot.getValue(UserModal.class);
                   assert user2 != null;
                   user2.setUid(dataSnapshot.getKey());
                   list.add(user2);
               }
               for (int i=0; i<list.size(); i++){
                   if(list.get(i).getEmail().equals(puid)) {
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
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.manu,menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @SuppressLint("NonConstantResourceId")
//    @Override
//
//
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch(item.getItemId()){
//            case R.id.profile:
//                Toast.makeText(this, "Your Email is "+puid, Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(ScrollingActivity.this , Dashboard.class);
//                startActivity(intent);
//                break;
//
//            case R.id.setting:
//                Toast.makeText(this, "Settings is open", Toast.LENGTH_SHORT).show();
//                break;
//
//            case R.id.LogOut:
//                mAuth.signOut();
//                Toast.makeText(this, "LogOut successfully", Toast.LENGTH_SHORT).show();
//
//                Intent intent1 = new Intent(ScrollingActivity.this , MainActivity.class);
//                startActivity(intent1);
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }
    }