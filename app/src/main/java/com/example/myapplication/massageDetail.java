package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
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

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.myapplication.adapter.chatAdapter;
import com.example.myapplication.model.MassageModal;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class massageDetail extends AppCompatActivity {

    FirebaseDatabase database;
    FirebaseAuth mAuth;
    ImageView previousact;
    ImageView Profile,send;
    TextView UserName;
    EditText masgText;
    RecyclerView chatRecycler;


    @RequiresApi(api = Build.VERSION_CODES.N)
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
        String title = getIntent().getStringExtra("title");
        String Token = getIntent().getStringExtra("token");

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
        layoutManager.setStackFromEnd(true);
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
                            chatRecycler.smoothScrollToPosition(massageModals1.size()-1);
                        }
                        chattadapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        send.setOnClickListener(view ->{

            FirebaseDatabase.getInstance().getReference().child("user")
                    .child(mAuth.getUid()).child("uId").child(receverId).setValue(receverId);


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
                            if (!title.equals("null")){
                            sendNotification(title,massagemodal.getMassage(),Token);
                            }
                            else {
                                sendNotification(mAuth.getCurrentUser().getDisplayName(),massagemodal.getMassage(),Token);
                            }
                        }
                    });
                }
            });
        });

    }

    public void sendNotification(String name,String massage,String token){
        try {
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "https://fcm.googleapis.com/fcm/send";

            JSONObject object = new JSONObject();
            object.put("title",name);
            object.put("body",massage);

            JSONObject notificationData = new JSONObject();
            notificationData.put("notification",object);
            notificationData.put("to",token);

            JsonObjectRequest request = new JsonObjectRequest(url, notificationData,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(massageDetail.this, "congrats", Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(massageDetail.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String,String> map = new HashMap<>();
                    String key = "Key=AAAAETrq4-4:APA91bHpvdKuO0xfstPwb1I84pugOuPaiTskW1d0Y8xir2MSSvesXzNSX4I6yF6kRFgUuGK4vVfa7tps1UCVUuBD02Sd1WdbvBRvezbhuH-i8Mk4IAN5v9Ij9tJ8wr2cbRWdFDSs_SWq";
                    map.put("Authorization",key);
                    map.put("Content-Type","application/json");
                    return map;
                }
            };
            queue.add(request);
        }
        catch (Exception ex){

        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menuforchat,menu);
        return super.onCreateOptionsMenu(menu);
    }

}