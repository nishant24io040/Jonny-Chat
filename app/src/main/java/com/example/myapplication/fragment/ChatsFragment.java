package com.example.myapplication.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.adapter.Adapter;
import com.example.myapplication.databinding.FragmentBlankBinding;
import com.example.myapplication.model.UserModal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class ChatsFragment extends Fragment {

    public ChatsFragment() {
    }
    Adapter adapter;
    DatabaseReference database;
    ArrayList<UserModal> list;
    FirebaseAuth mAuth;
    String puid;
    FragmentBlankBinding binding;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        binding = FragmentBlankBinding.inflate(inflater,container,false);

        database= FirebaseDatabase.getInstance().getReference().child("user");
//        binding.rcview3.setHasFixedSize(true);
        binding.rcview3.setNestedScrollingEnabled(true);
        binding.rcview3.setLayoutManager(new LinearLayoutManager(getContext()));
        mAuth = FirebaseAuth.getInstance();
        puid = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
        list = new ArrayList<>();
        adapter = new Adapter(getContext(),list);
        binding.rcview3.setAdapter(adapter);

        database.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
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

        return binding.getRoot();
    }
}