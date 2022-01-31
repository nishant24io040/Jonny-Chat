package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class luncherScreen extends AppCompatActivity {
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luncher_screen);
        imageView = findViewById(R.id.luncher);
        Glide.with(luncherScreen.this).load(R.drawable.luncher).centerCrop().into(imageView);
        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(2500);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                finally {
                    Intent intent = new Intent(luncherScreen.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        };
       thread.start();
    }
}